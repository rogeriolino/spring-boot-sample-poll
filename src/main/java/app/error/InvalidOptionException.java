package app.error;

public class InvalidOptionException extends BadRequestException {

    private static final long serialVersionUID = 1L;

    public InvalidOptionException() {
        super("Invalid option");
    }

}