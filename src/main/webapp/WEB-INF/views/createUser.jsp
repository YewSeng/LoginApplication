<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create User</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/createUser.css">
	<script src="${pageContext.request.contextPath}/js/userFormValidation.js"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	    <a class="navbar-brand" href="/api/v1/superadmins/home">Super Admin Dashboard</a>
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	    </button>
	    <div class="collapse navbar-collapse" id="navbarNav">
	        <ul class="navbar-nav ml-auto">
	            <li class="nav-item">
					<form action="<c:url value='/logout'/>" method="GET" onsubmit="clearTokenCookie()">
					    <button class="btn btn-outline-success my-2 my-sm-0" type="submit" id="submit-button">LOGOUT</button>
					</form>
	            </li>
	        </ul>
	    </div>
	</nav>
    <div class="center-container">
        <h1>Create User</h1>
        <%-- Display success message if exists --%>
        <c:if test="${not empty successMessage}">
        	<div class="message-container">
            	<div class="success-message alert alert-success" id="success-message"><b>${successMessage}</b></div>
        	</div>
        </c:if>
        
        <%-- Display error message if exists --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message alert alert-danger" id="error-message"><b>${errorMessage}</b></div>
        </c:if>
        
        <form action="<c:url value='/api/v1/superadmins/registerUser'/>" method="POST" onsubmit="return validateForm()">
            <%-- Input fields for user details --%>
            <div class="form-group">
                 <label for="name"><b>Name:</b></label>
                <input type="text" class="form-control" id="name" name="name" value="${name}" required>
		        <c:if test="${not empty nameError}">
		            <div class="fieldsError" id="name-error"><b>${nameError}</b></div>
		        </c:if>
            </div>
            <div class="form-group">
                <label for="username"><b>Username:</b></label>
                <input type="text" class="form-control" id="username" name="username" value="${username}" required>   
		        <c:if test="${not empty usernameError}">
		            <div class="fieldsError" id="username-error"><b>${usernameError}</b></div>
		        </c:if>
            </div>
            <div class="form-group">
                <label for="password"><b>Password:</b></label>
                <input type="password" class="form-control" id="password" name="password" value="${password}" required>
		        <c:if test="${not empty passwordError}">
		            <div class="fieldsError" id="password-error"><b>${passwordError}</b></div>
		        </c:if>
            </div>
            <div class="form-group form-check">
                <input type="checkbox" class="form-check-input" id="showPassword" onclick="togglePasswordVisibility()">
                <label class="form-check-label" for="showPassword"><b>Show Password</b></label>
            </div>
            <button type="submit" id="submit-button" class="btn btn-primary btn-center">Submit</button>
        </form>
    </div>
</body>
</html>