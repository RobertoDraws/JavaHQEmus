import org.omg.CORBA.Environment;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static int accountsLimit = 2800;
    public static MainGUI gui = new MainGUI();
    public static ArrayList<HQ_API> HQAccounts = new ArrayList<>();

    public static void main(String[] args){
        gui.main(args);

        try{
            //should add separate files later
            Scanner s = new Scanner(new File("tokens.txt"));
            while(s.hasNextLine() && HQAccounts.size() < accountsLimit){
                HQAccounts.add(new HQ_API(s.nextLine()));
                MainGUI.botsLoadedLabelText = "Bots Loaded: " + HQAccounts.size();
                MainGUI.botsStillInLabelText = String.format("Bots Still In: %d/%d", HQ_API.totalBotsInTheGame, HQAccounts.size());
            }
            HQAccounts.get(0).display = true;

            System.out.println("Finished Loading.");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
