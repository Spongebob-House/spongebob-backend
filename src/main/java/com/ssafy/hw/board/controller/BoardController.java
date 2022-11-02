package com.ssafy.hw.board.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.hw.board.model.BoardDto;
import com.ssafy.hw.board.model.FileInfoDto;
import com.ssafy.hw.board.model.service.BoardService;
import com.ssafy.hw.member.model.MemberDto;
import com.ssafy.hw.util.PageNavigation;

@Controller
@RequestMapping("/board")
public class BoardController {

	private final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private ServletContext servletContext;

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@GetMapping("/write")
	public String write(@RequestParam Map<String, String> map, Model model) {
		logger.debug("write call parameter {}", map);
		model.addAttribute("pgno", map.get("pgno"));
		model.addAttribute("key", map.get("key"));
		model.addAttribute("word", map.get("word"));
		return "board/write";
	}

	@PostMapping("/write")
	public String write(@Value("${file.path.upload-files}") String filePath, BoardDto boardDto, @RequestParam("upfile") MultipartFile[] files, HttpSession session,
			RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("write boardDto : {}", boardDto);
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		boardDto.setUserId(memberDto.getUserId());

//		FileUpload 관련 설정.
		logger.debug("MultipartFile.isEmpty : {}", files[0].isEmpty());
		if (!files[0].isEmpty()) {
			String realPath = servletContext.getRealPath("/upload");
//			String realPath = servletContext.getRealPath("/resources/img");
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = filePath + File.separator + today;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
			for (MultipartFile mfile : files) {
				FileInfoDto fileInfoDto = new FileInfoDto();
				String originalFileName = mfile.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = System.nanoTime()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					fileInfoDto.setSaveFolder(today);
					fileInfoDto.setOriginalFile(originalFileName);
					fileInfoDto.setSaveFile(saveFileName);
					logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
					mfile.transferTo(new File(folder, saveFileName));
				}
				fileInfos.add(fileInfoDto);
			}
			boardDto.setFileInfos(fileInfos);
		}
		boardService.writeArticle(boardDto);
		redirectAttributes.addAttribute("pgno", "1");
		redirectAttributes.addAttribute("key", "");
		redirectAttributes.addAttribute("word", "");
		return "redirect:/board/list";
	}

	@GetMapping("/list")
	public ModelAndView list(@RequestParam Map<String, String> map) throws Exception {
		logger.debug("list parameter : {}", map);
		ModelAndView mav = new ModelAndView();
//		try {
		List<BoardDto> list = boardService.listArticle(map);
		PageNavigation pageNavigation = boardService.makePageNavigation(map);
		mav.addObject("articles", list);
		mav.addObject("navigation", pageNavigation);
		mav.addObject("pgno", map.get("pgno"));
		mav.addObject("key", map.get("key"));
		mav.addObject("word", map.get("word"));
		mav.setViewName("board/list");
		return mav;
//		} catch (Exception e) {
//			e.printStackTrace();
//			mav.addObject("msg", "글작성 처리중 문제발생!!!");
//			mav.setViewName("error/error");
//			return mav;
//		}
	}

	@GetMapping("/view")
	public String view(@RequestParam("articleno") int articleNo, @RequestParam Map<String, String> map, Model model)
			throws Exception {
		logger.debug("view articleNo : {}", articleNo);
		BoardDto boardDto = boardService.getArticle(articleNo);
		boardService.updateHit(articleNo);
		model.addAttribute("article", boardDto);
		model.addAttribute("pgno", map.get("pgno"));
		model.addAttribute("key", map.get("key"));
		model.addAttribute("word", map.get("word"));
		return "board/view";
	}

	@GetMapping("/modify")
	public String modify(@RequestParam("articleno") int articleNo, @RequestParam Map<String, String> map, Model model)
			throws Exception {
		logger.debug("modify articleNo : {}", articleNo);
		BoardDto boardDto = boardService.getArticle(articleNo);
		model.addAttribute("article", boardDto);
		model.addAttribute("pgno", map.get("pgno"));
		model.addAttribute("key", map.get("key"));
		model.addAttribute("word", map.get("word"));
		return "board/modify";
	}

	@PostMapping("/modify")
	public String modify(BoardDto boardDto, @RequestParam Map<String, String> map,
			RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("modify boardDto : {}", boardDto);
		boardService.modifyArticle(boardDto);
		redirectAttributes.addAttribute("pgno", map.get("pgno"));
		redirectAttributes.addAttribute("key", map.get("key"));
		redirectAttributes.addAttribute("word", map.get("word"));
		return "redirect:/board/list";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("articleno") int articleNo, @RequestParam Map<String, String> map,
			RedirectAttributes redirectAttributes) throws Exception {
		logger.debug("delete articleNo : {}", articleNo);
		boardService.deleteArticle(articleNo, servletContext.getRealPath("/upload"));
		redirectAttributes.addAttribute("pgno", map.get("pgno"));
		redirectAttributes.addAttribute("key", map.get("key"));
		redirectAttributes.addAttribute("word", map.get("word"));
		return "redirect:/board/list";
	}

	@GetMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<Object> downloadFile(@Value("${file.path.upload-files}") String filePath1, @RequestParam("sfolder") String sfolder, @RequestParam("ofile") String ofile,
			@RequestParam("sfile") String sfile, HttpSession session) {
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		if (memberDto != null) {
			Map<String, Object> fileInfo = new HashMap<String, Object>();
			String path = filePath1 + File.separator + sfolder + File.separator + sfile;
			
			try {
				Path filePath = Paths.get(path);
				Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
				
				File file = new File(path);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
				
				return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			} catch(Exception e) {
				return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}

}
