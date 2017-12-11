package me.diax.diax.data.config

import com.google.inject.Provider
import me.diax.diax.data.config.entities.Config
import me.diax.diax.util.ObjectMappers
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class ConfigManager : Provider<Config> {
    var config: Config = load()

    override fun get(): Config = config

    fun load(): Config {
        val contents = readFile(path)

        config = ObjectMappers.DEFAULT.readValue(contents, Config::class.java)

        return config
    }

    fun reset() {
        config = Config()
    }

    fun save() {
        val contents = ObjectMappers.DEFAULT.writerWithDefaultPrettyPrinter()
                .writeValueAsString(config)
        writeFile(path, contents)
    }

    companion object {
        private val path = File("config.json").toPath()

        private fun readFile(path: Path): String {
            return String(Files.readAllBytes(path), StandardCharsets.UTF_8)
        }

        private fun writeFile(path: Path, content: String) {
            Files.write(path, content.toByteArray(StandardCharsets.UTF_8))
        }
    }
}
