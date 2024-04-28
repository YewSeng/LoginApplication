<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Super Admin Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/superAdminLogin.css">
	<script src="${pageContext.request.contextPath}/js/superAdminLoginValidation.js"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	    <a class="navbar-brand" href="/">Main</a>
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	    </button>
	</nav>
    <div class="center-container">
        <h1>Super Admin Login Form</h1>
        <%-- Display login error message if exists --%>
        <c:if test="${not empty loginErrorMessage}">
            <div class="error-message alert alert-danger" id="login-error-message"><b>${loginErrorMessage}</b></div>
        </c:if>
        <form action="${pageContext.request.contextPath}/superAdminLogin" method="POST" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="superAdminKey"><b>Super Admin Key:</b></label>
                <input type="text" class="form-control" id="superAdminKey" name="superAdminKey" value="${superAdminKey}" required>
		        <c:if test="${not empty superAdminKeyError}">
		            <div class="fieldsError" id="superAdminKey-error"><b>${superAdminKeyError}</b></div>
		        </c:if>
            </div>
			<button type="submit" id="submit-button" class="btn btn-primary btn-center">Login</button>
        </form>
    </div>
</body>
</html>