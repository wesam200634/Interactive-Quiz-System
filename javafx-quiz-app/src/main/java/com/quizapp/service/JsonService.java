package com.quizapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.quizapp.model.FillInTheBlankQuestion;
import com.quizapp.model.MultipleChoiceQuestion;
import com.quizapp.model.Question;
import com.quizapp.model.TrueFalseQuestion;
import com.quizapp.model.User;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles JSON file operations.
 */
public class JsonService {
    private final Gson gson;

    /**
     * Constructs the JSON service.
     */
    public JsonService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Ensures a runtime questions file exists by copying the default resource if needed.
     *
     * @param resourceName classpath resource name
     * @param target target file path
     * @throws IOException on I/O failure
     */
    public void ensureQuestionsFileExists(String resourceName, Path target) throws IOException {
        if (Files.exists(target)) {
            return;
        }
        Files.createDirectories(target.getParent());
        try (var input = getClass().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("Missing resource: " + resourceName);
            }
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Loads questions from JSON.
     *
     * @param file file path
     * @return loaded questions
     * @throws IOException on I/O failure
     */
    public ArrayList<Question> loadQuestions(Path file) throws IOException {
        if (!Files.exists(file) || Files.size(file) == 0L) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonArray()) {
                throw new IOException("questions.json must contain a JSON array.");
            }
            ArrayList<Question> questions = new ArrayList<>();
            JsonArray array = root.getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                String type = getString(object, "type");
                String questionText = getString(object, "question_text");
                String correctAns = getString(object, "correct_ans");
                double points = object.get("points").getAsDouble();
                long timeLimit = object.get("timeLimit").getAsLong();
                switch (type) {
                    case "MultipleChoice" -> {
                        List<String> choices = new ArrayList<>();
                        JsonArray choicesArray = object.getAsJsonArray("choices");
                        for (JsonElement choice : choicesArray) {
                            choices.add(choice.getAsString());
                        }
                        questions.add(new MultipleChoiceQuestion(questionText, correctAns, points, timeLimit, choices));
                    }
                    case "TrueFalse" -> questions.add(new TrueFalseQuestion(questionText, correctAns, points, timeLimit));
                    case "FillInTheBlank" -> questions.add(new FillInTheBlankQuestion(questionText, correctAns, points, timeLimit));
                    default -> throw new IOException("Unknown question type: " + type);
                }
            }
            return questions;
        } catch (JsonIOException | JsonSyntaxException ex) {
            throw new IOException("Failed to parse JSON: " + ex.getMessage(), ex);
        }
    }

    /**
     * Saves users to JSON.
     *
     * @param file target file
     * @param users users list
     * @throws IOException on I/O failure
     */
    public void saveUsers(Path file, List<User> users) throws IOException {
        Files.createDirectories(file.getParent());
        JsonArray array = new JsonArray();
        for (User user : users) {
            JsonObject object = new JsonObject();
            object.addProperty("role", user instanceof com.quizapp.model.Admin ? "Admin" : "User");
            object.addProperty("id", user.getId());
            object.addProperty("name", user.getName());
            object.addProperty("score", user.getScoreValue());
            object.addProperty("password", user.getPassword());
            object.addProperty("banned", user.isBanned());
            object.addProperty("banReason", user.getBanReason());
            array.add(object);
        }
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(array, writer);
        }
    }

    /**
     * Loads users from JSON.
     *
     * @param file source file
     * @return loaded users
     * @throws IOException on I/O failure
     */
    public ArrayList<User> loadUsers(Path file) throws IOException {
        if (!Files.exists(file) || Files.size(file) == 0L) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            ArrayList<User> users = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                String role = object.has("role") ? object.get("role").getAsString() : "User";
                User user = "Admin".equalsIgnoreCase(role)
                        ? new com.quizapp.model.Admin(object.get("id").getAsInt(), object.get("name").getAsString())
                        : new User(object.get("id").getAsInt(), object.get("name").getAsString());
                user.setScoreValue(object.get("score").getAsDouble());
                user.setPassword(object.get("password").getAsString());
                if (object.has("banned")) {
                    user.setBanned(object.get("banned").getAsBoolean());
                }
                if (object.has("banReason")) {
                    user.setBanReason(object.get("banReason").getAsString());
                }
                users.add(user);
            }
            return users;
        } catch (Exception ex) {
            throw new IOException("Failed to read users: " + ex.getMessage(), ex);
        }
    }

    /**
     * Saves text content to a file.
     *
     * @param file target file
     * @param content content text
     * @throws IOException on I/O failure
     */
    public void writeText(Path file, String content) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }

    /**
     * Reads text content from a file.
     *
     * @param file source file
     * @return file text
     * @throws IOException on I/O failure
     */
    public String readText(Path file) throws IOException {
        return Files.readString(file, StandardCharsets.UTF_8);
    }

    private String getString(JsonObject object, String key) {
        return object.has(key) ? object.get(key).getAsString() : "";
    }
}
