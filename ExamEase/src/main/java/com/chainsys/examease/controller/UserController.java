package com.chainsys.examease.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chainsys.examease.dao.ExamDAO;
import com.chainsys.examease.dao.ExamSeatAllocator;
import com.chainsys.examease.dao.HallTicketGenerator;
import com.chainsys.examease.dao.UserDAO;
import com.chainsys.examease.encrypt.PasswordEncryption;
import com.chainsys.examease.model.Exam;
import com.chainsys.examease.model.ExamAllocatedLocation;
import com.chainsys.examease.model.ExamLocation;
import com.chainsys.examease.model.User;
import com.chainsys.examease.model.UserQuery;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	 private static final String PARAM_EXAM_ID = "examId";
	 private static final String SESSION_ATTR_USER_DETAILS = "userDetails";

	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	PasswordEncryption passwordEncryption;
	
	@Autowired
	User user;
	
	@Autowired
	ExamSeatAllocator examSeatAllocator;
	
	@Autowired
	HallTicketGenerator hallTicketGenerator;
	
	@Autowired
	ExamDAO examDAO;
	
	
	@RequestMapping("/")
	public String home() {
		System.out.println("in landing page");
		return "landingPage.jsp";
	}
 
		@PostMapping("/login")
		public String userLogin(@RequestParam("email") String email, @RequestParam("password") String password,
				HttpSession session, Model model) throws Exception {
			if (userDAO.findUser(email, password, user, true)) {
				session.setAttribute("userDetails", user);
				session.setAttribute("exams", examDAO.getAllExams());
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

		if (userDAO.findUser(email, password, user, false)) {
			model.addAttribute("errorMessage", "Account already exists.");
			return "registerPage.jsp";
		} else {
			userDAO.userRegistration(name, email, passwordEncryption.encrypt(password));
			userDAO.findUser(email, password, user, false);
			session.setAttribute("userDetails", user);
			session.setAttribute("exams", examDAO.getAllExams());
			return "redirect:/homePage.jsp";
		}
	}
	
    @GetMapping("/getExamDetails")
    public ResponseEntity<String> getExamDetails(@RequestParam("examId") int examId) {
        try {
            Exam examDetails = examDAO.getExamById(examId);
            if (examDetails != null) {
            	 String examDate = formatDate(examDetails.getExamDate());
                 String applicationStartDate = formatDate(examDetails.getApplicationStartDate());
                 String applicationEndDate = formatDate(examDetails.getApplicationEndDate());
                StringBuilder response = new StringBuilder();
                response.append("<h2>").append(examDetails.getExamName()).append("</h2>")
                .append("<p><strong>Description:</strong> ").append(examDetails.getDescription()).append("</p>")
                .append("<p><strong>Exam Date:</strong> ").append(examDate).append("</p>")
                .append("<p><strong>Application Start Date:</strong> ").append(applicationStartDate).append("</p>")
                .append("<p><strong>Application End Date:</strong> ").append(applicationEndDate).append("</p>");
                return ResponseEntity.ok(response.toString());
            } else {
                return ResponseEntity.ok("<p>No exam found with ID: " + examId + "</p>");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving exam details: " + e.getMessage());
        }
    }
    
    private String formatDate(Date date) {
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return localDate.format(formatter);
    }
    
    @PostMapping("/loadAllExams")
    public String loadAllExams(Model model,HttpSession session) {
    	System.out.println("from load all exams controller");
            List<Exam> exams = examDAO.getAllExams();
            session.setAttribute("exams", exams);
            return "redirect:/homePage.jsp"; 
    }
    
    @GetMapping("/searchExam")
    public String findExam(@RequestParam("query") String queryString, HttpSession session, Model model) {
        if (queryString != null && !queryString.isEmpty()) {
            List<Exam> exams = examDAO.findExam(queryString);
            session.setAttribute("exams", exams);
        } else {
            model.addAttribute("errorMessage", "Query string cannot be empty.");
        }

        return "redirect:/homePage.jsp";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); 
        }
        return "redirect:/login.jsp"; 
    }
    
    @PostMapping("/uploadDoc")
    public String handleDocumentUpload(@RequestParam("passportPhoto") MultipartFile passportPhoto,
                                       @RequestParam("digitalSignature") MultipartFile digitalSignature,
                                       @RequestParam("qualificationDocuments") MultipartFile qualificationDocuments,
                                       HttpSession session,
                                       Model model) throws IOException {

    	user = (User) session.getAttribute(SESSION_ATTR_USER_DETAILS);
        String examIdStr = (String) session.getAttribute(PARAM_EXAM_ID);
        int examId = Integer.parseInt(examIdStr);

        user.setPassportSizePhoto(passportPhoto.getBytes());
        user.setDigitalSignature(digitalSignature.getBytes());
        user.setQualificationDocuments(qualificationDocuments.getBytes());

        session.setAttribute(SESSION_ATTR_USER_DETAILS, user);

        return "redirect:/applicationPreview.jsp?examId=" + examId;
    }

    @PostMapping("/applyExam")
    public String handleExamApplication(@RequestParam("full_name") String fullName,
                                        @RequestParam("gender") String gender,
                                        @RequestParam("dob") String dobString,
                                        @RequestParam("qualification") String qualification,
                                        @RequestParam("address") String address,
                                        @RequestParam("native_city") String nativeCity,
                                        @RequestParam("state") String state,
                                        @RequestParam("aadhar_number") String aadharNumber,
                                        @RequestParam("city_preference_1") String cityPreference1,
                                        @RequestParam("city_preference_2") String cityPreference2,
                                        @RequestParam("city_preference_3") String cityPreference3,
                                        @RequestParam(PARAM_EXAM_ID) String examId,
                                        HttpSession session) throws ParseException {

    	user = (User) session.getAttribute(SESSION_ATTR_USER_DETAILS);
        if (user == null) {
        	user = new User();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dob =  dateFormat.parse(dobString);

        user.setName(fullName);
        user.setGender(gender.charAt(0));
        user.setDob(dob);
        user.setQualification(qualification);
        user.setAddress(address);
        user.setNativeCity(nativeCity);
        user.setState(state);
        user.setAadharNumber(aadharNumber);
        user.setCityPreference1(cityPreference1);
        user.setCityPreference2(cityPreference2);
        user.setCityPreference3(cityPreference3);

        session.setAttribute(SESSION_ATTR_USER_DETAILS, user);
        session.setAttribute(PARAM_EXAM_ID, examId);

        return "redirect:/documentsUploadPage.jsp?examId=" + examId;
    }

    @PostMapping("/updateDoc")
    public String handleUpdateDocuments(@RequestParam("updatePassportPhoto") MultipartFile passportPhotoPart,
                                        @RequestParam("updateDigitalSignature") MultipartFile digitalSignaturePart,
                                        @RequestParam("updateQualificationDocuments") MultipartFile qualificationDocumentsPart,
                                        HttpSession session,
                                        Model model) throws IOException {
    	System.out.println("in updateDoc page");

    	user = (User) session.getAttribute(SESSION_ATTR_USER_DETAILS);
        String examIdStr = (String) session.getAttribute(PARAM_EXAM_ID);
        int examId = Integer.parseInt(examIdStr);

        if (passportPhotoPart != null && !passportPhotoPart.isEmpty()) {
        	user.setPassportSizePhoto(passportPhotoPart.getBytes());
        }
        if (digitalSignaturePart != null && !digitalSignaturePart.isEmpty()) {
        	user.setDigitalSignature(digitalSignaturePart.getBytes());
        }
        if (qualificationDocumentsPart != null && !qualificationDocumentsPart.isEmpty()) {
        	user.setQualificationDocuments(qualificationDocumentsPart.getBytes());
        }

        session.setAttribute(SESSION_ATTR_USER_DETAILS, user);

        if (userDAO.updateExamDoc(user)) {
            return "redirect:/applicationPreview.jsp?examId=" + examId;
        }

        return "errorPage"; 
    }

    @PostMapping("/submitDetails")
    public String handleSubmitDetails(HttpSession session, Model model) throws ClassNotFoundException {
        User details = (User) session.getAttribute(SESSION_ATTR_USER_DETAILS);
        String examIdStr = (String) session.getAttribute(PARAM_EXAM_ID);
        int examId = Integer.parseInt(examIdStr);
        String appId = examIdStr + details.getRollNo();

        byte[] passportPhoto = details.getPassportSizePhoto();
        byte[] digitalSignature = details.getDigitalSignature();
        byte[] qualificationDocuments = details.getQualificationDocuments();

        if (!examDAO.getExamDocById(details)) {
            if (userDAO.addUserDetails(details, appId) == 1 && userDAO.addUserDocument(details.getRollNo(), passportPhoto, digitalSignature, qualificationDocuments) == 1) {
            	examSeatAllocator.allocateSeats(details, examId);
                return "redirect:/homePage.jsp?message=examAppliedSuccessfully";
            } else {
                return "redirect:/homePage.jsp?message=examApplicationUnSuccessful";
            }
        } else {
            if (userDAO.addUserDetails(details, appId) == 1) {
            	examSeatAllocator.allocateSeats(details, examId);
                return "redirect:/homePage.jsp?message=examAppliedSuccessfully";
            } else {
                return "redirect:/homePage.jsp?message=examApplicationUnSuccessful";
            }
        }
    }
    
    
    @GetMapping("/document")
    public void getDocument(
            @RequestParam("type") String type, 
            HttpServletRequest request, 
            HttpServletResponse response) {
        
        HttpSession session = request.getSession();
        User userDetail = (User) session.getAttribute("userDetails");

        if (userDetail != null) {
            InputStream inputStream = null;
            String contentType = MediaType.IMAGE_JPEG_VALUE;

            try {
                switch (type) {
                    case "passportPhoto":
                        inputStream = new ByteArrayInputStream(userDetail.getPassportSizePhoto());
                        break;
                    case "digitalSignature":
                        inputStream = new ByteArrayInputStream(userDetail.getDigitalSignature());
                        break;
                    case "qualificationDocuments":
                        inputStream = new ByteArrayInputStream(userDetail.getQualificationDocuments());
                        contentType = MediaType.APPLICATION_PDF_VALUE;
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                }

                if (inputStream != null) {
                    response.setContentType(contentType);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }
                }
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } 
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @PostMapping("/fetchSeatAllocation")
    public ResponseEntity<String> fetchSeatAllocation(@RequestParam int examId, @RequestParam int rollNo) {
        ExamAllocatedLocation locationDetails = examDAO.getExamLocationDetails(rollNo, examId);
        System.out.println("allocated seat : "+locationDetails.getAllocatedSeat());
		Gson gson = new Gson();
		String jsonResponse = gson.toJson(locationDetails);
		System.out.println("jsonResponse" +jsonResponse);
		return ResponseEntity.ok(jsonResponse);
    }
    
    @PostMapping("/getHallTicket")
    public ResponseEntity<String> getHallTicket(@RequestParam String serialNumber) {
        try {
            String examDetailsJson = examDAO.getExamDetails(serialNumber);
            if (examDetailsJson != null) {
            	System.out.println("examDetailsJson --" + examDetailsJson);
                return ResponseEntity.ok(examDetailsJson);
            } else {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("error", "No hall ticket found for the given serial number.");
                return ResponseEntity.status(404).body(errorResponse.toString());
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Database access error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse.toString());
        }
    }
    
    @GetMapping("/downloadHallTicket")
    public ResponseEntity<byte[]> downloadHallTicket(@RequestParam("serialNumber") String serialNumber) throws DocumentException, IOException {
        JSONObject data;
        String jsonString = examDAO.getExamDetails(serialNumber);
		if (jsonString != null) {
		    data = new JSONObject(jsonString);
		} else {
		    return ResponseEntity.status(HttpStatus.NOT_FOUND)
		            .body(("Exam details not found for the given se rial number.").getBytes());
		}

        byte[] hallTicketContent = hallTicketGenerator.generateHallTicket(data, serialNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "hall_ticket_" + serialNumber + ".pdf");

        return new ResponseEntity<>(hallTicketContent, headers, HttpStatus.OK);
    }

    @PostMapping("/SendExamQuries")
    public String sendQuries( 
    		@RequestParam("userName") String userName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("issueType") String issueType,
            @RequestParam("message") String message,HttpSession session) {
    	
    	User userDetails = (User) session.getAttribute("userDetails");
    	userDetails.getRollNo();
    	 if(userDAO.addUserQuery(userDetails.getRollNo(),userName,userEmail,issueType,message)){
    		 return "redirect:/help.jsp?message=submittedSuccessfully";
    	 }
    	 else {
    		 return "redirect:/help.jsp?message=pleaseTryAgainLater";
    	 }
    }
    
    @PostMapping("/examQueries")
    public ResponseEntity<String> getExamQueries(HttpSession session) {
        User userDetails = (User) session.getAttribute("userDetails");
        List<UserQuery> queries = userDAO.findUserQueries(userDetails.getRollNo());
        Gson gson = new Gson();
        String queriesJsonResponse = gson.toJson(queries);
        System.out.println("queriesJsonResponse --" +queriesJsonResponse);
        return ResponseEntity.ok(queriesJsonResponse);
    }
    
    @PostMapping("/adminFetchQueries")
    public ResponseEntity<String> getAllExamQueries(HttpSession session) {
        List<UserQuery> queries = userDAO.findAdminQueries();
        Gson gson = new Gson();
        String queriesJsonResponse = gson.toJson(queries);
        System.out.println("queriesJsonResponse --" +queriesJsonResponse);
        return ResponseEntity.ok(queriesJsonResponse);
    }
    
    @PostMapping("/submitReply")
    public ResponseEntity<String> submitReply(@RequestBody Map<String, String> payload) {
    	System.out.println("in submitReply");
        String queryId = payload.get("id");
        String replyText = payload.get("reply");

        try {
        	if(userDAO.updateAdminReply(Integer.parseInt(queryId), replyText)) {
        		System.out.println("on success");
                return ResponseEntity.ok("Reply submitted successfully!");
        	}
        	else {
        		System.out.println("on failure");
        		return ResponseEntity.ok("Failed to Submit!");
        	}
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit reply. Please try again later.");
        }
    }
    
    @PostMapping("/fetchAppliedUsers")
    public ResponseEntity<String> fetchAppliedUsers(@RequestParam("examId") int examId) {
        List<User> users = userDAO.findUsersByExamId(examId);
        Gson gson = new Gson();
        String usersJsonResponse = gson.toJson(users);
        System.out.println("queriesJsonResponse --" +usersJsonResponse);
        return ResponseEntity.ok(usersJsonResponse);
    }
}
