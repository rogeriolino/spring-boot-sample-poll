package app.error;

class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        this("Not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}