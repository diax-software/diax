package me.diax.diax.commands.information

import com.google.common.collect.MultimapBuilder
import com.google.common.collect.SetMultimap
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.data.ManagedDatabase
import me.diax.diax.util.Embed
import net.dv8tion.jda.core.entities.Message
import java.util.stream.Collectors
import javax.inject.Inject

@CommandDescription(
    name = "help",
    triggers = arrayOf("help", "commands")
)
class Help
@Inject constructor(
    private val handler: CommandHandler,
    private val db: ManagedDatabase
) : Command {

    override fun execute(message: Message, s: String) {
        val guild = db.get(message.guild)
        val embed = Embed.themed().setAuthor("Diax - Help", null, null)
        if (guild.settings.categories) {
            val map: SetMultimap<String, String> = MultimapBuilder.treeKeys(nullsFirst(Comparator.naturalOrder<String>())).linkedHashSetValues().build()

            handler.commands.stream()
                .filter { !it.hasAttribute("hidden") }
                .sorted()
                .forEach { map.put(it.category?.value, it.description.name) }
            
            for (entry in map.asMap().entries) {
                embed.addField("${if (entry.key != null) "${entry.key} " else ""}Commands:", "`${entry.value.toTypedArray().joinToString("` `")}`", false)
                //embed.addField("${if (entry.key != null) "${entry.key} " else "Commands:", "`${entry.value.toTypedArray().joinToString("` `")}`", false)
            }
        } else {
            embed
                    .setDescription(
                        handler.commands.stream()
                            .filter { !it.hasAttribute("hidden") }
                            .filter { it.category?.name != "Developer" }
                            .sorted()
                            .map { it.description.name }
                            .collect(Collectors.joining("` `", "`", "`"))
                    )
        }
        message.channel.sendMessage(embed.build()).queue()


//        message.channel.sendMessage(Embed.themed()
//            .addField("__**Commands**__",
//                arrayOf(
//                    handler.commands.stream().filter { cmd -> !cmd.hasAttribute("hidden") }.sorted().map { cmd -> "`${cmd.description.name}`" }.collect(Collectors.joining(", "))
//                ).joinToString("\n"),
//                false)
//            .addField("__**Links**__", arrayOf(
//                "[Invite](https://discordapp.com/oauth2/authorize?scope=bot&client_id=295500621862404097&permissions=3198016) Invite me!",
//                "[Patreon](https://patreon.com/comportment) - Donate here to help support us!",
//                "[Discord](https://discord.gg/5sJZa2y) - Come here to chat or for help!",
//                "[Website](http://diax.me) - Check out our website!",
//                "[Upvote](https://discordbots.org/bot/295500621862404097) - Upvote me on DiscordBots!").joinToString("\n"), false
//            ).build()).queue()
    }
}
