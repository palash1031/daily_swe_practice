/** Base class for every error the layers panel reports. */
public class LayerException extends RuntimeException {
    public LayerException(String message) {
        super(message);
    }
}
