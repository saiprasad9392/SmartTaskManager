import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private String username;
    private String password;
    private List<Task> tasks = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public boolean checkPassword(String pw) { return password.equals(pw); }
    public List<Task> getTasks() { return tasks; }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
    }

    public void completeTask(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                t.markCompleted();
                break;
            }
        }
    }
}
