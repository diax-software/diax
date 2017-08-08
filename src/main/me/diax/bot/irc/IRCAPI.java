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
import me.diax.objects.channel.Channel;
import me.diax.objects.Platform;
import me.diax.objects.User;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class IRCAPI implements API {

    private String server;
    private String channel;
    private PircBotX instance = null;
    private SharedListener listener;

    public IRCAPI(SharedListener listener, String server, String channel) {
        this.listener = listener;
        this.server   = server;
        this.channel  = channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PircBotX getInstance() {
        return instance;
    }

    @Override
    public void start() {
        instance = new PircBotX(new Configuration.Builder()
                .addListener(new IRCListener(this))
                .addServer(server)
                .addAutoJoinChannel(channel)
                .setName("Diax")
                .setRealName("Diax")
                .setLogin("diax")
                .buildConfiguration()
        );
        try {
            instance.startBot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        instance.stopBotReconnect();
    }

    @Override
    public Platform getPlatform() {
        return Platform.IRC;
    }

    @Override
    public void sendMessage(Channel channel, String message) {
        this.getInstance().sendIRC().message(channel.getName(), message);
    }

    @Override
    public void messageUser(User user, String message) {
        this.getInstance().sendIRC().message(user.getName(), message);
    }

    @Override
    public SharedListener getBoundListener() {
        return listener;
    }
}