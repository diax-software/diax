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

import me.diax.bot.irc.IRCAPI;

public class Main {

    /**
     * Calls a new instance of this class using the {@link #main()} method.
     *
     * @param args Not needed, launched at runtime.
     * @author comportment
     * @since 0.0.0
     */
    public static void main(String[] args) throws Exception {
        new Main().main();
    }

    /**
     * Non static instance of this class to launch code and keep with conventions.
     *
     * @author comportment
     * @since 0.0.0
     */
    private void main() throws Exception {
        SharedListener listener = new SharedListener();
        new Thread(() -> new IRCAPI(listener, "irc.domirc.net", "##diax").start()).start();
        // new Thread(() -> new DiscordAPI(listener, "").start()).start();
    }
}