package com.ssafy.hw.board.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.ssafy.hw.board.model.BoardDto;
import com.ssafy.hw.board.model.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/board")
@Api("게시판 컨트롤러  API V1")
public class BoardController {


	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private ServletContext servletContext;

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}




//	@ApiOperation(value = "공지사항 쓰기", notes = "공지사항을 작성한다.", response = List.class)
//	@PostMapping
//	public ResponseEntity<String> write(@Value("${file.path.upload-files}") String filePath, BoardDto boardDto, @RequestParam("upfile") MultipartFile[] files) throws Exception {

//
////		FileUpload 관련 설정.
//		logger.debug("MultipartFile.isEmpty : {}", files[0].isEmpty());
//		if (!files[0].isEmpty()) {
//			String realPath = servletContext.getRealPath("/upload");
////			String realPath = servletContext.getRealPath("/resources/img");
//			String today = new SimpleDateFormat("yyMMdd").format(new Date());
//			String saveFolder = filePath + File.separator + today;
//			logger.debug("저장 폴더 : {}", saveFolder);
//			File folder = new File(saveFolder);
//			if (!folder.exists())
//				folder.mkdirs();
//			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
//			for (MultipartFile mfile : files) {
//				FileInfoDto fileInfoDto = new FileInfoDto();
//				String originalFileName = mfile.getOriginalFilename();
//				if (!originalFileName.isEmpty()) {
//					String saveFileName = System.nanoTime()
//							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
//					fileInfoDto.setSaveFolder(today);
//					fileInfoDto.setOriginalFile(originalFileName);
//					fileInfoDto.setSaveFile(saveFileName);
//					logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
//					mfile.transferTo(new File(folder, saveFileName));
//				}
//				fileInfos.add(fileInfoDto);
//			}
//			boardDto.setFileInfos(fileInfos);

//		}
//		if(boardService.writeArticle(boardDto)) {
//			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
//		}
//		return new ResponseEntity<String>(FAIL, HttpStatus.NOT_MODIFIED);
//	}
	@ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeArticle(@RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto) throws Exception {
		logger.info("writeArticle - 호출");
		if (boardService.writeArticle(boardDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "공지사항 리스트", notes = "공지사항을 리스트를 반환한다.", response = List.class)
	@GetMapping("/list/{pgno}")
	public ResponseEntity<List<BoardDto>> list(@PathVariable("pgno") @ApiParam(value = "페이지 번호") String pgno,
			@RequestParam(value="key", defaultValue="") @ApiParam(value = "검색 카테고리") String key,
			@RequestParam(value="word", defaultValue="") @ApiParam(value = "검색어") String word) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pgno", pgno);
		map.put("key", key == null ? "" : key);
		map.put("word", word == null ? "" : word);
		logger.debug("list parameter : {}", map);
//		try {
		List<BoardDto> list = boardService.listArticle(map);
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<BoardDto>>(list, HttpStatus.OK);

//		} catch (Exception e) {
//			e.printStackTrace();
//			mav.addObject("msg", "글작성 처리중 문제발생!!!");
//			mav.setViewName("error/error");
//			return mav;
//		}
	}

	@ApiOperation(value = "공지사항 글보기", notes = "공지사항 글을 반환한다.", response = BoardDto.class)
	@GetMapping("/view/{articleno}")
	public ResponseEntity<BoardDto> view(@PathVariable("articleno") @ApiParam(value = "글 번호", required = true) int articleNo)
			throws Exception {
		logger.debug("view articleNo : {}", articleNo);
		BoardDto boardDto = boardService.getArticle(articleNo);
		boardService.updateHit(articleNo);
		if(boardDto.getArticleNo() == articleNo) {
			return new ResponseEntity<BoardDto>(boardDto, HttpStatus.OK);
		}
		return new ResponseEntity<BoardDto>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "공지사항 글 수정", notes = "공지사항 글을 수정한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modify(@RequestBody BoardDto boardDto) throws Exception {
		logger.debug("modify boardDto : {}", boardDto);
		if(boardService.modifyArticle(boardDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		};

		return new ResponseEntity<String>(FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@ApiOperation(value = "공지사항 글 삭제", notes = "공지사항 글을 삭제한다.", response = String.class)
//	@DeleteMapping("/{articleno}")
//	public void delete(@PathVariable("articleno") @ApiParam(value = "글 번호", required = true) int articleNo) throws Exception {
//		logger.debug("delete articleNo : {}", articleNo);
//		boardService.deleteArticle(articleNo, servletContext.getRealPath("/upload"));
//		
//	}
	
	@ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@RequestMapping(value = "/{articleno}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteArticle(@ApiParam(value = "삭제할 글의 글번호.") @PathVariable("articleno") int articleno) throws Exception {

		logger.info("deleteArticle - 호출");
		if (boardService.deleteArticle(articleno, servletContext.getRealPath("/upload"))) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);

	}

	@GetMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<Object> downloadFile(@Value("${file.path.upload-files}") String filePath1, @RequestParam("sfolder") String sfolder, @RequestParam("ofile") String ofile,
			@RequestParam("sfile") String sfile) {
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

	}
//	@GetMapping("/delete")
//	public String delete(@RequestParam("articleno") int articleNo, @RequestParam Map<String, String> map,
//			RedirectAttributes redirectAttributes) throws Exception {
//		logger.debug("delete articleNo : {}", articleNo);
//		boardService.deleteArticle(articleNo, servletContext.getRealPath("/upload"));
//		redirectAttributes.addAttribute("pgno", map.get("pgno"));
//		redirectAttributes.addAttribute("key", map.get("key"));
//		redirectAttributes.addAttribute("word", map.get("word"));
//		return "redirect:/board/list";
//	}

//	@GetMapping(value = "/download")
//	@ResponseBody
//	public ResponseEntity<Object> downloadFile(@Value("${file.path.upload-files}") String filePath1, @RequestParam("sfolder") String sfolder, @RequestParam("ofile") String ofile,
//			@RequestParam("sfile") String sfile, HttpSession session) {
//		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
//		if (memberDto != null) {
//			Map<String, Object> fileInfo = new HashMap<String, Object>();
//			String path = filePath1 + File.separator + sfolder + File.separator + sfile;
//			
//			try {
//				Path filePath = Paths.get(path);
//				Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//				
//				File file = new File(path);
//				
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
//				
//				return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
//			} catch(Exception e) {
//				return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
//			}
//		} else {
//			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
//		}
//	}

}
