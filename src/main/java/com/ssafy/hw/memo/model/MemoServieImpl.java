package com.ssafy.hw.memo.model;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.hw.memo.model.mapper.MemoMapper;

@Service
public class MemoServieImpl implements MemoService {

	@Autowired
	private SqlSession sqlSession;

	public boolean writeMemo(MemoDto memoDto) throws Exception {
		if (memoDto.getComment() == null) {
			throw new Exception();
		}
		return sqlSession.getMapper(MemoMapper.class).writeMemo(memoDto) == 1;
	}

	@Override
	public MemoDto getMemo(int articleno) throws Exception {
		return sqlSession.getMapper(MemoMapper.class).getMemo(articleno);
	}

	@Override
	@Transactional
	public boolean modifyMemo(MemoDto memoDto) throws Exception {
		return sqlSession.getMapper(MemoMapper.class).modifyMemo(memoDto) == 1;
	}

	@Override
	@Transactional
	public boolean deleteMemo(int memono) throws Exception {
		return sqlSession.getMapper(MemoMapper.class).deleteMemo(memono) == 1;
	}

}