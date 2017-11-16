/*
 * Copyright (C) 2016-2017 David Alejandro Rubio Escares / Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro.  If not, see http://www.gnu.org/licenses/
 */

package me.diax.diax.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;

public class WeebAPI {
    private static final String API_BASE_URL = "https://api.weeb.sh/images";
    private static final String RANDOM_IMAGE = "/random";
    private static final String ALL_TAGS = "/tags";
    private static final String ALL_TYPES = "/types";
    private final String AUTH_HEADER;
    private final OkHttpClient httpClient = new OkHttpClient();

    @Inject
    public WeebAPI(@Named("token.weebSh") String token) {
        AUTH_HEADER = token;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    public String getImage(String type, String tags, NSFW nsfw, String filetype) {
        HashMap<String, Object> queryParams = new HashMap<>();
        if (!isEmpty(tags)) queryParams.put("tags", tags);
        if (!isEmpty(type)) queryParams.put("type", type);
        queryParams.put("nsfw", nsfw == null ? "false" : nsfw.name().toLowerCase());
        if (filetype != null) queryParams.put("filetype", filetype);
        String r = request(RANDOM_IMAGE, StringUtil.urlEncodeUTF8(queryParams));
        if (r == null)
            return null;
        return new JSONObject(r).getString("url");
    }

    public String getRandomImageByType(String type, NSFW nsfw, String filetype) {
        return this.getImage(type, null, nsfw, filetype);
    }

    public String getRandomImageByType(String type, NSFW nsfw) {
        return this.getRandomImageByType(type, nsfw, null);
    }

    public String getRandomImageByTags(String tags, NSFW nsfw, String filetype) {
        return this.getImage(null, tags, nsfw, filetype);
    }

    public JSONObject getTypes() {
        String r = request(ALL_TYPES, null);
        if (r == null) return null;
        return new JSONObject(r);
    }

    public JSONObject getTags() {
        String r = request(ALL_TAGS, null);
        if (r == null) return null;
        return new JSONObject(r);
    }

    private String request(String endpoint, String e) {
        try {
            StringBuilder builder = new StringBuilder(endpoint);
            if (e != null) {
                builder.append("?");
                builder.append(e);
            }
            Request r = new Request.Builder()
                    .url(API_BASE_URL + builder.toString())
                    .addHeader("User-Agent", DiaxInfo.USER_AGENT)
                    .addHeader("Authorization", AUTH_HEADER)
                    .build();
            Response r1 = httpClient.newCall(r).execute();
            String response = r1.body().string();
            r1.close();
            return response;
        } catch (Exception ex) {
            return null;
        }
    }

    public enum NSFW {
        ONLY, TRUE, FALSE
    }
}