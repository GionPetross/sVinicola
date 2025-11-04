package control;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.VinoBean;
import model.VinoDAO;

@WebServlet("/admin/aggiungi_vino")
public class AggiungiVinoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private VinoDAO vinoDAO = new VinoDAO();

	//FORM get
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_aggiungi_vino.jsp");
		dispatcher.forward(request, response);
	}

	//Inserisci nel db
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		VinoBean vino = new VinoBean();
		String nomeVino = request.getParameter("nome");
		
		try {
			vino.setNome(nomeVino);
			vino.setAnnata(Integer.parseInt(request.getParameter("annata")));
			vino.setTipo(request.getParameter("tipo"));
			vino.setDescrizione(request.getParameter("descrizione"));
			vino.setPercentualeAlcolica(Double.parseDouble(request.getParameter("alcol")));
			vino.setImmagine(request.getParameter("immagine"));
			vino.setPrezzo(new BigDecimal(request.getParameter("prezzo")));
			vino.setStock(Integer.parseInt(request.getParameter("stock")));
			vino.setFormato(request.getParameter("formato"));
			vino.setOrigine(request.getParameter("origine"));
			vino.setInVendita(true);
			vinoDAO.doSave(vino);
			
			// Messaggio Feedback successo
			session.setAttribute("feedbackSuccesso", "Il vino '" + nomeVino + "' è stato aggiunto al catalogo.");

		} catch (SQLException e) {
			System.err.println("Errore SQL AggiungiVino: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore del database durante il salvataggio.");
		} catch (NumberFormatException e) {
			System.err.println("Errore Formato AggiungiVino: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore nei dati: Assicurati che Prezzo, Stock, Annata e Alcol siano numeri validi.");
		}
		
		response.sendRedirect(request.getContextPath() + "/admin/dashboard");
	}

}