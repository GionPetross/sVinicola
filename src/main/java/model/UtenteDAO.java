package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.List;

public class UtenteDAO implements DAOinterface<UtenteBean> {

	private static final String TABLE_NAME = "Utente";
	//private static final String PREFERITI_TABLE = "Lista_Preferiti";
	//private static final String VINO_TABLE = "Vino";

	// 				==================================================================
	// 												METODI DAO
	// 				==================================================================

	@Override
	public synchronized void doSave(UtenteBean utente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO " + TABLE_NAME + " (Nome_Utente, Password, Ruolo, Email, Nome, Cognome) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setString(1, utente.getNomeUtente());
			ps.setString(2, utente.getPassword());
			ps.setString(3, utente.getRuolo());
			ps.setString(4, utente.getEmail());
			ps.setString(5, utente.getNome());
			ps.setString(6, utente.getCognome());

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}


	@Override
	public synchronized UtenteBean doRetrieveByKey(int idUtente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		UtenteBean bean = new UtenteBean();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID_Utente = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setNomeUtente(rs.getString("Nome_Utente"));
				bean.setPassword(rs.getString("Password"));
				bean.setRuolo(rs.getString("Ruolo"));
				bean.setEmail(rs.getString("Email"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
			} else {
				return null; // Nessun utente trovato con quell'ID
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return bean;
	}


	@Override
	public synchronized boolean doDelete(int idUtente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID_Utente = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);

			result = ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return (result != 0);
	}

	@Override
	public synchronized Collection<UtenteBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		Collection<UtenteBean> utenti = new ArrayList<>();

		String sql = "SELECT * FROM " + TABLE_NAME;
		
		if (order != null && !order.isEmpty()) {
			sql += " ORDER BY " + order;
		}

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UtenteBean bean = new UtenteBean();
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setNomeUtente(rs.getString("Nome_Utente"));
				bean.setPassword(rs.getString("Password"));
				bean.setRuolo(rs.getString("Ruolo"));
				bean.setEmail(rs.getString("Email"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
				utenti.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return utenti;
	}


	@Override
	public synchronized void doUpdate(UtenteBean utente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "UPDATE " + TABLE_NAME + 
					 " SET Nome_Utente = ?, Password = ?, Ruolo = ?, Email = ?, Nome = ?, Cognome = ? " +
					 " WHERE ID_Utente = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setString(1, utente.getNomeUtente());
			ps.setString(2, utente.getPassword());
			ps.setString(3, utente.getRuolo());
			ps.setString(4, utente.getEmail());
			ps.setString(5, utente.getNome());
			ps.setString(6, utente.getCognome());
			ps.setInt(7, utente.getIdUtente());

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}


	// ==================================================================
	// METODI SPECIFICI (NON in DAOinterface)
	// ==================================================================

	// Metodo di default per il login
	public synchronized UtenteBean doRetrieveByUsernamePassword(String username, String password) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		UtenteBean bean = new UtenteBean();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE Nome_Utente = ? AND Password = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password); // Password in chiaro

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setNomeUtente(rs.getString("Nome_Utente"));
				bean.setPassword(rs.getString("Password"));
				bean.setRuolo(rs.getString("Ruolo"));
				bean.setEmail(rs.getString("Email"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
			} else {
				return null; // Coppia username/password non trovata
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return bean;
	}
	
	//Query dell'username
	public synchronized UtenteBean doRetrieveByUsername(String username) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		UtenteBean bean = new UtenteBean();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE Nome_Utente = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setNomeUtente(rs.getString("Nome_Utente"));
				bean.setPassword(rs.getString("Password"));
				bean.setRuolo(rs.getString("Ruolo"));
				bean.setEmail(rs.getString("Email"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
			} else {
				return null; // Username non trovato
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return bean;
	}


	/* ==================================================================
	// METODI PER GESTIONE PREFERITI (Tabella Lista_Preferiti)
	// ==================================================================
	/
	//Aggiungere preferiti alla lista
	public synchronized void doAddPreferito(int idUtente, int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "INSERT INTO " + PREFERITI_TABLE + " (ID_Utente, ID_Vino) VALUES (?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setInt(1, idUtente);
			ps.setInt(2, idVino);

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}

	//Rimuovere preferiti dalla lista
	public synchronized boolean doRemovePreferito(int idUtente, int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "DELETE FROM " + PREFERITI_TABLE + " WHERE ID_Utente = ? AND ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);
			ps.setInt(2, idVino);

			result = ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return (result != 0);
	}

	//Controlla se è già preferito
	public synchronized boolean isPreferito(int idUtente, int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "SELECT COUNT(*) FROM " + PREFERITI_TABLE + " WHERE ID_Utente = ? AND ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);
			ps.setInt(2, idVino);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return false;
	}
	
	//Query che torna la lista di vini preferiti
	public synchronized List<VinoBean> doRetrievePreferiti(int idUtente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<VinoBean> preferiti = new ArrayList<>();

		// JOIN tra Vino e Lista_Preferiti
		String sql = "SELECT V.* FROM " + VINO_TABLE + " V JOIN " + PREFERITI_TABLE + " LP " +
					 "ON V.ID_Vino = LP.ID_Vino WHERE LP.ID_Utente = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				VinoBean bean = new VinoBean();
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setNome(rs.getString("Nome"));
				bean.setAnnata(rs.getInt("Annata"));
				bean.setTipo(rs.getString("Tipo"));
				bean.setDescrizione(rs.getString("Descrizione"));
				bean.setPercentualeAlcolica(rs.getDouble("Percentuale_Alcolica"));
				bean.setImmagine(rs.getString("Immagine"));
				bean.setPrezzo(rs.getBigDecimal("Prezzo"));
				bean.setStock(rs.getInt("Stock"));
				bean.setFormato(rs.getString("Formato"));
				bean.setOrigine(rs.getString("Origine"));
				bean.setInVendita(rs.getBoolean("In_Vendita"));
				
				preferiti.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return preferiti;
	}*/
}