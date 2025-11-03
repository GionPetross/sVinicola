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
import model.OffertaBean; 
import model.OffertaDAO; 

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private VinoDAO vinoDAO = new VinoDAO();
	private OffertaDAO offertaDAO = new OffertaDAO(); 

	public HomeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Collection<VinoBean> catalogo;
        
        String tipo = request.getParameter("tipo");
        String search = request.getParameter("search");
        String origine = request.getParameter("origine");
        String alcolOp = request.getParameter("alcol_op"); //operatore
        String alcolVal = request.getParameter("alcol_val"); 
        
        try {
            if (tipo != null && !tipo.isEmpty()) {
            	//Filtro tipo
                catalogo = vinoDAO.doRetrieveByTipo(tipo);
                request.setAttribute("filtroAttivo", "Tipo: " + tipo);
                
            } else if (search != null && !search.isEmpty()) {
                //Cerca Nome
                catalogo = vinoDAO.doRetrieveByName(search);
                request.setAttribute("filtroAttivo", "Ricerca per: " + search);
                
            } else if (origine != null && !origine.isEmpty()) {
                //Cerca Orignie
                catalogo = vinoDAO.doRetrieveByOrigine(origine);
                request.setAttribute("filtroAttivo", "Origine: " + origine);

            } else if (alcolOp != null && !alcolOp.isEmpty() && alcolVal != null && !alcolVal.isEmpty()) {
                //Cerca alcol
                try {
                    double valore = Double.parseDouble(alcolVal);
                    String operatore = alcolOp.equals("gt") ? ">=" : "<="; // gt = Greater Than, lt = Less Than
                    
                    catalogo = vinoDAO.doRetrieveByAlcol(valore, operatore);
                    request.setAttribute("filtroAttivo", "Alcol " + (alcolOp.equals("gt") ? ">" : "<") + " " + valore + "%");
                } catch (NumberFormatException e) {
                    // Valore non valido, torniamo al catalogo completo
                    catalogo = vinoDAO.doRetrieveAll("Data_Aggiunta DESC");
                }
                
            } else {
                // Caso E: Homepage normale
                catalogo = vinoDAO.doRetrieveAll("Data_Aggiunta DESC");
            }

            // Salviamo il catalogo nella request
            request.setAttribute("catalogo", catalogo);

        } catch (SQLException e) {
            System.err.println("Errore SQL durante il recupero del catalogo: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database. Riprova più tardi.");
            return;
        }

        // Inoltriamo alla JSP
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
        dispatcher.forward(request, response);
    }
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}