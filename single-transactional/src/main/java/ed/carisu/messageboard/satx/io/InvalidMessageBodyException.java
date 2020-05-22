package ed.carisu.messageboard.satx.io;

public final class InvalidMessageBodyException extends RuntimeException {
    private InvalidMessageBodyException(String message) {
        super(message);
    }

    public static InvalidMessageBodyException ofWrongLength() {
        return new InvalidMessageBodyException("The message body must be 1000 characters or less");
    }

    public static InvalidMessageBodyException ofWrongCharset() {
        return new InvalidMessageBodyException("The message body contains non-ASCII characters or non-printable characters");
    }
}
