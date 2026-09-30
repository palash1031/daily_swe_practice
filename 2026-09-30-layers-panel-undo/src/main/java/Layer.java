import java.util.ArrayList;
import java.util.List;

/** One node in the layer tree. Internal: code outside LayerTree only ever sees {@link LayerInfo} snapshots. */
final class Layer {
    final String id;
    String name;
    Layer parent;
    final List<Layer> children = new ArrayList<>();

    Layer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    LayerInfo info() {
        List<String> childIds = new ArrayList<>();
        for (Layer child : children) childIds.add(child.id);
        return new LayerInfo(id, name, parent == null ? null : parent.id, childIds);
    }
}
