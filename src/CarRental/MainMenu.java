/*
 * Created by JFormDesigner on Mon May 31 08:31:15 CEST 2021
 */

package CarRental;

import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Revan
 */
public class MainMenu extends JPanel {
    public MainMenu() {
        initComponents();
    }

    private void CarRegistration_buttonActionPerformed(ActionEvent e) {
        CarRegistrationManual CRM = CarRegistrationManual.getInstance();
    }

    private void customerButtonActionPerformed(ActionEvent e) {
        CustomerRegistration CR = CustomerRegistration.getInstance();
    }

    private void carRental_buttonActionPerformed(ActionEvent e) {
        CarRental CaRl = CarRental.getInstance();
    }

    private void carReturn_buttonActionPerformed(ActionEvent e) {
        CarReturn CRe = CarReturn.getInstance();
    }

    private void carDeployment_buttonActionPerformed(ActionEvent e) {
        CarDeployment CDe = CarDeployment.getInstance();
    }

    private void logoutButtonActionPerformed(ActionEvent e) {
        Variables.Flush();
        // demonstracja działania funkcji Flush()
        System.out.println("Login: " + Variables.Login);
        System.out.println("Password: " + Variables.password);
        System.exit(0);

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Revan
        button1 = new JButton();
        customerButton = new JButton();
        carRental_button = new JButton();
        carDeployment_button = new JButton();
        carReturn_button = new JButton();
        logoutButton = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing.
        border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER
        , javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font
        .BOLD ,12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (
        new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("borde\u0072"
        .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
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
            "[]" +
            "[]" +
            "[]"));

        //---- button1 ----
        button1.setText("Car Registration");
        button1.addActionListener(e -> CarRegistration_buttonActionPerformed(e));
        add(button1, "cell 5 1 8 1");

        //---- customerButton ----
        customerButton.setText("Customer");
        customerButton.addActionListener(e -> customerButtonActionPerformed(e));
        add(customerButton, "cell 5 3 8 1");

        //---- carRental_button ----
        carRental_button.setText("Car Rental");
        carRental_button.addActionListener(e -> carRental_buttonActionPerformed(e));
        add(carRental_button, "cell 5 5 8 1");

        //---- carDeployment_button ----
        carDeployment_button.setText("Car Deployment");
        carDeployment_button.addActionListener(e -> carDeployment_buttonActionPerformed(e));
        add(carDeployment_button, "cell 5 7 8 1");

        //---- carReturn_button ----
        carReturn_button.setText("Car Return");
        carReturn_button.addActionListener(e -> carReturn_buttonActionPerformed(e));
        add(carReturn_button, "cell 5 9 8 1");

        //---- logoutButton ----
        logoutButton.setText("Logout");
        logoutButton.addActionListener(e -> logoutButtonActionPerformed(e));
        add(logoutButton, "cell 5 11 8 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Revan
    private JButton button1;
    private JButton customerButton;
    private JButton carRental_button;
    private JButton carDeployment_button;
    private JButton carReturn_button;
    private JButton logoutButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
