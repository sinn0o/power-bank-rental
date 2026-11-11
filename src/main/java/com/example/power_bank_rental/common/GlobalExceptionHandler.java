package com.example.power_bank_rental.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// 모든 컨트롤러의 예외를 한 곳에서 잡아 통일된 에러 응답으로 변환
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404, 409 등 커스텀 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getStatus()).body(ErrorResponse.of(e));
    }

    // @Valid 검증 실패 (요청 바디)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(400, "INVALID_REQUEST", message));
    }

    // 쿼리 파라미터 타입이 맞지 않는 경우 (예: status=ABC 같이 존재하지 않는 enum 값)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorResponse body = new ErrorResponse(400, "INVALID_PARAMETER", "요청 파라미터 값이 올바르지 않습니다: " + e.getName());
        return ResponseEntity.badRequest().body(body);
    }

    // 낙관적 락 충돌 (동시에 같은 보조배터리를 대여/반납 시도한 경우)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException e) {
        ErrorResponse body = new ErrorResponse(409, "OPTIMISTIC_LOCK_CONFLICT", "다른 요청과 충돌하여 처리할 수 없습니다. 다시 시도해주세요.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
