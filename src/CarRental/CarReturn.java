/*
 * Created by JFormDesigner on Mon Jun 07 23:43:38 CEST 2021
 */

package CarRental;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import com.toedter.calendar.*;
import net.miginfocom.swing.*;

import static com.jayway.awaitility.Awaitility.await;

/**
 * @author Revan
 */

public class CarReturn extends JFrame {
    private static CarReturn CRe;
    private static Date rentDate;
    private static Date returnDate;
    private static int daysOfLoan;
    private static int previousFee;

    private CarReturn() {
        initComponents();
    }

    private CarReturn(String title) {
        super(title);
        initComponents();
        returnDate_dateChooser.setDate(new Date());
    }

    public static CarReturn getInstance()
    {
        if (CRe == null)
        {
            CRe = new CarReturn("Car return");
            CRe.pack();
            CRe.setResizable(false);
            CRe.setVisible(true);
            CRe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CRe.setIconImage(icon.getImage());
        }

        return CRe;
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
                        "WHERE id LIKE ? " +
                        "AND state LIKE 'active'");
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Order not found");

                else
                {
                    previousFee = resultSet.getInt("fee");
                    carID_textField.setText(resultSet.getString("car_id"));
                    customerID_textField.setText(resultSet.getString("cust_id"));

                    daysOfLoan = getDifferenceDays(resultSet.getString("date"), String.valueOf(new java.sql.Date(new Date().getTime())));
                    if (daysOfLoan == -1)
                        JOptionPane.showMessageDialog(this, "Return date can't be earlier than " + resultSet.getString("date"));
                    else
                        fee_textField.setText(String.valueOf(Variables.fee(resultSet.getString("car_id"), daysOfLoan)));
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

    private void carID_textFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            //await().atMost(10, TimeUnit.MILLISECONDS).until(() -> );
            String car_id = carID_textField.getText();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                        "FROM tblRental " +
                        "WHERE car_id LIKE ? " +
                        "AND state LIKE 'active'");
                preparedStatement.setString(1, car_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Order not found");

                else {
                    previousFee = resultSet.getInt("fee");
                    orderID_textField.setText(resultSet.getString("id"));
                    customerID_textField.setText(resultSet.getString("cust_id"));

                    daysOfLoan = getDifferenceDays(resultSet.getString("date"), String.valueOf(new java.sql.Date(new Date().getTime())));
                    if (daysOfLoan == -1)
                        JOptionPane.showMessageDialog(this, "Return date can't be earlier than " + resultSet.getString("date"));
                    else
                        fee_textField.setText(String.valueOf(Variables.fee(resultSet.getString("car_id"), daysOfLoan)));
                    // ToDo ilość dni do policzenia na fee + sprawdzenie, czy aby nie było dni dodatkowych
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            //await().atMost(10, TimeUnit.MILLISECONDS).until(() -> );
            String id = customerID_textField.getText();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                        "FROM tblRental " +
                        "WHERE cust_id LIKE ? " +
                        "AND state LIKE 'active'");
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Order not found");

                else if (resultSet.getRow() > 1)
                {
                    JOptionPane.showMessageDialog(this, "This customer has more than 1 order");
                }

                else {
                    previousFee = resultSet.getInt("fee");
                    orderID_textField.setText(resultSet.getString("id"));
                    carID_textField.setText(resultSet.getString("car_id"));

                    daysOfLoan = getDifferenceDays(resultSet.getString("date"), String.valueOf(new java.sql.Date(new Date().getTime())));
                    if (daysOfLoan == -1)
                        JOptionPane.showMessageDialog(this, "Return date can't be earlier than " + resultSet.getString("date"));
                    else
                        fee_textField.setText(String.valueOf(Variables.fee(resultSet.getString("car_id"), daysOfLoan)));


                    // ToDo ilość dni do policzenia na fee + sprawdzenie, czy aby nie było dni dodatkowych
                }

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());            }
        }

    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
        CRe = null;
    }

    private void ordersButtonActionPerformed(ActionEvent e) {
        //Queue queue;
        searchReturnTable sRT = searchReturnTable.getInstance();

        // Zablokowanie obiektu i oczekiwanie na odebranie danych z tabeli
        //await().atMost(200, TimeUnit.MILLISECONDS).until(() -> sRT.done);


/*
        // Po odebraniu zamknięcie okna tabeli
        String orderID      = sRT.orderID_textField.getText();
        String carID        = sRT.carID_textField.getText();
        String customerID   = sRT.customerID_textField.getText();
        sRT.dispose();
*/

    }

    private static int getDifferenceDays(String date1, String date2) {

        System.out.println(date1);
        System.out.println(date2);

        try {
            rentDate = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
            long diffInMillies = Math.abs(rentDate.getTime() - returnDate.getTime());

            // If returnDate is earlier than rentDate, return error
            if (returnDate.getTime() < rentDate.getTime())
                return -1;

            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return (int)diff + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int getDifferenceDays(Date date2)
    {
        if (rentDate == null || returnDate == null || date2 == null)
            return 0;
        returnDate = date2;
        long diffInMillies = Math.abs(rentDate.getTime() - returnDate.getTime());

        // If returnDate is earlier than rentDate, return error
        if (returnDate.getTime() < rentDate.getTime())
            return -1;

        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int)diff + 1;
    }

    private void returnDate_dateChooserPropertyChange(PropertyChangeEvent e) {
        if (returnDate_dateChooser.getDate() == null)
            return;

        Date d1 = returnDate_dateChooser.getDate();
        daysOfLoan = getDifferenceDays(d1);

        if (daysOfLoan == -1) {
            JOptionPane.showMessageDialog(this, "Return date can't be earlier than " + new java.sql.Date(rentDate.getTime()));    // ToDo czy to wystarczy
            daysOfLoan = 0;
            fee_textField.setText(String.valueOf(daysOfLoan));
        }
        else if (carID_textField.getText().isEmpty())
        {
            daysOfLoan = 0;
            fee_textField.setText(String.valueOf(daysOfLoan));
        }
        else {
            fee_textField.setText(String.valueOf(Variables.fee(carID_textField.getText(), daysOfLoan)));
        }
    }

    private void submitButtonActionPerformed(ActionEvent e) {
        String order_id = orderID_textField.getText();
        String car_id = carID_textField.getText();
        String cust_id = customerID_textField.getText();
        int fee = Variables.textFieldIntegerValueCorrect(fee_textField.getText());

        if (!incorrectRentValueHandler(fee))
            return;

        if (areBlankTextFields())
            return;

        java.sql.Date returnDate = new java.sql.Date(returnDate_dateChooser.getDate().getTime());          // converts java date to sql date

        try
        {
            // ToDo zabezpieczenie przed pustymi polami
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            PreparedStatement preparedStatement;

            // in fact checking fee multiplies code unnecessarily
            if (fee == previousFee) {
                preparedStatement = con.prepareStatement("UPDATE tblRental " +
                        "SET state='returned', " +
                        "returnDate=? " +
                        "WHERE id LIKE ?");
                preparedStatement.setDate(1, returnDate);
                preparedStatement.setString(2, order_id);
            }

            else
            {
                preparedStatement = con.prepareStatement("UPDATE tblRental " +
                        "SET state='returned', " +
                        "fee = ?, " +
                        "returnDate=? " +
                        "WHERE id LIKE ?");
                preparedStatement.setInt(1, fee);
                preparedStatement.setDate(2, returnDate);
                preparedStatement.setString(3, order_id);
            }
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

    private void deleteRental_buttonActionPerformed(ActionEvent e) {
        String order_id = orderID_textField.getText();
        String car_id = carID_textField.getText();
        String cust_id = customerID_textField.getText();

        if (areBlankTextFields())
            return;

        java.sql.Date returnDate = new java.sql.Date(returnDate_dateChooser.getDate().getTime());          // converts java date to sql date

        try
        {
            // ToDo zabezpieczenie przed pustymi polami
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            PreparedStatement preparedStatement = con.prepareStatement("UPDATE tblRental " +
                    "SET state='cancelled', " +
                    "returnDate=? " +
                    "WHERE id LIKE ?");
            preparedStatement.setDate(1, returnDate);
            preparedStatement.setString(2, order_id);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order deleted successfully");

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }
    }

    private boolean incorrectRentValueHandler(int errorCode)
    {
        if (errorCode == -1)
        {
            JOptionPane.showMessageDialog(this, "You can't enter bigger rent value than 2147483647");
            return false;
        }
        else if (errorCode == -2)
        {
            JOptionPane.showMessageDialog(this, "Price Per Day field can't contain letters");
            return false;
        }

        else if (errorCode == -3)
        {
            JOptionPane.showMessageDialog(this, "Price Per Day can't be negative value");
            return false;
        }

        else if (errorCode == -4)
        {
            JOptionPane.showMessageDialog(this, "Fee can't be blank");
            return false;
        }

        return true;
    }

    private boolean areBlankTextFields()
    {
        if (carID_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Car ID field can't be blank");
            return true;
        }

        // Customer address can be blank
        else if (returnDate_dateChooser.getDate() == null)
        {
            JOptionPane.showMessageDialog(this, "Please select deployment date");
            return true;
        }

        else if (fee_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Fee field can't be blank");
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
        ordersButton = new JButton();
        label1 = new JLabel();
        orderID_textField = new JTextField();
        label4 = new JLabel();
        carID_textField = new JTextField();
        label3 = new JLabel();
        customerID_textField = new JTextField();
        label5 = new JLabel();
        returnDate_dateChooser = new JDateChooser();
        label6 = new JLabel();
        fee_textField = new JTextField();
        submitButton = new JButton();
        deleteRental_button = new JButton();
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

        //---- ordersButton ----
        ordersButton.setText("Search orders");
        ordersButton.addActionListener(e -> ordersButtonActionPerformed(e));
        contentPane.add(ordersButton, "cell 16 1 3 3");

        //---- label1 ----
        label1.setText("Order ID");
        contentPane.add(label1, "cell 3 2 3 1");

        //---- orderID_textField ----
        orderID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                orderID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(orderID_textField, "cell 9 2 6 1");

        //---- label4 ----
        label4.setText("Car ID");
        contentPane.add(label4, "cell 3 5 3 1");

        //---- carID_textField ----
        carID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                carID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(carID_textField, "cell 9 5 6 1");

        //---- label3 ----
        label3.setText("Customer ID");
        contentPane.add(label3, "cell 3 8 3 1");

        //---- customerID_textField ----
        customerID_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                customerID_textFieldKeyPressed(e);
            }
        });
        contentPane.add(customerID_textField, "cell 9 8 6 1");

        //---- label5 ----
        label5.setText("Return Date");
        contentPane.add(label5, "cell 3 11 3 1");

        //---- returnDate_dateChooser ----
        returnDate_dateChooser.addPropertyChangeListener(e -> returnDate_dateChooserPropertyChange(e));
        contentPane.add(returnDate_dateChooser, "cell 9 11 6 1");

        //---- label6 ----
        label6.setText("Fee");
        contentPane.add(label6, "cell 3 14 3 1");
        contentPane.add(fee_textField, "cell 9 14 6 1");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.setFont(submitButton.getFont().deriveFont(submitButton.getFont().getStyle() | Font.BOLD, submitButton.getFont().getSize() + 10f));
        submitButton.addActionListener(e -> submitButtonActionPerformed(e));
        contentPane.add(submitButton, "cell 2 17 6 3");

        //---- deleteRental_button ----
        deleteRental_button.setText("Delete rental");
        deleteRental_button.setFont(deleteRental_button.getFont().deriveFont(deleteRental_button.getFont().getStyle() | Font.BOLD, deleteRental_button.getFont().getSize() + 10f));
        deleteRental_button.addActionListener(e -> deleteRental_buttonActionPerformed(e));
        contentPane.add(deleteRental_button, "cell 9 17 6 3");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getStyle() | Font.BOLD, cancelButton.getFont().getSize() + 10f));
        cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
        contentPane.add(cancelButton, "cell 16 17 5 3");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JButton ordersButton;
    private JLabel label1;
    private JTextField orderID_textField;
    private JLabel label4;
    private JTextField carID_textField;
    private JLabel label3;
    private JTextField customerID_textField;
    private JLabel label5;
    private JDateChooser returnDate_dateChooser;
    private JLabel label6;
    private JTextField fee_textField;
    private JButton submitButton;
    private JButton deleteRental_button;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
