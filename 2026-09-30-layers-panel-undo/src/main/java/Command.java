/**
 * One undoable edit. {@link #apply()} performs it and {@link #revert()} undoes it. Redo calls apply() again, so
 * apply() must work both the first time and after a revert().
 */
interface Command {
    void apply();

    void revert();
}
