package com.github.diaxsoftware.diax.commands.info;

import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.util.Util;
import com.github.rainestormee.jdacommand.CommandDescription;
import net.dv8tion.jda.api.entities.Message;

@CommandDescription(name = "Ping", description = "Pong!", triggers = {"ping"})
public class PingCommand implements DiaxCommand {

    @Override
    public void execute(Message message, String args) {
        message.getJDA().getRestPing().queue(
                ping -> Util.sendMessage(message.getChannel(), "\uD83C\uDFD3 Pong: " + ping + "ms"),
                ping -> Util.sendError(message.getChannel())
        );
    }
}
