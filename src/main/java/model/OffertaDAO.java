// File: model/OffertaDAO.java
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OffertaDAO implements DAOinterface<OffertaBean> {

    private static final String TABLE_NAME = "Offerta";
    private static final String APPLICATO_TABLE = "Applicato";
    private static final String VINO_TABLE = "Vino";

	// 				==================================================================
	// 												METODI DAO
	// 				==================================================================
    @Override
    public synchronized void doSave(OffertaBean offerta) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO " + TABLE_NAME + 
                     " (Data_Inizio, Data_Fine, Percentuale, Immagine_Promozionale) VALUES (?, ?, ?, ?)";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            
            ps.setDate(1, offerta.getDataInizio());
            ps.setDate(2, offerta.getDataFine());
            ps.setInt(3, offerta.getPercentuale());
            ps.setString(4, offerta.getImmaginePromozionale());

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


    @Override
    public synchronized OffertaBean doRetrieveByKey(int idOfferta) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        OffertaBean bean = new OffertaBean();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID_Offerta = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idOfferta);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bean.setIdOfferta(rs.getInt("ID_Offerta"));
                bean.setDataInizio(rs.getDate("Data_Inizio"));
                bean.setDataFine(rs.getDate("Data_Fine"));
                bean.setPercentuale(rs.getInt("Percentuale"));
                bean.setImmaginePromozionale(rs.getString("Immagine_Promozionale"));
            } else {
                return null; // Nessuna offerta trovata
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
    public synchronized boolean doDelete(int idOfferta) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        int result = 0;

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID_Offerta = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idOfferta);

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
    public synchronized Collection<OffertaBean> doRetrieveAll(String order) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        Collection<OffertaBean> offerte = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME;
        
        if (order != null && !order.isEmpty()) {
            sql += " ORDER BY " + order;
        }

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OffertaBean bean = new OffertaBean();
                bean.setIdOfferta(rs.getInt("ID_Offerta"));
                bean.setDataInizio(rs.getDate("Data_Inizio"));
                bean.setDataFine(rs.getDate("Data_Fine"));
                bean.setPercentuale(rs.getInt("Percentuale"));
                bean.setImmaginePromozionale(rs.getString("Immagine_Promozionale"));
                offerte.add(bean);
            }

        } finally {
            try {
                if (ps != null) ps.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return offerte;
    }


    @Override
    public synchronized void doUpdate(OffertaBean offerta) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "UPDATE " + TABLE_NAME + 
                     " SET Data_Inizio = ?, Data_Fine = ?, Percentuale = ?, Immagine_Promozionale = ? " +
                     " WHERE ID_Offerta = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            
            ps.setDate(1, offerta.getDataInizio());
            ps.setDate(2, offerta.getDataFine());
            ps.setInt(3, offerta.getPercentuale());
            ps.setString(4, offerta.getImmaginePromozionale());
            ps.setInt(5, offerta.getIdOfferta());

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
    // METODI SPECIFICI (NON in DAOinterface)
    // ==================================================================

    //Metodo specifico per recuperare tutti i vini associati a una singola offerta.
    public synchronized List<VinoBean> doRetrieveViniByOfferta(int idOfferta) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        List<VinoBean> vini = new ArrayList<>();

        // JOIN tra Vino e Applicato
        String sql = "SELECT V.* FROM " + VINO_TABLE + " V JOIN " + APPLICATO_TABLE + " A " +
                     "ON V.ID_Vino = A.ID_Vino WHERE A.ID_Offerta = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idOfferta);

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
                bean.setDataAggiunta(rs.getTimestamp("Data_Aggiunta"));
                
                vini.add(bean);
            }

        } finally {
            try {
                if (ps != null) ps.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return vini;
    }
}