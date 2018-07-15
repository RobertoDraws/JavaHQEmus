import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    public JTextField questionbox;
    public JButton answerbutton1;
    public JButton answerbutton2;
    public JButton answerbutton3;
    public JPanel MainGUI;
    public JTextField Cashout;
    public JTextArea paypal;
    public JButton conf_cashout;

    public MainGUI(){
        answerbutton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                }
            }
        } );

        answerbutton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                }
            }
        } );

        answerbutton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                }
            }
        } );

        conf_cashout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //confirm cashout btn clicked
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().MainGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
