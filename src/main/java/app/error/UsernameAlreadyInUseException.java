package app.error;

public class UsernameAlreadyInUseException extends BadRequestException {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyInUseException() {
        super("The choosed username is already in use. Please choose another username");
    }

}