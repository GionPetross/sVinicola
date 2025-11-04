package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UtenteBean;
import model.UtenteDAO;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtenteDAO utenteDAO = new UtenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/registrazione.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("conferma-password");
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/registrazione.jsp");
        
        if (!password.equals(confermaPassword)) {
            request.setAttribute("errore", "Le password non corrispondono.");
            dispatcher.forward(request, response);
            return;
        }

        try {
            if (utenteDAO.doRetrieveByUsername(username) != null) {
                request.setAttribute("errore", "Username già in uso. Scegline un altro.");
                dispatcher.forward(request, response);
                return;
            }
            
            UtenteBean nuovoUtente = new UtenteBean();
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome(cognome);
            nuovoUtente.setNomeUtente(username);
            nuovoUtente.setEmail(email);
            nuovoUtente.setPassword(password);
            nuovoUtente.setRuolo("cliente"); // Ruolo di default
            
            utenteDAO.doSave(nuovoUtente);

        } catch (SQLException e) {
            System.err.println("Errore SQL durante la registrazione: " + e.getMessage());
            request.setAttribute("errore", "Errore del database. Riprova più tardi.");
            dispatcher.forward(request, response);
            return;
        }
        
        response.sendRedirect("login.jsp?registrazione=success");
    }
}