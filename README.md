# Daily SWE Practice

Project-style exercises modeled on AI-assisted CoderPad assessments. Each one is realistic engineering work: implement an API, fix bugs in an existing codebase, or build a small feature. Then you write a design review about your own code. The point is to practice what these assessments grade (correct behavior, edge cases, tests that actually prove it, and explaining your tradeoffs), not to memorize LeetCode patterns.

## Languages

| Folder | Exercises |
|---|---|
| [java/](java/) | 1 |

Each language folder has its own README with its list of exercises and setup instructions.

## How to do one

1. Pick an exercise from a language folder's README, open the exercise's `README.md`, and start a timer.
2. Start a chat with your AI assistant using [AGENT_SEED.md](AGENT_SEED.md) as its instructions. It explains, discusses tradeoffs, flags bugs and writes tests that reproduce them. It won't write your implementation or your design answers.
3. When time is up, commit your work to a branch named `solve/<exercise-folder>`, push it, and send the branch name to Claude for grading.

## How grading works

- **Hidden checks** run against your code, grouped by the same categories real assessments use.
- **Your tests** are run against versions of the code with bugs planted in them, to measure which bugs your tests would catch.
- **`DESIGN.md`** is read against your code, to check that what you wrote matches what you built.

The graders are kept outside this repo so they don't spoil the exercises.
