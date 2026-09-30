/**
 * Scratch space. Run with ./run-main.sh (Windows: run-main.bat). Not graded.
 *
 * Use it to reproduce tickets and to produce the real output that DESIGN.md asks for.
 */
public class Main {
    public static void main(String[] args) {
        LayerTree tree = new LayerTree();
        tree.add("root", "header", "Header");
        tree.add("header", "logo", "Logo");
        tree.add("header", "nav", "Nav");
        tree.add("root", "body", "Body");
        System.out.print(tree.render());
    }
}
