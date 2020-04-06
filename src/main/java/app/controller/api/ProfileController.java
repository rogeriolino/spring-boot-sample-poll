package app.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import app.service.SecurityService;

@RestController
@RequestMapping("/api/profile")
class ProfileController {

    private SecurityService security;

    @Autowired
    ProfileController(SecurityService security) {
        this.security = security;
    }

    @GetMapping("")
    User get() {
        return security.getUser();
    }
}
