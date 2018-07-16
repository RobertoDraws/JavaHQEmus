import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Mon Jul 16 01:55:12 EDT 2018
 */



/**
 * @author unknown
 */
public class maingui extends JPanel {
    public maingui() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label3 = new JLabel();
        ServerStatus = new JLabel();
        questionresponce = new JLabel();
        label5 = new JLabel();
        label15 = new JLabel();
        label1 = new JLabel();
        totalaccounts = new JLabel();
        AnswerResponce1 = new JLabel();
        Toggle_Answer1 = new JToggleButton();
        domain = new JTextField();
        label2 = new JLabel();
        label11 = new JLabel();
        AnswerResponce2 = new JLabel();
        Toggle_Answer2 = new JToggleButton();
        Cashout_Toggle = new JToggleButton();
        toggleButton1 = new JToggleButton();
        AnswerResponce3 = new JLabel();
        Toggle_Answer3 = new JToggleButton();
        textField1 = new JTextField();
        label14 = new JLabel();
        button8 = new JButton();
        button4 = new JButton();
        label13 = new JLabel();
        Open_Google_Search = new JButton();
        makeitrainglitch = new JButton();
        getmoreemulives = new JButton();
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
            "[45]"));

        //---- label3 ----
        label3.setText("Connection To Web Server: (Aka if Parcel server is up)");
        add(label3, "cell 0 0,align center center,grow 0 0");

        //---- ServerStatus ----
        ServerStatus.setText("Online (Green) / Offline (Red)");
        add(ServerStatus, "cell 1 0,align center center,grow 0 0");

        //---- questionresponce ----
        questionresponce.setText("Question");
        add(questionresponce, "cell 2 0,align center center,grow 0 0");

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
        totalaccounts.setText("{responce}");
        add(totalaccounts, "cell 1 1,align center center,grow 0 0");

        //---- AnswerResponce1 ----
        AnswerResponce1.setText("...");
        add(AnswerResponce1, "cell 2 1,align center center,grow 0 0");

        //---- Toggle_Answer1 ----
        Toggle_Answer1.setText("Answer 1");
        add(Toggle_Answer1, "cell 3 1,align center center,grow 0 0");

        //---- domain ----
        domain.setText("domain");
        add(domain, "cell 4 1,align center center,grow 0 0");

        //---- label2 ----
        label2.setText("Total Connected Accounts:");
        add(label2, "cell 0 2,align center center,grow 0 0");

        //---- label11 ----
        label11.setText("{responce}");
        add(label11, "cell 1 2,align center center,grow 0 0");

        //---- AnswerResponce2 ----
        AnswerResponce2.setText("...");
        add(AnswerResponce2, "cell 2 2,align center center,grow 0 0");

        //---- Toggle_Answer2 ----
        Toggle_Answer2.setText("Answer 2");
        add(Toggle_Answer2, "cell 3 2,align center center,grow 0 0");

        //---- Cashout_Toggle ----
        Cashout_Toggle.setText("Cashout");
        add(Cashout_Toggle, "cell 4 2,align center center,grow 0 0");

        //---- toggleButton1 ----
        toggleButton1.setText("Toggle Web Connection");
        add(toggleButton1, "cell 0 3,align center center,grow 0 0");

        //---- AnswerResponce3 ----
        AnswerResponce3.setText("...");
        add(AnswerResponce3, "cell 2 3,align center center,grow 0 0");

        //---- Toggle_Answer3 ----
        Toggle_Answer3.setText("Answer 3");
        add(Toggle_Answer3, "cell 3 3,align center center,grow 0 0");

        //---- textField1 ----
        textField1.setText("email");
        add(textField1, "cell 4 3,align center center,grow 0 0");

        //---- label14 ----
        label14.setText("Balance: Balance Responce");
        add(label14, "cell 0 4,align center center,grow 0 0");

        //---- button8 ----
        button8.setText("Cashout");
        add(button8, "cell 4 4,align center center,grow 0 0");

        //---- button4 ----
        button4.setText("Update Balance");
        add(button4, "cell 0 5,align center center,grow 0 0");

        //---- label13 ----
        label13.setText("Other: ");
        add(label13, "cell 0 6,align center center,grow 0 0");

        //---- Open_Google_Search ----
        Open_Google_Search.setText("Google Search");
        add(Open_Google_Search, "cell 1 6,align center center,grow 0 0");

        //---- makeitrainglitch ----
        makeitrainglitch.setText("Get Weekly Life");
        add(makeitrainglitch, "cell 2 6,align center center,grow 0 0");

        //---- getmoreemulives ----
        getmoreemulives.setText("Get More Emulator Lives");
        add(getmoreemulives, "cell 3 6,align center center,grow 0 0");

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

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label3;
    private JLabel ServerStatus;
    private JLabel questionresponce;
    private JLabel label5;
    private JLabel label15;
    private JLabel label1;
    private JLabel totalaccounts;
    private JLabel AnswerResponce1;
    private JToggleButton Toggle_Answer1;
    private JTextField domain;
    private JLabel label2;
    private JLabel label11;
    private JLabel AnswerResponce2;
    private JToggleButton Toggle_Answer2;
    private JToggleButton Cashout_Toggle;
    private JToggleButton toggleButton1;
    private JLabel AnswerResponce3;
    private JToggleButton Toggle_Answer3;
    private JTextField textField1;
    private JLabel label14;
    private JButton button8;
    private JButton button4;
    private JLabel label13;
    private JButton Open_Google_Search;
    private JButton makeitrainglitch;
    private JButton getmoreemulives;
    private JLabel label16;
    private JProgressBar CashoutprogressBar;
    private JLabel label17;
    private JFormattedTextField Withdawl_Pin;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
