package ue.edu.consumoapivolley1;

public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;

    // Constructor
    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    // Getters
    public int getUserId() { return userId; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }

    // El ArrayAdapter usará este metodo automáticamente para mostrar el texto en la lista
    @Override
    public String toString() {
        return "User ID: " + userId +
                "\nID: " + id +
                "\nTítulo: " + title +
                "\nCuerpo: " + body;
    }
}
