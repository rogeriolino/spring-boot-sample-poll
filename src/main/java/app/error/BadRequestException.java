package app.error;

class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        this("Bad request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}