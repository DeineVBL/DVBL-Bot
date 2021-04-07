/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 28.03.2021 @ 14:05:25
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * AirportData.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.aviation;

import com.google.auth.oauth2.GoogleCredentials;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AirportData extends ICommand {


    public AirportData(String name, String description, String... roles) {
        super(name, description, roles);
    }


    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) throws IOException, InterruptedException {

        if (args[1].length() == 4) {

            HttpRequest runwayrequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/icao/" + args[1]))
                    .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                    .header("x-rapidapi-host", "aerodatabox.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> runwayresponse = HttpClient.newHttpClient().send(runwayrequest, HttpResponse.BodyHandlers.ofString());

            if (!runwayresponse.body().isEmpty()) {
                JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(runwayresponse.body()));
                JsonObject jsonObject2 = jsonElement2.getAsJsonObject();

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

                        // return it as a String
                        String result = EntityUtils.toString(entity);

                        JsonElement jsonElement = JsonParser.parseReader(new StringReader(result));
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        String icao = jsonObject.get("icao").getAsString();
                        String iata = jsonObject.get("iata").getAsString();
                        String name = jsonObject.get("name").getAsString();
                        String location = jsonObject.get("location").getAsString();
                        String locationraw = jsonObject.get("location").getAsString().replaceAll(" ", "%20").replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö", "oe");
                        String country = jsonObject.get("country").getAsString();
                        String countryraw = jsonObject.get("country").getAsString().replaceAll(" ", "%20").replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö", "oe");
                        String country_code = jsonObject.get("country_code").getAsString();
                        Double longitude = jsonObject.get("longitude").getAsDouble();
                        Double latitude = jsonObject.get("latitude").getAsDouble();
                        String link = jsonObject.get("link").getAsString();
                        Integer status = jsonObject.get("status").getAsInt();

                        String timezone = jsonObject2.get("timeZone").getAsString();

                        HttpRequest imagerequest = HttpRequest.newBuilder()
                                .uri(URI.create("https://pexelsdimasv1.p.rapidapi.com/v1/search?query=" + locationraw + "&locale=en-US&per_page=15&page=1"))
                                .header("authorization", "563492ad6f91700001000001d09d3f925aa148fb9cab7ab2c8dce7ad")
                                .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                                .header("x-rapidapi-host", "PexelsdimasV1.p.rapidapi.com")
                                .method("GET", HttpRequest.BodyPublishers.noBody())
                                .build();
                        HttpResponse<String> imageresponse = HttpClient.newHttpClient().send(imagerequest, HttpResponse.BodyHandlers.ofString());

                        JsonElement jsonElementImage = JsonParser.parseReader(new StringReader(imageresponse.body()));
                        JsonObject jsonObjectImage = jsonElementImage.getAsJsonObject();

                        String airporturl = jsonObject2.get("urls").getAsJsonObject().get("wikipedia").getAsString();

                        JsonElement jsonElement1 = JsonParser.parseReader(new StringReader(resultelevation));
                        JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                        Integer elevation = jsonObject1.get("elevation").getAsInt();

                        if (jsonObjectImage.get("photos").getAsJsonArray().size() >= 1) {
                            String image = jsonObjectImage.get("photos").getAsJsonArray().get(0).getAsJsonObject().get("src").getAsJsonObject().get("original").getAsString();

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                            embedBuilder.setTitle(name + " " + "[" + icao + " / " + iata + "]", airporturl);
                            embedBuilder.setDescription(location + ", " + country + " (" + country_code + ")" + " - " + timezone);
                            embedBuilder.setColor(Color.CYAN);
                            embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                            embedBuilder.setImage(image);
                            embedBuilder.addField("Höhe", elevation + "ft", true);
                            embedBuilder.addField("Längengrad", String.valueOf(longitude), true);
                            embedBuilder.addField("Breitengrad", String.valueOf(latitude), true);
                            embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                            textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                exitMessage.addReaction("✈").queue();
                            });

                        } else {
                            HttpRequest countryimagerequest = HttpRequest.newBuilder()
                                    .uri(URI.create("https://pexelsdimasv1.p.rapidapi.com/v1/search?query=" + countryraw + "&locale=en-US&per_page=15&page=1"))
                                    .header("authorization", "563492ad6f91700001000001d09d3f925aa148fb9cab7ab2c8dce7ad")
                                    .header("x-rapidapi-key", "81ff1c1890mshb1377f670cf68a0p1c3b6ajsn149809c64f49")
                                    .header("x-rapidapi-host", "PexelsdimasV1.p.rapidapi.com")
                                    .method("GET", HttpRequest.BodyPublishers.noBody())
                                    .build();
                            HttpResponse<String> countryimageresponse = HttpClient.newHttpClient().send(countryimagerequest, HttpResponse.BodyHandlers.ofString());

                            JsonElement jsonElementCountryImage = JsonParser.parseReader(new StringReader(countryimageresponse.body()));
                            JsonObject jsonObjectCountryImage = jsonElementCountryImage.getAsJsonObject();

                            if (jsonObjectCountryImage.get("photos").getAsJsonArray().size() >= 1) {
                                String countryimage = jsonObjectCountryImage.get("photos").getAsJsonArray().get(0).getAsJsonObject().get("src").getAsJsonObject().get("original").getAsString();

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                embedBuilder.setTitle(name + " " + "[" + icao + " / " + iata + "]", airporturl);
                                embedBuilder.setDescription(location + ", " + country + " (" + country_code + ")" + " - " + timezone);
                                embedBuilder.setColor(Color.CYAN);
                                embedBuilder.setImage(countryimage);
                                embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                                embedBuilder.addField("Höhe", elevation + "ft", true);
                                embedBuilder.addField("Längengrad", String.valueOf(longitude), true);
                                embedBuilder.addField("Breitengrad", String.valueOf(latitude), true);
                                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                    exitMessage.addReaction("✈").queue();
                                });
                            } else {
                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                                embedBuilder.setTitle(name + " " + "[" + icao + " / " + iata + "]", airporturl);
                                embedBuilder.setDescription(location + ", " + country + " (" + country_code + ")" + " - " + timezone);
                                embedBuilder.setColor(Color.CYAN);
                                embedBuilder.setThumbnail("https://raw.githubusercontent.com/DeineVBL/DVBL-Bot/dev/images/dvbl.png");
                                embedBuilder.addField("Höhe", elevation + "ft", true);
                                embedBuilder.addField("Längengrad", String.valueOf(longitude), true);
                                embedBuilder.addField("Breitengrad", String.valueOf(latitude), true);
                                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());
                                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                                    exitMessage.addReaction("✈").queue();
                                });
                            }
                        }
                    }
                }
            } else {
                Bot.getInstance().getMessageManager().printNoAirportFoundMessage(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printWrongAirportMessage(commandSender, textChannel);
        }
    }
}
