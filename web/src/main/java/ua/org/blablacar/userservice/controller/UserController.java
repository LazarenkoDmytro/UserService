package ua.org.blablacar.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.org.blablacar.userservice.dto.UserCreateDto;
import ua.org.blablacar.userservice.dto.UserPatchDto;
import ua.org.blablacar.userservice.dto.UserReadDto;
import ua.org.blablacar.userservice.dto.UserUpdateDto;
import ua.org.blablacar.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserReadDto> create(@Valid @RequestBody UserCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public Page<UserReadDto> getAll(@ParameterObject Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserReadDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable Long id,
                              @Valid @RequestBody UserUpdateDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public UserReadDto patch(@PathVariable Long id,
                             @RequestBody UserPatchDto dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
