package ua.org.blablacar.userservice.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginatedResponseDto<T>(
    List<T> items,
    PaginationDto pagination) {

    public static <T> PaginatedResponseDto<T> from(Page<T> page) {
        return new PaginatedResponseDto<>(
            page.getContent(),
            new PaginationDto(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()));
    }
}
