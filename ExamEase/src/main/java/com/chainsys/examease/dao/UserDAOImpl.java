package com.chainsys.examease.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.ExamAllocatedLocation;
import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;
import com.chainsys.examease.encrypt.PasswordEncryption;
import com.chainsys.examease.mapper.FindUserRowMapper;
import com.chainsys.examease.mapper.GetAllExamsRowMapper;
import com.chainsys.examease.mapper.GetExamIdsMapper;
import com.chainsys.examease.mapper.ExamDetailsRowMapper;
import com.chainsys.examease.mapper.GetUserDocRowMapper;
import com.chainsys.examease.mapper.ExamLocationDetailsRowMapper;
import com.chainsys.examease.mapper.LocationDetailsRowMapper;


@Configuration
@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PasswordEncryption passwordEncryption;

	private static final String COLUMN_ROLL_NO = "roll_no";
	private static final String COLUMN_EXAM_ID = "exam_id";
	private static final String COLUMN_EXAM_NAME = "exam_name";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_EXAM_DATE = "exam_date";
	private static final String COLUMN_APPLICATION_START_DATE = "application_start_date";
	private static final String COLUMN_APPLICATION_END_DATE = "application_end_date";
	private static final String COLUMN_LOCATION_ID = "location_id";
	private static final String COLUMN_CITY = "city";
	private static final String COLUMN_VENUE_NAME = "venue_name";
	private static final String COLUMN_HALL_NAME = "hall_name";
	private static final String COLUMN_TOTAL_CAPACITY = "total_capacity";
	private static final String COLUMN_ADDRESS = "address";
	private static final String COLUMN_LOCATION_URL = "location_url";
	private static final String COLUMN_FILLED_CAPACITY = "filled_capacity";
	private static final String COLUMN_ALLOCATED_SEAT = "allocated_seat";
	private static final String COLUMN_SERIAL_NO = "serial_no";
	private static final String COLUMN_PASSPORT_SIZE_PHOTO = "passport_size_photo";
	private static final String COLUMN_DIGITAL_SIGNATURE = "digital_signature";
	private static final String COLUMN_QUALIFICATION_DOCUMENTS = "qualification_documents";

	public int userRegistration(String fullName, String email, String password) {
		String addUser = "INSERT INTO user_credentials (full_name, email, password) VALUES (?, ?, ?)";
		Object[] userCredObj = { fullName, email, password };
		int noOfRows = jdbcTemplate.update(addUser, userCredObj);
		System.out.println("in save -" + noOfRows);
		return noOfRows;
	}

	public int createExam(String examName, String description, Date examDate, Date applicationStartDate,
			Date applicationEndDate) {
		System.out.println("in create Exam method");
		String addExam = "INSERT INTO exams (exam_name, description, exam_date, application_start_date, application_end_date) VALUES (?, ?, ?, ?, ?)";
		Object[] addExamObj = { examName, description, examDate, applicationStartDate, applicationEndDate };
		int noOfRows = jdbcTemplate.update(addExam, addExamObj);
		System.out.println("in save -" + noOfRows);
		System.out.println("examId -" + getLastInsertedExamId());
		return getLastInsertedExamId();
	}

	public int getLastInsertedExamId() {
		String query = "SELECT exam_id FROM exams ORDER BY exam_id DESC LIMIT 1";
		return jdbcTemplate.queryForObject(query, Integer.class);
	}

	public int addLocationToExam(String city, String venueName, String hallName, int capacity, String address,
			String locationUrl, int examId) {
		String addLocation = "INSERT INTO exam_locations (city, venue_name, hall_name, total_capacity, address, location_url, exam_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
		Object[] addLocationObj = { city, venueName, hallName, capacity, address, locationUrl, examId };
		return jdbcTemplate.update(addLocation, addLocationObj);
	}

	public List<Exam> getAllExams() {
		String selectAllExams = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams";
		return jdbcTemplate.query(selectAllExams, new GetAllExamsRowMapper());
	}

	public boolean findUser(String email, String password, User details, boolean isSignIn) throws Exception {
		String findUser = "SELECT roll_no, full_name, email, password, role_id FROM user_credentials WHERE email = ? AND is_active = ?";
		List<User> users = jdbcTemplate.query(findUser, new FindUserRowMapper(), new Object[] { email, true });

		if (!users.isEmpty()) {
			User user = users.get(0);
			System.out.println("From findUer - " + user.getPassword());
			boolean passwordMatches = passwordEncryption.decrypt(user.getPassword()).equals(password);
			boolean emailMatches = email.equals(user.getEmail());

			details.setUsername(user.getUsername());
			details.setRollNo(user.getRollNo());
			details.setRoleId(user.getRoleId());

			return (passwordMatches && emailMatches && isSignIn) || (emailMatches && !isSignIn);
		}
		return false;
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

//	public List<ExamDetails> findExam(String examName) throws SQLException {
//	    ArrayList<ExamDetails> searchedExams = new ArrayList<>();
//	    String searchQuery = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams WHERE exam_name LIKE ?";
//	    try(PreparedStatement preparedStatement = connect.prepareStatement(searchQuery)) {
//		    preparedStatement.setString(1, "%" + examName + "%"); 
//		    ResultSet resultSet = preparedStatement.executeQuery();
//		    while (resultSet.next()) {
//		        int examId = resultSet.getInt(COLUMN_EXAM_ID);
//		        String name = resultSet.getString(COLUMN_EXAM_NAME);
//		        String description = resultSet.getString(COLUMN_DESCRIPTION);
//		        Date examDate = resultSet.getDate(COLUMN_EXAM_DATE);
//		        Timestamp applicationStartDate = resultSet.getTimestamp(COLUMN_APPLICATION_START_DATE);
//		        Timestamp applicationEndDate = resultSet.getTimestamp(COLUMN_APPLICATION_END_DATE);
//		        ExamDetails exam = new ExamDetails(examId, name, description, examDate, applicationStartDate, applicationEndDate);
//		        searchedExams.add(exam);
//		    }
//		    return searchedExams;
//	    }
//	}

	public boolean updateExamDetails(int examId, Date examDate, Date applicationStart, Date applicationEnd) {
		String updateExamQuery = "UPDATE exams SET exam_date = ?, application_start_date = ?, application_end_date = ? WHERE exam_id = ?";

		int rowsUpdated = jdbcTemplate.update(updateExamQuery, new java.sql.Date(examDate.getTime()),
				new java.sql.Date(applicationStart.getTime()), new java.sql.Date(applicationEnd.getTime()), examId);

		return rowsUpdated > 0;
	}

	public int addUserDetails(User details, String appId) {
		String addUserDetailsQuery = "INSERT INTO user_details (roll_no, name, dob, qualification, gender, city_preference_1, city_preference_2, city_preference_3, address, native_city, state, aadhar_number, application_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		return jdbcTemplate.update(addUserDetailsQuery, details.getRollNo(), details.getName(),
				new Date(details.getDob().getTime()), details.getQualification(), String.valueOf(details.getGender()),
				details.getCityPreference1(), details.getCityPreference2(), details.getCityPreference3(),
				details.getAddress(), details.getNativeCity(), details.getState(), details.getAadharNumber(), appId);
	}

	public int addUserDocument(int rollNo, byte[] passportPhoto, byte[] digitalSignature,
			byte[] qualificationDocuments) {
		String addUserDocumentQuery = "INSERT INTO user_documents (roll_no, passport_size_photo, digital_signature, qualification_documents) VALUES (?, ?, ?, ?)";

		return jdbcTemplate.update(addUserDocumentQuery, rollNo, passportPhoto, digitalSignature,
				qualificationDocuments);
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
	
	public int addExamSeating(int rollNo, int examId, int locationId, String serialNo, int allocatedSeat) {
        String addExamSeatingQuery = "INSERT INTO exam_seating (roll_no, exam_id, location_id, allocated_seat, serial_no) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(addExamSeatingQuery, rollNo, examId, locationId, allocatedSeat, serialNo);
    }

	public int updateCapacity(int locationId, int newCapacity) {
		String updateCapacityQuery = "UPDATE exam_locations SET filled_capacity = ? WHERE location_id = ?";
		return jdbcTemplate.update(updateCapacityQuery, newCapacity, locationId);
	}

	public List<Integer> getExamIdsForUser(int rollNo) {
		String query = "SELECT exam_id FROM exam_seating WHERE roll_no = ?";
		return jdbcTemplate.query(query, new GetExamIdsMapper(), new Object[] { rollNo });
	}

	public List<Exam> findExam(String queryString) {
		String findExamQuery = "SELECT * FROM exams WHERE exam_name LIKE ?";
		return jdbcTemplate.query(findExamQuery, new ExamDetailsRowMapper(), "%" + queryString + "%");
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

	public boolean updateExamDoc(User details) {
		String updateQuery = "UPDATE user_documents SET passport_size_photo = ?, digital_signature = ?, qualification_documents = ? WHERE roll_no = ?";

		int rowsAffected = jdbcTemplate.update(updateQuery, details.getPassportSizePhoto(),
				details.getDigitalSignature(), details.getQualificationDocuments(), details.getRollNo());

		return rowsAffected > 0;
	}
    
//    public String getExamDetails(String serialNo) throws SQLException {
//    	 
//        String getExamDetails = "SELECT " +
//                "exams.exam_id, " +
//                "exams.exam_name, " +
//                "exams.exam_date, " +
//                "user_details.roll_no," +
//                "user_details.application_id, " +
//                "user_details.name, " +
//                "user_details.dob, " +
//                "user_details.address, " +
//                "user_details.gender, " +
//                "user_details.aadhar_number, " +
//                "user_details.qualification, " +
//                "user_details.native_city, " +
//                "user_details.state, " +
//                "user_documents.digital_signature, " +
//                "user_documents.passport_size_photo, " +
//                "user_documents.qualification_documents, " +
//                "exam_locations.location_id, " +
//                "exam_locations.city, " +
//                "exam_locations.venue_name, " +
//                "exam_locations.hall_name, " +
//                "exam_locations.address AS location_address, " +
//                "exam_locations.location_url, " +
//                "exam_seating.allocated_seat " +
//                "FROM exam_seating " +
//                "JOIN exams ON exam_seating.exam_id = exams.exam_id " +
//                "JOIN user_details ON CONCAT(exam_seating.exam_id, exam_seating.roll_no) = user_details.application_id " +
//                "JOIN exam_locations ON exam_seating.location_id = exam_locations.location_id " +
//                "JOIN user_documents ON exam_seating.roll_no = user_documents.roll_no " +
//                "WHERE exam_seating.serial_no = ?";
//        
//        
//        try (PreparedStatement preparedStatement = connect.prepareStatement(getExamDetails)) {
//            preparedStatement.setString(1, serialNo);
//            
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//            	
//                if (resultSet.next()) {
//                    JsonObject examDetails = new JsonObject();
//                    examDetails.addProperty(COLUMN_EXAM_ID, resultSet.getInt(COLUMN_EXAM_ID));
//                    examDetails.addProperty(COLUMN_EXAM_NAME, resultSet.getString(COLUMN_EXAM_NAME));
//                    examDetails.addProperty(COLUMN_EXAM_DATE, resultSet.getDate(COLUMN_EXAM_DATE).toString());
//                    examDetails.addProperty(COLUMN_ROLL_NO, resultSet.getString(COLUMN_ROLL_NO));
//                    examDetails.addProperty("application_id", resultSet.getString("application_id"));
//                    examDetails.addProperty("name", resultSet.getString("name"));
//                    examDetails.addProperty("dob", resultSet.getDate("dob").toString());
//                    examDetails.addProperty(COLUMN_ADDRESS, resultSet.getString(COLUMN_ADDRESS));
//                    examDetails.addProperty("gender", resultSet.getString("gender"));
//                    examDetails.addProperty("aadhar_number", resultSet.getString("aadhar_number"));
//                    examDetails.addProperty("qualification", resultSet.getString("qualification"));
//                    examDetails.addProperty("native_city", resultSet.getString("native_city"));
//                    examDetails.addProperty("state", resultSet.getString("state"));
//                    
//                    examDetails.addProperty(COLUMN_DIGITAL_SIGNATURE, encodeToBase64(resultSet.getBytes(COLUMN_DIGITAL_SIGNATURE)));
//                    examDetails.addProperty(COLUMN_PASSPORT_SIZE_PHOTO, encodeToBase64(resultSet.getBytes(COLUMN_PASSPORT_SIZE_PHOTO)));
//                    examDetails.addProperty(COLUMN_QUALIFICATION_DOCUMENTS, encodeToBase64(resultSet.getBytes(COLUMN_QUALIFICATION_DOCUMENTS)));
//
//                    examDetails.addProperty(COLUMN_LOCATION_ID, resultSet.getInt(COLUMN_LOCATION_ID));
//                    examDetails.addProperty(COLUMN_CITY, resultSet.getString(COLUMN_CITY));
//                    examDetails.addProperty(COLUMN_VENUE_NAME, resultSet.getString(COLUMN_VENUE_NAME));
//                    examDetails.addProperty(COLUMN_HALL_NAME, resultSet.getString(COLUMN_HALL_NAME));
//                    examDetails.addProperty("location_address", resultSet.getString("location_address"));
//                    examDetails.addProperty(COLUMN_LOCATION_URL, resultSet.getString(COLUMN_LOCATION_URL));
//                    examDetails.addProperty(COLUMN_ALLOCATED_SEAT, resultSet.getInt(COLUMN_ALLOCATED_SEAT));
//                    Gson gson = new Gson();
//                    return gson.toJson(examDetails);
//                } else {
//                    return null;
//                }
//            }
//        } catch (SQLException e) {
//            throw new SQLException("Error retrieving exam details", e);
//        }
//    }
//
//    private String encodeToBase64(byte[] data) {
//        return java.util.Base64.getEncoder().encodeToString(data);
//    }

}
