package com.schoolsaathi.school_managment.dto.common;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private String error;

    // Success response
    public static <T> ApiResponse<T> success(
            String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // Error response
    public static <T> ApiResponse<T> error(
            String error) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .build();
    }
}