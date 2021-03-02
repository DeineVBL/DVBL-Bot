/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 17.02.2021 @ 19:11:22
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * GeniusClient.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.utils;

import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

public class GeniusClient {

    private final OkHttpClient httpClient;
    private final String API_BASE = "https://api.genius.com";
    private final String GENIUS_BASE = "https://genius.com";

    public GeniusClient(String token) {
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + token);
                    return chain.proceed(requestBuilder.build());
                }).build();
    }

    public String searchSong(String query) {
        Request request = new Request.Builder()
                .url(API_BASE + "/search?q=" + query)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            assert response.body() != null;
            return GENIUS_BASE + new JSONObject(response.body().string()).getJSONObject("response").getJSONArray("hits").getJSONObject(0).getJSONObject("result").getString("path");
        } catch (IOException | JSONException e) {
            return null;
        }
    }

    public String getLyrics(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            document.select("br").append("\\n");
            document.select("p").prepend("\\n\\n");
            Element element = document.selectFirst(".lyrics");
            if (!element.hasText()) return "Something went wrong while fetching lyrics ...";
            return Jsoup.clean(element.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).replace("\\n", "\n");
        } catch (IOException e) {
            System.out.println("\"[GeniusClient] An error occurred while getting lyrics!\"" + e);
            return null;
        }
    }

    public String getTitle(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.title().split(" \\| Genius Lyrics")[0];
        } catch (IOException e) {
            System.out.println("[GeniusClient] An error occurred while getting lyrics!" + e);
            return null;
        }
    }
}