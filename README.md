# JavaFX Quiz Application

A university-style quiz application built with Java 17, JavaFX, Maven, Gson, and JUnit 5.

## Features

- JavaFX GUI only
- Inline styling using `setStyle()`
- Login for User or Admin
- Per-question countdown timer with color-changing progress bar
- Multiple Choice, True/False, and Fill in the Blank questions
- Manual Fisher-Yates shuffle
- Leaderboard using `TableView`
- Admin tools for creating users, banning users, resetting scores, and adding questions
- Questions loaded from JSON
- Results saved to a timestamped file in the `results` folder
- JUnit 5 tests for scoring, shuffle, answer checking, and file storage

## Default Accounts

- User: `student` / `1234`
- Admin: `admin` / `admin123`

## Project Structure

```text
src/main/java/com/quizapp
├── QuizApplication.java
├── model
├── service
├── ui
└── util

src/main/resources
└── questions.json

src/test/java/com/quizapp
└── tests
```

## Run

```bash
mvn clean test
mvn javafx:run
```

## Notes About the Design

- The UML model is respected through the core classes:
  - `Question` hierarchy
  - `Quiz`
  - `Answer`
  - `User` and `Admin`
  - `Leaderboard`
- File handling is separated into services.
- UI logic is centralized in the JavaFX application for simplicity.
- Manual shuffle is implemented with Fisher-Yates in `ShuffleService`.

## Data Files

- `data/questions.json` is created automatically from `src/main/resources/questions.json` on first run.
- `data/users.json` is created automatically with default users on first run.
- `results/` stores quiz reports.

## Edge Cases Handled

- Empty question file
- Missing question file
- Corrupted JSON file
- No answer submitted
- Timer auto-submit when time reaches zero


## Sign Up and Admin Passcode

- The login screen now includes a **Sign Up** button.
- New users can register as **User** or **Admin**.
- To create an **Admin** account, the passcode is: `00000`.
- Regular user sign up does not require an admin passcode.
