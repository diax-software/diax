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

import me.diax.objects.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class DiscordAPI implements API {

    private JDA instance = null;
    private String token;

    public DiscordAPI(String token) {
        this.token = token;
    }

    @Override
    public JDA getInstance() {
        return instance;
    }

    @Override
    public void start() {
        try {
            instance = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new DiscordListener(this))
                    .buildBlocking();
        } catch (Exception e) {
            start();
        }
    }

    @Override
    public void stop() {
        instance.shutdown();
        instance = null;
    }

    @Override
    public Platform getPlatform() {
        return Platform.DISCORD;
    }

    @Override
    public void sendMessage(Channel channel, String message) {
        this.getInstance().getTextChannelById(channel.getId()).sendMessage(message).queue();
    }

    @Override
    public void messageUser(User user, String message) {
        this.getInstance().getPrivateChannelById(user.getID()).sendMessage(message).queue();
    }
}