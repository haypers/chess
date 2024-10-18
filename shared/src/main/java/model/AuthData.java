package model;

public record AuthData(String userName, String username) {
    @Override
    public String userName() {
        return userName;
    }

    @Override
    public String username() {
        return username;
    }
}
