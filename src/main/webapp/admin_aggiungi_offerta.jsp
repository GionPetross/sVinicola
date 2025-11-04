<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Admin - Crea Offerta</title>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />

	<main class="admin-container">
		
		<%-- Stile centrato --%>
		<div class="form-container" style="max-width: 600px; margin: 40px auto;">
			
			<h1 style="text-align: center;">Crea Nuova Offerta</h1>
			<% 
				String errore = (String) session.getAttribute("feedbackErrore");
				if (errore != null) {
			%>
				<div class="error-message-global"><%= errore %></div>
			<% 
					session.removeAttribute("feedbackErrore");
				}
			%>
			
			<form id="form-offerta" method="POST" action="<%= contextPath %>/admin/aggiungi_offerta">
				
				<div class="form-group">
					<label for="percentuale">Percentuale di Sconto (%):</label>
					<input type="number" id="percentuale" name="percentuale" min="1" max="100" required>
					<%-- Qui puoi aggiungere un validatore JS per il range 1-100 --%>
				</div>
				
				<div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
					<div class="form-group">
						<label for="data_inizio">Data Inizio:</label>
						<input type="date" id="data_inizio" name="data_inizio" required>
					</div>
					<div class="form-group">
						<label for="data_fine">Data Fine:</label>
						<input type="date" id="data_fine" name="data_fine" required>
					</div>
				</div>
				
				<div class="form-group">
					<label for="immagine_promozionale">URL Immagine Banner (opzionale):</label>
					<input type="text" id="immagine_promozionale" name="immagine_promozionale" placeholder="Es. images/banner/promo_natale.png">
				</div>
				
				<div style="display: flex; gap: 20px; margin-top: 20px;">
					<button type="submit" class="btn">Salva Offerta</button>
					<a href="<%= contextPath %>/admin/dashboard" class="btn-secondary" style="text-align: center; text-decoration: none;">Annulla</a>
				</div>
			</form>
		</div>

	</main>
	<jsp:include page="/footer.jsp" />
	
</body>
</html>