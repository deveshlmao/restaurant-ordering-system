package main.utils;

public class SessionManager {
    private static int loggedInUserId = -1;
    private static String loggedInUsername = null;

    public static void setLoggedInUser(int userId, String username, boolean admin) {
        loggedInUserId = userId;
        loggedInUsername = username;
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void clearSession() {
        loggedInUserId = -1;
        loggedInUsername = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUserId != -1;
    }
}
