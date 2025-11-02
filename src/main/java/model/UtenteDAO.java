package model;

import java.sql.SQLException;
import java.util.List;

public interface UtenteDAO {

    //Metodo per il Login
    public UtenteBean doRetrieveByUsernamePassword(String username, String password) throws SQLException;

    // Recupera un utente usando solo il Nome_Utente (per controllo duplicati)
    public UtenteBean doRetrieveByUsername(String username) throws SQLException;
    
    // Recupera un utente dal suo ID
    public UtenteBean doRetrieveByKey(int idUtente) throws SQLException;

    // Salva un nuovo utente (Registrazione)
    public void doSave(UtenteBean utente) throws SQLException;
    
    // Aggiorna i dati di un utente
    public void doUpdate(UtenteBean utente) throws SQLException;


    /* * Metodi per la gestione dei Preferiti (Tabella Lista_Preferiti)
     */

    // Aggiunge un vino ai preferiti dell'utente
    public void doAddPreferito(int idUtente, int idVino) throws SQLException;

    // Rimuove un vino dai preferiti dell'utente
    public boolean doRemovePreferito(int idUtente, int idVino) throws SQLException;

    // Controlla se un vino è già nei preferiti
    public boolean isPreferito(int idUtente, int idVino) throws SQLException;

    // Recupera tutti i vini preferiti di un utente
    public List<VinoBean> doRetrievePreferiti(int idUtente) throws SQLException;
}