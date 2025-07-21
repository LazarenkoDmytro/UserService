package ua.org.blablacar.userservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import ua.org.blablacar.userservice.dto.UserCreateDto;
import ua.org.blablacar.userservice.dto.UserReadDto;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase(
    connection = EmbeddedDatabaseConnection.H2,
    replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class UserControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    String url(String p) {
        return "http://localhost:" + port + p;
    }

    @Test
    void createAndRetrieveUser() {
        var dto = new UserCreateDto("Ann", "Smith", "a@b.com",
            LocalDate.parse("2000-01-01"), "F");

        ResponseEntity<UserReadDto> created =
            rest.postForEntity(url("/api/users"), dto, UserReadDto.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertNotNull(created.getBody());
        UserReadDto fromGet = rest.getForObject(
            url("/api/users/" + created.getBody().id()), UserReadDto.class);

        assertThat(fromGet.email()).isEqualTo("a@b.com");
    }

    @Test
    void paginationParametersAreRespected() {
        ResponseEntity<Map> resp =
            rest.getForEntity(url("/api/users?pageNumber=1&pageSize=1"), Map.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> pag = (Map<String, Object>) resp.getBody().get("pagination");
        assertThat(pag.get("pageNumber")).isEqualTo(1);
        assertThat(pag.get("pageSize")).isEqualTo(1);
    }
}