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
package me.diax.bot;

import me.diax.objects.Message;

public class SharedListener {

    public void onMessage(Message message) {
        String content = message.getContent();
        if (!content.startsWith("<>")) return;
        message.getChannel().sendMessage("You are not registered with Diax! Please register to be able to use commands.");
        System.out.println(message.getAuthor().getName() + " " + message.getContent());
    }
}