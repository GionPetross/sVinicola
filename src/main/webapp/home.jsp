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
		
		<div id="catalogo-container">
            <jsp:include page="_catalogo.jsp" />
        </div>
	</main>
	<script src="scripts/filters-ajax.js"></script>
	<script src="scripts/carrello-ajax.js"></script>
	<jsp:include page="/footer.jsp" />

</body>
</html>