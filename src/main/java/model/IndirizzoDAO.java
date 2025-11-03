package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IndirizzoDAO implements DAOinterface<IndirizzoBean> {

    private static final String TABLE_NAME = "Indirizzo";

	// 				==================================================================
	// 												METODI DAO
	// 				==================================================================
    @Override
    public synchronized void doSave(IndirizzoBean indirizzo) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO " + TABLE_NAME + 
                     " (Via, CAP, Citta, Provincia, ID_Utente) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            
            ps.setString(1, indirizzo.getVia());
            ps.setString(2, indirizzo.getCap());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getProvincia());
            ps.setInt(5, indirizzo.getIdUtente());

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
    public synchronized IndirizzoBean doRetrieveByKey(int idIndirizzo) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        IndirizzoBean bean = new IndirizzoBean();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID_Indirizzo = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idIndirizzo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bean.setIdIndirizzo(rs.getInt("ID_Indirizzo"));
                bean.setVia(rs.getString("Via"));
                bean.setCap(rs.getString("CAP"));
                bean.setCitta(rs.getString("Citta"));
                bean.setProvincia(rs.getString("Provincia"));
                bean.setIdUtente(rs.getInt("ID_Utente"));
            } else {
                return null; // Nessun indirizzo trovato
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
    public synchronized boolean doDelete(int idIndirizzo) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        int result = 0;

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE ID_Indirizzo = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idIndirizzo);

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
    public synchronized Collection<IndirizzoBean> doRetrieveAll(String order) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        Collection<IndirizzoBean> indirizzi = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME;
        
        if (order != null && !order.isEmpty()) {
            sql += " ORDER BY " + order;
        }

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                IndirizzoBean bean = new IndirizzoBean();
                bean.setIdIndirizzo(rs.getInt("ID_Indirizzo"));
                bean.setVia(rs.getString("Via"));
                bean.setCap(rs.getString("CAP"));
                bean.setCitta(rs.getString("Citta"));
                bean.setProvincia(rs.getString("Provincia"));
                bean.setIdUtente(rs.getInt("ID_Utente"));
                indirizzi.add(bean);
            }

        } finally {
            try {
                if (ps != null) ps.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return indirizzi;
    }


    @Override
    public synchronized void doUpdate(IndirizzoBean indirizzo) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        String sql = "UPDATE " + TABLE_NAME + 
                     " SET Via = ?, CAP = ?, Citta = ?, Provincia = ?, ID_Utente = ? " +
                     " WHERE ID_Indirizzo = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            
            ps.setString(1, indirizzo.getVia());
            ps.setString(2, indirizzo.getCap());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getProvincia());
            ps.setInt(5, indirizzo.getIdUtente());
            ps.setInt(6, indirizzo.getIdIndirizzo());

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

    // Metodo specifico per recuperare tutti gli indirizzi di un singolo utente.
    public synchronized List<IndirizzoBean> doRetrieveByUtente(int idUtente) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        List<IndirizzoBean> indirizzi = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE ID_Utente = ?";

        try {
            connection = DataSourceProvider.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, idUtente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                IndirizzoBean bean = new IndirizzoBean();
                bean.setIdIndirizzo(rs.getInt("ID_Indirizzo"));
                bean.setVia(rs.getString("Via"));
                bean.setCap(rs.getString("CAP"));
                bean.setCitta(rs.getString("Citta"));
                bean.setProvincia(rs.getString("Provincia"));
                bean.setIdUtente(rs.getInt("ID_Utente"));
                indirizzi.add(bean);
            }

        } finally {
            try {
                if (ps != null) ps.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return indirizzi;
    }
}