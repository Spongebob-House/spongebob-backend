package com.ssafy.hw.memo.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.memo.model.MemoDto;

@Mapper
public interface MemoMapper {

	public int writeMemo(MemoDto memoDto) throws SQLException;

	public MemoDto getMemo(int articleno) throws SQLException;

	public int modifyMemo(MemoDto memoDto) throws SQLException;

	public int deleteMemo(int memono) throws SQLException;

}