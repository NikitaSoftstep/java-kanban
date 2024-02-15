import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TaskManager {
    private int counter = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    private int increaseCounter() {
        return ++counter;
    }
    void addSimpleTask(Task task) {
        int newID = increaseCounter();
        task.setTaskID(newID);
        tasks.put(newID, task);
    }

    void addEpicTask(Epic epic) {
        int newID = increaseCounter();
        epic.setTaskID(newID);
        epics.put(newID, epic);
    }

    void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) {
            int newID = increaseCounter();
            subtask.setTaskID(newID);
            subtasks.put(newID, subtask);
            Epic epic = epics.get(subtask.getEpicID());
            epic.addSutaskID(newID);
        }
    }

    Task getSimpleTask(int taskID) {
        return tasks.get(taskID);
    }

    Epic getEpicTask(int taskID) {
        return epics.get(taskID);
    }

    Subtask getSubtask(int taskID) {
        return subtasks.get(taskID);
    }

    void deleteSimpleTask(int taskID) {
        if (tasks.containsKey(taskID)) {
            tasks.remove(taskID);
        }
    }
    void deleteEpicTask(int taskID) {
        if (epics.containsKey(taskID)) {
            Epic epic = epics.get(taskID);
            ArrayList<Integer> ids = epic.getSubtaskIDs();
            for (int id : ids) {
                subtasks.remove(id);
            }
            epics.remove(taskID);
        }
    }

    void deleteSubtask(int taskID) {
        if (subtasks.containsKey(taskID)) {
            subtasks.remove(taskID);
        }
    }

    void updateSimpleTask(Task task) {
        if (tasks.containsKey(task.getTaskID())) {
            tasks.put(task.getTaskID(),task);
        }
    }

    void updateEpicTask(Epic epic) {
        if (epics.containsKey(epic.getTaskID())) {
            Epic prevEpic = epics.get(epic.getTaskID());
            TaskCategory category = prevEpic.getCategory();
            epic.setCategory(category);
            epics.put(epic.getTaskID(),epic);
        }
    }

    void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getTaskID())) {
            int taskID = subtask.getTaskID();
            Subtask prevTask = subtasks.get(taskID);
            int epicID = prevTask.getEpicID();
            subtask.setEpicID(epicID);
            subtasks.put(taskID, subtask);
            TaskCategory category = subtask.getCategory();
            correctEpicCategory(epicID, category);
        }
    }

    void correctEpicCategory(int epicID, TaskCategory category) {
        Epic epic = epics.get(epicID);
        int doneCounter = 0;
        switch(category) {
            case IN_PROGRESS:
                epic.setCategory(TaskCategory.IN_PROGRESS);
                break;
            case DONE:
                ArrayList<Integer> subIDs= epic.getSubtaskIDs();
                for(int id : subIDs) {
                    Subtask subtask = subtasks.get(id);
                    if (subtask.getCategory() != null && TaskCategory.DONE.equals(subtask.getCategory())) {
                        doneCounter++;
                    }
                }
                if (doneCounter == subIDs.size()) {
                    epic.setCategory(TaskCategory.DONE);
                }
                break;
        }

    }

    public ArrayList<Task> showSimpleTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> showEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> showSubtasks(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Integer> ids = epic.getSubtaskIDs();
        ArrayList<Subtask> currentSubtasks = new ArrayList<>();
        for (int id : ids) {
            currentSubtasks.add(subtasks.get(id));
        }
        return currentSubtasks;
    }

    void deleteSimpleTasks() {
        tasks.clear();
    }

    void deleteEpicTasks() {
        epics.clear();
        subtasks.clear();
    }

    void deleteSubtasks(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Integer> ids = epic.getSubtaskIDs();
        for (int id : ids) {
            subtasks.remove(id);
        }
    }
}