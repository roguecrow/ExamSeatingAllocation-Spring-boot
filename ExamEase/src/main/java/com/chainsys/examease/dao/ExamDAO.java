package com.chainsys.examease.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.ExamAllocatedLocation;
import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;

@Repository
public interface ExamDAO {
	    List<Exam> getAllExams();
	    Exam getExamById(int examId);
	    List<String> getCityLocationsForExam(int examId);
	    int deleteExam(int examId);
	    List<Exam> findExam(String queryString);
	    public boolean updateExamDetails(int examId, Date examDate, Date applicationStart, Date applicationEnd);
	    boolean getExamDocById(User details);
	    public List<ExamLocation> findExamLocationById(int examId);
	    public int getLastAllocatedSeatId(int locationId);
	    public int updateCapacity(int locationId, int newCapacity);
	    public int addExamSeating(int rollNo, int examId, int locationId, String serialNo, int allocatedSeat);
	    public ExamAllocatedLocation getExamLocationDetails(int rollNo, int examId);
	    public String getExamDetails(String serialNo);
}
