package com.flipkart.dao;

import com.flipkart.bean.*;
import com.flipkart.exception.BookingCancellationFailedException;
import com.flipkart.exception.RegistrationFailedException;
import com.flipkart.exception.SlotsUnavailableException;
import com.flipkart.utils.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;



/**
 * Implementation of FlipFitCustomerDAOInterface for customer operations.
 */
public class FlipFitCustomerDAOImpl implements FlipFitCustomerDAOInterface {

    DatabaseConnector connector;
    Connection conn;
    static Scanner obj = new Scanner(System.in);

    @Override
    public List<FlipFitGym> getAllGymsByArea() {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitGym> flipFitGyms = new ArrayList<FlipFitGym>();

        try {
            String sqlQuery = "SELECT * FROM gyms";
            preparedStatement = conn.prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("gymId");
                String gymAddress = resultSet.getString("gymAddress");
                String location = resultSet.getString("location");
                String gymName = resultSet.getString("gymName");
                String status = resultSet.getString("status");
                String gymOwnerID = resultSet.getString("ownerid");

                // Skip gyms that are already verified
                if (Objects.equals(status, "unverified")) continue;

                FlipFitGym flipFitGym = new FlipFitGym();
                flipFitGym.setGymId(id);
                flipFitGym.setGymName(gymName);
                flipFitGym.setGymAddress(gymAddress);
                flipFitGym.setOwnerId(gymOwnerID);
                flipFitGym.setLocation(location);
                flipFitGym.setStatus(status);

                // Fetch and set slots for this gym
                List<FlipFitSlots> flipFitSlots = getGymSlotsWithGymId(id);
                flipFitGym.setSlots(flipFitSlots);

                flipFitGyms.add(flipFitGym);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flipFitGyms;
    }

    public void AddCard(String UserEmail, String CardNumber, int cvv, String Exp ) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;

        String insertQuery = "INSERT INTO payment (userId, cardNumber, cvv, exp) VALUES(?,?,?,?)";

        try {
            preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, UserEmail);
            preparedStatement.setString(2, CardNumber);
            preparedStatement.setInt(3, cvv);
            preparedStatement.setString(4, Exp);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewCards(String userEmail){
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitPayment> flipFitPayments = new ArrayList<>();
        String viewCardQuery = "SELECT CardId, cardNumber, cvv, exp FROM payment where userId=?";
        try {
            preparedStatement = conn.prepareStatement(viewCardQuery);
            preparedStatement.setString(1, userEmail);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("CardId");
                String number = resultSet.getString("cardNumber");
                int cvv = resultSet.getInt("cvv");
                String exp = resultSet.getString("exp");

                FlipFitPayment flipFitPayment = new FlipFitPayment();
                flipFitPayment.setId(id);
                flipFitPayment.setCardNumber(number);
                flipFitPayment.setCvv(cvv);
                flipFitPayment.setExp(exp);
                flipFitPayments.add(flipFitPayment);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (FlipFitPayment card : flipFitPayments) {
            System.out.println("Card ID: " + card.getCardId() + " Card Number: " + card.getCardNumber() + " CVV: " + card.getCvv() + " Expiry: " + card.getExp() );
        }


//        return flipFitPayments;
    }


    public boolean IsSlotAlreadyRegistered(String email, int time, int GymId)
    {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitBookings> bookings = new ArrayList<>();
        String ViewBookingQuery=  "SELECT userID, time, gymId FROM Booking where UserEmail=?";
        try {
            preparedStatement = conn.prepareStatement(ViewBookingQuery);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int UserId = resultSet.getInt("userID");
                int slot_time = resultSet.getInt("time");
                int gym_id  = resultSet.getInt("gymId");

                FlipFitBookings flipFitbooking = new FlipFitBookings();
                flipFitbooking.setUserId(UserId);
                flipFitbooking.setTime(slot_time);
                flipFitbooking.setGymId(gym_id);
                bookings.add(flipFitbooking);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (FlipFitBookings booking: bookings) {
            if (booking.getTime() == time)
                return true;
        }

            return false;

    }

    @Override
    public boolean bookSlot(int gymId, int time, String email) {
        conn = DatabaseConnector.getConnection();
        if(IsSlotAlreadyRegistered(email, time, gymId))
        {
            System.out.println("Selected Slot already booked by same user");
            return false;
        }
        System.out.println("Press1 to view all saved cards:");
        System.out.println("Press2 to add new Card: ");

        int choice = Integer.parseInt(obj.nextLine());
        switch (choice) {

            case 1:
                System.out.println("Available Cards:");
                viewCards(email);
                break;

            case 2:
                System.out.println("Enter Card Number: ");
                String CardNumber= obj.nextLine();
                System.out.println("Enter CVV: ");
                int cvv = Integer.parseInt(obj.nextLine());
                System.out.println("Enter Expiry Date (in mm/yyyy format): ");
                String exp = obj.nextLine();

                AddCard(email, CardNumber, cvv, exp);
                viewCards(email);
                break;
            default:
                System.out.println("Invalid Input");
                break;


        }

        System.out.println("Select/Enter Card ID from Above Provided List: ");

        System.out.println("Enter Card Id: ");
        int cardID= Integer.parseInt(obj.nextLine());

        PreparedStatement preparedStatement = null;
        String insertQuery = "INSERT INTO Booking (status, date, time, slotId, GymId, UserEmail ) VALUES(?,?,?,?,?,?)";

        try {
            // Check if slots are available
            int remaining = getSeatNumberWithGymIDandSlotIdFromSlots(gymId, time);
            if (remaining <= 0) {
                throw new SlotsUnavailableException();
            }

            preparedStatement = conn.prepareStatement(insertQuery);

            preparedStatement.setString(1, "CONFIRMED");
            preparedStatement.setInt(2, 11); // Assuming date is fixed for this example
            preparedStatement.setInt(3, time);
            preparedStatement.setInt(4, time);
            preparedStatement.setInt(5, gymId);
            preparedStatement.setString(6, email);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Successfully booked
                alterSeatsWithGymIDSlotID(gymId, time, remaining - 1);
                System.out.println("Payment successful!");
                return true;
            } else {
                throw new SlotsUnavailableException();
            }

        } catch (SlotsUnavailableException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private int getSeatNumberWithGymIDandSlotIdFromSlots(int gymId, int time) {
        conn = DatabaseConnector.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        int remainingSeats = 0;

        try {
            String sqlQuery = "SELECT seatCount FROM slots WHERE gymId = ? AND startTime = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, gymId);
            preparedStatement.setInt(2, time);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                remainingSeats = resultSet.getInt("seatCount");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return remainingSeats;
    }

    private void alterSeatsWithGymIDSlotID(int gymId, int time, int remainingSeats) {
        conn = DatabaseConnector.getConnection();

        try {
            String sqlQuery = "UPDATE slots SET seatCount = ? WHERE gymId = ? AND startTime = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, remainingSeats);
            preparedStatement.setInt(2, gymId);
            preparedStatement.setInt(3, time);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Seats updated successfully!");
            } else {
                System.out.println("Failed to update seats.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<FlipFitBookings> getAllBookingByUserID(String userId) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitBookings> flipFitBookings = new ArrayList<>();

        try {
            String sqlQuery = "SELECT * FROM Booking WHERE UserEmail = ?";
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id= resultSet.getInt("userID");
                int date = resultSet.getInt("date");
                int time = resultSet.getInt("time");
                int slotId = resultSet.getInt("slotId");
                String status = resultSet.getString("status");
                int gymId = resultSet.getInt("gymId");
                String UserEmail = resultSet.getString("UserEmail");

                FlipFitBookings booking = new FlipFitBookings();

                booking.setBookingId(id);

                booking.setDate(date);
                booking.setTime(time);
                booking.setSlotId(slotId);
                booking.setStatus(status);
                booking.setGymId(gymId);
                booking.setUserEmail(UserEmail);

                flipFitBookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flipFitBookings;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            String deleteQuery = "DELETE FROM Booking WHERE bookingId = ?";
            preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, bookingId);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Booking cancellation successful!");
                return true;
            } else {
                throw new BookingCancellationFailedException();
            }

        } catch (BookingCancellationFailedException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean validateUser(String username, String pass) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT password FROM User WHERE email = ?";
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String passwordFromDB = resultSet.getString("password");
                return passwordFromDB.equals(pass);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean IsUserAlreadyRegistered(String UserEmail, String Phone)
    {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitUser> user = new ArrayList<>();
        String ViewUserQuery=  "SELECT * FROM User where email=? OR phoneNumber=?";
        try {
            preparedStatement = conn.prepareStatement(ViewUserQuery);
            preparedStatement.setString(1, UserEmail);
            preparedStatement.setString(2, Phone);
            resultSet = preparedStatement.executeQuery();

           if(resultSet.next())
               return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            // Close resources in the finally block to avoid resource leaks
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e); // Handle closing exceptions
            }
        }

        return false;

    }

    @Override
    public void createUser(FlipFitUser flipFitUser) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;

        try {

            if(IsUserAlreadyRegistered(flipFitUser.getEmail(), flipFitUser.getPhoneNumber()))
            {
                System.out.println("Already Registered User");
                return;
            }
            System.out.println(flipFitUser.toString());
            String insertQuery = "INSERT INTO User (userName, email, password, phoneNumber, Address, location) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, flipFitUser.getUserName());
            preparedStatement.setString(2, flipFitUser.getEmail());
            preparedStatement.setString(3, flipFitUser.getPassword());
            preparedStatement.setString(4, flipFitUser.getPhoneNumber());
            preparedStatement.setString(5, flipFitUser.getAddress());
            preparedStatement.setString(6, flipFitUser.getLocation());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
            	LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                System.out.println("User registration successful!\n"+formattedDateTime);
            } else {
                throw new RegistrationFailedException();
            }

        } catch (RegistrationFailedException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<FlipFitSlots> getGymSlotsWithGymId(int id) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitSlots> slotList = new ArrayList<>();

        try {
            String sqlQuery = "SELECT * FROM slots WHERE gymId = ?";
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                
                int startTime = resultSet.getInt("startTime");
              int seats = resultSet.getInt("seatCount");
              FlipFitSlots flipFitSlots = new FlipFitSlots(1,startTime,seats);

              slotList.add(flipFitSlots);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return slotList;
    }
}

