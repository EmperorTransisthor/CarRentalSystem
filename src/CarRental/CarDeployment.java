/*
 * Created by JFormDesigner on Tue Jun 08 14:15:45 CEST 2021
 */

package CarRental;

import java.awt.event.*;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import com.toedter.calendar.*;
import net.miginfocom.swing.*;

/**
 * @author Revan
 */
public class CarDeployment extends JFrame {
    private static CarDeployment CDe;
    private CarDeployment() {
        initComponents();
    }

    private CarDeployment(String title)
    {
        super(title);
        initComponents();
    }

    public static CarDeployment getInstance()
    {
        if (CDe == null)
        {
            CDe = new CarDeployment("Car Deployment");
            CDe.pack();
            CDe.setResizable(false);
            CDe.setVisible(true);
            CDe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CDe.setIconImage(icon.getImage());
        }

        return CDe;
    }

    private void carID_textFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            //await().atMost(10, TimeUnit.MILLISECONDS).until(() -> );
            String car_id = carID_textField.getText();
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                        "FROM tblRental " +
                        "WHERE car_id LIKE ?");
                preparedStatement.setString(1, car_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Order not found");

                else
                {
                    String state = resultSet.getString("state");
                    if (state.equals("active"))
                        JOptionPane.showMessageDialog(this, "Chosen active order. Changes will affect rent and return dates");
                    else if (state.equals("cancelled") || state.equals("returned"))
                        JOptionPane.showMessageDialog(this, "Warning: chosen cancelled or returned order.");
                    orderID_textField.setText(resultSet.getString("id"));
                    customerID_textField.setText(resultSet.getString("cust_id"));
                    deploymentDate_dateChooser.setDate(new Date());
                }

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());
            }
        }

    }

    private void orderID_textFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            //await().atMost(10, TimeUnit.MILLISECONDS).until(() -> );
            String id = orderID_textField.getText();
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                        "FROM tblRental " +
                        "WHERE id LIKE ?");
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Order not found");

                else
                {
                    String state = resultSet.getString("state");
                    if (state.equals("active"))
                        JOptionPane.showMessageDialog(this, "Chosen active order. Changes will affect rent and return dates");
                    else if (state.equals("cancelled") || state.equals("returned"))
                        JOptionPane.showMessageDialog(this, "Warning: chosen cancelled or returned order.");

                    carID_textField.setText(resultSet.getString("car_id"));
                    customerID_textField.setText(resultSet.getString("cust_id"));
                    deploymentDate_dateChooser.setDate(new Date());
                }

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());
            }
        }

    }

    private void customerID_textFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            //await().atMost(10, TimeUnit.MILLISECONDS).until(() -> );
            String cust_id = customerID_textField.getText();
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                        "FROM tblRental " +
                        "WHERE cust_id LIKE ?");
                preparedStatement.setString(1, cust_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Customer not found");

                else
                {
                    String state = resultSet.getString("state");
                    if (state.equals("active"))
                        JOptionPane.showMessageDialog(this, "Chosen active order. Changes will affect rent and return dates");
                    else if (state.equals("cancelled") || state.equals("returned"))
                        JOptionPane.showMessageDialog(this, "Warning: chosen cancelled or returned order.");
                    orderID_textField.setText(resultSet.getString("id"));
                    carID_textField.setText(resultSet.getString("car_id"));
                    deploymentDate_dateChooser.setDate(new Date());
                }

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());
            }
        }

    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
        CDe = null;
    }

    private void submitButtonActionPerformed(ActionEvent e) {
        String order_id = orderID_textField.getText();
        String car_id = carID_textField.getText();
        String cust_id = customerID_textField.getText();

        if (areBlankTextFields())
            return;

        java.sql.Date deploymentDate = new java.sql.Date(deploymentDate_dateChooser.getDate().getTime());          // converts java date to sql date

        try
        {
            // ToDo zabezpieczenie przed pustymi polami
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            PreparedStatement preparedStatement = con.prepareStatement("UPDATE tblRental " +
                    "SET state='active' " +
                    "WHERE id LIKE ?");
            preparedStatement.setString(1, order_id);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car rented successfully");

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }

    }

    private boolean areBlankTextFields()
    {
        if (carID_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Car ID field can't be blank");
            return true;
        }

        // Customer address can be blank
        else if (deploymentDate_dateChooser.getDate() == null)
        {
            JOptionPane.showMessageDialog(this, "Please select deployment date");
            return true;
        }

        else if (orderID_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Order ID field can't be blank");
            return true;
        }

        else if (customerID_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer ID field can't be blank");
            return true;
        }

        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - KK
        label1 = new JLabel();
        carID_textField = new JTextField();
        label2 = new JLabel();
        orderID_textField = new JTextField();
        label3 = new JLabel();
        customerID_textField = new JTextField();
        label4 = new JLabel();
        deploymentDate_dateChooser = new JDateChooser();
        submitButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
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
            "[]"));

        //---- label1 ----
        label1.setText("Car ID");
        contentPane.add(label1, "cell 2 1 2 1");

        //---- carID_textField ----
        carID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                carID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(carID_textField, "cell 7 1 10 1");

        //---- label2 ----
        label2.setText("Order ID");
        contentPane.add(label2, "cell 2 3 2 1");

        //---- orderID_textField ----
        orderID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                orderID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(orderID_textField, "cell 7 3 10 1");

        //---- label3 ----
        label3.setText("Customer ID");
        contentPane.add(label3, "cell 2 5 2 1");

        //---- customerID_textField ----
        customerID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                customerID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(customerID_textField, "cell 7 5 10 1");

        //---- label4 ----
        label4.setText("Deployment date");
        contentPane.add(label4, "cell 2 7 3 1");
        contentPane.add(deploymentDate_dateChooser, "cell 7 7 10 1");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.addActionListener(e -> submitButtonActionPerformed(e));
        contentPane.add(submitButton, "cell 2 10 5 1");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
        contentPane.add(cancelButton, "cell 9 10 6 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JLabel label1;
    private JTextField carID_textField;
    private JLabel label2;
    private JTextField orderID_textField;
    private JLabel label3;
    private JTextField customerID_textField;
    private JLabel label4;
    private JDateChooser deploymentDate_dateChooser;
    private JButton submitButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
