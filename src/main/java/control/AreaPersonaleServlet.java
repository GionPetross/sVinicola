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

@WebServlet("/area_personale")
public class AreaPersonaleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);

		//Controllo se utente o guest
		if (session == null || session.getAttribute("utente") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		UtenteBean utente = (UtenteBean) session.getAttribute("utente");

		//GESTIONE FEEDBACK DALLA SESSIONE
		String messaggioSuccesso = (String) session.getAttribute("feedbackSuccesso");
		String messaggioErrore = (String) session.getAttribute("feedbackErrore");
		
		if (messaggioSuccesso != null) {
			request.setAttribute("messaggioSuccesso", messaggioSuccesso);
			session.removeAttribute("feedbackSuccesso");
		}
		if (messaggioErrore != null) {
			request.setAttribute("messaggioErrore", messaggioErrore);
			session.removeAttribute("feedbackErrore");
		}
		
		try {
			List<IndirizzoBean> indirizzi = indirizzoDAO.doRetrieveByUtente(utente.getIdUtente()); 
			request.setAttribute("indirizzi", indirizzi); 
			
		} catch (SQLException e) {
			System.err.println("Errore SQL durante il recupero degli indirizzi: " + e.getMessage());
			request.setAttribute("messaggioErrore", "Errore database durante il caricamento dei tuoi indirizzi.");
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/area_personale.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}