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
	    
	    // Recupero parametri
	    String tipo = request.getParameter("tipo");
	    String search = request.getParameter("search");
	    String origine = request.getParameter("origine");
	    String alcolOp = request.getParameter("alcol_op");
	    String alcolValStr = request.getParameter("alcol_val");
	    boolean inPromozione = "true".equals(request.getParameter("in_promozione"));
	    String offertaIdStr = request.getParameter("offerta_id");
	    String isAjax = request.getParameter("ajax");
	    
	    double alcolVal = 0.0;
	    if (alcolValStr != null && !alcolValStr.isEmpty()) {
	        try {
	            alcolVal = Double.parseDouble(alcolValStr);
	        } catch (NumberFormatException e) {
	            alcolVal = 0.0; 
	        }
	    }

	    try {
	        
	        if (offertaIdStr != null && !offertaIdStr.isEmpty()) {
	            // === Click sul Banner ===
	            int idOfferta = Integer.parseInt(offertaIdStr);
	            catalogo = vinoDAO.doRetrieveByOfferta(idOfferta);
	            request.setAttribute("filtroAttivo", "Offerta Speciale");
	            
	        } else {
	            // === Filtri normali ===
	            catalogo = vinoDAO.doRetrieveByFilters(tipo, search, origine, alcolOp, alcolVal, inPromozione);
	        }
	        
	        request.setAttribute("catalogo", catalogo);
	        if (isAjax == null || !isAjax.equals("true")) {
	            Collection<OffertaBean> offerte = offertaDAO.doRetrieveAllActive("Data_Fine ASC");
	            request.setAttribute("offerte", offerte);
	        }

	    } catch (SQLException | NumberFormatException e) { // Aggiunto NumberFormat
	        System.err.println("Errore SQL durante il caricamento della home: " + e.getMessage());
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore del database.");
	        return;
	    }

	    if (isAjax != null && isAjax.equals("true")) {
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/_catalogo.jsp");
	        dispatcher.forward(request, response);
	    } else {
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/home.jsp");
	        dispatcher.forward(request, response);
	    }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}