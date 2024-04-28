<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Index Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <script src="${pageContext.request.contextPath}/js/loginFormValidation.js"></script>    
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	    <a class="navbar-brand" href="/hidden">Test</a>
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	    </button>
	</nav>
    <div class="center-container">
    	<h1>Login Form</h1>
    	
    	<%-- Display logout message if exists --%>
        <c:if test="${not empty logoutMessage}">
        	<div class="message-container">
            	<div class="success-message alert alert-success" id="logout-message"><b>${logoutMessage}</b></div>
        	</div>
        </c:if>
        
    	<%-- Display success message if exists --%>
        <c:if test="${not empty successMessage}">
        	<div class="message-container">
            	<div class="success-message alert alert-success" id="success-message"><b>${successMessage}</b></div>
        	</div>
        </c:if>
        
    	<%-- Display login error message if exists --%>
        <c:if test="${not empty loginErrorMessage}">
			<div class="error-message alert alert-danger" id="login-error-message"><b>${loginErrorMessage}</b></div>
        </c:if>
        					
 		<form action="<c:url value='/login'/>" method="POST" onsubmit="return validateForm()">
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
            <button type="submit" id="submit-button" class="btn btn-primary btn-center">Login</button>
        </form>
    </div>
</body>
</html>