package {{Package}}.util.joda;

import java.io.IOException;
import java.time.Instant;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom Jackson serializer for transforming a Joda LocalDate object to JSON.
 */
public class CustomInstantSerializer extends JsonSerializer<Instant> {

    private static DateTimeFormatter formatter = DateTimeFormat
            .forPattern("yyyy-MM-dd");

    @Override
    public void serialize(Instant value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(value.toString());
        
    }
}
