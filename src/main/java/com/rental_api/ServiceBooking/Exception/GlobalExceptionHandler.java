package com.rental_api.ServiceBooking.Exception;

import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> userNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> roleNotFound(RoleNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> conflict(ConflictException ex) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> badRequest(RuntimeException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "Bad Request", ex.getMessage()));
    }
    @ExceptionHandler(RequestNotFoundException.class)
public ResponseEntity<ApiResponse<Object>> requestNotFound(RequestNotFoundException ex) {
    return ResponseEntity.status(404)
            .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
}
@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String paramName = ex.getName();
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
    String message = String.format("Invalid value for parameter '%s'. Expected type: %s.", paramName, requiredType);

    return ResponseEntity.status(400)
            .body(ApiResponse.error(400, "Bad Request", message));
}

    // <-- ADD THIS TO HANDLE WRONG ENDPOINTS -->
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "Not Found",
                        "Endpoint not found. Please check the URL or method."));
    }
}
