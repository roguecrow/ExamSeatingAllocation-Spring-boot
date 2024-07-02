package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.examease.model.Exam;

public class ExamDetailsRowMapper implements RowMapper<Exam> {
	
	@Override
    public Exam mapRow(ResultSet resultSet, int rowNum) throws SQLException  {
        int id = resultSet.getInt("exam_id");
        String examName = resultSet.getString("exam_name");
        String description = resultSet.getString("description");
        Date examDate = resultSet.getDate("exam_date");
        Date applicationStartDate = resultSet.getDate("application_start_date");
        Date applicationEndDate = resultSet.getDate("application_end_date");

        return new Exam(id, examName, description, examDate, applicationStartDate, applicationEndDate);
    }
}
