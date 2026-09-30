# Tickets

Every example below starts from this tree, the same one the existing tests and `Main` build:

```
Page (root)
  Header (header)
    Logo (logo)
    Nav (nav)
  Body (body)
```

---

## BUG-101: Moving a group into one of its own children makes it vanish

**Steps**

```java
tree.move("header", "logo", 0);   // logo is inside header
```

**Expected:** an `InvalidOperationException`. A layer can't be moved into itself or into anything inside it.

**Actual:** no error. Header and everything inside it disappear from `render()`, but `exists("header")` still returns `true`. Undo brings it back.

```
Page (root)
  Body (body)
```

---

## BUG-102: Deleted layers leave ghosts behind

**Steps**

```java
tree.delete("header");   // header contains logo and nav
```

**Expected:** logo and nav are gone along with header.

**Actual:**

- `exists("logo")` returns `true`.
- `add("body", "logo", "New logo")` throws `InvalidOperationException: a layer with id 'logo' already exists`.
- `get("nav")` still returns `LayerInfo[id=nav, name=Nav, parentId=header, childIds=[]]`.

---

## BUG-103: Undoing a delete restores the wrong thing

**Steps**

```java
tree.delete("header");
tree.undo();
```

**Expected:** the tree is exactly as it was before the delete. Header is back in its original position, with logo and nav inside it.

**Actual:** header comes back at the bottom, and empty.

```
Page (root)
  Body (body)
  Header (header)
```

---

## BUG-104: Redo re-applies an edit I already replaced

**Steps**

```java
tree.rename("logo", "Brand");
tree.undo();
tree.rename("nav", "Menu");
tree.redo();
```

**Expected:** `redo()` returns `false`. Once you make a new edit, the edits you undid can't be redone.

**Actual:** `redo()` returns `true` and renames logo to "Brand" again.

---

## BUG-105: A failed move loses the layer, and undo then crashes

**Steps**

```java
tree.move("nav", "body", 5);   // body has no children, so 5 is out of range
tree.undo();
```

**Expected:** the move throws `InvalidOperationException` and nothing else changes. The following `undo()` undoes whatever edit came before the failed move.

**Actual:** the move throws the right error, but nav is gone from `render()`. Then `undo()` throws:

```
java.lang.NullPointerException: Cannot read field "children" because "<parameter1>.parent" is null
```

**Note from the tech lead:** find out whether any other edit can fail halfway through in the same way.

---

## BUG-106: Renaming to the same name adds an undo step (needs triage)

**Report:** "If I rename a layer to the name it already has, I have to press undo an extra time to get back."

**Steps**

```java
tree.rename("logo", "Brand");
tree.rename("logo", "Brand");
tree.undo();   // still "Brand"
tree.undo();   // now "Logo"
```

**Your call:** decide whether this is a bug. If you change the behavior, cover it with a test. Either way, explain your decision in `DESIGN.md`.

---

## FEAT-201: Duplicate a layer

Implement `String duplicate(String id)` in `LayerTree`.

- It copies the layer and everything inside it. The copy goes directly after the original, in the same parent.
- The copy of the top layer is named `"<original name> copy"`. Layers inside it keep their names.
- Every copied layer gets a new id: its original id plus `-copy`. If that id is taken, use `-copy-2`, then `-copy-3`, and so on. Assign ids in outline order: a layer before its children, children top to bottom. An id counts as taken as soon as it's assigned.
- It returns the id of the top copy.
- Duplicating the root isn't allowed (`InvalidOperationException`). An unknown id throws `LayerNotFoundException`.
- The whole duplicate is one undoable step. Undo removes the entire copy, and redo brings it back with the same ids.

**Example**

```java
tree.duplicate("header");   // returns "header-copy"
```

```
Page (root)
  Header (header)
    Logo (logo)
    Nav (nav)
  Header copy (header-copy)
    Logo (logo-copy)
    Nav (nav-copy)
  Body (body)
```

Calling `tree.duplicate("header")` again returns `"header-copy-2"`, and its children are `logo-copy-2` and `nav-copy-2`. The new copy goes directly after the original header, so above `header-copy`.
