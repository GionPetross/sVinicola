package model;

public class Utente {
	private int id;
	private String username;
	private String password;
	private String indirizzo;
	private String email;
	private boolean admin;



	public Utente(int id, String username, String password, String indirizzo, String email, boolean admin) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.indirizzo = indirizzo;
		this.email = email;
		this.admin = admin;
	}
	
	
	// Getter e Setter
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public String getIndirizzo() { return indirizzo; }
	public void Indirizzo(String indirizzo) { this.indirizzo = indirizzo; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public boolean isAdmin() { return admin; }
	public void setAdmin(boolean admin) { this.admin = admin; }
}