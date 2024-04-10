package taskmanager;

import Exceptions.TaskManagerIOException;
import category.TaskCategory;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskTypes;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File savePath;

    public FileBackedTaskManager(File savePath) {
        this.savePath = savePath;
    }

    private void save() {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(savePath));

            br.write("id,type,name,status,description,epic" + "\n");

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

        } catch (TaskManagerIOException | IOException e) {
            System.err.println(e.getMessage());
        }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (TaskManagerIOException | IOException e) {
                System.err.println("Ошибка закрытия BufferedWriter: " + e.getMessage());
            }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = null;
        int counter;
        try {
            fileBackedTaskManager = new FileBackedTaskManager(file);
            List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)));
            for (String line : lines) {
                if (line.matches("^\\d+,(TASK|EPIC|SUBTASK),.*$")) {
                    String[] split = line.split(",");
                    String id = split[0];
                    counter = id.matches("-?\\d+") ? Integer.parseInt(id) : 0;
                    fileBackedTaskManager.setCounter(counter);
                    String type = split[1];
                    String title = split[2];
                    String category = split[3];
                    String description = split[4];
                    String epicID = split.length == 6 ? split[5] : "0";

                    if (type.equals(TaskTypes.TASK.toString())) {
                        Task task = new Task(title, description);
                        task.setTaskID(Integer.parseInt(id));
                        task.setType(TaskTypes.valueOf(type));
                        task.setCategory(TaskCategory.valueOf(category));
                        fileBackedTaskManager.tasks.put(Integer.parseInt(id), task);
                    } else if (type.equals(TaskTypes.EPIC.toString())) {
                        Epic epic = new Epic(title, description);
                        epic.setTaskID(Integer.parseInt(id));
                        epic.setType(TaskTypes.valueOf(type));
                        epic.setCategory(TaskCategory.valueOf(category));
                        fileBackedTaskManager.epics.put(Integer.parseInt(id), epic);
                    } else if (type.equals(TaskTypes.SUBTASK.toString())) {
                        Subtask subtask = new Subtask(title, description);
                        subtask.setTaskID(Integer.parseInt(id));
                        subtask.setType(TaskTypes.valueOf(type));
                        subtask.setCategory(TaskCategory.valueOf(category));
                        subtask.setEpicID(Integer.parseInt(epicID));
                        fileBackedTaskManager.subtasks.put(Integer.parseInt(id), subtask);
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
            throw new TaskManagerIOException("Ошибка чтения с файла");
        } catch (TaskManagerIOException | IOException e) {
            System.out.println(e.getMessage());
        }
        return fileBackedTaskManager;
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
