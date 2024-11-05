package com.flipfit.business;

import com.flipfit.bean.Booking;
import com.flipfit.bean.Gym;
import com.flipfit.bean.Slot;
import com.flipfit.bean.User;

import java.util.List;

 public interface FlipfitCustomerInterface {

    List<Gym> getAllGymCenterDetailsByCity(String city);
    List<Slot> getAvailableSlots(String centreID, String date);
    List<Booking> getCustomerBookings(String customerId);
    boolean bookSlot(String userID,String date, String slotId,String centreId);
    void cancelBookingbyID(String bookingID);
    //    FlipfitCustomer registerCustomer(FlipfitCustomer customer);
    User viewMyProfile(String userName);
    boolean isUserValid(String userName, String password);

}
