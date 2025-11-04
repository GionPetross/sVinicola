document.addEventListener("DOMContentLoaded", function() {
    
    const sidebar = document.querySelector(".sidebar");
    const catalogoContainer = document.getElementById("catalogo-container");
    
    // Filtri
    const searchInput = document.querySelector(".nav-search-bar input[name='search']");
    const filtriSidebar = document.querySelectorAll(".sidebar .filtro-campo");


    function aggiornaCatalogo(chiudiSidebar = false) { // Alla fine non so se chiudere la side bar sia una buona scelta
        let params = new URLSearchParams();

        filtriSidebar.forEach(filtro => {
            if (filtro.type === 'checkbox') {
                if (filtro.checked) {
                    params.append(filtro.name, filtro.value);
                }
            } else if (filtro.value) {
                params.append(filtro.name, filtro.value);
            }
        });
        if (searchInput.value) {
            params.append("search", searchInput.value);
        }
        
        params.append("ajax", "true");
		
        fetch("home?" + params.toString())
            .then(response => response.text())
            .then(html => {
                catalogoContainer.innerHTML = html;
                
                if (chiudiSidebar && sidebar) {
                    sidebar.classList.remove("open");
                }
            })
            .catch(error => {
                console.error("Errore durante il filtraggio AJAX:", error);
                catalogoContainer.innerHTML = "<p class='error'>Errore nel caricamento dei filtri.</p>";
            });
    }

    searchInput.addEventListener("keyup", function() {
        if (searchInput.value.length >= 3 || searchInput.value.length === 0) {
            aggiornaCatalogo(false); 
        }
    });


    filtriSidebar.forEach(filtro => {
        
        if (filtro.tagName === 'SELECT' || filtro.type === 'checkbox') {
            filtro.addEventListener("change", () => aggiornaCatalogo(false));
        } 
        
        else if (filtro.tagName === 'INPUT') {
            filtro.addEventListener("change", () => aggiornaCatalogo(false));
        }
    });
	
	if (bannerContainer) {
			bannerContainer.addEventListener("click", function(event) {
				
				const bannerLink = event.target.closest(".banner-link");
				if (!bannerLink) return; // Click non su un link
				
				event.preventDefault(); // Impedisce la navigazione
				
				const offertaId = bannerLink.dataset.offertaId;
				if (!offertaId) return;
				
				// Pulisce tutti i filtri della sidebar
				filtriSidebar.forEach(filtro => {
					if (filtro.type === 'checkbox') filtro.checked = false;
					else filtro.value = '';
				});
				searchInput.value = '';
				
				// Crea i parametri SOLO per l'offerta
				let params = new URLSearchParams();
				params.append("offerta_id", offertaId);
				
				// Chiama l'aggiornamento
				aggiornaCatalogo(params, false);
			});
		}
});
