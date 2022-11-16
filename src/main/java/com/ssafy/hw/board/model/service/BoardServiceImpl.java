package com.ssafy.hw.board.model.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.hw.board.model.BoardDto;
import com.ssafy.hw.board.model.FileInfoDto;
import com.ssafy.hw.board.model.mapper.BoardMapper;
import com.ssafy.hw.util.PageNavigation;
import com.ssafy.hw.util.SizeConstant;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;

	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	@Override
//	@Transactional
	public boolean writeArticle(BoardDto boardDto) throws Exception {
		System.out.println("글입력 전 dto : " + boardDto);
		if(boardMapper.writeArticle(boardDto) == 1) {
			System.out.println("글입력 후 dto : " + boardDto);
			List<FileInfoDto> fileInfos = boardDto.getFileInfos();
			if (fileInfos != null && !fileInfos.isEmpty()) {
				if(boardMapper.registerFile(boardDto) == 1) {
					return true;
				}
			}
			else return true;
		};
		return false;
	}

	@Override
	public List<BoardDto> listArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		String key = map.get("key");
		if ("userid".equals(key))
			key = "b.user_id";
		int pgNo = Integer.parseInt((map.get("pgno") != null && !"".equals(map.get("pgno")))? map.get("pgno"): "1");
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
		param.put("key", map.get("key"));
		param.put("word", map.get("word"));
		param.put("start", start);
		param.put("listsize", SizeConstant.LIST_SIZE);
		return boardMapper.listArticle(param);
	}

	@Override
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
		
		PageNavigation pageNavigation = new PageNavigation();

		int naviSize = SizeConstant.NAVIGATION_SIZE;
		int sizePerPage = SizeConstant.LIST_SIZE;
		int currentPage = Integer.parseInt((map.get("pgno") != null && !"".equals(map.get("pgno")))? map.get("pgno"): "1");
		

		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setNaviSize(naviSize);
		
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");
		if ("userid".equals(key))
			key = "user_id";
		param.put("key", key == null ? "" : key);
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		
		int totalCount = boardMapper.getTotalArticleCount(param);
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = currentPage <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();
		
		return pageNavigation;
	}

	@Override
	public BoardDto getArticle(int articleNo) throws Exception {
		return boardMapper.getArticle(articleNo);
	}

	@Override
	public void updateHit(int articleNo) throws Exception {
		boardMapper.updateHit(articleNo);
	}

	@Override
	public boolean modifyArticle(BoardDto boardDto) throws Exception {
		return boardMapper.modifyArticle(boardDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteArticle(int articleNo, String path) throws Exception {
		List<FileInfoDto> fileList = boardMapper.fileInfoList(articleNo);
		if(boardMapper.deleteFile(articleNo) == 1) {
			if(boardMapper.deleteArticle(articleNo) == 1) {
				for(FileInfoDto fileInfoDto : fileList) {
					File file = new File(path + File.separator + fileInfoDto.getSaveFolder() + File.separator + fileInfoDto.getSaveFile());
					file.delete();
				}
				return true;
			}
		}

		return false;
	}

}