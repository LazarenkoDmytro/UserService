package ua.org.blablacar.userservice.dto;

import java.time.LocalDate;

public record UserCreateDto(
    String firstName,
    String lastName,
    String email,
    LocalDate dateOfBirth,
    String gender) {
}