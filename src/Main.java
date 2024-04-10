import category.TaskCategory;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.FileBackedTaskManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {
    static Scanner scanner = new Scanner(System.in);

    static File savedManager = new File("src/resources/mapsAndHistory.csv");

    static TaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(savedManager);



    public static void main(String[] args) throws IOException {
        startApp();
    }

    public static void startApp() throws IOException {
        while (true) {
            printMenu();
            int action = scanner.nextInt();
            chooseAction(action);
        }
    }

    public static void printMenu() {
        System.out.println("Выберите команду: ");
        System.out.println("1 - создать задачу:");
        System.out.println("2 - получить задачу по ID:");
        System.out.println("3 - удалить задачу по ID:");
        System.out.println("4 - обновление задачи по ID:");
        System.out.println("5 - показать все задачи:");
        System.out.println("6 - удалить все задачи:");
        System.out.println("7 - показать историю вызовов задач");
        System.out.println("8 - выход из программы:");
    }

    public static void chooseAction(int action) throws IOException {
        switch (action) {
            case 1 -> {
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType1 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType1) {
                    case 1 -> createSimpleTask();
                    case 2 -> createEpicTask();
                    case 3 -> createSubtask();
                }
            }
            case 2 -> {
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType2 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType2) {
                    case 1 -> getSimpleTask();
                    case 2 -> getEpicTask();
                    case 3 -> getSubtask();
                }
            }
            case 3 -> {
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType3 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType3) {
                    case 1 -> deleteSimpleTask();
                    case 2 -> deleteEpicTask();
                    case 3 -> deleteSubtask();
                }
            }
            case 4 -> {
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType4 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType4) {
                    case 1 -> updateSimpleTask();
                    case 2 -> updateEpicTask();
                    case 3 -> updateSubtask();
                }
            }
            case 5 -> {
                System.out.println("Выберите тип задач:" +
                        " 1 - простые," +
                        " 2 - эпичные," +
                        " 3 - подзадачи");
                int taskType5 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType5) {
                    case 1 -> showSimpleTasks();
                    case 2 -> showEpicTasks();
                    case 3 -> showSubtasks();
                }
            }
            case 6 -> {
                System.out.println("Выберите тип задач:" +
                        " 1 - простые," +
                        " 2 - эпичные," +
                        " 3 - подзадачи");
                int taskType6 = scanner.nextInt();
                scanner.nextLine();
                switch (taskType6) {
                    case 1 -> deleteSimpleTasks();
                    case 2 -> deleteEpicTasks();
                    case 3 -> deleteSubtasks();
                }
            }
            case 7 -> showHistory();
            case 8 -> System.exit(0);
        }

    }

    public static void createSimpleTask() throws IOException {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        task.setCategory(TaskCategory.NEW);
        fileBackedTaskManager.addSimpleTask(task);
    }

    public static void createEpicTask() throws IOException {
        System.out.println("Введите название эпической задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпической задачи:");
        String description = scanner.nextLine();
        Epic epic = new Epic(title, description);
        epic.setCategory(TaskCategory.NEW);
        fileBackedTaskManager.addEpicTask(epic);
    }

    public static void createSubtask() throws IOException {
        System.out.println("Введите ID эпической задачи:");
        int epicID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название подзадачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine();
        Subtask subtask = new Subtask(title, description);
        subtask.setEpicID(epicID);
        subtask.setCategory(TaskCategory.NEW);
        fileBackedTaskManager.addSubtask(subtask);
    }

    public static void getSimpleTask() throws IOException {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Task task = fileBackedTaskManager.getSimpleTask(taskID);
        System.out.println(task);
    }

    public static void getEpicTask() throws IOException {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Epic epic = fileBackedTaskManager.getEpicTask(taskID);
        System.out.println(epic);
    }

    public static void getSubtask() throws IOException {
        System.out.println("Введите ID подзадачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Subtask subtask = fileBackedTaskManager.getSubtask(taskID);
        System.out.println(subtask);
    }

    public static void deleteSimpleTask() throws IOException {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        fileBackedTaskManager.deleteSimpleTask(taskID);
    }

    public static void deleteEpicTask() throws IOException {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        fileBackedTaskManager.deleteEpic(taskID);
    }

    public static void deleteSubtask() throws IOException {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        fileBackedTaskManager.deleteSubtask(taskID);
    }

    public static void updateSimpleTask() throws IOException {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        task.setTaskID(taskID);
        System.out.println("Введите категорию/статус задачи: 1 - in progress, 2 - done");
        TaskCategory category;
        switch (scanner.nextInt()) {
            case 1 -> {
                category = TaskCategory.IN_PROGRESS;
                task.setCategory(category);
            }
            case 2 -> {
                category = TaskCategory.DONE;
                task.setCategory(category);
            }
        }
        fileBackedTaskManager.updateSimpleTask(task);
    }

    public static void updateEpicTask() throws IOException {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Epic epic = new Epic(title, description);
        epic.setTaskID(taskID);
        fileBackedTaskManager.updateEpicTask(epic);
    }

    public static void updateSubtask() throws IOException {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Subtask subtask = new Subtask(title, description);
        subtask.setTaskID(taskID);
        System.out.println("Введите категорию/статус задачи: 1 - in progress, 2 - done");
        TaskCategory category;
        switch (scanner.nextInt()) {
            case 1 -> {
                category = TaskCategory.IN_PROGRESS;
                subtask.setCategory(category);
            }
            case 2 -> {
                category = TaskCategory.DONE;
                subtask.setCategory(category);
            }
        }
        fileBackedTaskManager.updateSubtask(subtask);
    }

    public static void showSimpleTasks() {
        System.out.println(fileBackedTaskManager.getSimpleTasks());
    }

    public static void showEpicTasks() {
        System.out.println(fileBackedTaskManager.getEpicTasks());
    }

    public static void showSubtasks() {
        System.out.println(fileBackedTaskManager.getSubtasks());
    }

    public static void deleteSimpleTasks() throws IOException {
        fileBackedTaskManager.deleteSimpleTasks();
    }

    public static void deleteEpicTasks() throws IOException {
        fileBackedTaskManager.deleteEpicTasks();
    }

    public static void deleteSubtasks() throws IOException {
        fileBackedTaskManager.deleteSubtasks();
    }

    public static void showHistory() {
        List<Task> tasksHistory = fileBackedTaskManager.getHistory();
        System.out.println(tasksHistory);
    }
}