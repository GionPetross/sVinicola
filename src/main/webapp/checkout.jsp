<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.VoceCarrello" %>
<%@ page import="model.IndirizzoBean" %>
<%@ page import="java.util.List" %>

<%
	Carrello carrello = (Carrello) session.getAttribute("carrello");
	List<?> indirizzi = (List<?>) request.getAttribute("indirizziUtente");
	
	if (carrello == null || carrello.isEmpty()) {
		response.sendRedirect("carrello");
		return;
	}
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Checkout - sVinicola</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<link rel="stylesheet" type="text/css" href="styles/carrello.css"> <%-- Riusiamo lo stile tabella --%>
	<link rel="stylesheet" type="text/css" href="styles/form.css">
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />

	<main class="carrello-container" style="max-width: 700px; margin: 40px auto;">
		<h1>Checkout: Riepilogo Ordine</h1>

		<%-- 1. MESSAGGIO DI ERRORE (Se la POST è fallita) --%>
		<% 
			String messaggioErrore = (String) request.getAttribute("messaggioErrore");
			if (messaggioErrore != null) {
		%>
			<div class="error-message-global" style="margin-bottom: 20px;"><%= messaggioErrore %></div>
		<%
			}
		%>

		<div class="checkout-sezione">
			<h3>Dettagli Carrello</h3>
			<table class="carrello-tabella">
				<thead>
					<tr>
						<th>Prodotto</th>
						<th>Quantità</th>
						<th>Prezzo Unitario</th>
						<th>Subtotale</th>
					</tr>
				</thead>
				<tbody>
					<% for (VoceCarrello voce : carrello.getVoci()) { %>
					<tr>
						<td><%= voce.getVino().getNome() %></td>
						<td><%= voce.getQuantita() %></td>
						<td><%= voce.getVino().getPrezzo() %> &euro;</td>
						<td><%= voce.getSubtotale() %> &euro;</td>
					</tr>
					<% } %>
				</tbody>
			</table>
			<div class="totale" style="text-align: right; margin-top: 10px;">
				<strong>Totale Complessivo: <%= carrello.getTotale() %> &euro;</strong>
			</div>
		</div>

		<hr style="margin: 30px 0;">

		<%-- 2. Form per la Scelta dell'Indirizzo e Conferma --%>
		<form method="POST" action="checkout" class="form-style-checkout">
			
			<div class="checkout-sezione">
				<h3>1. Indirizzo di Spedizione</h3>
				<div class="form-group">
					<label for="idIndirizzo">Scegli un indirizzo salvato:</label>
					<select id="idIndirizzo" name="idIndirizzo" required>
						<% 
						// La verifica "indirizzi è vuoto" è stata fatta nella Servlet
						for (Object obj : indirizzi) { 
							IndirizzoBean indirizzo = (IndirizzoBean) obj;
						%>
							<option value="<%= indirizzo.getIdIndirizzo() %>">
								<%= indirizzo.getVia() %>, <%= indirizzo.getCap() %> - <%= indirizzo.getCitta() %> (<%= indirizzo.getProvincia() %>)
							</option>
						<% } %>
					</select>
				</div>
				
				<p class="nota">Vuoi aggiungere o modificare un indirizzo? Vai alla tua <a href="area-personale">Area Personale</a>.</p>
			</div>
			
			<div style="text-align: center; margin-top: 40px;">
				<button type="submit" class="btn btn-confirm">
					CONFERMA ORDINE (Totale: <%= carrello.getTotale() %> &euro;)
				</button>
			</div>
			
		</form>

	</main>
	<jsp:include page="/footer.jsp" />
</body>
</html>