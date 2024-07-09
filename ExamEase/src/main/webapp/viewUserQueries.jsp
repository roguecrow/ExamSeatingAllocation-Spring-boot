<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Admin Query Management - ExamEase Portal</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<%
    request.setAttribute("currentPage", "otherPage");
%>
<jsp:include page="navbar.jsp" />
<style>
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
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	background-color: #ffffff;
}

.navbar-nav .nav-link:hover {
	color: blue;
}

body {
	background-color: #f8f9fa;
	color: #343a40;
	font-family: 'Arial', sans-serif;
}

.container {
	background-color: #ffffff;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	padding: 20px;
	margin-top: 30px;
}

h1 {
	color: #007bff;
	margin-bottom: 20px;
}

.list-group-item {
	cursor: pointer;
}

.reply-form {
	display: none;
	margin-top: 15px;
}
</style>
</head>
<body>
<div class="container">
  <h1>Manage User Queries</h1>
  <div class="list-group" id="queryList">
    <!-- Queries will be populated here -->
  </div>
</div>

<script>
$(document).ready(function() {
    $.ajax({
        type: "POST",
        url: "/adminFetchQueries",
        success: function(response) {
            var queries = JSON.parse(response);
            console.log("queries --" + JSON.stringify(queries, null, 2));
            populateQueries(queries);
        },
        error: function() {
            alert('Failed to load queries. Please try again later.');
        }
    });

    function populateQueries(queries) {
        var container = $('#queryList');
        container.empty();
        if (queries.length === 0) {
            container.append('<div class="list-group-item">No queries found.</div>');
            return;
        }
        queries.forEach(function(query) {
            console.log("query --"+ JSON.stringify(query));
            var queryItem = $('<div>').addClass('list-group-item').data('query-id', query.id);
            var queryType = $('<h5>').text('Query: ' + query.issueType);
            queryItem.append(queryType);
            var message = $('<p>').text('Message: ' + query.message);
            queryItem.append(message);
            var userName = $('<p>').text('Name: ' + query.userName);
            queryItem.append(userName);
            var userEmail = $('<p>').text('Email: ' + query.userEmail);
            queryItem.append(userEmail);
            var reply = $('<p>').html('<strong>Reply:</strong> ' + (query.reply ? query.reply : 'No reply yet.'));
            queryItem.append(reply);
            
            var replyForm = $('<div>').addClass('reply-form').attr('id', 'replyForm-' + query.id);
            var replyTextarea = $('<textarea>').addClass('form-control').attr('rows', '3').attr('placeholder', 'Type your reply here...');
            var submitButton = $('<button>').addClass('btn btn-primary mt-2').text('Submit Reply');
            submitButton.click(function(event) {
                event.stopPropagation();
                var replyText = replyTextarea.val();
                var queryId = queryItem.data('query-id');
                submitReply(queryId, replyText);
            });
            replyForm.append(replyTextarea).append(submitButton);
            queryItem.append(replyForm);
            
            queryItem.click(function(event) {
                if ($(event.target).closest('.reply-form').length === 0) {
                    $('.reply-form').not(replyForm).hide();
                    replyForm.toggle();
                }
            });
            
            container.append(queryItem);
        });
    }

    function submitReply(queryId, replyText) {
        $.ajax({
            type: "POST",
            url: "/submitReply",
            data: JSON.stringify({ id: queryId, reply: replyText }),
            contentType: "application/json",
            success: function(response) {
                alert('Reply submitted successfully!');
                location.reload();
            },
            error: function() {
                alert('Failed to submit reply. Please try again later.');
            }
        });
    }
});
</script>
</body>
</html>
