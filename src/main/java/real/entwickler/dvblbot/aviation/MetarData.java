/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 28.03.2021 @ 23:53:35
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MetarData.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
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
import real.entwickler.dvblbot.utils.Property;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class MetarData extends ICommand {

    public MetarData(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) throws MalformedURLException, IOException, InterruptedException {

        if (args[1].length() == 4) {

            Property property = Bot.getInstance().getProperty();

            HttpRequest metarrequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://private-anon-408c710db5-avwx.apiary-proxy.com/api/metar/" + args[1]))
                    .header("Authorization", "ee2UE_AsMFFtP9g-_aRVUg04sZY2gNCu1onFaxkcV9k")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> metarresponse = HttpClient.newHttpClient().send(metarrequest, HttpResponse.BodyHandlers.ofString());

            JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(metarresponse.body()));
            JsonObject jsonObject2 = jsonElement2.getAsJsonObject();

            CloseableHttpClient httpClient = HttpClients.createDefault();


            HttpGet request = new HttpGet("https://wx.void.fo/metar?location=" + args[1]);
            HttpGet airportrequest = new HttpGet("https://www.airport-data.com/api/ap_info.json?icao=" + args[1]);

            // add request headers

            try (CloseableHttpResponse responsemetar = httpClient.execute(request)) {

                try (CloseableHttpResponse airportresponse = httpClient.execute(airportrequest)) {

                    // Get HttpResponse Status

                    HttpEntity entity = responsemetar.getEntity();

                    HttpEntity airportentity = airportresponse.getEntity();


                    String result = EntityUtils.toString(entity);
                    String airportresult = EntityUtils.toString(airportentity);

                    JsonElement jsonElement = JsonParser.parseReader(new StringReader(result));

                    if (!jsonElement.isJsonNull()) {

                        JsonArray jsonArray = jsonElement.getAsJsonArray();

                        JsonElement jsonElement1 = JsonParser.parseReader(new StringReader(airportresult));
                        JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                        System.out.println(jsonObject1);

                        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

                        String metar = jsonObject2.get("sanitized").getAsString();

                        String airportname = jsonObject1.get("name").getAsString();
                        String airporticao = jsonObject1.get("icao").getAsString();
                        String airportiata = jsonObject1.get("iata").getAsString();

                        String date = jsonObject2.get("meta").getAsJsonObject().get("timestamp").getAsString();
                        String flightrules = jsonObject2.get("flight_rules").getAsString();
                        String pressure = jsonObject2.get("altimeter").getAsJsonObject().get("value").getAsString();
                        String visibility = jsonObject2.get("visibility").getAsJsonObject().get("value").getAsString();
                        String temperature = jsonObject2.get("temperature").getAsJsonObject().get("value").getAsString();
                        String dewpoint = jsonObject2.get("dewpoint").getAsJsonObject().get("value").getAsString();
                        String remarks = "-";
                        if (!jsonObject2.get("remarks").getAsString().isEmpty())
                            remarks = jsonObject2.get("remarks").getAsString();
                        String clouds = "0";
                        if (jsonObject2.get("clouds").getAsJsonArray().size() >= 5)
                            clouds = jsonObject2.get("clouds").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString();
                        String cloudsheight = "0";
                        if (jsonObject2.get("clouds").getAsJsonArray().size() >= 5)
                            cloudsheight = jsonObject2.get("clouds").getAsJsonArray().get(0).getAsJsonObject().get("altitude").getAsString();
                        String winddirection = "0";
                        if (!jsonObject2.get("wind_direction").getAsJsonObject().get("value").isJsonNull())
                            winddirection = jsonObject2.get("wind_direction").getAsJsonObject().get("value").getAsString();
                        String windspeed = jsonObject2.get("wind_speed").getAsJsonObject().get("value").getAsString();
                        String windvariabledirection = " - ";
                        if (jsonObject2.get("wind_variable_direction").getAsJsonArray().size() >= 5)
                            windvariabledirection = jsonObject2.get("wind_variable_direction").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString() + "° - " + jsonObject2.get("wind_variable_direction").getAsJsonArray().get(1).getAsJsonObject().get("value").getAsString() + "°";

                        if (!message.getContentRaw().contains(".ms")) {

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                            embedBuilder.setTitle("Metar");
                            embedBuilder.setDescription("**" + metar + "**");
                            embedBuilder.setColor(Color.CYAN);
                            embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                            embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                            textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                exitMessage.addReaction("✈").queue();
                            });
                        } else {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                            embedBuilder.setTitle("Metar");
                            embedBuilder.setDescription("**" + metar + "**");
                            embedBuilder.addField("Flughafen", airportname + " (" + airporticao + " / " + airportiata + ")", false);
                            embedBuilder.addField("Aktualisiert", date, false);
                            embedBuilder.addField("Wind", windspeed + "kt" + ", " + winddirection + "°" + " / " + windvariabledirection, false);
                            embedBuilder.addField("Sichtweite", visibility + "m", false);
                            embedBuilder.addField("Temperatur", temperature + "°C", false);
                            embedBuilder.addField("Taupunkt", dewpoint + "°C", false);
                            embedBuilder.addField("Druck", pressure + "hpa", false);
                            embedBuilder.addField("Wolken", clouds + ", " + cloudsheight + "00ft", false);
                            embedBuilder.addField("Flugregeln", flightrules, false);
                            embedBuilder.addField("Bemerkungen", remarks, false);
                            embedBuilder.setColor(Color.CYAN);
                            embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                            embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                            textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                exitMessage.addReaction("✈").queue();
                            });
                        }
                    } else {
                        Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
                    }
                }
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

                String icaocode = jsonObject3.get("items").getAsJsonArray().get(0).getAsJsonObject().get("icao").getAsString();

                Property property = Bot.getInstance().getProperty();

                HttpRequest metarrequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://private-anon-408c710db5-avwx.apiary-proxy.com/api/metar/" + icaocode))
                        .header("Authorization", "ee2UE_AsMFFtP9g-_aRVUg04sZY2gNCu1onFaxkcV9k")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> metarresponse = HttpClient.newHttpClient().send(metarrequest, HttpResponse.BodyHandlers.ofString());

                JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(metarresponse.body()));

                JsonObject jsonObject2 = jsonElement2.getAsJsonObject();

                CloseableHttpClient httpClient = HttpClients.createDefault();


                HttpGet request = new HttpGet("https://wx.void.fo/metar?location=" + icaocode);
                HttpGet airportrequest = new HttpGet("https://www.airport-data.com/api/ap_info.json?icao=" + icaocode);

                // add request headers

                try (CloseableHttpResponse responsemetar = httpClient.execute(request)) {

                    try (CloseableHttpResponse airportresponse = httpClient.execute(airportrequest)) {

                        // Get HttpResponse Status

                        HttpEntity entity = responsemetar.getEntity();

                        HttpEntity airportentity = airportresponse.getEntity();


                        String result = EntityUtils.toString(entity);
                        String airportresult = EntityUtils.toString(airportentity);

                        JsonElement jsonElement = JsonParser.parseReader(new StringReader(result));

                        if (!jsonElement.isJsonNull()) {

                            JsonArray jsonArray = jsonElement.getAsJsonArray();

                            JsonElement jsonElement1 = JsonParser.parseReader(new StringReader(airportresult));
                            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

                            String metar = jsonObject2.get("sanitized").getAsString();


                            String airportname = jsonObject1.get("name").getAsString();
                            String airporticao = jsonObject1.get("icao").getAsString();
                            String airportiata = jsonObject1.get("iata").getAsString();

                            String date = jsonObject2.get("meta").getAsJsonObject().get("timestamp").getAsString();
                            String flightrules = jsonObject2.get("flight_rules").getAsString();
                            String pressure = jsonObject2.get("altimeter").getAsJsonObject().get("value").getAsString();
                            String visibility = jsonObject2.get("visibility").getAsJsonObject().get("value").getAsString();
                            String temperature = jsonObject2.get("temperature").getAsJsonObject().get("value").getAsString();
                            String dewpoint = jsonObject2.get("dewpoint").getAsJsonObject().get("value").getAsString();
                            String remarks = "-";
                            if (!jsonObject2.get("remarks").getAsString().isEmpty())
                                remarks = jsonObject2.get("remarks").getAsString();
                            String clouds = "0";
                            if (jsonObject2.get("clouds").getAsJsonArray().size() >= 5)
                                clouds = jsonObject2.get("clouds").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString();
                            String cloudsheight = "0";
                            if (jsonObject2.get("clouds").getAsJsonArray().size() >= 5)
                                cloudsheight = jsonObject2.get("clouds").getAsJsonArray().get(0).getAsJsonObject().get("altitude").getAsString();
                            String winddirection = "0";
                            if (!jsonObject2.get("wind_direction").getAsJsonObject().get("value").isJsonNull())
                                winddirection = jsonObject2.get("wind_direction").getAsJsonObject().get("value").getAsString();
                            String windspeed = jsonObject2.get("wind_speed").getAsJsonObject().get("value").getAsString();
                            String windvariabledirection = " - ";
                            if (jsonObject2.get("wind_variable_direction").getAsJsonArray().size() >= 5)
                                windvariabledirection = jsonObject2.get("wind_variable_direction").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString() + "° - " + jsonObject2.get("wind_variable_direction").getAsJsonArray().get(1).getAsJsonObject().get("value").getAsString() + "°";

                            if (!message.getContentRaw().contains(".ms")) {

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                embedBuilder.setTitle("Metar");
                                embedBuilder.setDescription("**" + metar + "**");
                                embedBuilder.setColor(Color.CYAN);
                                embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                    exitMessage.addReaction("✈").queue();
                                });
                            } else {
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                embedBuilder.setTitle("Metar");
                                embedBuilder.setDescription("**" + metar + "**");
                                embedBuilder.addField("Flughafen", airportname + " (" + airporticao + " / " + airportiata + ")", false);
                                embedBuilder.addField("Aktualisiert", date, false);
                                embedBuilder.addField("Wind", windspeed + "kt" + ", " + winddirection + "°" + " / " + windvariabledirection, false);
                                embedBuilder.addField("Sichtweite", visibility + "m", false);
                                embedBuilder.addField("Temperatur", temperature + "°C", false);
                                embedBuilder.addField("Taupunkt", dewpoint + "°C", false);
                                embedBuilder.addField("Druck", pressure + "hpa", false);
                                embedBuilder.addField("Wolken", clouds + ", " + cloudsheight + "00ft", false);
                                embedBuilder.addField("Flugregeln", flightrules, false);
                                embedBuilder.addField("Bemerkungen", remarks, false);
                                embedBuilder.setColor(Color.CYAN);
                                embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                    exitMessage.addReaction("✈").queue();
                                });
                            }
                        } else {
                            Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
                        }
                    }
                }
            } else {
                Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
            }
        }
    }
}