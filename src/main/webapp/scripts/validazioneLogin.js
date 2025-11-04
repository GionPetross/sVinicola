document.addEventListener("DOMContentLoaded", function() {

	const form = document.getElementById("form-login");
	const username = document.getElementById("username");
	const password = document.getElementById("password");

	const erroreUsername = document.getElementById("errore-username");
	const errorePassword = document.getElementById("errore-password");


	function mostraErrore(inputEl, errorEl, messaggio) {
		errorEl.textContent = messaggio; 
		inputEl.classList.add("input-errore");
	}

	function rimuoviErrore(inputEl, errorEl) {
		errorEl.textContent = ""; 
		inputEl.classList.remove("input-errore");
	}

	function validaUsername() {
		if (username.value.trim() === "") {
			mostraErrore(username, erroreUsername, "Il campo Username è obbligatorio.");
			return false;
		}
		rimuoviErrore(username, erroreUsername);
		return true;
	}

	function validaPassword() {
		if (password.value.trim() === "") {
			mostraErrore(password, errorePassword, "Il campo Password è obbligatorio.");
			return false;
		}
		rimuoviErrore(password, errorePassword);
		return true;
	}
	username.addEventListener("blur", validaUsername);
	password.addEventListener("blur", validaPassword);
	form.addEventListener("submit", function(event) {
		
		const isUsernameValido = validaUsername();
		const isPasswordValida = validaPassword();

		if (!isUsernameValido || !isPasswordValida) {
			event.preventDefault(); // Blocca l'invio del form
			console.log("Form Login non valido. Invio bloccato.");
		}
	});

});