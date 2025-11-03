<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Classi Import --%>
<%@ page import="model.VinoBean" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<%-- RIMOSSO LO SCRIPT DA QUI --%>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>sVinicola - Benvenuto</title>
	
	<link rel="stylesheet" type="text/css" href="styles/main.css">
</head>
<body>

	<jsp:include page="/header.jsp" />

	<main>
		<%-- Banner promozionali --%>
		<section class="promozioni-banner">
			<div class="banner-scroll-container">
				
				<%-- Esempio di banner 1 (da generare con un ciclo JSP) --%>
				<div class="banner-card">
					<img src="images/banner_esempio_1.jpg" alt="Promo 1">
				</div>
				<%-- Esempio di banner 2 --%>
				<div class="banner-card">
					<img src="images/banner_esempio_2.jpg" alt="Promo 2">
				</div>
				<%-- Esempio di banner 3 --%>
				<div class="banner-card">
					<img src="images/banner_esempio_3.jpg" alt="Promo 3">
				</div>
				
			</div>
		</section>

		<%-- ID per l'autoscroll --%>
		<h2 id="catalogo-start">Il nostro Catalogo</h2>
		
		<div class="catalogo-grid">
			
			<%
				Collection<?> catalogo = (Collection<?>) request.getAttribute("catalogo");
				if (catalogo != null && !catalogo.isEmpty()) {
					for (Object obj : catalogo) {
						VinoBean vino = (VinoBean) obj;
			%>
				<%-- Product card sta qui --%>
				<div class="product-card">
					
					<div class="image-container">
						<a href="dettaglio-prodotto?id=<%= vino.getIdVino() %>">
							<%
								String immaginePath = vino.getImmagine();
								if (immaginePath == null || immaginePath.trim().isEmpty()) {
									immaginePath = "images/placeholder_vino_" + vino.getTipo() + ".png";
								}
							%>
							<img src="<%= immaginePath %>" alt="<%= vino.getNome() %>">
						</a>
						
						<div class="hover-overlay">
							<p><strong>Tipo:</strong> <%= vino.getTipo() %></p>
							<p><strong>Origine:</strong> <%= (vino.getOrigine() != null) ? vino.getOrigine() : "N/D" %></p>
							<p><strong>Alcol:</strong> <%= vino.getPercentualeAlcolica() %> %</p>
						</div>
					</div>
					
					<%-- Contenitore Nome --%>
					<div class="product-info">
						<h3><%= vino.getNome() %></h3>
					</div>
					
					<%-- Contenitore Prezzo--%>
					<div class="product-purchase-bar">
						
						<div class="product-price">
							<%= vino.getPrezzo() %> &euro;
						</div>
						
						<a href="carrello?action=add&id=<%= vino.getIdVino() %>" class="btn-aggiungi">
							<img src="images/aggiungi_carello.png" alt="Aggiungi" class="icon-cart">
							<span class="text-cart">Aggiungi al Carrello</span>
						</a>
					</div>
					
				</div>
			
			<%
					}
				} else {
			%>
			
				<p>Nessun prodotto disponibile al momento.</p>
			
			<%
				}
			%>
			
		</div> 
	</main>

	<jsp:include page="/footer.jsp" />

</body>
</html>