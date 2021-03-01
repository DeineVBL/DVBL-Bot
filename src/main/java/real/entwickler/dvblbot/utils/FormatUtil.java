/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 18.02.2021 @ 15:38:58
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * FormatUtil.java is part of the DVBL-Bot which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

package real.entwickler.dvblbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import okio.Options;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class FormatUtil {

    public static String formatTrack(AudioTrack audioTrack) {
        return String.format("%s (%s)", audioTrack.getInfo().title, audioTrack.getInfo().author);
    }

    /**
     * Retrieves the thumbnail of a Youtube video
     *
     * @param track The AudioTrack {@link com.sedmelluq.discord.lavaplayer.track.AudioTrack} of the video
     * @return The thumbnails URL
     */
    public static String getThumbnail(AudioTrack track) {
        return String.format("https://img.youtube.com/vi/%s/default.jpg", track.getIdentifier());
    }

    /**
     * Formats memory bytes to mb
     *
     * @param bytes the actual bytes
     * @return amount of megabytes
     */
    public static long parseBytes(long bytes) {
        return bytes / 1024 / 1024;
    }

    public static String formatDuration(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        StringBuilder stringBuilder = new StringBuilder();
        if (hours > 0) stringBuilder.append(String.format("%dh", hours)).append(' ');
        if (minutes >= 0) stringBuilder.append(String.format("%dm", minutes)).append(' ');
        if (seconds >= 0) stringBuilder.append(String.format("%ds", seconds));
        return stringBuilder.toString();
    }

    public static String getRoleName(Role role) {
        if (role.isMentionable()) {
            return role.getAsMention();
        } else {
            return String.format("`%s`", role.getName());
        }
    }

    /**
     * Formats the milliseconds of a song duration to a readable timestamp
     *
     * @param millis The milliseconds
     * @return the timestamp as a string
     */
    public static String formatTimestamp(long millis) {
        long seconds = millis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    public static String formatProgressBar(long progress, long full) {
        double percentage = (double) progress / full;
        StringBuilder progressBar = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if ((int) (percentage * 20) == i)
                progressBar.append("\uD83D\uDD18");
            else
                progressBar.append("▬");
        }
        return progressBar.toString();
    }

    /**
     * Formats the helpmessage for a command
     *
     * @return an EmbedBuilder
     */
   /* public static EmbedBuilder formatCommand(Command command) {
        return info(WordUtils.capitalizeFully(command.getName()) + "Command", formatUsage(command));
    }

    private static String formatUsage(Command command) {
        StringBuilder stringBuilder = new StringBuilder();
        return addUsages(stringBuilder, command).toString();
    }

    private static StringBuilder addUsages(StringBuilder stringBuilder, Command command) {
        stringBuilder.append("Usage: `").append(buildUsage(command)).append("`\n");
        stringBuilder.append("Aliases: `").append(Arrays.toString(command.getAliases()).replace("[", "").replace("]", "")).append("`\n");
        stringBuilder.append("Description: `").append(command.getDescription()).append("`").append("\n");
        stringBuilder.append("Executable by: `").append(command.getPermissions().getIdentifier()).append("`").append("\n");

        if (!command.getSubCommandAssociations().isEmpty()) {
            stringBuilder.append("Subcommands:").append("\n");
            command.getSubCommandAssociations().values().parallelStream().distinct().collect(Collectors.toList()).forEach(subCommand -> stringBuilder.append(buildUsage(subCommand)).append("\n"));
        }

        return stringBuilder;
    }

    private static String buildUsage(Command command) {
        if (command instanceof SubCommand) {
            SubCommand subCommand = ((SubCommand) command);
            return String.format("▫ `" + GroovyBot.getInstance().getConfig().getJSONObject("settings").getString("prefix") + "%s %s %s` `%s`", subCommand.getMainCommand().getName(), subCommand.getName(), subCommand.getUsage(), subCommand.getDescription());
        }
        return String.format(GroovyBot.getInstance().getConfig().getJSONObject("settings").getString("prefix") + "%s %s", command.getName(), command.getUsage());
    }*/

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + "B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + ("");
        return String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre).replace(",", ".");
    }

    public static String parseUptime(long time) {
        long days = time / 24 / 60 / 60 / 1000;
        long hours = (time - days * 86400000) / 60 / 60 / 1000;
        long mins = (time - days * 86400000 - hours * 3600000) / 60 / 1000;
        long secs = (time - days * 86400000 - hours * 3600000 - mins * 60000) / 1000;
        return String.format("%sd, %sh, %smin, %ss", days, hours, mins, secs);
    }

    public static String formatUserName(User user) {
        return String.format("%s#%s", user.getName(), user.getDiscriminator());
    }

    /**
     * Converts a timestamp like 2:34 into it's millis
     *
     * @param timestamp The timestamp
     * @return The timestamp's millis
     * @throws ParseException when the provided String where invalid
     */
    public static long convertTimestamp(String timestamp) throws ParseException {
        DateFormat dateFormat = null;
        String[] splittedTimestamp = timestamp.split(":");
        int count = splittedTimestamp.length;
        String[] fixedTimestamp = new String[count];

        for (int i = 0; i < count; i++) {
            if (splittedTimestamp[i].length() == 1) {
                fixedTimestamp[i] = "0" + splittedTimestamp[i];
            } else if (splittedTimestamp[i].length() > 2 || splittedTimestamp[i].length() < 1) {
                throw new ParseException("Invalid timestamp, part is longer than 2 chars.", i);
            } else {
                fixedTimestamp[i] = splittedTimestamp[i];
            }
        }

        if (count == 1)
            dateFormat = new SimpleDateFormat("ss");
        else if (count == 2)
            dateFormat = new SimpleDateFormat("mm:ss");
        else if (count > 2)
            dateFormat = new SimpleDateFormat("HH:mm:ss");

        assert dateFormat != null;
        return dateFormat.parse(String.join(":", fixedTimestamp)).getTime() + TimeUnit.HOURS.toMillis(1);
    }

    public static String[] formatLyrics(String lyrics) {
        System.out.println("null: " + (lyrics == null));
        String pre = lyrics.replaceAll("\\[(.*?)]", "%split%t:[$1]%split%");
        if (pre.length() > 6000) {
            pre = pre.substring(0, 6000);
            if (pre.substring(pre.lastIndexOf(' ') + 1).contains("**"))
                pre = pre.substring(0, pre.lastIndexOf(" "));
            pre += " ...";
        }

        String[] comps = pre.split("%split%");

        int count = 0;
        for (String comp : comps) {
            if (comp.length() > 1024)
                comps[count] = comp.substring(0, 1020) + " ...";
            count++;
        }

        if (Pattern.compile("\\w+").matcher(comps[0]).find()) return comps;
        return Arrays.copyOfRange(comps, 1, comps.length);
    }

    public static String formatStacktrace(Throwable throwable) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i > throwable.getStackTrace().length)
                break;
            try {
                out.append(throwable.getStackTrace()[i]).append("\n");
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return out.toString();
    }

    public static String formatException(Throwable throwable) {
        return String.format("%s:%s", throwable.getClass().getCanonicalName(), throwable.getMessage());
    }

    /**
     * Formats CLI help
     *
     * @param syntax  Thea syntax
     * @param options the options
     * @return The formatted help
     */
    public static String formatHelp(String syntax, Options options) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        return stringWriter.getBuffer().toString();
    }
}