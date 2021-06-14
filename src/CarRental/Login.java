/*
 * Created by JFormDesigner on Mon May 31 12:38:37 CEST 2021
 */

package CarRental;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;

import CarRental.Old.JFrameSet;
import net.miginfocom.swing.*;

/**
 * @author Revan
 */
public class Login extends JFrame {

    private static Login login;
    public Login()
    {
        super("Login");
        initComponents();
    }

    /*
    // SINGLETON
    public static Login getInstance()
    {
        if (login == null)
        {
            login = new Login("Login");
            login.pack();
            login.setResizable(false);
            login.setVisible(true);
            login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            login.setIconImage(icon.getImage());
        }

        return login;
    }
    */

    private void loginButtonActionPerformed(ActionEvent e) {
        Variables.Login = login_textField.getText();
        Variables.password = passwordField.getPassword();                 // password is array of chars, because string is a plaintext and is more difficult to erase and override than array


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            // chcę, żeby wartości login i hasło były potem pobierane podczas logowania do systemu
            // ToDo later
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
            JOptionPane.showMessageDialog(this, "Database connected correctly");
            JFrame frame = new JFrameSet(new MainMenu(), "Main menu");
            loginFrame.dispose();
        } catch (SQLException throwables) {
            String error = throwables.toString();

            if (error.indexOf("Access denied for user") != -1) {
                JOptionPane.showMessageDialog(this, "Login or password incorrect");
                Variables.logger.error("Login or password incorrect");
                Variables.logger.error(throwables.getMessage());
            }
            else {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error("Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            }

            //throwables.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }


        /*char[] correctPassword = {'u', 's', 'e', 'r'};

        // has to be replaced with JDBC
        if (Variables.Login.equals("user") && Arrays.equals(Variables.password, correctPassword))
        {
            // login
            //loginCorrectly = true;
            JFrame frame = new JFrameSet(new MainMenu(), "Main menu");
            loginFrame.dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(loginFrame, "Username and Password not correct");
        }*/
    }

    private void exitButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Revan
        loginFrame = new JFrame();
        titleLabel = new JLabel();
        loginLabel = new JLabel();
        login_textField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        button2 = new JButton();
        button1 = new JButton();

        //======== loginFrame ========
        {
            var loginFrameContentPane = loginFrame.getContentPane();
            loginFrameContentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- titleLabel ----
            titleLabel.setText("Car Rental Service");
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleLabel.getFont().getSize() + 10f));
            loginFrameContentPane.add(titleLabel, "cell 5 1 9 2");

            //---- loginLabel ----
            loginLabel.setText("login");
            loginFrameContentPane.add(loginLabel, "cell 4 4 2 1");
            loginFrameContentPane.add(login_textField, "cell 8 4 8 1");

            //---- passwordLabel ----
            passwordLabel.setText("password");
            loginFrameContentPane.add(passwordLabel, "cell 4 7");
            loginFrameContentPane.add(passwordField, "cell 8 7 8 1");

            //---- button2 ----
            button2.setText("Login");
            button2.setFont(button2.getFont().deriveFont(button2.getFont().getStyle() | Font.BOLD));
            button2.addActionListener(e -> loginButtonActionPerformed(e));
            loginFrameContentPane.add(button2, "cell 4 9 4 1");

            //---- button1 ----
            button1.setText("Exit");
            button1.setFont(button1.getFont().deriveFont(button1.getFont().getStyle() | Font.BOLD));
            button1.addActionListener(e -> exitButtonActionPerformed(e));
            loginFrameContentPane.add(button1, "cell 11 9 5 1");
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(loginFrame.getOwner());

            // user added
            loginFrame.pack();
            loginFrame.setResizable(false);
            loginFrame.setVisible(true);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            loginFrame.setIconImage(icon.getImage());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Revan
    private JFrame loginFrame;
    private JLabel titleLabel;
    private JLabel loginLabel;
    private JTextField login_textField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton button2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
