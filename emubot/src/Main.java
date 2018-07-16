import com.google.gson.Gson;
import org.omg.CORBA.Environment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static int accountsLimit = 500;
    public static MainGUI gui = new MainGUI();
    public static ArrayList<HQ_API> HQAccounts = new ArrayList<>();

    public static void main(String[] args){
        try{
            gui.OpenGUI();
            //should add separate files later
            Scanner s = new Scanner(new File("tokens.txt"));
            while(s.hasNextLine() && HQAccounts.size() < accountsLimit){
                HQAccounts.add(new HQ_API(s.nextLine()));
                //gui.setTotalAccountsText(HQAccounts.size()+"");
                //gui.setTotalConnAccounts(String.format("%d/%d", HQ_API.totalBotsInTheGame, HQAccounts.size())
            }
            HQAccounts.get(0).display = true;
        } catch(Exception e){
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
