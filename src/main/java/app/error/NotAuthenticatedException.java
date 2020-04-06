package app.error;

public class NotAuthenticatedException extends ForbiddenException {

    private static final long serialVersionUID = 1L;

    public NotAuthenticatedException() {
        super("User not authenticated");
    }

}