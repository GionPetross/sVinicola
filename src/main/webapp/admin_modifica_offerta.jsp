<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection" %>
<%@ page import="model.OffertaBean" %>
<%@ page import="model.VinoBean" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
	String contextPath = request.getContextPath();
	
	OffertaBean offerta = (OffertaBean) request.getAttribute("offertaDaModificare");
	Collection<?> tuttiIvini = (Collection<?>) request.getAttribute("tuttiIvini");
	List<?> idViniAssociati = (List<?>) request.getAttribute("idViniAssociati");

	Collection<?> tutteLeOfferte = (Collection<?>) request.getAttribute("tutteLeOfferte");
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Admin - Modifica Offerte</title>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />

	<main class="admin-container">
		
		<%-- Feedback da sessione --%>
		<% 
			String successo = (String) session.getAttribute("feedbackSuccesso");
			String errore = (String) session.getAttribute("feedbackErrore");
			if (successo != null) {
		%>
			<div class="success-message-global"><%= successo %></div>
		<% 
				session.removeAttribute("feedbackSuccesso");
			}
			if (errore != null) {
		%>
			<div class="error-message-global"><%= errore %></div>
		<% 
				session.removeAttribute("feedbackErrore");
			}
		%>
		
		
		<%-- Logica: Mostra il Form di Modifica o la Lista --%>
		
		<% if (offerta != null) { %>

			<div class="form-container" style="max-width: 900px; margin: 20px auto;">
				
				<h1 style="text-align: center;">Modifica Offerta #<%= offerta.getIdOfferta() %></h1>
				
				<form id="form-offerta" method="POST" action="<%= contextPath %>/admin/modifica_offerte">
					<input type="hidden" name="idOfferta" value="<%= offerta.getIdOfferta() %>">
					<div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px;">
						<div class="form-group">
							<label for="percentuale">Percentuale (%):</label>
							<input type="number" id="percentuale" name="percentuale" min="1" max="100" 
								   value="<%= offerta.getPercentuale() %>" required>
						</div>
						<div class="form-group">
							<label for="data_inizio">Data Inizio:</label>
							<input type="date" id="data_inizio" name="data_inizio" 
								   value="<%= dateFormat.format(offerta.getDataInizio()) %>" required>
						</div>
						<div class="form-group">
							<label for="data_fine">Data Fine:</label>
							<input type="date" id="data_fine" name="data_fine" 
								   value="<%= dateFormat.format(offerta.getDataFine()) %>" required>
						</div>
					</div>
					<div class="form-group">
						<label for="immagine_promozionale">URL Immagine Banner:</label>
						<input type="text" id="immagine_promozionale" name="immagine_promozionale" 
							   value="<%= offerta.getImmaginePromozionale() != null ? offerta.getImmaginePromozionale() : "" %>">
					</div>
					
					<hr style="margin: 20px 0;">
					
					<div class="form-group">
						<h3>Associa Vini a questa Offerta</h3>
						<p class="nota">Seleziona tutti i vini (attivi) che devono essere inclusi in questa promozione.</p>
						
						<%-- Contenitore scrollabile per la tabella --%>
						<div style="max-height: 400px; overflow-y: auto; border: 1px solid var(--colore-bordo-etichetta);">
							
							<table class="carrello-tabella" style="margin-bottom: 0;">
								<thead>
									<tr>
										<th>Seleziona</th>
										<th>Nome Vino</th>
										<th>Tipo</th>
										<th>Origine</th>
										<th>Prezzo</th>
									</tr>
								</thead>
								<tbody>
								<% if (tuttiIvini != null && !tuttiIvini.isEmpty()) { 
									for (Object obj : tuttiIvini) {
										VinoBean vino = (VinoBean) obj;
										int idVino = vino.getIdVino();
										
										// Controlla se questo vino è già associato
										boolean isChecked = idViniAssociati.contains(idVino);
								%>
									<tr>
										<td style="text-align: center;">
											<input type="checkbox" name="viniSelezionati" value="<%= idVino %>"
												   <%= (isChecked ? "checked" : "") %> >
										</td>
										<td><%= vino.getNome() %></td>
										<td><%= vino.getTipo() %></td>
										<td><%= (vino.getOrigine() != null ? vino.getOrigine() : "N/D") %></td>
										<td><%= vino.getPrezzo() %> &euro;</td>
									</tr>
								<% 
									} // fine for
								} else { // fine if
								%>
									<tr>
										<td colspan="5" style="text-align: center;">Nessun vino attivo trovato nel catalogo.</td>
									</tr>
								<% } %>
								</tbody>
							</table>
						</div>
					</div>
					
					<div style="display: flex; gap: 20px; margin-top: 20px;">
						<button type="submit" class="btn">Salva Modifiche Offerta</button>
						<a href="<%= contextPath %>/admin/modifica_offerte" class="btn-secondary" style="text-align: center; text-decoration: none;">Annulla</a>
					</div>
				</form>
			</div>
			
		<% } else { %>

			<div class="admin-section">
				<h1 style="text-align: center;">Gestione Offerte</h1>
				<p style="text-align: center;">Clicca "Modifica" per cambiare le date, la percentuale o i vini associati a un'offerta.</p>
				
				<table class="carrello-tabella">
					<thead>
						<tr>
							<th>ID</th>
							<th>Sconto</th>
							<th>Data Inizio</th>
							<th>Data Fine</th>
							<th>Azione</th>
						</tr>
					</thead>
					<tbody>
					<% if (tutteLeOfferte != null && !tutteLeOfferte.isEmpty()) {
						for (Object obj : tutteLeOfferte) {
							OffertaBean o = (OffertaBean) obj;
					%>
						<tr>
							<td><strong>#<%= o.getIdOfferta() %></strong></td>
							<td style="font-weight: bold; color: green;"><%= o.getPercentuale() %>%</td>
							<td><%= o.getDataInizio() %></td>
							<td><%= o.getDataFine() %></td>
							<td>
								<a href="<%= contextPath %>/admin/modifica_offerte?action=modifica&id=<%= o.getIdOfferta() %>" 
								   class="admin-action-btn admin-action-edit">
									Modifica
								</a>
							</td>
						</tr>
					<% 
						} // fine for
					} else { 
					%>
						<tr><td colspan="5" style="text-align: center;">Nessuna offerta creata. <a href="<%= contextPath %>/admin/aggiungi_offerta">Creane una ora</a>.</td></tr>
					<% } %>
					</tbody>
				</table>
			</div>
		
		<% } %>

	</main>
	<jsp:include page="/footer.jsp" />
</body>
</html>