<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Admin - Aggiungi Vino</title>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />

	<main class="admin-container">
		<div class="form-container" style="max-width: 800px; margin: 40px auto;">
			
			<h1 style="text-align: center;">Aggiungi Vino</h1>
			
			<form id="form-vino" method="POST" action="<%= contextPath %>/admin/aggiungi_vino">
				
				<div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
					
					<div class="form-group">
						<label for="nome">Nome Vino:</label>
						<input type="text" id="nome" name="nome" required>
					</div>
					<div class="form-group">
						<label for="annata">Annata:</label>
						<input type="number" id="annata" name="annata" required>
					</div>
					<div class="form-group">
						<label for="prezzo">Prezzo (&euro;):</label>
						<input type="number" id="prezzo" name="prezzo" step="0.01" required>
					</div>
					<div class="form-group">
						<label for="stock">Stock (Unità):</label>
						<input type="number" id="stock" name="stock" required>
					</div>
					<div class="form-group">
						<label for="tipo">Tipo:</label>
						<select id="tipo" name="tipo" required>
							<option value="Rosso">Rosso</option>
							<option value="Bianco">Bianco</option>
							<option value="Rosé">Rosé</option>
							<option value="Spumante">Spumante</option>
							<option value="Dolce">Dolce</option>
							<option value="Altro">Altro</option>
						</select>
					</div>
					<div class="form-group">
						<label for="formato">Formato (es. 0.75L):</label>
						<input type="text" id="formato" name="formato">
					</div>
					<div class="form-group">
						<label for="origine">Origine/Cantina:</label>
						<input type="text" id="origine" name="origine">
					</div>
					<div class="form-group">
						<label for="alcol">Alcol (%):</label>
						<input type="number" id="alcol" name="alcol" step="0.1">
					</div>
				</div>
				
				<div class="form-group">
					<label for="immagine">(Opzionale) URL Immagine:</label>
					<input type="text" id="immagine" name="immagine">
				</div>
				<div class="form-group">
					<label for="descrizione">Descrizione:</label>
					<textarea id="descrizione" name="descrizione" rows="4"></textarea>
				</div>
				<div style="display: flex; gap: 20px; margin-top: 20px;">
					<button type="submit" class="btn">Salva Nuovo Prodotto</button>
					<a href="<%= contextPath %>/admin/dashboard" class="btn-secondary" style="text-align: center; text-decoration: none;">Annulla</a>
				</div>
			</form>
		</div>

	</main>
	<jsp:include page="/footer.jsp" />
	<script src="<%= contextPath %>/scripts/validazioneAdminVino.js"></script>
</body>
</html>