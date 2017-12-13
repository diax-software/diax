package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


@CommandDescription(
        name = "eval",
        triggers = ["eval", "hack"],
        attributes = [
            CommandAttribute(key = "permission", value = "developer")
        ]
)
class Eval : Command {

    override fun execute(trigger: Message, truncated: String) {
        val engine = this.addMethods(ScriptEngineManager().getEngineByName("nashorn"), trigger)
        var output: String
        try {
            output = "${engine.eval(arrayOf("load('nashorn:mozilla_compat.js');", "imports = new JavaImporter(java.util, java.io);", "(function(){", "with(imports){", truncated, "}", "})()").joinToString("\n"))}"
        } catch (exception: Exception) {
            output = exception.localizedMessage
        }

        trigger.channel.sendMessage(Embed.themed().setDescription(String.format("```js\n%s ```", output)).build()).queue()
    }

    private fun addMethods(engine: ScriptEngine, trigger: Message): ScriptEngine {
        engine["jda"] = trigger.jda
        engine["message"] = trigger
        engine["event"] = trigger
        engine["guild"] = trigger.guild
        engine["channel"] = trigger.channel
        engine["embed"] = Embed
        return engine
    }

    private operator fun ScriptEngine.set(key: String?, value: Any?) {
        put(key, value)
    }
}

