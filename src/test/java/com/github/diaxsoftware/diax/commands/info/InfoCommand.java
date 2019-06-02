package com.github.diaxsoftware.diax.commands.info;

import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.util.Util;
import com.github.diaxsoftware.diax.util.Tuple;
import com.github.rainestormee.jdacommand.CommandDescription;
import com.github.rainestormee.jdacommand.JDACommandInfo;
import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Message;

@CommandDescription(name = "Info", description = "Information for Diax", triggers = {"info"})
public class InfoCommand implements DiaxCommand {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Message message, String args) {
        JDA jda = message.getJDA();
        Util.sendMessage(message.getChannel(), String.join("\n",
                "```",
                " ___ ___   _   __  __      ___   __   __",
                "|   \\_ _| /_\\  \\ \\/ / __ _|_  ) /  \\ /  \\ ",
                "| |) | | / _ \\  >  <  \\ V // / | () | () |",
                "|___/___/_/ \\_\\/_/\\_\\  \\_//___(_)__(_)__/ ",
                "```"),
                new Tuple<>("Libraries", new String[]{
                        "\uD83D\uDCDA JDA " + JDAInfo.VERSION + " | " + JDAInfo.GITHUB,
                        "⚡ JDA-Command " + JDACommandInfo.VERSION + " | https://github.com/rainestormee/jda-command",
                        "\uD83C\uDFBC Lavaplayer " + PlayerLibrary.VERSION + " | https://github.com/sedmelluq/lavaplayer",
                        "\uD83C\uDFAE Discord Rest v" + JDAInfo.DISCORD_REST_VERSION
                }),
                new Tuple<>("Statistics", new String[]{
                        "Guilds: " + jda.getGuilds().size(),
                        "Text Channels: " + jda.getTextChannels().size(),
                        "Users: " + jda.getUsers().size(),
                }),
                new Tuple<>("Links", new String[]{
                        "Patreon: https://www.patreon.com/rainestormee",
                        "Discord: https://discord.gg/kPaVt3N",
                        "Website: https://diax-software.github.io",
                        "GitHub: https://github.com/diax-software"
                }),
                new Tuple<>("Contributors", new String[]{
                        "⭐ Raine#8544 - Developer (Head) and Owner of Diax.",
                        "https://github.com/rainestormee",
                        "Coolguy3298#2290 - Developer (Website) and VPS provider for Diax 1.0.0",
                        "https://github.com/Coolguy3289",
                        "aquarium#9352 - Developer (Chatbot / Machine Learning) for Diax 1.0.0",
                        "https://github.com/theaquarium",
                        "NachtRaben#8307 - Developer (Music) for Diax 1.0.0",
                        "https://github.com/NachtRaben",
                        "Crystal#3166 - Developer (Backend) for Diax 1.0.0",
                        "https://github.com/CrystalMare",
                        "AdrianTodt#0722 - Developer (Backend) for Diax 1.0.0",
                        "https://github.com/adriantodt",
                        "skil#3888 Designer (Branding) for Diax 1.0.0",
                        "https://github.com/skiletro",
                        "nomsy#2000 - Contributor for Diax 1.0.0",
                        "https://github.com/truency",
                        "Felix Vogel#1234 - Contributor for Diax 1.0.0",
                        "https://github.com/FelixVogel"
                })
        );
    }
}
