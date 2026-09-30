/** Thrown when an operation names a layer id that isn't in the tree. */
public class LayerNotFoundException extends LayerException {
    public LayerNotFoundException(String id) {
        super("no layer with id '" + id + "'");
    }
}
