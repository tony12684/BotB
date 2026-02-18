package io.github.tony12684.BotB;

public class Config {
    // TODO: merge config and database into one private file that is hidden from git
    private String url;
    private int port;
    private String username;
    private String password;
    private Database database;

    // Getters and setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }
}