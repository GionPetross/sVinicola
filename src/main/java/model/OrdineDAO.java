package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrdineDAO implements DAOinterface<OrdineBean> {

	private static final String ORDINE_TABLE = "Ordine";
	private static final String DETTAGLIO_TABLE = "DettaglioOrdine";

	// 				==================================================================
	// 												METODI DAO
	// 				==================================================================
	public synchronized int doSaveCompleto(OrdineBean ordine, Collection<DettaglioOrdineBean> dettagli) throws SQLException {
		Connection connection = null;
		PreparedStatement psOrdine = null;
		PreparedStatement psDettaglio = null;
		int idOrdineGenerato = -1;

		// AGGIUNTO 'Stato' all'INSERT
		String sqlOrdine = "INSERT INTO " + ORDINE_TABLE + 
						   " (ID_Utente, ViaSpedizione, CapSpedizione, CittaSpedizione, ProvinciaSpedizione, Totale_Complessivo, Stato) VALUES (?, ?, ?, ?, ?, ?, ?)";

		String sqlDettaglio = "INSERT INTO " + DETTAGLIO_TABLE + 
							  " (ID_Ordine, ID_Vino, Quantita, Prezzo_Storico) VALUES (?, ?, ?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			
			connection.setAutoCommit(false); 

			psOrdine = connection.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS);
			psOrdine.setInt(1, ordine.getIdUtente());
			psOrdine.setString(2, ordine.getViaSpedizione());
			psOrdine.setString(3, ordine.getCapSpedizione());
			psOrdine.setString(4, ordine.getCittaSpedizione());
			psOrdine.setString(5, ordine.getProvinciaSpedizione());
			psOrdine.setBigDecimal(6, ordine.getTotaleComplessivo());
			psOrdine.setString(7, ordine.getStato() != null ? ordine.getStato() : "in attesa"); // Inserisce lo Stato
			
			psOrdine.executeUpdate();
			
			ResultSet rsKeys = psOrdine.getGeneratedKeys();
			if (rsKeys.next()) {
				idOrdineGenerato = rsKeys.getInt(1);
			} else {
				throw new SQLException("Creazione ordine fallita, nessun ID ottenuto.");
			}


			psDettaglio = connection.prepareStatement(sqlDettaglio);
			
			for (DettaglioOrdineBean dettaglio : dettagli) {
				psDettaglio.setInt(1, idOrdineGenerato);
				psDettaglio.setInt(2, dettaglio.getIdVino());
				psDettaglio.setInt(3, dettaglio.getQuantita());
				psDettaglio.setBigDecimal(4, dettaglio.getPrezzoStorico());
				
				psDettaglio.addBatch();
			}
			
			psDettaglio.executeBatch();
			
			
			connection.commit();
			
			return idOrdineGenerato;

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e2) {
				System.err.println("Errore durante il rollback della transazione: " + e2.getMessage());
			}
			throw new SQLException("Errore durante il salvataggio dell'ordine: " + e.getMessage());
			
		} finally {
			try {
				if (psDettaglio != null) psDettaglio.close();
				if (psOrdine != null) psOrdine.close();
				if (connection != null) {
					connection.setAutoCommit(true);
					connection.close();
				}
			} catch (SQLException e) {
				System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
			}
		}
	}


	@Override
	public void doSave(OrdineBean bean) throws SQLException {
		throw new UnsupportedOperationException("Metodo non supportato. Usare doSaveCompleto(OrdineBean ordine, Collection<DettaglioOrdineBean> dettagli).");
	}


	@Override
	public synchronized OrdineBean doRetrieveByKey(int idOrdine) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		OrdineBean bean = new OrdineBean();

		String sql = "SELECT * FROM " + ORDINE_TABLE + " WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idOrdine);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setViaSpedizione(rs.getString("ViaSpedizione"));
				bean.setCapSpedizione(rs.getString("CapSpedizione"));
				bean.setCittaSpedizione(rs.getString("CittaSpedizione"));
				bean.setProvinciaSpedizione(rs.getString("ProvinciaSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
				bean.setStato(rs.getString("Stato")); // LETTURA STATO
			} else {
				return null;
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
	public synchronized boolean doDelete(int idOrdine) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "DELETE FROM " + ORDINE_TABLE + " WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idOrdine);

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
	public synchronized Collection<OrdineBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		Collection<OrdineBean> ordini = new ArrayList<>();

		String sql = "SELECT * FROM " + ORDINE_TABLE;
		
		if (order != null && !order.isEmpty()) {
			sql += " ORDER BY " + order;
		}

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrdineBean bean = new OrdineBean();
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setViaSpedizione(rs.getString("ViaSpedizione"));
				bean.setCapSpedizione(rs.getString("CapSpedizione"));
				bean.setCittaSpedizione(rs.getString("CittaSpedizione"));
				bean.setProvinciaSpedizione(rs.getString("ProvinciaSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
				bean.setStato(rs.getString("Stato")); // LETTURA STATO
				ordini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return ordini;
	}


	@Override
	public synchronized void doUpdate(OrdineBean ordine) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		// AGGIUNTO 'Stato' all'UPDATE
		String sql = "UPDATE " + ORDINE_TABLE + 
					 " SET ID_Utente = ?, ViaSpedizione = ?, CapSpedizione = ?, CittaSpedizione = ?, ProvinciaSpedizione = ?, Data = ?, Totale_Complessivo = ?, Stato = ? " +
					 " WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setInt(1, ordine.getIdUtente());
			ps.setString(2, ordine.getViaSpedizione());
			ps.setString(3, ordine.getCapSpedizione());
			ps.setString(4, ordine.getCittaSpedizione());
			ps.setString(5, ordine.getProvinciaSpedizione());
			ps.setTimestamp(6, ordine.getData());
			ps.setBigDecimal(7, ordine.getTotaleComplessivo());
			ps.setString(8, ordine.getStato()); // SCRITTURA STATO
			ps.setInt(9, ordine.getIdOrdine());

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
	// 							METODI SPECIFICI
	// ==================================================================

	public synchronized List<OrdineBean> doRetrieveByUtente(int idUtente) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<OrdineBean> ordini = new ArrayList<>();

		String sql = "SELECT * FROM " + ORDINE_TABLE + " WHERE ID_Utente = ? ORDER BY Data DESC";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idUtente);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrdineBean bean = new OrdineBean();
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setViaSpedizione(rs.getString("ViaSpedizione"));
				bean.setCapSpedizione(rs.getString("CapSpedizione"));
				bean.setCittaSpedizione(rs.getString("CittaSpedizione"));
				bean.setProvinciaSpedizione(rs.getString("ProvinciaSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
				bean.setStato(rs.getString("Stato")); // LETTURA STATO
				ordini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return ordini;
	}
	
	public synchronized List<DettaglioOrdineBean> doRetrieveDettagli(int idOrdine) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<DettaglioOrdineBean> dettagli = new ArrayList<>();

		String sql = "SELECT * FROM " + DETTAGLIO_TABLE + " WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idOrdine);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				DettaglioOrdineBean bean = new DettaglioOrdineBean();
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setQuantita(rs.getInt("Quantita"));
				bean.setPrezzoStorico(rs.getBigDecimal("Prezzo_Storico"));
				dettagli.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return dettagli;
	}
	
	public synchronized List<OrdineBean> doRetrieveByDateRange(java.sql.Date start, java.sql.Date end) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<OrdineBean> ordini = new ArrayList<>();

		String sql = "SELECT * FROM " + ORDINE_TABLE + " WHERE CAST(Data AS DATE) BETWEEN ? AND ? ORDER BY Data ASC";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setDate(1, start);
			ps.setDate(2, end);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrdineBean bean = new OrdineBean();
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setViaSpedizione(rs.getString("ViaSpedizione"));
				bean.setCapSpedizione(rs.getString("CapSpedizione"));
				bean.setCittaSpedizione(rs.getString("CittaSpedizione"));
				bean.setProvinciaSpedizione(rs.getString("ProvinciaSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
				bean.setStato(rs.getString("Stato")); // LETTURA STATO
				ordini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return ordini;
	}
	
	// AGGIUNTA METODO SPECIFICO PER AGGIORNARE LO STATO (Admin)
	public synchronized void doUpdateStato(int idOrdine, String nuovoStato) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "UPDATE " + ORDINE_TABLE + " SET Stato = ? WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setString(1, nuovoStato);
			ps.setInt(2, idOrdine);

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}
	
	//AGGIUNTA PER FILTRAGGIO PER L'ADMIN
	public synchronized List<OrdineBean> doRetrieveByStati(List<String> stati) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<OrdineBean> ordini = new ArrayList<>();

		String sql = "SELECT * FROM " + ORDINE_TABLE + " WHERE Stato IN (";
		for (int i = 0; i < stati.size(); i++) {
			sql += "?";
			if (i < stati.size() - 1) {
				sql += ",";
			}
		}
		sql += ") ORDER BY Data DESC";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			for (int i = 0; i < stati.size(); i++) {
				ps.setString(i + 1, stati.get(i));
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrdineBean bean = new OrdineBean();
				bean.setIdOrdine(rs.getInt("ID_Ordine"));
				bean.setIdUtente(rs.getInt("ID_Utente"));
				bean.setViaSpedizione(rs.getString("ViaSpedizione"));
				bean.setCapSpedizione(rs.getString("CapSpedizione"));
				bean.setCittaSpedizione(rs.getString("CittaSpedizione"));
				bean.setProvinciaSpedizione(rs.getString("ProvinciaSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
				bean.setStato(rs.getString("Stato"));
				ordini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return ordini;
	}
}