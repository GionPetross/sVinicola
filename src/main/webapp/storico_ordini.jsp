<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.OrdineBean" %>
<%@ page import="model.DettaglioOrdineBean" %>
<%@ page import="model.VinoBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
	// Recupero i dati dalla Servlet
	List<?> ordini = (List<?>) request.getAttribute("ordini");
	Map<?, ?> dettagliOrdini = (Map<?, ?>) request.getAttribute("dettagliOrdini");
	Map<?, ?> cacheVini = (Map<?, ?>) request.getAttribute("cacheVini");
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Storico Ordini - sVinicola</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<link rel="stylesheet" type="text/css" href="styles/carrello.css"> 
	<link rel="stylesheet" type="text/css" href="styles/form.css">
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />

	<main class="carrello-container" style="max-width: 900px; margin: 40px auto;">
		<h1 style="text-align: center;">I Tuoi Ordini Passati</h1>

		<% if (ordini == null || ordini.isEmpty()) { %>
			<div class="carrello-vuoto">
				<p>Non hai ancora effettuato nessun ordine.</p>
				<a href="home" class="btn">Inizia a esplorare il Catalogo</a>
			</div>
		<% } else { %>
		
			<%-- Inizio ciclo Ordini --%>
			<% for (Object objOrdine : ordini) { 
				OrdineBean ordine = (OrdineBean) objOrdine;
				int idOrdine = ordine.getIdOrdine();
			%>
				<div class="ordine-card" style="border: 2px solid var(--colore-bordo-etichetta); padding: 20px; margin-bottom: 25px; border-radius: 8px;">
					
					<div class="riepilogo-intestazione" style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px dashed var(--colore-bordo-etichetta); padding-bottom: 10px; margin-bottom: 15px;">
						<p><strong>Ordine #<%= idOrdine %></strong></p>
						<p>Data: <%= dateFormat.format(ordine.getData()) %></p>
						<p>Stato: <span style="font-weight: bold; color: green;"><%= ordine.getStato() %></span></p>
						<p>Totale: <strong><%= ordine.getTotaleComplessivo() %> &euro;</strong></p>
					</div>

					<div class="dettagli-indirizzo" style="font-size: 0.95rem; margin-bottom: 15px;">
						<strong>Spedito a:</strong><br>
						<%= ordine.getViaSpedizione() %>, <%= ordine.getCapSpedizione() %><br>
						<%= ordine.getCittaSpedizione() %> (<%= ordine.getProvinciaSpedizione() %>)
					</div>

					<table class="carrello-tabella" style="margin: 0; width: 100%;">
						<thead>
							<tr>
								<th>Prodotto</th>
								<th>Quantità</th>
								<th>Prezzo di Acquisto</th>
							</tr>
						</thead>
						<tbody>
							<%-- Ciclo sui Dettagli --%>
							<% 
								List<?> articoli = (List<?>) dettagliOrdini.get(idOrdine);
								for (Object objDettaglio : articoli) {
									DettaglioOrdineBean dettaglio = (DettaglioOrdineBean) objDettaglio;
									VinoBean vinoCache = (VinoBean) cacheVini.get(dettaglio.getIdVino());
							%>
							<tr>
								<td>
									<% if (vinoCache != null) { %>
										<%= vinoCache.getNome() %>
									<% } else { %>
										[Prodotto Cancellato]
									<% } %>
								</td>
								<td><%= dettaglio.getQuantita() %></td>
								<td><%= dettaglio.getPrezzoStorico() %> &euro;</td>
							</tr>
							<% } %>
						</tbody>
					</table>
					
				</div>
			<% } %>
			
		<% } %>

	</main>
	<jsp:include page="/footer.jsp" />
</body>
</html>