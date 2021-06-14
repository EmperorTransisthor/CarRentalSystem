package CarRental;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import javax.swing.*;

public class Variables {



    // CarRental variables

    public static Logger logger = Logger.getLogger("Car Rental");

    // Login
    public static String Login;
    public static char[] password;

    public static void Flush()
    {
        Login = "";
        password = null;
    }

    public static int fee(String carID, int daysOfLoan)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.26/carRental", Login, String.valueOf(password));
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM tblCarBasic " +
                    "WHERE car_no LIKE ?");
            preparedStatement.setString(1, carID);
            ResultSet resultset = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            if (resultset.next() == false)
                return -3;
            else
                return resultset.getInt("rentPricePerDay") * daysOfLoan;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Variables.logger.error(throwables.getMessage());
            return -1;
        } catch (ClassNotFoundException e) {
            Variables.logger.error(e.getMessage());
            return -2;
        }
    }

    public static int getDifferenceDays(String date1, String date2) {

        try {
            Date rentDate, returnDate;
            rentDate = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
            long diffInMillies = Math.abs(rentDate.getTime() - returnDate.getTime());

            // If returnDate is earlier than rentDate, return error
            if (returnDate.getTime() < rentDate.getTime())
                return -1;

            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            return (int)diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDifferenceDays(Date date1, Date date2)
    {
        Date rentDate = date1;
        Date returnDate = date2;
        long diffInMillies = Math.abs(rentDate.getTime() - returnDate.getTime());

        // If returnDate is earlier than rentDate, return error
        if (returnDate.getTime() < rentDate.getTime())
            return -1;

        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int)diff + 1;
    }

    public static boolean isNotOverlapping(Date start1, Date end1, Date start2, Date end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null)
            return false;

        // ToDo outdated, temporary
        start1.setHours(0);
        start1.setMinutes(0);
        start1.setSeconds(0);
        end1.setHours(0);
        end1.setMinutes(0);
        end1.setSeconds(1);
        end2.setSeconds(1);
        return !(!start1.after(end2) && !start2.after(end1));
        //return (start1.after(start2) && start1.before(end2)) && (end1.after(start2) && end1.before(end2));
    }

    public static boolean constainsLetters (String string)
    {
        return !string.chars().allMatch(Character::isDigit);
    }

    public static int textFieldIntegerValueCorrect (String string)
    {
        try
        {
            int value = Integer.parseInt(string);
            if (value < 0)
                return -3;
            else
                return value;
        }
        catch (Exception e)
        {
            if (constainsLetters(string))
                return -2;
            else if (string.isEmpty())
                return -4;
            return -1;
        }
    }
}
