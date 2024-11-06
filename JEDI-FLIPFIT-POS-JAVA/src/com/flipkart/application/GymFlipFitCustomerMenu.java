package com.flipkart.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.flipkart.bean.*;

import java.util.Scanner;
/*
 * This class represents the customer menu for the GymFlipFit application
 * It provides various functions for customer actions such as viewing gyms, booking slots, and managing bookings.
 */
public class GymFlipFitCustomerMenu {
    static Scanner obj = new Scanner(System.in);
    FlipFitUser flipFitUser = new FlipFitUser();

    public boolean userLogin(String username, String pass) {
        if (validateUser(username, pass)) {
            boolean flag = true;
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            System.out.println("Login Successful\n"+formattedDateTime);
            while (flag) {
                System.out.println("*********CUSTOMER MENU*********\n");
                System.out.println("Press 1 to Book your slot");
                System.out.println("Press 2 to View all available gyms with slots");
                System.out.println("Press 3 to View all your bookings");
                System.out.println("Press 4 to Logout");
                int choice = Integer.parseInt(obj.nextLine());
                switch (choice) {
                    case 2:
                        List<FlipFitGym> flipFitGyms = viewAllGymswithSlots();
                        printGyms(flipFitGyms);
                        break;
                    case 1:
                        List<FlipFitGym> gyms1 = viewAllGymswithSlots();
                        printGyms(gyms1);
                        System.out.println("Enter the following:");
                        System.out.println("Gym ID");
                        int gymId = Integer.parseInt(obj.nextLine());
                        System.out.println("Slot Time");
                        int time = Integer.parseInt(obj.nextLine());
                        if (bookSlot(gymId, time, username)) {
                            System.out.println("Booked Successfully");
                        } else {
                            System.out.println("Booking Unsuccessful");
                        }
                        break;
//                    case 5:
//                        Scanner sc = new Scanner(System.in);
//                        System.out.println("My Bookings");
//                        System.out.println(viewAllBookings(username));
//                        System.out.println("Enter Booking ID");
//                        int bookingId = sc.nextInt();
//                        cancelSlot(bookingId);
//                        break;
                    case 3:
                        System.out.println("My Bookings");
                        List<FlipFitBookings> flipFitBookings = viewAllBookings(username);
                        for (FlipFitBookings booking : flipFitBookings) {
                            System.out.println("Booking ID: " + booking.getBookingId() + " Booking Status: " + booking.getStatus() + " Time: " + booking.getTime() + " GymID: " + booking.getGymId());
                        }
                        break;
                    case 4:
                        flag = false;
                        break;
                    default:
                        System.out.println("Wrong Choice");
                }
            }

        } else return false;
        return true;
    }

/*
 *Print the lists of gym
 */
    private void printGyms(List<FlipFitGym> y) {
        for (FlipFitGym flipFitGym : y) {
            System.out.println("====================");
            System.out.println("Gym name: " + flipFitGym.getGymName() + " Gym ID: " + flipFitGym.getGymId() + " Gym Location: " + flipFitGym.getLocation() + " Gym Address: " + flipFitGym.getGymAddress());
            System.out.println("Slot List");
            String leftAlignFormat = "| %-15d | %-15d | %-20d |%n";
            System.out.format("+-----------------+-----------------+----------------------+\n");
            System.out.format("| Start Time      |   End Time      | Remaining Seats      |\n");
            System.out.format("+-----------------+-----------------+----------------------+\n");

            for (FlipFitSlots slot : flipFitGym.getSlots()) {
                System.out.format(leftAlignFormat,slot.getStartTime(),(slot.getStartTime() + 1),slot.getSeatCount());
            }
            System.out.format("+-----------------+-----------------+----------------------+\n");

        }
    }

/*
 *validate user credentials.
 *@param username The username of the customer
 *@param pass The password of the customer
 *@return True if user credentials are valid, false otherwise
 */
    public boolean validateUser(String username, String pass) {
        return true;
    }

/*
 *View all gyms with available slots
 *@return list of gyms with available slots.
 */
    List<FlipFitGym> viewAllGymswithSlots() {
        System.out.println("List of Gyms");
        List<FlipFitGym> gymList = new ArrayList<>();
        return gymList;
    }

/*
 * Book a slot for a specific gym
 * @param gymId The Id of the gym
 * @param time The slot time
 * @param email The email of the user booking the slot
 * @return True if the slot is booked successfully, false otherwise
 */
    public boolean bookSlot(int gymId, int time, String email) {
        return true;
    }

/*
 *Cancel a booked slot
 *@param bookingId The ID of the booking to be cancelled.
 */
    public void cancelSlot(int bookingId) {
        System.out.println("Slot Cancelled");
    }

/*
 *View all bookings of a specific user.
 *@param userid The ID of the user.
 *@return List of user's bookings.
 */
    public List<FlipFitBookings> viewAllBookings(String userid) {
        List<FlipFitBookings> ls=new ArrayList<>();
        return ls;
    }

/*
 *View all gyms in a specific area.
 *@param location The location (area) to filter gyms
 *@return List of gyms in the specified area.
 */
    List<FlipFitGym> viewAllGymsByArea(String location) {
        System.out.println("List of Gyms");
        List<FlipFitGym> gymList = new ArrayList<>();
        return gymList;
    }

/*/
 * Create a new customer user.
 */
    public void createCustomer() {
        System.out.println("Enter the following info:");
        System.out.println("\nYour email: ");
        String ownerEmail = obj.nextLine();
        System.out.println("\nYour name: ");
        String ownerName = obj.nextLine();
        System.out.println("\nEnter a password: ");
        String password = obj.nextLine();
        System.out.println("\nPhone number: ");
        String phoneNo = obj.nextLine();
        System.out.println("\nEnter Address ");
        String nationalId = obj.nextLine();
        System.out.println("\nLocation: ");
        String GST = obj.nextLine();

        FlipFitUser flipFitUser = new FlipFitUser();
        flipFitUser.setEmail(ownerEmail);
        flipFitUser.setAddress(nationalId);
        flipFitUser.setLocation(GST);
        flipFitUser.setPassword(password);
        flipFitUser.setUserName(ownerName);
        flipFitUser.setPhoneNumber(phoneNo);
    }
}
