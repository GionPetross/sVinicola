package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.IndirizzoBean;
import model.IndirizzoDAO;
import model.UtenteBean;

@WebServlet("/gestione-indirizzo")
public class GestioneIndirizzoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Gestiamo solo l'azione 'delete' in GET
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        
        if (action != null && action.equals("delete") && session != null && session.getAttribute("utente") != null) {
            try {
                int idIndirizzo = Integer.parseInt(request.getParameter("id"));
                // (In un'app reale, controlleremmo che l'ID utente in sessione
                // sia il proprietario di questo indirizzo prima di cancellarlo)
                indirizzoDAO.doDelete(idIndirizzo);
            } catch (SQLException | NumberFormatException e) {
                System.err.println("Errore cancellazione indirizzo: " + e.getMessage());
            }
        }
        
        // In ogni caso, rimandiamo all'area personale per vedere l'aggiornamento
        response.sendRedirect("area-personale");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Gestiamo solo l'azione 'add' in POST
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        
        if (action != null && action.equals("add") && session != null && session.getAttribute("utente") != null) {
            
            UtenteBean utente = (UtenteBean) session.getAttribute("utente");
            
            // 1. Recuperiamo i dati dal form
            String via = request.getParameter("via");
            String cap = request.getParameter("cap");
            String citta = request.getParameter("citta");
            String provincia = request.getParameter("provincia");
            
            // 2. Creiamo il Bean
            IndirizzoBean nuovoIndirizzo = new IndirizzoBean();
            nuovoIndirizzo.setVia(via);
            nuovoIndirizzo.setCap(cap);
            nuovoIndirizzo.setCitta(citta);
            nuovoIndirizzo.setProvincia(provincia);
            nuovoIndirizzo.setIdUtente(utente.getIdUtente()); // Colleghiamo l'utente
            
            try {
                // 3. Salviamo nel DB
                indirizzoDAO.doSave(nuovoIndirizzo);
            } catch (SQLException e) {
                System.err.println("Errore salvataggio indirizzo: " + e.getMessage());
            }
        }
        
        // In ogni caso, rimandiamo all'area personale per vedere l'aggiornamento
        response.sendRedirect("area-personale");
    }
}