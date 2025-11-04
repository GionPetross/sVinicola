<%-- File: admin_modifica_vino.jsp (Aggiornato con nuove classi CSS) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VinoBean" %>
<%@ page import="java.util.Collection" %>
<%
	String contextPath = request.getContextPath();
	Collection<?> catalogo = (Collection<?>) request.getAttribute("catalogoCompleto");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Admin - Modifica Vini</title>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />

	<main class="admin-container">
		
		<h1 style="text-align: center;">Modifica Catalogo Vini</h1>
		
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

		<%-- Form per MODIFICA --%>
		<div id="form-container-modifica" class="form-container" style="max-width: 800px; margin: 20px auto 40px auto; display: none;">
			
			<h2 id="form-titolo">Modifica Prodotto</h2>
			
			<form id="form-vino" method="POST" action="<%= contextPath %>/admin/modifica_vino">
				
				<input type="hidden" id="form-idVino" name="idVino">
				
				<div class="form-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
					<div class="form-group">
						<label for="form-nome">Nome Vino:</label>
						<input type="text" id="form-nome" name="nome" required>
					</div>
					<div class="form-group">
						<label for="form-annata">Annata:</label>
						<input type="number" id="form-annata" name="annata" required>
					</div>
					<div class="form-group">
						<label for="form-prezzo">Prezzo (&euro;):</label>
						<input type="number" id="form-prezzo" name="prezzo" step="0.01" required>
						<div class="error-message" id="errore-prezzo"></div>
					</div>
					<div class="form-group">
						<label for="form-stock">Stock (Unità):</label>
						<input type="number" id="form-stock" name="stock" required>
						<div class="error-message" id="errore-stock"></div>
					</div>
					<div class="form-group">
						<label for="form-tipo">Tipo:</label>
						<select id="form-tipo" name="tipo" required>
							<option value="Rosso">Rosso</option>
							<option value="Bianco">Bianco</option>
							<option value="Rosé">Rosé</option>
							<option value="Spumante">Spumante</option>
							<option value="Dolce">Dolce</option>
							<option value="Altro">Altro</option>
						</select>
					</div>
					<div class="form-group">
						<label for="form-formato">Formato:</label>
						<input type="text" id="form-formato" name="formato">
					</div>
					<div class="form-group">
						<label for="form-origine">Origine/Cantina:</label>
						<input type="text" id="form-origine" name="origine">
					</div>
					<div class="form-group">
						<label for="form-alcol">Alcol (%):</label>
						<input type="number" id="form-alcol" name="alcol" step="0.1">
						<div class="error-message" id="errore-alcol"></div>
					</div>
				</div>
				<div class="form-group">
					<label for="form-immagine">URL Immagine:</label>
					<input type="text" id="form-immagine" name="immagine">
				</div>
				<div class="form-group">
					<label for="form-descrizione">Descrizione:</label>
					<textarea id="form-descrizione" name="descrizione" rows="4"></textarea>
				</div>
				<div class="form-group">
					<label class="checkbox-label">
						<input type="checkbox" id="form-in_vendita" name="in_vendita" value="true">
						In Vendita (visibile nel catalogo)
					</label>
				</div>
				<div style="display: flex; gap: 20px; margin-top: 20px;">
					<button type="submit" class="btn">Salva Modifiche</button>
					<button type="button" class="btn-secondary" id="annulla-modifica-btn">Annulla</button>
				</div>
			</form>
		</div>

		<%-- Tabella Catalogo Completo --%>
		<h2>Catalogo Completo (Ordinato per Stato)</h2>
		<table class="carrello-tabella">
			<thead>
				<tr>
					<th>ID</th>
					<th>Nome</th>
					<th>Prezzo</th>
					<th>Stock</th>
					<th>Stato (In Vendita)</th>
					<th>Modifica</th>
					<th>Azione</th>
				</tr>
			</thead>
			<tbody>
			<% if (catalogo != null && !catalogo.isEmpty()) {
				for (Object obj : catalogo) {
					VinoBean vino = (VinoBean) obj;
			%>
				<tr id="riga-vino-<%= vino.getIdVino() %>"
					data-id="<%= vino.getIdVino() %>"
					data-nome="<%= vino.getNome() %>"
					data-annata="<%= vino.getAnnata() %>"
					data-prezzo="<%= vino.getPrezzo() %>"
					data-stock="<%= vino.getStock() %>"
					data-tipo="<%= vino.getTipo() %>"
					data-formato="<%= vino.getFormato() != null ? vino.getFormato() : "" %>"
					data-origine="<%= vino.getOrigine() != null ? vino.getOrigine() : "" %>"
					data-alcol="<%= vino.getPercentualeAlcolica() %>"
					data-immagine="<%= vino.getImmagine() != null ? vino.getImmagine() : "" %>"
					data-descrizione="<%= vino.getDescrizione() != null ? vino.getDescrizione() : "" %>"
					data-in_vendita="<%= vino.isInVendita() %>"
				>
					<td><%= vino.getIdVino() %></td>
					<td><%= vino.getNome() %></td>
					<td><%= vino.getPrezzo() %> &euro;</td>
					<td><%= vino.getStock() %></td>
					<td style="font-weight: bold; color: <%= vino.isInVendita() ? "green" : "red" %>;">
						<%= vino.isInVendita() ? "ATTIVO" : "DISATTIVATO" %>
					</td>
					
					<td>
						<a href="#" class="admin-action-btn admin-action-edit" onclick="popolaFormModifica(<%= vino.getIdVino() %>); return false;">
							Modifica
						</a>
					</td>
					
					<td>
						<% if (vino.isInVendita()) { %>
							<a href="<%= contextPath %>/admin/modifica_vino?action=delete&id=<%= vino.getIdVino() %>" class="admin-action-btn admin-action-disable">
							   Disattiva
							</a>
						<% } else { %>
							<a href="<%= contextPath %>/admin/modifica_vino?action=restore&id=<%= vino.getIdVino() %>" class="admin-action-btn admin-action-enable">
							   Riattiva
							</a>
						<% } %>
					</td>
				</tr>
			<% 
				} // fine for
			} // fine if
			%>
			</tbody>
		</table>
	</main>
	
	<jsp:include page="/footer.jsp" />
	
	<script src="<%= contextPath %>/scripts/validazioneAdminVino.js"></script>
	<script src="<%= contextPath %>/scripts/nascondiModifica.js"></script>
</body>
</html>