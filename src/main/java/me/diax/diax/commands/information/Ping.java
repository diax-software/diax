package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

@CommandDescription(
        name = "ping",
        triggers = {"ping", "pang", "peng", "pong", "pung"},
        description = "Shows the message response time and api ping.",
        attributes = @CommandAttribute(key = "private")
)
public class Ping implements Command {

    @Override
    public void execute(Message message, String s) {
        long start = System.currentTimeMillis();
        message.getChannel().sendTyping().queue(typing -> {
            long ping = System.currentTimeMillis() - start;
            message.getChannel().sendMessage(Emote.PING_PONG + " - ***P" + new String[]{"a", "e", "i", "o", "u"}[new Random().nextInt(5)] + "ng!***\n" + this.pingToEmote(ping) + " Response: " + ping + "ms\n" + Emote.BOOKS + " API: " + message.getJDA().getPing() + "ms").queue();
        });
    }

    public String pingToEmote(long ping) {
        if (ping == 69) return Emote.EGGPLANT;
        if (ping <= 0) return Emote.UPSIDE_DOWN;
        if (ping <= 10) return Emote.SMILE;
        if (ping <= 100) return Emote.SMILEY;
        if (ping <= 200) return Emote.SLIGHT_SMILE;
        if (ping <= 300) return Emote.NEUTRAL_FACE;
        if (ping <= 400) return Emote.CONFUSED;
        if (ping <= 500) return Emote.SLIGHT_FROWN;
        if (ping <= 600) return Emote.FROWNING2;
        if (ping <= 700) return Emote.WORRIED;
        if (ping <= 800) return Emote.DISAPPOINTED;
        if (ping <= 900) return Emote.SOB;
        if (ping <= 1600) return Emote.FIRE;
        if (ping <= 10000) return Emote.FIRE + Emote.FIRE;
        return Emote.FIRE + Emote.FIRE + Emote.FIRE;
    }
}