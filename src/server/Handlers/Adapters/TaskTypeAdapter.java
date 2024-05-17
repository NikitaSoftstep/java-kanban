package server.Handlers.Adapters;

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
    public TaskTypes read(JsonReader jsonReader) throws IOException {
        try {
            return TaskTypes.valueOf(jsonReader.nextString());
        } catch (IllegalArgumentException e) {
            return TaskTypes.TASK;
        }
    }
}