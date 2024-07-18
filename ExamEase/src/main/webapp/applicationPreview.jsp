<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.chainsys.examease.model.User"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Preview Details</title>
<!-- Bootstrap CSS -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<style>
.preview-container {
    background-color: #fff;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    margin-top: 50px;
}

.preview-container h2 {
    margin-bottom: 20px;
    font-weight: bold;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    font-weight: bold;
}

.form-group p {
    margin-left: 150px;
}

.btn-container {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

button.btn {
	background-color:#2B3467;
	color:white;
	height:42px;
}
button.btn:hover{
	background-color:#3E54AC;
	color:white;
	height:42px;
}
.modal-header {
  background-color: #2B3467;
  color: white;
}
.modal-header > button{
  color: white;
}
label.radio-card {
  cursor: pointer;
  margin: .5em;
}
label.radio-card .card-content-wrapper {
  background: #fff;
  border-radius: 5px;
  padding: 15px;
  box-shadow: 0 2px 4px 0 rgba(219, 215, 215, 0.04);
  transition: 200ms linear;
  position: relative;
	 min-width: 170px;
}
label.radio-card .check-icon {
  width: 20px;
  height: 20px;
  display: inline-block;
  border-radius: 50%;
  transition: 200ms linear;
  position: absolute;
  right: -10px;
  top: -10px;
}
label.radio-card .check-icon:before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='12' height='9' viewBox='0 0 12 9' fill='none' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M0.93552 4.58423C0.890286 4.53718 0.854262 4.48209 0.829309 4.42179C0.779553 4.28741 0.779553 4.13965 0.829309 4.00527C0.853759 3.94471 0.889842 3.88952 0.93552 3.84283L1.68941 3.12018C1.73378 3.06821 1.7893 3.02692 1.85185 2.99939C1.91206 2.97215 1.97736 2.95796 2.04345 2.95774C2.11507 2.95635 2.18613 2.97056 2.2517 2.99939C2.31652 3.02822 2.3752 3.06922 2.42456 3.12018L4.69872 5.39851L9.58026 0.516971C9.62828 0.466328 9.68554 0.42533 9.74895 0.396182C9.81468 0.367844 9.88563 0.353653 9.95721 0.354531C10.0244 0.354903 10.0907 0.369582 10.1517 0.397592C10.2128 0.425602 10.2672 0.466298 10.3112 0.516971L11.0651 1.25003C11.1108 1.29672 11.1469 1.35191 11.1713 1.41247C11.2211 1.54686 11.2211 1.69461 11.1713 1.82899C11.1464 1.88929 11.1104 1.94439 11.0651 1.99143L5.06525 7.96007C5.02054 8.0122 4.96514 8.0541 4.90281 8.08294C4.76944 8.13802 4.61967 8.13802 4.4863 8.08294C4.42397 8.0541 4.36857 8.0122 4.32386 7.96007L0.93552 4.58423Z' fill='white'/%3E%3C/svg%3E%0A");
  background-repeat: no-repeat;
  background-size: 12px;
  background-position: center center;
  transform: scale(1.6);
  transition: 200ms linear;
  opacity: 0;
}
label.radio-card input[type=radio] {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
}
label.radio-card input[type=radio]:checked + .card-content-wrapper {
  box-shadow: 0 2px 4px 0 rgba(219, 215, 215, 0.5), 0 0 0 2px #3057d5;
}
label.radio-card input[type=radio]:checked + .card-content-wrapper .check-icon {
  background: #3057d5;
  border-color: #3057d5;
  transform: scale(1.2);
}
label.radio-card input[type=radio]:checked + .card-content-wrapper .check-icon:before {
  transform: scale(1);
  opacity: 1;
}
label.radio-card input[type=radio]:focus + .card-content-wrapper .check-icon {
  box-shadow: 0 0 0 4px rgba(48, 86, 213, 0.2);
  border-color: #3056d5;
}
label.radio-card .card-content img {
  margin-bottom: 10px;
}
label.radio-card .card-content h4 {
  font-size: 16px;
  letter-spacing: -0.24px;
  text-align: center;
  color: #1f2949;
		margin: 0;
}
label.radio-card .card-content h5 {
  font-size: 14px;
  line-height: 1.4;
  text-align: center;
  color: #686d73;
}
.card-content > img{
	max-height:35px;
}
.modal-footer > button{
	width:50%;
	height:50px;
	border:0;
	color:#222;
}
.btn-outline-primary:hover {
  color: #fff;
  background-color: #2B3467!important;
}

.btn-outline-light:hover {
  color: #cecece!important;
}
.payment-details-container{
  background-color:yellow;
  border-color:activeborder;
  height:130px;
  width:auto;
  border-radius: 10px;
  margin: 10px;
  
}
.exam-details {
  padding: 15px 30px 0px;
  display:flex;
  justify-content:space-between;
}
.disclaimer{
padding: 20px;
color: red;
}

