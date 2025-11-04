<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.OrdineBean" %>
<%@ page import="model.UtenteBean" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
	List<?> ordiniInAttesa = (List<?>) request.getAttribute("ordiniInAttesa");
	List<?> ordiniCompleti = (List<?>) request.getAttribute("ordiniCompleti");
	Map<?, ?> mappaUtenti = (Map<?, ?>) request.getAttribute("mappaUtenti");
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale-1.0">
	<title>Admin - Dashboard</title>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/styles/main.css">
</head>
<body>
	<jsp:include page="/header.jsp" />

	<main class="admin-container">
		<h1 style="text-align: center;">Pannello Amministratore</h1>

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
	
		<%-- Roba per l'admin --%>
		<div class="admin-section">
			<h2>Gestione Catalogo</h2>
			<div class="admin-quick-links">
				<a href="<%= contextPath %>/admin/aggiungi_vino" class="btn">Aggiungi Vino</a>
				<a href="<%= contextPath %>/admin/modifica_vino" class="btn">Modifica Vini Esistenti</a>
				<a href="<%= contextPath %>/admin/aggiungi_offerta" class="btn">Crea Offerta</a>
				<a href="<%= contextPath %>/admin/modifica_offerte" class="btn">Modifica Offerte</a>
			</div>
		</div>

		<%-- === MODIFICA QUI: Inizio del Form Unico === --%>
		<form method="POST" action="<%= contextPath %>/admin/dashboard">
			<div class="admin-section">
				<h2>Ordini da Processare (In Attesa)</h2>
				<table class="carrello-tabella">
					<thead>
						<tr>
							<th>ID Ordine</th>
							<th>Data</th>
							<th>Cliente</th>
							<th>Indirizzo Spedizione</th>
							<th>Totale</th>
							<th>Stato Attuale</th>
							<th>Nuovo Stato</th> <%-- Colonna "Azione" rinominata --%>
						</tr>
					</thead>
					<tbody>
					<% if (ordiniInAttesa != null && !ordiniInAttesa.isEmpty()) {
						for (Object obj : ordiniInAttesa) {
							OrdineBean ordine = (OrdineBean) obj;
							UtenteBean cliente = (UtenteBean) mappaUtenti.get(ordine.getIdUtente());
					%>
						<tr>
							<td><strong>#<%= ordine.getIdOrdine() %></strong></td>
							<td><%= dateFormat.format(ordine.getData()) %></td>
							<td><%= (cliente != null) ? cliente.getNomeUtente() : "ID: " + ordine.getIdUtente() %></td>
							<td>
								<%= ordine.getViaSpedizione() %>, <%= ordine.getCapSpedizione() %>, <%= ordine.getCittaSpedizione() %> (<%= ordine.getProvinciaSpedizione() %>)
							</td>
							<td><%= ordine.getTotaleComplessivo() %> &euro;</td>
							<td style="font-weight: bold; color: #cc8400;"><%= ordine.getStato() %></td>
							<td>
								<%-- 
								  MODIFICA: 
								  Rimosso il form.
								  Il 'name' ora è dinamico (es. "stato-123")
								  per identificare l'ID dell'ordine.
								--%>
								<select name="stato-<%= ordine.getIdOrdine() %>">
									<% if (ordine.getStato().equals("in attesa")) { %>
										<option value="in attesa" selected>In Attesa</option>
									<% } else { %>
										<option value="in preparazione" selected>In Preparazione</option>
									<% } %>
									<option value="consegnato">Consegnato</option>
									<option value="annullato">Annullato</option>
								</select>
							</td>
						</tr>
					<% 
						} // fine for
					} else {
					%>
						<tr><td colspan="7" style="text-align: center;">Nessun ordine da processare al momento.</td></tr>
					<% } %>
					</tbody>
				</table>
				
				<%-- === MODIFICA QUI: Bottone Unico === --%>
				<% if (ordiniInAttesa != null && !ordiniInAttesa.isEmpty()) { %>
				<div style="text-align: right; margin-top: 20px;">
					<button type="submit" class="btn">Aggiorna Tutti gli Stati</button>
				</div>
				<% } %>
			</div>
		</form>
		<%-- === MODIFICA QUI: Fine del Form Unico === --%>
		
		
		<%-- Sezione 3: Ordini "Completi" (Invariata) --%>
		<div class="admin-section">
			<h2>Storico Ordini Completi (Consegnati / Annullati)</h2>
			<table class="carrello-tabella">
				<thead>
					<tr>
						<th>ID Ordine</th>
						<th>Data</th>
						<th>Cliente</th>
						<th>Indirizzo Spedizione</th>
						<th>Totale</th>
						<th>Stato Finale</th>
					</tr>
				</thead>
				<tbody>
				<% if (ordiniCompleti != null && !ordiniCompleti.isEmpty()) {
					for (Object obj : ordiniCompleti) {
						OrdineBean ordine = (OrdineBean) obj;
						UtenteBean cliente = (UtenteBean) mappaUtenti.get(ordine.getIdUtente());
				%>
					<tr>
						<td>#<%= ordine.getIdOrdine() %></td>
						<td><%= dateFormat.format(ordine.getData()) %></td>
						<td><%= (cliente != null) ? cliente.getNomeUtente() : "ID: " + ordine.getIdUtente() %></td>
						<td>
							<%= ordine.getViaSpedizione() %>, <%= ordine.getCapSpedizione() %>, <%= ordine.getCittaSpedizione() %> (<%= ordine.getProvinciaSpedizione() %>)
						</td>
						
						<td><%= ordine.getTotaleComplessivo() %> &euro;</td>
						<td style="font-weight: bold; color: <%= ordine.getStato().equals("consegnato") ? "green" : "red" %>;">
							<%= ordine.getStato() %>
						</td>
					</tr>
				<% 
					} // fine for
				} else {
				%>
					<tr><td colspan="6" style="text-align: center;">Nessun ordine completato.</td></tr>
				<% } %>
				</tbody>
			</table>
		</div>

	</main>
	<jsp:include page="/footer.jsp" />
</body>
</html>