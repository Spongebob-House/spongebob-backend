package com.ssafy.hw.memo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.hw.memo.model.MemoDto;
import com.ssafy.hw.memo.model.MemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/memo")
@Api("QnA 댓글 컨트롤러  API V1")
public class MemoController {
	private static final Logger logger = LoggerFactory.getLogger(MemoController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private MemoService memoService;

	@ApiOperation(value = "게시판 댓글작성", notes = "새로운 댓글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeMemo(@RequestBody @ApiParam(value = "댓글 정보.", required = true) MemoDto memoDto)
			throws Exception {
		logger.info("writeMemo - 호출");
		if (memoService.writeMemo(memoDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "게시판 댓글보기", notes = "글번호에 해당하는 댓글의 정보를 반환한다.", response = MemoDto.class)
	@GetMapping("/{articleno}")
	public ResponseEntity<MemoDto> getMemo(
			@PathVariable("articleno") @ApiParam(value = "얻어올 댓글의 글번호.", required = true) int articleno) throws Exception {
		logger.info("getMemo - 호출 : " + articleno);
		return new ResponseEntity<MemoDto>(memoService.getMemo(articleno), HttpStatus.OK);
	}

	@ApiOperation(value = "게시판 댓글수정", notes = "수정할 댓글 정보를 입력한다. 그리고 DBf수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyMemo(
			@RequestBody @ApiParam(value = "수정할 댓글정보.", required = true) MemoDto memoDto) throws Exception {
		logger.info("modifyMemo - 호출 {}", memoDto);

		if (memoService.modifyMemo(memoDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.OK);
	}

	@ApiOperation(value = "게시판 댓글삭제", notes = "글번호에 해당하는 댓글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{memono}")
	public ResponseEntity<String> deleteMemo(
			@PathVariable("memono") @ApiParam(value = "삭제할 댓글의 글번호.", required = true) int memono) throws Exception {
		logger.info("deleteMemo - 호출");
		if (memoService.deleteMemo(memono)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}
