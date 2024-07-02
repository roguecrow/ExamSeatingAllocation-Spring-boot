<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.chainsys.examease.model.User" %>

<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setHeader("Expires", "0"); // Proxies.

if (session == null || session.getAttribute("userDetails") == null) {
    response.sendRedirect("login.jsp");
    return;
}

User userDetails = (User) session.getAttribute("userDetails");
String userName = userDetails != null ? userDetails.getUsername() : "User";
int roleId = userDetails != null ? userDetails.getRoleId() : 1; 
String currentPage = (String) request.getAttribute("currentPage");
System.out.println("currentPage :" + currentPage);
%>
<header>
    <nav class="navbar navbar-expand-lg custom-navbar">
        <div class="container-fluid">
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="homePage.jsp">Home</a>
                    </li>
                    <% if (roleId == 1) { %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle active" href="#" id="servicesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Services
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="servicesDropdown">
                            <li><a class="dropdown-item" href="viewHallTicket.jsp">View Exam Hall Tickets</a></li>
                            <li><a class="dropdown-item" href="examSeatViewer.jsp">View Exam Seat Allocation</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="help.jsp">Help</a>
                    </li>
                    <% } %>
                    <% if ("homePage".equals(currentPage)) { %>
                    <form class="d-flex search-form" role="search" action="searchExam" method="GET">
                        <input class="form-control me-2 search-bar" type="search" name="query" placeholder="Search Exams..." aria-label="Search">
                        <button class="btn btn-outline-success submit-button btn-black" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </form>
                    <% } %>
                </ul>
                <div class="dropdown profile-dropdown">
                    <a href="#" class="d-flex align-items-center text-decoration-none" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                        <div class="profile-container">
                            <i class="fas fa-user-circle profile-icon"></i>
                            <span class="username"><%= userName %></span>
                        </div>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownUser1">
                        <li><a class="dropdown-item" href="#"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a class="dropdown-item" href="#"><i class="fas fa-cog"></i> Settings</a></li>
                        <div class="dropdown-divider"></div>
                        <li>
                            <form action="logout" method="post" style="display: inline;">
                                <button type="submit" class="dropdown-item"><i class="fas fa-sign-out-alt"></i> Log out</button>
                            </form>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</header>
