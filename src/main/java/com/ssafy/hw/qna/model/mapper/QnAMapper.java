package com.ssafy.hw.qna.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.memo.model.MemoDto;
import com.ssafy.hw.qna.model.QnADto;
import com.ssafy.hw.qna.model.QnAParameterDto;


@Mapper
public interface QnAMapper {
	
	public int writeArticle(QnADto qnaDto) throws SQLException;
	public int writeMemo(MemoDto memoDto) throws SQLException;
	public List<QnADto> listArticle(QnAParameterDto qnaParameterDto) throws SQLException;
	public int getTotalCount(QnAParameterDto qnaParameterDto) throws SQLException;
	public QnADto getArticle(int articleno) throws SQLException;
	public void updateHit(int articleno) throws SQLException;
	public int modifyArticle(QnADto qnaDto) throws SQLException;
	public void deleteMemo(int articleno) throws SQLException;
	public int deleteArticle(int articleno) throws SQLException;
	
}