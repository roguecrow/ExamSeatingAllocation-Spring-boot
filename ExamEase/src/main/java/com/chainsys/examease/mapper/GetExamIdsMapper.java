package com.chainsys.examease.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GetExamIdsMapper implements RowMapper<Integer> {
	
    private static final String COLUMN_EXAM_ID = "exam_id";

	@Override
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt(COLUMN_EXAM_ID);
    }

}
