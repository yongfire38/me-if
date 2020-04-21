package common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	
	public static String getProperty(String keyName) {
		
		String value = "";
		String resource = "properties/apiConfig.properties";
		
		try {
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(resource);

			// 프로퍼티 파일 로딩
			props.load(new java.io.BufferedInputStream(fis));
			
			value = props.getProperty(keyName).trim();
			
            fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return value;
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		
		Class.forName(getProperty("driver"));

		Connection connection = DriverManager.getConnection(getProperty("url"), getProperty("username"),
				getProperty("password"));

		return connection;
	}

	// 사업코드 값을 DB에서 읽어 와 리스트에 저장 후 리턴
	public static List<String> getBusinnessCodeList() throws SQLException, ClassNotFoundException {

		List<String> list = new ArrayList<String>();

		try {

			// DB Connection
			Class.forName(getProperty("driver"));
			Connection con = DriverManager.getConnection(getProperty("url"), getProperty("username"),
					getProperty("password"));

			// statement 생성
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(getProperty("query"));

			while (rs.next()) {
				list.add(rs.getString(getProperty("column")));
			}

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	//오라클 로컬 커넥션
	static Connection getOraConnection() throws SQLException, ClassNotFoundException {
		
		 Connection conn = null;
         try {
             String user = "scott"; 
             String pw = "1234";
             String url = "jdbc:oracle:thin:@localhost:1521:orcl";
             
             Class.forName("oracle.jdbc.driver.OracleDriver");        
             conn = DriverManager.getConnection(url, user, pw);
             
             System.out.println("Database에 연결되었습니다.\n");
             
         } catch (ClassNotFoundException cnfe) {
             System.out.println("DB 드라이버 로딩 실패 :"+cnfe.toString());
         } catch (SQLException sqle) {
             System.out.println("DB 접속실패 : "+sqle.toString());
         } catch (Exception e) {
             System.out.println("Unkonwn error");
             e.printStackTrace();
         }
         return conn;   

	}
	

}
