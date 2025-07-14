package ua.org.blablacar.userservice.mapper;


import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ua.org.blablacar.userservice.dto.UserCreateDto;
import ua.org.blablacar.userservice.dto.UserPatchDto;
import ua.org.blablacar.userservice.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntityCopiesFields() {
        var dto = new UserCreateDto("A", "B", "a@b.com",
            LocalDate.parse("2000-01-01"), "F");
        User e = mapper.toEntity(dto);

        assertThat(e.getEmail()).isEqualTo("a@b.com");
    }

    @Test
    void patchSkipsNulls() {
        User e = User.builder().firstName("X").build();
        var patch = new UserPatchDto();
        patch.setLastName("Y");

        mapper.patch(patch, e);

        assertThat(e.getFirstName()).isEqualTo("X");
        assertThat(e.getLastName()).isEqualTo("Y");
    }
}