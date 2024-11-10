package com.flipkart.utils;

public class StringFormatter {

    // ANSI escape codes for colors
    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // Bright colors
    public static final String BRIGHT_BLACK = "\033[1;30m";
    public static final String BRIGHT_RED = "\033[1;31m";
    public static final String BRIGHT_GREEN = "\033[1;32m";
    public static final String BRIGHT_YELLOW = "\033[1;33m";
    public static final String BRIGHT_BLUE = "\033[1;34m";
    public static final String BRIGHT_MAGENTA = "\033[1;35m";
    public static final String BRIGHT_CYAN = "\033[1;36m";
    public static final String BRIGHT_WHITE = "\033[1;37m";

    /**
     * Formats a string with the given parameters.
     * @author Ujjval
     * @param template The format string.
     * @param args The arguments to format.
     * @return The formatted string.
     */
    public static String format(String template, Object... args) {
        return String.format(template, args);
    }

    /**
     * Applies color formatting to a string.
     * @author Ujjval
     * @param color The ANSI color code.
     * @param text The text to color.
     * @return The colored text.
     */
    public static String color(String color, String text) {
        return color + text + RESET;
    }

    public static void main(String[] args) {
        String name = "John";
        int age = 30;

        String coloredString = StringFormatter.color(RED, "This text is red!");
        System.out.println(coloredString);
    }
}
