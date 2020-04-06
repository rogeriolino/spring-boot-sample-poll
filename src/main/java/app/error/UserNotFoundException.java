package app.error;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(UUID id) {
        super("User not found. Id: " + id);
    }

    public UserNotFoundException(String username) {
        super("User not found. Username: " + username);
    }

}