<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Registrati - sVinicola</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />
	<div id="bubble-canvas"></div>

	<main class="form-container">
		<h1>Crea un nuovo account</h1>
		

		<form id="form-registrazione" method="POST" action="registrazione">
			
			<%-- Blocco per un errore generico --%>
			<% 
				String errore = (String) request.getAttribute("errore");
				if (errore != null && !errore.isEmpty()) {
			%>
				<div class="error-message-global"><%= errore %></div>
			<% 
				} 
			%>

			<div class="form-group">
				<label for="nome">Nome:</label>
				<input type="text" id="nome" name="nome">
				<div class="error-message" id="errore-nome"></div>
			</div>
			
			<div class="form-group">
				<label for="cognome">Cognome:</label>
				<input type="text" id="cognome" name="cognome">
				<div class="error-message" id="errore-cognome"></div>
			</div>
			
			<div class="form-group">
				<label for="username">Username:</label>
				<input type="text" id="username" name="username">
				<div class="error-message" id="errore-username"></div>
			</div>
			
			<div class="form-group">
				<label for="email">Email:</label>
				<input type="email" id="email" name="email">
				<div class="error-message" id="errore-email"></div>
			</div>

			<div class="form-group">
				<label for="password">Password:</label>
				<input type="password" id="password" name="password">
				<div class="error-message" id="errore-password"></div>
			</div>
			
			<div class="form-group">
				<label for="conferma-password">Conferma Password:</label>
				<input type="password" id="conferma-password" name="conferma-password">
				<div class="error-message" id="errore-conferma-password"></div>
			</div>

			<button type="submit" class="btn">Registrati</button>
		</form>
		
		<p>Hai già un account? <a href="login.jsp">Accedi ora</a></p>
	
	</main>
	
	<script src="scripts/validazioneRegistrazione.js"></script>
	<jsp:include page="/footer.jsp" />
</body>
</html>