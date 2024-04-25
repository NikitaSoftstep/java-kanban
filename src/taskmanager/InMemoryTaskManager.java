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

public class InMemoryTaskManager implements TaskManager {
    protected int counter = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator
            .comparing(Task::getStartTime));


    protected int increaseCounter() {
        return ++counter;
    }

    protected void setCounter(Integer value) {
        this.counter = value;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void setPrioritizedTasks(TreeSet<Task> priorityTasks) {
        if (!priorityTasks.isEmpty()) {
            prioritizedTasks = priorityTasks;
        }
    }


    public void removeIfPresentFromPriority(Task task) {
        prioritizedTasks.remove(task);
    }

    public void checkTimeAndDuration(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            try {
                addToPriorityList(task);
            } catch (TaskTimeOverlapException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addToPriorityList(Task task) throws TaskTimeOverlapException {
        if (isNotOverlapping(task)) {
            prioritizedTasks.add(task);
        } else {
            throw new TaskTimeOverlapException("Задача пересекается по времени с другой: ");
        }
    }

    private boolean isNotOverlapping(Task newTask) {
        boolean notOverlapping;
        if (prioritizedTasks.isEmpty()) {
            notOverlapping = true;
        } else {
            notOverlapping = prioritizedTasks
                    .stream()
                    .allMatch(task -> newTask.getEndTime().isBefore(task.getStartTime())
                            || newTask.getStartTime().isAfter(task.getEndTime()));
            // if (overlapping) overlapping = false;
        }
        return notOverlapping;
    }

    private void checkEpicTimeAndStatus(Subtask subtask) {
        calculateEpicTimeAndDuration(subtask);
        correctEpicStatus(subtask.getEpicID());
    }

    private void calculateEpicTimeAndDuration(Subtask subtask) {

        int epicID = subtask.getEpicID();
        List<Subtask> epicSubtasks = epics.get(epicID).getSubtaskIDs()
                .stream()
                .map(subtasks::get)
                .toList();
        Duration duration = epicSubtasks
                .stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        List<Subtask> sortedSubtasks = epicSubtasks
                .stream()
                .sorted(Comparator.comparing(Subtask::getStartTime))
                .toList();
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
            checkEpicTimeAndStatus(subtask);
            checkTimeAndDuration(subtask);
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
        if (!prioritizedTasks.isEmpty()) {
            prioritizedTasks.removeIf(task -> task.getTaskID() == taskID);
        }
        tasks.remove(taskID);
    }

    @Override
    public void deleteEpic(int taskID) {
        if (epics.containsKey(taskID)) {
            Epic epic = epics.get(taskID);
            ArrayList<Integer> ids = epic.getSubtaskIDs();
            ids.forEach(id -> {
                subtasks.remove(id);
                prioritizedTasks.removeIf(task -> task.getTaskID() == id);
            });
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
            if (!prioritizedTasks.isEmpty()) {
                prioritizedTasks.removeIf(task -> task.getTaskID() == taskID);
            }
            epic.getSubtaskIDs().remove(taskID);
            checkEpicTimeAndStatus(subTask);
        }
    }

    @Override
    public void updateSimpleTask(Task task) {
        if (tasks.containsKey(task.getTaskID())) {
            tasks.put(task.getTaskID(), task);
            checkTimeAndDuration(task);
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
            checkEpicTimeAndStatus(subtask);
            checkTimeAndDuration(subtask);
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
        tasks.keySet().forEach(id -> {
            history.remove(id);
            prioritizedTasks.removeIf(task -> task.getTaskID() == id);
        });
        tasks.clear();
    }

    @Override
    public void deleteEpicTasks() {
        List<Integer> ids = new ArrayList<>(epics.keySet());
        ids.addAll(subtasks.keySet());
        ids.forEach(id -> {
            history.remove(id);
            prioritizedTasks.removeIf(task -> task.getTaskID() == id);
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.keySet().forEach(id -> {
            history.remove(id);
            prioritizedTasks.removeIf(task -> task.getTaskID() == id);
        });
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubtaskIDs().clear();
            epic.setStatus(TaskStatus.NEW);
        });
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}