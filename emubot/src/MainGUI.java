import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    public static String questionText = "Question";
    public static String answerBtn1Text = "Answer 1";
    public static String answerBtn2Text = "Answer 2";
    public static String answerBtn3Text = "Answer 3";
    public static String botsLoadedLabelText = "Bots Loaded: 0";
    public static String botsStillInLabelText = "Bots Still In: 0/0";

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
    private JLabel botsLoadedLabel;
    private JLabel botsStillInLabel;

    public MainGUI(){
        answerbutton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(HQ_API hqclient : Main.HQAccounts){
                    System.out.println("Sending answer 1");
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
                String wsurl = Main.HQAccounts.get(0).getAPIData().broadcast.socketUrl.replace("https", "wss");
                //String wsurl = "ws://127.0.0.1:8081/";
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
                for(HQ_API client : Main.HQAccounts){
                    new Thread(() -> { client.cashout(Cashout.getText()); }).start();
                }
            }
        });

        //really fucking hacky way to change the text of the gui components
        new Thread(() -> {
            try {
                String lastQuestionText = questionText;
                String lastAnswerBtn1Text = answerBtn1Text;
                String lastAnswerBtn2Text = answerBtn2Text;
                String lastAnswerBtn3Text = answerBtn2Text;
                String lastBotsLoadedLabelText = botsLoadedLabelText;
                String lastBotsStillInLabelText = botsStillInLabelText;
                while (true) {
                    if (!lastQuestionText.equals(questionText)) {
                        questionLabel.setText(questionText);
                        lastQuestionText = questionText;
                    } else if (!lastAnswerBtn1Text.equals(answerBtn1Text)) {
                        answerbutton1.setText(answerBtn1Text);
                        lastAnswerBtn1Text = answerBtn1Text;
                    } else if (!lastAnswerBtn2Text.equals(answerBtn2Text)) {
                        answerbutton2.setText(answerBtn2Text);
                        lastAnswerBtn2Text = answerBtn2Text;
                    } else if (!lastAnswerBtn3Text.equals(answerBtn3Text)) {
                        answerbutton3.setText(answerBtn3Text);
                        lastAnswerBtn3Text = answerBtn3Text;
                    } else if (!lastBotsLoadedLabelText.equals(botsLoadedLabelText)){
                        botsLoadedLabel.setText(botsLoadedLabelText);
                        lastBotsLoadedLabelText = botsLoadedLabelText;
                    } else if (!lastBotsStillInLabelText.equals(botsStillInLabelText)){
                        botsStillInLabel.setText(botsStillInLabelText);
                        lastBotsStillInLabelText = botsStillInLabelText;
                    }
                    Thread.sleep(10);
                }
            } catch(Exception e){}
        }).start();
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
