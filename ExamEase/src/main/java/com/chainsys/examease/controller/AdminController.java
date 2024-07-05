package com.chainsys.examease.controller;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.chainsys.examease.dao.ExamDAO;
import com.chainsys.examease.dao.UserDAO;
import com.chainsys.examease.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	private static final String LOCATIONS_PREFIX = "locations[";
    private static final String HOME_PAGE_ERROR = "homePage.jsp?message=errorAddingExam&type=error";
    private static final String HOME_PAGE_SUCCESS = "homePage.jsp?message=examAddedSuccessfully&type=success";
   
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	User user;
	
	@Autowired
	ExamDAO examDAO;

	
    @PostMapping("/updateExamDetails")
    public String updateExamDetails(
            @RequestParam("examId") String examIdStr,
            @RequestParam("examDate") String examDate,
            @RequestParam("applicationStart") String applicationStart,
            @RequestParam("applicationEnd") String applicationEnd,
            Model model) {

        int examId = Integer.parseInt(examIdStr); 
        examDAO.updateExamDetails(examId, Date.valueOf(examDate), Date.valueOf(applicationStart), Date.valueOf(applicationEnd));
        return "redirect:/homePage.jsp?message=examUpdatedSuccessfully&type=success";
    }
    
    @PostMapping("/AddExam")
    public ModelAndView addExam(@RequestParam("examName") String examName,
                                @RequestParam("examDescription") String examDescription,
                                @RequestParam("examDate") Date examDate,
                                @RequestParam("locationIndex") int locationIndex,
                                @RequestParam("applicationStart") Date applicationStart,
                                @RequestParam("applicationEnd") Date applicationEnd,
                                HttpServletRequest request,HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("in addExam Controller");

        int examId = userDAO.createExam(examName, examDescription, examDate, applicationStart, applicationEnd);

        for (int i = 0; i < locationIndex; i++) {
            if (!processLocation(request, examId, i)) {
                modelAndView.setViewName(HOME_PAGE_ERROR);
                return modelAndView;
            }
        }
        session.setAttribute("exams", examDAO.getAllExams());
        modelAndView.setViewName(HOME_PAGE_SUCCESS);
        return modelAndView;
    }

    private boolean processLocation(HttpServletRequest request, int examId, int i) {
        String city = request.getParameter(LOCATIONS_PREFIX + i + "].city");
        String venueName = request.getParameter(LOCATIONS_PREFIX + i + "].venueName");
        String hallName = request.getParameter(LOCATIONS_PREFIX + i + "].hallName");
        int capacity = Integer.parseInt(request.getParameter(LOCATIONS_PREFIX + i + "].capacity"));
        String address = request.getParameter(LOCATIONS_PREFIX + i + "].address");
        String locationUrl = request.getParameter(LOCATIONS_PREFIX + i + "].locationUrl");

        int affectedRows = userDAO.addLocationToExam(city, venueName, hallName, capacity, address, locationUrl, examId);

        if (affectedRows != 1) {
        	examDAO.deleteExam(examId);
            return false;
        }
        return true;
    }
}








