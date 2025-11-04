package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Rappresenta un ordine momentaneo
public class Carrello implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //Hashmap in modo per rendere aggiungiProdotto più veloce (e fa figo)
    private Map<Integer, VoceCarrello> voci;

    public Carrello() {
        this.voci = new HashMap<>();
    }
    
    //Crea voci o modifica voci
    public synchronized void aggiungiProdotto(VinoBean vino, int quantitaDaAggiungere) {
        int idVino = vino.getIdVino();

        if (voci.containsKey(idVino)) {
            VoceCarrello voceEsistente = voci.get(idVino);
            voceEsistente.setQuantita(voceEsistente.getQuantita() + quantitaDaAggiungere);
        } else {
            VoceCarrello nuovaVoce = new VoceCarrello(vino, quantitaDaAggiungere);
            voci.put(idVino, nuovaVoce);
        }
    }
    
    //Aggiunge una singola quantità
    public synchronized void aggiungiProdotto(VinoBean vino) {
        this.aggiungiProdotto(vino, 1);
    }

    //Rimuovi prodotto
    public synchronized void rimuoviProdotto(int idVino) {
        voci.remove(idVino);
    }

    //Modifica la quantità
    public synchronized void modificaQuantita(int idVino, int nuovaQuantita) {
        if (voci.containsKey(idVino)) {
            if (nuovaQuantita > 0) {
                VoceCarrello voce = voci.get(idVino);
                voce.setQuantita(nuovaQuantita);
            } else {
                // Se la quantità è 0 o negativa, rimuoviamo il prodotto
                this.rimuoviProdotto(idVino);
            }
        }
    }

    //Svuota carrello
    public synchronized void svuota() {
        voci.clear();
    }

    //Ritorna tutto il carrello
    public synchronized Collection<VoceCarrello> getVoci() {
        return voci.values();
    }

    public synchronized BigDecimal getTotale() {
        BigDecimal totale = BigDecimal.ZERO;
        
        for (VoceCarrello voce : voci.values()) {
            totale = totale.add(voce.getSubtotale());
        }
        return totale;
    }
    

    public synchronized boolean isEmpty() {
        return voci.isEmpty();
    }
    

    public synchronized int getNumVoci() {
        return voci.size();
    }
    
    public synchronized Collection<DettaglioOrdineBean> getDettagliOrdine() {
        List<DettaglioOrdineBean> dettagli = new ArrayList<>();
        for (VoceCarrello voce : voci.values()) {
            DettaglioOrdineBean dett = new DettaglioOrdineBean();
            dett.setIdVino(voce.getVino().getIdVino());
            dett.setQuantita(voce.getQuantita());
            dett.setPrezzoStorico(voce.getVino().getPrezzo());
            dettagli.add(dett);
        }
        return dettagli;
    }

}