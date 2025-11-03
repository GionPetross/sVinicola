// File: model/OrdineDAO.java
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

		// Query per inserire l'ordine principale
		String sqlOrdine = "INSERT INTO " + ORDINE_TABLE + 
						   " (ID_Utente, ID_IndirizzoSpedizione, Totale_Complessivo) VALUES (?, ?, ?)";

		// Query per inserire i dettagli dell'ordine
		String sqlDettaglio = "INSERT INTO " + DETTAGLIO_TABLE + 
							  " (ID_Ordine, ID_Vino, Quantita, Prezzo_Storico) VALUES (?, ?, ?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			
			//commit solo alla fine se non ci sono errori
			connection.setAutoCommit(false); 

			psOrdine = connection.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS);
			psOrdine.setInt(1, ordine.getIdUtente());
			psOrdine.setInt(2, ordine.getIdIndirizzoSpedizione());
			psOrdine.setBigDecimal(3, ordine.getTotaleComplessivo());
			
			psOrdine.executeUpdate();
			
			ResultSet rsKeys = psOrdine.getGeneratedKeys();
			if (rsKeys.next()) {
				idOrdineGenerato = rsKeys.getInt(1); // Genera il nuovo id
			} else {
				throw new SQLException("Creazione ordine fallita, nessun ID ottenuto.");
			}


			psDettaglio = connection.prepareStatement(sqlDettaglio);
			
			for (DettaglioOrdineBean dettaglio : dettagli) {
				psDettaglio.setInt(1, idOrdineGenerato);
				psDettaglio.setInt(2, dettaglio.getIdVino());
				psDettaglio.setInt(3, dettaglio.getQuantita());
				psDettaglio.setBigDecimal(4, dettaglio.getPrezzoStorico());
				
				psDettaglio.addBatch(); //
			}
			
			psDettaglio.executeBatch(); // 
			
			
			connection.commit(); //Commit manuale
			
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
			//ritorna alle informazione di default
			try {
				if (psDettaglio != null) psDettaglio.close();
				if (psOrdine != null) psOrdine.close();
				if (connection != null) {
					connection.setAutoCommit(true); // Ripristina l'autocommit
					connection.close();
				}
			} catch (SQLException e) {
				System.err.println("Errore durante la chiusura delle risorse: " + e.getMessage());
			}
		}
	}


	//Metodo non supportato
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
				bean.setIdIndirizzoSpedizione(rs.getInt("ID_IndirizzoSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
			} else {
				return null; // Nessun ordine trovato
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
			connection.commit();

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
				bean.setIdIndirizzoSpedizione(rs.getInt("ID_IndirizzoSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
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

		String sql = "UPDATE " + ORDINE_TABLE + 
					 " SET ID_Utente = ?, ID_IndirizzoSpedizione = ?, Data = ?, Totale_Complessivo = ? " +
					 " WHERE ID_Ordine = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setInt(1, ordine.getIdUtente());
			ps.setInt(2, ordine.getIdIndirizzoSpedizione());
			ps.setTimestamp(3, ordine.getData());
			ps.setBigDecimal(4, ordine.getTotaleComplessivo());
			ps.setInt(5, ordine.getIdOrdine());

			ps.executeUpdate();
			connection.commit();

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

	//Recupera tutti gli ordini di un utente
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
				bean.setIdIndirizzoSpedizione(rs.getInt("ID_IndirizzoSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
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
	
	//Restituisci tutti i prodotti di un ordine
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
	
	//Querry utile per visualizzare gli ultimi ordini per l'admin
	public synchronized List<OrdineBean> doRetrieveByDateRange(java.sql.Date start, java.sql.Date end) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<OrdineBean> ordini = new ArrayList<>();

		// CAST(Data AS DATE) :)
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
				bean.setIdIndirizzoSpedizione(rs.getInt("ID_IndirizzoSpedizione"));
				bean.setData(rs.getTimestamp("Data"));
				bean.setTotaleComplessivo(rs.getBigDecimal("Totale_Complessivo"));
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