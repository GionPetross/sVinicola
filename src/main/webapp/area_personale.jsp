<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.IndirizzoBean" %>
<%@ page import="java.util.List" %>

<%-- 
  Recuperiamo la lista degli indirizzi preparata 
  dalla AreaPersonaleServlet
--%>
<% List<?> indirizzi = (List<?>) request.getAttribute("indirizzi"); %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Personale - sVinicola</title>
    <link rel="stylesheet" type="text/css" href="styles/main.css">
    <link rel="stylesheet" type="text/css" href="styles/form.css"> <%-- Riusiamo lo stile dei form --%>
</head>
<body>
    <div id="bubble-canvas"></div>
    <jsp:include page="/header.jsp" />

    <main>
        
        <%-- Primo Blocco: Gestione Indirizzi --%>
        <div class="form-container" style="margin-bottom: 30px;">
            <h1>I Miei Indirizzi</h1>
            
            <%-- Lista degli indirizzi esistenti --%>
            <div class="lista-indirizzi">
                <% if (indirizzi == null || indirizzi.isEmpty()) { %>
                    <p>Non hai ancora salvato nessun indirizzo.</p>
                <% } else { %>
                    <table class="carrello-tabella"> <%-- Riusiamo lo stile tabella del carrello --%>
                        <thead>
                            <tr>
                                <th>Indirizzo</th>
                                <th>Azione</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% for (Object obj : indirizzi) { 
                             IndirizzoBean indirizzo = (IndirizzoBean) obj;
                        %>
                            <tr>
                                <td>
                                    <%= indirizzo.getVia() %>, <%= indirizzo.getCap() %><br>
                                    <%= indirizzo.getCitta() %> (<%= indirizzo.getProvincia() %>)
                                </td>
                                <td>
                                    <%-- Link alla servlet che gestisce la cancellazione --%>
                                    <a href="gestione-indirizzo?action=delete&id=<%= indirizzo.getIdIndirizzo() %>" class="btn-remove">&times;</a>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                <% } %>
            </div>
            
            <hr style="margin: 20px 0;">
            
            <%-- Form per aggiungere un NUOVO indirizzo --%>
            <h3>Aggiungi un nuovo indirizzo</h3>
            <form id="form-indirizzo" method="POST" action="gestione-indirizzo">
                <input type="hidden" name="action" value="add">
            
                <div class="form-group">
                    <label for="via">Via (e numero civico):</label>
                    <input type="text" id="via" name="via" required>
                </div>
                <div class="form-group">
                    <label for="cap">CAP:</label>
                    <input type="text" id="cap" name="cap" required maxlength="5">
                </div>
                <div class="form-group">
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" required>
                </div>
                <div class="form-group">
                    <label for="provincia">Provincia (Sigla):</label>
                    <input type="text" id="provincia" name="provincia" required maxlength="2">
                </div>
                
                <button type="submit" class="btn">Salva Indirizzo</button>
            </form>
        </div>
        
        <%-- Secondo Blocco: Modifica Profilo --%>
        <div class="form-container">
             <h3>Modifica Profilo</h3>
             <%-- 
               Creeremo "ModificaProfiloServlet" per gestire questo form.
               Dovrà avere logica per validare la password vecchia prima di cambiare.
             --%>
             <form id="form-profilo" method="POST" action="modifica-profilo">
                <div class="form-group">
                    <label for="username">Nuovo Username:</label>
                    <input type="text" id="username" name="username" placeholder="Lascia vuoto per non modificare">
                </div>
                <div class="form-group">
                    <label for="new_password">Nuova Password:</label>
                    <input type="password" id="new_password" name="new_password">
                </div>
                <hr>
                <div class="form-group">
                    <label for="old_password">Password Attuale (Obbligatoria per salvare):</label>
                    <input type="password" id="old_password" name="old_password" required>
                </div>
                
                <button type="submit" class="btn">Salva Modifiche Profilo</button>
             </form>
        </div>

    </main>

    <jsp:include page="/footer.jsp" />
    
    <%-- Qui andrà il JS per validare il form dell'indirizzo e del profilo --%>
    <%-- <script src="scripts/validazioneAreaPersonale.js"></script> --%>
</body>
</html>s