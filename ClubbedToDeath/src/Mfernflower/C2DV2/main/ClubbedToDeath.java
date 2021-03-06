package Mfernflower.C2DV2.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;

public class ClubbedToDeath {
    public final static String ver = "FINAL";
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try {
        p.load(new FileInputStream(new File("./config.cfg")));
        }
         catch(IOException e)
    {
    System.out.println("Config file not found. Generating one and exiting");
            try (PrintWriter writer = new PrintWriter("./config.cfg", "UTF-8")) { 
                writer.println("# Clubbed To Death 2 - Based off of http://www.jibble.org/logbot/");
                writer.println("# https://github.com/LostPotatoFoundation/ClubbedToDeathV2/");
                writer.println("# CONFIG FILE");
                writer.println("Server = localhost");
                writer.println("Channel = #test");
                writer.println("Nick = LogBot");
                writer.println("LoginName = Walrus");
                writer.println("RealName = Stegosarus");
            }
        System.exit(2); // Return status when config is not found 
    }
        String server = p.getProperty("Server", "localhost");
        String channel = p.getProperty("Channel", "#test");
        String nick = p.getProperty("Nick", "LogBot");
        String loginName = p.getProperty("LoginName", "Walrus");
        String realName = p.getProperty("RealName", "Stegosarus");
        
        File outDir = new File(p.getProperty("OutputDir", "./output/"));
        outDir.mkdirs();
        if (!outDir.isDirectory()) {
            System.out.println("Cannot make output directory (" + outDir + ")");
            System.exit(2);
        }
        System.out.println("Clubbed To Death 2 - Version " + ver ); 
        LogBot bot = new LogBot(nick, outDir, loginName, realName);
        bot.connect(server);
        bot.joinChannel(channel);
        }
}
