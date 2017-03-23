<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">

<body>

	<div class="page-header" style="background: #d3d3d3">
		<center>
			<h1>REGISTER</h1>
		</center>
	</div>

	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-9">
			<ul class="nav navbar-nav">
				<li><a href="home.jsp">Home</a>
				</li>
				<li><a href="login.jsp">Login</a>
				</li>
				<li><a href="register.jsp">Register</a>
				</li>
			</ul>
		</div>
	</div>
	</nav>

	<form action="Register" method="post">
		<center>
			Nickname: <br> <input type="text" name="nickname"> <br>
			Password: <br> <input type="password" name="password"> <br>
			Retype password: <br> <input type="password"
				name="retypedpassword"> <br> E-mail: <br> <input
				type="text" name="email"> <br>
			<br> <input type="submit" name="submit" value="Submit"
				class="btn" style="width: 157px">
		</center>
	</form>

	<br>

	<center>
		<a href="login.jsp"> Already have an account ? Log in here ! </a>
	</center>

	<hr>

</body>

</html>