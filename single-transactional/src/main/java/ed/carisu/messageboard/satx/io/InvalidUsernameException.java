package ed.carisu.messageboard.satx.io;

public final class InvalidUsernameException extends RuntimeException {
    private InvalidUsernameException(String message) {
        super(message);
    }

    public static InvalidUsernameException ofWrongLength() {
        return new InvalidUsernameException("The username must be between 1 and 10 characters inclusive");
    }
}
