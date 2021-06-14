/*
 * Created by JFormDesigner on Mon May 31 16:29:17 CEST 2021
 */

package CarRental;

import javax.swing.table.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
//import java.lang.String;

/**
 * @author Revan
 */
public class CustomerRegistration extends JFrame {

    // Variables

    private CustomerRegistration()
    {
        initComponents();
        autoID();
        tableUpdate();
    }

    private CustomerRegistration(String title)
    {
        super(title);           // window title
        initComponents();
        autoID();
        tableUpdate();
        cars_scrollPane.setEnabled(false);      // można ustawić w JFormDesigner
    }

    // SINGLETON
    public static CustomerRegistration getInstance()
    {
        if (CRM == null)
        {
            CRM = new CustomerRegistration("Customer Registration");
            CRM.pack();
            CRM.setResizable(false);
            CRM.setVisible(true);
            CRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CRM.setIconImage(icon.getImage());
        }

        return CRM;
    }

    // to nie ma sensu
    public static CustomerRegistration getInstance(String title)
    {
        if (CRM == null)
        {
            CRM = new CustomerRegistration(title);
            CRM.pack();
            CRM.setResizable(false);
            CRM.setVisible(true);
            CRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CRM.setIconImage(icon.getImage());
        }

        return CRM;
    }

    private Connection con;
    private static CustomerRegistration CRM;

