<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.VoceCarrello" %>
<%@ page import="java.math.BigDecimal" %>

<%
    Carrello carrello = (Carrello) session.getAttribute("carrello");
    
    if (carrello == null) {
        carrello = new Carrello();
        session.setAttribute("carrello", carrello);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il tuo Carrello - sVinicola</title>
    <link rel="stylesheet" type="text/css" href="styles/main.css">
    <link rel="stylesheet" type="text/css" href="styles/carrello.css"> 
</head>
<body>
    <div id="bubble-canvas"></div>
    <jsp:include page="/header.jsp" />

    <main class="carrello-container">
        <h1>Il tuo Carrello</h1>

        <% if (carrello.isEmpty()) { %>
            
            <div class="carrello-vuoto">
                <a href="home" class="btn">Torna al Catalogo</a>
            </div>

        <% } else { %>

            <div class="carrello-pieno">
                
                <table class="carrello-tabella">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo</th>
                            <th>Quantità</th>
                            <th>Subtotale</th>
                            <th>Rimuovi</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (VoceCarrello voce : carrello.getVoci()) { 
                            int idVino = voce.getVino().getIdVino();
                        %>
                       
                        <tr id="riga-prodotto-<%= idVino %>">
                            <td><%= voce.getVino().getNome() %></td>
                            <td><%= voce.getVino().getPrezzo() %> &euro;</td>
                            
                            <td>
                                <input type="number" 
                                       value="<%= voce.getQuantita() %>" 
                                       min="0"
                                       max="99" 
                                       class="input-quantita ajax-update" 
                                       data-id="<%= idVino %>"> 
                            </td>
                            
                            <td id="subtotale-<%= idVino %>"><%= voce.getSubtotale() %> &euro;</td>
                            <td>
                                <button class="btn-remove ajax-remove" data-id="<%= idVino %>">&times;</button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                
                <div class="carrello-riepilogo">
                    <div class="totale">
                        <strong>Totale: <span id="carrello-totale"><%= carrello.getTotale() %></span> &euro;</strong>
                    </div>
                    <div class="carrello-azioni">
                        <a href="carrello?action=clear" class="btn-secondary">Svuota Carrello</a>
                        
                        <a href="checkout" class="btn">Procedi al Checkout</a>
                    </div>
                </div>
            </div>
            
        <% } %>

    </main>
    <jsp:include page="/footer.jsp" />
    <script src="scripts/carrello-ajax.js"></script>
</body>
</html>