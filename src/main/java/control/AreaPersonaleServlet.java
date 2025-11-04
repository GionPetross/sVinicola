package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.IndirizzoBean;
import model.IndirizzoDAO;
import model.UtenteBean;

@WebServlet("/area-personale")
public class AreaPersonaleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Non crearla se non esiste
        // Controlla se l'utente è loggato
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect("login.jsp"); // Non loggato, rimanda al login
            return;
        }
        
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        
        try {
            // Recuperiamo tutti gli indirizzi di questo utente
            List<IndirizzoBean> indirizzi = indirizzoDAO.doRetrieveByUtente(utente.getIdUtente());
            request.setAttribute("indirizzi", indirizzi);

        } catch (SQLException e) {
            System.err.println("Errore SQL recupero indirizzi: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database.");
            return;
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/area_personale.jsp");
        dispatcher.forward(request, response);
    }
}