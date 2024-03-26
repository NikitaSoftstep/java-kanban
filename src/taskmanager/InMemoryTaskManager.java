package taskmanager;

import category.TaskCategory;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int counter = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager history = Managers.getDefaultHistory();


    private int increaseCounter() {
        return ++counter;
    }
    @Override
    public void addSimpleTask(Task task) {
        int newID = increaseCounter();
        task.setTaskID(newID);
        tasks.put(newID, task);
    }

    @Override
    public void addEpicTask(Epic epic) {
        int newID = increaseCounter();
        epic.setTaskID(newID);
        epics.put(newID, epic);
    }

    @Override
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

    @Override
    public Task getSimpleTask(int taskID) {
        history.add(tasks.get(taskID));
        return tasks.get(taskID);
    }

    @Override
    public Epic getEpicTask(int taskID) {
        history.add(epics.get(taskID));
        return epics.get(taskID);
    }

    @Override
    public Subtask getSubtask(int taskID) {
        history.add(subtasks.get(taskID));
        return subtasks.get(taskID);
    }

    @Override
    public void deleteSimpleTask(int taskID) {
        history.remove(taskID);
        tasks.remove(taskID);
    }

    @Override
    public void deleteEpic(int taskID) {
        if (epics.containsKey(taskID)) {
            Epic epic = epics.get(taskID);
            ArrayList<Integer> ids = epic.getSubtaskIDs();
            for (int id : ids) {
                subtasks.remove(id);
            }
            history.remove(taskID);
            epics.remove(taskID);
        }
    }

    @Override
    public void deleteSubtask(int taskID) {
        if (subtasks.containsKey(taskID)) {
            Subtask subTask = subtasks.get(taskID);
            int epicID = subTask.getEpicID();
            subtasks.remove(taskID);
            Epic epic = epics.get(epicID);
            history.remove(taskID);
            epic.getSubtaskIDs().remove(taskID);
            correctEpicCategory(epicID);
        }
    }

    @Override
    public void updateSimpleTask(Task task) {
        if (tasks.containsKey(task.getTaskID())) {
            tasks.put(task.getTaskID(), task);
        }
    }

    @Override
    public void updateEpicTask(Epic epic) {
        if (epics.containsKey(epic.getTaskID())) {
            Epic prevEpic = epics.get(epic.getTaskID());
            TaskCategory category = prevEpic.getCategory();
            epic.setCategory(category);
            epics.put(epic.getTaskID(), epic);
        }
    }

    @Override
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

    @Override
    public ArrayList<Task> getSimpleTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSimpleTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIDs().clear();
            epic.setCategory(TaskCategory.NEW);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}