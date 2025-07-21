package ua.org.blablacar.userservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserReadDto(
    UUID id,
    String firstName,
    String lastName,
    String email,
    LocalDate dateOfBirth,
    String gender) {
}