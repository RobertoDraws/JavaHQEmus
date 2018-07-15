import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    private JButton answerbutton1;
    private JButton answerbutton2;
    private JButton answerbutton3;
    private JPanel MainGUI;
    private JTextField Cashout;
    private JTextArea paypal;
    private JButton conf_cashout;
    private JLabel questionLabel;

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
                    hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(1));
                }
            }
        } );

        answerbutton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(2));
                }
            }
        } );

        conf_cashout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //confirm cashout btn clicked
            }
        });
    }

    public void setQuestion(String text){
        questionLabel.setText(text);
        questionLabel.updateUI();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().MainGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        questionLabel = new JLabel("Question");
    }
}
