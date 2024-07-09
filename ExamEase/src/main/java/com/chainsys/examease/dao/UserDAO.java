package com.chainsys.examease.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.ExamAllocatedLocation;
import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;
import com.chainsys.examease.model.UserQuery;

@Repository
//public interface UserDAO {
//    int userRegistration(String fullName, String email, String password);
//    int createExam(String examName, String description, Date examDate, Date applicationStartDate,Date applicationEndDate);
//    int addLocationToExam(String city, String venueName, String hallName, int capacity, String address, String locationUrl, int examId);
//    List<Exam> getAllExams();
//    boolean findUser(String email, String password, User details, boolean isSignIn) throws Exception;
//    Exam getExamById(int examId);
//    List<String> getCityLocationsForExam(int examId);
//    int deleteExam(int examId);
//    List<Exam> findExam(String queryString);
//    List<Integer> getExamIdsForUser(int rollNo);
//    public boolean updateExamDetails(int examId, Date examDate, Date applicationStart, Date applicationEnd);
//    boolean updateExamDoc(User details);
//    boolean getExamDocById(User details);
//    public int addUserDetails(User details, String appId);
//    public List<ExamLocation> findExamLocationById(int examId);
//    public int getLastAllocatedSeatId(int locationId);
//    public int updateCapacity(int locationId, int newCapacity);
//    public int addExamSeating(int rollNo, int examId, int locationId, String serialNo, int allocatedSeat);
//    public int addUserDocument(int rollNo, byte[] passportPhoto, byte[] digitalSignature,byte[] qualificationDocuments);
//    public ExamAllocatedLocation getExamLocationDetails(int rollNo, int examId);
//    public String getExamDetails(String serialNo);
//}

public interface UserDAO {
    int userRegistration(String fullName, String email, String password);
    int createExam(String examName, String description, Date examDate, Date applicationStartDate, Date applicationEndDate);
    int addLocationToExam(String city, String venueName, String hallName, int capacity, String address, String locationUrl, int examId);
    boolean findUser(String email, String password, User details, boolean isSignIn) throws Exception;
    List<Integer> getExamIdsForUser(int rollNo);
    boolean updateExamDoc(User details);
    int addUserDetails(User details, String appId);
    int addUserDocument(int rollNo, byte[] passportPhoto, byte[] digitalSignature, byte[] qualificationDocuments);
	public boolean addUserQuery(int rollNo,String userName, String userEmail, String issueType, String message);
	public List<UserQuery> findUserQueries(int rollNo);
	public List<UserQuery> findAdminQueries();
	public boolean updateAdminReply(int queryId, String adminReply);
    List<User> findUsersByExamId(int examId);

}

