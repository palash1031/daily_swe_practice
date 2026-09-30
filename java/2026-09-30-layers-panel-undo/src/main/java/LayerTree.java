import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The layers panel for one page: a tree of named layers with undo and redo.
 *
 * <p>A new tree holds a single root layer with id {@code "root"} and name {@code "Page"}. Every edit is wrapped in
 * a {@link Command} and goes through {@link #execute(Command)}, which is what makes it undoable.
 */
public class LayerTree {

    public static final String ROOT_ID = "root";

    private final Layer root = new Layer(ROOT_ID, "Page");
    private final Map<String, Layer> index = new HashMap<>();
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public LayerTree() {
        index.put(ROOT_ID, root);
    }

    // ------------------------------------------------------------------ edits

    /** Adds a new layer as the last child of {@code parentId}. */
    public void add(String parentId, String id, String name) {
        requireId(id);
        requireName(name);
        if (index.containsKey(id)) throw new InvalidOperationException("a layer with id '" + id + "' already exists");
        Layer parent = find(parentId);
        execute(new AddCommand(parent, new Layer(id, name)));
    }

    /** Renames a layer. */
    public void rename(String id, String newName) {
        requireName(newName);
        Layer layer = find(id);
        execute(new RenameCommand(layer, layer.name, newName));
    }

    /**
     * Moves a layer, with everything inside it, so it becomes a child of {@code newParentId} at position
     * {@code index}. The position counts the new parent's children without the layer being moved, so it can be
     * anything from 0 up to that count.
     */
    public void move(String id, String newParentId, int index) {
        Layer layer = find(id);
        Layer newParent = find(newParentId);
        if (layer == root) throw new InvalidOperationException("the root can't be moved");
        if (layer == newParent) throw new InvalidOperationException("a layer can't be moved into itself");
        execute(new MoveCommand(layer, newParent, index));
    }

    /** Deletes a layer and everything inside it. */
    public void delete(String id) {
        Layer layer = find(id);
        if (layer == root) throw new InvalidOperationException("the root can't be deleted");
        execute(new DeleteCommand(layer));
    }

    /** Duplicates a layer and everything inside it. See FEAT-201 in TICKETS.md. */
    public String duplicate(String id) {
        throw new UnsupportedOperationException("FEAT-201 isn't implemented yet");
    }

    // ------------------------------------------------------------------ undo / redo

    /** Undoes the most recent edit. Returns false if there is nothing to undo. */
    public boolean undo() {
        if (undoStack.isEmpty()) return false;
        Command command = undoStack.pop();
        command.revert();
        redoStack.push(command);
        return true;
    }

    /** Redoes the most recently undone edit. Returns false if there is nothing to redo. */
    public boolean redo() {
        if (redoStack.isEmpty()) return false;
        Command command = redoStack.pop();
        command.apply();
        undoStack.push(command);
        return true;
    }

    // ------------------------------------------------------------------ reads

    /** Whether a layer with this id is in the tree. */
    public boolean exists(String id) {
        return id != null && index.containsKey(id);
    }

    /** A snapshot of one layer. */
    public LayerInfo get(String id) {
        return find(id).info();
    }

    /** The ids of a layer's children, top to bottom. */
    public List<String> childrenOf(String id) {
        return find(id).info().childIds();
    }

    /** The whole tree as an indented outline, one layer per line: {@code "  ".repeat(depth) + "Name (id)"}. */
    public String render() {
        StringBuilder out = new StringBuilder();
        render(root, 0, out);
        return out.toString();
    }

    private static void render(Layer layer, int depth, StringBuilder out) {
        out.append("  ".repeat(depth)).append(layer.name).append(" (").append(layer.id).append(")\n");
        for (Layer child : layer.children) render(child, depth + 1, out);
    }

    // ------------------------------------------------------------------ internals

    private void execute(Command command) {
        undoStack.push(command);
        command.apply();
    }

    private Layer find(String id) {
        Layer layer = id == null ? null : index.get(id);
        if (layer == null) throw new LayerNotFoundException(id);
        return layer;
    }

    private static void requireId(String id) {
        if (id == null || id.isBlank()) throw new InvalidOperationException("layer id must not be empty");
    }

    private static void requireName(String name) {
        if (name == null || name.isBlank()) throw new InvalidOperationException("layer name must not be blank");
    }

    /** Puts layer into parent's children at the given position. */
    private static void link(Layer parent, Layer layer, int position) {
        parent.children.add(position, layer);
        layer.parent = parent;
    }

    /** Takes layer out of its parent's children and returns the position it had. */
    private static int unlink(Layer layer) {
        int position = layer.parent.children.indexOf(layer);
        layer.parent.children.remove(position);
        layer.parent = null;
        return position;
    }

    // ------------------------------------------------------------------ commands

    private final class AddCommand implements Command {
        private final Layer parent;
        private final Layer layer;

        AddCommand(Layer parent, Layer layer) {
            this.parent = parent;
            this.layer = layer;
        }

        @Override
        public void apply() {
            link(parent, layer, parent.children.size());
            index.put(layer.id, layer);
        }

        @Override
        public void revert() {
            unlink(layer);
            index.remove(layer.id);
        }
    }

    private static final class RenameCommand implements Command {
        private final Layer layer;
        private final String oldName;
        private final String newName;

        RenameCommand(Layer layer, String oldName, String newName) {
            this.layer = layer;
            this.oldName = oldName;
            this.newName = newName;
        }

        @Override
        public void apply() {
            layer.name = newName;
        }

        @Override
        public void revert() {
            layer.name = oldName;
        }
    }

    private static final class MoveCommand implements Command {
        private final Layer layer;
        private final Layer newParent;
        private final int newPosition;
        private Layer oldParent;
        private int oldPosition;

        MoveCommand(Layer layer, Layer newParent, int newPosition) {
            this.layer = layer;
            this.newParent = newParent;
            this.newPosition = newPosition;
        }

        @Override
        public void apply() {
            oldParent = layer.parent;
            oldPosition = unlink(layer);
            if (newPosition < 0 || newPosition > newParent.children.size()) {
                throw new InvalidOperationException("index " + newPosition + " is out of range");
            }
            link(newParent, layer, newPosition);
        }

        @Override
        public void revert() {
            unlink(layer);
            link(oldParent, layer, oldPosition);
        }
    }

    private final class DeleteCommand implements Command {
        private final Layer layer;
        private Layer oldParent;

        DeleteCommand(Layer layer) {
            this.layer = layer;
        }

        @Override
        public void apply() {
            oldParent = layer.parent;
            unlink(layer);
            index.remove(layer.id);
            layer.children.clear(); // the subtree is gone, so let it be garbage-collected
        }

        @Override
        public void revert() {
            link(oldParent, layer, oldParent.children.size());
            index.put(layer.id, layer);
        }
    }
}
