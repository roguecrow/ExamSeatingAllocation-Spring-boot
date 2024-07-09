package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.UserQuery;

public class UserQueryRowMapper implements RowMapper<UserQuery> {
    @Override
    public UserQuery mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserQuery userQuery = new UserQuery();
        userQuery.setId(rs.getInt("query_id"));
        userQuery.setRollNo(rs.getInt("roll_no"));
        userQuery.setUserName(rs.getString("name"));
        userQuery.setUserEmail(rs.getString("email"));
        userQuery.setIssueType(rs.getString("issue_type"));
        userQuery.setMessage(rs.getString("message"));
        userQuery.setReply(rs.getString("admin_reply"));
        userQuery.setClosed(rs.getBoolean("is_closed"));

        return userQuery;
    }
}
