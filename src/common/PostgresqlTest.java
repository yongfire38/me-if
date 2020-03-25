package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresqlTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        String url = "jdbc:postgresql://localhost/IF_DB";
        String user = "postgres";
        String password = "1234";
        
        try {
        	
        	Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            st = conn.createStatement();
            //rs = st.executeQuery("select * from  public.\"TIF_WMS_V_WMS_SITEINFO_WPCS\"");
            rs = st.executeQuery(JsonParser.getProperty("post_colCount_query") + "'" + args[0] + "'");

            if (rs.next())
                System.out.println(rs.getString(1));
        } catch (SQLException sqlEX) {
            System.out.println(sqlEX);
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException sqlEX) {
                System.out.println(sqlEX);
            }
        }
        
        
        
        
	}

}
