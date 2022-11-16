package com.ssafy.hw.qna.model.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.hw.qna.model.QnADto;
import com.ssafy.hw.qna.model.QnAParameterDto;
import com.ssafy.hw.qna.model.mapper.QnAMapper;
import com.ssafy.hw.util.PageNavigation;

@Service
public class QnAServiceImpl implements QnAService {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public boolean writeArticle(QnADto qnaDto) throws Exception {
		if (qnaDto.getSubject() == null || qnaDto.getContent() == null) {
			throw new Exception();
		}
		return sqlSession.getMapper(QnAMapper.class).writeArticle(qnaDto) == 1;
	}

	@Override
	public List<QnADto> listArticle(QnAParameterDto qnaParmeterDto) throws Exception {
		int start = qnaParmeterDto.getPg() == 0 ? 0 : (qnaParmeterDto.getPg() - 1) * qnaParmeterDto.getSpp();
		qnaParmeterDto.setStart(start);
		return sqlSession.getMapper(QnAMapper.class).listArticle(qnaParmeterDto);
	}

	@Override
	public PageNavigation makePageNavigation(QnAParameterDto qnaParmeterDto) throws Exception {
		int naviSize = 5;
		PageNavigation pageNavigation = new PageNavigation();
		pageNavigation.setCurrentPage(qnaParmeterDto.getPg());
		pageNavigation.setNaviSize(naviSize);
		int totalCount = sqlSession.getMapper(QnAMapper.class).getTotalCount(qnaParmeterDto);// 총글갯수 269
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / qnaParmeterDto.getSpp() + 1;// 27
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = qnaParmeterDto.getPg() <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < qnaParmeterDto.getPg();
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();
		return pageNavigation;
	}

	@Override
	public QnADto getArticle(int articleno) throws Exception {
		return sqlSession.getMapper(QnAMapper.class).getArticle(articleno);
	}

	@Override
	public void updateHit(int articleno) throws Exception {
		sqlSession.getMapper(QnAMapper.class).updateHit(articleno);
	}

	@Override
	@Transactional
	public boolean modifyArticle(QnADto qnaDto) throws Exception {
		return sqlSession.getMapper(QnAMapper.class).modifyArticle(qnaDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteArticle(int articleno) throws Exception {
		sqlSession.getMapper(QnAMapper.class).deleteMemo(articleno);
		return sqlSession.getMapper(QnAMapper.class).deleteArticle(articleno) == 1;
	}

}