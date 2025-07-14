package ua.org.blablacar.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserReadDto create(UserCreateDto dto) {
        log.info("createUser start firstName={} email={}", dto.firstName(), dto.email());
        UserReadDto result = mapper.toDto(repo.save(mapper.toEntity(dto)));
        log.info("createUser success id={}", result.id());
        return result;
    }

    public Page<UserReadDto> findAll(Pageable pageable) {
        log.debug("findAllUsers page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(mapper::toDto);
    }

    public UserReadDto findById(UUID id) {
        log.debug("findUserById id={}", id);
        return mapper.toDto(get(id));
    }

    public UserReadDto update(UUID id, UserUpdateDto dto) {
        log.info("updateUser id={}", id);
        User entity = get(id);
        mapper.update(dto, entity);
        UserReadDto result = mapper.toDto(repo.save(entity));
        log.info("updateUser success id={}", id);
        return result;
    }

    public UserReadDto patch(UUID id, UserPatchDto dto) {
        log.info("patchUser id={}", id);
        User entity = get(id);
        mapper.patch(dto, entity);
        UserReadDto result = mapper.toDto(repo.save(entity));
        log.info("patchUser success id={}", id);
        return result;
    }

    public void delete(UUID id) {
        log.warn("deleteUser id={}", id);
        repo.delete(get(id));
    }

    private User get(UUID id) {
        return repo.findById(id)
            .orElseThrow(() -> {
                log.error("userNotFound id={}", id);
                return new EntityNotFoundException("User " + id + " not found");
            });
    }
}