package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.ExamAllocatedLocation;

public class LocationDetailsRowMapper implements RowMapper<ExamAllocatedLocation> {

    private static final String COLUMN_ALLOCATED_SEAT = "allocated_seat";
    private static final String COLUMN_LOCATION_ID = "location_id";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_VENUE_NAME = "venue_name";
    private static final String COLUMN_HALL_NAME = "hall_name";
    private static final String COLUMN_TOTAL_CAPACITY = "total_capacity";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LOCATION_URL = "location_url";
    private static final String COLUMN_FILLED_CAPACITY = "filled_capacity";
    private static final String COLUMN_SERIAL_NO = "serial_no";
    
    private final int examId;
    
    public LocationDetailsRowMapper(int examId) {
        this.examId = examId;
    }

    @Override
    public ExamAllocatedLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
        int allocatedSeat = rs.getInt(COLUMN_ALLOCATED_SEAT);
        int locationId = rs.getInt(COLUMN_LOCATION_ID);
        String city = rs.getString(COLUMN_CITY);
        String venueName = rs.getString(COLUMN_VENUE_NAME);
        String hallName = rs.getString(COLUMN_HALL_NAME);
        int totalCapacity = rs.getInt(COLUMN_TOTAL_CAPACITY);
        String address = rs.getString(COLUMN_ADDRESS);
        String locationUrl = rs.getString(COLUMN_LOCATION_URL);
        int filledCapacity = rs.getInt(COLUMN_FILLED_CAPACITY);
        String serialNo = rs.getString(COLUMN_SERIAL_NO);
        System.out.println("in Exam AllocatedLoaction");
        return new ExamAllocatedLocation(allocatedSeat, locationId, city, venueName, hallName, totalCapacity, address, locationUrl, examId, filledCapacity, serialNo);
    }
}
