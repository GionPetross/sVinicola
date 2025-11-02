package model;

import java.io.Serializable;

public class UtenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// Variabili che mappano le colonne della tabella Utente
	private int idUtente;
	private String nomeUtente;
	private String password; // Nota: il DAO gestirà l'hashing, il bean porta solo il dato
	private String ruolo;
	private String email;
	private String nome;
	private String cognome;
	
	// Costruttore vuoto
	public UtenteBean() {}

	// Getters e Setters
	public int getIdUtente() {return idUtente;}
	public void setIdUtente(int idUtente) {this.idUtente = idUtente;}
	
	public String getNomeUtente() {return nomeUtente;}
	public void setNomeUtente(String nomeUtente) {this.nomeUtente = nomeUtente;}

	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	public String getRuolo() {return ruolo;}
	public void setRuolo(String ruolo) {this.ruolo = ruolo;}

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public String getCognome() {return cognome;}
	public void setCognome(String cognome) {this.cognome = cognome;}
	
	@Override
	public String toString() {
		return "UtenteBean [idUtente=" + idUtente + ", nomeUtente=" + nomeUtente + ", ruolo=" + ruolo + ", email=" + email + "]";
	}
}