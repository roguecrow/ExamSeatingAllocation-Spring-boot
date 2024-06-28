//package com.chainsys.examease.dao;
//
//public class ExamSeatAllocator {
//	
//    public void allocateSeats(UserDetails details, int examId) throws SQLException, ClassNotFoundException  {
//    	DbManager manage = new DbManager();
//
//        String cityPreference1 = details.getCityPreference1();
//        String cityPreference2 = details.getCityPreference2();
//        String cityPreference3 = details.getCityPreference3();
//        
//        
//        List<ExamLocationDetails> examLocations = manage.findExamLocationById(examId);
//        
//        boolean seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference1, details, examId);
//        if (!seatAllocated) {
//            seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference2, details, examId);
//        }
//        if (!seatAllocated) {
//            seatAllocated = allocateSeatByCityPreference(examLocations, cityPreference3, details, examId);
//        }
//        
//        if (!seatAllocated) {
//            allocateSeatInAnyAvailableCity(examLocations, details, examId);
//        }
//    }
//    
//    private boolean allocateSeatByCityPreference(List<ExamLocationDetails> examLocations, String cityPreference, UserDetails details, int examId) throws SQLException, ClassNotFoundException {
//    	DbManager manage = new DbManager();
//
//        for (ExamLocationDetails location : examLocations) {
//            if (location.getCity().equalsIgnoreCase(cityPreference) && location.getCapacity() > location.getFilledCapacity()) {
//            	
//                location.setFilledCapacity(location.getFilledCapacity() + 1);
//                
//                int lastAllocatedSeat = manage.getLastAllocatedSeatId(location.getLocationId());
//                
//                manage.updateCapacity(location.getLocationId(), location.getFilledCapacity());
//                
//                
//                manage.addExamSeating(details.getRollNo(), examId, location.getLocationId(),
//                		seatNoGenerator(location.getLocationId(), location.getCity(), location.getVenueName(), location.getHallName(), lastAllocatedSeat + 1), lastAllocatedSeat + 1);
//                return true;
//            }
//        }
//        return false;
//    }
//    
//    private boolean allocateSeatInAnyAvailableCity(List<ExamLocationDetails> examLocations, UserDetails details, int examId) throws SQLException, ClassNotFoundException {
//    	DbManager manage = new DbManager();
//
//        for (ExamLocationDetails location : examLocations) {
//            if (location.getCapacity() > location.getFilledCapacity()) {
//                
//                location.setFilledCapacity(location.getFilledCapacity() + 1);
//                
//                int lastAllocatedSeat = manage.getLastAllocatedSeatId(location.getLocationId());
//                
//                manage.updateCapacity(location.getLocationId(), location.getFilledCapacity());
//                
//                
//                manage.addExamSeating(details.getRollNo(), examId, location.getLocationId(),
//                		seatNoGenerator(location.getLocationId(), location.getCity(), location.getVenueName(), location.getHallName(), lastAllocatedSeat + 1), lastAllocatedSeat + 1);
//                return true;
//            }
//        }
//        return false;
//    }
//    
//    public static String seatNoGenerator(int locationId, String city, String venue, String hall, int seatingId) {
//        char cityFirstChar = city.charAt(0);
//        char venueFirstChar = venue.charAt(0);
//        char hallFirstChar = hall.charAt(0);
//
//        return locationId + "" + cityFirstChar + "" + venueFirstChar + "" + hallFirstChar + String.format("%02d", seatingId);
//    }
//}