package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.VinoBean;
import model.VinoDAO;

//Servlet che gestisce la lista di vini
@WebServlet(urlPatterns = {"", "/home"}) // /home
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private VinoDAO vinoDAO = new VinoDAO();

    public HomeServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            Collection<VinoBean> catalogo = vinoDAO.doRetrieveAll("Data_Aggiunta DESC");
            request.setAttribute("catalogo", catalogo); //Salva il catalogo in Attribute

        } catch (SQLException e) {
            System.err.println("Errore SQL durante il recupero del catalogo: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database. Riprova più tardi.");
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}