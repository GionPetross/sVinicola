package control;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ordine-confermato")
public class OrdineConfermatoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		Integer idOrdine = (Integer) session.getAttribute("idOrdineConfermato");
		
		if (idOrdine == null) {
			// Se l'ID non è presente, l'utente è arrivato per errore
			response.sendRedirect("home");
			return;
		}
		
		//Passa l'ID alla request
		request.setAttribute("idOrdine", idOrdine);
		
		session.removeAttribute("idOrdineConfermato");
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ordine_confermato.jsp");
		dispatcher.forward(request, response);
	}
}