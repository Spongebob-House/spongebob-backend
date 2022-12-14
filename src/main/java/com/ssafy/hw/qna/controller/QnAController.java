package com.ssafy.hw.qna.controller;

import java.util.List;

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

import com.ssafy.hw.qna.model.QnADto;
import com.ssafy.hw.qna.model.QnAParameterDto;
import com.ssafy.hw.qna.model.service.QnAService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/qna")
@Api("QnA게시판 컨트롤러  API V1")
public class QnAController {
	private static final Logger logger = LoggerFactory.getLogger(QnAController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Autowired
	private QnAService qnaService;

	// 글 작성
	@ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> writeArticle(@RequestBody @ApiParam(value = "게시글 정보.", required = true) QnADto qnaDto)
			throws Exception {
		logger.info("writeArticle - 호출");
		if (qnaService.writeArticle(qnaDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	// 글 목록
	@ApiOperation(value = "게시판 글목록", notes = "모든 게시글의 정보를 반환한다.", response = List.class)
	@GetMapping
	public ResponseEntity<List<QnADto>> listArticle(
			@ApiParam(value = "게시글을 얻기위한 부가정보.", required = true) QnAParameterDto qnaParameterDto) throws Exception {
		logger.info("listArticle - 호출");
		return new ResponseEntity<List<QnADto>>(qnaService.listArticle(qnaParameterDto), HttpStatus.OK);
	}
	
	// qna 목록
	@ApiOperation(value = "qna 글목록", notes = "모든 qna 정보를 반환한다.", response = List.class)
	@GetMapping("/inquiry")
	public ResponseEntity<List<QnADto>> qnaList(
			@ApiParam(value = "qna 얻기위한 부가정보.", required = true) QnAParameterDto qnaParameterDto) throws Exception {
		logger.info("listArticle - 호출");
		return new ResponseEntity<List<QnADto>>(qnaService.listArticle(qnaParameterDto), HttpStatus.OK);
	}


	// 글 상세 보기
	@ApiOperation(value = "게시판 글보기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = QnADto.class)
	@GetMapping("/{articleno}")
	public ResponseEntity<QnADto> getArticle(
			@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		logger.info("getArticle - 호출 : " + articleno);
		qnaService.updateHit(articleno);
		return new ResponseEntity<QnADto>(qnaService.getArticle(articleno), HttpStatus.OK);
	}

	// 글 수정
	@ApiOperation(value = "게시판 글수정", notes = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyArticle(
			@RequestBody @ApiParam(value = "수정할 글정보.", required = true) QnADto qnaDto) throws Exception {
		logger.info("modifyArticle - 호출 {}", qnaDto);

		if (qnaService.modifyArticle(qnaDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.OK);
	}

	// 글 삭제
	@ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(
			@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleno)
			throws Exception {
		logger.info("deleteArticle - 호출");
		if (qnaService.deleteArticle(articleno)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}
}
