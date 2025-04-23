package main.models;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id;
    private final StringProperty fullName;
    private final StringProperty username;
    private final StringProperty email;
    private final BooleanProperty isAdmin;

    public User(int id, String fullName, String username, String email, boolean isAdmin) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.isAdmin = new SimpleBooleanProperty(isAdmin);
    }

    public int getId() {
        return id.get();
    }

    public String getFullName() {
        return fullName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }

    public boolean isAdmin() {
        return isAdmin.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty roleProperty() {
        return new SimpleStringProperty(isAdmin.get() ? "Admin" : "User");
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }
}
