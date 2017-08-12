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
package me.diax.bot.twitch;


import me.diax.bot.SharedListener;
import me.diax.objects.API;
import me.diax.objects.Platform;
import me.diax.objects.User;
import me.diax.objects.channel.Channel;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.endpoints.ChannelEndpoint;


public class TwitchAPI implements API {

    private String channel;
    private TwitchClient instance = null;
    private SharedListener listener;
    private String clientid;
    private String clientsec;
    private String oauth;

    public TwitchAPI(SharedListener listener, String channel, String clientid, String clientsec, String oauth) {
        this.listener = listener;
        this.channel = channel;
        this.clientid = clientid;
        this.clientsec = clientsec;
        this.oauth = oauth;
    }

    public TwitchClient getInstance() {
        return instance;
    }



    @Override
    public void start() {

        instance = TwitchClient.builder()
                    .clientId(clientid)
                    .clientSecret(clientsec)
                    .ircCredential(new OAuthCredential(oauth))
                    .build();

        Long channelId = instance.getUserEndpoint().getUserIdByUserName(channel).get();
        ChannelEndpoint channelEndpoint = instance.getChannelEndpoint(channelId);
        channelEndpoint.registerEventListener();


    }
    @Override
    public void stop() {
        instance.disconnect()
    }

    public Platform getPlatform() {
        return Platform.TWITCH;
    }

    @Override
    public void sendMessage(Channel channel, String message) {
        this.getInstance().getIrcClient().sendMessage(channel.getName(), message);
    }

    @Override
    public void messageUser(User user, String message) {
        this.getInstance().getIrcClient().sendMessage(user.getName(), message);
    }

    @Override
    public SharedListener getBoundListener() {
        return listener;
    }

}
