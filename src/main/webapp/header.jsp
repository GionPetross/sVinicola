<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Import --%>
<%@ page import="model.UtenteBean" %>

<%
	UtenteBean utente = (UtenteBean) session.getAttribute("utente");
%>

<header>
	<%-- RIMOSSO LO SCRIPT DA QUI --%>
	<div class="header-container">
		
		<%-- Bottone per aprire la Sidebar (nuovo) --%>
		<button id="sidebar-toggle-btn" class="sidebar-toggle">
			<span></span>
			<span></span>
			<span></span>
		</button>
		
		<div class="logo">
			<a href="home">
				<img src="images/logo.png" alt="sVinicola Logo" class="header-logo-img">
			</a>
		</div>
		
		<nav class="main-nav">
			<ul>
				<%-- Link per lo scroll --%>
				<li><a href="#catalogo-start">Catalogo</a></li>
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
					<a href="carrello.jsp" class="header-icon-link">
						<img src="images/carrello.png" alt="Carrello" class="header-icon-img">
					</a>
				</li>
				
				<%
					if (utente == null) {
				%>
				
					<li><a href="login.jsp">Login</a></li>
					<li><a href="registrazione.jsp">Registrati</a></li>
				
				<%
					} else {
				%>
				
					<li class="welcome-msg">Ben Tornato, <%= utente.getNomeUtente() %></li>
					
					<%
						if (utente.getRuolo().equals("admin")) {
					%>
						<li><a href="admin/dashboard.jsp">Pannello Admin</a></li>
					<%
						} else {
					%>
						<li><a href="area-personale.jsp">I Miei Ordini</a></li>
					<%
						}
					%>
					
					<li><a href="logout">Logout</a></li>
					
				<%
					}
				%>
			</ul>
		</div>
		
	</div>
</header>

<%-- Questo HTML per la sidebar è corretto che stia qui --%>
<div class="sidebar">
    <h3>Filtri Catalogo</h3>

    <div class="filter-group">
        <h4>Tipo di Vino</h4>
        <a href="catalogo?tipo=Rosso">Vini Rossi</a><br>
        <a href="catalogo?tipo=Bianco">Vini Bianchi</a><br>
        <a href="catalogo?tipo=Rosé">Vini Rosé</a><br>
        <a href="catalogo?tipo=Spumante">Spumanti</a><br>
    </div>

    <%-- Altri filtri --%>

</div>