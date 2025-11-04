let formContainer;
let formTitolo;

document.addEventListener("DOMContentLoaded", function() {
    formContainer = document.getElementById("form-container-modifica");
    formTitolo = document.getElementById("form-titolo");
    
    const annullaBtn = document.querySelector("#form-container-modifica .btn-secondary");
    if (annullaBtn) {
        annullaBtn.addEventListener("click", nascondiFormModifica);
    }
});

//Fai comparire il form
function popolaFormModifica(idVino) {
    const riga = document.getElementById("riga-vino-" + idVino);
    
    formTitolo.innerText = "Modifica Prodotto (ID: " + idVino + ")";
    document.getElementById("form-idVino").value = riga.dataset.id;
    document.getElementById("form-nome").value = riga.dataset.nome;
    document.getElementById("form-annata").value = riga.dataset.annata;
    document.getElementById("form-prezzo").value = riga.dataset.prezzo;
    document.getElementById("form-stock").value = riga.dataset.stock;
    document.getElementById("form-tipo").value = riga.dataset.tipo;
    document.getElementById("form-formato").value = riga.dataset.formato;
    document.getElementById("form-origine").value = riga.dataset.origine;
    document.getElementById("form-alcol").value = riga.dataset.alcol;
    document.getElementById("form-immagine").value = riga.dataset.immagine;
    document.getElementById("form-descrizione").value = riga.dataset.descrizione;
    document.getElementById("form-in_vendita").checked = (riga.dataset.in_vendita === 'true');
    

    if (formContainer) {
        formContainer.style.display = "block";
        formContainer.scrollIntoView({ behavior: 'smooth' });
    }
}

//Nascondi il form
function nascondiFormModifica() {
    if (formContainer) {
        formContainer.style.display = "none";
        
        // Resetta il form
        document.getElementById("form-vino").reset();
        formTitolo.innerText = "Modifica Prodotto";
        document.getElementById("form-idVino").value = "";
    }
}