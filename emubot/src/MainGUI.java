package emubot.src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Mon Jul 16 01:55:12 EDT 2018
 */



/**
 * @author unknown
 */
public class MainGUI extends JPanel {
    public MainGUI() {
        initComponents();
    }

    public void setTotalAccountsText(String text){
        totalaccounts.setText(text);
    }

    public void setTotalConnAccounts(String text){
        totalConnAccounts.setText(text);
    }

    public void setQuestionText(String text){
        questionResponse.setText("<html>"+text+"</html>");
    }

    public void setAnswer1Text(String text){
        Toggle_Answer1.setText(text);
    }

    public void setAnswer2Text(String text){
        Toggle_Answer2.setText(text);
    }

    public void setAnswer3Text(String text){
        Toggle_Answer3.setText(text);
    }

    public int answersSubbed = 0;
    public void answerSubmitted(){
        answersSubmittedLabel.setText("Answers Submitted: " + ++answersSubbed);
    }

    public void resetAnswersSubmitted(){
        answersSubbed = 0;
        answersSubmittedLabel.setText("Answers Submitted: " + answersSubbed);
    }

    public void resetButtons(){
        Toggle_Answer1.setSelected(false);
        Toggle_Answer2.setSelected(false);
        Toggle_Answer3.setSelected(false);
        splitButton.setSelected(false);
        Toggle_Answer1.setEnabled(true);
        Toggle_Answer2.setEnabled(true);
        Toggle_Answer3.setEnabled(true);
        splitButton.setEnabled(true);
    }

    private void lockIn(){
        Toggle_Answer1.setEnabled(false);
        Toggle_Answer2.setEnabled(false);
        Toggle_Answer3.setEnabled(false);
        splitButton.setEnabled(false);
    }

    private void toggleConnectionClicked(MouseEvent e) {
        if(((JToggleButton)e.getComponent()).isSelected()){
            HQAPIData apiData = Main.HQAccounts.get(0).getAPIData();
            if(apiData.active) {
                String wsurl = Main.HQAccounts.get(0).getAPIData().broadcast.socketUrl.replace("https", "wss");
                for (HQ_API client : Main.HQAccounts) {
                    new Thread(() -> {
                        client.openWebSocket(wsurl);
                    }).start();
                }
                Main.HQAccounts.get(0).display = true;
            } else {
                JOptionPane.showMessageDialog(frame, "HQ WebSocket is not live!");
                ((JToggleButton)e.getComponent()).setSelected(false);
            }
        } else {
            HQ_API.totalBotsInTheGame = 0;
            Main.gui.setTotalConnAccounts(String.format("%d / %d", HQ_API.totalBotsInTheGame, Main.HQAccounts.size()));
            for(HQ_API client : Main.HQAccounts){
                client.closeWebSocket();
            }
        }
    }

    private double bal = 0;
    private int checkedAccounts = 0;

    private void updateBalanceClicked(MouseEvent e) {
        if(!balanceLabel.getText().equals("Balance: Updating...")) {
            char countryChar = Main.HQAccounts.get(0).getCountryChar();
            balanceLabel.setText("Balance: Updating...");
            new Thread(() -> {
                for (HQ_API client : Main.HQAccounts) {
                    new Thread(() -> {
                        double b = client.getBalance();
                        if(b > 0)
                            System.out.println(b + " " + client.bearer);

                        balanceLabel.setText(String.format("Balance: %s%.2f [%d / %d]", countryChar, b += bal, checkedAccounts, Main.HQAccounts.size()));
                        checkedAccounts++;
                    }).start();
                }
            }).start();
        }
    }

    private void openGoogleSearchMouseClicked(MouseEvent e) {
        //TODO implement feature
        JOptionPane.showMessageDialog(frame, "Feature not implemented yet.");
    }

    private void weeklyLifeMouseClicked(MouseEvent e) {
        //TODO implement feature
        JOptionPane.showMessageDialog(frame, "Feature not implemented yet.");
    }

    private void moreEmuLivesButtonClicked(MouseEvent e) {
        //TODO implement feature
        JOptionPane.showMessageDialog(frame, "Feature not implemented yet.");
    }

    public void sendDialogBox(String text){
        JOptionPane.showMessageDialog(frame, text);
    }

