package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UtenteBean;
import model.UtenteDAO;

@WebServlet("/modifica-profilo")
public class ModificaProfiloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UtenteDAO utenteDAO = new UtenteDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("utente") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/area_personale.jsp");
		UtenteBean utenteInSessione = (UtenteBean) session.getAttribute("utente");
		int idUtente = utenteInSessione.getIdUtente();

		String nuovoUsername = request.getParameter("username");
		String nuovaPassword = request.getParameter("new_password");
		String vecchiaPassword = request.getParameter("old_password");
		
		String attualePassword = utenteInSessione.getPassword();
		String attualeUsername = utenteInSessione.getNomeUtente();
		String messaggioErrore = null;

		try {
			if (vecchiaPassword == null || !vecchiaPassword.equals(attualePassword)) {
				messaggioErrore = "La password attuale inserita non è corretta. Modifica annullata.";
			} else {
				if (nuovoUsername != null && !nuovoUsername.trim().isEmpty() && !nuovoUsername.equals(attualeUsername)) {
					UtenteBean checkUser = utenteDAO.doRetrieveByUsername(nuovoUsername);
					if (checkUser != null && checkUser.getIdUtente() != idUtente) {
						messaggioErrore = "Il nuovo Username è già in uso da un altro utente.";
					} else {
						utenteInSessione.setNomeUtente(nuovoUsername);
					}
				}
				if (nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
					utenteInSessione.setPassword(nuovaPassword);
				}
			}

			if (messaggioErrore == null) {
				utenteDAO.doUpdate(utenteInSessione);
				session.setAttribute("utente", utenteInSessione);
				request.setAttribute("messaggioSuccesso", "Profilo aggiornato con successo!");
			} else {
				request.setAttribute("messaggioErrore", messaggioErrore);
			}

		} catch (SQLException e) {
			System.err.println("Errore SQL durante l'aggiornamento profilo: " + e.getMessage());
			request.setAttribute("messaggioErrore", "Errore database durante l'aggiornamento.");
		}

		response.sendRedirect("area-personale");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.sendRedirect("area_personale.jsp");
	}
}