    public void autoID ()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT MAX(cust_id)" +
                                                "FROM tblCustomerBasic");
            rs.next();
            rs.getString("MAX(cust_id)");
            System.out.println("dziaua");
            if (rs.getString("MAX(cust_id)") == null)
            {
                customerID_textField.setText("C0001");
            }
            else
            {
                String temp = rs.getString("MAX(cust_id)");  // just because intellij is stupid
                try
                {
                    long id = Long.parseLong(rs.getString("MAX(cust_id)").substring(2, temp.length()));
                    id++;
                    customerID_textField.setText("C0" + String.format("%03d", id));
                }
                catch (Exception e)
                {
                    Variables.logger.error(e.toString());
                }

            }
            tableUpdate();

        }
        catch (ClassNotFoundException e)
        {
            // Logger
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(e.getMessage());
        }
        catch (SQLException throwables)
        {
            if (throwables.toString().contains("Duplicate entry"))
                JOptionPane.showMessageDialog(this, "This Customer ID already exists");
            else
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        }
    }

    public void tableUpdate()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26

            PreparedStatement statement = con.prepareStatement("SELECT *" +
                                                                "FROM tblCustomerBasic");
            ResultSet           resultSet           = statement.executeQuery();
            ResultSetMetaData   resultSetMetaData   = resultSet.getMetaData();
            int                 columnCount         = resultSetMetaData.getColumnCount();
            DefaultTableModel   defaultTableModel   = (DefaultTableModel)RentTable.getModel();  // ToDo has to be changed to carsTable, because intellij is dumb
            defaultTableModel.setRowCount(0);

            while(resultSet.next())
            {
                Vector vector = new Vector();

                // Building row
                for (int i = 0; i <= columnCount; i++)
                {
                    vector.add(resultSet.getString("cust_id"));
                    vector.add(resultSet.getString("name"));
                    vector.add(resultSet.getString("address"));
                    vector.add(resultSet.getString("phoneNum"));
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

    private void AddCustomer_buttonActionPerformed(ActionEvent e) {

        String custID       = customerID_textField.getText();
        String custName     = customerName_textField.getText();
        String custAddress  = customerAddress_textField.getText();
        String custPhoneNum = customerPhoneNum_textField.getText();

        if (areBlankTextFields())
            return;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));
            PreparedStatement statement = con.prepareStatement("INSERT INTO tblCustomerBasic(cust_id, name, address, phoneNum) VALUES (?, ?, ?, ?)");
            statement.setString(1, custID);
            statement.setString(2, custName);
            statement.setString(3, custAddress);
            statement.setString(4, custPhoneNum);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer added successfully");

            // emptying text fields
            customerID_textField.setText("");
            customerName_textField.setText("");
            customerAddress_textField.setText("");
            customerPhoneNum_textField.setText("");
            autoID();

        } catch (SQLException throwables) {
            if (throwables.toString().contains("Duplicate entry"))
                JOptionPane.showMessageDialog(this, "This Customer ID already exists");
            else
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }
    }

    private void cars_scrollPaneMouseClicked(MouseEvent e) {
        System.out.println("nothing, just clicked scrollPane");
    }

    private void CustomerTableMouseClicked(MouseEvent e) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) RentTable.getModel();

        int index = RentTable.getSelectedRow();

        customerID_textField.setText(defaultTableModel.getValueAt(index, 0).toString());
        customerName_textField.setText(defaultTableModel.getValueAt(index, 1).toString());
        customerAddress_textField.setText(defaultTableModel.getValueAt(index, 2).toString());
        customerPhoneNum_textField.setText(defaultTableModel.getValueAt(index, 3).toString());
    }

    private void editButtonActionPerformed(ActionEvent e) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) RentTable.getModel();

        int index = RentTable.getSelectedRow();
        try
        {
            String custID;
            String custName         = customerName_textField.getText();
            String custAddress      = customerAddress_textField.getText();
            String custPhoneN       = customerPhoneNum_textField.getText();

            if (areBlankTextFields())
                return;

            // Prevents error from not choosing customer from the table
            try
            {
                custID = defaultTableModel.getValueAt(index, 0).toString();
            }
            catch (Exception ex)
            {
                custID = customerID_textField.getText();
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));

            PreparedStatement preparedStatement = con.prepareStatement("UPDATE tblCustomerBasic " +
                                                                        "SET name=?, address=?, phoneNum=? " +
                                                                        "WHERE cust_id LIKE ?");
            preparedStatement.setString(1, custName);
            preparedStatement.setString(2, custAddress);
            preparedStatement.setString(3, custPhoneN);
            preparedStatement.setString(4, custID);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer updated");
            tableUpdate();

        } catch (SQLException throwables) {
            if (throwables.toString().contains("Duplicate entry"))
                JOptionPane.showMessageDialog(this, "This Customer ID already exists");
            else
                JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        }

    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) RentTable.getModel();
        int index = RentTable.getSelectedRow();
        try
        {
            String ID = defaultTableModel.getValueAt(index, 0).toString();
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM tblCustomerBasic " +
                                                                        "WHERE cust_id LIKE ?");
            preparedStatement.setString(1, ID);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer deleted");
            tableUpdate();
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
        CRM = null;
    }

    private boolean areBlankTextFields()
    {
        if (customerID_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer ID field can't be blank");
            return true;
        }
        else if (customerName_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer name field can't be blank");
            return true;
        }
        /*
        // Customer address can be blank
        else if (customerAddress_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer address field can't be blank");
            return true;
        }
         */
        else if (customerPhoneNum_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Customer phone number field can't be blank");
            return true;
        }

        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - KK
        panel1 = new JPanel();
        label2 = new JLabel();
        customerID_textField = new JTextField();
        label3 = new JLabel();
        customerName_textField = new JTextField();
        label1 = new JLabel();
        customerAddress_textField = new JTextField();
        label4 = new JLabel();
        customerPhoneNum_textField = new JTextField();
        AddCustomer_button = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        cancelButton = new JButton();
        cars_scrollPane = new JScrollPane();
        RentTable = new JTable();

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
            "[18,fill]" +
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

        //======== panel1 ========
        {
            panel1.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax .
            swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border
            . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog"
            , java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) ,panel1. getBorder
            () ) ); panel1. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java
            . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException
            ( ) ;} } );
            panel1.setLayout(new MigLayout(
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
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- label2 ----
            label2.setText("Customer ID");
            panel1.add(label2, "cell 2 3 5 2");
            panel1.add(customerID_textField, "cell 10 3 15 2");

            //---- label3 ----
            label3.setText("Name");
            panel1.add(label3, "cell 2 6 5 3");
            panel1.add(customerName_textField, "cell 10 6 15 2");

            //---- label1 ----
            label1.setText("Address");
            panel1.add(label1, "cell 2 10 5 3");
            panel1.add(customerAddress_textField, "cell 10 10 15 2");

            //---- label4 ----
            label4.setText("Phone number");
            panel1.add(label4, "cell 2 13 5 3");
            panel1.add(customerPhoneNum_textField, "cell 10 13 15 3");

            //---- AddCustomer_button ----
            AddCustomer_button.setText("Add");
            AddCustomer_button.addActionListener(e -> AddCustomer_buttonActionPerformed(e));
            panel1.add(AddCustomer_button, "cell 2 18 6 2");

            //---- editButton ----
            editButton.setText("Edit");
            editButton.addActionListener(e -> editButtonActionPerformed(e));
            panel1.add(editButton, "cell 11 18 7 2");

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.addActionListener(e -> deleteButtonActionPerformed(e));
            panel1.add(deleteButton, "cell 2 21 6 2");

            //---- cancelButton ----
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
            panel1.add(cancelButton, "cell 11 21 7 2");
        }
        contentPane.add(panel1, "cell 2 4 23 19");

        //======== cars_scrollPane ========
        {

            //---- RentTable ----
            RentTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "Customer ID", "Name", "Address", "Phone number"
                }
            ) {
                Class<?>[] columnTypes = new Class<?>[] {
                    String.class, Object.class, String.class, String.class
                };
                boolean[] columnEditable = new boolean[] {
                    false, false, false, true
                };
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = RentTable.getColumnModel();
                cm.getColumn(0).setResizable(false);
                cm.getColumn(1).setResizable(false);
                cm.getColumn(2).setResizable(false);
            }
            RentTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    CustomerTableMouseClicked(e);
                }
            });
            cars_scrollPane.setViewportView(RentTable);
        }
        contentPane.add(cars_scrollPane, "cell 27 0 26 30");
        setSize(1260, 720);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JPanel panel1;
    private JLabel label2;
    private JTextField customerID_textField;
    private JLabel label3;
    private JTextField customerName_textField;
    private JLabel label1;
    private JTextField customerAddress_textField;
    private JLabel label4;
    private JTextField customerPhoneNum_textField;
    private JButton AddCustomer_button;
    private JButton editButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JScrollPane cars_scrollPane;
    private JTable RentTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
