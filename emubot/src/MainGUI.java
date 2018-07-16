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

    public void setQuestionText(String text){
        questionresponce.setText(text);
    }

    private void openGoogleSearchMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void weeklyLifeMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label3 = new JLabel();
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
        setForeground(new Color(102, 102, 102));
        setBackground(new Color(51, 51, 51));
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
        label3.setText("Online/Offline");
        label3.setForeground(Color.white);
        add(label3, "cell 0 0,align center center,grow 0 0");

        //---- questionresponce ----
        questionresponce.setText("Question");
        questionresponce.setForeground(Color.white);
        add(questionresponce, "cell 2 0,align center center,grow 0 0");

        //---- label5 ----
        label5.setText("LOCKIN");
        label5.setForeground(Color.white);
        add(label5, "cell 3 0,align center center,grow 0 0");

        //---- label15 ----
        label15.setText("Money Management");
        label15.setForeground(Color.white);
        add(label15, "cell 4 0,align center center,grow 0 0");

        //---- label1 ----
        label1.setText("Total Standing By Accounts:");
        label1.setForeground(Color.white);
        add(label1, "cell 0 1,align center center,grow 0 0");

        //---- totalaccounts ----
        totalaccounts.setText("{responce}");
        totalaccounts.setForeground(Color.white);
        add(totalaccounts, "cell 1 1,align center center,grow 0 0");

        //---- AnswerResponce1 ----
        AnswerResponce1.setText("...");
        AnswerResponce1.setForeground(Color.white);
        add(AnswerResponce1, "cell 2 1,align center center,grow 0 0");

        //---- Toggle_Answer1 ----
        Toggle_Answer1.setText("Answer 1");
        Toggle_Answer1.setBackground(Color.gray);
        Toggle_Answer1.setForeground(new Color(204, 204, 204));
        add(Toggle_Answer1, "cell 3 1,align center center,grow 0 0");

        //---- domain ----
        domain.setText("domain");
        add(domain, "cell 4 1,align center center,grow 0 0");

        //---- label2 ----
        label2.setText("Total Connected Accounts:");
        label2.setForeground(Color.white);
        add(label2, "cell 0 2,align center center,grow 0 0");

        //---- label11 ----
        label11.setText("{responce}");
        label11.setForeground(Color.white);
        add(label11, "cell 1 2,align center center,grow 0 0");

        //---- AnswerResponce2 ----
        AnswerResponce2.setText("...");
        AnswerResponce2.setFont(AnswerResponce2.getFont().deriveFont(AnswerResponce2.getFont().getStyle() & ~Font.BOLD));
        AnswerResponce2.setForeground(Color.white);
        add(AnswerResponce2, "cell 2 2,align center center,grow 0 0");

        //---- Toggle_Answer2 ----
        Toggle_Answer2.setText("Answer 2");
        Toggle_Answer2.setBackground(Color.gray);
        Toggle_Answer2.setForeground(new Color(204, 204, 204));
        add(Toggle_Answer2, "cell 3 2,align center center,grow 0 0");

        //---- Cashout_Toggle ----
        Cashout_Toggle.setText("Cashout");
        Cashout_Toggle.setBackground(Color.gray);
        Cashout_Toggle.setForeground(new Color(204, 204, 204));
        add(Cashout_Toggle, "cell 4 2,align center center,grow 0 0");

        //---- toggleButton1 ----
        toggleButton1.setText("Toggle Web Connection");
        toggleButton1.setBackground(Color.gray);
        toggleButton1.setForeground(new Color(204, 204, 204));
        add(toggleButton1, "cell 0 3,align center center,grow 0 0");

        //---- AnswerResponce3 ----
        AnswerResponce3.setText("...");
        AnswerResponce3.setForeground(Color.white);
        add(AnswerResponce3, "cell 2 3,align center center,grow 0 0");

        //---- Toggle_Answer3 ----
        Toggle_Answer3.setText("Answer 3");
        Toggle_Answer3.setBackground(Color.gray);
        Toggle_Answer3.setForeground(new Color(204, 204, 204));
        add(Toggle_Answer3, "cell 3 3,align center center,grow 0 0");

        //---- textField1 ----
        textField1.setText("email");
        add(textField1, "cell 4 3,align center center,grow 0 0");

        //---- label14 ----
        label14.setText("Balance: Balance Responce");
        label14.setForeground(Color.white);
        add(label14, "cell 0 4,align center center,grow 0 0");

        //---- button8 ----
        button8.setText("Cashout");
        button8.setBackground(Color.gray);
        button8.setForeground(new Color(204, 204, 204));
        add(button8, "cell 4 4,align center center,grow 0 0");

        //---- button4 ----
        button4.setText("Update Balance");
        button4.setBackground(Color.gray);
        button4.setForeground(new Color(204, 204, 204));
        add(button4, "cell 0 5,align center center,grow 0 0");

        //---- label13 ----
        label13.setText("Other: ");
        label13.setForeground(Color.white);
        add(label13, "cell 0 6,align center center,grow 0 0");

        //---- Open_Google_Search ----
        Open_Google_Search.setText("Google Search");
        Open_Google_Search.setBackground(Color.gray);
        Open_Google_Search.setForeground(new Color(204, 204, 204));
        Open_Google_Search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openGoogleSearchMouseClicked(e);
                openGoogleSearchMouseClicked(e);
            }
        });
        add(Open_Google_Search, "cell 1 6,align center center,grow 0 0");

        //---- makeitrainglitch ----
        makeitrainglitch.setText("Get Weekly Life");
        makeitrainglitch.setBackground(Color.gray);
        makeitrainglitch.setForeground(new Color(204, 204, 204));
        makeitrainglitch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                weeklyLifeMouseClicked(e);
            }
        });
        add(makeitrainglitch, "cell 2 6,align center center,grow 0 0");

        //---- getmoreemulives ----
        getmoreemulives.setText("Get More Emulator Lives");
        getmoreemulives.setBackground(Color.gray);
        getmoreemulives.setForeground(new Color(204, 204, 204));
        add(getmoreemulives, "cell 3 6,align center center,grow 0 0");

        //---- label16 ----
        label16.setText("Cashout Progress:");
        label16.setForeground(Color.white);
        add(label16, "cell 0 7,align center center,grow 0 0");
        add(CashoutprogressBar, "cell 1 7,aligny center,growy 0");

        //---- label17 ----
        label17.setText("Withdraw Pin:");
        label17.setForeground(Color.white);
        add(label17, "cell 2 7,align center center,grow 0 0");

        //---- Withdawl_Pin ----
        Withdawl_Pin.setText("####");
        add(Withdawl_Pin, "cell 3 7,align center center,grow 0 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public JFrame OpenGUI(){
        JFrame frame = new JFrame("HQ EMUS");
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label3;
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
