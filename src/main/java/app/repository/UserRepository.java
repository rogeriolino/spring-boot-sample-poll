package app.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.model.User;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);
}