/*
Copyright © 2017 Ryan Arrowsmith

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package me.diax.bot.discord;

import me.diax.objects.API;
import me.diax.objects.channel.Channel;
import me.diax.objects.Message;
import me.diax.objects.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

    private DiscordAPI api;

    public DiscordListener(DiscordAPI api) {
        this.api = api;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().equals(event.getJDA().getSelfUser())) return;
        api.getBoundListener().onMessage(new Message(
                event.getMessage().getRawContent(),
                new User(api, event.getAuthor().getName(), event.getAuthor().getId()),
                new Channel() {
                    @Override
                    public API getAPI() {
                        return api;
                    }

                    @Override
                    public String getName() {
                        return event.getChannel().getName();
                    }

                    @Override
                    public String getId() {
                        return event.getChannel().getId();
                    }
                })
        );
    }
}