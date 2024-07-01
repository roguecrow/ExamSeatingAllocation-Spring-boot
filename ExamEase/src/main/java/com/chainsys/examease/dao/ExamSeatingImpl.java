package com.chainsys.examease.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.User;
import com.chainsys.examease.encrypt.PasswordEncryption;
import com.chainsys.examease.mapper.FindUserRowMapper;
import com.chainsys.examease.mapper.GetAllExamsRowMapper;
import com.chainsys.examease.mapper.ExamIdMapper;


@Configuration
@Service
public class ExamSeatingImpl {
	
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
		Object[] userCredObj = { fullName,email,password };
		int noOfRows = jdbcTemplate.update(addUser, userCredObj);
		System.out.println("in save -" + noOfRows);
		return noOfRows;
	}

	public int createExam(String examName, String description, Date examDate, Date applicationStartDate,
			Date applicationEndDate) {
		String addExam = "INSERT INTO exams (exam_name, description, exam_date, application_start_date, application_end_date) VALUES (?, ?, ?, ?, ?)";
	    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		Object[] addExamObj = { examName,description,examDate,applicationStartDate,applicationEndDate };
		int noOfRows = jdbcTemplate.update(addExam, addExamObj);
		int examId = -1;
		System.out.println("in save -" + noOfRows);
		examId = keyHolder.getKey().intValue();
		return examId;
	}

