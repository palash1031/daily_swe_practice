# Layers Panel: Fix Bugs, Add a Feature (Java)

*Practice exercise for 2026-09-30. Type: bug fixes and a small feature in an existing codebase. This is not a real Figma question.*

- **Time:** 100 minutes. Start a timer when you begin reading. Save at least 25 minutes for `DESIGN.md`. The time is tight on purpose, so prioritize.
- **Language:** Java 17 or newer, standard library only. Tests use JUnit 4.13.

## The situation

You've joined the team that owns the layers panel. The code in `src/main/java` keeps a page's layers as a tree and supports undo and redo: every edit is a `Command` with `apply()` and `revert()`, and `LayerTree.execute` records it. The existing tests pass, but QA has filed a stack of tickets. They're all in `TICKETS.md`.

## Your tasks

1. **Fix BUG-101 to BUG-105.** For each one, first write a test that fails because of the bug, then fix the code. Name each test after its ticket, like `bug101_cannotMoveIntoDescendant`. Fix the root cause, not just the symptom in the report.
2. **Triage BUG-106.** Decide whether it's a bug, act on your decision, and explain it in `DESIGN.md`.
3. **Implement FEAT-201**, `duplicate`, with tests.
4. **Answer every question in `DESIGN.md`.**

## Ground rules

- Keep the public API as it is: the public methods of `LayerTree`, the `LayerInfo` record, and the exception classes. You may change anything private or package-private, and add private helpers or package-private classes.
- Report errors with the existing exceptions: `LayerNotFoundException` for an id that isn't in the tree, and `InvalidOperationException` for everything else. Don't add new public exception classes.
- Every edit must stay undoable and redoable.
- The existing tests must keep passing.
- Your tests should use only the public API. The grader also runs your tests against its own implementation to see which bugs they catch.

## API reference

| Method | Behavior |
|---|---|
| `new LayerTree()` | A tree holding only the root: id `"root"`, name `"Page"`. |
| `add(parentId, id, name)` | Adds a layer as the last child of `parentId`. The id must be non-blank and unused, and the name non-blank. |
| `rename(id, newName)` | Renames a layer. The name must be non-blank. |
| `move(id, newParentId, index)` | Moves a layer, with everything inside it, to position `index` among `newParentId`'s children. `index` counts those children without the layer being moved, so it runs from 0 to that count. The root can't be moved. |
| `delete(id)` | Deletes a layer and everything inside it. The root can't be deleted. |
| `duplicate(id)` | FEAT-201. Not implemented yet. |
| `undo()` / `redo()` | Undo or redo one edit. Each returns `false` if there's nothing to undo or redo. |
| `exists(id)` | Whether a layer with this id is in the tree. |
| `get(id)` | A `LayerInfo` snapshot: id, name, parent id (`null` for the root), and child ids. |
| `childrenOf(id)` | A layer's child ids, top to bottom. |
| `render()` | The tree as an indented outline: two spaces per level, then `Name (id)`, one layer per line. |

## Files

| File | Purpose |
|---|---|
| `TICKETS.md` | The bug reports and the feature request. |
| `src/main/java/LayerTree.java` | The tree, undo/redo, and the commands. Most of your work happens here. |
| `src/main/java/Layer.java`, `Command.java` | Internal node class and command interface. |
| `src/main/java/LayerInfo.java` | The public snapshot record. Don't change it. |
| `src/main/java/*Exception.java` | The existing exception types. |
| `src/main/java/Main.java` | Scratch space. Not graded. |
| `src/test/java/LayerTreeTest.java` | The existing tests. Add yours here or in a new `*Test.java` file. |
| `DESIGN.md` | Design review. Answer every question in this file. |
| `run-tests.sh` / `run-tests.bat`, `run-main.sh` / `run-main.bat` | Compile and run tests, or compile and run `Main`. |
| `pom.xml` | Lets IntelliJ or Maven open the exercise. |

## Running things

From this folder, with only a JDK installed (the scripts use the JUnit jars in `../lib`):

```
./run-tests.sh        # Windows: run-tests.bat
./run-main.sh         # Windows: run-main.bat
```

On macOS or Linux, run `chmod +x *.sh` once if the scripts won't start. Or open this folder in IntelliJ (**File → Open**, then trust the Maven project) and use the green arrows next to the tests.

Right now all 9 existing tests pass. That's the point: they don't cover the bugs.

## AI assistant

You may use an AI assistant. For this practice run, that's Claude in your chat. It will explain concepts, talk through tradeoffs, check your reasoning, help you track down a bug, and write a test that reproduces one. It will not write your fixes, your feature, or your design answers.

You are graded on what you submit, not on how much you use the assistant. You must be able to explain every part of your submission and the tradeoff behind it.

## Grading

| Category | What's evaluated |
|---|---|
| Coding | Fixes are correct, `duplicate` works, undo/redo stay consistent, runs cleanly |
| Problem solving | You fixed root causes, not symptoms |
| Reliability | Each bug has a regression test that fails without the fix, plus edge cases and invalid input |
| Design | Written answers about your own changes |
| Communication | Everything you wrote is consistent with your actual code |

## Submitting

When time is up, stop. Commit your work on a branch and push it:

```
git checkout -b solve/2026-09-30-layers-panel-undo
git add -A
git commit -m "Solve layers panel exercise"
git push -u origin solve/2026-09-30-layers-panel-undo
```

Then send the branch name in the chat. You can also zip this folder and send it instead.

Your code will be run against a hidden test suite. Your tests will be run against versions of the code with the bugs put back in, and with bugs planted in `duplicate`, to see which ones they catch.
