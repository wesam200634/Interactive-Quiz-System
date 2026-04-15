package com.quizapp.ui;

/**
 * Inline JavaFX styles.
 */
public final class UiStyles {
    public static final String ROOT = "-fx-background-color: linear-gradient(to bottom, #111827, #0f172a); -fx-padding: 24;";
    public static final String CARD = "-fx-background-color: #1f2937; -fx-background-radius: 16; -fx-padding: 24; -fx-border-color: #2563eb; -fx-border-radius: 16;";
    public static final String TITLE = "-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;";
    public static final String LABEL = "-fx-text-fill: #e5e7eb; -fx-font-size: 14px;";
    public static final String BUTTON = "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 18 10 18;";
    public static final String BUTTON_DANGER = "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 18 10 18;";
    public static final String FIELD = "-fx-background-color: #111827; -fx-text-fill: white; -fx-prompt-text-fill: #94a3b8; -fx-border-color: #334155; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;";
    public static final String TABLE = "-fx-background-color: #1f2937; -fx-control-inner-background: #1f2937; -fx-text-fill: white;";

    private UiStyles() {
    }

    public static String progressBarColor(String color) {
        return "-fx-accent: " + color + ";";
    }
}
