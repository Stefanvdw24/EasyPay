<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div>This web app was made by Daniel van der Westhuizen to complete an assignment for EasyPay</div>
<div>Select a file to process with the form below and then the app will check if its formatting is correct or not</div>
<form action="ProcessInfo" method="post" enctype="multipart/form-data">
	<input type="file" name="TheFile">
	<input type="submit" value="Send">
</form>
</body>
</html>