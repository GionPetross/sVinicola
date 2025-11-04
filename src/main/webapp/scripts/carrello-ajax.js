document.addEventListener("DOMContentLoaded", function() {

	const catalogoContainer = document.getElementById("catalogo-container"); // Per home.jsp E dettaglio_prodotto.jsp
	const carrelloContainer = document.querySelector(".carrello-container"); // Per carrello.jsp

	// --- FUNZIONE GLOBALE PER AGGIORNARE IL BADGE ---
	function aggiornaBadge(cartCount) {
		let badge = document.querySelector(".cart-badge");
		if (cartCount > 0) {
			if (!badge) {
				badge = document.createElement("span");
				badge.className = "cart-badge";
				document.querySelector(".cart-link-container").appendChild(badge);
			}
			badge.textContent = cartCount;
		} else {
			if (badge) {
				badge.remove();
			}
		}
	}

	// --- AZIONE: AGGIUNGI AL CARRELL ---
	if (catalogoContainer) {
		catalogoContainer.addEventListener("click", function(event) {
			const bottoneAggiungi = event.target.closest(".btn-aggiungi");
			
			if (bottoneAggiungi) {
				event.preventDefault();
				const url = bottoneAggiungi.href;
				
				fetch(url)
					.then(response => response.text()) 
					.then(text => {
						const data = JSON.parse(text);
						
						if (data.cartCount) {
							aggiornaBadge(data.cartCount); // Aggiorna il badge LIVE
							mostraNotificaAggiunto(bottoneAggiungi);
						}
					})
					.catch(error => console.error("Errore Fetch (Add):", error));
			}
		});
	}

	// --- AZIONI LIVE NELLA PAGINA CARRELLO ---
	if (carrelloContainer) {
		
		carrelloContainer.addEventListener("click", function(event) {
			//Rimuovi Prodotto
			if (event.target.classList.contains("ajax-remove")) {
				const bottoneRimuovi = event.target;
				const idVino = bottoneRimuovi.dataset.id;
				
				fetch(`carrello?action=remove&id=${idVino}`)
					.then(response => response.text())
					.then(text => {
						const data = JSON.parse(text); 
						document.getElementById(`riga-prodotto-${idVino}`).remove();
						document.getElementById("carrello-totale").textContent = data.newTotal;
						aggiornaBadge(data.cartCount);
					})
					.catch(error => console.error("Errore Fetch (Remove):", error));
			}
		});

		carrelloContainer.addEventListener("change", function(event) {
			//Aggiorna Quantità
			if (event.target.classList.contains("ajax-update")) {
				const inputQuantita = event.target;
				const idVino = inputQuantita.dataset.id;
				const quantita = inputQuantita.value;

				fetch(`carrello?action=update&id=${idVino}&quantita=${quantita}`)
					.then(response => response.text())
					.then(text => {
						const data = JSON.parse(text);
						
						if (data.newSubtotal !== undefined) {
							document.getElementById(`subtotale-${idVino}`).textContent = data.newSubtotal + " €";
						}
						document.getElementById("carrello-totale").textContent = data.newTotal;
						aggiornaBadge(data.cartCount);
						
						if (quantita <= 0) {
							document.getElementById(`riga-prodotto-${idVino}`).remove();
						}
					})
					.catch(error => console.error("Errore Fetch (Update):", error));
			}
		});
	}
	
	// Funzione di feedback
	function mostraNotificaAggiunto(bottone) {
		bottone.classList.add("notifica-aggiunto");
		setTimeout(() => {
			bottone.classList.remove("notifica-aggiunto");
		}, 600);
	}
});