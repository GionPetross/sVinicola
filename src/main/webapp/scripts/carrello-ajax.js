document.addEventListener("DOMContentLoaded", function() {

    const catalogoContainer = document.getElementById("catalogo-container");
    const carrelloContainer = document.querySelector(".carrello-container"); 

    // --- 1. FUNZIONE GLOBALE PER AGGIORNARE IL BADGE ---
    // Questa funzione realizza il tuo Punto (1) (Badge Live)
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

    // --- 2. AZIONE: AGGIUNGI AL CARRELLO (Dal Catalogo) ---
    if (catalogoContainer) {
        catalogoContainer.addEventListener("click", function(event) {
            const bottoneAggiungi = event.target.closest(".btn-aggiungi");
            if (bottoneAggiungi) {
                event.preventDefault();
                const url = bottoneAggiungi.href;
                
                fetch(url)
                    .then(response => response.text()) // 1. Ricevi come TESTO
                    .then(text => {
                        const data = JSON.parse(text); // 2. Esegui il PARSE come richiesto
                        
                        if (data.cartCount) {
                            aggiornaBadge(data.cartCount); // 3. Aggiorna il badge LIVE
                            mostraNotificaAggiunto(bottoneAggiungi);
                        }
                    })
                    .catch(error => console.error("Errore Fetch (Add):", error));
            }
        });
    }

    // --- 3. AZIONI LIVE NELLA PAGINA CARRELLO ---
    if (carrelloContainer) {
        
        carrelloContainer.addEventListener("click", function(event) {
            
            // AZIONE: Rimuovi Prodotto
            if (event.target.classList.contains("ajax-remove")) {
                const bottoneRimuovi = event.target;
                const idVino = bottoneRimuovi.dataset.id;
                
                fetch(`carrello?action=remove&id=${idVino}`)
                    .then(response => response.text()) // 1. Ricevi come TESTO
                    .then(text => {
                        const data = JSON.parse(text); // 2. Esegui il PARSE
                        
                        document.getElementById(`riga-prodotto-${idVino}`).remove();
                        document.getElementById("carrello-totale").textContent = data.newTotal;
                        aggiornaBadge(data.cartCount); // 3. Aggiorna il badge LIVE
                    })
                    .catch(error => console.error("Errore Fetch (Remove):", error));
            }
        });

        carrelloContainer.addEventListener("change", function(event) {
            
            // AZIONE: Aggiorna Quantità
            if (event.target.classList.contains("ajax-update")) {
                const inputQuantita = event.target;
                const idVino = inputQuantita.dataset.id;
                const quantita = inputQuantita.value;

                fetch(`carrello?action=update&id=${idVino}&quantita=${quantita}`)
                    .then(response => response.text()) // 1. Ricevi come TESTO
                    .then(text => {
                        const data = JSON.parse(text); // 2. Esegui il PARSE
                        
                        if (data.newSubtotal !== undefined) {
                            document.getElementById(`subtotale-${idVino}`).textContent = data.newSubtotal + " €";
                        }
                        document.getElementById("carrello-totale").textContent = data.newTotal;
                        aggiornaBadge(data.cartCount); // 3. Aggiorna il badge LIVE
                        
                        if (quantita <= 0) {
                            document.getElementById(`riga-prodotto-${idVino}`).remove();
                        }
                    })
                    .catch(error => console.error("Errore Fetch (Update):", error));
            }
        });
    }
    
    // Funzione di feedback (invariata)
    function mostraNotificaAggiunto(bottone) {
        const icona = bottone.querySelector("img");
        if (icona) {
            icona.style.transform = "scale(1.2)";
            // Puoi aggiungere altri stili di feedback qui
        }
        setTimeout(() => {
            if (icona) {
                icona.style.transform = "scale(1)";
            }
        }, 1000);
    }
});