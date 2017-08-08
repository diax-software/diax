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

import me.diax.bot.SharedListener;
import me.diax.objects.API;
import me.diax.objects.Channel;
import me.diax.objects.Message;
import me.diax.objects.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.Timestamp;

public class IRCListener extends ListenerAdapter {

    private IRCAPI api;

    public IRCListener(IRCAPI api) {
        this.api = api;
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        api.getBoundListener().onMessage(new Message() {
            @Override
            public String getContent() {
                return event.getMessage();
            }

            @Override
            public User getAuthor() {
                return new User() {
                    @Override
                    public API getApi() {
                        return api;
                    }

                    @Override
                    public String getName() {
                        return event.getUser().getNick();
                    }

                    @Override
                    public String getID() {
                        return event.getUser().getUserId().toString();
                    }
                };
            }

            @Override
            public Timestamp getTimestamp() {
                return new Timestamp(System.currentTimeMillis());
            }

            @Override
            public Channel getChannel() {
                return new Channel() {
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
                };
            }
        });
    }
}