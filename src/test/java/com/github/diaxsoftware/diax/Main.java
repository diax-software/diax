package com.github.diaxsoftware.diax;

import com.github.diaxsoftware.diax.commands.info.InfoCommand;
import com.github.diaxsoftware.diax.commands.info.PingCommand;
import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.commands.info.SuggestCommand;
import com.github.rainestormee.jdacommand.CommandDescription;
import com.github.rainestormee.jdacommand.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class Main extends ListenerAdapter {

    private CommandHandler<Message> handler;

    public static void main(String[] args) throws Exception {
        new Main().main(args[0]);
    }

    public void main(String token) throws Exception {
        JDA jda = new JDABuilder().setToken(token).setStatus(OnlineStatus.ONLINE).setActivity(Activity.of(Activity.ActivityType.DEFAULT, "<>help | Coming Soon! | discord.gg/Q5HtCVE")).build().awaitReady();
        jda.addEventListener(this);
        handler = new CommandHandler<Message>(){{
            registerCommand(new PingCommand());
            registerCommand(new InfoCommand());
            registerCommand(new SuggestCommand());
        }};
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) return;
        System.out.println(event.getGuild().getName() + " | #" + event.getChannel().getName() + " : " + event.getMessage().getContentRaw());
        if (!event.getMessage().getContentRaw().startsWith("<>") || event.getMessage().getAuthor().isBot()) return;
        String[] args = event.getMessage().getContentDisplay().replaceFirst("<>", "").split("\\s+");
        DiaxCommand command = (DiaxCommand) handler.findCommand(args[0]);
        args = Arrays.copyOfRange(args, 1, args.length);
        if (command == null) return;
        command.execute(event.getMessage(), String.join(" ", args));
    }
}
