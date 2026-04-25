package com.quizapp;

import com.quizapp.model.Admin;
import com.quizapp.model.Answer;
import com.quizapp.model.FillInTheBlankQuestion;
import com.quizapp.model.Leaderboard;
import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.model.TrueFalseQuestion;
import com.quizapp.model.User;
import com.quizapp.service.QuizDataService;
import com.quizapp.service.ResultService;
import com.quizapp.service.ShuffleService;
import com.quizapp.service.UserService;
import com.quizapp.ui.UiStyles;
import com.quizapp.util.AppPaths;
import com.quizapp.util.DialogUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuizApplication extends Application{
    private static final int WINDOW_WIDTH = 1000;
    
    private static final int WINDOW_HEIGHT = 700;
    
    private static final String ADMIN_SIGNUP_PASSCODE = "00000";

    private Stage stage;
    
    private UserService userService;
    
   
    private QuizDataService quizDataService;    
    
    private ResultService resultService;
    
    private Leaderboard leaderboard;

    private User currentUser;
    
    private Quiz currentQuiz;
    
    private int currentQuestionIndex;
    
    private Timeline questionTimeline;
    
    private long secondsRemaining;
    
    private Instant quizStartTime;

    @Override
    public void start(Stage primaryStage){
        
        this.stage = primaryStage;
        
       stage.setTitle("JavaFX Quiz Application");
       
        try {
            Files.createDirectories(AppPaths.DATA_DIR);
            
            Files.createDirectories(AppPaths.RESULTS_DIR);
            
            this.quizDataService = new QuizDataService(AppPaths.QUESTIONS_FILE);
            
            quizDataService.initialize();
            
            this.userService = new UserService(AppPaths.USERS_FILE);
            
            this.resultService = new ResultService();
            
            this.leaderboard = new Leaderboard();
            
            refreshLeaderboard();
            
            showLoginScreen();
            
            stage.show();
            
        } catch (IOException ex) {
            DialogUtil.error("Startup Error", ex.getMessage());
        }
    }

    private void refreshLeaderboard() {
        
        leaderboard.setUsers(new ArrayList<>(userService.getUsers()));
        
        leaderboard.displayLeaderboard();
        
    }

    private VBox createCardContainer() {
        
        VBox card = new VBox(15);
        
        card.setPadding(new Insets(24));
        
        card.setAlignment(Pos.CENTER_LEFT);
        
        card.setStyle(UiStyles.CARD);
        
        return card;
        
    }

    private void installEscapeShortcut(Scene scene, Runnable action) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> action.run();
                default -> {
                }
            }
        });
    }

    private void showLoginScreen() {
        
        Label title = new Label("Quiz Login");
        
        title.setStyle(UiStyles.TITLE);

        TextField nameField = new TextField();
        
        nameField.setPromptText("Enter username");
        
        nameField.setStyle(UiStyles.FIELD);

        PasswordField passwordField = new PasswordField();
        
        passwordField.setPromptText("Enter password");
        
        passwordField.setStyle(UiStyles.FIELD);

        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("User", "Admin"));
        
        roleBox.getSelectionModel().selectFirst();
        
        roleBox.setStyle(UiStyles.FIELD);

        Button loginButton = new Button("Login");
        
        loginButton.setStyle(UiStyles.BUTTON);
        
        loginButton.setOnAction(e -> {
            User user = userService.authenticate(nameField.getText().trim(), passwordField.getText(), "Admin".equals(roleBox.getValue()));
            if (user == null) {
                DialogUtil.error("Login Failed", "Invalid credentials, wrong role, or banned user.");
                return;
            }
            currentUser = user;
            if (currentUser instanceof Admin) {
                showAdminPanel();
            } else {
                showMainMenu();
            }
        });

        Button signUpButton = new Button("Sign Up");
       
        signUpButton.setStyle(UiStyles.BUTTON);
        
        signUpButton.setOnAction(e -> showSignUpScreen());

        VBox card = createCardContainer();

        card.getChildren().addAll(title, labeledField("Username", nameField), labeledField("Password", passwordField), labeledField("Role", roleBox), new HBox(10, loginButton, signUpButton));

        VBox root = new VBox(card);
     
        root.setAlignment(Pos.CENTER);
        
        root.setStyle(UiStyles.ROOT);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
     
        scene.setOnKeyPressed(event -> {
        
            switch (event.getCode()) {
            
                case ENTER -> loginButton.fire();
                case ESCAPE -> stage.close();
                default -> {
                }
            }
        });
        stage.setScene(scene);
    }

    private VBox labeledField(String labelText, javafx.scene.Node field) {
        Label label = new Label(labelText);
        label.setStyle(UiStyles.LABEL);
        VBox box = new VBox(6, label, field);
        return box;
    }


    private void showSignUpScreen() {
        Label title = new Label("Create Account");
        title.setStyle(UiStyles.TITLE);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter username");
        nameField.setStyle(UiStyles.FIELD);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UiStyles.FIELD);

        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("User", "Admin"));
        roleBox.getSelectionModel().selectFirst();
        roleBox.setStyle(UiStyles.FIELD);

        PasswordField adminCodeField = new PasswordField();
        adminCodeField.setPromptText("Enter admin passcode");
        adminCodeField.setStyle(UiStyles.FIELD);
        adminCodeField.setVisible(false);
        adminCodeField.setManaged(false);

        roleBox.setOnAction(e -> {
            boolean isAdmin = "Admin".equals(roleBox.getValue());
            adminCodeField.setVisible(isAdmin);
            adminCodeField.setManaged(isAdmin);
        });

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setStyle(UiStyles.BUTTON);
        createAccountButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            boolean isAdmin = "Admin".equals(roleBox.getValue());

            if (name.isEmpty() || password.isEmpty()) {
                DialogUtil.error("Validation Error", "Username and password are required.");
                return;
            }

            boolean exists = userService.getUsers().stream().anyMatch(u -> u.getName().equalsIgnoreCase(name));
            if (exists) {
                DialogUtil.error("Duplicate User", "This username already exists.");
                return;
            }

            if (isAdmin && !ADMIN_SIGNUP_PASSCODE.equals(adminCodeField.getText().trim())) {
                DialogUtil.error("Access Denied", "Invalid admin passcode.");
                return;
            }

            User newUser = isAdmin ? new Admin(userService.nextId(), name) : new User(userService.nextId(), name);
            newUser.setPassword(password);
            userService.addUser(newUser);

            try {
                userService.save();
                refreshLeaderboard();
                DialogUtil.info("Success", "Account created successfully. You can now log in.");
                showLoginScreen();
            } catch (IOException ex) {
                DialogUtil.error("Save Error", ex.getMessage());
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle(UiStyles.BUTTON_DANGER);
        backButton.setOnAction(e -> showLoginScreen());

        VBox card = createCardContainer();
        card.getChildren().addAll(
                title,
                labeledField("Username", nameField),
                labeledField("Password", passwordField),
                labeledField("Role", roleBox),
                labeledField("Admin Passcode", adminCodeField),
                new HBox(10, createAccountButton, backButton)
        );

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> createAccountButton.fire();
                case ESCAPE -> backButton.fire();
                default -> {
                }
            }
        });
        stage.setScene(scene);
    }

    private void showMainMenu() {
        Label title = new Label("Welcome, " + currentUser.getName());
        title.setStyle(UiStyles.TITLE);

        Button startQuiz = new Button("Start Quiz");
        startQuiz.setStyle(UiStyles.BUTTON);
        startQuiz.setOnAction(e -> startQuizFlow());

        Button leaderboardButton = new Button("View Leaderboard");
        leaderboardButton.setStyle(UiStyles.BUTTON);
        leaderboardButton.setOnAction(e -> showLeaderboardScreen(false));

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(UiStyles.BUTTON_DANGER);
        logoutButton.setOnAction(e -> logoutToLogin(false));

        Button exitButton = new Button("Exit Application");
        exitButton.setStyle(UiStyles.BUTTON_DANGER);
        exitButton.setOnAction(e -> {
            if (DialogUtil.confirm("Exit Application", "Do you want to close the application?")) {
                stopTimer();
                stage.close();
            }
        });

        VBox card = createCardContainer();
        card.setAlignment(Pos.CENTER);
        card.getChildren().addAll(title, startQuiz, leaderboardButton, logoutButton, exitButton);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void startQuizFlow() {
        try {
            ArrayList<Question> questions = quizDataService.loadQuestions();
            if (questions.isEmpty()) {
                DialogUtil.info("No Questions", "questions.json is empty. Add questions first.");
                return;
            }
            ShuffleService.shuffle(questions);
            for (Question question : questions) {
                question.shuffleQuestions();
            }
            currentQuiz = new Quiz(currentUser, "Java Programming Quiz");
            currentQuiz.setQuestions(questions);
            ArrayList<Answer> answers = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                answers.add(new Answer());
            }
            currentQuiz.setAnswers(answers);
            currentQuestionIndex = 0;
            quizStartTime = Instant.now();
            showQuizScreen();
        } catch (IOException ex) {
            DialogUtil.error("Load Error", ex.getMessage());
        }
    }

    
