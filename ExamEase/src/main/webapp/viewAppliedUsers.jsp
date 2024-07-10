<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.chainsys.examease.model.User"%>
<%@ page import="com.chainsys.examease.model.Exam"%>
<%@ page import="com.chainsys.examease.dao.UserDAO"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<!DOCTYPE html>
<html lang ="en">
<head>
<meta charset="ISO-8859-1">
<title>Exams</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<style>
    .container-wrapper {
        display: flex;
        height: 80vh;
    }
    .left-container {
        flex: 0 0 40%;
        border-right: 1px solid #ddd;
        padding: 20px;
        
    }
    .right-container {
        flex: 1;
        padding: 20px;
    }

    .profile-dropdown {
        margin-right: 20px;
    }

    .profile-container {
        display: flex;
        align-items: center;
        border-radius: 20px;
        background-color: #f8f9fa;
        border: 1px solid #ddd;
        padding: 5px 10px;
        color: black;
    }

    .profile-container:hover {
        color: blue;
    }

    .profile-icon {
        font-size: 1.5em;
        border-radius: 50%;
        padding: 5px;
        background-color: #fff;
        margin-right: 10px;
    }

    .username {
        font-size: 1em;
        font-weight: bold;
    }

    .dropdown-item i {
        margin-right: 10px;
    }

    .navbar-nav {
        margin-left: 20px;
    }
    .search-form {
        margin-left: 70px;
        width: 400px;
    }

    .search-bar {
        border-radius: 20px;
    }

    .submit-button {
        border-radius: 20px;
    }

    .btn-black {
        color: #000;
        background-color: #0000;
        border-color: #000;
    }

    .btn-black:hover {
        color: #fff;
        background-color: #000;
        border-color: #000;
    }

    .custom-navbar {
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        background-color: #ffffff;
    }

    .navbar-nav .nav-link:hover {
        color: blue;
    }

    .list-group-item {
        cursor: pointer;
    }

    .list-group-item:hover {
        background-color: #f0f0f0;
    }

    .list-group-item.active {
        background-color: #007bff;
        border-color: #007bff;
        color: white;
    }

    .scrollable-exam-list {
        max-height: 70vh; 
        overflow-y: auto;
    }
    .Scrollable-user-list {
        max-height: 70vh; 
        overflow-y: auto;
    }
    .user-name {
        font-size: 1.0em; 
        font-weight:500;
    }
</style>
</head>
<body>
<%
    request.setAttribute("currentPage", "otherPage");
%>
<jsp:include page="navbar.jsp" />

<%
    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    User userDetails = (User) session.getAttribute("userDetails");
    String userName = userDetails != null ? userDetails.getUsername() : "User";
    int roleId = userDetails != null ? userDetails.getRoleId() : 1;
    UserDAO userDAO = (UserDAO) context.getBean("userDAO");
    List<Exam> exams = (List<Exam>) session.getAttribute("exams");
  
%>
<div class="container-fluid container-wrapper">
    <div class="left-container">
    <h2>All Exams</h2>
    <div class="scrollable-exam-list">
        <ul class="list-group">
            <%
            if (exams != null && !exams.isEmpty()) {
                for (Exam exam : exams) {
            %>
            <li class="list-group-item" data-exam-id="<%= exam.getExamId() %>"><span class="exam-name"><%= exam.getExamName() %></span></li>
            <%
                }
            } else {
            %>
            <li class="list-group-item">No exams to show</li>
            <%
            }
            %>
        </ul>
    </div>
</div>
     <div class="right-container">
        <h2>Users Applied for Exam</h2>
        <div class = "Scrollable-user-list">
        <div class="accordion" id="accordionExample">
            <!-- User details will be dynamically loaded here -->
        </div>
        </div>
    </div>
</div>
<script>
    $(function() {
        $(".list-group-item").click(function() {
            $(".list-group-item").removeClass("active");
            $(this).addClass("active");

            var examId = $(this).data("exam-id");
            console.log("Selected Exam ID:");

            $.ajax({
                url: '/fetchAppliedUsers',
                method: 'POST',
                data: { examId: examId },
                success: function(response) {
                    var users = JSON.parse(response);
                    var accordion = $("#accordionExample");
                    accordion.empty();

                    if (users.length > 0) {
                        users.forEach(function(user, index) {
                            var userAccordionItem = `
                                <div class="accordion-item">
                                    <h2 class="accordion-header">
                                        <button class="accordion-button \${index === 0 ? '' : 'collapsed'}" type="button" data-bs-toggle="collapse" data-bs-target="#collapse\${index}" aria-expanded="\${index === 0 ? 'true' : 'false'}" aria-controls="collapse\${index}">
                                            <span class="user-name">\${user.name}</span> (Roll No: \${user.rollNo})
                                        </button>
                                    </h2>
                                    <div id="collapse\${index}" class="accordion-collapse collapse \${index === 0 ? 'show' : ''}" data-bs-parent="#accordionExample">
                                    <div class="accordion-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p><strong>Roll No:</strong> \${user.rollNo}</p>
                                            <p><strong>Name:</strong> \${user.name}</p>
                                            <p><strong>Date of Birth:</strong> \${user.dob}</p>
                                            <p><strong>Qualification:</strong> \${user.qualification}</p>
                                            <p><strong>Gender:</strong> \${user.gender}</p>
                                        </div>
                                        <div class="col-md-6">
                                            <p><strong>Address:</strong> \${user.address}</p>
                                            <p><strong>Native City:</strong> \${user.nativeCity}</p>
                                            <p><strong>State:</strong> \${user.state}</p>
                                            <p><strong>Aadhar Number:</strong> \${user.aadharNumber}</p>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                            `;
                            accordion.append(userAccordionItem);
                        });
                    } else {
                        accordion.append('<div class="accordion-item"><div class="accordion-header"><button class="accordion-button" type="button">No users have applied for this exam</button></div></div>');
                    }
                }
            });
        });
    });
</script>

</body>
</html>
