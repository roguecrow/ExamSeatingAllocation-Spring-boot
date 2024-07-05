package com.chainsys.examease.dao;

import java.sql.ResultSet;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.examease.mapper.ExamDetailsRowMapper;
import com.chainsys.examease.mapper.ExamLocationDetailsRowMapper;
import com.chainsys.examease.mapper.GetAllExamsRowMapper;
import com.chainsys.examease.mapper.GetUserDocRowMapper;
import com.chainsys.examease.mapper.LocationDetailsRowMapper;
import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.ExamAllocatedLocation;
import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Configuration
@Repository("examDAO")
public class ExamDAOImpl implements ExamDAO {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String COLUMN_ROLL_NO = "roll_no";
	private static final String COLUMN_EXAM_ID = "exam_id";
	private static final String COLUMN_EXAM_NAME = "exam_name";
	private static final String COLUMN_EXAM_DATE = "exam_date";
	private static final String COLUMN_LOCATION_ID = "location_id";
	private static final String COLUMN_CITY = "city";
	private static final String COLUMN_VENUE_NAME = "venue_name";
	private static final String COLUMN_HALL_NAME = "hall_name";
	private static final String COLUMN_ADDRESS = "address";
	private static final String COLUMN_LOCATION_URL = "location_url";
	private static final String COLUMN_ALLOCATED_SEAT = "allocated_seat";
	private static final String COLUMN_PASSPORT_SIZE_PHOTO = "passport_size_photo";
	private static final String COLUMN_DIGITAL_SIGNATURE = "digital_signature";
	private static final String COLUMN_QUALIFICATION_DOCUMENTS = "qualification_documents";
	
	
	public List<Exam> getAllExams() {
		String selectAllExams = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams";
		return jdbcTemplate.query(selectAllExams, new GetAllExamsRowMapper());
	}

	public Exam getExamById(int examId) {
		String getExamQuery = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams WHERE exam_id = ?";

		return jdbcTemplate.queryForObject(getExamQuery, new ExamDetailsRowMapper(), examId);
	}

	public List<String> getCityLocationsForExam(int examId) {
		String getCitiesQuery = "SELECT DISTINCT city FROM exam_locations WHERE exam_id = ?";
		return jdbcTemplate.query(getCitiesQuery, (resultSet, rowNum) -> resultSet.getString("city"), examId);
	}

	public int deleteExam(int examId) {
		String deleteExam = "DELETE FROM exams WHERE exam_id = ?";
		return jdbcTemplate.update(deleteExam, examId);
	}

	public List<Exam> findExam(String queryString) {
		String findExamQuery = "SELECT * FROM exams WHERE exam_name LIKE ?";
		return jdbcTemplate.query(findExamQuery, new ExamDetailsRowMapper(), "%" + queryString + "%");
	}

	public boolean updateExamDetails(int examId, Date examDate, Date applicationStart, Date applicationEnd) {
		String updateExamQuery = "UPDATE exams SET exam_date = ?, application_start_date = ?, application_end_date = ? WHERE exam_id = ?";

		int rowsUpdated = jdbcTemplate.update(updateExamQuery, new java.sql.Date(examDate.getTime()),
				new java.sql.Date(applicationStart.getTime()), new java.sql.Date(applicationEnd.getTime()), examId);

		return rowsUpdated > 0;
	}

