<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.VinoBean" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>

<%--Catalogo isolato per l'AJAX--%>

<div class="catalogo-grid" id="catalogo-grid">
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
		            <span class="icon-cart">
		                <img src="images/aggiungi_carello.png" alt="Aggiungi al Carrello">
		            </span>
		            <span class="text-cart">Aggiungi</span>
		            
		        </a>
		    </div>
		</div>
	
	<%
			}
		} else {
	%>
		<p>Nessun prodotto trovato per i filtri selezionati.</p>
	<%
		}
	%>
</div>