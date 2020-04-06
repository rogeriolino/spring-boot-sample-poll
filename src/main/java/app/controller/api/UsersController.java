package app.controller.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.config.SecurityConfiguration;
import app.error.UserNotFoundException;
import app.error.UsernameAlreadyInUseException;
import app.model.User;
import app.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private UserRepository repository;

    @Autowired
    public UsersController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable UUID id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        User existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new UsernameAlreadyInUseException();
        }

        PasswordEncoder encoder = SecurityConfiguration.getPasswordEncode();

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(new String[]{"ROLE_USER"});

        return repository.save(user);
    }
}
