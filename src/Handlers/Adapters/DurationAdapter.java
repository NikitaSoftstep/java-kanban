package Handlers.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        Optional<Duration> dur = Optional.ofNullable(duration);
        if (dur.isEmpty()) {
            jsonWriter.value("0");
        } else {
            jsonWriter.value(duration.toMinutes());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        long minutes = jsonReader.nextLong();
        return Duration.ofMinutes(minutes);
    }
}
