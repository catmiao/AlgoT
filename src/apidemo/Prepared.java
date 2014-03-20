package apidemo;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;
	import java.util.logging.Level;
	import java.util.logging.Logger;

	public class Prepared {

	    public static void main(String[] args) {

	        Connection con = null;
	        PreparedStatement pst = null;

	        String url = "jdbc:mysql://localhost:3306/algoT";
	        String user = "wenshen";
	        String password = "111111";

	        try {

	            String tablename = "test";
	            con = DriverManager.getConnection(url, user, password);

	            pst = con.prepareStatement("INSERT INTO "+user+" VALUES(?)");
	            pst.setString(1,"1,'IBM',10,100.1,100.2,100.0,100,122231415");
	            pst.executeUpdate();

	        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(Prepared.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);

	        } finally {

	            try {
	                if (pst != null) {
	                    pst.close();
	                }
	                if (con != null) {
	                    con.close();
	                }

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(Prepared.class.getName());
	                lgr.log(Level.SEVERE, ex.getMessage(), ex);
	            }
	        }
	    }
	}

