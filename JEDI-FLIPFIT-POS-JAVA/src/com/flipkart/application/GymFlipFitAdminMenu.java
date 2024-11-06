package com.flipkart.application;
import com.flipkart.bean.FlipFitGym;
import com.flipkart.bean.FlipFitGymOwner;
import com.flipkart.business.FlipFitAdminService;
//import com.flipkart.utils.DatabaseConnector;
import java.util.List;
import java.util.Objects;

/*
 * This class represents the administrative menu for the GymFlipFit application.
 * It provides various functions for managing gyms, gym owners, and admin credentials.
 */

public class GymFlipFitAdminMenu {


	    public void viewGyms() {
			System.out.println("all the gyms");
	    }

	    public void viewUsers() {
			System.out.println("all the users");;
	    }

	    public void viewGymOwners() {

			System.out.println("all the gym owners");;
	    }


	    public void verifyGym(int id) {

			System.out.println("gym verified");
	    }
	    public void viewUnverifiedGyms() {
			System.out.println("unverified gyms");
	    }
	    public boolean verifyAdminCredentials(String id, String pass) {
            return Objects.equals(id, "admin") && Objects.equals(pass, "password");
	    }
	}
