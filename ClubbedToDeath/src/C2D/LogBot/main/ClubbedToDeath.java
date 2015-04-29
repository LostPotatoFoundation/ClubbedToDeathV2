package C2D.LogBot.main;
import java.io.*;
import java.util.*;

public class ClubbedToDeath {
    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try {
        p.load(new FileInputStream(new File("./config.ini")));
        }
         catch(IOException e)
    {
    System.out.println("Config file not found. Generating one and exiting");
            try (PrintWriter writer = new PrintWriter("./config.ini", "UTF-8")) {
                writer.println("# Clubbed To Death v1.0 - IRC Logging Bot - Based off of http://www.jibble.org/logbot/");
                writer.println("Server = localhost");
                writer.println("Channel = #test");
                writer.println("Nick = LogBot");
                writer.println("LoginName = Walrus");
                writer.println("RealName = Stegosarus");
            }
    System.exit(1);
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
            System.exit(1);
        }
        System.out.println("Clubbed To Death v1.0"); 
        LogBot bot = new LogBot(nick, outDir, loginName, realName);
        bot.connect(server);
        bot.joinChannel(channel);
        }
}