private void showQuizScreen() {
        if (currentQuestionIndex >= currentQuiz.getQuestions().size()) {
            finishQuiz();
            return;
        }
        Question question = currentQuiz.getQuestions().get(currentQuestionIndex);
        Label numberLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + currentQuiz.getQuestions().size());
        numberLabel.setStyle(UiStyles.LABEL);

        Label questionLabel = new Label(question.getQuestion_text());
        questionLabel.setWrapText(true);
        questionLabel.setStyle(UiStyles.TITLE + "-fx-font-size: 20px;");

        Label timerLabel = new Label();
        timerLabel.setStyle(UiStyles.LABEL);

        ProgressBar progressBar = new ProgressBar(1.0);
        progressBar.setPrefWidth(600);
        progressBar.setStyle(UiStyles.progressBarColor("#22c55e"));

        VBox answerBox = new VBox(10);
        answerBox.setAlignment(Pos.CENTER_LEFT);
        Answer answer = currentQuiz.getAnswerObjects().get(currentQuestionIndex);

        if (question instanceof MultipleChoiceQuestion mcq) {
            ToggleGroup group = new ToggleGroup();
            for (String choice : mcq.getChoices()) {
                RadioButton radioButton = new RadioButton(choice);
                radioButton.setStyle(UiStyles.LABEL);
                radioButton.setToggleGroup(group);
                if (choice.equals(answer.getResponse())) {
                    radioButton.setSelected(true);
                }
                answerBox.getChildren().add(radioButton);
            }
            group.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue != null) {
                    answer.setResponse(((RadioButton) newValue).getText());
                } else {
                    answer.setResponse("");
                }
            });
        } else if (question instanceof TrueFalseQuestion) {
            ToggleGroup group = new ToggleGroup();
            ToggleButton trueButton = new ToggleButton("True");
            ToggleButton falseButton = new ToggleButton("False");
            applyToggleStyle(trueButton, false);
            applyToggleStyle(falseButton, false);
            trueButton.setToggleGroup(group);
            falseButton.setToggleGroup(group);

            if ("true".equalsIgnoreCase(answer.getResponse())) {
                trueButton.setSelected(true);
            } else if ("false".equalsIgnoreCase(answer.getResponse())) {
                falseButton.setSelected(true);
            }

            applyToggleStyle(trueButton, trueButton.isSelected());
            applyToggleStyle(falseButton, falseButton.isSelected());

            trueButton.setOnAction(e -> {
                if (trueButton.isSelected()) {
                    falseButton.setSelected(false);
                    answer.setResponse("True");
                } else {
                    answer.setResponse("");
                }
                applyToggleStyle(trueButton, trueButton.isSelected());
                applyToggleStyle(falseButton, falseButton.isSelected());
            });

            falseButton.setOnAction(e -> {
                if (falseButton.isSelected()) {
                    trueButton.setSelected(false);
                    answer.setResponse("False");
                } else {
                    answer.setResponse("");
                }
                applyToggleStyle(trueButton, trueButton.isSelected());
                applyToggleStyle(falseButton, falseButton.isSelected());
            });

            group.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == null) {
                    answer.setResponse("");
                } else if (newValue == trueButton) {
                    answer.setResponse("True");
                } else if (newValue == falseButton) {
                    answer.setResponse("False");
                }
                applyToggleStyle(trueButton, trueButton.isSelected());
                applyToggleStyle(falseButton, falseButton.isSelected());
            });

            answerBox.getChildren().add(new HBox(10, trueButton, falseButton));
        } else if (question instanceof FillInTheBlankQuestion) {
            TextField field = new TextField(answer.getResponse());
            field.setPromptText("Type your answer here");
            field.setStyle(UiStyles.FIELD);
            field.textProperty().addListener((obs, oldValue, newValue) -> answer.setResponse(newValue));
            answerBox.getChildren().add(field);
        }

        Button nextButton = new Button(currentQuestionIndex == currentQuiz.getQuestions().size() - 1 ? "Submit Quiz" : "Next Question");
        nextButton.setStyle(UiStyles.BUTTON);
        nextButton.setOnAction(e -> {
            stopTimer();
            currentQuestionIndex++;
            showQuizScreen();
        });

        Button quitQuizButton = new Button("Quit Quiz");
        quitQuizButton.setStyle(UiStyles.BUTTON_DANGER);
        quitQuizButton.setOnAction(e -> quitQuizToMenu());

        Button signOutButton = new Button("Sign Out");
        signOutButton.setStyle(UiStyles.BUTTON_DANGER);
        signOutButton.setOnAction(e -> logoutToLogin(true));

        VBox card = createCardContainer();
        card.getChildren().addAll(numberLabel, questionLabel, timerLabel, progressBar, answerBox, new HBox(10, nextButton, quitQuizButton, signOutButton));

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        installEscapeShortcut(scene, () -> quitQuizButton.fire());
        stage.setScene(scene);

        startTimer(question.getTimeLimit(), timerLabel, progressBar);
    }

    private void startTimer(long seconds, Label timerLabel, ProgressBar progressBar) {
        stopTimer();
        secondsRemaining = seconds;
        updateTimerUi(timerLabel, progressBar, seconds, secondsRemaining);
        questionTimeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            secondsRemaining--;
            updateTimerUi(timerLabel, progressBar, seconds, secondsRemaining);
            if (secondsRemaining <= 0) {
                stopTimer();
                currentQuestionIndex++;
                showQuizScreen();
            }
        }));
        questionTimeline.setCycleCount(Timeline.INDEFINITE);
        questionTimeline.play();
    }

    private void updateTimerUi(Label timerLabel, ProgressBar progressBar, long totalSeconds, long remaining) {
        timerLabel.setText("Time Left: " + Math.max(remaining, 0) + " seconds");
        double progress = totalSeconds == 0 ? 0 : Math.max(0.0, (double) remaining / totalSeconds);
        progressBar.setProgress(progress);
        if (progress > 0.6) {
            progressBar.setStyle(UiStyles.progressBarColor("#22c55e"));
        } else if (progress > 0.3) {
            progressBar.setStyle(UiStyles.progressBarColor("#eab308"));
        } else {
            progressBar.setStyle(UiStyles.progressBarColor("#ef4444"));
        }
    }

    private void stopTimer() {
        if (questionTimeline != null) {
            questionTimeline.stop();
        }
    }

    private void finishQuiz() {
        stopTimer();
        double score = currentQuiz.checkAnswers();
        currentUser.setScoreValue(score);
        try {
            userService.save();
            refreshLeaderboard();
            long elapsed = Duration.between(quizStartTime, Instant.now()).getSeconds();
            resultService.saveResult(AppPaths.RESULTS_DIR, currentQuiz, elapsed);
            showResultsScreen(elapsed);
        } catch (IOException ex) {
            DialogUtil.error("Save Error", ex.getMessage());
        }
    }

    private void showResultsScreen(long elapsedSeconds) {
        double score = currentQuiz.checkAnswers();
        double total = currentQuiz.getQuestions().stream().mapToDouble(Question::getPoints).sum();
        double percentage = total == 0 ? 0 : (score / total) * 100.0;
        String grade = percentage >= 90 ? "A" : percentage >= 80 ? "B" : percentage >= 70 ? "C" : percentage >= 60 ? "D" : "F";

        Label title = new Label("Quiz Results");
        title.setStyle(UiStyles.TITLE);
        Label summary = new Label(String.format("Score: %.2f / %.2f\nPercentage: %.2f%%\nGrade: %s\nTime Taken: %d seconds", score, total, percentage, grade, elapsedSeconds));
        summary.setStyle(UiStyles.LABEL + "-fx-font-size: 16px;");

        VBox reviewBox = new VBox(10);
        for (int i = 0; i < currentQuiz.getQuestions().size(); i++) {
            Question question = currentQuiz.getQuestions().get(i);
            Answer answer = currentQuiz.getAnswerObjects().get(i);
            boolean correct = question.checkAnswer(answer);
            Label item = new Label((i + 1) + ". " + question.getQuestion_text() + "\nYour Answer: " + answer.getResponse() + "\nCorrect Answer: " + question.getCorrect_ans() + "\nResult: " + (correct ? "Correct" : "Wrong"));
            item.setWrapText(true);
            item.setStyle((correct ? "-fx-text-fill: #86efac;" : "-fx-text-fill: #fca5a5;") + "-fx-font-size: 14px;");
            reviewBox.getChildren().add(item);
        }
        ScrollPane reviewScroll = new ScrollPane(reviewBox);
        reviewScroll.setFitToWidth(true);
        reviewScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button backButton = new Button("Back to Menu");
        backButton.setStyle(UiStyles.BUTTON);
        backButton.setOnAction(e -> showMainMenu());

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(UiStyles.BUTTON_DANGER);
        logoutButton.setOnAction(e -> logoutToLogin(false));

        VBox card = createCardContainer();
        card.getChildren().addAll(title, summary, reviewScroll, new HBox(10, backButton, logoutButton));
        VBox.setVgrow(reviewScroll, Priority.ALWAYS);

        VBox root = new VBox(card);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(UiStyles.ROOT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        installEscapeShortcut(scene, () -> backButton.fire());
        stage.setScene(scene);
    }

    private void showLeaderboardScreen(boolean adminBack) {
        refreshLeaderboard();
        Label title = new Label("Leaderboard");
        title.setStyle(UiStyles.TITLE);

        TableView<User> table = new TableView<>();
        table.setStyle(UiStyles.TABLE);
        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<User, String> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getScore()));
        table.getColumns().setAll(List.of(idColumn, nameColumn, scoreColumn));
        table.setItems(FXCollections.observableArrayList(leaderboard.getUsers()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Button backButton = new Button("Back");
        backButton.setStyle(UiStyles.BUTTON);
        backButton.setOnAction(e -> {
            if (adminBack) {
                showAdminPanel();
            } else {
                showMainMenu();
            }
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(UiStyles.BUTTON_DANGER);
        logoutButton.setOnAction(e -> logoutToLogin(false));

        VBox card = createCardContainer();
        card.getChildren().addAll(title, table, new HBox(10, backButton, logoutButton));
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        installEscapeShortcut(scene, () -> backButton.fire());
        stage.setScene(scene);
    }

    private void showAdminPanel() {
        Label title = new Label("Admin Panel - " + currentUser.getName());
        title.setStyle(UiStyles.TITLE);

        Button createQuizButton = new Button("Create Quiz");
        Button addQuestionButton = new Button("Add Question");
        Button createUserButton = new Button("Create User");
        Button banUserButton = new Button("Ban User");
        Button resetScoreButton = new Button("Reset Score");
        Button publishQuizButton = new Button("Publish Quiz");
        Button viewLeaderboardButton = new Button("View Leaderboard");
        Button logoutButton = new Button("Logout");
        for (Button button : List.of(createQuizButton, addQuestionButton, createUserButton, banUserButton, resetScoreButton, publishQuizButton, viewLeaderboardButton)) {
            button.setStyle(UiStyles.BUTTON);
        }
        logoutButton.setStyle(UiStyles.BUTTON_DANGER);

        createQuizButton.setOnAction(e -> DialogUtil.info("Create Quiz", "This demo creates the runtime quiz from questions.json. Use Add Question and Publish Quiz to manage content."));
        publishQuizButton.setOnAction(e -> DialogUtil.info("Publish Quiz", "Questions are already published to data/questions.json for user sessions."));
        addQuestionButton.setOnAction(e -> showAddQuestionScreen());
        createUserButton.setOnAction(e -> showCreateUserScreen());
        banUserButton.setOnAction(e -> showBanUserScreen());
        resetScoreButton.setOnAction(e -> showResetScoreScreen());
        viewLeaderboardButton.setOnAction(e -> showLeaderboardScreen(true));
        logoutButton.setOnAction(e -> logoutToLogin(false));

        VBox card = createCardContainer();
        card.setAlignment(Pos.CENTER);
        card.getChildren().addAll(title, createQuizButton, addQuestionButton, createUserButton, banUserButton, resetScoreButton, publishQuizButton, viewLeaderboardButton, logoutButton);

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        stage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void showCreateUserScreen() {
        TextField nameField = new TextField();
        nameField.setStyle(UiStyles.FIELD);
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(UiStyles.FIELD);
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("User", "Admin"));
        roleBox.getSelectionModel().selectFirst();
        roleBox.setStyle(UiStyles.FIELD);
        Button createButton = new Button("Create User");
        createButton.setStyle(UiStyles.BUTTON);
        createButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText();
            if (name.isEmpty() || password.isEmpty()) {
                DialogUtil.error("Validation", "Name and password are required.");
                return;
            }
            User user = "Admin".equals(roleBox.getValue()) ? new Admin(userService.nextId(), name) : new User(userService.nextId(), name);
            user.setPassword(password);
            userService.addUser(user);
            try {
                userService.save();
                refreshLeaderboard();
                DialogUtil.info("Success", "User created successfully.");
                showAdminPanel();
            } catch (IOException ex) {
                DialogUtil.error("Save Error", ex.getMessage());
            }
        });
        showSimpleForm("Create User", List.of(labeledField("Name", nameField), labeledField("Password", passwordField), labeledField("Role", roleBox)), createButton, this::showAdminPanel);
    }

    private void showBanUserScreen() {
        ComboBox<User> userBox = new ComboBox<>(FXCollections.observableArrayList(userService.getUsers().stream().filter(u -> !(u instanceof Admin)).toList()));
        userBox.setStyle(UiStyles.FIELD);
        userBox.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(User item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getName()); }
        });
        userBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(User item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getName()); }
        });
        TextField reasonField = new TextField();
        reasonField.setStyle(UiStyles.FIELD);
        Button banButton = new Button("Ban User");
        banButton.setStyle(UiStyles.BUTTON_DANGER);
        banButton.setOnAction(e -> {
            User selected = userBox.getValue();
            if (selected == null) {
                DialogUtil.error("Validation", "Please select a user.");
                return;
            }
            selected.setBanned(true);
            selected.setBanReason(reasonField.getText());
            try {
                userService.save();
                DialogUtil.info("Success", "User banned successfully.");
                showAdminPanel();
            } catch (IOException ex) {
                DialogUtil.error("Save Error", ex.getMessage());
            }
        });
        showSimpleForm("Ban User", List.of(labeledField("User", userBox), labeledField("Reason", reasonField)), banButton, this::showAdminPanel);
    }

    private void showResetScoreScreen() {
        ComboBox<User> userBox = new ComboBox<>(FXCollections.observableArrayList(userService.getUsers()));
        userBox.setStyle(UiStyles.FIELD);
        userBox.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(User item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getName()); }
        });
        userBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(User item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getName()); }
        });
        Button resetButton = new Button("Reset Score");
        resetButton.setStyle(UiStyles.BUTTON);
        resetButton.setOnAction(e -> {
            User selected = userBox.getValue();
            if (selected == null) {
                DialogUtil.error("Validation", "Please select a user.");
                return;
            }
            selected.setScoreValue(0.0);
            try {
                userService.save();
                refreshLeaderboard();
                DialogUtil.info("Success", "Score reset successfully.");
                showAdminPanel();
            } catch (IOException ex) {
                DialogUtil.error("Save Error", ex.getMessage());
            }
        });
        showSimpleForm("Reset Score", List.of(labeledField("User", userBox)), resetButton, this::showAdminPanel);
    }

    
