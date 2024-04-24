package taskmanager;

import exceptions.TaskManagerIOException;
import category.TaskStatus;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskTypes;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File savePath;

    public FileBackedTaskManager(File savePath) {
        this.savePath = savePath;
    }



    private void save() {

        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(savePath));

            br.write("id,type,name,description,status,startTime,duration,epic" + "\n");

            List<Task> taskList = new ArrayList<>(tasks.values());
            for (Task task : taskList) {
                br.write(task.toString() + "\n");
            }

            List<Epic> epicList = new ArrayList<>(epics.values());
            for (Epic epic : epicList) {
                br.write(epic.toString() + "\n");
            }

            List<Subtask> subtaskList = new ArrayList<>(subtasks.values());
            for (Subtask subtask : subtaskList) {
                br.write(subtask.toString() + "\n");
            }

            br.write("\n" + "ids" + "\n");

            List<Task> tasks = new ArrayList<>(history.getHistory());
            if (!tasks.isEmpty()) {
                List<Integer> ids = new ArrayList<>();
                for (Task task : tasks) {
                    ids.add(task.getTaskID());
                }

                for (Integer id : ids) {
                    br.write(id + ",");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new TaskManagerIOException("Runtime exception: возможно ошибка поиска файлов или ресурсов");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (TaskManagerIOException | IOException e) {
                System.out.println("Ошибка закрытия BufferedWriter: " + e.getMessage());
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = null;
        int counter = 0;
        try {
            fileBackedTaskManager = new FileBackedTaskManager(file);
            List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)));
            for (String line : lines) {
                if (line.matches("^\\d+,(TASK|EPIC|SUBTASK),.*$")) {
                    String[] split = line.split(",");
                    String id = split[0];
                    counter = (id.matches("-?\\d+") && Integer.parseInt(id) > counter) ?
                            Integer.parseInt(id) : counter;
                    fileBackedTaskManager.setCounter(counter);
                    String type = split[1];
                    String title = split[2];
                    String description = split[3];
                    String status = split[4];
                    String startTime = !split[5].equals("null") ? split[5] : null;
                    String duration = !split[6].equals("null") ? split[6] : null;
                    String epicID = split.length == 8 ? split[7] : "0";

                    if (type.equals(TaskTypes.TASK.toString())) {
                        Task task = new Task(title, description, TaskStatus.valueOf(status));
                        task.setTaskID(Integer.parseInt(id));
                        task.setType(TaskTypes.valueOf(type));
                        if (startTime != null) {
                            LocalDateTime time = LocalDateTime.parse(startTime, Task.formatter);
                            task.setStartTime(time.toInstant(ZoneOffset.UTC));
                            task.setDuration(Duration.ofMinutes(Integer.parseInt(duration)));
                        }
                        fileBackedTaskManager.tasks.put(Integer.parseInt(id), task);
                    } else if (type.equals(TaskTypes.EPIC.toString())) {
                        Epic epic = new Epic(title, description, TaskStatus.valueOf(status));
                        epic.setTaskID(Integer.parseInt(id));
                        epic.setType(TaskTypes.valueOf(type));
                        if (startTime != null) {
                            LocalDateTime time = LocalDateTime.parse(startTime, Task.formatter);
                            epic.setStartTime(time.toInstant(ZoneOffset.UTC));
                            epic.setDuration(Duration.ofMinutes(Integer.parseInt(duration)));
                        }
                        fileBackedTaskManager.epics.put(Integer.parseInt(id), epic);
                    } else if (type.equals(TaskTypes.SUBTASK.toString())) {
                        Subtask subtask = new Subtask(title, description, TaskStatus.valueOf(status));
                        subtask.setTaskID(Integer.parseInt(id));
                        subtask.setType(TaskTypes.valueOf(type));
                        subtask.setEpicID(Integer.parseInt(epicID));
                        if (startTime != null) {
                            LocalDateTime time = LocalDateTime.parse(startTime, Task.formatter);
                            subtask.setStartTime(time.toInstant(ZoneOffset.UTC));
                            subtask.setDuration(Duration.ofMinutes(Integer.parseInt(duration)));
                        }
                        fileBackedTaskManager.subtasks.put(Integer.parseInt(id), subtask);


//                        int newID = increaseCounter();
//                        subtask.setTaskID(newID);
//                        subtasks.put(newID, subtask);
//                        Epic epic = epics.get(subtask.getEpicID());
//                        epic.addSubtaskID(newID);
//                        int epicID = subtask.getEpicID();
//                        correctEpicStatus(epicID);
                    }
                }

            }
            List<String> historyLines = Files.readAllLines(file.toPath());
            String lastLine = historyLines.getLast();

                char firstChar = lastLine.charAt(0);
                if (Character.isDigit(firstChar)) {
                    String[] stringIds = lastLine.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String id : stringIds) {
                        ids.add(Integer.parseInt(id));
                    }

                    for (Integer id : ids) {
                        if (fileBackedTaskManager.getSimpleTask(id) != null &&
                                fileBackedTaskManager.getSimpleTask(id).getClass() == Task.class) {
                            fileBackedTaskManager.history.add(fileBackedTaskManager.getSimpleTask(id));
                        } else if (fileBackedTaskManager.getEpicTask(id) != null &&
                                fileBackedTaskManager.getEpicTask(id).getClass() == Epic.class) {
                            fileBackedTaskManager.history.add(fileBackedTaskManager.getEpicTask(id));
                        } else if (fileBackedTaskManager.getSubtask(id) != null &&
                                fileBackedTaskManager.getSubtask(id).getClass() == Subtask.class) {
                            fileBackedTaskManager.history.add(fileBackedTaskManager.getSubtask(id));
                        }
                    }

                }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new TaskManagerIOException("Runtime exception: возможна ошибка поиска ресурсов или файла");
        }
        TreeSet<Task> priorityTasks;
        priorityTasks = collectPrioritezedTasks(fileBackedTaskManager.tasks,
                fileBackedTaskManager.subtasks,
                fileBackedTaskManager);
        fileBackedTaskManager.setPrioritizedTasks(priorityTasks);
        return fileBackedTaskManager;
    }


    public static TreeSet<Task> collectPrioritezedTasks(Map<Integer, Task> tasks,
                                                        Map<Integer, Subtask> subtasks,
                                                        FileBackedTaskManager manager) {
        TreeSet<Task> allTimeTasks = new TreeSet<>(Comparator.comparing(Task::getTaskID));

        tasks.values()
                .stream()
                .filter(task -> task.getStartTime() != null && task.getDuration() != null)
                .forEach(allTimeTasks::add);

        subtasks.values()
                .stream()
                .filter(subtask -> subtask.getStartTime() != null && subtask.getDuration() != null)
                .forEach(allTimeTasks::add);

        allTimeTasks.forEach(manager::checkTimeAndDuration);

        if (!allTimeTasks.isEmpty()) {
            return manager.getPrioritizedTasks();
        }
        return new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }
    @Override
    public void addSimpleTask(Task task) {
        super.addSimpleTask(task);
        save();
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task getSimpleTask(int taskID) {
        Task task = super.getSimpleTask(taskID);
        save();
        return task;
    }

    @Override
    public Epic getEpicTask(int taskID) {
        Epic epic = super.getEpicTask(taskID);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int taskID) {
        Subtask subtask = super.getSubtask(taskID);
        save();
        return subtask;
    }

    @Override
    public void deleteSimpleTask(int taskID) {
        super.deleteSimpleTask(taskID);
        save();
    }

    @Override
    public void deleteEpic(int taskID) {
        super.deleteEpic(taskID);
        save();
    }

    @Override
    public void deleteSubtask(int taskID) {
        super.deleteSubtask(taskID);
        save();
    }

    @Override
    public void updateSimpleTask(Task task) {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSimpleTasks() {
        super.deleteSimpleTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public ArrayList<Task> getSimpleTasks() {
        ArrayList<Task> taskList = super.getSimpleTasks();
        save();
        return taskList;
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        ArrayList<Epic> epicList = super.getEpicTasks();
        save();
        return epicList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskList = super.getSubtasks();
        save();
        return subtaskList;
    }


}
