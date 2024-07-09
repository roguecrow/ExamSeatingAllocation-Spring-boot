package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.User;

public class UserFinderRowMapper implements RowMapper<User>{
	
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setName(rs.getString("name"));
        user.setRollNo(rs.getInt("roll_no"));
        user.setEmail(rs.getString("email"));
        //user.setp(rs.getInt("phone"));
        return user;
    }

}
