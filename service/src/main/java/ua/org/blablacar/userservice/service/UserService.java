package ua.org.blablacar.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.org.blablacar.userservice.dto.UserCreateDto;
import ua.org.blablacar.userservice.dto.UserPatchDto;
import ua.org.blablacar.userservice.dto.UserReadDto;
import ua.org.blablacar.userservice.dto.UserUpdateDto;
import ua.org.blablacar.userservice.entity.User;
import ua.org.blablacar.userservice.mapper.UserMapper;
import ua.org.blablacar.userservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserReadDto create(UserCreateDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    public Page<UserReadDto> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toDto);
    }

    public UserReadDto findById(Long id) {
        return mapper.toDto(get(id));
    }

    public UserReadDto update(Long id, UserUpdateDto dto) {
        User entity = get(id);
        mapper.update(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    public UserReadDto patch(Long id, UserPatchDto dto) {
        User entity = get(id);
        mapper.patch(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    public void delete(Long id) {
        repo.delete(get(id));
    }

    private User get(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));
    }
}