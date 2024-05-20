package server.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import task.TaskTypes;

import java.io.IOException;

public class TaskTypeAdapter extends TypeAdapter<TaskTypes> {

    @Override
    public void write(JsonWriter jsonWriter, TaskTypes taskType) throws IOException {
        jsonWriter.value(taskType.name());
    }

    @Override
    public TaskTypes read(JsonReader jsonReader) {
        TaskTypes result = TaskTypes.TASK;
        try {
            result = TaskTypes.valueOf(jsonReader.nextString());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при чтении значения TaskTypes: некорректное значение");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при чтении значения TaskTypes");
            e.printStackTrace(); // Вывод ошибки в консоль
        }
        return result;
    }
}