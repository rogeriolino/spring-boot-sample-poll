package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import app.config.SecurityConfiguration;
import app.model.User;
import app.repository.UserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository repository;

	@Autowired
	public DatabaseLoader(UserRepository repository) {
		this.repository = repository;
	}

    @Override
    public void run(String... args) throws Exception {
        User user = repository.findByUsername("admin");
        if (user == null) {
            PasswordEncoder encoder = SecurityConfiguration.getPasswordEncode();

            user = new User();
            user.setUsername("admin");
            user.setPassword(encoder.encode("123456"));
            user.setRoles(new String[]{"ROLE_ADMIN"});

            repository.save(user);
        }
    }
}