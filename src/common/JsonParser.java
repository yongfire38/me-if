package common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JsonParser {
	
	final static Logger logger = Logger.getLogger(JsonParser.class);

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

	public static String parseJson(String service_url, String service_key, String mgtNo) {

		BufferedReader br = null;
		String json = "";
		
		//logger.info("mgtNo :" + mgtNo);

		String urlstr = service_url + mgtNo
				+ "&serviceKey=" + service_key;
		try {

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

			String line;
			while ((line = br.readLine()) != null) {
				json = json + line + "\n";
			}

			// 테스트 출력
			// logger.info(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

}
