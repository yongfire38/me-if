package common;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	
	//수집서버의 프로퍼티 파일 용
		public static String getProperty(String keyName) {

			String os = System.getProperty("os.name").toLowerCase();

			String value = "";
			String resource = "";

			if (os.indexOf("windows") > -1) {
				// 윈도우면 현재 실행위치 내 conf 폴더 안
				resource = System.getProperty("user.dir") + "\\conf\\apiConfig.properties";
			} else {
				// 윈도우 외에는 (사실상 리눅스 서버) 서버 절대경로를 하드코딩
				resource = "/home/eibpadm/EIBP2_APP/conf/apiConfig.properties";
			}

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
		
		//과학원은 절대경로가 어떻게 될 지 모르므로 상대경로로 작성
		public static String getScienceProperty(String keyName) {

			String os = System.getProperty("os.name").toLowerCase();

			String value = "";
			String resource = "";
			
			// OS 환경변수에서 받은 경로
			Path relativePath = Paths.get(System.getenv("APP_ROOT"));
			
		    String path = relativePath.toString();

			if (os.indexOf("windows") > -1) {
				// 윈도우면 현재 실행위치 내 conf 폴더 안
				resource = System.getProperty("user.dir") + "\\conf\\apiConfig.properties";
			} else {
				// 윈도우 외에는 (사실상 리눅스 서버) OS 환경변수에서 받은 경로 아래 conf 폴더 밑
				resource = path + "/conf/apiConfig.properties";
			}
			
			System.out.println("resource::" + resource);

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
	static Connection getOraConnection_local() throws SQLException, ClassNotFoundException {
		
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
	
	//외부 시스템 오라클 커넥션
	public static Connection getOraConnection(String sysNm) throws SQLException, ClassNotFoundException {
		
		 Connection conn = null;
		 
		 String user = "";
         String pw = "";
         String url = "";
		 
		 if(sysNm.equals("eic")){
			 
			  user = getProperty("eic_oracle_user");
	          pw = getProperty("eic_oracle_pw");
	          url = getProperty("eic_oracle_url");
	          
		 } else if(sysNm.equals("kwa")){
			 
			  user = getProperty("kwa_oracle_user");
	          pw = getProperty("kwa_oracle_pw");
	          url = getProperty("kwa_oracle_url");
	          
		 } else if(sysNm.equals("sgs")){
			 
			  user = getScienceProperty("sgs_oracle_user");
	          pw = getScienceProperty("sgs_oracle_pw");
	          url = getScienceProperty("sgs_oracle_url");
	          
		 } else if(sysNm.equals("wem")){
			 
			  user = getScienceProperty("wem_oracle_user");
	          pw = getScienceProperty("wem_oracle_pw");
	          url = getScienceProperty("wem_oracle_url");
	          
		 } else if(sysNm.equals("tmd")){
			 
			  user = getScienceProperty("tmd_oracle_user");
	          pw = getScienceProperty("tmd_oracle_pw");
	          url = getScienceProperty("tmd_oracle_url");
	          
		 }
       
        try {
        	
        	
        	if(sysNm.equals("eic") || sysNm.equals("kwa")) {
        		 Class.forName(getProperty("eic_oracle_driver"));    
        	} else {
        		 Class.forName(getScienceProperty("eic_oracle_driver"));    
        	}
            
            conn = DriverManager.getConnection(url, user, pw);
            
            System.out.println("Database에 연결되었습니다.\n");
            
        } catch (ClassNotFoundException cnfe) {
            System.out.println("DB 드라이버 로딩 실패 :"+ cnfe.toString());
        } catch (SQLException sqle) {
            System.out.println("DB 접속실패 : "+ sqle.toString());
            System.out.println("DB 접속시도 정보  user :"+ user +": pw : "+ pw +": url :"+ url);
        } catch (Exception e) {
            System.out.println("Unkonwn error");
            e.printStackTrace();
        }
        return conn;   

	}
	
	//외부시스템 postgresql 커넥션
	public static Connection getPostConnection(String sysNm) throws SQLException, ClassNotFoundException {
		
		 Connection conn = null;
		 
		 String user = "";
        String pw = "";
        String url = "";
		 
		 if(sysNm.equals("eco")){
			 
			  user = getProperty("eco_post_username");
	          pw = getProperty("eco_post_password");
	          url = getProperty("eco_post_url");
	          
		 } 
      
       try {
       	       	
    	   //드라이버는 어느 시스템이든 postgresql 이면 동일
           Class.forName(getProperty("eco_post_driver"));        
           conn = DriverManager.getConnection(url, user, pw);
           
           System.out.println("Database에 연결되었습니다.\n");
           
       } catch (ClassNotFoundException cnfe) {
           System.out.println("DB 드라이버 로딩 실패 :"+ cnfe.toString());
       } catch (SQLException sqle) {
           System.out.println("DB 접속실패 : "+ sqle.toString());
           System.out.println("DB 접속시도 정보  user :"+ user +": pw : "+ pw +": url :"+ url);
       } catch (Exception e) {
           System.out.println("Unkonwn error");
           e.printStackTrace();
       }
       return conn;   

	}

}
