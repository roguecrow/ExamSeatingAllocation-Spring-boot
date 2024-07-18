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
        ExamAllocatedLocation examAllocatedLocation = new ExamAllocatedLocation();
        examAllocatedLocation.setAllocatedSeat(rs.getInt(COLUMN_ALLOCATED_SEAT));
        examAllocatedLocation.setLocationId(rs.getInt(COLUMN_LOCATION_ID));
        examAllocatedLocation.setCity(rs.getString(COLUMN_CITY));
        examAllocatedLocation.setVenueName(rs.getString(COLUMN_VENUE_NAME));
        examAllocatedLocation.setHallName(rs.getString(COLUMN_HALL_NAME));
        examAllocatedLocation.setTotalCapacity(rs.getInt(COLUMN_TOTAL_CAPACITY));
        examAllocatedLocation.setAddress(rs.getString(COLUMN_ADDRESS));
        examAllocatedLocation.setLocationUrl(rs.getString(COLUMN_LOCATION_URL));
        examAllocatedLocation.setFilledCapacity(rs.getInt(COLUMN_FILLED_CAPACITY));
        examAllocatedLocation.setSerialNo(rs.getString(COLUMN_SERIAL_NO));
        examAllocatedLocation.setExamId(this.examId);
        
        return examAllocatedLocation;
    }
}
