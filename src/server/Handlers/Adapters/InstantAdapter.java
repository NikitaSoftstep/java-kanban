package server.Handlers.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class InstantAdapter extends TypeAdapter<Instant> {

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        Optional<Instant> ins = Optional.ofNullable(instant);
        if (ins.isEmpty()) {
            jsonWriter.value("0");
        } else {
            LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            jsonWriter.value(formatter.format(local));
        }
    }

    @Override
    public Instant read(JsonReader jsonReader) throws IOException {
        LocalDateTime local = LocalDateTime.parse(jsonReader.nextString(), formatter);
        Instant instantTime = local.atOffset(ZoneOffset.UTC).toInstant();
        return instantTime;
    }
}
