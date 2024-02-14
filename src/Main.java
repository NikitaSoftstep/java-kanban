import java.util.Scanner;

public class Main {
    static TaskManager taskManager = new TaskManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        while (true) {
            printMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    addTask(chooseTask());
                    break;
                case 2:
                    deleteTask();
                    break;
                case 3:
                    getTaskByID();
                    break;
                case 4:
                    updateByID();
                    break;
                case 5:
                    deleteAllTasks();
                    break;
                case 6:
                    showTaskLists();
                    break;
            }
        }
    }

    public static void printMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - создание объекта");
        System.out.println("2 - удаление объекта по ID");
        System.out.println("3 - получение объекта по ID");
        System.out.println("4 - обновление по ID (новый объект)");
        System.out.println("5 - удалить все задачи");
        System.out.println("6 - вывести списки задач");

    }

    public static int chooseTask() {
        System.out.println("Выберите тип задачи для создания/добавления:");
        System.out.println("1 - простая, 2 - эпик, 3 - подзадача");
        int taskType = scanner.nextInt();
        scanner.nextLine();
        return taskType;
    }

    public static void addTask(int taskType) {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        switch (taskType) {
            case 1:
                taskManager.createTask(new Task(title, description));
                break;
            case 2:
                taskManager.createTask(new Epic(title, description));
                break;
            case 3:
                System.out.println("Введите ID эпика:");
                int epicID = scanner.nextInt();
                taskManager.createSubtask(new Subtask(title, description), epicID);
                break;
        }
    }

    static public void deleteTask() {
        System.out.println("Введите тип задачи: 1 - простая, 2 - эпик, 3 - подзадача");
        int taskType = scanner.nextInt();
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        taskManager.deleteTask(taskType, taskID);

    }
    static public void getTaskByID() {
        System.out.println("Введите тип задачи: 1 - простая, 2 - эпик, 3 - подзадача:");
        int taskType = scanner.nextInt();
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        taskManager.getTask(taskType, taskID);
    }

    static public void updateByID() {
        System.out.println("Введите тип задачи: 1 - простая, 2 - эпик, 3 - подзадача");
        int taskType = scanner.nextInt();
        System.out.println("Введите ID задачи:");
        int taskID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Введите название:");
        String title = scanner.nextLine();
        System.out.println("Введите описание:");
        String description = scanner.nextLine();

        switch(taskType) {
            case 1:
                System.out.println("Установите статус: 1 - in progress, 2 - done");
                int status = scanner.nextInt();
                Task task = new Task(title, description);
                switch(status) {
                    case 1:
                        task.setCategory(TaskCategory.IN_PROGRESS);
                        break;
                    case 2:
                        task.setCategory(TaskCategory.DONE);
                        break;
                }
                taskManager.updateByID(task, taskID);
                break;
            case 2:
                Epic epic = new Epic(title, description);
                taskManager.updateByID(epic, taskID);
                break;
            case 3:
                System.out.println("Установите статус: 1 - in progress, 2 - done");
                int status2 = scanner.nextInt();
                Subtask subtask = new Subtask(title, description);
                switch(status2) {
                    case 1:
                        subtask.setCategory(TaskCategory.IN_PROGRESS);
                        break;
                    case 2:
                        subtask.setCategory(TaskCategory.DONE);
                        break;
                }
                taskManager.updateByID(subtask, taskID);

                break;

        }
    }

    public static void deleteAllTasks() {
        System.out.println("Введите тип задачи: 1 - все простые," +
                " 2 - все эпики, " +
                "3 - все подзадачи эпика");
        int taskType = scanner.nextInt();
        taskManager.deleteAllTasks(taskType);
    }

    public static void showTaskLists() {
        System.out.println("Введите тип задачи: 1 - все простые, " +
                "2 - все эпики, " +
                "3 - все подзадачи эпика");
        int taskType = scanner.nextInt();
        taskManager.showTaskList(taskType);
    }
}