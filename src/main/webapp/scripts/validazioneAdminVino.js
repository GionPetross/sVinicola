document.addEventListener("DOMContentLoaded", function() {

    const form = document.getElementById("form-vino");
    if (!form) return;

    // Campi da validare
    const prezzo = document.getElementById("prezzo");
    const stock = document.getElementById("stock");
    const alcol = document.getElementById("alcol");

    // Contenitori di errore
    const errorePrezzo = document.getElementById("errore-prezzo");
    const erroreStock = document.getElementById("errore-stock");
    const erroreAlcol = document.getElementById("errore-alcol");

    function mostraErrore(inputEl, errorEl, messaggio) {
        errorEl.textContent = messaggio; 
        inputEl.classList.add("input-errore");
    }

    function rimuoviErrore(inputEl, errorEl) {
        errorEl.textContent = ""; 
        inputEl.classList.remove("input-errore");
    }

    // --- Funzioni di Validazione Specifiche ---
    function validaPrezzo() {
        const valore = parseFloat(prezzo.value);
        if (isNaN(valore) || valore <= 0) {
            mostraErrore(prezzo, errorePrezzo);
            return false;
        }
        rimuoviErrore(prezzo, errorePrezzo);
        return true;
    }

    function validaStock() {
        const valore = parseInt(stock.value);
        if (isNaN(valore) || valore < 0) { 
            mostraErrore(stock, erroreStock);
            return false;
        }
        rimuoviErrore(stock, erroreStock);
        return true;
    }

    function validaAlcol() {
        if (alcol.value.trim() === "") {
            rimuoviErrore(alcol, erroreAlcol);
            return true;
        }
        
        const valore = parseFloat(alcol.value);
        if (isNaN(valore) || valore < 0 || valore > 100) {
            mostraErrore(alcol, erroreAlcol);
            return false;
        }
        rimuoviErrore(alcol, erroreAlcol);
        return true;
    }

    // --- Listener ---
    prezzo.addEventListener("input", validaPrezzo);
    stock.addEventListener("input", validaStock);
    alcol.addEventListener("input", validaAlcol);

    // --- Validazione Finale ---
    form.addEventListener("submit", function(event) {
        
        const isPrezzoValido = validaPrezzo();
        const isStockValido = validaStock();
        const isAlcolValido = validaAlcol();

        // Blocca Se false
        if (!isPrezzoValido || !isStockValido || !isAlcolValido) {
            event.preventDefault(); 
            console.log("Form Admin non valido. Invio bloccato.");
        }
    });

});