<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VinoBean" %>
<%@ page import="model.OffertaBean" %>
<%@ page import="java.util.List" %>

<%
	// Recupero i dati dalla Servlet
	VinoBean vino = (VinoBean) request.getAttribute("vino");
	List<?> offerte = (List<?>) request.getAttribute("offerteAttive");
	
	// Controllo di sicurezza
	if (vino == null) {
		response.sendRedirect("home");
		return;
	}
	
	// Prepara l'immagine placeholder
	String immaginePath = vino.getImmagine();
	if (immaginePath == null || immaginePath.trim().isEmpty()) {
		immaginePath = "images/placeholder_vino_" + vino.getTipo() + ".png";
	}
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><%= vino.getNome() %> - Dettaglio</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<link rel="stylesheet" type="text/css" href="styles/dettaglio.css"> <%-- Stile specifico --%>
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />

	<main class="dettaglio-container">
		
		<div class="prodotto-scheda stile-etichetta">
			
			<%-- Sezione Immagine e Acquisto --%>
			<div class="colonna-immagine">
				<img src="<%= immaginePath %>" alt="<%= vino.getNome() %> - Immagine" class="prodotto-immagine">
				
				<div class="prezzo-acquisto">
					<div class="prezzo-base">
						Prezzo: <strong><%= vino.getPrezzo() %> &euro;</strong>
					</div>
					<div class="aggiungi-form" id="catalogo-container">
					    <a href="carrello?action=add&id=<%= vino.getIdVino() %>" class="btn-aggiungi fake-btn">
					        <span class="fake-btn-label">Aggiungi al Carrello</span>
					    </a>
					</div>
					
					<div class="stock-info">
						<% if (vino.getStock() > 0) { %>
							<span style="color: green;">Disponibilità immediata (<%= vino.getStock() %> unità)</span>
						<% } else { %>
							<span style="color: red;">Esaurito</span>
						<% } %>
					</div>
				</div>
			</div>
			
			<%-- Sezione Dettagli e Descrizione --%>
			<div class="colonna-dettagli">
				<h1><%= vino.getNome() %></h1>
				<p class="descrizione"><%= vino.getDescrizione() %></p>
				
				<hr>
				
				<div class="lista-dettagli">
					<p><strong>Tipo:</strong> <%= vino.getTipo() %></p>
					<p><strong>Annata:</strong> <%= vino.getAnnata() %></p>
					<p><strong>Origine / Cantina:</strong> <%= vino.getOrigine() %></p>
					<p><strong>Formato:</strong> <%= vino.getFormato() %></p>
					<p><strong>Grado Alcolico:</strong> <%= vino.getPercentualeAlcolica() %> %</p>
					<p><strong>Aggiunto il:</strong> <%= vino.getDataAggiunta() %></p>
				</div>
				
				<%-- 3Sezione Offerte --%>
				<% if (offerte != null && !offerte.isEmpty()) { %>
					<div class="sezione-offerte">
						<h3>Offerte Attive</h3>
						<ul>
							<% for (Object objOfferta : offerte) { 
								OffertaBean offerta = (OffertaBean) objOfferta;
							%>
								<li style="color: var(--colore-accento); font-weight: bold;">
									<%= offerta.getPercentuale() %>% di sconto (fino al <%= offerta.getDataFine() %>)
								</li>
							<% } %>
						</ul>
					</div>
				<% } %>
			</div>
			
		</div>

	</main>
	<jsp:include page="/footer.jsp" />
	
	<%-- AJAX per aggiunta al carrello --%>
	<script src="scripts/carrello-ajax.js"></script>
</body>
</html>