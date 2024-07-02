package com.chainsys.examease.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.chainsys.examease.dao.ExamSeatingImpl;
import com.chainsys.examease.encrypt.PasswordEncryption;
import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.User;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	@Autowired
	ExamSeatingImpl examSeatingImpl;
	
	@Autowired
	PasswordEncryption passwordEncryption;
	
	@Autowired
	User user;
	

	
	@RequestMapping("/")
	public String home() {
		System.out.println("in landing page");
		return "landingPage.jsp";
	}
 
		@PostMapping("/login")
		public String userLogin(@RequestParam("email") String email, @RequestParam("password") String password,
				HttpSession session, Model model) throws Exception {
			if (examSeatingImpl.findUser(email, password, user, true)) {
				session.setAttribute("userDetails", user);
				session.setAttribute("exams", examSeatingImpl.getAllExams());
				//session.setAttribute("appliedExams", examSeatingImpl.getExamIdsForUser(user.getRollNo()));
				return "redirect:/homePage.jsp";
			} else {
				model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
				return "login.jsp";
			}
		}
 
	@PostMapping("/register")
	public String userRegister(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, HttpSession session, Model model) throws Exception {

		if (examSeatingImpl.findUser(email, password, user, false)) {
			model.addAttribute("errorMessage", "Account already exists.");
			return "registerPage.jsp";
		} else {
			examSeatingImpl.userRegistration(name, email, passwordEncryption.encrypt(password));
			examSeatingImpl.findUser(email, password, user, false);
			session.setAttribute("userDetails", user);
			session.setAttribute("exams", examSeatingImpl.getAllExams());
			return "redirect:/homePage.jsp";
		}
	}
	
    @GetMapping("/getExamDetails")
    public ResponseEntity<String> getExamDetails(@RequestParam("examId") int examId) {
        try {
            Exam examDetails = examSeatingImpl.getExamById(examId);
            if (examDetails != null) {
                StringBuilder response = new StringBuilder();
                response.append("<h2>").append(examDetails.getExamName()).append("</h2>")
                        .append("<p><strong>Description:</strong> ").append(examDetails.getDescription()).append("</p>")
                        .append("<p><strong>Exam Date:</strong> ").append(examDetails.getExamDate()).append("</p>")
                        .append("<p><strong>Application Start Date:</strong> ").append(examDetails.getApplicationStartDate()).append("</p>")
                        .append("<p><strong>Application End Date:</strong> ").append(examDetails.getApplicationEndDate()).append("</p>");
                return ResponseEntity.ok(response.toString());
            } else {
                return ResponseEntity.ok("<p>No exam found with ID: " + examId + "</p>");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving exam details: " + e.getMessage());
        }
    }
    
    @PostMapping("/loadAllExams")
    public String loadAllExams(Model model,HttpSession session) {
            List<Exam> exams = examSeatingImpl.getAllExams();
            session.setAttribute("exams", exams);
            return "redirect:/homePage.jsp"; 
    }
    
    @GetMapping("/searchExam")
    public ModelAndView findExam(@RequestParam("query") String queryString, HttpSession session, Model model) {
        ModelAndView modelAndView = new ModelAndView("homePage.jsp");
        if (queryString != null && !queryString.isEmpty()) {
            List<Exam> exams = examSeatingImpl.findExam(queryString);
            session.setAttribute("exams", exams);
        } else {
            model.addAttribute("errorMessage", "Query string cannot be empty.");
        }

        return modelAndView;
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); 
        }
        return "redirect:/login.jsp"; 
    }
 
}
