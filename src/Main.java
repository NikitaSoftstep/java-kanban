import task_category.TaskCategory;
import task_manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Scanner;



public class Main {
    static Scanner scanner = new Scanner(System.in);
    static TaskManager taskManager = new TaskManager();
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
        System.out.println("7 - выход из программы:");
    }

    public static void chooseAction(int action) {
        switch(action) {
            case 1:
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType1 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType1) {
                    case 1:
                        createSimpleTask();
                        break;
                    case 2:
                        createEpicTask();
                        break;
                    case 3:
                        createSubtask();
                        break;
                }
                break;

            case 2:
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType2 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType2) {
                    case 1:
                        getSimpleTask();
                        break;
                    case 2:
                        getEpicTask();
                        break;
                    case 3:
                        getSubtask();
                        break;
                }
                break;
            case 3:
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType3 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType3) {
                    case 1:
                        deleteSimpleTask();
                        break;
                    case 2:
                        deleteEpicTask();
                        break;
                    case 3:
                        deleteSubtask();
                        break;
                }
                break;
            case 4:
                System.out.println("Выберите тип задачи:" +
                        " 1 - простая," +
                        " 2 - эпичная," +
                        " 3 - подзадача");
                int taskType4 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType4) {
                    case 1:
                        updateSimpleTask();
                        break;
                    case 2:
                        updateEpicTask();
                        break;
                    case 3:
                        updateSubtask();
                        break;
                }
                break;
            case 5:
                System.out.println("Выберите тип задач:" +
                        " 1 - простые," +
                        " 2 - эпичные," +
                        " 3 - подзадачи");
                int taskType5 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType5) {
                    case 1:
                        showSimpleTasks();
                        break;
                    case 2:
                        showEpicTasks();
                        break;
                    case 3:
                        showSubtasks();
                        break;
                }
                break;
            case 6:
                System.out.println("Выберите тип задач:" +
                        " 1 - простые," +
                        " 2 - эпичные," +
                        " 3 - подзадачи");
                int taskType6 = scanner.nextInt();
                scanner.nextLine();
                switch(taskType6) {
                    case 1:
                        deleteSimpleTasks();
                        break;
                    case 2:
                        deleteEpicTasks();
                        break;
                    case 3:
                        deleteSubtasks();
                        break;
                }
                break;
            case 7:
                System.exit(0);
        }

    }

    public static void createSimpleTask() {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task task = new Task(title, description);
        task.setCategory(TaskCategory.NEW);
        taskManager.addSimpleTask(task);
    }

    public static void createEpicTask() {
        System.out.println("Введите название эпической задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпической задачи:");
        String description = scanner.nextLine();
        Epic epic = new Epic(title, description);
        epic.setCategory(TaskCategory.NEW);
        taskManager.addEpicTask(epic);
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
        taskManager.addSubtask(subtask);
    }

    public static void getSimpleTask() {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Task task = taskManager.getSimpleTask(taskID);
        System.out.println(task);
    }

    public static void getEpicTask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Epic epic = taskManager.getEpicTask(taskID);
        System.out.println(epic);
    }

    public static void getSubtask() {
        System.out.println("Введите ID подзадачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        Subtask subtask = taskManager.getSubtask(taskID);
        System.out.println(subtask);
    }

    public static void deleteSimpleTask() {
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteSimpleTask(taskID);
    }

    public static void deleteEpicTask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteEpic(taskID);
    }

    public static void deleteSubtask() {
        System.out.println("Введите ID эпической задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        taskManager.deleteSubtask(taskID);
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
        switch(scanner.nextInt()) {
            case 1:
                category = TaskCategory.IN_PROGRESS;
                task.setCategory(category);
                break;
            case 2:
                category = TaskCategory.DONE;
                task.setCategory(category);
                break;
        }
        taskManager.updateSimpleTask(task);
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
        taskManager.updateEpicTask(epic);
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
        switch(scanner.nextInt()) {
            case 1:
                category = TaskCategory.IN_PROGRESS;
                subtask.setCategory(category);
                break;
            case 2:
                category = TaskCategory.DONE;
                subtask.setCategory(category);
                break;
        }
        taskManager.updateSubtask(subtask);
    }

    public static void showSimpleTasks() {
        System.out.println(taskManager.getSimpleTasks());
    }

    public static void showEpicTasks() {
        System.out.println(taskManager.getEpicTasks());
    }

    public static void showSubtasks() {
        System.out.println(taskManager.getSubtasks());
    }

    public static void deleteSimpleTasks() {
        taskManager.deleteSimpleTasks();
    }

    public static void deleteEpicTasks() {
        taskManager.deleteEpicTasks();
    }

    public static void deleteSubtasks() {
        taskManager.deleteSubtasks();
    }
}