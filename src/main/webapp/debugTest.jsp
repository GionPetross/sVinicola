<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VinoDAO" %>
<%@ page import="model.VinoBean" %>
<%@ page import="model.OrdineDAO" %>
<%@ page import="model.OrdineBean" %>
<%@ page import="model.DettaglioOrdineBean" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.sql.SQLException" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test DAO e DataSource</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
        pre { background-color: #f4f4f4; border: 1px solid #ccc; padding: 10px; }
    </style>
</head>
<body>
    <h1>Test di Connessione e DAO</h1>
    
    <hr>
    <h2>1. Test di SCRITTURA TRANSAZIONALE (OrdineDAO.doSaveCompleto)</h2>
    <%
    try {
        // 1. Istanziamo il DAO
        OrdineDAO ordineDAO = new OrdineDAO();

        // 2. Creiamo i Dettagli dell'Ordine (i prodotti)
        Collection<DettaglioOrdineBean> dettagli = new ArrayList<>();

        // Prodotto 1: 2 bottiglie di Chianti (ID=1)
        DettaglioOrdineBean dett1 = new DettaglioOrdineBean();
        dett1.setIdVino(1); 
        dett1.setQuantita(2);
        dett1.setPrezzoStorico(new BigDecimal("15.50"));
        dettagli.add(dett1);

        // Prodotto 2: 1 bottiglia di Bolgheri (ID=2)
        DettaglioOrdineBean dett2 = new DettaglioOrdineBean();
        dett2.setIdVino(2);
        dett2.setQuantita(1);
        dett2.setPrezzoStorico(new BigDecimal("32.00"));
        dettagli.add(dett2);
        
        // 3. Calcoliamo il totale: (15.50 * 2) + 32.00 = 63.00
        BigDecimal totale = new BigDecimal("63.00");

        // 4. Creiamo l'Ordine (l'intestazione)
        OrdineBean nuovoOrdine = new OrdineBean();
        nuovoOrdine.setIdUtente(1); // L'utente 'admin' (ID=1)
        nuovoOrdine.setIdIndirizzoSpedizione(1); // L'indirizzo (ID=1)
        nuovoOrdine.setTotaleComplessivo(totale);

        // 5. Eseguiamo il salvataggio transazionale
        int idOrdineCreato = ordineDAO.doSaveCompleto(nuovoOrdine, dettagli);

        out.println("<p class='success'>SUCCESS: Transazione riuscita! Creato Ordine con ID: " + idOrdineCreato + "</p>");
        
    } catch (SQLException e) {
        out.println("<p class='error'>FALLITO: Impossibile eseguire la transazione doSaveCompleto().</p>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    } catch (Exception e) {
        out.println("<p class'error'>FALLITO: Errore generico (forse logico).</p>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    }
    %>
    
    <hr>
    <h2>2. Test di LETTURA (OrdineDAO.doRetrieveAll)</h2>
    <%
    try {
        OrdineDAO ordineDAO = new OrdineDAO();
        
        // Usiamo "Data DESC" per vedere i più nuovi in cima
        Collection<OrdineBean> ordini = ordineDAO.doRetrieveAll("Data DESC");
        
        if (ordini != null && !ordini.isEmpty()) {
            out.println("<p class='success'>SUCCESS: Trovati " + ordini.size() + " ordini nel database.</p>");
            out.println("<ul>");
            for (OrdineBean ordine : ordini) {
                out.println("<li><b>Ordine ID: " + ordine.getIdOrdine() + "</b>"
                          + " | Utente ID: " + ordine.getIdUtente()
                          + " | Data: " + ordine.getData()
                          + " | Totale: " + ordine.getTotaleComplessivo() + " &euro;"
                          + "</li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p class='error'>FALLITO: doRetrieveAll() non ha restituito ordini.</p>");
        }
        
    } catch (SQLException e) {
        out.println("<p class='error'>FALLITO: Impossibile eseguire OrdineDAO.doRetrieveAll().</p>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    }
    %>

    <hr>
    <h2>3. Test di SCRITTURA (VinoDAO.doSave)</h2>
    <%
    try {
        VinoDAO vinoDAO = new VinoDAO();
        VinoBean vinoDiTest = new VinoBean();
        vinoDiTest.setNome("Vino di Prova (Test 2)"); // Nome diverso
        vinoDiTest.setAnnata(2025);
        vinoDiTest.setTipo("Bianco");
        vinoDiTest.setPrezzo(new BigDecimal("12.34"));
        vinoDiTest.setStock(5);
        vinoDiTest.setInVendita(true);
        
        vinoDAO.doSave(vinoDiTest);
        
        out.println("<p class='success'>SUCCESS: Nuovo vino 'Vino di Prova (Test 2)' inserito correttamente!</p>");
        
    } catch (SQLException e) {
        out.println("<p class='error'>FALLITO: Impossibile eseguire VinoDAO.doSave().</p>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    }
    %>

    <hr>
    <h2>4. Test di LETTURA (VinoDAO.doRetrieveAll)</h2>
    <%
    try {
        VinoDAO vinoDAO = new VinoDAO();
        Collection<VinoBean> vini = vinoDAO.doRetrieveAll(null);
        
        if (vini != null && !vini.isEmpty()) {
            out.println("<p class='success'>SUCCESS: Trovati " + vini.size() + " vini nel database.</p>");
            out.println("<ul>");
            for (VinoBean vino : vini) {
                out.println("<li>" + vino.getNome() + " (ID: " + vino.getIdVino() + ") - Prezzo: " + vino.getPrezzo() + "</li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p class='error'>FALLITO: VinoDAO.doRetrieveAll() non ha restituito risultati.</p>");
        }
        
    } catch (SQLException e) {
        out.println("<p class='error'>FALLITO: Impossibile eseguire VinoDAO.doRetrieveAll().</Gps>");
        out.println("<pre>");
        e.printStackTrace(new java.io.PrintWriter(out));
        out.println("</pre>");
    }
    %>

</body>
</html>