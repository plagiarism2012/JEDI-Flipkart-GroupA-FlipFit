
package com.flipkart.dao;

import com.flipkart.bean.FlipFitGym;
import com.flipkart.bean.FlipFitGymOwner;
import com.flipkart.exception.VerificationFailedException;
import com.flipkart.constants.SQLConstants;
import com.flipkart.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of FlipFitAdminDAOInterface for administrative operations related to gyms and gym owners.
 */
public class FlipFitAdminDAOImpl implements FlipFitAdminDAOInterface {
    DatabaseConnector connector;
    Connection conn;

    /**
     * Retrieves and displays details of all gyms from the database.
     */
    @Override
    public void viewGyms() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = DatabaseConnector.getConnection();
            String sqlQuery = SQLConstants.ADMIN_VIEW_ALL_GYMS;
            preparedStatement = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("gymId");
                String ownerId = resultSet.getString("ownerId");
                String name = resultSet.getString("gymName");
                String gymAddress = resultSet.getString("gymAddress");
                String location = resultSet.getString("location");
                String status = resultSet.getString("status");

                // Display gym details
                System.out.println("Gym ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Owner Id : " + ownerId);
                System.out.println("Gym Address : " + gymAddress);
                System.out.println("Gym Location : " + location);
                System.out.println("Status of Gym (verified or not) : " + status);
                System.out.println("---------------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves and displays details of all users from the database.
     */
    @Override
    public void viewUsers() {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            conn = DatabaseConnector.getConnection();
            preparedStatement = conn.prepareStatement(SQLConstants.ADMIN_VIEW_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("userId");
                String phoneNo = resultSet.getString("phoneNumber");
                String name = resultSet.getString("userName");
                String address = resultSet.getString("Address");
                String loc = resultSet.getString("location");
                String email = resultSet.getString("email");

                // Display user details
                System.out.println("User ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Phone No : " + phoneNo);
                System.out.println("Address : " + address);
                System.out.println("Email Id : " + email);
                System.out.println("Location : " + loc);
                System.out.println("---------------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves and displays details of all gym owners from the database.
     */
    @Override
    public void viewGymOwners() {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            conn = DatabaseConnector.getConnection();
            preparedStatement = conn.prepareStatement(SQLConstants.ADMIN_VIEW_ALL_GYMOWNERS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String phoneNo = resultSet.getString("phone_number");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String adhaar = resultSet.getString("aadhar");
                String pan = resultSet.getString("pancard");
                String statusGymOwner = resultSet.getString("status");

                // Display gym owner details
                System.out.println("Gym Owner ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Phone No : " + phoneNo);
                System.out.println("Email Id : " + email);
                System.out.println("Adhaar no : " + adhaar);
                System.out.println("PAN Card Number : " + pan);
                System.out.println("Status  of The Gym Owner : " + statusGymOwner);
                System.out.println("---------------------------------");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Verifies a gym owner identified by the given id by updating their status in the database.
     * 
     * @param id The ID of the gym owner to verify
     */
    @Override
    public void verifyGymOwners(int id) {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String query = SQLConstants.ADMIN_VERIFY_GYMOWNERS;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "verified");
            pstmt.setString(2, Integer.toString(id));
            int stats = pstmt.executeUpdate();

            if (stats > 0) {
              System.out.println("Verified Gym successfully");
          } else {
              throw new VerificationFailedException();
//             System.out.println("Gym Owner verification failed");
          }

            System.out.println("---------------------------------");
        } catch (VerificationFailedException ex) {
            System.out.println("Gym Owner " + ex.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Verifies a gym identified by the given id by updating its status in the database.
     * 
     * @param id The ID of the gym to verify
     */
    @Override
    public void verifyGyms(int id) {
        PreparedStatement preparedStatement = null;
        conn = DatabaseConnector.getConnection();
        try {
            String query = SQLConstants.ADMIN_VERIFY_GYMS;
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "verified");
            pstmt.setString(2, Integer.toString(id));
            int stats = pstmt.executeUpdate();

            if (stats > 0) {
              System.out.println("Verified Gym successfully");
          } else {
              throw new VerificationFailedException();
//              System.out.println("Gym Owner verification failed");
          }

            System.out.println("---------------------------------");
        } catch (VerificationFailedException ex) {
            System.out.println("Gym " + ex.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieves a list of gyms that are currently unverified from the database.
     * 
     * @return List of unverified FlipFitGym objects
     */
    @Override
    public List<FlipFitGym> getUnverifiedGyms() {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitGym> flipFitGyms = new ArrayList<>();

        try {
            String sqlQuery = SQLConstants.ADMIN_VIEW_UNVERIFIED_GYMS;
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, "Unverified");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("gymId");
                String gymAddress = resultSet.getString("gymAddress");
                String location = resultSet.getString("location");
                String gymName = resultSet.getString("gymName");
                String status = resultSet.getString("status");
                String gymOwnerID = resultSet.getString("ownerId");

                FlipFitGym flipFitGym = new FlipFitGym();
                flipFitGym.setGymId(id);
                flipFitGym.setGymName(gymName);
                flipFitGym.setGymAddress(gymAddress);
                flipFitGym.setLocation(location);
                flipFitGym.setStatus(status);
                flipFitGym.setOwnerId(gymOwnerID);

                flipFitGyms.add(flipFitGym);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flipFitGyms;
    }

    /**
     * Retrieves a list of gym owners that are currently unverified from the database.
     * 
     * @return List of unverified FlipFitGymOwner objects
     */
    @Override
    public List<FlipFitGymOwner> getUnverifiedGymOwner() {
        conn = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<FlipFitGymOwner> flipFitGymOwners = new ArrayList<>();

        try {
            String sqlQuery = SQLConstants.ADMIN_VIEW_UNVERIFIED_GYMOWNER;
            preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, "Unverified");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String ph = resultSet.getString("phone_number");
                String nationalId = resultSet.getString("aadhar");
                String gst = resultSet.getString("gst");

                FlipFitGymOwner gymOwner = new FlipFitGymOwner();
                gymOwner.setOwnerId(id);
                gymOwner.setOwnerName(name);
                gymOwner.setOwnerEmail(email);
                gymOwner.setPhoneNo(ph);
                gymOwner.setNationalId(nationalId);
                gymOwner.setGST(gst);

                flipFitGymOwners.add(gymOwner);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return flipFitGymOwners;
    }
}
