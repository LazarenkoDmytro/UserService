package ua.org.blablacar.userservice.dto;

public record PaginationDto(
    // pageNumber is 1-based
    int pageNumber,
    int pageSize,
    long totalItems,
    int totalPages) {
}