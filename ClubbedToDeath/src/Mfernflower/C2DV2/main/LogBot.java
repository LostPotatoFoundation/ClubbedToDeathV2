package Mfernflower.C2DV2.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class LogBot extends PircBot {

    private static final Pattern urlPattern = Pattern.compile("(?i:\\b((http|https|ftp|irc)://[^\\s]+))");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("H:mm");
    
    public LogBot(String name, File outDir, String login, String realName) {
        setName(name);
        setVerbose(true);
        this.outDir = outDir;
        this.setLogin(login);
        this.setVersion(realName);
    }
    
    public void append(String line) {
        line = Colors.removeFormattingAndColors(line);
        
        line = line.replaceAll("&", "&amp;");
        line = line.replaceAll("<", "&lt;");
        line = line.replaceAll(">", "&gt;");
        
        Matcher matcher = urlPattern.matcher(line);
        line = matcher.replaceAll("<a href=\"$1\">$1</a>");
        
                
        try {
            
            Date now = new Date();
            String date = DATE_FORMAT.format(now);
            String time = TIME_FORMAT.format(now);
            File file = new File(outDir, date + ".html");
            try (BufferedWriter configGen = new BufferedWriter(new FileWriter(file, true))) {
                String entry = "<span class=\"irc-date\">[" + time + "]</span> <span>" + line + "</span><br />";
                configGen.write(entry);
                configGen.newLine();
                configGen.flush();
            }
        }
        catch (IOException e) {
            System.out.println("Could not write to log: " + e);
        }
    }
    @Override
    public void onConnect()
    {
    append("Clubbed To Death Revision 2 - Version " + ClubbedToDeath.ver + " - IRC Logging Bot - Based off of http://www.jibble.org/logbot/");  
    }
   
    @Override 
    public void onAction(String sender, String login, String hostname, String target, String action) {
        append("* " + sender + " " + action);
    }
    @Override 
    public void onJoin(String channel, String sender, String login, String hostname) {
       append("* " + sender + " (" + login + "@" + hostname + ") has joined " + channel);
    }
    @Override 
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        append("<" + sender + "> " + message);       
    }
    @Override 
    public void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        append("* " + sourceNick + " sets mode " + mode);
    }
    @Override  
    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        append("* " + oldNick + " is now known as " + newNick);
    }
    @Override 
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        append("-" + sourceNick + "- " + notice);
    }
    @Override 
    public void onPart(String channel, String sender, String login, String hostname) {
        append("* " + sender + " (" + login + "@" + hostname + ") has left " + channel);
    }
    @Override 
    public void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
        append("[" + sourceNick + " PING]");
    }
    @Override 
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
         append("<- *" + sender + "* " + message);
    }
    @Override 
    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        append("* " + sourceNick + " (" + sourceLogin + "@" + sourceHostname + ") Quit (" + reason + ")");
    }
    @Override 
    public void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        append("[" + sourceNick + " TIME]");
    }
    @Override 
    public void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        if (changed) {
            append("* " + setBy + " changes topic to '" + topic + "'");
        }
        else {
            append("* Topic is '" + topic + "'");
            append("* Set by " + setBy + " on " + new Date(date));
        }
    }
    @Override 
    public void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        append("[" + sourceNick + " VERSION]");
    }
    @Override 
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        append("* " + recipientNick + " was kicked from " + channel + " by " + kickerNick);
        if (recipientNick.equalsIgnoreCase(getNick())) {
            System.out.println("**** KICKED FROM CHANNEL ***** \n");
            append("**** KICKED FROM CHANNEL *****");
           
    }
    }
    @Override 
    @SuppressWarnings("SleepWhileInLoop")
    public void onDisconnect() {
        append("**** DISCONNECTED FROM SERVER ****");
        System.out.println("Server connection lost - Attempting reconnect \n");
        while (!isConnected()) {
            try {
                reconnect();
            }
            catch (IOException | IrcException e) {
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException anye) {
                    // Do nothing.
                }
            }
        }
    }
  
    private final File outDir; 
}
