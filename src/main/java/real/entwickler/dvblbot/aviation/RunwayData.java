/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 28.03.2021 @ 22:05:41
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * RunwayData.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.aviation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import real.entwickler.dvblbot.Bot;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RunwayData extends ICommand {

    public RunwayData(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) throws MalformedURLException, IOException, InterruptedException {

        if (args[1].length() == 4) {


            HttpRequest runwayrequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/icao/" + args[1] + "/runways"))
                    .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                    .header("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> runwayresponse = HttpClient.newHttpClient().send(runwayrequest, HttpResponse.BodyHandlers.ofString());


            if (!runwayresponse.body().equals("[]")) {
                JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(runwayresponse.body()));

                JsonArray jsonArray = jsonElement2.getAsJsonArray();

                JsonObject jsonRunway1 = jsonArray.get(0).getAsJsonObject();
                JsonObject jsonRunway2 = jsonArray.get(1).getAsJsonObject();

                JsonObject jsonRunway3 = null;
                JsonObject jsonRunway4 = null;
                JsonObject jsonRunway5 = null;
                JsonObject jsonRunway6 = null;
                JsonObject jsonRunway7 = null;
                JsonObject jsonRunway8 = null;


                if (jsonArray.size() > 2) {
                    jsonRunway3 = jsonArray.get(2).getAsJsonObject();
                }
                if (jsonArray.size() > 3) {
                    jsonRunway4 = jsonArray.get(3).getAsJsonObject();
                }
                if (jsonArray.size() > 4) {
                    jsonRunway5 = jsonArray.get(4).getAsJsonObject();
                }
                if (jsonArray.size() > 5) {
                    jsonRunway6 = jsonArray.get(5).getAsJsonObject();
                }
                if (jsonArray.size() > 6) {
                    jsonRunway7 = jsonArray.get(6).getAsJsonObject();
                }
                if (jsonArray.size() > 7) {
                    jsonRunway8 = jsonArray.get(7).getAsJsonObject();
                }

                CloseableHttpClient httpClient = HttpClients.createDefault();


                HttpGet request = new HttpGet("https://www.airport-data.com/api/ap_info.json?icao=" + args[1]);
                HttpGet requestelevation = new HttpGet("https://airports-api.s3-us-west-2.amazonaws.com/icao/" + args[1] + ".json");

                // add request headers

                try (CloseableHttpResponse responseelevation = httpClient.execute(requestelevation)) {
                    HttpEntity httpEntity = responseelevation.getEntity();
                    Header header = httpEntity.getContentType();

                    String resultelevation = EntityUtils.toString(httpEntity);

                    try (CloseableHttpResponse response = httpClient.execute(request)) {

                        // Get HttpResponse Status

                        HttpEntity entity = response.getEntity();
                        Header headers = entity.getContentType();

                        if (entity != null) {
                            // return it as a String
                            String result = EntityUtils.toString(entity);

                            JsonElement jsonElement = JsonParser.parseReader(new StringReader(result));
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            String icao = jsonObject.get("icao").getAsString();
                            String iata = jsonObject.get("iata").getAsString();
                            String name = jsonObject.get("name").getAsString();
                            String location = jsonObject.get("location").getAsString();
                            String country = jsonObject.get("country").getAsString();
                            String country_code = jsonObject.get("country_code").getAsString();

                            String runwaysurface = jsonRunway1.get("surface").getAsString();
                            Boolean runwaylightened = jsonRunway1.get("hasLighting").getAsBoolean();

                            String runway1 = jsonRunway1.get("name").getAsString();
                            Integer runway1lengthm = jsonRunway1.get("length").getAsJsonObject().get("meter").getAsInt();
                            Integer runway1lengthft = jsonRunway1.get("length").getAsJsonObject().get("feet").getAsInt();

                            String runway2 = jsonRunway2.get("name").getAsString();
                            Integer runway2lengthm = jsonRunway2.get("length").getAsJsonObject().get("meter").getAsInt();
                            Integer runway2lengthft = jsonRunway1.get("length").getAsJsonObject().get("feet").getAsInt();

                            String runway3 = null;
                            Integer runway3lengthm = null;
                            Integer runway3lengthft = null;
                            if (jsonRunway3 != null) {
                                runway3 = jsonRunway3.get("name").getAsString();
                                runway3lengthm = jsonRunway3.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway3lengthft = jsonRunway3.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            Integer runway4lengthft = null;
                            Integer runway4lengthm = null;
                            String runway4 = null;
                            if (jsonRunway4 != null) {
                                runway4 = jsonRunway4.get("name").getAsString();
                                runway4lengthm = jsonRunway4.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway4lengthft = jsonRunway4.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway5 = null;
                            Integer runway5lengthm = null;
                            Integer runway5lengthft = null;
                            if (jsonRunway5 != null) {
                                runway5 = jsonRunway5.get("name").getAsString();
                                runway5lengthm = jsonRunway5.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway5lengthft = jsonRunway5.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            Integer runway6lengthft = null;
                            Integer runway6lengthm = null;
                            String runway6 = null;
                            if (jsonRunway6 != null) {
                                runway6 = jsonRunway6.get("name").getAsString();
                                runway6lengthm = jsonRunway6.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway6lengthft = jsonRunway6.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway7 = null;
                            Integer runway7lengthm = null;
                            Integer runway7lengthft = null;
                            if (jsonRunway7 != null) {
                                runway7 = jsonRunway7.get("name").getAsString();
                                runway7lengthm = jsonRunway7.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway7lengthft = jsonRunway7.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway8 = null;
                            Integer runway8lengthm = null;
                            Integer runway8lengthft = null;
                            if (jsonRunway8 != null) {
                                runway8 = jsonRunway8.get("name").getAsString();
                                runway8lengthm = jsonRunway8.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway8lengthft = jsonRunway8.get("length").getAsJsonObject().get("feet").getAsInt();
                            }


                            String airporturl = "https://www.airport-data.com/" + jsonObject.get("link").getAsString();

                            JsonElement jsonElement1 = JsonParser.parseReader(new StringReader(resultelevation));
                            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                            Integer elevation = jsonObject1.get("elevation").getAsInt();

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                            embedBuilder.setTitle(name + " " + "[" + icao + " / " + iata + "]", airporturl);
                            embedBuilder.setDescription("Runways " + "[" + runwaysurface + "]");
                            embedBuilder.setColor(Color.CYAN);
                            embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                            embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                            embedBuilder.addField("~ " + runway1, runway1lengthm + "m " + "(" + runway1lengthft + "ft)", true);
                            embedBuilder.addField("~ " + runway2, runway2lengthm + "m " + "(" + runway2lengthft + "ft)", true);

                            if (runway3 != null)
                                embedBuilder.addField("~ " + runway3, runway3lengthm + "m " + "(" + runway3lengthft + "ft)", true);
                            if (runway4 != null)
                                embedBuilder.addField("~ " + runway4, runway4lengthm + "m " + "(" + runway4lengthft + "ft)", true);
                            if (runway5 != null)
                                embedBuilder.addField("~ " + runway5, runway5lengthm + "m " + "(" + runway5lengthft + "ft)", true);
                            if (runway6 != null)
                                embedBuilder.addField("~ " + runway6, runway6lengthm + "m " + "(" + runway6lengthft + "ft)", true);
                            if (runway7 != null)
                                embedBuilder.addField("~ " + runway7, runway7lengthm + "m " + "(" + runway7lengthft + "ft)", true);
                            if (runway8 != null)
                                embedBuilder.addField("~ " + runway8, runway8lengthm + "m " + "(" + runway8lengthft + "ft)", true);
                            if (runwaylightened) {
                                embedBuilder.addField("Beleuchtet", "ja" + " 🟢", false);
                            } else {
                                embedBuilder.addField("Beleuchtet", "nein" + " 🔴", false);
                            }
                            textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                exitMessage.addReaction("✈").queue();
                            });
                        }
                    }
                }
            } else {
                Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
            }
        } else {
            HttpRequest icaorequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/search/term?q=" + args[1] + "&limit=10"))
                    .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                    .header("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> icaoresponse = HttpClient.newHttpClient().send(icaorequest, HttpResponse.BodyHandlers.ofString());

            JsonElement jsonElement3 = JsonParser.parseReader(new StringReader(icaoresponse.body()));
            JsonObject jsonObject3 = jsonElement3.getAsJsonObject();

            if (jsonObject3.get("items").getAsJsonArray().size() >= 1) {

            }
            String icaocode = jsonObject3.get("items").getAsJsonArray().get(0).getAsJsonObject().get("icao").getAsString();

            HttpRequest runwayrequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/icao/" + icaocode + "/runways"))
                    .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                    .header("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> runwayresponse = HttpClient.newHttpClient().send(runwayrequest, HttpResponse.BodyHandlers.ofString());


            if (!runwayresponse.body().equals("[]")) {
                JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(runwayresponse.body()));

                JsonArray jsonArray = jsonElement2.getAsJsonArray();

                JsonObject jsonRunway1 = jsonArray.get(0).getAsJsonObject();
                JsonObject jsonRunway2 = jsonArray.get(1).getAsJsonObject();

                JsonObject jsonRunway3 = null;
                JsonObject jsonRunway4 = null;
                JsonObject jsonRunway5 = null;
                JsonObject jsonRunway6 = null;
                JsonObject jsonRunway7 = null;
                JsonObject jsonRunway8 = null;


                if (jsonArray.size() > 2) {
                    jsonRunway3 = jsonArray.get(2).getAsJsonObject();
                }
                if (jsonArray.size() > 3) {
                    jsonRunway4 = jsonArray.get(3).getAsJsonObject();
                }
                if (jsonArray.size() > 4) {
                    jsonRunway5 = jsonArray.get(4).getAsJsonObject();
                }
                if (jsonArray.size() > 5) {
                    jsonRunway6 = jsonArray.get(5).getAsJsonObject();
                }
                if (jsonArray.size() > 6) {
                    jsonRunway7 = jsonArray.get(6).getAsJsonObject();
                }
                if (jsonArray.size() > 7) {
                    jsonRunway8 = jsonArray.get(7).getAsJsonObject();
                }

                CloseableHttpClient httpClient = HttpClients.createDefault();


                HttpGet request = new HttpGet("https://www.airport-data.com/api/ap_info.json?icao=" + icaocode);
                HttpGet requestelevation = new HttpGet("https://airports-api.s3-us-west-2.amazonaws.com/icao/" + icaocode + ".json");

                // add request headers

                try (CloseableHttpResponse responseelevation = httpClient.execute(requestelevation)) {
                    HttpEntity httpEntity = responseelevation.getEntity();
                    Header header = httpEntity.getContentType();

                    String resultelevation = EntityUtils.toString(httpEntity);

                    try (CloseableHttpResponse response = httpClient.execute(request)) {

                        // Get HttpResponse Status

                        HttpEntity entity = response.getEntity();
                        Header headers = entity.getContentType();

                        if (entity != null) {
                            // return it as a String
                            String result = EntityUtils.toString(entity);

                            JsonElement jsonElement = JsonParser.parseReader(new StringReader(result));
                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                            String icao = jsonObject.get("icao").getAsString();
                            String iata = jsonObject.get("iata").getAsString();
                            String name = jsonObject.get("name").getAsString();
                            String location = jsonObject.get("location").getAsString();
                            String country = jsonObject.get("country").getAsString();
                            String country_code = jsonObject.get("country_code").getAsString();

                            String runwaysurface = jsonRunway1.get("surface").getAsString();
                            Boolean runwaylightened = jsonRunway1.get("hasLighting").getAsBoolean();

                            String runway1 = jsonRunway1.get("name").getAsString();
                            Integer runway1lengthm = jsonRunway1.get("length").getAsJsonObject().get("meter").getAsInt();
                            Integer runway1lengthft = jsonRunway1.get("length").getAsJsonObject().get("feet").getAsInt();

                            String runway2 = jsonRunway2.get("name").getAsString();
                            Integer runway2lengthm = jsonRunway2.get("length").getAsJsonObject().get("meter").getAsInt();
                            Integer runway2lengthft = jsonRunway1.get("length").getAsJsonObject().get("feet").getAsInt();

                            String runway3 = null;
                            Integer runway3lengthm = null;
                            Integer runway3lengthft = null;
                            if (jsonRunway3 != null) {
                                runway3 = jsonRunway3.get("name").getAsString();
                                runway3lengthm = jsonRunway3.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway3lengthft = jsonRunway3.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            Integer runway4lengthft = null;
                            Integer runway4lengthm = null;
                            String runway4 = null;
                            if (jsonRunway4 != null) {
                                runway4 = jsonRunway4.get("name").getAsString();
                                runway4lengthm = jsonRunway4.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway4lengthft = jsonRunway4.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway5 = null;
                            Integer runway5lengthm = null;
                            Integer runway5lengthft = null;
                            if (jsonRunway5 != null) {
                                runway5 = jsonRunway5.get("name").getAsString();
                                runway5lengthm = jsonRunway5.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway5lengthft = jsonRunway5.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            Integer runway6lengthft = null;
                            Integer runway6lengthm = null;
                            String runway6 = null;
                            if (jsonRunway6 != null) {
                                runway6 = jsonRunway6.get("name").getAsString();
                                runway6lengthm = jsonRunway6.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway6lengthft = jsonRunway6.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway7 = null;
                            Integer runway7lengthm = null;
                            Integer runway7lengthft = null;
                            if (jsonRunway7 != null) {
                                runway7 = jsonRunway7.get("name").getAsString();
                                runway7lengthm = jsonRunway7.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway7lengthft = jsonRunway7.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            String runway8 = null;
                            Integer runway8lengthm = null;
                            Integer runway8lengthft = null;
                            if (jsonRunway8 != null) {
                                runway8 = jsonRunway8.get("name").getAsString();
                                runway8lengthm = jsonRunway8.get("length").getAsJsonObject().get("meter").getAsInt();
                                runway8lengthft = jsonRunway8.get("length").getAsJsonObject().get("feet").getAsInt();
                            }

                            HttpGet requesturl = new HttpGet("https://www.airport-data.com/api/ap_info.json?icao=" + args[1]);
                            CloseableHttpResponse responseurl = httpClient.execute(requesturl);

                            HttpEntity urlEntity = responseurl.getEntity();
                            Header urlheader = urlEntity.getContentType();

                            String urlresult = EntityUtils.toString(urlEntity);

                            JsonElement urljsonElement = JsonParser.parseReader(new StringReader(urlresult));
                            JsonObject urljsonObject = urljsonElement.getAsJsonObject();

                            String airporturl = urljsonObject.get("link").getAsString();

                            JsonElement jsonElement1 = JsonParser.parseReader(new StringReader(resultelevation));
                            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                            Integer elevation = jsonObject1.get("elevation").getAsInt();

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                            embedBuilder.setTitle(name + " " + "[" + icao + " / " + iata + "]", airporturl);
                            embedBuilder.setDescription("Runways " + "[" + runwaysurface + "]");
                            embedBuilder.setColor(Color.CYAN);
                            embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                            embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                            embedBuilder.addField("~ " + runway1, runway1lengthm + "m " + "(" + runway1lengthft + "ft)", true);
                            embedBuilder.addField("~ " + runway2, runway2lengthm + "m " + "(" + runway2lengthft + "ft)", true);

                            if (runway3 != null)
                                embedBuilder.addField("~ " + runway3, runway3lengthm + "m " + "(" + runway3lengthft + "ft)", true);
                            if (runway4 != null)
                                embedBuilder.addField("~ " + runway4, runway4lengthm + "m " + "(" + runway4lengthft + "ft)", true);
                            if (runway5 != null)
                                embedBuilder.addField("~ " + runway5, runway5lengthm + "m " + "(" + runway5lengthft + "ft)", true);
                            if (runway6 != null)
                                embedBuilder.addField("~ " + runway6, runway6lengthm + "m " + "(" + runway6lengthft + "ft)", true);
                            if (runway7 != null)
                                embedBuilder.addField("~ " + runway7, runway7lengthm + "m " + "(" + runway7lengthft + "ft)", true);
                            if (runway8 != null)
                                embedBuilder.addField("~ " + runway8, runway8lengthm + "m " + "(" + runway8lengthft + "ft)", true);
                            if (runwaylightened) {
                                embedBuilder.addField("Beleuchtet", "ja" + " 🟢", false);
                            } else {
                                embedBuilder.addField("Beleuchtet", "nein" + " 🔴", false);
                            }
                            textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                exitMessage.addReaction("✈").queue();
                            });
                        } else {
                            Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
                        }
                    }
                }
            }
        }
    }
}
