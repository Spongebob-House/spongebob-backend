package com.ssafy.hw.memo.model;

public interface MemoService {

	boolean writeMemo(MemoDto memoDto) throws Exception;

	MemoDto getMemo(int articleno) throws Exception;

	boolean modifyMemo(MemoDto memoDto) throws Exception;

	boolean deleteMemo(int memono) throws Exception;

}
