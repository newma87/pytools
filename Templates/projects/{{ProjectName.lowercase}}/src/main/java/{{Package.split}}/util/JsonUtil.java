package {{Package}}.util;

import {{Package}}.util.joda.CustomInstantSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import java.time.Instant;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-13 18:12:29
 * Description:
 */
public final class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper;

    private JsonUtil() {
    }

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        JodaModule module = new JodaModule();
        module.addSerializer(Instant.class, new CustomInstantSerializer());
        mapper.registerModule(module);
    }

    public static String fromObject(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert to String", e);
        }
        return "";
    }

    public static <T> T toObject(String str, Class<T> cls) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        try {
            return mapper.readValue(str, cls);
        } catch (IOException e) {
            log.warn("Cannot convert to Object", e);
        }
        return null;
    }

    public static <T> List<T> toList(String str, Class<T> cls) {

        List<T> list = new ArrayList<>();
        if (StringUtils.isBlank(str)) {
            return list;
        }

        try {
            ArrayNode array = (ArrayNode) toJson(str);
            Iterator<JsonNode> nodes = array.elements();
            while (nodes.hasNext()) {
                T t = mapper.readValue(nodes.next().toString(), cls);
                list.add(t);
            }
        } catch (IOException e) {
            log.warn("Cannot convert to Object", e);
        }
        return list;
    }

    public static JsonNode toJson(String str) {
        if (StringUtils.trimToEmpty(str).startsWith("[")) {
            return toObject(str, ArrayNode.class);
        }
        return toObject(str, ObjectNode.class);
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

}