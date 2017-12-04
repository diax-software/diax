package me.diax.diax.util

import me.diax.diax.util.style.Embed
import mu.KLogging
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.webhook.WebhookMessageBuilder

object WebHookUtil : KLogging() {

    fun log(jda: JDA, title: String, message: String) {
        logger.info(message)
        try {
            jda.getTextChannelById("357109761533149185").webhooks.queue { whs ->
                val wh = whs[0].newClient().build()
                wh.send(WebhookMessageBuilder().addEmbeds(Embed.themed().setTitle(title).setDescription(message).build()).setUsername("Diax Logging").build())
                wh.close()
            }
        } catch (ignored: Exception) {
        }

    }

    fun err(jda: JDA, message: String) {
        try {
            WebHookUtil.log(jda, Emote.X + " - Error!", message)
        } catch (ignored: Exception) {
        }

    }

    fun announce(jda: JDA, message: String) {
        jda.getTextChannelById("343552348670656532").webhooks.queue { whs ->
            val wh = whs[0].newClient().build()
            wh.send(WebhookMessageBuilder().addEmbeds(Embed.themed().setTitle(Emote.SPARKLES + " Diax Announcement").setDescription(message).build()).setUsername("Diax Announcements").build())
            wh.close()
        }
    }

    fun report(jda: JDA, message: String) {
        jda.getTextChannelById("356682048510885889").webhooks.queue { whs ->
            val wh = whs[0].newClient().build()
            wh.send(WebhookMessageBuilder().addEmbeds(Embed.themed().setTitle(Emote.BUG + " Diax Bug Report").setDescription(message).build()).setUsername("Diax Bug Reports").build())
            wh.close()
        }
    }

    fun suggest(jda: JDA, message: String) {
        jda.getTextChannelById("356564127864586240").webhooks.queue { whs ->
            val wh = whs[0].newClient().build()
            wh.send(WebhookMessageBuilder().addEmbeds(Embed.themed().setTitle(Emote.SPARKLES + " Diax Suggestion").setDescription(message).build()).setUsername("Diax Suggestions").build())
            wh.close()
        }
    }

}