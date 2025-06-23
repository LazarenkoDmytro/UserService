package ua.org.blablacar.userservice.dto;

import java.time.LocalDate;

public record UserReadDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    LocalDate dateOfBirth,
    String gender) {
}