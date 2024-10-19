package model;

public record AuthData(String authToken, String userName) {
    @Override
    public String userName() {
        return userName;
    }

    @Override
    public String authToken() {
        return authToken;
    }
}
