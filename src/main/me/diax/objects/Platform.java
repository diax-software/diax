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

/**
 *
 * This class represents a platform and everything which makes it unique.
 * When another platform is added to the supported ones, it will be added here.
 */
public enum Platform {

    DISCORD(true, true, true),
    IRC(false, false, false);

    private boolean voiceEnabled;
    private boolean attachments;
    private boolean embeds;

    Platform(boolean voiceEnabled, boolean attachments, boolean embeds) {
        this.voiceEnabled = voiceEnabled;
        this.attachments = attachments;
        this.embeds = embeds;
    }

    public boolean isVoiceEnabled() {
        return voiceEnabled;
    }

    public boolean hasAttachments() {
        return attachments;
    }

    public boolean hasEmbeds() {
        return embeds;
    }
}