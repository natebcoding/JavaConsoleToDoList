import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

class TaskManagerTest {

    private TaskManager taskManager;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
        // Clear the static task list before each test
        TaskManager.myTaskList.clear();
        // Redirect System.out to capture output
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);
        // Clear the static task list after each test
        TaskManager.myTaskList.clear();
    }

    // Helper method to simulate user input
    private void setSystemInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        // Reinitialize the scanner in TaskManager (this is a limitation of your current design)
        TaskManager.sc = new java.util.Scanner(System.in);
    }

    @Test
    @DisplayName("Should add a valid task successfully")
    void addTask_ValidInput_ShouldAddTask() {
        // Arrange
        String input = "Test Task\nTest Description\nNS\n";
        setSystemInput(input);

        // Act
        taskManager.addTask();

        // Assert
        assertEquals(1, TaskManager.myTaskList.size());
        assertEquals("Test Task", TaskManager.myTaskList.get(0).getTaskName());
        assertEquals("Test Description", TaskManager.myTaskList.get(0).getTaskDescription());
        assertEquals(TaskStatus.NOT_STARTED, TaskManager.myTaskList.get(0).getTaskStatus());
        assertEquals(LocalDate.now(), TaskManager.myTaskList.get(0).getLocalDate());
        assertTrue(outputStream.toString().contains("Task added successfully!"));
    }

    @Test
    @DisplayName("Should handle empty task name and retry")
    void addTask_EmptyTaskName_ShouldRetry() {
        // Arrange
        String input = "\nValid Task\nValid Description\nS\n";
        setSystemInput(input);

        // Act
        taskManager.addTask();

        // Assert
        assertEquals(1, TaskManager.myTaskList.size());
        assertEquals("Valid Task", TaskManager.myTaskList.get(0).getTaskName());
        assertTrue(outputStream.toString().contains("Task cannot be empty."));
    }

    @Test
    @DisplayName("Should handle empty task description and retry")
    void addTask_EmptyDescription_ShouldRetry() {
        // Arrange
        String input = "Valid Task\n\nValid Description\nC\n";
        setSystemInput(input);

        // Act
        taskManager.addTask();

        // Assert
        assertEquals(1, TaskManager.myTaskList.size());
        assertEquals("Valid Description", TaskManager.myTaskList.get(0).getTaskDescription());
        assertTrue(outputStream.toString().contains("Task description cannot be empty."));
    }

    @Test
    @DisplayName("Should handle different task status inputs")
    void addTask_DifferentStatusInputs_ShouldWork() {
        // Test "S" for STARTED
        String input1 = "Task1\nDesc1\nS\n";
        setSystemInput(input1);
        taskManager.addTask();

        // Reset for next test
        setUp();

        // Test "C" for COMPLETED
        String input2 = "Task2\nDesc2\nC\n";
        setSystemInput(input2);
        taskManager.addTask();

        assertEquals(TaskStatus.COMPLETED, TaskManager.myTaskList.get(0).getTaskStatus());
    }

    @Test
    @DisplayName("Should display empty list message when no tasks")
    void viewTasks_EmptyList_ShouldDisplayMessage() {
        // Arrange
        ArrayList<Task> emptyList = new ArrayList<>();

        // Act
        TaskManager.viewTasks(emptyList);

        // Assert
        assertTrue(outputStream.toString().contains("There are no tasks available..."));
    }

    @Test
    @DisplayName("Should display tasks correctly")
    void viewTasks_WithTasks_ShouldDisplayTasks() {
        // Arrange
        ArrayList<Task> taskList = new ArrayList<>();
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NOT_STARTED);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.COMPLETED);
        taskList.add(task1);
        taskList.add(task2);

        // Act
        TaskManager.viewTasks(taskList);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Task 1"));
        assertTrue(output.contains("Description 1"));
        assertTrue(output.contains("Not Started"));
        assertTrue(output.contains("Task 2"));
        assertTrue(output.contains("Description 2"));
        assertTrue(output.contains("Completed"));
    }

    @Test
    @DisplayName("Should delete task successfully")
    void deleteTask_ValidIndex_ShouldDeleteTask() {
        // Arrange
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Task 1", "Desc 1", TaskStatus.NOT_STARTED));
        taskList.add(new Task("Task 2", "Desc 2", TaskStatus.STARTED));

        String input = "1\nN\n"; // Delete task 1, then answer No to delete another
        setSystemInput(input);

        // Act
        TaskManager.deleteTask(taskList);

        // Assert
        assertEquals(1, taskList.size());
        assertEquals("Task 2", taskList.get(0).getTaskName());
        assertTrue(outputStream.toString().contains("Task 1 has been deleted"));
    }

    @Test
    @DisplayName("Should handle invalid task number gracefully")
    void deleteTask_InvalidIndex_ShouldHandleGracefully() {
        // Arrange
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Task 1", "Desc 1", TaskStatus.NOT_STARTED));

        String input = "5\n1\nN\n"; // Try invalid index 5, then valid index 1, then No
        setSystemInput(input);

        // Act
        TaskManager.deleteTask(taskList);

        // Assert
        assertTrue(outputStream.toString().contains("That task number is invalid"));
        assertEquals(0, taskList.size()); // Task should eventually be deleted
    }

    @Test
    @DisplayName("Should handle empty list in delete operation")
    void deleteTask_EmptyList_ShouldReturnToMenu() {
        // Arrange
        ArrayList<Task> emptyList = new ArrayList<>();
        String input = ""; // No input needed as it should return immediately
        setSystemInput(input);

        // Act
        TaskManager.deleteTask(emptyList);

        // Assert
        assertTrue(outputStream.toString().contains("There are no tasks available..."));
    }

    @Test
    @DisplayName("Should continue deleting when user answers yes")
    void deleteTask_MultipleDeletes_ShouldWork() {
        // Arrange
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Task 1", "Desc 1", TaskStatus.NOT_STARTED));
        taskList.add(new Task("Task 2", "Desc 2", TaskStatus.STARTED));
        taskList.add(new Task("Task 3", "Desc 3", TaskStatus.COMPLETED));

        String input = "1\nY\n1\nN\n"; // Delete task 1, Yes to continue, delete task 1 again, No to stop
        setSystemInput(input);

        // Act
        TaskManager.deleteTask(taskList);

        // Assert
        assertEquals(1, taskList.size());
        assertEquals("Task 3", taskList.get(0).getTaskName());
    }
}


