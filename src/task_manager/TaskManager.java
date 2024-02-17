package task_manager;

import task_category.TaskCategory;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private int counter = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    private int increaseCounter() {
        return ++counter;
    }
    public void addSimpleTask(Task task) {
        int newID = increaseCounter();
        task.setTaskID(newID);
        tasks.put(newID, task);
    }

    public void addEpicTask(Epic epic) {
        int newID = increaseCounter();
        epic.setTaskID(newID);
        epics.put(newID, epic);
    }

    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicID())) {
            int newID = increaseCounter();
            subtask.setTaskID(newID);
            subtasks.put(newID, subtask);
            Epic epic = epics.get(subtask.getEpicID());
            epic.addSubtaskID(newID);
            int epicID = subtask.getEpicID();
            correctEpicCategory(epicID);
        }
    }

    public Task getSimpleTask(int taskID) {
        return tasks.get(taskID);
    }

    public Epic getEpicTask(int taskID) {
        return epics.get(taskID);
    }

    public Subtask getSubtask(int taskID) {
        return subtasks.get(taskID);
    }

    public void deleteSimpleTask(int taskID) {
            tasks.remove(taskID);

    }
    public void deleteEpic(int taskID) {
        if (epics.containsKey(taskID)) {
            Epic epic = epics.get(taskID);
            ArrayList<Integer> ids = epic.getSubtaskIDs();
            for (int id : ids) {
                subtasks.remove(id);
            }
            epics.remove(taskID);
        }
    }

    public void deleteSubtask(int taskID) {
        if (subtasks.containsKey(taskID)) {
            Subtask subTask = subtasks.get(taskID);
            int epicID = subTask.getEpicID();
            subtasks.remove(taskID);
            Epic epic = epics.get(epicID);
            epic.getSubtaskIDs().remove(taskID);
            correctEpicCategory(epicID);
        }
    }

    public void updateSimpleTask(Task task) {
        if (tasks.containsKey(task.getTaskID())) {
            tasks.put(task.getTaskID(),task);
        }
    }

    public void updateEpicTask(Epic epic) {
        if (epics.containsKey(epic.getTaskID())) {
            Epic prevEpic = epics.get(epic.getTaskID());
            TaskCategory category = prevEpic.getCategory();
            epic.setCategory(category);
            epics.put(epic.getTaskID(),epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getTaskID())) {
            int taskID = subtask.getTaskID();
            Subtask prevTask = subtasks.get(taskID);
            int epicID = prevTask.getEpicID();
            subtask.setEpicID(epicID);
            subtasks.put(taskID, subtask);
            correctEpicCategory(epicID);
        }
    }

    private void correctEpicCategory(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Task> currentTasks = new ArrayList<>(subtasks.values());
        if (currentTasks.stream().allMatch(Objects::isNull) ||
                currentTasks.stream().allMatch(task ->
                        task.getCategory().equals(TaskCategory.NEW))) {
            epic.setCategory(TaskCategory.NEW);
        } else if (currentTasks.stream().anyMatch(task ->
                task.getCategory().equals(TaskCategory.IN_PROGRESS))) {
            epic.setCategory(TaskCategory.IN_PROGRESS);
        } else {
            epic.setCategory(TaskCategory.DONE);
        }

    }

    public ArrayList<Task> getSimpleTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteSimpleTasks() {
        tasks.clear();
    }

    public void deleteEpicTasks() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic: epics.values()) {
            epic.getSubtaskIDs().clear();
            epic.setCategory(TaskCategory.NEW);
        }
    }
}