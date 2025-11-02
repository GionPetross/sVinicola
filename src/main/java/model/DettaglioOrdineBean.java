package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DettaglioOrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// Chiave primaria composta (e FK)
	private int idOrdine;
	private int idVino;
	
	// Attributi della relazione
	private int quantita;
	private BigDecimal prezzoStorico; // Prezzo al momento dell'acquisto

	// Costruttore vuoto
	public DettaglioOrdineBean() {
	}

	// Getters e Setters
	public int getIdOrdine() { return idOrdine; }
	public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

	public int getIdVino() { return idVino; }
	public void setIdVino(int idVino) { this.idVino = idVino; }

	public int getQuantita() { return quantita; }
	public void setQuantita(int quantita) { this.quantita = quantita; }

	public BigDecimal getPrezzoStorico() { return prezzoStorico; }
	public void setPrezzoStorico(BigDecimal prezzoStorico) { this.prezzoStorico = prezzoStorico; }

	@Override
	public String toString() {
		return "DettaglioOrdineBean [idOrdine=" + idOrdine + ", idVino=" + idVino + ", quantita=" + quantita + "]";
	}
}