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
    private JButton startButton;
    private JButton stopButton;

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

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String wsurl = Main.HQAccounts.get(0).getAPIData().broadcast.socketUrl.replace("https", "wss");
                for(HQ_API client : Main.HQAccounts){
                    new Thread(() -> {client.openWebSocket(wsurl);}).start();
                }
                Main.HQAccounts.get(0).display = true;
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API client : Main.HQAccounts){
                    client.closeWebSocket();
                }
            }
        });

        conf_cashout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.HQAccounts.get(0).cashout(Cashout.getText());
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
