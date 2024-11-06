package com.flipkart.application;
import com.flipkart.business.FlipFitGymOwnerService;
import com.flipkart.business.FlipFitUserServices;
//import com.flipkart.utils.DatabaseConnector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;

//import static com.flipkart.utils.StringFormatter.*;


public class GymFlipFitApplication {
    static GymFlipFitGymOwnerMenu owner = new GymFlipFitGymOwnerMenu();
    static GymFlipFitCustomerMenu customer = new GymFlipFitCustomerMenu();

    static Scanner obj = new Scanner(System.in);

    static Properties pr = new Properties();

    /*
     * @main application 
     * @param args
     */

   
    public static void main(String[] args) {
        System.out.println("************************************************************");
        System.out.println("        Welcome to the FlipFit Application!!");
        System.out.println("************************************************************");
        boolean exitFlag = false;
        while(true) {
            System.out.println("================================");

            System.out.println("Press 1 for Registration");
            System.out.println("Press 2 for Login");
            System.out.println("Press 3 for Exit");
            System.out.println("Select Choice: ");

            int option= Integer.parseInt(obj.nextLine());
            switch (option) {
                case 2 :
                    System.out.println("Enter email");
                    String userId = obj.nextLine();
                    System.out.println("Enter password");
                    String password = obj.nextLine();
                    System.out.println("Enter role (Admin/Customer/GymOwner)");
                    String role = obj.nextLine();

                    switch (role) {
                        case "Admin" :
                            GymFlipFitAdminMenu admin = new GymFlipFitAdminMenu();

                            if(!admin.verifyAdminCredentials(userId,password)){
                                System.out.println("Invalid Credentials");
                                break;
                            }

                            boolean flag = true;
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String formattedDateTime = now.format(formatter);
                            System.out.println("Login Successful\n"+formattedDateTime);
                            while(flag) {

                                System.out.println("Press 1 for View all users");
                                System.out.println("Press 2 for View all Gyms");
                                System.out.println("Press 3 for View all Gym Owners");
                                System.out.println("Press 4 for Verify Gym");
                                System.out.println("Press 5 for View pending Gyms");
                                System.out.println("Press 6 for Exit");

                                int k = Integer.parseInt(obj.nextLine());

                                switch (k) {
                                    case 1:
                                        admin.viewUsers();
                                        break;
                                    case 2:
                                        admin.viewGyms();
                                        break;
                                    case 3:
                                        admin.viewGymOwners();
                                        break;
                                    case 4:
                                        System.out.println("Enter the Gym Id to be verified ");
                                        int id1 = Integer.parseInt(obj.nextLine());
                                        admin.verifyGym(id1);
                                        break;
                                    case 5:
                                        admin.viewUnverifiedGyms();
                                        break;
                                    case 6:
                                        flag = false;
                                        break;
                                }
                                if(!flag) break;
                            }
                            break;
                        case "Customer" :
                            if(!customer.userLogin(userId,password))
                                System.out.println("Invalid credentials");
                            break;
                        case "GymOwner" :
                            if(!owner.gymOwnerLogin(userId,password)){
                                System.out.println("Invalid credentials");
                            }
                            break;
                        default:
                            System.out.println("Invalid Options Selected. Please Try Again:(");
                            break;

                    }
                    break;
                case 1 :
                    System.out.println("Press 1 to Register as a GymOwner");
                    System.out.println("Press 2 to Register as a Customer");
                    System.out.println("Press 3 to Go Back");
                    int k = Integer.parseInt(obj.nextLine());
                    switch(k){
                        case 2:
                            customer.createCustomer();
                            break;
                        case 1:
                            owner.createGymOwner();
                        default:
                            break;
                    }
                    break;
                case 3 :
                    //end
                    exitFlag = true;
                    System.out.println("Thank you for using FlipFit :)");
                    break;
                default:
                    System.out.println("Invalid Options Selected. Please Try Again:( ");
                    break;
                }
            if(exitFlag)break;
        }


    }
}

