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
package me.diax.objects;

import me.diax.objects.channel.Channel;

import java.sql.Timestamp;

public class Message {

    private String content;
    private User author;
    private Timestamp timestamp;
    private Channel channel;

    public Message(String content, User author, Timestamp timestamp, Channel channel) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.channel = channel;
    }

    public Message(String content, User author, Channel channel) {
        this (content, author, new Timestamp(System.currentTimeMillis()), channel);
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Channel getChannel() {
        return channel;
    }
}