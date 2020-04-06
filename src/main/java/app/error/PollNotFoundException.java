package app.error;

import java.util.UUID;

public class PollNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public PollNotFoundException(UUID id) {
        super("Poll not found. Id: " + id);
    }

    public PollNotFoundException(String slug) {
        super("Poll not found. Slug: " + slug);
    }

}