	public boolean getExamDocById(User details) {
		String getExamQuery = "SELECT doc_id, passport_size_photo, digital_signature, qualification_documents FROM user_documents WHERE roll_no = ?";

		try {
			User result = jdbcTemplate.queryForObject(getExamQuery, new GetUserDocRowMapper(),
					new Object[] { details.getRollNo() });
			if (result != null) {
				details.setPassportSizePhoto(result.getPassportSizePhoto());
				details.setDigitalSignature(result.getDigitalSignature());
				details.setQualificationDocuments(result.getQualificationDocuments());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<ExamLocation> findExamLocationById(int examId) {
		String getExamLocations = "SELECT location_id, city, venue_name, hall_name, total_capacity, address, location_url, filled_capacity FROM exam_locations WHERE exam_id = ?";
		return jdbcTemplate.query(getExamLocations, new ExamLocationDetailsRowMapper(), new Object[] { examId });
	}

	public int getLastAllocatedSeatId(int locationId) {
	    String getSeatId = "SELECT MAX(allocated_seat) AS last_seat FROM exam_seating WHERE location_id = ?";
	    Integer lastSeat = jdbcTemplate.queryForObject(getSeatId, Integer.class, locationId);
	    return lastSeat != null ? lastSeat : 0;
	}

	public int updateCapacity(int locationId, int newCapacity) {
		String updateCapacityQuery = "UPDATE exam_locations SET filled_capacity = ? WHERE location_id = ?";
		return jdbcTemplate.update(updateCapacityQuery, newCapacity, locationId);
	}

	public int addExamSeating(int rollNo, int examId, int locationId, String serialNo, int allocatedSeat) {
        String addExamSeatingQuery = "INSERT INTO exam_seating (roll_no, exam_id, location_id, allocated_seat, serial_no) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(addExamSeatingQuery, rollNo, examId, locationId, allocatedSeat, serialNo);
    }

	public ExamAllocatedLocation getExamLocationDetails(int rollNo, int examId) {
        String query = "SELECT es.allocated_seat, es.serial_no, el.* " +
                       "FROM exam_seating es " +
                       "JOIN exam_locations el ON es.location_id = el.location_id " +
                       "WHERE es.roll_no = ? AND es.exam_id = ?";
        System.out.println(rollNo + " - " + examId);
        System.out.println("in examLocationDetails");
        return jdbcTemplate.queryForObject(query, new LocationDetailsRowMapper(examId), new Object[]{rollNo, examId});
    }

	 public String getExamDetails(String serialNo) {
	        String getExamDetails = "SELECT " +
	                "exams.exam_id, " +
	                "exams.exam_name, " +
	                "exams.exam_date, " +
	                "user_details.roll_no," +
	                "user_details.application_id, " +
	                "user_details.name, " +
	                "user_details.dob, " +
	                "user_details.address, " +
	                "user_details.gender, " +
	                "user_details.aadhar_number, " +
	                "user_details.qualification, " +
	                "user_details.native_city, " +
	                "user_details.state, " +
	                "user_documents.digital_signature, " +
	                "user_documents.passport_size_photo, " +
	                "user_documents.qualification_documents, " +
	                "exam_locations.location_id, " +
	                "exam_locations.city, " +
	                "exam_locations.venue_name, " +
	                "exam_locations.hall_name, " +
	                "exam_locations.address AS location_address, " +
	                "exam_locations.location_url, " +
	                "exam_seating.allocated_seat " +
	                "FROM exam_seating " +
	                "JOIN exams ON exam_seating.exam_id = exams.exam_id " +
	                "JOIN user_details ON CONCAT(exam_seating.exam_id, exam_seating.roll_no) = user_details.application_id " +
	                "JOIN exam_locations ON exam_seating.location_id = exam_locations.location_id " +
	                "JOIN user_documents ON exam_seating.roll_no = user_documents.roll_no " +
	                "WHERE exam_seating.serial_no = ?";

	        return jdbcTemplate.query(getExamDetails,(ResultSet rs) -> {
	            if (rs.next()) {
	                JsonObject examDetails = new JsonObject();
	                examDetails.addProperty(COLUMN_EXAM_ID, rs.getInt(COLUMN_EXAM_ID));
	                examDetails.addProperty(COLUMN_EXAM_NAME, rs.getString(COLUMN_EXAM_NAME));
	                examDetails.addProperty(COLUMN_EXAM_DATE, rs.getDate(COLUMN_EXAM_DATE).toString());
	                examDetails.addProperty(COLUMN_ROLL_NO, rs.getString(COLUMN_ROLL_NO));
	                examDetails.addProperty("application_id", rs.getString("application_id"));
	                examDetails.addProperty("name", rs.getString("name"));
	                examDetails.addProperty("dob", rs.getDate("dob").toString());
	                examDetails.addProperty(COLUMN_ADDRESS, rs.getString(COLUMN_ADDRESS));
	                examDetails.addProperty("gender", rs.getString("gender"));
	                examDetails.addProperty("aadhar_number", rs.getString("aadhar_number"));
	                examDetails.addProperty("qualification", rs.getString("qualification"));
	                examDetails.addProperty("native_city", rs.getString("native_city"));
	                examDetails.addProperty("state", rs.getString("state"));
	                
	                examDetails.addProperty(COLUMN_DIGITAL_SIGNATURE, encodeToBase64(rs.getBytes(COLUMN_DIGITAL_SIGNATURE)));
	                examDetails.addProperty(COLUMN_PASSPORT_SIZE_PHOTO, encodeToBase64(rs.getBytes(COLUMN_PASSPORT_SIZE_PHOTO)));
	                examDetails.addProperty(COLUMN_QUALIFICATION_DOCUMENTS, encodeToBase64(rs.getBytes(COLUMN_QUALIFICATION_DOCUMENTS)));

	                examDetails.addProperty(COLUMN_LOCATION_ID, rs.getInt(COLUMN_LOCATION_ID));
	                examDetails.addProperty(COLUMN_CITY, rs.getString(COLUMN_CITY));
	                examDetails.addProperty(COLUMN_VENUE_NAME, rs.getString(COLUMN_VENUE_NAME));
	                examDetails.addProperty(COLUMN_HALL_NAME, rs.getString(COLUMN_HALL_NAME));
	                examDetails.addProperty("location_address", rs.getString("location_address"));
	                examDetails.addProperty(COLUMN_LOCATION_URL, rs.getString(COLUMN_LOCATION_URL));
	                examDetails.addProperty(COLUMN_ALLOCATED_SEAT, rs.getInt(COLUMN_ALLOCATED_SEAT));
	                Gson gson = new Gson();
	                return gson.toJson(examDetails);
	            } else {
	                return null;
	            }
	        },new Object[]{serialNo});
	    }

	    private String encodeToBase64(byte[] data) {
	        return Base64.getEncoder().encodeToString(data);
	    }

}
