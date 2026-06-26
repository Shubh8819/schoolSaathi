package com.schoolsaathi.school_managment.dto.common;


import lombok.*;

import java.util.List;

// Jab list return karni ho pagination ke saath
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean isLast;
}