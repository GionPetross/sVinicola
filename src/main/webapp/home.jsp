<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VinoBean" %>
<%@ page import="model.OffertaBean" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>sVinicola - Benvenuto</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />
	<main>
		<section class="promozioni-banner">
			<div class="banner-scroll-container">
				<button id="banner-scroll-left" class="banner-scroll-btn">&lt;</button>
				
				<div class="banner-scroll-viewport">
					<div class="banner-scroll-container">
						
						<%
							Collection<?> offerte = (Collection<?>) request.getAttribute("offerte");
							if (offerte != null && !offerte.isEmpty()) {
								// Se ci sono offerte, cicala su di esse
								for (Object obj : offerte) {
									OffertaBean offerta = (OffertaBean) obj;
									String imgPromo = offerta.getImmaginePromozionale();
									
									if (imgPromo != null && !imgPromo.trim().isEmpty()) {
						%>
										<div class="banner-card">
											<a href="offerta?id=<%= offerta.getIdOfferta() %>">
												<img src="<%= imgPromo %>" alt="Promozione <%= offerta.getPercentuale() %>%">
											</a>
										</div>
						<%
									}
								}
							} else {
								// Se non ci sono offerte, mostra il fallback
						%>
								<div class="banner-card"> <%-- Inserito in un banner-card per lo slider --%>
									<img src="images/nessuna_offerta.png" alt="Nessuna offerta disponibile">
								</div>
						<%
							}
						%>
						
					</div>
				</div>
				<button id="banner-scroll-right" class="banner-scroll-btn">&gt;</button>
			</div>
		</section>
		<h2 id="catalogo-start">Il nostro Catalogo</h2>
		
		<div class="catalogo-grid">
			
			<%
				Collection<?> catalogo = (Collection<?>) request.getAttribute("catalogo");
				if (catalogo != null && !catalogo.isEmpty()) {
					for (Object obj : catalogo) {
						VinoBean vino = (VinoBean) obj;
			%>
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
					<div class="product-info">
						<h3><%= vino.getNome() %></h3>
					</div>
					<div class="product-purchase-bar">
						<div class="product-price">
							<%= vino.getPrezzo() %> &euro;
						</div>
						<a href="carrello?action=add&id=<%= vino.getIdVino() %>" class="btn-aggiungi">
							<img src="images/aggiungi_carello.png" alt="Aggiungi" class="icon-cart invert-on-dark">
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