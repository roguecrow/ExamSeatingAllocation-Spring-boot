package com.chainsys.examease.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chainsys.examease.dao.ExamSeatingImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
	
	@Autowired
	ExamSeatingImpl examSeatingImpl;
	
    @PostMapping("/updateExamDetails")
    public String updateExamDetails(
            @RequestParam("examId") String examIdStr,
            @RequestParam("examDate") String examDate,
            @RequestParam("applicationStart") String applicationStart,
            @RequestParam("applicationEnd") String applicationEnd,
            Model model) {

        int examId = Integer.parseInt(examIdStr); 
        examSeatingImpl.updateExamDetails(examId, Date.valueOf(examDate), Date.valueOf(applicationStart), Date.valueOf(applicationEnd));
        return "redirect:/homePage.jsp?message=examUpdatedSuccessfully&type=success";
    }
	

}
