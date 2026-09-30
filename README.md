# Daily SWE Practice

Project-style exercises modeled on AI-assisted CoderPad assessments. Each one is realistic engineering work: implement an API, fix bugs in an existing codebase, or build a small feature. Then you write a design review about your own code. The point is to practice what these assessments grade (correct behavior, edge cases, tests that actually prove it, and explaining your tradeoffs), not to memorize LeetCode patterns.

## Exercises

| Date | Exercise | Type | Time |
|---|---|---|---|
| 2026-09-30 | [Layers panel: fix bugs, add a feature](2026-09-30-layers-panel-undo/) | Bug fixes and a feature in an existing codebase (layer tree, undo/redo) | 100 min |

## How to do one

1. Open the exercise's `README.md` and start a timer.
2. Use Claude as the AI assistant. It will explain, discuss tradeoffs, help find bugs and write tests that reproduce them. It won't write your implementation or your design answers.
3. When time is up, commit your work to a branch named `solve/<exercise-folder>`, push it, and send the branch name to Claude for grading.

## How grading works

- **Hidden checks** run against your code, grouped by the same categories real assessments use.
- **Your tests** are run against versions of the code with bugs planted in them, to measure which bugs your tests would catch.
- **`DESIGN.md`** is read against your code, to check that what you wrote matches what you built.

The graders are kept outside this repo so they don't spoil the exercises.

## Setup

You need JDK 17 or newer. `lib/` holds JUnit 4.13.2 and Hamcrest 1.3, which the run scripts use, so no build tool is needed. Each exercise also has a `pom.xml` if you'd rather open it in IntelliJ or run it with Maven.
