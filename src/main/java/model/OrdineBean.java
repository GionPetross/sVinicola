package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idOrdine;
	private int idUtente; // FK
	private int idIndirizzoSpedizione; // FK
	private Timestamp data;
	private BigDecimal totaleComplessivo;

	// Costruttore vuoto
	public OrdineBean() {
	}

	// Getters e Setters
	public int getIdOrdine() { return idOrdine; }
	public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

	public int getIdUtente() { return idUtente; }
	public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

	public int getIdIndirizzoSpedizione() { return idIndirizzoSpedizione; }
	public void setIdIndirizzoSpedizione(int idIndirizzoSpedizione) { this.idIndirizzoSpedizione = idIndirizzoSpedizione; }

	public Timestamp getData() { return data; }
	public void setData(Timestamp data) { this.data = data; }

	public BigDecimal getTotaleComplessivo() { return totaleComplessivo; }
	public void setTotaleComplessivo(BigDecimal totaleComplessivo) { this.totaleComplessivo = totaleComplessivo; }

	@Override
	public String toString() {
		return "OrdineBean [idOrdine=" + idOrdine + ", idUtente=" + idUtente + ", data=" + data + ", totale=" + totaleComplessivo + "]";
	}
}