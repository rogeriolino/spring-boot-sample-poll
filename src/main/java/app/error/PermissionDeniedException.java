package app.error;

public class PermissionDeniedException extends ForbiddenException {

    private static final long serialVersionUID = 1L;

    public PermissionDeniedException() {
        super("You dont have permission to complete this action");
    }

}