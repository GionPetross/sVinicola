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
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<title>sVinicola - Benvenuto</title>
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />
	<main>
		<section class="promozioni-banner">
			<%
				Collection<?> offerte = (Collection<?>) request.getAttribute("offerte");
				int offertaCount = 0;
				
				// 1. Contiamo le offerte valide
				if (offerte != null) {
					for (Object obj : offerte) {
						OffertaBean offerta = (OffertaBean) obj;
						String imgPromo = offerta.getImmaginePromozionale();
						if (imgPromo != null && !imgPromo.trim().isEmpty()) {
							offertaCount++;
						}
					}
				}
				
				if (offertaCount == 0) {
			%>
					<div class="slideshow-container fallback">
						 <div class="mySlides fade" style="display:block;">
							 <img src="images/nessuna_offerta.png" alt="Nessuna offerta disponibile">
						 </div>
					</div>
			<%
				} else {
			%>
					<%-- Contenitore Slider --%>
					<div class="slideshow-container">
			<%
						int slideCounter = 1;
						for (Object obj : offerte) {
							OffertaBean offerta = (OffertaBean) obj;
							String imgPromo = offerta.getImmaginePromozionale();
							
							if (imgPromo != null && !imgPromo.trim().isEmpty()) {
			%>
								<div class="mySlides fade">
									<div class="numbertext"><%= slideCounter %> / <%= offertaCount %></div>
									<a href="home?offerta_id=<%= offerta.getIdOfferta() %>" 
									   class="banner-link" 
									   data-offerta-id="<%= offerta.getIdOfferta() %>">
										<img src="<%= imgPromo %>" alt="Promozione <%= offerta.getPercentuale() %>%">
									</a>
								</div>
			<%
								slideCounter++;
							}
						}
			%>
						<a class="prev" onclick="plusSlides(-1)">&#10094;</a>
						<a class="next" onclick="plusSlides(1)">&#10095;</a>
					</div>
					
			<%
				} 
			%>
		</section>
		<h2 id="catalogo-start">Il nostro Catalogo</h2>
		
		<div id="catalogo-container">
            <jsp:include page="_catalogo.jsp" />
        </div>
	</main>
	<script src="scripts/filters-ajax.js"></script>
	<script src="scripts/carrello-ajax.js"></script>
	<jsp:include page="/footer.jsp" />

</body>
</html>