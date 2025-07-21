package ua.org.blablacar.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackages = {
        "ua.org.blablacar.userservice"
    }
)
@EntityScan("ua.org.blablacar.userservice.entity")
@EnableJpaRepositories("ua.org.blablacar.userservice.repository")
public class UserServiceApplication {
    // slight change to test
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
