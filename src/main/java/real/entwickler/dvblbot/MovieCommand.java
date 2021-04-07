/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 02.04.2021 @ 17:44:10
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * MovieCommand.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import real.entwickler.dvblbot.utils.ICommand;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

public class MovieCommand extends ICommand {

    public MovieCommand(String name, String description, String... roles) {
        super(name, description, roles);
    }

    @Override
    public void onCommand(Member commandSender, TextChannel textChannel, Message message, String[] args) throws MalformedURLException, IOException, InterruptedException {
        if (args.length == 2) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String movietitle = args[1];

            HttpGet requestmovie = new HttpGet("http://www.omdbapi.com/?apikey=9d864cad&t=" + movietitle);

            CloseableHttpResponse responsemovie = httpClient.execute(requestmovie);
            HttpEntity httpEntity = responsemovie.getEntity();

            String resultmovie = EntityUtils.toString(httpEntity);

            JsonElement jsonElement = JsonParser.parseReader(new StringReader(resultmovie));
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if(!jsonObject.get("Response").getAsString().equalsIgnoreCase("False")) {

                String title = jsonObject.get("Title").getAsString();
                String plot = jsonObject.get("Plot").getAsString();
                String poster = jsonObject.get("Poster").getAsString();
                String year = jsonObject.get("Year").getAsString();
                String country = jsonObject.get("Country").getAsString();
                String runtime = jsonObject.get("Runtime").getAsString();
                String genre = jsonObject.get("Genre").getAsString();
                String release = jsonObject.get("Released").getAsString();
                String rating = jsonObject.get("Ratings").getAsJsonArray().get(0).getAsJsonObject().get("Value").getAsString();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                embedBuilder.setTitle(title);
                embedBuilder.setDescription(plot);
                embedBuilder.setImage(poster);
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.addField("Year \uD83D\uDCFA", year, true);
                embedBuilder.addField("Genre \uD83C\uDFAC", genre, true);
                embedBuilder.addField("Runtime \uD83D\uDD51", runtime, true);
                embedBuilder.addField("Country \uD83C\uDFF3", country, true);
                embedBuilder.addField("Release \uD83C\uDF9E", release, true);
                embedBuilder.addField("Rating \u2B50", rating, true);
                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());

                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                    exitMessage.addReaction("U+1F3A5").queue();
                });
            } else {
               Bot.getInstance().getMessageManager().printNoMovieFound(commandSender, textChannel);
            }
        } else if (args.length == 3) {
            CloseableHttpClient httpClient2 = HttpClients.createDefault();
            String movietitle2 = args[1] + "%20" + args[2];

            HttpGet requestmovie2 = new HttpGet("http://www.omdbapi.com/?apikey=9d864cad&t=" + movietitle2);

            CloseableHttpResponse responsemovie2 = httpClient2.execute(requestmovie2);
            HttpEntity httpEntity2 = responsemovie2.getEntity();

            String resultmovie2 = EntityUtils.toString(httpEntity2);

            JsonElement jsonElement2 = JsonParser.parseReader(new StringReader(resultmovie2));
            JsonObject jsonObject2 = jsonElement2.getAsJsonObject();

            if(!jsonObject2.get("Response").getAsString().equalsIgnoreCase("False")) {

                String title = jsonObject2.get("Title").getAsString();
                String plot = jsonObject2.get("Plot").getAsString();
                String poster = jsonObject2.get("Poster").getAsString();
                String year = jsonObject2.get("Year").getAsString();
                String country = jsonObject2.get("Country").getAsString();
                String runtime = jsonObject2.get("Runtime").getAsString();
                String genre = jsonObject2.get("Genre").getAsString();
                String release = jsonObject2.get("Released").getAsString();
                String rating = jsonObject2.get("Ratings").getAsJsonArray().get(0).getAsJsonObject().get("Value").getAsString();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                embedBuilder.setTitle(title);
                embedBuilder.setDescription(plot);
                embedBuilder.setImage(poster);
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.addField("Year \uD83D\uDCFA", year, true);
                embedBuilder.addField("Genre \uD83C\uDFAC", genre, true);
                embedBuilder.addField("Runtime \uD83D\uDD51", runtime, true);
                embedBuilder.addField("Country \uD83C\uDFF3", country, true);
                embedBuilder.addField("Release \uD83C\uDF9E", release, true);
                embedBuilder.addField("Rating \u2B50", rating, true);
                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());

                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                    exitMessage.addReaction("U+1F3A5").queue();
                });
            } else {
                Bot.getInstance().getMessageManager().printNoMovieFound(commandSender, textChannel);
            }
        } else if (args.length == 4) {
            CloseableHttpClient httpClient3 = HttpClients.createDefault();
            String movietitle3 = args[1] + "%20" + args[2] + "%20" + args[3];

            HttpGet requestmovie3 = new HttpGet("http://www.omdbapi.com/?apikey=9d864cad&t=" + movietitle3);

            CloseableHttpResponse responsemovie3 = httpClient3.execute(requestmovie3);
            HttpEntity httpEntity3 = responsemovie3.getEntity();

            String resultmovie3 = EntityUtils.toString(httpEntity3);

            JsonElement jsonElement3 = JsonParser.parseReader(new StringReader(resultmovie3));
            JsonObject jsonObject3 = jsonElement3.getAsJsonObject();

            if(!jsonObject3.get("Response").getAsString().equalsIgnoreCase("False")) {

                String title = jsonObject3.get("Title").getAsString();
                String plot = jsonObject3.get("Plot").getAsString();
                String poster = jsonObject3.get("Poster").getAsString();
                String year = jsonObject3.get("Year").getAsString();
                String country = jsonObject3.get("Country").getAsString();
                String runtime = jsonObject3.get("Runtime").getAsString();
                String genre = jsonObject3.get("Genre").getAsString();
                String release = jsonObject3.get("Released").getAsString();
                String rating = jsonObject3.get("Ratings").getAsJsonArray().get(0).getAsJsonObject().get("Value").getAsString();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                embedBuilder.setTitle(title);
                embedBuilder.setDescription(plot);
                embedBuilder.setImage(poster);
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.addField("Year \uD83D\uDCFA", year, true);
                embedBuilder.addField("Genre \uD83C\uDFAC", genre, true);
                embedBuilder.addField("Runtime \uD83D\uDD51", runtime, true);
                embedBuilder.addField("Country \uD83C\uDFF3", country, true);
                embedBuilder.addField("Release \uD83C\uDF9E", release, true);
                embedBuilder.addField("Rating \u2B50", rating, true);
                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());

                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                    exitMessage.addReaction("U+1F3A5").queue();
                });
            } else {
                Bot.getInstance().getMessageManager().printNoMovieFound(commandSender, textChannel);
            }
        } else if (args.length == 5) {
            CloseableHttpClient httpClient4 = HttpClients.createDefault();
            String movietitle4 = args[1] + "%20" + args[2] + "%20" + args[3] + "%20" + args[4];

            System.out.println(movietitle4);

            HttpGet requestmovie4 = new HttpGet("http://www.omdbapi.com/?apikey=9d864cad&t=" + movietitle4);

            CloseableHttpResponse responsemovie4 = httpClient4.execute(requestmovie4);
            HttpEntity httpEntity4 = responsemovie4.getEntity();

            String resultmovie4 = EntityUtils.toString(httpEntity4);

            JsonElement jsonElement4 = JsonParser.parseReader(new StringReader(resultmovie4));
            JsonObject jsonObject4 = jsonElement4.getAsJsonObject();

            if(!jsonObject4.get("Response").getAsString().equalsIgnoreCase("False")) {

                String title = jsonObject4.get("Title").getAsString();
                String plot = jsonObject4.get("Plot").getAsString();
                String poster = jsonObject4.get("Poster").getAsString();
                String year = jsonObject4.get("Year").getAsString();
                String country = jsonObject4.get("Country").getAsString();
                String runtime = jsonObject4.get("Runtime").getAsString();
                String genre = jsonObject4.get("Genre").getAsString();
                String release = jsonObject4.get("Released").getAsString();
                String rating = jsonObject4.get("Ratings").getAsJsonArray().get(0).getAsJsonObject().get("Value").getAsString();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("DVBL-Bot - " + commandSender.getEffectiveName());
                embedBuilder.setTitle(title);
                embedBuilder.setDescription(plot);
                embedBuilder.setImage(poster);
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.addField("Year \uD83D\uDCFA", year, true);
                embedBuilder.addField("Genre \uD83C\uDFAC", genre, true);
                embedBuilder.addField("Runtime \uD83D\uDD51", runtime, true);
                embedBuilder.addField("Country \uD83C\uDFF3", country, true);
                embedBuilder.addField("Release \uD83C\uDF9E", release, true);
                embedBuilder.addField("Rating \u2B50", rating, true);
                embedBuilder.setFooter("DVBL-Bot - Copyright © swausb ||  Nils K.-E. 2021", commandSender.getUser().getEffectiveAvatarUrl());

                textChannel.sendMessage(embedBuilder.build()).queue(exitMessage -> {
                    exitMessage.addReaction("U+1F3A5").queue();
                });
            } else {
                Bot.getInstance().getMessageManager().printNoMovieFound(commandSender, textChannel);
            }
        } else {
            Bot.getInstance().getMessageManager().printWrongMovieMessage(commandSender, textChannel);
        }

    }
}