    private void Answer1Clicked(MouseEvent e) {
        if(e.getComponent().isEnabled()) {
            if(HQ_API.lastQuestion != null) {
                lockIn();
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                    }).start();
                }
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                        resetButtons();
                    } catch (Exception exc) { }
                }).start();
            }
        }
    }

    private void Answer2Clicked(MouseEvent e) {
        if(e.getComponent().isEnabled()) {
            if(HQ_API.lastQuestion != null) {
                lockIn();
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(1));
                    }).start();
                }
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                        resetButtons();
                    } catch (Exception exc) { }
                }).start();
            }
        }
    }

    private void Answer3Clicked(MouseEvent e) {
        if(e.getComponent().isEnabled()) {
            if(HQ_API.lastQuestion != null) {
                lockIn();
                for (HQ_API hqclient : Main.HQAccounts) {
                    new Thread(() -> {
                        hqclient.sendAnswer(HQ_API.lastQuestion.answers.get(2));
                    }).start();
                }
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                        resetButtons();
                    } catch (Exception exc) { }
                }).start();
            }
        }
    }

    private void SplitButtonClicked(MouseEvent e) {
        if(e.getComponent().isEnabled()) {
            if (HQ_API.lastQuestion != null) {
                lockIn();
                int splitAmt = Main.HQAccounts.size() / 3;

                for (int i = 0; i < splitAmt; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(0));
                    }).start();
                }

                for (int i = splitAmt; i < splitAmt * 2; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(1));
                    }).start();
                }

                for (int i = splitAmt * 2; i < splitAmt * 3; i++) {
                    HQ_API client = Main.HQAccounts.get(i);
                    new Thread(() -> {
                        client.sendAnswer(HQ_API.lastQuestion.answers.get(2));
                    }).start();
                }
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(150);
                        resetButtons();
                    } catch (Exception exc) { }
                }).start();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label3 = new JLabel();
        ServerStatus = new JLabel();
        questionResponse = new JLabel();
        label5 = new JLabel();
        label15 = new JLabel();
        label1 = new JLabel();
        totalaccounts = new JLabel();
        AnswerResponce1 = new JLabel();
        Toggle_Answer1 = new JToggleButton();
        domain = new JTextField();
        label2 = new JLabel();
        totalConnAccounts = new JLabel();
        AnswerResponce2 = new JLabel();
        Toggle_Answer2 = new JToggleButton();
        Cashout_Toggle = new JToggleButton();
        toggleButton1 = new JToggleButton();
        AnswerResponce3 = new JLabel();
        Toggle_Answer3 = new JToggleButton();
        textField1 = new JTextField();
        balanceLabel = new JLabel();
        answersSubmittedLabel = new JLabel();
        splitButton = new JToggleButton();
        button8 = new JButton();
        button4 = new JButton();
        label13 = new JLabel();
        Open_Google_Search = new JButton();
        makeitrainglitch = new JButton();
        moreEmuLivesButton = new JButton();
        label16 = new JLabel();
        CashoutprogressBar = new JProgressBar();
        label17 = new JLabel();
        Withdawl_Pin = new JFormattedTextField();

        //======== this ========
        setLayout(new MigLayout(
            "fillx,hidemode 3",
            // columns
            "[271,fill]" +
            "[171,fill]" +
            "[252,fill]" +
            "[229,fill]" +
            "[fill]",
            // rows
            "[40]" +
            "[44]" +
            "[45]" +
            "[48]" +
            "[46]" +
            "[50]" +
            "[42]" +
            "[45]" +
            "[]"));

        //---- label3 ----
        label3.setText("Connection To Web Server: (Aka if Parcel server is up)");
        add(label3, "cell 0 0,align center center,grow 0 0");

        //---- ServerStatus ----
        ServerStatus.setText("Online (Green) / Offline (Red)");
        add(ServerStatus, "cell 1 0,align center center,grow 0 0");

        //---- questionResponse ----
        questionResponse.setText("<html>Question</html>");
        add(questionResponse, "cell 2 0,align center center,grow 0 0");

        //---- label5 ----
        label5.setText("LOCKIN");
        add(label5, "cell 3 0,align center center,grow 0 0");

        //---- label15 ----
        label15.setText("Money Management");
        add(label15, "cell 4 0,align center center,grow 0 0");

        //---- label1 ----
        label1.setText("Total Standing Byaccounts:");
        add(label1, "cell 0 1,align center center,grow 0 0");

        //---- totalaccounts ----
        totalaccounts.setText("{response}");
        add(totalaccounts, "cell 1 1,align center center,grow 0 0");

        //---- AnswerResponce1 ----
        AnswerResponce1.setText("...");
        add(AnswerResponce1, "cell 2 1,align center center,grow 0 0");

        //---- Toggle_Answer1 ----
        Toggle_Answer1.setText("Answer 1");
        Toggle_Answer1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Answer1Clicked(e);
            }
        });
        add(Toggle_Answer1, "cell 3 1,align center center,grow 0 0");

        //---- domain ----
        domain.setText("domain");
        add(domain, "cell 4 1,align center center,grow 0 0");

        //---- label2 ----
        label2.setText("Total Connected Accounts:");
        add(label2, "cell 0 2,align center center,grow 0 0");

        //---- totalConnAccounts ----
        totalConnAccounts.setText("{response}");
        add(totalConnAccounts, "cell 1 2,align center center,grow 0 0");

        //---- AnswerResponce2 ----
        AnswerResponce2.setText("...");
        add(AnswerResponce2, "cell 2 2,align center center,grow 0 0");

        //---- Toggle_Answer2 ----
        Toggle_Answer2.setText("Answer 2");
        Toggle_Answer2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Answer2Clicked(e);
            }
        });
        add(Toggle_Answer2, "cell 3 2,align center center,grow 0 0");

        //---- Cashout_Toggle ----
        Cashout_Toggle.setText("Cashout");
        add(Cashout_Toggle, "cell 4 2,align center center,grow 0 0");

        //---- toggleButton1 ----
        toggleButton1.setText("Toggle Web Connection");
        toggleButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleConnectionClicked(e);
            }
        });
        add(toggleButton1, "cell 0 3,align center center,grow 0 0");

        //---- AnswerResponce3 ----
        AnswerResponce3.setText("...");
        add(AnswerResponce3, "cell 2 3,align center center,grow 0 0");

        //---- Toggle_Answer3 ----
        Toggle_Answer3.setText("Answer 3");
        Toggle_Answer3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Answer3Clicked(e);
            }
        });
        add(Toggle_Answer3, "cell 3 3,align center center,grow 0 0");

        //---- textField1 ----
        textField1.setText("email");
        add(textField1, "cell 4 3,align center center,grow 0 0");

        //---- balanceLabel ----
        balanceLabel.setText("Balance: {response}");
        add(balanceLabel, "cell 0 4,align center center,grow 0 0");

        //---- answersSubmittedLabel ----
        answersSubmittedLabel.setText("Answers Submitted: 0");
        add(answersSubmittedLabel, "cell 2 4,align center center,grow 0 0");

        //---- splitButton ----
        splitButton.setText("Split");
        splitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Answer3Clicked(e);
                SplitButtonClicked(e);
            }
        });
        add(splitButton, "cell 3 4,align center center,grow 0 0");

        //---- button8 ----
        button8.setText("Cashout");
        add(button8, "cell 4 4,align center center,grow 0 0");

        //---- button4 ----
        button4.setText("Update Balance");
        button4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateBalanceClicked(e);
            }
        });
        add(button4, "cell 0 5,align center center,grow 0 0");

        //---- label13 ----
        label13.setText("Other: ");
        add(label13, "cell 0 6,align center center,grow 0 0");

        //---- Open_Google_Search ----
        Open_Google_Search.setText("Google Search");
        Open_Google_Search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openGoogleSearchMouseClicked(e);
            }
        });
        add(Open_Google_Search, "cell 1 6,align center center,grow 0 0");

        //---- makeitrainglitch ----
        makeitrainglitch.setText("Get Weekly Life");
        makeitrainglitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                weeklyLifeMouseClicked(e);
            }
        });
        add(makeitrainglitch, "cell 2 6,align center center,grow 0 0");

        //---- moreEmuLivesButton ----
        moreEmuLivesButton.setText("Get More Emulator Lives");
        moreEmuLivesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                moreEmuLivesButtonClicked(e);
            }
        });
        add(moreEmuLivesButton, "cell 3 6,align center center,grow 0 0");

        //---- label16 ----
        label16.setText("Cashout Progress:");
        add(label16, "cell 0 7,align center center,grow 0 0");
        add(CashoutprogressBar, "cell 1 7,aligny center,growy 0");

        //---- label17 ----
        label17.setText("Withdraw Pin");
        add(label17, "cell 2 7,align center center,grow 0 0");

        //---- Withdawl_Pin ----
        Withdawl_Pin.setText("####");
        add(Withdawl_Pin, "cell 3 7,align center center,grow 0 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void OpenGUI(){
        JFrame frame = new JFrame("HQ EMUS");
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.frame = frame;
    }

    public JFrame frame;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label3;
    private JLabel ServerStatus;
    private JLabel questionResponse;
    private JLabel label5;
    private JLabel label15;
    private JLabel label1;
    private JLabel totalaccounts;
    private JLabel AnswerResponce1;
    private JToggleButton Toggle_Answer1;
    private JTextField domain;
    private JLabel label2;
    private JLabel totalConnAccounts;
    private JLabel AnswerResponce2;
    private JToggleButton Toggle_Answer2;
    private JToggleButton Cashout_Toggle;
    private JToggleButton toggleButton1;
    private JLabel AnswerResponce3;
    private JToggleButton Toggle_Answer3;
    private JTextField textField1;
    private JLabel balanceLabel;
    private JLabel answersSubmittedLabel;
    private JToggleButton splitButton;
    private JButton button8;
    private JButton button4;
    private JLabel label13;
    private JButton Open_Google_Search;
    private JButton makeitrainglitch;
    private JButton moreEmuLivesButton;
    private JLabel label16;
    private JProgressBar CashoutprogressBar;
    private JLabel label17;
    private JFormattedTextField Withdawl_Pin;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
