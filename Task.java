public class Task {
    private int id;
    private String title;
    private String category;
    private String deadline;
    private boolean completed;

    public Task(int id, String title, String category, String deadline, boolean completed) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.deadline = deadline;
        this.completed = completed;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }

    @Override
    public String toString() {
        return id + ". [" + (completed ? "✔" : " " ) + "] " + title + " (" + category + ") - Due: " + deadline;
    }
}
