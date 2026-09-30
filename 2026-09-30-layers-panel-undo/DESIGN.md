# Design Review

Answer each question about **your** changes. Refer to your actual methods and tests by name. Wherever you show output, paste the real output from running your code, not output written from memory.

---

## 1. Root causes

For each of BUG-101 to BUG-105, give:

- the root cause: which method, and what exactly was wrong
- your fix
- the test that fails without your fix

For BUG-103, paste the real `render()` output before and after your fix.

_Your answer:_

---

## 2. Undo and redo

What must be true of every `Command` so that undo and redo stay correct, no matter how they're interleaved with new edits? Then explain how your `duplicate` is a single undoable step, and why redoing it gives the copies the same ids as before. Point to the code.

_Your answer:_

---

## 3. Failing halfway

BUG-105 happened because an edit changed state before finding out it was invalid. For every edit method (`add`, `rename`, `move`, `delete`, `duplicate`), say where it validates its input and where it first changes state. Is there any remaining way for an edit to throw after it has changed something? How do you know?

_Your answer:_

---

## 4. BUG-106 triage

Is BUG-106 a bug? What did you decide, what's the tradeoff, and which test covers it? If your change affects the redo stack, say how.

_Your answer:_

---

## 5. Scaling and multiplayer

Files now have 200,000 layers. Deleting a frame with 50,000 layers inside it must still be undoable, and each user's undo history keeps up to 1,000 steps. Two people also edit the same file at the same time. You move layer A into frame F, then a collaborator deletes F, then you press undo.

- Estimate the memory your current undo design uses for that big delete, and for a full history. Propose a concrete change, and show the data structures.
- Say what your undo should do in the collaborator scenario, and how your `Command` design would have to change to do it.
- Name at least two risks of your proposal and how you'd handle them.

A general, high-level answer here is graded the same as a blank one.

_Your answer:_

---

## Anything unfinished?

If any part of your submission is incomplete or you know it doesn't work, say so here.

_Your answer:_
