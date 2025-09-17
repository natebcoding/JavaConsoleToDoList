import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class TaskManager{
    static Scanner sc = new Scanner(System.in);
    public static ArrayList<Task> myTaskList = new ArrayList<>();



    public void mainMenu() {
        String userChoice = " ";

        while (!userChoice.contains("5")) {
            System.out.println("==============================");
            System.out.println("WELCOME TO THE TO DO LIST APP");
            System.out.println("==============================");
            System.out.println();
            System.out.println("Please choose from one of the following: ");
            System.out.println("1. Add a Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete a task");
            System.out.println("4. Update a Task");
            System.out.println("5. Exit");
            System.out.print("Enter your choice now: ");
            userChoice = sc.nextLine();
            switch (userChoice) {
                case "1" -> addTask();
                case "2" -> viewTasks(myTaskList);
                case "3" -> deleteTask(myTaskList);
                case "4" -> updateTaskStatus(myTaskList);
                case "5" -> System.out.println("Goodbye!");
            }




        }
    }

    public void addTask() {
        String taskName;
        String taskDescription = " ";
        String taskStatus;
        boolean close = false;

        while (true) {
            System.out.println("What is the name of the task you'd like to add?");
            taskName = sc.nextLine().trim();
            if (taskName.isEmpty()) {
                System.out.println("Task cannot be empty.");
                continue;
            }


            while (!close) {
                System.out.println("Enter a task description: ");
                taskDescription = sc.nextLine().trim();
                if (taskDescription.isEmpty()) {
                    System.out.println("Task description cannot be empty.");
                } else {
                    close = true;
                }
            }


            System.out.println("What is the tasks status? (NS - Not Started, S - Started, C - Complete): ");
            taskStatus = sc.nextLine().trim();

            if (taskStatus.isEmpty()) {
                System.out.println("Task status cannot be empty!");
                continue;
            }
            TaskStatus status = TaskStatus.fromInput(taskStatus);


            Task task = new Task(taskName, taskDescription, status);
            addTaskToArrayList(task);
            System.out.println(UX_Colors.cyan("Task added successfully!"));
            break;


        }

        startCountdownTimer();

    }

    static void viewTasks(ArrayList<Task> targetTaskList) {
        int counter = 1;
        if (targetTaskList.isEmpty()) {
            System.out.println("There are no tasks available...");
        }
        for (Task task : targetTaskList) {
            System.out.println(UX_Colors.blue(task.getLocalDate().toString() + "| " + counter + ". " + "Task Name: " + task.getTaskName() + "\t| Task Description: " + task.getTaskDescription() + "\t| Task Status: " + task.getTaskStatus().displayStatus()));
            counter++;
        }

    }

    static void deleteTask(ArrayList<Task> targetTaskList) {
        if (targetTaskList.isEmpty()) {
            System.out.println("There are no tasks available...");
        } else if (!targetTaskList.isEmpty()){
            String taskDeleteNum;
            int delIndex;
            boolean close = false;
            System.out.println("Current Task List: ");
            viewTasks(targetTaskList);
            System.out.println("============");

            while (!close) {
                try {
                    System.out.println("Enter the # of the task you'd like to delete: ");
                    taskDeleteNum = sc.nextLine();
                    delIndex = Integer.parseInt(taskDeleteNum);
                    targetTaskList.remove(delIndex - 1);
                    System.out.println(UX_Colors.cyan("Task " + delIndex + " has been deleted"));
                    if (targetTaskList.isEmpty()) {
                        close = true;
                    }
                    System.out.println("Would you like to delete another task?");
                    String answer = sc.nextLine();
                    if (targetTaskList.isEmpty()) {
                        System.out.println("List is empty. Returning to main menu.");
                    } else if (answer.equalsIgnoreCase("N")) {
                        close = true;
                    }
                    viewTasks(targetTaskList);
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a number.");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("That task number is invalid. Please note once a task is deleted, it will take the # of the task deleted.");
                }
            }
        }

    }

    static void updateTaskStatus(ArrayList<Task> targetTaskList) {
        String newTaskStatus;

        if (targetTaskList.isEmpty()) {
            System.out.println("There are no tasks available...");
        } else if (!targetTaskList.isEmpty()){
            String taskUpdateNum;
            int updateIndex;
            boolean close = false;
            System.out.println("Current Task List: ");
            viewTasks(targetTaskList);
            System.out.println("============");

            while (!close) {
                try {
                    System.out.println("Enter the # of the task you'd like to update: ");
                    taskUpdateNum = sc.nextLine();
                    updateIndex = Integer.parseInt(taskUpdateNum)-1;
                    System.out.println("Current task status: " + myTaskList.get(updateIndex).getTaskStatus());
                    System.out.println("What would you like to update this to? (NS - Not Started, S - Started, C - Complete)");
                    newTaskStatus = sc.nextLine().trim();

                    if (newTaskStatus.isEmpty()) {
                        System.out.println("Task status cannot be empty!");
                        continue;
                    }
                    TaskStatus newStatus = TaskStatus.fromInput(newTaskStatus);
                    targetTaskList.get(updateIndex).setTaskStatus(newStatus);
                    System.out.println(UX_Colors.cyan("Task successfully updated"));
                    close = true;
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("List number does not exist.");
                }
            }
        }

    }

    private void addTaskToArrayList(Task taskToAdd) {
        myTaskList.add(taskToAdd);
    }

    private static void startCountdownTimer() {
        Thread timer = new Thread(new Timer_Helper(3));
        timer.start();
        try {
            timer.join();
        } catch (InterruptedException e) {
            System.out.println("The timer was interrupted!");
        }
    }


}
