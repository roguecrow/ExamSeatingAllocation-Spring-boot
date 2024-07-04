package com.chainsys.examease.mapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.User;

public class GetUserDocRowMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User userDetails = new User();
        try {
            userDetails.setPassportSizePhoto(rs.getBlob("passport_size_photo").getBinaryStream().readAllBytes());
            userDetails.setDigitalSignature(rs.getBlob("digital_signature").getBinaryStream().readAllBytes());
            userDetails.setQualificationDocuments(rs.getBlob("qualification_documents").getBinaryStream().readAllBytes());
        } catch (IOException e) {
            throw new SQLException("Error reading BLOB data", e);
        }
        return userDetails;
    }

}
