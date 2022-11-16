package com.ssafy.hw.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.hw.board.model.BoardDto;
import com.ssafy.hw.util.PageNavigation;

public interface BoardService {

	void writeArticle(BoardDto boardDto) throws Exception;
	List<BoardDto> listArticle(Map<String, String> map) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	void modifyArticle(BoardDto boardDto) throws Exception;
	boolean deleteArticle(int articleNo, String path) throws Exception;
	
}