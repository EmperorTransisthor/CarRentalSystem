package CarRental;


//import org.apache.log4j.Logger;

import javax.swing.*;

public class main {
    public static void main(String[] args){
        /*JFrame frame = new JFrame("Login screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new test());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        // Set icon image
        ImageIcon icon = new ImageIcon("resources/icon.png");
        frame.setIconImage(icon.getImage());*/
        Variables.logger.info("Program started");
        Login loginScreen = new Login();
        //System.out.println("program started");
        //JFrame frame = new JFrameSet(new Login(), "Login screen");

        //JFramesManager.

        /*if (test.isLoginCorrectly())
        {
            frame.dispose();        // closes login window


        }*/



    }
}
