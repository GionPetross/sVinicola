document.addEventListener("DOMContentLoaded", function() {

	// --- Gestione Cambio Tema ---
	const themeToggleBtn = document.getElementById("theme-toggle-btn");
	
	themeToggleBtn.addEventListener("click", function() {
		// Controlla il tema attuale del body
		let currentTheme = document.body.getAttribute("data-theme");
		
		if (currentTheme === "dark") {
			// Passa a light
			document.body.setAttribute("data-theme", "light");
		} else {
			// Passa a dark
			document.body.setAttribute("data-theme", "dark");
		}
	});
	

	// --- Gestione Apertura/Chiusura Sidebar ---
	const sidebarToggleBtn = document.getElementById("sidebar-toggle-btn");
	const sidebar = document.querySelector(".sidebar"); // Seleziona la sidebar
	
	
	if (sidebarToggleBtn && sidebar) {
		sidebarToggleBtn.addEventListener("click", function() {
			sidebar.classList.toggle("open");
		});
	}

	// --- Gestione Banner a Scomparsa ---

});