/** Thrown when an operation isn't allowed: bad input, or an edit that would break the tree. */
public class InvalidOperationException extends LayerException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
