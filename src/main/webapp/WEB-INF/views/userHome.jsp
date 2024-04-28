<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Home Page</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	    <a class="navbar-brand" href="#">User Dashboard</a>
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
        <h1>User Dashboard</h1>
        <h3>Welcome Home, <span class="authenticated-user">${username}</span>!</h3>
        <p>Name: <span class="authenticated-user">${name}</span></p>
        <p>Username: <span class="authenticated-user">${username}</span></p>
        <p>Role: <span class="authenticated-user">${role}</p>
    </div>
</body>
</html>