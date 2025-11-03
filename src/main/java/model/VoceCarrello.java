package model;

import java.io.Serializable;
import java.math.BigDecimal;

//Dettaglio Ordine momentaneo. 
//Diverso dal BEAN perché viene usato momentaneamente
public class VoceCarrello implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private VinoBean vino;
    private int quantita;

    public VoceCarrello(VinoBean vino, int quantita) {
        this.vino = vino;
        this.quantita = quantita;
    }

    public VinoBean getVino() {
        return vino;
    }

    public void setVino(VinoBean vino) {
        this.vino = vino;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
    
    //Calcola il subtotal
    public BigDecimal getSubtotale() {
        BigDecimal prezzo = vino.getPrezzo();
        BigDecimal quantitaBD = new BigDecimal(this.quantita);
        return prezzo.multiply(quantitaBD);
    }
    
    @Override
    public String toString() {
        return "VoceCarrello [vino=" + vino.getNome() + ", quantita=" + quantita + "]";
    }
}