//	public int addLocationToExam(String city, String venueName, String hallName, int capacity, String address,
//			String locationUrl, int examId) throws SQLException {
//		String addLocation = "INSERT INTO exam_locations (city, venue_name, hall_name, total_capacity, address, location_url, exam_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
//		try(PreparedStatement prepareStatement = connect.prepareStatement(addLocation)) {
//			prepareStatement.setString(1, city);
//			prepareStatement.setString(2, venueName);
//			prepareStatement.setString(3, hallName);
//			prepareStatement.setInt(4, capacity);
//			prepareStatement.setString(5, address);
//			prepareStatement.setString(6, locationUrl);
//			prepareStatement.setInt(7, examId);
//
//			return prepareStatement.executeUpdate();
//		}
//	}
//
    public List<Exam> getAllExams() {
    	String selectAllExams = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams";
        return jdbcTemplate.query(selectAllExams, new GetAllExamsRowMapper());
    }
	
	public boolean findUser(String email, String password, User details, boolean isSignIn) throws Exception {
	    String findUser = "SELECT roll_no, full_name, email, password, role_id FROM user_credentials WHERE email = ? AND is_active = ?";
        List<User> users = jdbcTemplate.query(findUser,new FindUserRowMapper(), new Object[]{email, true} );
        
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
//
//
//	public ExamDetails getExamById(int examId) throws SQLException {
//		ExamDetails exam = null; 
//
//		String getExamQuery = "SELECT exam_id, exam_name, description, exam_date, application_start_date, application_end_date FROM exams WHERE exam_id = ?";
//		try (PreparedStatement preparedStatement = connect.prepareStatement(getExamQuery)) {
//			preparedStatement.setInt(1, examId);
//			try (ResultSet resultSet = preparedStatement.executeQuery()) {
//				if (resultSet.next()) {
//					int id = resultSet.getInt(COLUMN_EXAM_ID);
//					String examName = resultSet.getString(COLUMN_EXAM_NAME);
//					String description = resultSet.getString(COLUMN_DESCRIPTION);
//					Date examDate = resultSet.getDate(COLUMN_EXAM_DATE);
//					Date applicationStartDate = resultSet.getDate(COLUMN_APPLICATION_START_DATE);
//					Date applicationEndDate = resultSet.getDate(COLUMN_APPLICATION_END_DATE);
//					exam = new ExamDetails(id, examName, description, examDate, applicationStartDate,
//							applicationEndDate);
//				}
//			}
//		}
//		return exam;
//	}
//	
//	public List<String> cityLocationsForExam(int examId) throws SQLException {
//	    List<String> cities = new ArrayList<>();
//
//	    String getCitiesQuery = "SELECT DISTINCT city FROM exam_locations WHERE exam_id = ?";
//
//	    try (PreparedStatement citiesStatement = connect.prepareStatement(getCitiesQuery)) {
//	        citiesStatement.setInt(1, examId);
//	        
//	        try (ResultSet citiesResultSet = citiesStatement.executeQuery()) {
//	            while (citiesResultSet.next()) {
//	                String city = citiesResultSet.getString("city");
//	                cities.add(city);
//	            }
//	        }
//	    }
//	    return cities;
//	}
//
//
//	public int deleteExam(int examId) throws SQLException {
//		String deleteExam = "DELETE FROM exams WHERE exam_id = ?";
//		try(PreparedStatement preparedStatement = connect.prepareStatement(deleteExam)) {
//			preparedStatement.setInt(1, examId);			
//			return preparedStatement.executeUpdate();
//		}
//	}
//	
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
//	
//	public boolean updateExamDetails(int examId,Date examDate,Date applicationStart,Date applicationEnd) throws SQLException {
//	    String updateExamQuery = "UPDATE exams SET exam_date = ?, application_start_date = ?, application_end_date = ? WHERE exam_id = ?";
//	    boolean rowUpdated = false;
//	    
//	    try (PreparedStatement preparedStatement = connect.prepareStatement(updateExamQuery)) {
//	        preparedStatement.setDate(1, new java.sql.Date(examDate.getTime()));
//	        preparedStatement.setDate(2, new java.sql.Date(applicationStart.getTime()));
//	        preparedStatement.setDate(3, new java.sql.Date(applicationEnd.getTime()));
//	        preparedStatement.setInt(4,examId);
//	        
//	        rowUpdated = preparedStatement.executeUpdate() > 0;
//	    }
//	    
//	    return rowUpdated;
//	}
//	
//	
//	public int addUserDetails(UserDetails details,String appId) throws SQLException {
//		String addUserDetailsQuery = "INSERT INTO user_details (roll_no, name, dob, qualification, gender, city_preference_1, city_preference_2, city_preference_3, address, native_city, state, aadhar_number,application_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		try(PreparedStatement preparedStatement = connect.prepareStatement(addUserDetailsQuery)) {
//			preparedStatement.setInt(1, details.getRollNo());
//			preparedStatement.setString(2, details.getName());
//			preparedStatement.setDate(3, new java.sql.Date(details.getDob().getTime()));
//			preparedStatement.setString(4, details.getQualification());
//			preparedStatement.setString(5, String.valueOf(details.getGender()));
//			preparedStatement.setString(6, details.getCityPreference1());
//			preparedStatement.setString(7, details.getCityPreference2());
//			preparedStatement.setString(8, details.getCityPreference3());
//			preparedStatement.setString(9, details.getAddress());
//			preparedStatement.setString(10, details.getNativeCity());
//			preparedStatement.setString(11, details.getState());
//			preparedStatement.setString(12, details.getAadharNumber());
//			preparedStatement.setString(13, appId);
//
//			return preparedStatement.executeUpdate();
//		}
//
//	}
//	
//	public int addUserDocument(int rollNo, byte[] passportPhoto, byte[] digitalSignature, byte[] qualificationDocuments) throws SQLException {
//		String addUserDocumentQuery = "INSERT INTO user_documents (roll_no, passport_size_photo, digital_signature, qualification_documents) VALUES (?, ?, ?, ?)";
//		try(PreparedStatement preparedStatement = connect.prepareStatement(addUserDocumentQuery)) {
//			preparedStatement.setInt(1, rollNo);
//			preparedStatement.setBytes(2, passportPhoto);
//			preparedStatement.setBytes(3, digitalSignature);
//			preparedStatement.setBytes(4, qualificationDocuments);
//
//			return  preparedStatement.executeUpdate();
//		}
//	}
//	
//	public List<ExamLocationDetails> findExamLocationById(int examId) throws SQLException {
//		List<ExamLocationDetails> examLocationList = new ArrayList<>();
//	    String getExamLocations = "SELECT location_id, city, venue_name, hall_name, total_capacity, address, location_url, filled_capacity FROM exam_locations WHERE exam_id = ?";
//	    try(PreparedStatement preparedStatement = connect.prepareStatement(getExamLocations)) {
//	        preparedStatement.setInt(1, examId);
//	        ResultSet resultSet = preparedStatement.executeQuery();
//	        while (resultSet.next()) {
//	            ExamLocationDetails location = new ExamLocationDetails();
//	            location.setLocationId(resultSet.getInt(COLUMN_LOCATION_ID));
//	            location.setCity(resultSet.getString(COLUMN_CITY));
//	            location.setVenueName(resultSet.getString(COLUMN_VENUE_NAME));
//	            location.setHallName(resultSet.getString(COLUMN_HALL_NAME));
//	            location.setCapacity(resultSet.getInt(COLUMN_TOTAL_CAPACITY));
//	            location.setAddress(resultSet.getString(COLUMN_ADDRESS));
//	            location.setLocationUrl(resultSet.getString(COLUMN_LOCATION_URL));
//	            location.setFilledCapacity(resultSet.getInt(COLUMN_FILLED_CAPACITY));
//
//	            examLocationList.add(location);
//	        }
//	        return examLocationList;	
//	    }
//	}
//	
//    public  int getLastAllocatedSeatId(int locationId)throws SQLException {
//        int lastAllocatedSeatId = 0;
//        String getSeatId = "SELECT MAX(allocated_seat) AS last_seat FROM exam_seating WHERE location_id = ?";
//        try (PreparedStatement preparedStatement = connect.prepareStatement(getSeatId)) {
//            preparedStatement.setInt(1, locationId);
//            
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    lastAllocatedSeatId = resultSet.getInt("last_seat");
//                }
//            }
//        }
//        return lastAllocatedSeatId;
//    }
//    
//    public int addExamSeating(int rollNo, int examId, int locationId, String serialNo, int allocatedSeat) throws SQLException {
//        String addExamSeatingQuery = "INSERT INTO exam_seating (roll_no, exam_id, location_id, allocated_seat, serial_no) VALUES (?, ?, ?, ?, ?)";
//        try (PreparedStatement preparedStatement = connect.prepareStatement(addExamSeatingQuery)) {
//        	preparedStatement.setInt(1, rollNo);
//            preparedStatement.setInt(2, examId);
//            preparedStatement.setInt(3, locationId);
//            preparedStatement.setInt(4, allocatedSeat);
//            preparedStatement.setString(5, serialNo);
//            
//            return preparedStatement.executeUpdate();
//        }
//       
//    }
//    
//    public int updateCapacity(int locationId, int newCapacity) throws SQLException {
//        String updateCapacityQuery = "UPDATE exam_locations SET filled_capacity = ? WHERE location_id = ?";
//        try(PreparedStatement preparedStatement = connect.prepareStatement(updateCapacityQuery)) {
//        	 preparedStatement.setInt(1, newCapacity);
//             preparedStatement.setInt(2, locationId);
//
//             return preparedStatement.executeUpdate();
//        }
//    }
//    
    public List<Integer> getExamIdsForRollNo(int rollNo) {
        String query = "SELECT exam_id FROM exam_seating WHERE roll_no = ?";
        return jdbcTemplate.query(query,new ExamIdMapper(), new Object[]{rollNo});
    }
//
//    public LocationDetails getExamLocationDetails(int rollNo , int examId) throws SQLException {
//    	 String query = "SELECT es.allocated_seat,es.serial_no, el.* " +
//                 "FROM exam_seating es " +
//                 "JOIN exam_locations el ON es.location_id = el.location_id " +
//                 "WHERE es.roll_no = ? AND es.exam_id = ?";
//        
//        try(PreparedStatement preparedStatement = connect.prepareStatement(query)) {
//            preparedStatement.setInt(1, rollNo);
//            preparedStatement.setInt(2, examId);
//            
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int allocatedSeat = resultSet.getInt(COLUMN_ALLOCATED_SEAT);
//                int locationId = resultSet.getInt(COLUMN_LOCATION_ID);
//                String city = resultSet.getString(COLUMN_CITY);
//                String venueName = resultSet.getString(COLUMN_VENUE_NAME);
//                String hallName = resultSet.getString(COLUMN_HALL_NAME);
//                int totalCapacity = resultSet.getInt(COLUMN_TOTAL_CAPACITY);
//                String address = resultSet.getString(COLUMN_ADDRESS);
//                String locationUrl = resultSet.getString(COLUMN_LOCATION_URL);
//                int filledCapacity = resultSet.getInt(COLUMN_FILLED_CAPACITY);
//                String serialNo = resultSet.getString(COLUMN_SERIAL_NO);
//
//                return new LocationDetails(allocatedSeat, locationId, city, venueName, hallName, totalCapacity, address, locationUrl, examId, filledCapacity, serialNo);
//            } else {
//                return null; 
//            }
//        }
//    }
//    
//    public boolean getExamDocById(UserDetails details) throws SQLException {
//        String getExamQuery = "SELECT doc_id, passport_size_photo, digital_signature,qualification_documents FROM user_documents WHERE roll_no = ?";
//        try (PreparedStatement preparedStatement = connect.prepareStatement(getExamQuery)) {
//            preparedStatement.setInt(1, details.getRollNo());
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    try (InputStream passportPhoto = resultSet.getBlob(COLUMN_PASSPORT_SIZE_PHOTO).getBinaryStream();
//                         InputStream digitalSignature = resultSet.getBlob(COLUMN_DIGITAL_SIGNATURE).getBinaryStream();
//                         InputStream qualificationDocuments = resultSet.getBlob(COLUMN_QUALIFICATION_DOCUMENTS).getBinaryStream()) {
//
//                        details.setDigitalSignature(passportPhoto.readAllBytes());
//                        details.setPassportSizePhoto(digitalSignature.readAllBytes());
//                        details.setQualificationDocuments(qualificationDocuments.readAllBytes());
//                    } catch (IOException e) {
//                        throw new SQLException("Error reading BLOB data", e);
//                    }
//                    return true; 
//                }
//            }
//        }
//        return false; 
//    }
//    
//    public boolean updateExamDoc(UserDetails details) throws SQLException {
//        String updateQuery = "UPDATE user_documents SET passport_size_photo = ?, digital_signature = ?, qualification_documents = ? WHERE roll_no = ?";
//        
//        try (PreparedStatement preparedStatement = connect.prepareStatement(updateQuery)) {
//            preparedStatement.setBytes(1, details.getPassportSizePhoto());
//            preparedStatement.setBytes(2, details.getDigitalSignature());
//            preparedStatement.setBytes(3, details.getQualificationDocuments());
//            preparedStatement.setInt(4, details.getRollNo());
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            throw new SQLException("Error updating user documents", e);
//        }
//    }
//    
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
