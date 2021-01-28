package nextstep.subway.common.exception.application;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.auth.exception.UnauthorizedException;
import nextstep.subway.common.exception.dto.ErrorResponse;
import nextstep.subway.line.exception.CanNotFindPathException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(HttpServletRequest request,
		DataIntegrityViolationException e) {
		final String message = "기본 키 또는 유니크 제약조건에 위배됩니다.";
		return ResponseEntity
			.badRequest()
			.body(new ErrorResponse(message, e.getMessage(), extractRequestedPath(request)));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(HttpServletRequest request,
		IllegalArgumentException e) {
		return ResponseEntity
			.badRequest()
			.body(new ErrorResponse(e.getMessage(), extractRequestedPath(request)));
	}

	@ExceptionHandler(CanNotFindPathException.class)
	public ResponseEntity<ErrorResponse> handleCanNotFindPathException(HttpServletRequest request,
		CanNotFindPathException e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(e.getMessage(), extractRequestedPath(request)));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpServletRequest request,
		UnauthorizedException e) {
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(e.getMessage(), extractRequestedPath(request)));
	}

	private String extractRequestedPath(HttpServletRequest request) {
		return request.getServletPath();
	}

}