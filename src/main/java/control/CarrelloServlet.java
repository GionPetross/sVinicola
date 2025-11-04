package control;

import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Carrello;
import model.VinoBean;
import model.VinoDAO;
import model.VoceCarrello;

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VinoDAO vinoDAO = new VinoDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Carrello carrello = (Carrello) session.getAttribute("carrello");
		if (carrello == null) {
			carrello = new Carrello();
			session.setAttribute("carrello", carrello);
		}
		
		String action = request.getParameter("action");
		
		// JSON-Java
		JSONObject jsonResponse = new JSONObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			if (action != null) {
				String idVinoStr = request.getParameter("id");
				int idVino = (idVinoStr != null) ? Integer.parseInt(idVinoStr) : -1; //Parsa la string
				
				switch (action) {
					
					case "add":
						// --- AGGIUNGI (AJAX) ---
						VinoBean vinoDaAggiungere = vinoDAO.doRetrieveByKey(idVino);
						if (vinoDaAggiungere != null) {
							carrello.aggiungiProdotto(vinoDaAggiungere);
						}
						jsonResponse.put("cartCount", carrello.getNumVoci());
						response.getWriter().write(jsonResponse.toString());
						return;
						
					case "remove":
						// --- RIMUOVI (AJAX) ---
						carrello.rimuoviProdotto(idVino);
						
						jsonResponse.put("cartCount", carrello.getNumVoci());
						jsonResponse.put("newTotal", carrello.getTotale());
						response.getWriter().write(jsonResponse.toString());
						return;
						
					case "update":
						// --- AGGIORNA (AJAX) ---
						int quantita = Integer.parseInt(request.getParameter("quantita"));
						carrello.modificaQuantita(idVino, quantita);
						
						VoceCarrello voceAggiornata = null;
						for (VoceCarrello voce : carrello.getVoci()) {
							if (voce.getVino().getIdVino() == idVino) {
								voceAggiornata = voce;
								break;
							}
						}
						
						// Rispondi con i nuovi totali
						jsonResponse.put("cartCount", carrello.getNumVoci());
						jsonResponse.put("newTotal", carrello.getTotale());
						if (voceAggiornata != null) {
							jsonResponse.put("newSubtotal", voceAggiornata.getSubtotale());
						}
						response.getWriter().write(jsonResponse.toString());
						return;
						
					case "clear":
						carrello.svuota();
						response.sendRedirect("carrello");
						return;
				}
			}
		
		} catch (Exception e) { 
			System.err.println("Errore nella CarrelloServlet: " + e.getMessage());
			jsonResponse.put("error", "Errore nell'aggiornamento del carrello.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(jsonResponse.toString());
			return;
		}

		// --- AZIONE DEFAULT ---
		response.setContentType("text/html");
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/carrello.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}
