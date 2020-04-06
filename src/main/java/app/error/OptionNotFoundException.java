package app.error;

import java.util.UUID;

public class OptionNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public OptionNotFoundException(UUID id) {
        super("Option not found: " + id);
    }

}