private void showAddQuestionScreen() {
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList("MultipleChoice", "TrueFalse", "FillInTheBlank"));
        typeBox.getSelectionModel().selectFirst();
        typeBox.setStyle(UiStyles.FIELD);

        TextArea questionArea = new TextArea();
        questionArea.setStyle(UiStyles.TEXT_AREA);
        questionArea.setPrefRowCount(3);
        questionArea.setPromptText("Enter the full question text");

        TextField correctField = new TextField();
        correctField.setStyle(UiStyles.FIELD);
        correctField.setPromptText("Enter the correct answer");

        TextField pointsField = new TextField("1");
        pointsField.setStyle(UiStyles.FIELD);

        TextField timeField = new TextField("20");
        timeField.setStyle(UiStyles.FIELD);

        TextField choicesField = new TextField();
        choicesField.setPromptText("Only for MCQ: comma separated choices");
        choicesField.setStyle(UiStyles.FIELD);

        VBox choicesBox = labeledField("Choices", choicesField);
        choicesBox.setManaged(true);
        choicesBox.setVisible(true);

        typeBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean isMcq = "MultipleChoice".equals(newValue);
            choicesBox.setManaged(isMcq);
            choicesBox.setVisible(isMcq);
            if ("TrueFalse".equals(newValue)) {
                correctField.setPromptText("Enter True or False");
            } else if ("FillInTheBlank".equals(newValue)) {
                correctField.setPromptText("Enter the exact correct answer");
            } else {
                correctField.setPromptText("Enter the correct choice exactly as written");
            }
        });

        Button addButton = new Button("Add Question");
        addButton.setStyle(UiStyles.BUTTON);
        addButton.setOnAction(e -> {
            try {
                ArrayList<Question> questions = quizDataService.loadQuestions();
                String type = typeBox.getValue();
                String questionText = questionArea.getText().trim();
                String correctAnswer = correctField.getText().trim();
                String pointsText = pointsField.getText().trim();
                String timeText = timeField.getText().trim();

                if (questionText.isEmpty() || correctAnswer.isEmpty() || pointsText.isEmpty() || timeText.isEmpty()) {
                    DialogUtil.error("Validation Error", "Please fill in all required fields.");
                    return;
                }

                double points = Double.parseDouble(pointsText);
                long timeLimit = Long.parseLong(timeText);
                if (!Double.isFinite(points) || points <= 0.0) {
                    DialogUtil.error("Validation Error", "Points must be a positive number.");
                    return;
                }
                if (timeLimit <= 0L) {
                    DialogUtil.error("Validation Error", "Time limit must be a positive whole number of seconds.");
                    return;
                }

                Question question;
                if ("MultipleChoice".equals(type)) {
                    List<String> choices = new ArrayList<>();
                    for (String item : choicesField.getText().split(",")) {
                        String trimmed = item.trim();
                        if (!trimmed.isEmpty()) {
                            choices.add(trimmed);
                        }
                    }
                    if (choices.size() < 2) {
                        DialogUtil.error("Validation Error", "A multiple choice question needs at least two choices.");
                        return;
                    }
                    boolean correctIncluded = choices.stream().anyMatch(c -> c.equalsIgnoreCase(correctAnswer));
                    if (!correctIncluded) {
                        DialogUtil.error("Validation Error", "For multiple choice, the correct answer must match one of the choices.");
                        return;
                    }
                    question = new MultipleChoiceQuestion(questionText, correctAnswer, points, timeLimit, choices);
                } else if ("TrueFalse".equals(type)) {
                    if (!"true".equalsIgnoreCase(correctAnswer) && !"false".equalsIgnoreCase(correctAnswer)) {
                        DialogUtil.error("Validation Error", "For a True/False question, the correct answer must be True or False.");
                        return;
                    }
                    question = new TrueFalseQuestion(questionText, correctAnswer, points, timeLimit);
                } else {
                    question = new FillInTheBlankQuestion(questionText, correctAnswer, points, timeLimit);
                }

                questions.add(question);
                saveQuestionsToFile(questions);
                DialogUtil.info("Success", "Question added successfully.");
                showAdminPanel();
            } catch (NumberFormatException ex) {
                DialogUtil.error("Validation Error", "Points must be a number and time limit must be a whole number.");
            } catch (Exception ex) {
                DialogUtil.error("Error", "Could not add question: " + ex.getMessage());
            }
        });

        showSimpleForm(
                "Add Question",
                List.of(
                        labeledField("Type", typeBox),
                        labeledField("Question Text", questionArea),
                        labeledField("Correct Answer", correctField),
                        labeledField("Points", pointsField),
                        labeledField("Time Limit (seconds)", timeField),
                        choicesBox
                ),
                addButton,
                this::showAdminPanel
        );
    }

    private void saveQuestionsToFile(ArrayList<Question> questions) throws IOException {
        quizDataService.saveQuestions(questions);
    }

    private void showSimpleForm(String titleText, List<VBox> fields, Button primaryButton, Runnable backAction) {
        Label title = new Label(titleText);
        title.setStyle(UiStyles.TITLE);
        Button backButton = new Button("Back");
        backButton.setStyle(UiStyles.BUTTON);
        backButton.setOnAction(e -> backAction.run());
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(UiStyles.BUTTON_DANGER);
        logoutButton.setOnAction(e -> logoutToLogin(false));
        VBox card = createCardContainer();
        card.getChildren().add(title);
        card.getChildren().addAll(fields);
        card.getChildren().add(new HBox(10, primaryButton, backButton, logoutButton));
        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle(UiStyles.ROOT);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> primaryButton.fire();
                case ESCAPE -> backButton.fire();
                default -> {
                }
            }
        });
        stage.setScene(scene);
    }

    private void logoutToLogin(boolean quizInProgress) {
        String message = quizInProgress
                ? "Logging out now will discard the current quiz attempt. Continue?"
                : "Do you want to sign out and return to the login screen?";
        if (!DialogUtil.confirm("Logout", message)) {
            return;
        }
        stopTimer();
        currentQuiz = null;
        currentQuestionIndex = 0;
        quizStartTime = null;
        currentUser = null;
        showLoginScreen();
    }

    private void applyToggleStyle(ToggleButton button, boolean selected) {
        button.setStyle(selected ? UiStyles.TOGGLE_BUTTON_SELECTED : UiStyles.TOGGLE_BUTTON);
    }

    private void quitQuizToMenu() {
        if (!DialogUtil.confirm("Quit Quiz", "Do you want to leave this quiz and return to the main menu? Your current quiz attempt will be discarded.")) {
            return;
        }
        stopTimer();
        currentQuiz = null;
        currentQuestionIndex = 0;
        quizStartTime = null;
        showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
