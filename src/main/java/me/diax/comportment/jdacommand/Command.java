/*
 * Copyright 2017 Comportment | comportment@diax.me
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.diax.comportment.jdacommand;

import net.dv8tion.jda.core.entities.Message;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * This represents a generic command.
 *
 * @author Comportment
 * @since 1.0.0
 */
public interface Command extends Comparable<Command> {

    /**
     * This is the method called on to execute the command.
     *
     * @param message The message which triggered the command.
     * @param args    The arguments of the commands.
     * @since 1.0.0
     */
    void execute(Message message, String args);

    @Override
    default int compareTo(@Nullable Command that) {
        return this.getDescription().name().compareTo(Objects.requireNonNull(that, "Commands must not be null").getDescription().name());
    }

    /**
     * Returns the value of the attribute from the key given.
     *
     * @param key The key of the {@link CommandAttribute} to find.
     * @return The value of the attribute, could be <code>null</code>.
     * @since 1.0.2
     */
    default String getAttributeValueFromKey(String key) {
        if (!hasAttribute(key)) return null;
        return Arrays.stream(getDescription().attributes()).filter(ca -> ca.key().equals(key)).findFirst().map(CommandAttribute::value).orElse(null);
    }

    /**
     * Returns the {@link CommandDescription} annotation that this command is annotated with.
     *
     * @return The {@link CommandDescription} annotation of the command.
     * @since 1.0.0
     */
    default CommandDescription getDescription() {
        return getClass().getAnnotation(CommandDescription.class);
    }

    /**
     * Returns if the command has an attribute with the matching key.
     *
     * @param key The key of the attribute
     * @return True if the command has the attribute, false if it does not.
     * @since 1.0.2
     */
    default boolean hasAttribute(String key) {
        return Arrays.stream(getDescription().attributes()).anyMatch(ca -> ca.key().equals(key));
    }
}