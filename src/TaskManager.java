import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TaskManager {
    private static int counter = 0;
    ArrayList<Task> simpleTasks = new ArrayList<>();
    ArrayList<Epic> epics = new ArrayList<>();

    public void createTask(Task task) {
        task.setCategory(TaskCategory.NEW);
        task.setTaskID(++counter);
        System.out.println("ID задачи - " + counter);
        if (task instanceof Epic) {
            epics.add((Epic) task);
        } else if (task instanceof Task) {
            simpleTasks.add(task);
        }
    }

    public void createSubtask(Subtask subtask, int epicID) {
        subtask.setCategory(TaskCategory.NEW);
        subtask.setTaskID(++counter);
        System.out.println("ID задачи - " + counter);
        for (Epic currentEpic : epics) {
            if (currentEpic.getTaskID() == epicID) {
                currentEpic.addSubtask(subtask);
            }
        }
    }

    public void deleteTask(int taskType, int taskID) {

        switch(taskType) {
            case 1:
                Iterator<Task> iterator = simpleTasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getTaskID() == taskID) {
                        iterator.remove();
                        break;
                    }
                }
                break;

            case 2:
                Iterator<Epic> iterator1 = epics.iterator();
                while (iterator1.hasNext()) {
                    Epic task = iterator1.next();
                    if (task.getTaskID() == taskID) {
                        iterator1.remove();
                        break;
                    }
                }
                break;


            case 3:
                System.out.println("Введите ID эпика:");
                int epicID = Main.scanner.nextInt();
                for (Epic currentEpic : epics) {
                    if (currentEpic.getTaskID() == epicID) {
                        Iterator<Subtask> iterator2 = currentEpic.subtasks.iterator();
                        while (iterator2.hasNext()) {
                            Subtask task = iterator2.next();
                            if (task.getTaskID() == taskID) {
                                iterator2.remove();
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void getTask(int taskType, int taskID) {

        switch(taskType) {
            case 1:
                for (Task task : simpleTasks) {
                    if (task.getTaskID() == taskID) {
                        System.out.println(task);
                    }
                }
                break;
            case 2:
                for (Epic task : epics) {
                    if (task.getTaskID() == taskID) {
                        System.out.println(task);
                    }
                }
                break;
            case 3:
                for (Epic epic : epics) {
                    for (Subtask subtask : epic.subtasks) {
                        if (subtask.getTaskID() == taskID) {
                            System.out.println(subtask);
                        }
                    }
                }
        }
    }

    public void updateByID(Task task, int taskID) {
        if (task.getClass() == Task.class) {
            for (Task currentTask : simpleTasks) {
                if (currentTask.getTaskID() == taskID) {
                    simpleTasks.remove(currentTask);
                    task.setTaskID(taskID);
                    simpleTasks.add(task);

                }
            }
        } else if (task instanceof Epic) {

            for (Epic currentEpic : epics) {
                if (currentEpic.getTaskID() == taskID) {
                    epics.remove(currentEpic);
                    task.setTaskID(taskID);
                    task.setCategory(TaskCategory.NEW);
                    epics.add((Epic) task);
                }
            }
        } else if (task instanceof Subtask) {
            for (Epic epic : epics) {
                Iterator<Subtask> iterator2 = epic.subtasks.iterator();
                while (iterator2.hasNext()) {
                    Subtask task2 = iterator2.next();
                    if (task2.getTaskID() == taskID) {
                        iterator2.remove();
                        task.setTaskID(taskID);
                        epic.subtasks.add((Subtask) task);
                        setEpicCategory(epic, task.getCategory());
                        break;
                    }
                }
            }
        }
    }

    public void deleteAllTasks(int taskType) {
        switch(taskType) {
            case 1:
                simpleTasks.clear();
                break;
            case 2:
                epics.clear();
                break;
            case 3:
                System.out.println("Введите ID эпика ");
                int epicID = Main.scanner.nextInt();
                for (Epic epic : epics) {
                    if (epic.getTaskID() == epicID) {
                        epic.subtasks.clear();
                    }
                }
        }
    }

    public void showTaskList(int taskType) {
        switch(taskType) {
            case 1:
                System.out.println(simpleTasks);
                break;
            case 2:
                System.out.println(epics);
                break;
            case 3:
                System.out.println("Введите ID эпика");
                int epicID = Main.scanner.nextInt();
                for (Epic epic : epics) {
                    if (epic.getTaskID() == epicID) {
                        System.out.println(epic.subtasks);
                    }
                }
                break;
        }
    }

    public void setEpicCategory(Epic epic, TaskCategory status) {
        if (status.equals(TaskCategory.IN_PROGRESS)) {
            epic.setCategory(TaskCategory.IN_PROGRESS);
        } else if (status.equals(TaskCategory.DONE)) {
            int doneTasks = 0;
            for (Subtask subtask : epic.subtasks) {
                if (subtask.getCategory().equals(TaskCategory.DONE)) {
                    doneTasks++;
                }
            }
            if (doneTasks == epic.subtasks.size()) {
                epic.setCategory(TaskCategory.DONE);
            }
        }


    }

}