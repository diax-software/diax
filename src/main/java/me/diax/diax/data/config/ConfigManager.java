package me.diax.diax.data.config;

import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.diax.diax.data.config.entities.Config;
import me.diax.diax.util.ObjectMappers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ConfigManager implements Provider<Config> {
    private static final Path path = new File("config.json").toPath();

    private static String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static void writeFile(Path path, String content) throws IOException {
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    @Getter
    @Setter
    private Config config;

    @Override
    public Config get() {
        return config;
    }

    @SneakyThrows
    public Config load() {
        String contents = readFile(path);

        config = ObjectMappers.DEFAULT.readValue(contents, Config.class);

        return config;
    }

    @SneakyThrows
    public void save() {
        if (config == null) config = new Config();

        String contents = ObjectMappers.DEFAULT.writerWithDefaultPrettyPrinter()
            .writeValueAsString(config);
        writeFile(path, contents);
    }
}
