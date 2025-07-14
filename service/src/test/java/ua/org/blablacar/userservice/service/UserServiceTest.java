package ua.org.blablacar.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.org.blablacar.userservice.dto.UserCreateDto;
import ua.org.blablacar.userservice.dto.UserPatchDto;
import ua.org.blablacar.userservice.dto.UserReadDto;
import ua.org.blablacar.userservice.dto.UserUpdateDto;
import ua.org.blablacar.userservice.entity.User;
import ua.org.blablacar.userservice.mapper.UserMapper;
import ua.org.blablacar.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repo;
    @Mock
    UserMapper mapper;
    @InjectMocks
    UserService service;

    UUID id;
    User entity;
    UserReadDto dto;

    @BeforeEach
    void init() {
        id = UUID.randomUUID();
        entity = User.builder()
            .id(id)
            .firstName("Ann")
            .lastName("Smith")
            .email("a@b.com")
            .dateOfBirth(LocalDate.parse("2000-01-01"))
            .gender("F").build();
        dto = new UserReadDto(id, "Ann", "Smith", "a@b.com",
            LocalDate.parse("2000-01-01"), "F");
    }

    @Test
    void createSavesAndReturnsDto() {
        var createDto = new UserCreateDto("Ann", "Smith", "a@b.com",
            LocalDate.parse("2000-01-01"), "F");
        when(mapper.toEntity(createDto)).thenReturn(entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        var result = service.create(createDto);

        assertThat(result).isEqualTo(dto);
        verify(repo).save(entity);
    }

    @Test
    void findAllMapsPage() {
        var pageable = PageRequest.of(0, 2);
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity)));
        when(mapper.toDto(entity)).thenReturn(dto);

        Page<UserReadDto> page = service.findAll(pageable);

        assertThat(page.getContent()).containsExactly(dto);
    }

    @Test
    void findByIdReturnsDto() {
        when(repo.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        assertThat(service.findById(id)).isEqualTo(dto);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateMergesAndSaves() {
        var update = new UserUpdateDto("Bob", "Stone", "b@c.com",
            LocalDate.parse("1990-05-05"), "M");
        when(repo.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(a -> {
            entity.setFirstName("Bob");
            return null;
        }).when(mapper).update(update, entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        UserReadDto res = service.update(id, update);

        assertThat(res).isEqualTo(dto);
        verify(mapper).update(update, entity);
    }

    @Test
    void patchIgnoresNulls() {
        var patch = new UserPatchDto();
        patch.setLastName("White");
        when(repo.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(a -> {
            entity.setLastName("White");
            return null;
        }).when(mapper).patch(patch, entity);
        when(repo.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        UserReadDto res = service.patch(id, patch);

        assertThat(res).isEqualTo(dto);
    }

    @Test
    void deleteDelegatesToRepo() {
        when(repo.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        verify(repo).delete(entity);
    }
}