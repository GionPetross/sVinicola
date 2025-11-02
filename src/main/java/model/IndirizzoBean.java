package model;

import java.io.Serializable;

public class IndirizzoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idIndirizzo;
	private String via;
	private String cap;
	private String citta;
	private String provincia;
	private int idUtente; // FK

	// Costruttore vuoto
	public IndirizzoBean() {
	}

	// Getters e Setters
	public int getIdIndirizzo() { return idIndirizzo; }
	public void setIdIndirizzo(int idIndirizzo) { this.idIndirizzo = idIndirizzo; }

	public String getVia() { return via; }
	public void setVia(String via) { this.via = via; }

	public String getCap() { return cap; }
	public void setCap(String cap) { this.cap = cap; }

	public String getCitta() { return citta; }
	public void setCitta(String citta) { this.citta = citta; }

	public String getProvincia() { return provincia; }
	public void setProvincia(String provincia) { this.provincia = provincia; }

	public int getIdUtente() { return idUtente; }
	public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

	@Override
	public String toString() {
		return "IndirizzoBean [idIndirizzo=" + idIndirizzo + ", via=" + via + ", citta=" + citta + ", idUtente=" + idUtente + "]";
	}
}