package app.error;

class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        this("Forbiden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}