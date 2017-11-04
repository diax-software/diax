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

import java.lang.annotation.*;

/**
 * This is for annotating classes which extend {@link Command}
 *
 * @author Comportment
 * @see Command
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandDescription {

    /**
     * This represents the name of the {@link Command} which is annotated with this {@link CommandDescription}
     *
     * @return The name of the command, should never be <code>null</code>.
     * @since 1.0.0
     */
    String name();

    /**
     * This represents the description of the {@link Command} which is annotated with this {@link CommandDescription} has.
     *
     * @return The description of the command, default as an empty string.
     * @since 1.0.6
     */
    String description() default "";

    /**
     * This represents an array of Strings which could trigger the {@link Command} which is annotated with this {@link CommandDescription} has.
     *
     * @return An Array of Strings which could trigger the command.
     * @since 1.0.0
     */
    String[] triggers();

    /**
     * This represents the minimum amount of arguments that the {@link Command} which is annotated with this {@link CommandDescription} has.
     *
     * @return The minimum amount of arguments.
     * @since 1.0.0
     */
    int args() default 0;

    /**
     * This represents an Array of {@link CommandAttribute} that the {@link Command} which is annotated with this {@link CommandDescription} has.
     *
     * @return An array of {@link CommandAttribute}s, will return an empty array if no attributes are used.
     * @since 1.0.0
     */
    CommandAttribute[] attributes() default {};
}