package com.rental_api.ServiceBooking.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String error;
    private T message;

    public static <T> ApiResponse<T> error(int status, String error, T message) {
        return new ApiResponse<>(status, error, message);
    }

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}
