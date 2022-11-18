package com.ssafy.hw.qna.model.service;

import java.util.List;

import com.ssafy.hw.memo.model.MemoDto;
import com.ssafy.hw.qna.model.QnADto;
import com.ssafy.hw.qna.model.QnAParameterDto;
import com.ssafy.hw.util.PageNavigation;

public interface QnAService {
	public boolean writeArticle(QnADto qnaDto) throws Exception;

	public List<QnADto> listArticle(QnAParameterDto boardParameterDto) throws Exception;

	public PageNavigation makePageNavigation(QnAParameterDto boardParameterDto) throws Exception;

	public QnADto getArticle(int articleno) throws Exception;

	public void updateHit(int articleno) throws Exception;

	public boolean modifyArticle(QnADto boardDto) throws Exception;

	public boolean deleteArticle(int articleno) throws Exception;

	public boolean writeMemo(MemoDto memoDto) throws Exception;

//	public MemoDto getMemo(int memono) throws Exception;
}
