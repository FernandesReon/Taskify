package com.reonfernandes.Taskify.helper;

import java.util.ArrayList;
import java.util.List;

public class UniqueUsernameAssigner {
    private static final List<String> assignedUsernames = new ArrayList<>();

    public static String assignUniqueUsername(String baseName) {
        // Sanitize the base name (remove spaces and special characters, if needed)
        String sanitizedBaseName = baseName.replaceAll("\\s+", "").replaceAll("[^a-zA-Z0-9]", "");

        // Generate unique username
        String username = sanitizedBaseName;
        int suffix = 1;

        // Check if the username is already taken
        while (assignedUsernames.contains(username)) {
            username = sanitizedBaseName + suffix; // Append a number to make it unique
            suffix++;
        }

        // Add the generated username to the list of assigned usernames
        assignedUsernames.add(username);
        return username.toLowerCase();
    }
}
