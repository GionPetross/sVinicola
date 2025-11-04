<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBean" %>
<%@ page import="model.Carrello" %>
<%
	UtenteBean utente = (UtenteBean) session.getAttribute("utente");
	String contextPath = request.getContextPath(); // Variabile per pulizia
%>
<header>
	<div class="header-container">
		
		<button id="sidebar-toggle-btn" class="sidebar-toggle">
			<span></span>
			<span></span>
			<span></span>
		</button>
		
		<div class="logo">
			<a href="<%= contextPath %>/home">
				<img src="<%= contextPath %>/images/logo.png" alt="sVinicola Logo" class="header-logo-img invert-on-dark">
			</a>
		</div>
		
		<nav class="main-nav">
            <ul>
                <li class="nav-search-bar">
                    <form action="<%= contextPath %>/home" method="GET" class="filter-form">
                        <input type="text" name="search" placeholder="Cerca un vino...">
                    </form>
                </li>
            </ul>
        </nav>
		
		<div class="user-nav">
			<ul>
				<li>
					<button id="theme-toggle-btn" class="theme-toggle">
						Tema
					</button>
				</li>
				
				<li>
					<a href="<%= contextPath %>/carrello" class="header-icon-link cart-link-container">
                        <img src="<%= contextPath %>/images/carrello.png" alt="Carrello" class="header-icon-img invert-on-dark">
                        
                        <%
                            Carrello carrello = (Carrello) session.getAttribute("carrello");
                            if (carrello != null && !carrello.isEmpty()) {
                        %>
                                <span class="cart-badge"><%= carrello.getNumVoci() %></span>
                        <%
                            }
                        %>
                    </a>
				</li>
				
				<%
					if (utente == null) {
				%>
					<li><a href="<%= contextPath %>/login.jsp">Login</a></li>
					<li><a href="<%= contextPath %>/registrazione.jsp">Registrati</a></li>
				<%
					} else {
				%>
					<li class="welcome-msg"><a href="<%= contextPath %>/area_personale">Ben Tornato, <%= utente.getNomeUtente() %></a></li>
					
					<%
						if (utente.getRuolo().equals("admin")) {
					%>
						<li><a href="<%= contextPath %>/admin/dashboard">Pannello Admin</a></li>
					<%
						} else {
					%>
						<li><a href="<%= contextPath %>/storico-ordini">I Miei Ordini</a></li>
					<%
						}
					%>
					
					<li><a href="<%= contextPath %>/logout">Logout</a></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
</header>

<div class="sidebar">
    <button id="sidebar-close-btn" class="sidebar-close">&times;</button>
    <h3>Filtri Catalogo</h3>

    <%-- AJAX filter --%>
    
    <div class="filter-group">
        <h4>Tipo di Vino</h4>
        <select id="filtro-tipo" name="tipo" class="filtro-campo">
            <option value="">Tutti i tipi</option>
            <option value="Rosso">Vini Rossi</option>
            <option value="Bianco">Vini Bianchi</option>
            <option value="Rosé">Vini Rosé</option>
            <option value="Spumante">Spumanti</option>
            <option value="Dolce">Vini Dolci</option>
        </select>
    </div>
    
    <div class="filter-group">
        <h4>Regione</h4>
        <input type="text" id="filtro-origine" name="origine" class="filtro-campo" placeholder="Es. Toscana...">
    </div>
    
    <div class="filter-group">
        <h4>Percentuale Alcolica</h4>
        <select id="filtro-alcol-op" name="alcol_op" class="filtro-campo">
            <option value="">Qualsiasi</option>
            <option value="gt">Maggiore di</option>
            <option value="lt">Minore di</option>
        </select>
        <input type="number" id="filtro-alcol-val" name="alcol_val" class="filtro-campo" step="0.1" placeholder="13.5" style="width: 60px;">
    </div>
    
    <div class="filter-group">
        <h4>Offerte</h4>
        <label class="checkbox-label">
            <input type="checkbox" id="filtro-promozione" name="in_promozione" value="true" class="filtro-campo">
            Solo vini in promozione
        </label>
    </div>
</div>