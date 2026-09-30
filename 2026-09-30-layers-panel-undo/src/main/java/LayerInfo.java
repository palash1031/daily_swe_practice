import java.util.List;

/**
 * A read-only snapshot of one layer, as the rest of the app sees it.
 *
 * @param id       the layer's id
 * @param name     the layer's display name
 * @param parentId the parent's id, or null for the root
 * @param childIds the ids of the layer's children, top to bottom
 */
public record LayerInfo(String id, String name, String parentId, List<String> childIds) {
    public LayerInfo {
        childIds = List.copyOf(childIds);
    }
}
