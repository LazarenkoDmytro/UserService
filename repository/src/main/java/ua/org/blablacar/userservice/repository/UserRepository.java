package ua.org.blablacar.userservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.org.blablacar.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
