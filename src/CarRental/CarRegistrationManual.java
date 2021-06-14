/*
 * Created by JFormDesigner on Mon May 31 16:29:17 CEST 2021
 */

package CarRental;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import net.miginfocom.swing.*;
import java.sql.*;
import java.util.Vector;
//import java.lang.String;

/**
 * @author Revan
 */
public class CarRegistrationManual extends JFrame {

    // Variables

    private CarRegistrationManual()
    {
        initComponents();
        autoID();
        tableUpdate();
    }

    private CarRegistrationManual(String title)
    {
        super(title);           // window title
        initComponents();
        autoID();
        tableUpdate();
        cars_scrollPane.setEnabled(false);

        // https://stackoverflow.com/questions/4154780/jframe-catch-dispose-event   code by cbaldan
        /*this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // your code
                CRM = null;
            }
        });*/
    }

    // SINGLETON
    public static CarRegistrationManual getInstance()
    {
        if (CRM == null)
        {
            CRM = new CarRegistrationManual("Car Registration");
            CRM.pack();
            CRM.setResizable(false);
            CRM.setVisible(true);
            CRM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //CRM.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Set icon image
            ImageIcon icon = new ImageIcon("resources/icon.png");
            CRM.setIconImage(icon.getImage());

        }

        return CRM;
    }

