<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Super Admin Home Page</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/superAdminHome.css">
	<script src="${pageContext.request.contextPath}/js/logout.js"></script> 
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
    	<div class="container-fluid">
        	<h1 class="mt-4">Super Admin Dashboard</h1>
        	<h3>Welcome Home, <span class="authenticated-user">${username}</span>!</h3>
			<div class="row d-flex justify-content-center">
				<div class="card">					
                    <div class="card-body">
                        <h5 class="card-title">User</h5>
                        <p class="card-text">Create User Here</p>
                    </div>
                    <div class="card-footer">
                        <small class="text-muted">Last updated eons ago</small>
                        <div class="btn-group">
                            <p><b>Create A User</b></p>
                            <button class="btn btn-primary btn-center" id="createUser" onClick="window.location.href='<c:url value='/api/v1/superadmins/createUser'/>';">Create a User</button>
                        </div>
                    </div>
                </div>
        		<div class="card">					
                    <div class="card-body">
                        <h5 class="card-title">Manager</h5>
                        <p class="card-text">Create Manager Here</p>
                    </div>
                    <div class="card-footer">
						<small class="text-muted">Last updated eons ago</small>
                        <div class="btn-group">
                            <p><b>Create A Manager</b></p>
                            <button class="btn btn-primary btn-center" id="createManager" onClick="window.location.href='<c:url value='/api/v1/superadmins/createManager'/>';">Create a Manager</button>
                        </div>
                    </div>
                </div>
    		</div>
    	</div>
    </div>
</body>
</html>