package emubot.src;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.omg.CORBA.Environment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> accountdata;
    public static int botsLoaded = 0;

    public static boolean finishedCmdExec = false;

    public static String stk = null;
    public static int accountsLimit = 5000;
    public static MainGUI gui = new MainGUI();
    public static ArrayList<HQ_API> HQAccounts = new ArrayList<>();

    public static boolean headless = false;
    public static boolean debug = false;

    public static void main(String[] args){
        try{
            if(args.length >= 1){
                List<String> argslist = Arrays.asList(args);
                if(argslist.contains("headless")){
                    headless = true;
                }

                if(argslist.contains("debug")){
                    debug = true;
                } else {
                    debug = false;
                }
            }

            if(!headless)
                gui.OpenGUI();

            new Thread(() -> {
                try {
                    //should add separate files later
                    accountdata = Files.readAllLines(Paths.get("tokens.txt"));

                    for (int i = 0; i < accountdata.size(); i++) {
                        String data = accountdata.get(i);
                        if (i < accountsLimit) {
                            if (data.charAt(0) == '{') {
                                JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
                                HQAccounts.add(new HQ_API(jsonObject.get("username").getAsString(), jsonObject.get("bearer").getAsString()));
                            } else {
                                HQAccounts.add(new HQ_API(data));
                            }

                            if (!headless) {
                                gui.setTotalAccountsText(HQAccounts.size() + "");
                                gui.setTotalConnAccounts(String.format("%d / %d", HQ_API.totalBotsInTheGame(), HQAccounts.size()));
                            }
                        }
                    }
                    HQAccounts.get(0).display = true;
                    HQAccounts.get(0).getSTK();
                }catch(Exception e){e.printStackTrace();}
            }).start();

            while(headless) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("# ");
                String command = scanner.nextLine();

                if(command.equals("savetokens")){
                    saveTokensToFile();
                } else if(command.equals("test")){
                    System.out.println("testing");
                } else if(command.equals("join")){
                    HQ_API.joinGameHeadless();
                    while(!finishedCmdExec){Thread.sleep(10);}
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void loadingComplete(){
        try {
            saveTokensToFile();
            System.out.println("Loading Complete.");
            Thread.sleep(1000);
            clearScreen();

            System.out.println(" _   _  ___          _____ __  __ _   _ ____   \n" +
                                "| | | |/ _ \\        | ____|  \\/  | | | / ___|  \n" +
                                "| |_| | | | |       |  _| | |\\/| | | | \\___ \\  \n" +
                                "|  _  | |_| |       | |___| |  | | |_| |___) | \n" +
                                "|_| |_|\\__\\_\\       |_____|_|  |_|\\___/|____/  \n" +
                                "                                               \n");
            System.out.println("Written by RobertoDraws and jakecrowley");

            System.out.println("You currently have " + botsLoaded + " loaded bots.\n");
            System.out.println("Available commands are: join, checkbal");

            System.out.print("# ");
            finishedCmdExec = false;
        } catch(Exception e){e.printStackTrace();}
    }

    public static void saveTokensToFile(){
        try {
            PrintWriter writer = new PrintWriter("tokenstemp.txt", "UTF-8");
            for(String s : accountdata){
                writer.println(s);
            }
            writer.close();
            Files.delete(Paths.get("tokens.txt"));
            Files.move(Paths.get("tokenstemp.txt"), Paths.get("tokens.txt"));
        }catch(Exception e){
            System.out.println("Err: " + e.getMessage());
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void sleep(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class GameSummary{
    public List<HQUser> winners;
}

class HQUser{
    public String name;
    public long id;
    public String avatarUrl;
    public String prize;
    public int wins;
}
