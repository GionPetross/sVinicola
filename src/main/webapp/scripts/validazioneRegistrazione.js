document.addEventListener("DOMContentLoaded", function() {


	const form = document.getElementById("form-registrazione");
	
	const nome = document.getElementById("nome");
	const cognome = document.getElementById("cognome");
	const username = document.getElementById("username");
	const email = document.getElementById("email");
	const password = document.getElementById("password");
	const confermaPassword = document.getElementById("conferma-password");

	//contenitori per i messaggi di errore
	const erroreNome = document.getElementById("errore-nome");
	const erroreCognome = document.getElementById("errore-cognome");
	const erroreUsername = document.getElementById("errore-username");
	const erroreEmail = document.getElementById("errore-email");
	const errorePassword = document.getElementById("errore-password");
	const erroreConferma = document.getElementById("errore-conferma-password");

	// --- Regex per la validazione ---

	const regexNomeCognome = /^[A-Za-z\s']{2,}$/; 
	const regexUsername = /^[A-Za-z0-9_.-]{4,20}$/; 
	// Regex standard per email
	const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 
	// Min 8 caratteri, almeno una lettera e un numero
	const regexPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; 

	// --- Funzioni Helper per mostrare/nascondere errori ---


	function mostraErrore(inputEl, errorEl, messaggio) {
		errorEl.textContent = messaggio; // Scrive il messaggio
		inputEl.classList.add("input-errore"); // Aggiunge bordo rosso (da definire nel CSS)
	}

	function rimuoviErrore(inputEl, errorEl) {
		errorEl.textContent = ""; // Svuota il messaggio
		inputEl.classList.remove("input-errore"); // Rimuove bordo rosso
	}

	// --- Funzioni di Validazione Specifiche ---

	function validaNome() {
		if (!regexNomeCognome.test(nome.value)) {
			mostraErrore(nome, erroreNome, "Nome non valido. (Min 2 lettere, non accettiamo simboli e numeri)");
			return false;
		}
		rimuoviErrore(nome, erroreNome);
		return true;
	}

	function validaCognome() {
		if (!regexNomeCognome.test(cognome.value)) {
			mostraErrore(cognome, erroreCognome, "Cognome non valido. (Min 2 lettere, non accettiamo simboli e numeri)");
			return false;
		}
		rimuoviErrore(cognome, erroreCognome);
		return true;
	}

	function validaUsername() {
		if (!regexUsername.test(username.value)) {
			mostraErrore(username, erroreUsername, "Username non valido. (Min 4 caratteri, solo lettere, numeri, _, .)");
			return false;
		}
		rimuoviErrore(username, erroreUsername);
		return true;
	}

	function validaEmail() {
		if (!regexEmail.test(email.value)) {
			mostraErrore(email, erroreEmail, "Email non valida. (Es. nome@dominio.it)");
			return false;
		}
		rimuoviErrore(email, erroreEmail);
		return true;
	}

	function validaPassword() {
		if (!regexPassword.test(password.value)) {
			mostraErrore(password, errorePassword, "Password debole. (Min 8 caratteri, almeno una lettera e un numero)");
			return false;
		}
		rimuoviErrore(password, errorePassword);
		return true;
	}

	function validaConfermaPassword() {
		validaPassword(); 
		
		if (password.value !== confermaPassword.value) {
			mostraErrore(confermaPassword, erroreConferma, "Le password non corrispondono.");
			return false;
		}
		// Non rimuoviamo l'errore se la password principale è ancora debole
		if (regexPassword.test(password.value)) {
			rimuoviErrore(confermaPassword, erroreConferma);
		}
		return true;
	}

	// --- VALIDAZIONE BLUR

	nome.addEventListener("blur", validaNome);
	cognome.addEventListener("blur", validaCognome);
	username.addEventListener("blur", validaUsername);
	email.addEventListener("blur", validaEmail);
	
	// 	--- VALIDAZIONE SUBMIT
	password.addEventListener("input", validaConfermaPassword);
	confermaPassword.addEventListener("input", validaConfermaPassword);

	//VALIDAZIONE FINALE

	form.addEventListener("submit", function(event) {

		const isNomeValido = validaNome();
		const isCognomeValido = validaCognome();
		const isUsernameValido = validaUsername();
		const isEmailValida = validaEmail();
		const isPasswordValida = validaPassword();
		const isConfermaValida = validaConfermaPassword();

		if (!isNomeValido || !isCognomeValido || !isUsernameValido || !isEmailValida || !isPasswordValida || !isConfermaValida) {
			event.preventDefault(); 
			
			console.log("Form non valido. Invio bloccato.");
		}
	});

});