package ru.doublebyte.availabilitymonitor.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractStorage<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStorage.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    ///////////////////////////////////////////////////////////////////////////

    private String fileName = null;

    protected void setFileName(String fileName) {
        this.fileName = fileName;
    }

    ///////////////////////////////////////////////////////////////////////////

    protected synchronized ConcurrentHashMap<Long, T> load(TypeReference<Map<Long, T>> typeReference) {
        if (fileName == null) {
            throw new IllegalStateException("Storage file name is not defined");
        }

        try {
            byte[] contents = Files.readAllBytes(Paths.get(fileName));

            HashMap<Long, T> value = OBJECT_MAPPER.readValue(contents, typeReference);
            return new ConcurrentHashMap<>(value);
        } catch (Exception e) {
            logger.error("Storage load error", e);
            throw new IllegalStateException(e);
        }
    }

    protected synchronized void save(Map<Long, T> value) {
        if (fileName == null) {
            throw new IllegalStateException("Storage file name is not defined");
        }

        try (
            OutputStream out = Files.newOutputStream(Paths.get(fileName))
        ) {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, value);
        } catch (Exception e) {
            logger.error("Storage save error", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public abstract List<T> getAll();
    public abstract T get(Long id);
    public abstract T save(T value);
    public abstract void delete(Long id);
    public abstract void delete(T value);

}
