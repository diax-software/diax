package me.diax.diax.commands.image;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;

@CommandDescription(
        name = "catgirl",
        triggers = {
                "catgirl", "neko", "nya"
        },
        attributes = {
                @CommandAttribute(key = "private")
        }
)
public class Catgirl implements Command {

    private static String ENDPOINT = "https://nekos.life/api/neko";

    @Override
    public void execute(Message message, String s) {
        try {
            JSONObject json = new JSONObject(new OkHttpClient().newCall(new Request.Builder().url(ENDPOINT).build()).execute().body().string());
            message.getChannel().sendMessage(Embed.transparent().setTitle(Emote.CAT2 + " nyaaaa~").setImage(json.getString("neko")).build()).queue();
        } catch (Exception e) {
            message.getChannel().sendMessage(Emote.X + " - Something went wrong fetching a catgirl ;-;").queue();
            message.getChannel().sendMessage(e.getMessage()).queue();
        }
    }
}
