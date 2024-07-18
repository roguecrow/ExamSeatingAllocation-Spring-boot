package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.User;

public class FindUserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setRollNo(resultSet.getInt("roll_no"));
        user.setUsername(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRoleId(resultSet.getInt("role_id"));
        return user;
    }
}
