package com.flipkart.dao;

import com.flipkart.bean.FlipFitGym;
import com.flipkart.bean.FlipFitGymOwner;
import com.flipkart.bean.FlipFitSlots;
import com.flipkart.constants.SQLConstants;
import com.flipkart.exception.RegistrationFailedException;
import com.flipkart.exception.SlotInsertionFailedException;
import com.flipkart.utils.DatabaseConnector;

//import com.flipkart.dao.GymOwnerDAOImplementation;
//import com.flipkart.dao.DatabaseConnector;

import java.sql.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.sound.sampled.FloatControl.Type.PAN;

public class FlipFitGymOwnerDAOImpl implements FlipFitGymOwnerDAOInterface {
    Connection conn;

    DatabaseConnector connector;


    public boolean IsGymAlreadyRegistered(String GymName, String GymAddress)
    {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String ViewGymQuery=  "SELECT * FROM gyms where gymName=? AND gymAddress=?";
        try {
            preparedStatement = conn.prepareStatement(ViewGymQuery);
            preparedStatement.setString(1, GymName);
            preparedStatement.setString(2, GymAddress);

            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);

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
                System.out.println(e);// Handle closing exceptions
            }
        }

        return false;

    }

    @Override
    public void addGym(FlipFitGym flipFitGym){

        if(IsGymAlreadyRegistered(flipFitGym.getGymName(), flipFitGym.getGymAddress()))
        {
            System.out.println("Gym Already Registered");
            return;

        }

        conn = DatabaseConnector.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        int id = 0;
        try {
            statement = conn.createStatement();
            preparedStatement =  conn.prepareStatement(SQLConstants.GYM_OWNER_INSERT_GYM, statement.RETURN_GENERATED_KEYS);


            preparedStatement.setString(1, flipFitGym.getGymAddress());
            preparedStatement.setString(2, flipFitGym.getLocation());
            preparedStatement.setString(3, flipFitGym.getGymName());
            preparedStatement.setString(4, flipFitGym.getStatus());
            preparedStatement.setString(5, flipFitGym.getOwnerId());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Record inserted successfully!");
            } else {
                throw new RegistrationFailedException();
////                System.out.println("Failed to insert the record.");
//                return ;
            }
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }


        } catch (RegistrationFailedException ex){
            System.out.println("Gym "+ex.getMessage());

        }
        catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        insertSlots(flipFitGym.getSlots(),id);

    }


    public boolean IsGymOwnerAlreadyRegistered(String OwnerEmail, String Phone, String PAN, String GST, String Aadhar)
    {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String ViewUserQuery=  "SELECT * FROM gym_owner where email=? OR phone_number=? OR pancard=? OR aadhar=? OR gst=?";
        try {
            preparedStatement = conn.prepareStatement(ViewUserQuery);
            preparedStatement.setString(1, OwnerEmail);
            preparedStatement.setString(2, Phone);
            preparedStatement.setString(3, PAN);
            preparedStatement.setString(4, Aadhar);
            preparedStatement.setString(5, GST);
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
    public void newGymOwner(FlipFitGymOwner flipFitGymOwner) {


        String email= flipFitGymOwner.getOwnerEmail();
        String Phone= flipFitGymOwner.getPhoneNo();
        String pan= flipFitGymOwner.getPAN();
        String gst= flipFitGymOwner.getGST();
        String Aadhar= flipFitGymOwner.getNationalId();

        if(IsGymOwnerAlreadyRegistered(email, Phone, pan, gst, Aadhar))
        {
            System.out.println("Gym Owner Already Registered.");
            return;
        }
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        conn = DatabaseConnector.getConnection();
        System.out.println(flipFitGymOwner.getPAN()+"kk");
        try {
            statement = conn.createStatement();
//            resultSet = statement.executeQuery(insertQuery);
            preparedStatement =  conn.prepareStatement(SQLConstants.GYM_OWNER_INSERT);

            // 5. Set values for the placeholders in the prepared statement

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, flipFitGymOwner.getOwnerName());
            preparedStatement.setString(3, flipFitGymOwner.getPassword());
            preparedStatement.setString(4, Phone);
            preparedStatement.setString(5, pan);
            preparedStatement.setString(6, Aadhar);
            preparedStatement.setString(7, gst);
            preparedStatement.setString(8, flipFitGymOwner.getStatus());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
            	 LocalDateTime now = LocalDateTime.now();
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                 String formattedDateTime = now.format(formatter);
                System.out.println("Gym Owner registered successfully!\n"+formattedDateTime);
            } else {
                throw new RegistrationFailedException();
//                System.out.println("Failed to insert the record.");
//                return ;
            }

        }catch(RegistrationFailedException ex){
            System.out.println("Gym Owner" + ex.getMessage());
        }catch (SQLException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateLogin(String email, String password) {
        conn = DatabaseConnector.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            statement = conn.createStatement();
            preparedStatement = conn.prepareStatement(SQLConstants.GYM_USER_VERIFY_PASSWORD, statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);


            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public void insertSlots(List<FlipFitSlots> flipFitSlots, int gymId){
        conn = DatabaseConnector.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        for( FlipFitSlots slot: flipFitSlots){
            String insertQuery = "INSERT INTO slots (startTime, seatCount, gymId) VALUES (?, ?, ?)";

            try {
                statement = conn.createStatement();
//                resultSet = statement.executeQuery(insertQuery);
                preparedStatement =  conn.prepareStatement(insertQuery);

                // 5. Set values for the placeholders in the prepared statement

                preparedStatement.setInt(1, slot.getStartTime());
                preparedStatement.setInt(2, slot.getSeatCount());
                preparedStatement.setInt(3, gymId);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Record inserted successfully!");
                } else {
                    throw new SlotInsertionFailedException();

//                    System.out.println("Failed to insert the record.");
//                    return ;
                }

            }catch(SlotInsertionFailedException | SQLException ex){
                System.out.println(ex.getMessage());
            }
//
        }


    }

    @Override
    public List<FlipFitGym> viewGymSlots(String gymOwnerID) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitGym> flipFitGyms = new ArrayList<>();

        try {
            String sqlQuery = "SELECT * FROM gyms WHERE ownerId=?";
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, gymOwnerID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("gymId");
                String gymAddress = resultSet.getString("gymAddress");
                String location = resultSet.getString("location");
                String gymName = resultSet.getString("gymName");
                String status = resultSet.getString("status");
                FlipFitGym flipFitGym = new FlipFitGym();
                flipFitGym.setGymName(gymName);
                flipFitGym.setGymAddress(gymAddress);
                flipFitGym.setOwnerId(gymOwnerID);
                flipFitGym.setLocation(location);
                flipFitGym.setStatus(status);
                flipFitGyms.add(flipFitGym);

                List<FlipFitSlots> flipFitSlots = getGymSlotsWithGymId(id);
                flipFitGym.setSlots(flipFitSlots);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return flipFitGyms;
    }

    public List<FlipFitSlots> getGymSlotsWithGymId(int id){
        conn = DatabaseConnector.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        List<FlipFitSlots> slotList = new ArrayList<>();
        try {
            String sqlQuery = "SELECT * FROM slots WHERE gymId= " + id;
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
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