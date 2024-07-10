package com.chainsys.examease.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.chainsys.examease.model.User;
import com.chainsys.examease.model.UserQuery;

@Repository
public interface UserDAO {
    int userRegistration(String fullName, String email, String password);
    int createExam(String examName, String description, Date examDate, Date applicationStartDate, Date applicationEndDate);
    int addLocationToExam(String city, String venueName, String hallName, int capacity, String address, String locationUrl, int examId);
    boolean findUser(String email, String password, User details, boolean isSignIn) throws Exception;
    List<Integer> getExamIdsForUser(int rollNo);
    boolean updateExamDoc(User details);
    int addUserDetails(User details, String appId, int examId);
    int addUserDocument(int rollNo, byte[] passportPhoto, byte[] digitalSignature, byte[] qualificationDocuments);
	public boolean addUserQuery(int rollNo,String userName, String userEmail, String issueType, String message);
	public List<UserQuery> findUserQueries(int rollNo);
	public List<UserQuery> findAdminQueries();
	public boolean updateAdminReply(int queryId, String adminReply);
    List<User> findUsersByExamId(int examId);

}

