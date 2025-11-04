let slideIndex = 1;

function plusSlides(n) {
  showSlides(slideIndex += n);
}

function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  let i;
  let slides = document.getElementsByClassName("mySlides");
  
  // RIMOZIONE: Riferimento ai "dots" rimosso
  
  if (slides.length === 0) {
	  return; 
  }
  
  if (n > slides.length) {slideIndex = 1}
  if (n < 1) {slideIndex = slides.length}
  
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  
  // RIMOZIONE: Loop sui "dots" rimosso
  
  slides[slideIndex-1].style.display = "block";
}
// --- FINE LOGICA SLIDER ---


document.addEventListener("DOMContentLoaded", function() {

	// --- Gestione Cambio Tema ---
	const themeToggleBtn = document.getElementById("theme-toggle-btn");
	const htmlTag = document.documentElement; 

	const savedTheme = localStorage.getItem("theme");
	if (savedTheme) {
		htmlTag.setAttribute("data-theme", savedTheme);
	}

	themeToggleBtn.addEventListener("click", function() {
		let currentTheme = htmlTag.getAttribute("data-theme");
		let newTheme = (currentTheme === "dark") ? "light" : "dark";
		htmlTag.setAttribute("data-theme", newTheme);
		localStorage.setItem("theme", newTheme);
	});
	

	// --- Gestione Apertura/Chiusura Sidebar ---
	const sidebarToggleBtn = document.getElementById("sidebar-toggle-btn");
	const sidebarCloseBtn = document.getElementById("sidebar-close-btn");
	const sidebar = document.querySelector(".sidebar");
		
	if (sidebarToggleBtn && sidebar) {
		sidebarToggleBtn.addEventListener("click", function() {
			sidebar.classList.add("open");
		});
	}
		
	if (sidebarCloseBtn && sidebar) {
		sidebarCloseBtn.addEventListener("click", function() {
			sidebar.classList.remove("open");
		});
	}

	
	// --- Creazione Bollicine ---
	function createBubbles() {
		const canvas = document.getElementById("bubble-canvas");
		if (!canvas) return; 
		
		const bubbleCount = 100;
	
		for (let i = 0; i < bubbleCount; i++) {
			const bubble = document.createElement("div");
			bubble.classList.add("bubble");
			const size = Math.random() * (60 - 10) + 10;
			bubble.style.width = `${size}px`;
			bubble.style.height = `${size}px`;
			bubble.style.left = `${Math.random() * 100}vw`;
			const duration = Math.random() * (20 - 8) + 8;
			bubble.style.animationDuration = `${duration}s`;
			const delay = Math.random() * 5; 
			bubble.style.animationDelay = `${delay}s`;
			const horizontalDrift = (Math.random() - 0.5) * 2 * 150;
			bubble.style.setProperty('--horizontal-drift', `${horizontalDrift}px`);
			canvas.appendChild(bubble);
		}
	}
	createBubbles();

	// --- AVVIO SLIDER ---
	showSlides(slideIndex);
	
});