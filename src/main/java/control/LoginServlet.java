package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Importiamo la Sessione

import model.UtenteBean;
import model.UtenteDAO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UtenteDAO utenteDAO = new UtenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            UtenteBean utente = utenteDAO.doRetrieveByUsernamePassword(username, password);

            if (utente == null) {
                //Login Fallito
                request.setAttribute("errore", "Username o password non validi.");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                dispatcher.forward(request, response);
                return;
            }

            //Login Riuscito
            HttpSession session = request.getSession();
            
            // --- L'UTENTE IN SESSIONE ---
            session.setAttribute("utente", utente); 
            
            // Reindirizziamo alla homepage
            response.sendRedirect("home");

        } catch (SQLException e) {
            System.err.println("Errore SQL durante il login: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database. Riprova più tardi.");
        }
    }
}