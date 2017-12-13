package me.diax.diax.commands

import br.com.brjdevs.java.utils.texts.StringUtils
import me.diax.comportment.jdacommand.Command
import net.dv8tion.jda.core.entities.Message

abstract class ArgsCommand(private val expectedArgs: Int = 0) : Command {
    override fun execute(message: Message, args: String) {
        execute(message, splitArgs(args))
    }

    abstract fun execute(message: Message, args: Array<String>)

    private fun splitArgs(args: String): Array<String> {
        return StringUtils.efficientSplitArgs(args, expectedArgs)
    }
}
