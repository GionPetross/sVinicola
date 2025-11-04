package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idOrdine;
	private int idUtente; // FK
	private String viaSpedizione;
	private String capSpedizione;
	private String cittaSpedizione;
	private String provinciaSpedizione;
	private String stato;
	private Timestamp data;
	private BigDecimal totaleComplessivo;

	// Costruttore vuoto
	public OrdineBean() {
	}

	// Getters e Setters
	public int getIdOrdine() { return idOrdine; }
	public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }
	
	public String getStato() { return stato;}
	public void setStato(String stato) { this.stato= stato; }

	public int getIdUtente() { return idUtente; }
	public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

	public String getViaSpedizione() { return viaSpedizione; }
	public void setViaSpedizione(String viaSpedizione) { this.viaSpedizione = viaSpedizione; }

	public String getCapSpedizione() { return capSpedizione; }
	public void setCapSpedizione(String capSpedizione) { this.capSpedizione = capSpedizione; }

	public String getCittaSpedizione() { return cittaSpedizione; }
	public void setCittaSpedizione(String cittaSpedizione) { this.cittaSpedizione = cittaSpedizione; }

	public String getProvinciaSpedizione() { return provinciaSpedizione; }
	public void setProvinciaSpedizione(String provinciaSpedizione) { this.provinciaSpedizione = provinciaSpedizione; }

	public Timestamp getData() { return data; }
	public void setData(Timestamp data) { this.data = data; }

	public BigDecimal getTotaleComplessivo() { return totaleComplessivo; }
	public void setTotaleComplessivo(BigDecimal totaleComplessivo) { this.totaleComplessivo = totaleComplessivo; }

	@Override
	public String toString() {
		return "OrdineBean [idOrdine=" + idOrdine + ", idUtente=" + idUtente + ", data=" + data + ", totale=" + totaleComplessivo + "]";
	}
}