import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/** The existing test suite. It passes, but it clearly doesn't cover everything. */
public class LayerTreeTest {
    private LayerTree tree;

    @Before
    public void setUp() {
        tree = new LayerTree();
        tree.add("root", "header", "Header");
        tree.add("header", "logo", "Logo");
        tree.add("header", "nav", "Nav");
        tree.add("root", "body", "Body");
    }

    @Test
    public void addAppendsAsLastChild() {
        assertEquals(List.of("header", "body"), tree.childrenOf("root"));
        assertEquals(List.of("logo", "nav"), tree.childrenOf("header"));
        assertEquals(new LayerInfo("logo", "Logo", "header", List.of()), tree.get("logo"));
    }

    @Test
    public void renderShowsTheOutline() {
        assertEquals("Page (root)\n"
                + "  Header (header)\n"
                + "    Logo (logo)\n"
                + "    Nav (nav)\n"
                + "  Body (body)\n", tree.render());
    }

    @Test
    public void renameChangesTheName() {
        tree.rename("logo", "Brand mark");
        assertEquals("Brand mark", tree.get("logo").name());
    }

    @Test
    public void moveToAnotherParent() {
        tree.move("nav", "body", 0);
        assertEquals(List.of("logo"), tree.childrenOf("header"));
        assertEquals(List.of("nav"), tree.childrenOf("body"));
        assertEquals("body", tree.get("nav").parentId());
    }

    @Test
    public void deleteALeaf() {
        tree.delete("nav");
        assertFalse(tree.exists("nav"));
        assertEquals(List.of("logo"), tree.childrenOf("header"));
    }

    @Test
    public void undoAndRedoAnAdd() {
        tree.add("body", "hero", "Hero");
        assertTrue(tree.undo());
        assertFalse(tree.exists("hero"));
        assertTrue(tree.redo());
        assertEquals(List.of("hero"), tree.childrenOf("body"));
    }

    @Test
    public void undoARename() {
        tree.rename("body", "Main");
        tree.undo();
        assertEquals("Body", tree.get("body").name());
    }

    @Test
    public void rejectsDuplicateIds() {
        assertThrows(InvalidOperationException.class, () -> tree.add("root", "logo", "Another logo"));
    }

    @Test
    public void unknownLayersThrow() {
        assertThrows(LayerNotFoundException.class, () -> tree.get("nope"));
        assertThrows(LayerNotFoundException.class, () -> tree.add("nope", "x", "X"));
    }
}
