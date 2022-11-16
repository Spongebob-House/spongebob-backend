package com.ssafy.hw.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.board.model.BoardDto;
import com.ssafy.hw.board.model.FileInfoDto;
import com.ssafy.hw.memo.model.MemoDto;

@Mapper
public interface BoardMapper {

	int writeArticle(BoardDto boardDto) throws SQLException;

	int registerFile(BoardDto boardDto) throws Exception;

	List<BoardDto> listArticle(Map<String, Object> map) throws SQLException;

	int getTotalArticleCount(Map<String, Object> map) throws SQLException;

	BoardDto getArticle(int articleNo) throws SQLException;

	void updateHit(int articleNo) throws SQLException;

	int modifyArticle(BoardDto boardDto) throws SQLException;

	int deleteFile(int articleNo) throws Exception;

	int deleteArticle(int articleNo) throws SQLException;

	List<FileInfoDto> fileInfoList(int articleNo) throws Exception;

}
