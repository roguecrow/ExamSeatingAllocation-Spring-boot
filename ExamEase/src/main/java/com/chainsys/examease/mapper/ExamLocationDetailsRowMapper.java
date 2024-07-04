package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.chainsys.examease.model.ExamLocation;

public class ExamLocationDetailsRowMapper implements RowMapper<ExamLocation> {
    @Override
    public ExamLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExamLocation location = new ExamLocation();
        location.setLocationId(rs.getInt("location_id"));
        location.setCity(rs.getString("city"));
        location.setVenueName(rs.getString("venue_name"));
        location.setHallName(rs.getString("hall_name"));
        location.setCapacity(rs.getInt("total_capacity"));
        location.setAddress(rs.getString("address"));
        location.setLocationUrl(rs.getString("location_url"));
        location.setFilledCapacity(rs.getInt("filled_capacity"));
        return location;
    }
}
