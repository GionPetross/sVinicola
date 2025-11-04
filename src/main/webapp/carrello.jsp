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
                            
                            <%-- 
                              MODIFICA 2: 
                              Rimosso il <form>. 
                              Aggiunte classi e attributi "data-" per JS.
                            --%>
                            <td>
                                <input type="number" 
                                       value="<%= voce.getQuantita() %>" 
                                       min="0" <%-- min="0" permette di rimuovere impostando a 0 --%>
                                       max="99" 
                                       class="input-quantita ajax-update" 
                                       data-id="<%= idVino %>"> <%-- Il JS legge questo ID --%>
                            </td>
                            
                            <%-- 
                              MODIFICA 3: 
                              Diamo un ID al Subtotale per 
                              permettere a JS di aggiornarlo.
                            --%>
                            <td id="subtotale-<%= idVino %>"><%= voce.getSubtotale() %> &euro;</td>
                            
                            <%-- 
                              MODIFICA 4: 
                              Rimosso <a>. 
                              Usiamo un <button> con classi e "data-" per JS.
                            --%>
                            <td>
                                <button class="btn-remove ajax-remove" data-id="<%= idVino %>">&times;</button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                
                <div class="carrello-riepilogo">
                    <div class="totale">
                        <%-- 
                          MODIFICA 5: 
                          Aggiunto uno <span> con ID attorno al valore totale
                          per permettere a JS di aggiornarlo.
                        --%>
                        <strong>Totale: <span id="carrello-totale"><%= carrello.getTotale() %></span> &euro;</strong>
                    </div>
                    <div class="carrello-azioni">
                        <%-- Questo link ricarica la pagina (corretto) --%>
                        <a href="carrello?action=clear" class="btn-secondary">Svuota Carrello</a>
                        
                        <%-- Link al checkout (prossimo passo) --%>
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