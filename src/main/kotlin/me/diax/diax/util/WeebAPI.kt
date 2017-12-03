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

package me.diax.diax.util

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class WeebAPI @Inject
constructor(@param:Named("token.weebSh") private val AUTH_HEADER: String) {
    private val httpClient = OkHttpClient()

    val types: JSONObject?
        get() {
            val r = request(ALL_TYPES, null) ?: return null
            return JSONObject(r)
        }

    val tags: JSONObject?
        get() {
            val r = request(ALL_TAGS, null) ?: return null
            return JSONObject(r)
        }

    fun getImage(type: String?, tags: String?, nsfw: NSFW?, filetype: String?): String? {
        val queryParams = HashMap<String, Any>()
        if (!isEmpty(tags)) queryParams.put("tags", tags!!)
        if (!isEmpty(type)) queryParams.put("type", type!!)
        queryParams.put("nsfw", nsfw?.name?.toLowerCase() ?: "false")
        if (filetype != null) queryParams.put("filetype", filetype)
        val r = request(RANDOM_IMAGE, StringUtil.urlEncodeUTF8(queryParams)) ?: return null
        return JSONObject(r).getString("url")
    }

    fun getRandomImageByType(type: String, nsfw: NSFW, filetype: String?): String? {
        return this.getImage(type, null, nsfw, filetype)
    }

    fun getRandomImageByType(type: String, nsfw: NSFW): String? {
        return this.getRandomImageByType(type, nsfw, null)
    }

    fun getRandomImageByTags(tags: String, nsfw: NSFW, filetype: String): String? {
        return this.getImage(null, tags, nsfw, filetype)
    }

    private fun request(endpoint: String, e: String?): String? {
        try {
            val builder = StringBuilder(endpoint)
            if (e != null) {
                builder.append("?")
                builder.append(e)
            }
            val r = Request.Builder()
                .url(API_BASE_URL + builder.toString())
                .addHeader("User-Agent", DiaxInfo.USER_AGENT)
                .addHeader("Authorization", AUTH_HEADER)
                .build()
            val r1 = httpClient.newCall(r).execute()
            val response = r1.body()!!.string()
            r1.close()
            return response
        } catch (ex: Exception) {
            return null
        }

    }

    enum class NSFW {
        ONLY, TRUE, FALSE
    }

    companion object {
        private val API_BASE_URL = "https://api.weeb.sh/images"
        private val RANDOM_IMAGE = "/random"
        private val ALL_TAGS = "/tags"
        private val ALL_TYPES = "/types"

        fun isEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }
    }
}