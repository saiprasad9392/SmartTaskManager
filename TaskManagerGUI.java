import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TaskManagerGUI {
    private JFrame frame;
    private JTextArea taskArea;
    private JTextField titleField, categoryField, deadlineField, taskIdField;
    private User currentUser;

    public TaskManagerGUI() {
        loginScreen();
    }

    private void loginScreen() {
        frame = new JFrame("Task Manager - Login");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(4, 2));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        frame.add(new JLabel("Username:"));
        frame.add(userField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);
        frame.add(loginBtn);
        frame.add(registerBtn);

        loginBtn.addActionListener(e -> {
            int userId = UserDAO.login(userField.getText(), new String(passField.getPassword()));
            if (userId != -1) {
                currentUser = new User(userId, userField.getText());
                frame.dispose();
                dashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed.");
            }
        });

        registerBtn.addActionListener(e -> {
            boolean success = UserDAO.register(userField.getText(), new String(passField.getPassword()));
            JOptionPane.showMessageDialog(frame, success ? "Registration successful!" : "Username already exists.");
        });

        frame.setVisible(true);
    }

    private void dashboard() {
        frame = new JFrame("Task Manager - Welcome " + currentUser.getUsername());
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        taskArea = new JTextArea();
        taskArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskArea);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        titleField = new JTextField();
        categoryField = new JTextField();
        deadlineField = new JTextField();
        taskIdField = new JTextField();

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Deadline:"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Task ID to Complete/Delete:"));
        inputPanel.add(taskIdField);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton completeBtn = new JButton("Complete");
        JButton deleteBtn = new JButton("Delete");
        JButton logoutBtn = new JButton("Logout");

        addBtn.addActionListener(e -> {
            String title = titleField.getText();
            String category = categoryField.getText();
            String deadline = deadlineField.getText();
            Task task = new Task(0, title, category, deadline, false);
            TaskDAO.addTask(task, currentUser.getId());
            refreshTasks();
        });

        completeBtn.addActionListener(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                TaskDAO.updateTaskCompletion(taskId);
                refreshTasks();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid task ID.");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                TaskDAO.deleteTask(taskId);
                refreshTasks();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid task ID.");
            }
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            loginScreen();
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(logoutBtn);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        refreshTasks();
    }

    private void refreshTasks() {
        List<Task> tasks = TaskDAO.getTasks(currentUser.getId());
        taskArea.setText("");
        for (Task task : tasks) {
            taskArea.append(task + \"\\n\");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerGUI::new);
    }
}
