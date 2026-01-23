package com.rental_api.ServiceBooking.Exception;

import com.rental_api.ServiceBooking.Dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.rental_api.ServiceBooking.Exception.CategoryNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - User Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> userNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    // 404 - Role Not Found
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> roleNotFound(RoleNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "Not Found", ex.getMessage()));
    }

    // 401 - Unauthorized (custom)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error(401, "Unauthorized", ex.getMessage()));
    }

    // 401 - Bad credentials (wrong email/password)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> badCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401)
                .body(ApiResponse.error(401, "Unauthorized", ex.getMessage()));
    }

    // 409 - Conflict (e.g., email already exists)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> conflict(ConflictException ex) {
        return ResponseEntity.status(409)
                .body(ApiResponse.error(409, "Conflict", ex.getMessage()));
    }

    // 400 - Invalid input (e.g., email format)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<Object>> invalidInput(InvalidInputException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "Bad Request", ex.getMessage()));
    }

    // 400 - Other runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> badRequest(RuntimeException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "Bad Request", ex.getMessage()));
    }

    // 400 - Type mismatch (invalid path or query parameter)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Invalid value for parameter '%s'. Expected type: %s.", paramName, requiredType);

        return ResponseEntity.status(400)
                .body(ApiResponse.error(400, "Bad Request", message));
    }

    // 404 - Endpoint not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "Not Found",
                        "Endpoint not found. Please check the URL or method."));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCategoryNotFound(CategoryNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(
                    404,
                    "Not Found",
                    ex.getMessage()
            ));
    } 



}