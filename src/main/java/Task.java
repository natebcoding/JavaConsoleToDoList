import java.time.LocalDate;

public class Task {
    private final String taskName;
    private final String taskDescription;
    public TaskStatus status;
    public LocalDate localDate;

    Task (String taskName, String taskDescription, TaskStatus status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.localDate = LocalDate.now();
    }

    // Getters

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription () {
        return taskDescription;
    }

    public TaskStatus getTaskStatus() {
        return status;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    void setTaskStatus(TaskStatus status) {
        this.status = status;
    }




}
