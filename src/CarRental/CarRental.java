/*
 * Created by JFormDesigner on Sat Jun 05 11:57:45 CEST 2021
 */

package CarRental;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import com.toedter.calendar.*;
import net.miginfocom.swing.*;

/**
 * @author Revan
 */

public class CarRental extends JFrame {
    private static CarRental CaRl;
    //private ResultSet resultSetCarAvailability;

    public CarRental() {
        initComponents();
        LoadCarsTo_carIDComboBox();

        /*date_JDateChooser.setDateFormatString("yyyy/mm/dd");
        returnDate_JDateChooser.setDateFormatString("yyyy/mm/dd");*/
    }

    public CarRental(String title) {
        super(title);
        initComponents();
        LoadCarsTo_carIDComboBox();

        /*date_JDateChooser.setDateFormatString("yyyy/mm/dd");
        returnDate_JDateChooser.setDateFormatString("yyyy/mm/dd");*/
    }

    public void LoadCarsTo_carIDComboBox()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                                                                            "FROM tblCarBasic");
            ResultSet resultSet = preparedStatement.executeQuery();
            carID_comboBox.removeAllItems();

            while (resultSet.next()) {
                String carID = resultSet.getString(1);
                String carBrand = resultSet.getString(2);
                String carModel = resultSet.getString(3);
                //carID_comboBox.addItem(carID + " " + carBrand + " " + carModel);
                String isAvailable = resultSet.getString("available");


                if (isAvailable.equals("Yes"))
                {
                    carID_comboBox.addItem("<html><font color='green'>" + carID + " " + carBrand + " " + carModel + "</font></html>");
                }
                else
                {
                    carID_comboBox.addItem("<html><font color='red'>" + carID + " " + carBrand + " " + carModel + "</font></html>");
                }
            }

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(e.getMessage());
        }
    }

    public static CarRental getInstance()
    {
        if (CaRl == null)
        {
            CaRl = new CarRental("Car Rental");
            CaRl.pack();
            CaRl.setResizable(false);
            CaRl.setVisible(true);
            CaRl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CaRl.setIconImage(icon.getImage());
        }

        return CaRl;
    }

    private void carID_comboBoxActionPerformed(ActionEvent e) {

        // Handles error when removing items from ComboBox - program reads it as event and throws exception
        if (carID_comboBox.getSelectedItem() == null)
            return;

        String carID = "C" + carID_comboBox.getSelectedItem().toString().split("C")[1].substring(0, 4);

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                                                "FROM tblCarBasic " +
                                                "WHERE car_no LIKE ?");
            preparedStatement.setString(1, carID);
            ResultSet resultSet = preparedStatement.executeQuery();

            preparedStatement = con.prepareStatement("SELECT * " +
                    "FROM tblRental " +
                    "WHERE car_id LIKE ? " +
                    "AND state IN ('active', 'rented')");
            preparedStatement.setString(1, carID);
            ResultSet resultSet2 = preparedStatement.executeQuery();

            if (resultSet.next() == false)
            {
                JOptionPane.showMessageDialog(this, "Car not found");
            }
            else
            {
                String isAvailable = resultSet.getString("available");

                //if (isAvailable.equals("Yes"))
                if (isAvailableInGivenPeriod(resultSet2, date_JDateChooser.getDate(), returnDate_JDateChooser.getDate()))
                {
                    //isAvailable_label.setForeground(Color.green);
                    // 25:00
                    customerID_JTextField.setEnabled(true);
                    Fee_textField.setEnabled(true);
                    //date_JDateChooser.setEnabled(true);
                    //returnDate_JDateChooser.setEnabled(true);
                    rentButton.setEnabled(true);
                }
                else
                {
                    //isAvailable_label.setForeground(Color.red);
                    customerID_JTextField.setEnabled(false);
                    Fee_textField.setEnabled(false);
                    //date_JDateChooser.setEnabled(false);
                    //returnDate_JDateChooser.setEnabled(false);
                    rentButton.setEnabled(false);
                }
            }
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        }
    }

    private void customerID_JTextFieldKeyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            System.out.println("EVENT!");
            String cust_id = customerID_JTextField.getText();
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                                                                            "FROM CUSTOMER " +
                                                                            "WHERE cust_id LIKE ?");
                preparedStatement.setString(1, cust_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Customer not found");

                else
                    JOptionPane.showMessageDialog(this, "OK");

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());
            }
        }
    }

    private void customerID_JTextFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            System.out.println("EVENT!");
            String cust_id = customerID_JTextField.getText();
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                                                                            "FROM tblCustomerBasic " +
                                                                            "WHERE cust_id LIKE ?");
                preparedStatement.setString(1, cust_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next() == false)
                    // Nie zrobić tego na lampce?
                    JOptionPane.showMessageDialog(this, "Customer not found");

                else
                    JOptionPane.showMessageDialog(this, "OK");

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
                Variables.logger.error(throwables.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                JOptionPane.showMessageDialog(this, "Unknown error");
                Variables.logger.error(classNotFoundException.getMessage());
            }
        }

    }

    private void rentButtonActionPerformed(ActionEvent e) {

        int fee         = Variables.textFieldIntegerValueCorrect(Fee_textField.getText());

        if (!incorrectRentValueHandler(fee))
            return;

        if (areBlankTextFields())
            return;

        String car_id = "C" + carID_comboBox.getSelectedItem().toString().split("C")[1].substring(0, 4);
        String cust_id = customerID_JTextField.getText();
        java.sql.Date rentDate = new java.sql.Date(date_JDateChooser.getDate().getTime());          // converts java date to sql date
        java.sql.Date returnDate = new java.sql.Date(returnDate_JDateChooser.getDate().getTime());

        try
        {
            // ToDo zabezpieczenie przed pustymi polami
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO tblRental (car_id, cust_id, fee, date, returnDate, state) VALUES " +
                                                                        "(?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, car_id);
            preparedStatement.setString(2, cust_id);
            preparedStatement.setInt(3, fee);
            preparedStatement.setString(4, rentDate.toString());
            preparedStatement.setString(5, returnDate.toString());
            preparedStatement.setString(6, "rented");

            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();

            // ToDo sprawdzanie, czy samochód dalej jest dostępny
            preparedStatement = con.prepareStatement("UPDATE tblCarBasic " +
                                                        "SET available='No' " +
                                                        "WHERE car_no LIKE ?");
            preparedStatement.setString(1, car_id);
            preparedStatement.executeUpdate();

            LoadCarsTo_carIDComboBox();

            JOptionPane.showMessageDialog(this, "Car rented successfully");

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
        CaRl = null;
    }

    private void returnDate_JDateChooserPropertyChange(PropertyChangeEvent e) {
        if (carID_comboBox.getSelectedItem() == null || returnDate_JDateChooser.getDate() == null)
            return;

        carAvailabilityActivation();
        String car_id = "C" + carID_comboBox.getSelectedItem().toString().split("C")[1].substring(0, 4);
        if (date_JDateChooser.getDate() != null && rentButton.isEnabled())
            Fee_textField.setText(String.valueOf(Variables.fee(car_id, Variables.getDifferenceDays(date_JDateChooser.getDate(), returnDate_JDateChooser.getDate()))));
    }

    private void date_JDateChooserPropertyChange(PropertyChangeEvent e) {
        if (carID_comboBox.getSelectedItem() == null || date_JDateChooser.getDate() == null)
            return;

        carAvailabilityActivation();
        String car_id = "C" + carID_comboBox.getSelectedItem().toString().split("C")[1].substring(0, 4);
        if (returnDate_JDateChooser.getDate() != null && rentButton.isEnabled())
            Fee_textField.setText(String.valueOf(Variables.fee(car_id, Variables.getDifferenceDays(date_JDateChooser.getDate(), returnDate_JDateChooser.getDate()))));
    }

    private String carID_Getter() {
        return "C" + carID_comboBox.getSelectedItem().toString().split("C")[1].substring(0, 4);
    }

    private boolean isAvailableInGivenPeriod(ResultSet resultSet, Date desiredStartDate, Date desiredEndDate)
    {
        try
        {
            while(resultSet.next())
            {
                try
                {
                    System.out.println("1");
                    Date orderStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(resultSet.getDate("date")));
                    Date orderEndDate   = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(resultSet.getDate("returnDate")));
                    if (!Variables.isNotOverlapping(desiredStartDate, desiredEndDate, orderStartDate, orderEndDate))
                        return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            System.out.println("Out");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private void carAvailabilityActivation()
    {
        ResultSet resultSetCarAvailability = obtainCarOrders(carID_Getter());     // done this way not to modify resultSetCarAvailability
        System.out.println(carID_Getter());
        if (isAvailableInGivenPeriod(resultSetCarAvailability, date_JDateChooser.getDate(), returnDate_JDateChooser.getDate()))
        {
            //isAvailable_label.setForeground(Color.green);
            // 25:00
            customerID_JTextField.setEnabled(true);
            Fee_textField.setEnabled(true);
            //date_JDateChooser.setEnabled(true);
            //returnDate_JDateChooser.setEnabled(true);
            rentButton.setEnabled(true);
        }
        else
        {
            //isAvailable_label.setForeground(Color.red);
            customerID_JTextField.setEnabled(false);
            Fee_textField.setEnabled(false);
            //date_JDateChooser.setEnabled(false);
            //returnDate_JDateChooser.setEnabled(false);
            rentButton.setEnabled(false);
        }
    }

    private ResultSet obtainCarOrders(String carID)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * " +
                    "FROM tblRental " +
                    "WHERE car_id LIKE ? " +
                    "AND state IN ('active', 'rented')");
            preparedStatement.setString(1, carID);
            System.out.println(preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
        if (carID_comboBox.getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(this, "Please select car");
            return true;
        }
        else if (customerID_JTextField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer ID field can't be blank");
            return true;
        }

        // Customer address can be blank
        else if (date_JDateChooser.getDate() == null)
        {
            JOptionPane.showMessageDialog(this, "Please select rent date");
            return true;
        }

        else if (returnDate_JDateChooser.getDate() == null)
        {
            JOptionPane.showMessageDialog(this, "Please select return date");
            return true;
        }

        else if (Fee_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Fee field can't be blank");
            return true;
        }

        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - KK
        label1 = new JLabel();
        carID_comboBox = new JComboBox();
        label2 = new JLabel();
        customerID_JTextField = new JTextField();
        label3 = new JLabel();
        date_JDateChooser = new JDateChooser();
        label4 = new JLabel();
        returnDate_JDateChooser = new JDateChooser();
        label5 = new JLabel();
        Fee_textField = new JTextField();
        rentButton = new JButton();
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
            "[123,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[36]" +
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

        //---- label1 ----
        label1.setText("Car ID");
        contentPane.add(label1, "cell 6 3");

        //---- carID_comboBox ----
        carID_comboBox.addActionListener(e -> carID_comboBoxActionPerformed(e));
        contentPane.add(carID_comboBox, "cell 11 3 4 1");

        //---- label2 ----
        label2.setText("Customer ID");
        contentPane.add(label2, "cell 6 6");

        //---- customerID_JTextField ----
        customerID_JTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                customerID_JTextFieldKeyPressed(e);
            }
        });
        contentPane.add(customerID_JTextField, "cell 11 6 4 1");

        //---- label3 ----
        label3.setText("Rent Date");
        contentPane.add(label3, "cell 6 9");

        //---- date_JDateChooser ----
        date_JDateChooser.addPropertyChangeListener(e -> date_JDateChooserPropertyChange(e));
        contentPane.add(date_JDateChooser, "cell 11 9 4 1");

        //---- label4 ----
        label4.setText("Return Date");
        contentPane.add(label4, "cell 6 12");

        //---- returnDate_JDateChooser ----
        returnDate_JDateChooser.addPropertyChangeListener(e -> returnDate_JDateChooserPropertyChange(e));
        contentPane.add(returnDate_JDateChooser, "cell 11 12 4 1");

        //---- label5 ----
        label5.setText("Rental Fee");
        contentPane.add(label5, "cell 6 15");
        contentPane.add(Fee_textField, "cell 11 15 4 1");

        //---- rentButton ----
        rentButton.setText("Rent");
        rentButton.setFont(rentButton.getFont().deriveFont(rentButton.getFont().getStyle() | Font.BOLD, rentButton.getFont().getSize() + 10f));
        rentButton.addActionListener(e -> rentButtonActionPerformed(e));
        contentPane.add(rentButton, "cell 4 18 8 3");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getStyle() | Font.BOLD, cancelButton.getFont().getSize() + 10f));
        cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
        contentPane.add(cancelButton, "cell 13 18 3 3");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JLabel label1;
    private JComboBox carID_comboBox;
    private JLabel label2;
    private JTextField customerID_JTextField;
    private JLabel label3;
    private JDateChooser date_JDateChooser;
    private JLabel label4;
    private JDateChooser returnDate_JDateChooser;
    private JLabel label5;
    private JTextField Fee_textField;
    private JButton rentButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
