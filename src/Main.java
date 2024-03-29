import category.TaskCategory;
import taskmanager.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.util.List;
import java.util.Scanner;



public class Main {
    static Scanner scanner = new Scanner(System.in);
    static TaskManager inMemoryTaskManager = Managers.getDefaultManager();
    public static void main(String[] args) {

        startApp();
    }

    public static void startApp() {
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
        System.out.println("7 - показать последние " + InMemoryHistoryManager.MAX_HISTORY_SIZE
                + " вызовов задач");
        System.out.println("8 - выход из программы:");
    }

    public static void chooseAction(int action) {
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

    public static void createSimpleTask() {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        task.setCategory(TaskCategory.NEW);
        inMemoryTaskManager.addSimpleTask(task);
    }

    public static void createEpicTask() {
        System.out.println("Введите название эпической задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпической задачи:");
        String description = scanner.nextLine();
        Epic epic = new Epic(title, description);
        epic.setCategory(TaskCategory.NEW);
        inMemoryTaskManager.addEpicTask(epic);
    }

    public static void createSubtask() {
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
        inMemoryTaskManager.addSubtask(subtask);
    }

    public static void getSimpleTask() {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Task task = inMemoryTaskManager.getSimpleTask(taskID);
        System.out.println(task);
    }

    public static void getEpicTask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Epic epic = inMemoryTaskManager.getEpicTask(taskID);
        System.out.println(epic);
    }

    public static void getSubtask() {
        System.out.println("Введите ID подзадачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Subtask subtask = inMemoryTaskManager.getSubtask(taskID);
        System.out.println(subtask);
    }

    public static void deleteSimpleTask() {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        inMemoryTaskManager.deleteSimpleTask(taskID);
    }

    public static void deleteEpicTask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        inMemoryTaskManager.deleteEpic(taskID);
    }

    public static void deleteSubtask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        inMemoryTaskManager.deleteSubtask(taskID);
    }

    public static void updateSimpleTask() {
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
        inMemoryTaskManager.updateSimpleTask(task);
    }

    public static void updateEpicTask() {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Epic epic = new Epic(title, description);
        epic.setTaskID(taskID);
        inMemoryTaskManager.updateEpicTask(epic);
    }

    public static void updateSubtask() {
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
        inMemoryTaskManager.updateSubtask(subtask);
    }

    public static void showSimpleTasks() {
        System.out.println(inMemoryTaskManager.getSimpleTasks());
    }

    public static void showEpicTasks() {
        System.out.println(inMemoryTaskManager.getEpicTasks());
    }

    public static void showSubtasks() {
        System.out.println(inMemoryTaskManager.getSubtasks());
    }

    public static void deleteSimpleTasks() {
        inMemoryTaskManager.deleteSimpleTasks();
    }

    public static void deleteEpicTasks() {
        inMemoryTaskManager.deleteEpicTasks();
    }

    public static void deleteSubtasks() {
        inMemoryTaskManager.deleteSubtasks();
    }

    public static void showHistory() {
        List<Task> tasksHistory = inMemoryTaskManager.getHistory();
        System.out.println(tasksHistory);
    }
}