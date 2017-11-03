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

/**
 * This can be used for extra attributes in the {@link CommandDescription} annotation.
 *
 * @author Comportment
 * @since 1.0.0
 * @see CommandDescription
 */
public @interface CommandAttribute {

    /**
     * The key which identifies the attribute.
     *
     * @return The key of the description.
     * @since 1.0.0
     */
    String key();

    /**
     * The value of the attribute.
     *
     * @return The value of the description, could be <code>null</code>.
     * @since 1.0.0
     * Changed to allow for no value present in 1.0.2
     */
    String value() default "";
}