</style>
</head>
<body>
    <%! 
        String formatDate(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            return dateFormat.format(date);
        }
    %>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="preview-container">
                    <h2>Preview Details</h2>
                    <%
                        User userDetail = (User) session.getAttribute("userDetails");
                        String examId = request.getParameter("examId");
                    %>
                    <div class="form-group">
                        <label><strong>Full Name:</strong></label>
                        <p><%= userDetail.getName() %></p>
                    </div>
                    <div class="form-group">
                        <label><strong>Gender:</strong></label>
                        <p><%= userDetail.getGender() %></p>
                    </div>
                    <div class="form-group">
                        <label><strong>Date of Birth:</strong></label>
                        <p><%= formatDate(userDetail.getDob()) %></p>
                    </div>
                    <div class="form-group">
                        <label><strong>Qualification:</strong></label>
                        <p><%= userDetail.getQualification() %></p>
                    </div>
                    <div class="form-group">
                        <label>Address:</label>
                        <p><%= userDetail.getAddress() %></p>
                    </div>
                    <div class="form-group">
                        <label>Native City:</label>
                        <p><%= userDetail.getNativeCity() %></p>
                    </div>
                    <div class="form-group">
                        <label>State:</label>
                        <p><%= userDetail.getState() %></p>
                    </div>
                    <div class="form-group">
                        <label>Aadhar Number:</label>
                        <p><%= userDetail.getAadharNumber() %></p>
                    </div>
                    <div class="form-group">
                        <label>City Preference 1:</label>
                        <p><%= userDetail.getCityPreference1() %></p>
                    </div>
                    <div class="form-group">
                        <label>City Preference 2:</label>
                        <p><%= userDetail.getCityPreference2() %></p>
                    </div>
                    <div class="form-group">
                        <label>City Preference 3:</label>
                        <p><%= userDetail.getCityPreference3() %></p>
                    </div>
                    <div class="form-group">
                        <label>Passport Size Photo:</label>
                        <p><img src="document?type=passportPhoto" alt="Passport pic" style="max-width: 200px; max-height: 200px;"></p>
                    </div>
                    <div class="form-group">
                        <label>Digital Signature:</label>
                        <p><img src="document?type=digitalSignature" alt="Digital Signature" style="max-width: 200px; max-height: 200px;"></p>
                    </div>
                    <div class="form-group">
                        <label>Qualification Documents:</label>
                        <p><a href="document?type=qualificationDocuments" target="_blank">Download Qualification Documents</a></p>
                    </div>
                    <div class="btn-container">
                        <a href="documentsUploadPage.jsp?examId=<%= examId %>" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Back</a>
                       <%--  <form action="submitDetails" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action1" value="submitDetails">
                            <input type="hidden" name="examId" value="<%= examId %>">
                            <button type="submit" class="btn btn-success">Submit <i class="fas fa-check"></i></button>
                        </form> --%>
							<button type="button" class="btn" data-toggle="modal"
								data-target="#modal-lg">Show Payment Details</button>
					</div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bd-example-modal-lg" id="modal-lg">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Payment Details</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body p-0">
              <div class="container">
              <div class="payment-details-container">
              <div class="exam-details">
                 <p><strong>Exam Name:</strong> <%= session.getAttribute("examName") %></p>
                 <p><strong>Exam Date:</strong> <%= session.getAttribute("examDate") %></p>
                 <p><strong>Application Fee:</strong> <%= session.getAttribute("applicationFee") %></p>
                 
              </div>
              <p class="disclaimer"><strong>Disclaimer:</strong>The amount you are about to pay is " NON-REFUNDABLE !",Please make sure you have filled all the details correctly before Payment.</p>
              </div>
                  <div class="grid-wrapper grid-col-auto">
                  <h6>Please select a Payment Method :</h6>
                    <label for="radio-card-1" class="radio-card">
                      <input type="radio" name="radio-card" id="radio-card-1" checked />
                      <div class="card-content-wrapper">
                        <span class="check-icon"></span>
                        <div class="card-content text-center">
                          <img src="https://www.bca.co.id/-/media/Feature/Header/Header-Logo/logo-bca.svg?"
                            class="img-fluid" />
                          <h4>BCA</h4>
                        </div>
                      </div>
                    </label>
                    <!-- /.radio-card -->
                    <label for="radio-card-2" class="radio-card">
                      <input type="radio" name="radio-card" id="radio-card-2" />
                      <div class="card-content-wrapper">
                        <span class="check-icon"></span>
                        <div class="card-content text-center">
                          <img src="https://bankmandiri.co.id/image/layout_set_logo?img_id=31567&t=1678035789124"
                            class="img-fluid" />
                          <h4>Mandiri</h4>
                        </div>
                      </div>
                    </label>
                    <!-- /.radio-card -->
                    <label for="radio-card-3" class="radio-card">
                      <input type="radio" name="radio-card" id="radio-card-3" />
                      <div class="card-content-wrapper">
                        <span class="check-icon"></span>
                        <div class="card-content text-center">
                          <img src="https://bri.co.id/o/bri-corporate-theme/images/bri-logo.png"
                            class="img-fluid" />
                          <h4>BRI</h4>
                        </div>
                      </div>
                    </label>
                    <!-- /.radio-card -->
                    <label for="radio-card-5" class="radio-card">
                      <input type="radio" name="radio-card" id="radio-card-5" />
                      <div class="card-content-wrapper">
                        <span class="check-icon"></span>
                        <div class="card-content text-center">
                          <img src="https://cdn-icons-png.flaticon.com/512/2175/2175515.png"
                            class="img-fluid" />
                          <h4>Others</h4>
                        </div>
                      </div>
                    </label>
                    <!-- /.radio-card -->

                  </div>
                  <!-- /.grid-wrapper -->
                </div>
            </div>
            <div class="modal-footer justify-content-end p-0 mt-3">
			  <button type="button" class="btn-outline-light m-0" data-dismiss="modal" aria-label="Close">Cancel</button>
              <button type="button" class="btn-outline-primary m-0" data-dismiss="modal" aria-label="Close">Proceed to Pay</button>
            </div>
          </div>
          <!-- /.modal-content -->
        </div>
</body>
</html>
