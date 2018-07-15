import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static int accountsLimit = 30;
    public static MainGUI gui;
    public static ArrayList<HQ_API> HQAccounts = new ArrayList<>();

    public static void main(String[] args){
        gui = new MainGUI();
        gui.main(args);

        HQAccounts.add(new HQ_API("test"));
        HQAccounts.get(0).display = true;

        /*
        ArrayList<HQ_API> HQAccounts = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File("tokens.txt"));
            while(s.hasNextLine() && HQAccounts.size() < accountsLimit){
                HQAccounts.add(new HQ_API(s.nextLine()));
            }
            HQAccounts.get(0).display = true;
        } catch(Exception e){
            e.printStackTrace();
        }
        */
    }
}
