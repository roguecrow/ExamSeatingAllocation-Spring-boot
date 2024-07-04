package com.chainsys.examease.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;

@Configuration
public class ExamSeatAllocator {
	
	@Autowired
	UserDAO userDAO;
	
    public void allocateSeats(User details, int examId) throws ClassNotFoundException  {

        String cityPreference1 = details.getCityPreference1();
        String cityPreference2 = details.getCityPreference2();
        String cityPreference3 = details.getCityPreference3();
        
        
        List<ExamLocation> examLocations = userDAO.findExamLocationById(examId);
        
        boolean seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference1, details, examId);
        if (!seatAllocated) {
            seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference2, details, examId);
        }
        if (!seatAllocated) {
            seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference3, details, examId);
        }
        
        if (!seatAllocated) {
            allocateSeatInAnyAvailableCity(examLocations, details, examId);
        }
    }
    
    private boolean allocateSeatByCityPreference(List<ExamLocation> examLocations, String cityPreference, User details, int examId) throws ClassNotFoundException {

        for (ExamLocation location : examLocations) {
            if (location.getCity().equalsIgnoreCase(cityPreference) && location.getCapacity() > location.getFilledCapacity()) {
            	
                location.setFilledCapacity(location.getFilledCapacity() + 1);
                
                int lastAllocatedSeat = userDAO.getLastAllocatedSeatId(location.getLocationId());
                
                userDAO.updateCapacity(location.getLocationId(), location.getFilledCapacity());
                
                
                userDAO.addExamSeating(details.getRollNo(), examId, location.getLocationId(),
                		seatNoGenerator(location.getLocationId(), location.getCity(), location.getVenueName(), location.getHallName(), lastAllocatedSeat + 1), lastAllocatedSeat + 1);
                return true;
            }
        }
        return false;
    }
    
    private boolean allocateSeatInAnyAvailableCity(List<ExamLocation> examLocations, User details, int examId) throws ClassNotFoundException {

        for (ExamLocation location : examLocations) {
            if (location.getCapacity() > location.getFilledCapacity()) {
                
                location.setFilledCapacity(location.getFilledCapacity() + 1);
                
                int lastAllocatedSeat = userDAO.getLastAllocatedSeatId(location.getLocationId());
                
                userDAO.updateCapacity(location.getLocationId(), location.getFilledCapacity());
                
                
                userDAO.addExamSeating(details.getRollNo(), examId, location.getLocationId(),
                		seatNoGenerator(location.getLocationId(), location.getCity(), location.getVenueName(), location.getHallName(), lastAllocatedSeat + 1), lastAllocatedSeat + 1);
                return true;
            }
        }
        return false;
    }
    
    public static String seatNoGenerator(int locationId, String city, String venue, String hall, int seatingId) {
        char cityFirstChar = city.charAt(0);
        char venueFirstChar = venue.charAt(0);
        char hallFirstChar = hall.charAt(0);

        return locationId + "" + cityFirstChar + "" + venueFirstChar + "" + hallFirstChar + String.format("%02d", seatingId);
    }
}