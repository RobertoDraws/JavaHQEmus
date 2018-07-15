import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    private JTextField questionbox;
    private JButton answerbutton1;
    private JButton answerbutton2;
    private JButton answerbutton3;
    private JPanel MainGUI;


    public MainGUI() {
        answerbutton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //button 1 clicked
            }
        } );

        answerbutton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //button 2 clicked
            }
        } );

        answerbutton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //button 3 clicked
            }
        } );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().MainGUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
