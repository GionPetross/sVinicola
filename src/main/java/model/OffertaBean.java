package model;

import java.io.Serializable;
import java.sql.Date; // Per mappare il tipo DATE di SQL

public class OffertaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idOfferta;
    private Date dataInizio;
    private Date dataFine;
    private int percentuale;
    private String immaginePromozionale;

    // Costruttore vuoto
    public OffertaBean() {
    }

    // Getters e Setters
    public int getIdOfferta() { return idOfferta; }
    public void setIdOfferta(int idOfferta) { this.idOfferta = idOfferta; }

    public Date getDataInizio() { return dataInizio; }
    public void setDataInizio(Date dataInizio) { this.dataInizio = dataInizio; }

    public Date getDataFine() { return dataFine; }
    public void setDataFine(Date dataFine) { this.dataFine = dataFine; }

    public int getPercentuale() { return percentuale; }
    public void setPercentuale(int percentuale) { this.percentuale = percentuale; }

    public String getImmaginePromozionale() { return immaginePromozionale; }
    public void setImmaginePromozionale(String immaginePromozionale) { this.immaginePromozionale = immaginePromozionale; }
    
    @Override
    public String toString() {
        return "OffertaBean [idOfferta=" + idOfferta + ", percentuale=" + percentuale + "%, dal " + dataInizio + " al " + dataFine + "]";
    }
}