/*
 * Created by JFormDesigner on Wed Jun 09 16:26:40 CEST 2021
 */

package CarRental;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import net.miginfocom.swing.*;

/**
 * @author Revan
 */
public class searchReturnTable extends JFrame {

    private static searchReturnTable sRT;
    public static boolean done;

    private searchReturnTable() {
        initComponents();
    }
    private searchReturnTable(String title) {
        super(title);
        initComponents();
        tableUpdate();
    }

    public static searchReturnTable getInstance()
    {
        if (sRT == null)
        {
            sRT = new searchReturnTable("Order Table");
            sRT.pack();
            sRT.setResizable(false);
            sRT.setVisible(true);
            sRT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            sRT.setIconImage(icon.getImage());

        }

        return sRT;
    }

    private void submitButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }
    // ToDo wypełnić combo box state

    public void tableUpdate()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            PreparedStatement statement = con.prepareStatement("SELECT *" +
                    "FROM tblRental");
            ResultSet resultSet           = statement.executeQuery();
            ResultSetMetaData resultSetMetaData   = resultSet.getMetaData();
            int                 columnCount         = resultSetMetaData.getColumnCount();
            DefaultTableModel   defaultTableModel   = (DefaultTableModel)orderTable.getModel();
            defaultTableModel.setRowCount(0);

            while(resultSet.next())
            {
                Vector vector = new Vector();

                // Building row
                for (int i = 0; i <= columnCount; i++)
                {
                    vector.add(resultSet.getString("id"));
                    vector.add(resultSet.getString("car_id"));
                    vector.add(resultSet.getString("cust_id"));
                    vector.add(resultSet.getString("fee"));
                    vector.add(resultSet.getString("state"));
                    vector.add(resultSet.getString("date"));
                    vector.add(resultSet.getString("returnDate"));
                }
                defaultTableModel.addRow(vector);
            }
        }

        catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        }
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(e.getMessage());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - KK
        orderID_label = new JLabel();
        scrollPane1 = new JScrollPane();
        orderTable = new JTable();
        orderID_textField = new JTextField();
        carID_label = new JLabel();
        carID_textField = new JTextField();
        customerID_label = new JLabel();
        customerID_textField = new JTextField();
        label4 = new JLabel();
        state_comboBox = new JComboBox();
        button2 = new JButton();
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

        //---- orderID_label ----
        orderID_label.setText("Order ID");
        contentPane.add(orderID_label, "cell 1 2 3 1");

        //======== scrollPane1 ========
        {

            //---- orderTable ----
            orderTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "Order ID", "Car ID", "Customer ID", "Fee", "State", "Rental date", "Return date"
                }
            ));
            scrollPane1.setViewportView(orderTable);
        }
        contentPane.add(scrollPane1, "cell 15 0 20 18");
        contentPane.add(orderID_textField, "cell 5 1 8 3");

        //---- carID_label ----
        carID_label.setText("Car ID");
        contentPane.add(carID_label, "cell 1 5 3 1");
        contentPane.add(carID_textField, "cell 5 4 8 3");

        //---- customerID_label ----
        customerID_label.setText("Customer ID");
        contentPane.add(customerID_label, "cell 1 8 3 1");
        contentPane.add(customerID_textField, "cell 5 7 8 3");

        //---- label4 ----
        label4.setText("State");
        contentPane.add(label4, "cell 1 11 3 1");
        contentPane.add(state_comboBox, "cell 5 11 8 1");

        //---- button2 ----
        button2.setText("Search");
        contentPane.add(button2, "cell 2 16 6 1");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.setFont(submitButton.getFont().deriveFont(submitButton.getFont().getStyle() | Font.BOLD, submitButton.getFont().getSize() + 5f));
        submitButton.addActionListener(e -> submitButtonActionPerformed(e));
        contentPane.add(submitButton, "cell 3 18 8 3");

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getStyle() | Font.BOLD, cancelButton.getFont().getSize() + 5f));
        cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
        contentPane.add(cancelButton, "cell 14 18 9 3");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JLabel orderID_label;
    private JScrollPane scrollPane1;
    private JTable orderTable;
    public JTextField orderID_textField;
    private JLabel carID_label;
    public JTextField carID_textField;
    private JLabel customerID_label;
    public JTextField customerID_textField;
    private JLabel label4;
    public JComboBox state_comboBox;
    private JButton button2;
    private JButton submitButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
