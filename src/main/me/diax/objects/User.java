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
 * This class is to represent a Diax user object.
 */
public class User {

    private API api;
    private String name;
    private String id;

    public User(API api, String name, String id) {
        this.api = api;
        this.name = name;
        this.id = id;
    }

    /**
     *
     * @return the API associated with this User
     */

    public API getApi() {
        return api;
    }

    /**
     *
     * @return the username of the User
     */

    public String getName() {
        return name;
    }

    /**
     * This returns a UUID if the User is an IRC user,
     * or a long Snowflake (as a string) if the User is a Discord user.
     *
     * @return the ID of the User, as a String
     */

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return getName() + " [" + getID() + "]";
    }
}