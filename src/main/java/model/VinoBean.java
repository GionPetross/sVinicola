package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class VinoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idVino;
	private String nome;
	private int annata;
	private String tipo; // Corrisponde all'ENUM ('Rosso', 'Bianco', ...)
	private String descrizione;
	private double percentualeAlcolica; // Mappato da DECIMAL(4, 2)
	private String immagine;
	private BigDecimal prezzo; // Usiamo BigDecimal per la valuta
	private int stock;
	private String formato;
	private String origine;
	private boolean inVendita;
	private Timestamp dataAggiunta;

	// Costruttore vuoto
	public VinoBean() {
	}

	// Getters e Setters
	public int getIdVino() { return idVino; }
	public void setIdVino(int idVino) { this.idVino = idVino; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public int getAnnata() { return annata; }
	public void setAnnata(int annata) { this.annata = annata; }

	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }

	public String getDescrizione() { return descrizione; }
	public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

	public double getPercentualeAlcolica() { return percentualeAlcolica; }
	public void setPercentualeAlcolica(double percentualeAlcolica) { this.percentualeAlcolica = percentualeAlcolica; }

	public String getImmagine() { return immagine; }
	public void setImmagine(String immagine) { this.immagine = immagine; }

	public BigDecimal getPrezzo() { return prezzo; }
	public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

	public int getStock() { return stock; }
	public void setStock(int stock) { this.stock = stock; }

	public String getFormato() { return formato; }
	public void setFormato(String formato) { this.formato = formato; }

	public String getOrigine() { return origine; }
	public void setOrigine(String origine) { this.origine = origine; }

	public boolean isInVendita() { return inVendita; }
	public void setInVendita(boolean inVendita) { this.inVendita = inVendita; }

	public Timestamp getDataAggiunta() { return dataAggiunta; }
	public void setDataAggiunta(Timestamp dataAggiunta) { this.dataAggiunta = dataAggiunta;}
	
	@Override
	public String toString() {
		return "VinoBean [idVino=" + idVino + ", nome=" + nome + ", tipo=" + tipo + ", prezzo=" + prezzo + "]";
	}
}