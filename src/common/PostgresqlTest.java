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
			pstat = conn.prepareStatement("select * from \"HIAA02\"",  ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstat.executeQuery();
			//rs = st.executeQuery(JsonParser.getProperty("post_colCount_query") + "'" + args[0] + "'");

			//ResultSetMetaData rsmd = rs.getMetaData();

			// Column 수를 반환해 줌
			//int numberOfColumns = rsmd.getColumnCount();
			
			//전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
			rs.last();
			
			int rowCount = rs.getRow();
			
			//전체 레코드 개수만큼의 배열
			String[] if_seq = new String[rowCount];
			String[] if_indate = new String[rowCount];
			String[] site_id = new String[rowCount];
			String[] site_name = new String[rowCount];
			
			//다시 처음부터 조회해야 하므로 커서는 초기화
			rs.beforeFirst();
			
			int i=0;
	
			while (rs.next()) {
				
				if_seq[i] = rs.getString(1);
				if_indate[i] = rs.getString(2);
				site_id[i] = rs.getString(3);
				site_name[i] = rs.getString(4);

				 System.out.println("if_seq::"+if_seq[i]+"::if_indate::"+if_indate[i]+"::site_id::"+site_id[i]+"::site_name::"+site_name[i]);
				 i++;
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
