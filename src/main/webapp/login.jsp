<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Login - sVinicola</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<link rel="stylesheet" type="text/css" href="styles/form.css">
</head>
<body>
	<jsp:include page="/header.jsp" />
	<div id="bubble-canvas"></div>

	<main class="form-container">
		<h1>Accedi al tuo account</h1>
		<% 
			if ("success".equals(request.getParameter("registrazione"))) {
		%>
			<div class="success-message-global">Registrazione completata! Accedi ora.</div>
		<% 
			} 
		%>
		
		<%-- Errore in caso di login fallito --%>
		<% 
			String errore = (String) request.getAttribute("errore");
			if (errore != null && !errore.isEmpty()) {
		%>
			<div class="error-message-global"><%= errore %></div>
		<% 
			} 
		%>

		<form id="form-login" method="POST" action="login">
			<div class="form-group">
				<label for="username">Username:</label>
				<input type="text" id="username" name="username">
				<div class="error-message" id="errore-username"></div>
			</div>
			
			<div class="form-group">
				<label for="password">Password:</label>
				<input type="password" id="password" name="password">
				<div class="error-message" id="errore-password"></div>
			</div>
			
			<button type="submit" class="btn">Accedi</button>
		</form>
		
		<p>Non hai un account? <a href="registrazione.jsp">Registrati</a></p>
		
	</main>

	<jsp:include page="/footer.jsp" />
	
	<script src="scripts/validazioneLogin.js"></script>
</body>
</html>