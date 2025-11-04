<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	Integer idOrdine = (Integer) request.getAttribute("idOrdine");
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Ordine Confermato - sVinicola</title>
	<link rel="stylesheet" type="text/css" href="styles/main.css">
	<link rel="stylesheet" type="text/css" href="styles/carrello.css">
</head>
<body>
	<div id="bubble-canvas"></div>
	<jsp:include page="/header.jsp" />

	<main class="carrello-container" style="max-width: 600px; text-align: center;">
		<div class="success-icon" style="font-size: 4rem; color: #4CAF50; margin-bottom: 20px;">
			&#x2714;
		</div>
		<h1>Ordine Confermato con Successo!</h1>

		<p style="font-size: 1.2rem; margin-bottom: 30px;">
			Grazie per il tuo acquisto. Il tuo ordine numero **#<%= idOrdine %>** è stato ricevuto e sarà elaborato a breve.
		</p>
		
		<div style="display: flex; justify-content: center; gap: 20px;">
			<a href="storico-ordini" class="btn">Visualizza Dettagli Ordine</a>
			<a href="home" class="btn-secondary">Torna al Catalogo</a>
		</div>
		
	</main>
	<jsp:include page="/footer.jsp" />
</body>
</html>