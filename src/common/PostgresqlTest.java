package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresqlTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		String url = JsonParser.getProperty("post_hfg_url");
		String user = JsonParser.getProperty("post_hfg_username");
		String password = JsonParser.getProperty("post_hfg_password");

		try {

			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, user, password);
			//그냥 Statement 쓰면 커서 뒤쪽으로 이동이 안 된다...
			pstat = conn.prepareStatement("SELECT VERSION()",  ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstat.executeQuery();
			
	
			if (rs.next()) {
				
				System.out.println(rs.getString(1));
				
			} else {
				System.out.println("rs 없음");
			}

		} catch (SQLException sqlEX) {
			System.out.println(sqlEX);
		} finally {
			try {
				rs.close();
				pstat.close();
				conn.close();
			} catch (SQLException sqlEX) {
				System.out.println(sqlEX);
			}
		}

	}

}
