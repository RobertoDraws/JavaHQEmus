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
    private JButton splitButton;

    public MainGUI(){
        answerbutton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    new Thread(() -> { hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0)); }).start();
                }
            }
        } );

        answerbutton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    new Thread(() -> { hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(1)); }).start();
                }
            }
        } );

        answerbutton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    new Thread(() -> { hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(2)); }).start();
                }
            }
        } );

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String wsurl = "ws://127.0.0.1:8081/"; /*Main.HQAccounts.get(0).getAPIData().broadcast.socketUrl.replace("https", "wss")*/
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

        splitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int splitAmt = Main.HQAccounts.size()/3;

                for(int i = 0; i < splitAmt; i++){
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> { client.sendAnswer(HQ_API.lastQuestion.answers.get(0)); }).start();
                }

                for(int i = splitAmt; i < splitAmt*2; i++){
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> { client.sendAnswer(HQ_API.lastQuestion.answers.get(1)); }).start();
                }

                for(int i = splitAmt*2; i < splitAmt*3; i++){
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> { client.sendAnswer(HQ_API.lastQuestion.answers.get(2)); }).start();
                }
            }
        });

        conf_cashout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HQ_API("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIxMTcyNTYxLCJ1c2VybmFtZSI6IlJvYmVydG9EcmF3c0dPRCIsImF2YXRhclVybCI6InMzOi8vaHlwZXNwYWNlLXF1aXovZGVmYXVsdF9hdmF0YXJzL1VudGl0bGVkLTFfMDAwMV9ibHVlLnBuZyIsInRva2VuIjoicFNFRkV1Iiwicm9sZXMiOltdLCJjbGllbnQiOiIiLCJndWVzdElkIjpudWxsLCJ2IjoxLCJpYXQiOjE1MzAwODExMzMsImV4cCI6MTUzNzg1NzEzMywiaXNzIjoiaHlwZXF1aXovMSJ9.JIBJG0qhZ_AnmQMG46mrSVTJZH4CqvcAh7rdhYit-wc").cashout(Cashout.getText());
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
