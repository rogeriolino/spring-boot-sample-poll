package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import app.model.User;
import app.repository.UserRepository;

@Component
public class SecurityService {

    private UserRepository userRepo;

    @Autowired
    public SecurityService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User getUser() {
        User user = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof Authentication) {
            user = userRepo.findByUsername(auth.getName());
        }

        return user;
    }
}