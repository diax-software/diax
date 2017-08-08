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
package me.diax.bot.irc;

import me.diax.objects.API;
import me.diax.objects.Message;
import me.diax.objects.channel.Channel;
import me.diax.objects.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class IRCListener extends ListenerAdapter {

    private IRCAPI api;

    public IRCListener(IRCAPI api) {
        this.api = api;
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        api.getBoundListener().onMessage(new Message(
                event.getMessage(),
                new User(api, event.getUser().getNick(), event.getUser().getUserId().toString()),
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
                        return event.getChannel().getChannelId().toString();
                    }
                })
        );
    }
}