    // to nie ma sensu
    public static CarRegistrationManual getInstance(String title)
    {
        if (CRM == null)
        {
            CRM = new CarRegistrationManual(title);
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
    private static CarRegistrationManual CRM;

    public void autoID ()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));   // link do bazy danych, jest 192.168.1.26


            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT MAX(car_no)" +
                                                "FROM tblCarBasic");
            rs.next();
            rs.getString("MAX(car_no)");
            if (rs.getString("MAX(car_no)") == null)
            {
                carID_textField.setText("C0001");
            }
            else
            {
                String temp = rs.getString("MAX(car_no)");
                try
                {
                    long id = Long.parseLong(rs.getString("MAX(car_no)").substring(2, temp.length()));
                    id++;
                    carID_textField.setText("C0" + String.format("%03d", id));
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
            JOptionPane.showMessageDialog(this, "Class exception");
            Variables.logger.error(e.getMessage());
        }
        catch (SQLException throwables)
        {
            String error = throwables.toString();
            if (error.indexOf("Duplicate entry") != -1)
                JOptionPane.showMessageDialog(this, "This Car ID already exists");
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
                                                                "FROM tblCarBasic");
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
                    vector.add(resultSet.getString("car_no"));
                    vector.add(resultSet.getString("make"));
                    vector.add(resultSet.getString("model"));
                    vector.add(resultSet.getString("available"));
                    vector.add(resultSet.getString("rentPricePerDay"));
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

    private void AddCarManual_buttonActionPerformed(ActionEvent e) {

        String ID           = carID_textField.getText();
        String carBrand     = carBrand_textField.getText();
        String carModel     = carModel_textField.getText();
        int pricePD         = Variables.textFieldIntegerValueCorrect(pricepd_textField.getText());

        if (!incorrectRentValueHandler(pricePD))
            return;

        if (areBlankTextFields())
            return;
        String carAvailable = carAvailable_comboBox.getSelectedItem().toString();

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));
            PreparedStatement statement = con.prepareStatement("INSERT INTO tblCarBasic(car_no, make, model, available, rentPricePerDay) VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, ID);
            statement.setString(2, carBrand);
            statement.setString(3, carModel);
            statement.setString(4, carAvailable);
            statement.setInt   (5, pricePD);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Car added successfully");

            // emptying text fields
            carID_textField.setText("");
            carBrand_textField.setText("");
            carModel_textField.setText("");
            carAvailable_comboBox.setSelectedIndex(-1);
            carAvailable_comboBox.requestFocus();
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

    private void RentTableMouseClicked(MouseEvent e) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) RentTable.getModel();

        int index = RentTable.getSelectedRow();

        carID_textField.setText(defaultTableModel.getValueAt(index, 0).toString());
        carBrand_textField.setText(defaultTableModel.getValueAt(index, 1).toString());
        carModel_textField.setText(defaultTableModel.getValueAt(index, 2).toString());
        carAvailable_comboBox.setSelectedItem(defaultTableModel.getValueAt(index, 3).toString());
        pricepd_textField.setText(defaultTableModel.getValueAt(index, 4).toString());
    }

    private void editButtonActionPerformed(ActionEvent e) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) RentTable.getModel();

        int index = RentTable.getSelectedRow();
        try
        {
            String ID;
            String carBrand         = carBrand_textField.getText();
            String carModel         = carModel_textField.getText();
            int pricePD         = Variables.textFieldIntegerValueCorrect(pricepd_textField.getText());

            if (!incorrectRentValueHandler(pricePD))
                return;

            if (areBlankTextFields())
                return;
            String carAvailable     = carAvailable_comboBox.getSelectedItem().toString();

            // Prevents error from not choosing car from the table
            try
            {
                ID = defaultTableModel.getValueAt(index, 0).toString();
            }
            catch (Exception ex)
            {
                ID = carID_textField.getText();
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Variables.Login, String.valueOf(Variables.password));

            PreparedStatement preparedStatement = con.prepareStatement("UPDATE tblCarBasic " +
                                                                        "SET make=?, model=?, available=?, rentPricePerDay=? " +
                                                                        "WHERE car_no LIKE ?");
            preparedStatement.setString(1, carBrand);
            preparedStatement.setString(2, carModel);
            preparedStatement.setString(3, carAvailable);
            preparedStatement.setInt   (4, pricePD);
            preparedStatement.setString(5, ID);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car updated");
            tableUpdate();

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this, "Database unreachable. Check your internet connection");
            Variables.logger.error(throwables.getMessage());
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(classNotFoundException.getMessage());
        } catch (ArrayIndexOutOfBoundsException a)
        {
            JOptionPane.showMessageDialog(this, "Unknown error");
            Variables.logger.error(a.getMessage());
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
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM tblCarBasic " +
                                                                        "WHERE car_no LIKE ?");
            preparedStatement.setString(1, ID);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car deleted");
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
            JOptionPane.showMessageDialog(this, "Price Per Day can't be blank");
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
        else if (carModel_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Car Model field can't be blank");
            return true;
        }
        else if (carBrand_textField.getText().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Car Brand field can't be blank");
            return true;
        }
        else if (carAvailable_comboBox.getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(this, "Car Model field can't be blank");
            return true;
        }
        else if (pricepd_textField.getText().equals(""))            // it is present in errorHandler, but added just to ensure safety
        {
            JOptionPane.showMessageDialog(this, "Price per day field can't be blank");
            return true;
        }
        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - KK
        panel1 = new JPanel();
        label2 = new JLabel();
        carID_textField = new JTextField();
        label3 = new JLabel();
        carBrand_textField = new JTextField();
        label1 = new JLabel();
        carModel_textField = new JTextField();
        label4 = new JLabel();
        carAvailable_comboBox = new JComboBox<>();
        label5 = new JLabel();
        pricepd_textField = new JTextField();
        AddCarManual_button = new JButton();
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
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== panel1 ========
        {
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
            ( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder. CENTER, javax. swing. border
            . TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
            . Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
            propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( )
            ; }} );
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
                "[]" +
                "[]" +
                "[]"));

            //---- label2 ----
            label2.setText("Car ID");
            panel1.add(label2, "cell 2 3 5 2");
            panel1.add(carID_textField, "cell 10 3 16 2");

            //---- label3 ----
            label3.setText("Car Brand");
            panel1.add(label3, "cell 2 6 5 3");
            panel1.add(carBrand_textField, "cell 10 6 16 2");

            //---- label1 ----
            label1.setText("Car Model");
            panel1.add(label1, "cell 2 10 5 3");
            panel1.add(carModel_textField, "cell 10 10 16 2");

            //---- label4 ----
            label4.setText("Available");
            panel1.add(label4, "cell 2 14 5 2");

            //---- carAvailable_comboBox ----
            carAvailable_comboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Yes",
                "No"
            }));
            panel1.add(carAvailable_comboBox, "cell 10 14 8 2");

            //---- label5 ----
            label5.setText("Price per Day");
            panel1.add(label5, "cell 2 18 5 4");
            panel1.add(pricepd_textField, "cell 10 18 16 3");

            //---- AddCarManual_button ----
            AddCarManual_button.setText("Add");
            AddCarManual_button.addActionListener(e -> AddCarManual_buttonActionPerformed(e));
            panel1.add(AddCarManual_button, "cell 2 25 5 2");

            //---- editButton ----
            editButton.setText("Edit");
            editButton.addActionListener(e -> editButtonActionPerformed(e));
            panel1.add(editButton, "cell 10 25 8 2");

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.addActionListener(e -> deleteButtonActionPerformed(e));
            panel1.add(deleteButton, "cell 2 28 5 2");

            //---- cancelButton ----
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
            panel1.add(cancelButton, "cell 10 28 8 2");
        }
        contentPane.add(panel1, "cell 2 4 8 26");

        //======== cars_scrollPane ========
        {
            cars_scrollPane.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cars_scrollPaneMouseClicked(e);
                }
            });

            //---- RentTable ----
            RentTable.setModel(new DefaultTableModel(
                new Object[][] {
                    {null, null, null, null, null},
                },
                new String[] {
                    "Car ID", "Car Brand", "Car Model", "Available", "Price per day"
                }
            ));
            RentTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    RentTableMouseClicked(e);
                }
            });
            cars_scrollPane.setViewportView(RentTable);
        }
        contentPane.add(cars_scrollPane, "cell 14 0 26 33");
        setSize(1295, 720);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - KK
    private JPanel panel1;
    private JLabel label2;
    private JTextField carID_textField;
    private JLabel label3;
    private JTextField carBrand_textField;
    private JLabel label1;
    private JTextField carModel_textField;
    private JLabel label4;
    private JComboBox<String> carAvailable_comboBox;
    private JLabel label5;
    private JTextField pricepd_textField;
    private JButton AddCarManual_button;
    private JButton editButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JScrollPane cars_scrollPane;
    private JTable RentTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
