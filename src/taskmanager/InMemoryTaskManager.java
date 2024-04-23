package taskmanager;

import category.TaskStatus;
import exceptions.TaskTimeOverlapException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected int counter = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.
            comparing(Task::getStartTime).thenComparing(Task::getDuration));


    protected int increaseCounter() {
        return ++counter;
    }

    protected void setCounter(Integer value) {
        this.counter = value;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void checkTimeAndDuration(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            addToPriorityList(task);
        }
    }

    private void addToPriorityList(Task task) {
        try {
            if (isOverlapping(task)) {
                throw new TaskTimeOverlapException("Задача пересекается по времени с другой: " + task.getTaskID());
            } else {
                prioritizedTasks.add(task);
            }
        } catch (TaskTimeOverlapException e) {
            e.getStackTrace();
        }
    }

    private boolean isOverlapping(Task task) {

    }


    private void calculateEpicTimeAndDuration(Subtask subtask) {
        int epicID = subtask.getEpicID();
        List<Subtask> ofEpicSubtasks = subtasks.values()
                .stream()
                .filter(task -> task.getEpicID() == epicID)
                .collect(Collectors.toList());
        Duration duration = ofEpicSubtasks
                .stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        List<Subtask> sortedSubtasks = ofEpicSubtasks
                .stream()
                .sorted(Comparator.comparing(Subtask::getStartTime))
                .collect(Collectors.toList());
        Instant startTime = null;
        Instant endTimeNoDuration;
        Instant endTime = null;
        if (!sortedSubtasks.isEmpty()) {
            startTime = sortedSubtasks.get(0).getStartTime();
            endTimeNoDuration = sortedSubtasks.getLast().getStartTime();
            endTime = endTimeNoDuration.plus(sortedSubtasks.getLast().getDuration());
        }

        Epic epic = epics.get(epicID);
        epic.setStartTime(startTime);
        epic.setDuration(duration);
        epic.setEndTime(endTime);
        epics.put(epicID, epic);

    }

    @Override
    public void addSimpleTask(Task task) {
        int newID = increaseCounter();
        task.setTaskID(newID);
        tasks.put(newID, task);
        checkTimeAndDuration(task);
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
            correctEpicStatus(epicID);
            calculateEpicTimeAndDuration(subtask);
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
            correctEpicStatus(epicID);
            calculateEpicTimeAndDuration(subTask);
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
            TaskStatus status = prevEpic.getStatus();
            epic.setStatus(status);
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
            correctEpicStatus(epicID);
        }
    }

    protected void correctEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Task> currentTasks = new ArrayList<>(subtasks.values());
        if (currentTasks.stream().allMatch(Objects::isNull) ||
                currentTasks.stream().allMatch(task ->
                        task.getStatus().equals(TaskStatus.NEW))) {
            epic.setStatus(TaskStatus.NEW);
        } else if (currentTasks.stream().anyMatch(task ->
                task.getStatus().equals(TaskStatus.IN_PROGRESS))) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.DONE);
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
        for (Integer id : tasks.keySet()) {
            history.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        List<Integer> ids = new ArrayList<>(epics.keySet());
        ids.addAll(subtasks.keySet());
        for (Integer id : ids) {
            history.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer id : subtasks.keySet()) {
            history.remove(id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIDs().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}