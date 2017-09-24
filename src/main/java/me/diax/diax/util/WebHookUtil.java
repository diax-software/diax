package me.diax.diax.util;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;

public class WebHookUtil {

    public static void log(JDA jda, String title, String message) {
        jda.getTextChannelById("357109761533149185").getWebhooks().queue(whs -> {
            WebhookClient wh = whs.get(0).newClient().build();
            wh.send(new WebhookMessageBuilder().addEmbeds(Embed.transparent().setTitle(title).setDescription(message).build()).setUsername("Diax Logging").build());
            wh.close();
        });
    }

    public static void announce(JDA jda, String message) {
        jda.getTextChannelById("343552348670656532").getWebhooks().queue(whs -> {
            WebhookClient wh = whs.get(0).newClient().build();
            wh.send(new WebhookMessageBuilder().addEmbeds(Embed.transparent().setTitle(Emote.SPARKLES + " Diax Announcements").setDescription(message).build()).setUsername("Diax Announcements").build());
            wh.close();
        });
    }
}