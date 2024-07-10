package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.User;

public class UserFinderRowMapper implements RowMapper<User>{
	
	@Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setRollNo(rs.getInt("roll_no"));
        user.setName(rs.getString("name"));
        user.setDob(rs.getDate("dob"));
        user.setQualification(rs.getString("qualification"));
        user.setGender(rs.getString("gender").charAt(0));
        user.setAddress(rs.getString("address"));
        user.setNativeCity(rs.getString("native_city"));
        user.setState(rs.getString("state"));
        user.setAadharNumber(rs.getString("aadhar_number"));
        return user;
    }

}
