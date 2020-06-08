package common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser {

	//인터넷 연계서버의 프로퍼티 파일 용
	public static String getProperty(String keyName) {

		String os = System.getProperty("os.name").toLowerCase();

		String value = "";
		String resource = "";

		if (os.indexOf("windows") > -1) {
			// 윈도우면 현재 실행위치 내 conf 폴더 안
			resource = System.getProperty("user.dir") + "\\conf\\apiConfig.properties";
		} else {
			// 윈도우 외에는 (사실상 리눅스 서버) 서버 절대경로를 하드코딩
			resource = "/home/chkusr/EIBP2_APP/conf/apiConfig.properties";
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

	// mysql 커넥션
	// 로컬 설정, 설정은 프로퍼티 파일에서 변경
	public static Connection getConnection() throws SQLException, ClassNotFoundException {

		Class.forName(getProperty("driver"));

		Connection connection = DriverManager.getConnection(getProperty("url"), getProperty("username"),
				getProperty("password"));

		return connection;
	}

	// postgreSQL 커넥션
	// 로컬 설정, 설정은 프로퍼티 파일에서 변경

	public static Connection getPostGreSqlConnection() throws SQLException, ClassNotFoundException {

		Class.forName(getProperty("post_driver"));

		Connection connection = DriverManager.getConnection(getProperty("post_url"), getProperty("post_username"),
				getProperty("post_password"));

		return connection;
	}

	// 사업코드 값을 DB에서 읽어 와 리스트에 저장 후 리턴 (mySql)
	// 로컬 설정, 설정은 프로퍼티 파일에서 변경
	public static List<String> getBusinnessCodeList() throws SQLException, ClassNotFoundException {
		List<String> list = new ArrayList<String>();

		try {

			Connection con = getConnection();

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

	// 해당 테이블에 컬럼 개수 조회해서 리턴 (postgreSQL)

	public static int getColumnCount(String table_name) throws SQLException, ClassNotFoundException {

		int colCount = 0;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {

			con = getPostGreSqlConnection();

			// statement 생성
			st = con.createStatement();

			// 테이블 이름은 파라미터로 받더라도 하드 코딩한 스키마는 변경해줘야 됨
			rs = st.executeQuery(getProperty("post_colCount_query") + "'" + table_name + "'");

			if (rs.next()) {
				colCount = rs.getInt(1);
			}

		} catch (SQLException sqlEX) {
			System.out.println(sqlEX);
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (SQLException sqlEX) {
				System.out.println(sqlEX);
			}
		}

		return colCount;

	}

	// 문자열 utf 8 인코딩
	public static String encode_UTF8(String param) {

		try {
			param = URLEncoder.encode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("검색어 인코딩 실패", e);
		}

		return param;
	}

	// 공백 또는 null 체크
	public static boolean isEmpty(Object obj) {

		if (obj == null)
			return true;

		if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
			return true;
		}

		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj instanceof List) {
			return ((List<?>) obj).isEmpty();
		}

		if (obj instanceof Object[]) {
			return (((Object[]) obj).length == 0);
		}

		return false;

	}

	// 특정문자열이 숫자인지 체크
	public static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// 특문 제거하고 결과가 8글자 아니면 공백 처리
	// ex) 1984.10.06 -> 19841006, 1984.10. -> " "

	public static StringBuffer colWrite_waterMeasuring(StringBuffer sb, String keyname, String chkCol,
			JSONObject item) {

		if (keyname.equals(chkCol)) {

			if (!(JsonParser.isEmpty(item.get(keyname)))) {

				String content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
						.replaceAll("(\\s{2,}|\\t{2,})", " ").replace(".", "");

				// System.out.println(content);

				SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
				dateFormatParser.setLenient(false);

				try {
					dateFormatParser.parse(content);

					sb.setLength(0);
					sb.append(content);
				} catch (Exception e) {
					System.out.println("잘못된 날짜 형식이므로 빈 값으로 바꿉니다.");

					sb.setLength(0);
					sb.append(" ");

				}

				// System.out.println(sb.toString());

			} else {
				sb.setLength(0);
				sb.append(" ");
			}

		}

		return sb;
	}

	// colWrite_waterMeasuring의 String 버전
	public static String colWrite_waterMeasuring_String(String content, String keyname, String chkCol,
			JSONObject item) {

		if (keyname.equals(chkCol)) {

			if (!(JsonParser.isEmpty(item.get(keyname)))) {

				content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
						.replaceAll("(\\s{2,}|\\t{2,})", " ").replace(".", "").replace(",", "");

				// System.out.println(content);

				SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
				dateFormatParser.setLenient(false);

				try {
					dateFormatParser.parse(content);

				} catch (Exception e) {
					System.out.println("잘못된 날짜 형식이므로 빈 값으로 바꿉니다.");

					content = " ";

				}

			} else {
				content = " ";
			}

		}

		return content;
	}

	// 파싱한 데이터를 StringBuffer에 씀(null 체크, trim처리와 줄바꿈 없애는 것, 연속된 공백 치환도 같이)
	// sns와 같은 로직이면 음수값이 없어져 버리는 이슈로 분리
	public static StringBuffer colWrite(StringBuffer sb, String keyname, String chkCol, JSONObject item) {

		if (keyname.equals(chkCol)) {

			if (!(JsonParser.isEmpty(item.get(keyname)))) {

				String content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
						.replaceAll("(\\s{2,}|\\t{2,})", " ");

				sb.setLength(0);
				sb.append(content);
			} else {
				sb.setLength(0);
				sb.append(" ");
			}

		}

		return sb;
	}

	// colWrite의 String 버전
	public static String colWrite_String(String content, String keyname, String chkCol, JSONObject item) {

		if (keyname.equals(chkCol)) {
			if (!(JsonParser.isEmpty(item.get(keyname)))) {
				content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
						.replaceAll("(\\s{2,}|\\t{2,})", " ").replace(",", "");
			} else {
				content = " ";
			}
		}
		return content;
	}

	// 파싱한 데이터를 StringBuffer에 씀(null 체크, trim처리와 줄바꿈 없애는 것, utf8 인코딩 처리도 같이)
	// sns 쪽 데이터에서 문제되는 emoji와 홑따옴표도 제거함
	public static StringBuffer colWrite_sns(StringBuffer sb, String keyname, String chkCol, JSONObject item) {

		if (keyname.equals(chkCol)) {

			if (!(JsonParser.isEmpty(item.get(keyname)))) {

				String content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
						.replace("'", "").replace("&#39;", "").replace("&#34;", "")
						.replaceAll("(\\s{2,}|\\t{2,})", " ");

				// 에러 유발자들인 emoji 제거..
				Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
				Matcher emoticonsMatcher = emoticons.matcher(content);
				content = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
						.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

				// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
				if (content.isEmpty()) {
					content = " ";
				}

				sb.setLength(0);
				sb.append(content);
			} else {
				sb.setLength(0);
				sb.append(" ");
			}

		}

		return sb;
	}

	// colWrite_sns의 String 버전
	public static String colWrite_sns_String(String content, String keyname, String chkCol, JSONObject item) {

		if (keyname.equals(chkCol)) {

			if (!(JsonParser.isEmpty(item.get(keyname)))) {

				content = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ").replace("'", "")
						.replace("&#39;", "").replace("&#34;", "").replaceAll("(\\s{2,}|\\t{2,})", " ");

				// 에러 유발자들인 emoji 제거..
				Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
				Matcher emoticonsMatcher = emoticons.matcher(content);
				content = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
						.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

				// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
				if (content.isEmpty()) {
					content = " ";
				}

			} else {
				content = " ";
			}

		}

		return content;
	}
	
	// eic에서 쓰기 위한 String 널 값 변환 반환
		public static String colWrite_String_eic(String content) {

			if (!(JsonParser.isEmpty(content))) {
				content = content.toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("(\\s{2,}|\\t{2,})",
						" ");
			} else {
				content = " ";
			}

			return content;
		}
		
		// epe에서 쓰기 위한 String 널 값 변환 반환
		public static String colWrite_String_epe(String content) {

			if (!(JsonParser.isEmpty(content))) {
				content = content.toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("(\\s{2,}|\\t{2,})",
						" ").replace("&lt;", "<").replace("&gt;", ">").replace("&apos;", "'").replace("&quot;", "\"").replace("&#8231;", "‧").replace("&#65378;", "｢").replace("&#65379;", "｣");
			} else {
				content = " ";
			}

			return content;
		}
	

	// dms to decimal, latitude
	public static String dmsTodecimal_latitude(String dms) {

		String decimalStr = "";

		// 일단 공백을 전부 없앤다
		dms = dms.replace(" ", "");

		int dmsDo = 0;
		int dmsMinute = 0;
		double dmsSecond = 0.0;
		String dmsDirection = "";

		// 형식이 제각각이다.... 방위 알파벳 있다면
		if ((dms.indexOf("N") > -1) || (dms.indexOf("E") > -1) || (dms.indexOf("S") > -1) || (dms.indexOf("W") > -1)) {

			dmsDo = Integer.parseInt(dms.substring(0, 2));
			dmsMinute = Integer.parseInt(dms.substring(3, 5));
			// 마지막 글자 전전까지가 초
			dmsSecond = Double.parseDouble(dms.substring(6, dms.length() - 2));
			// 마지막 글자가 방위 알파벳
			dmsDirection = dms.substring(dms.length() - 1, dms.length());

		} else {

			dmsDo = Integer.parseInt(dms.substring(0, 2));
			dmsMinute = Integer.parseInt(dms.substring(3, 5));
			// 마지막 글자까지 초에 포함됨
			dmsSecond = Double.parseDouble(dms.substring(6, dms.length()));
			dmsDirection = "";

		}

		double decimal = 0;

		decimal = Math.signum(dmsDo) * (Math.abs(dmsDo) + (dmsMinute / 60.0) + (dmsSecond / 3600.0));

		// 남반구일 경우
		if (dmsDirection.equals("S") || dmsDirection.equals("W")) {
			decimal *= -1;
		}
		// 소수점 넷째 자리 반올림
		decimal = Math.round(decimal * 10000) / 10000.0;

		decimalStr = Double.toString(decimal);

		return decimalStr;

	}

	// dms to decimal, longitude
	public static String dmsTodecimal_longitude(String dms) {

		String decimalStr = "";

		// 일단 공백을 전부 없앤다
		dms = dms.replace(" ", "");

		int dmsDo = 0;
		int dmsMinute = 0;
		double dmsSecond = 0.0;
		String dmsDirection = "";

		// 형식이 제각각이다.... 방위 알파벳 있다면
		if ((dms.indexOf("N") > -1) || (dms.indexOf("E") > -1) || (dms.indexOf("S") > -1) || (dms.indexOf("W") > -1)) {

			dmsDo = Integer.parseInt(dms.substring(0, 3));
			dmsMinute = Integer.parseInt(dms.substring(4, 6));
			// 마지막 글자 전까지가 초
			dmsSecond = Double.parseDouble(dms.substring(7, dms.length() - 2));
			// 마지막 글자가 방위 알파벳
			dmsDirection = dms.substring(dms.length() - 1, dms.length());

		} else {

			dmsDo = Integer.parseInt(dms.substring(0, 3));
			dmsMinute = Integer.parseInt(dms.substring(4, 6));
			// 마지막 글자까지 초에 포함됨
			dmsSecond = Double.parseDouble(dms.substring(7, dms.length()));
			dmsDirection = "";

		}

		double decimal = 0;

		decimal = Math.signum(dmsDo) * (Math.abs(dmsDo) + (dmsMinute / 60.0) + (dmsSecond / 3600.0));

		// 남반구일 경우
		if (dmsDirection.equals("S") || dmsDirection.equals("W")) {
			decimal *= -1;
		}
		// 소수점 넷째 자리 반올림
		decimal = Math.round(decimal * 10000) / 10000.0;

		decimalStr = Double.toString(decimal);

		return decimalStr;

	}

	// dms to decimal, split 이용(위도 경도가 제멋대로여서 기존 메서드 사용 불가능한 경우)
	public static String dmsTodecimal_split(String dms) {

		String decimalStr = "";

		// 일단 공백을 전부 없앤다
		dms = dms.replace(" ", "").replace(".", "").replace("⑴", "").replace("?", "").replace("*", "");

		// 남반구 좌표가 올 것 같진 않으므로 방위도 날린다 (앞에 붙었다 뒤에 붙었다 불규칙해서 판단이 어려움)
		// 한글도 들어가 있으니 한글도 날린다
		dms = dms.replaceAll("[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z]", "");

		// 첫 글자가 숫자가 아닌 경우가 있었다...
		if (!(isNumeric(dms.substring(0, 1)))) {
			dms = dms.substring(1);
		}

		System.out.println("dms::::" + dms);

		String[] dms_inds = dms
				.split("[°|˚|＇|．|，|,|;|:|；|^|´|'|′|’|“|″|˝|‘|”|/|`|?|(|)|{|}|[|]|<|>|&|#|$|%|!|-|_|~|`|=|+|@|\"]");

		int dmsDo = 0;
		int dmsMinute = 0;
		double dmsSecond = 0.0;
		String dmsDirection = "";

		// 형식이 제각각이다.... 방위 알파벳 있다면
		if ((dms.indexOf("N") > -1) || (dms.indexOf("E") > -1) || (dms.indexOf("S") > -1) || (dms.indexOf("W") > -1)) {

			dmsDo = Integer.parseInt(dms_inds[0]);
			dmsMinute = Integer.parseInt(dms_inds[1]);

			if (dms_inds.length >= 3) {
				dmsSecond = Double.parseDouble(dms_inds[2]);
			}

			if (dms_inds.length == 4) {
				// 마지막 글자가 방위 알파벳
				dmsDirection = dms_inds[3];
			}

		} else {

			dmsDo = Integer.parseInt(dms_inds[0]);
			dmsMinute = Integer.parseInt(dms_inds[1]);

			if (dms_inds.length >= 3) {
				dmsSecond = Double.parseDouble(dms_inds[2]);
			}

			dmsDirection = "";

		}

		double decimal = 0;

		decimal = Math.signum(dmsDo) * (Math.abs(dmsDo) + (dmsMinute / 60.0) + (dmsSecond / 3600.0));

		// 남반구일 경우
		if (dmsDirection.equals("S") || dmsDirection.equals("W")) {
			decimal *= -1;
		}
		// 소수점 넷째 자리 반올림
		decimal = Math.round(decimal * 10000) / 10000.0;

		decimalStr = Double.toString(decimal);

		return decimalStr;

	}

	// 환경영향평가 파싱 (사업코드 하나를 파라미터로 받아서 파싱)
	public static String parseEiaJson(String service_url, String service_key, String mgtNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + mgtNo + "&serviceKey=" + service_key;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);

			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : mgtNo :" + mgtNo);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : mgtNo :" + mgtNo);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");

			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 환경영향평가 파싱 (사업코드 하나를 파라미터로 받아서 파싱)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseEiaJson_obj(String service_url, String service_key, String mgtNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + mgtNo + "&serviceKey=" + service_key;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);

			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.airquality.GetIvstg") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";		
						} else if(a[i].getClassName().indexOf("eia.airquality.GetPredict") > -1) {	
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.airquality.GetStackStdr") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.beffatStrtgySmallScaleDscssSttusInfoInqireService.GetBsnsStrtgySmallScaleDscssBsnsDetailIngInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : perCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.beffatStrtgySmallScaleDscssSttusInfoInqireService.GetBsnsStrtgySmallScaleDscssBsnsDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : perCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.ecoCycle.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDecsnCnInfoInqireService.GetDecsnCnIngbtntOpinionDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : resultCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDraftDsplayInfoInqireService.GetDraftPblancDsplaybtntOpinionDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : eiaCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDraftDsplayInfoInqireService.GetStrategyDraftPblancDsplaybtntOpinionDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : perCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDscssSttusInfoInqireService.GetDscssSttusDscssChngIngDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : eiaCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDscssSttusInfoInqireService.GetDscssSttusDscssOpinionDetailInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : eiaCd :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";	
						} else if(a[i].getClassName().indexOf("eia.envrnAffcSelfDgnssLocplcInfoInqireService.GetEnvrnExmntInfoInqire") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : buBun :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.floraFauna.GetGreen") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.floraFauna.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.floraFauna.GetScope") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.foulsmell.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.foulsmell.GetPredict") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetAl") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetMine") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetNtrfs") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetRidge") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetSlr") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.geological.GetSlt") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.greenhouseGas.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.hydraulics.GetArea") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.hydraulics.GetRiver") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.landUse.GetCategory") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.landUse.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.landUse.GetSpfc") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.maritime.GetAmplt") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.maritime.GetAmpltNm") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.maritime.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.maritime.GetModel") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.noiseVibration.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.population.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.population.GetPredict") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.sntPbh.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.soil.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.waterquality.GetInfo") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("eia.waterquality.GetIvstg") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
							json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";	
						} else if(a[i].getClassName().indexOf("wri.excllncobsrvt.Excllcode") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : damcode :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";	
						} else if(a[i].getClassName().indexOf("wri.excllncobsrvt.Walcode") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : damcode :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";	
						} else if(a[i].getClassName().indexOf("wrs.waterFlux.FcltyList") > -1) {
							System.out.print("공공데이터 서버 비 JSON 응답 : damcode :" + mgtNo);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";	
						}
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : mgtNo :" + mgtNo);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : mgtNo :" + mgtNo);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");

			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 환경영향평가 파싱 (타입 구분자와 코드 하나를 파라미터로 받아서 파싱)
	public static String parseEiaJson(String service_url, String service_key, String code, String type)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&type=" + type;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": type :" + type);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": type :" + type);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": type :" + type);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 환경영향평가 파싱 (타입 구분자와 코드 하나를 파라미터로 받아서 파싱)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseEiaJson_obj(String service_url, String service_key, String code, String type)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&type=" + type;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": type :" + type);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.envrnAffcEvlDecsnCnInfoInqireService.GetFileInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": type :" + type);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} else if(a[i].getClassName().indexOf("eia.envrnAffcSelfDgnssLocplcInfoInqireService.GetSelfDgnssLocplcInfoLegaldongAdstrdManageInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : addr :" + code + ": type :" + type);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": type :" + type);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": type :" + type);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 환경영향평가 파싱 (반경 수치와 페이지 번호를 받아서 파싱)
	public static String parseEiaJson_distance(String service_url, String service_key, String pageNo, String code)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo + ": code :" + code);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : pageNo :" + pageNo + ": code :" + code);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : pageNo :" + pageNo + ": code :" + code);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 환경영향평가 파싱 (반경 수치와 페이지 번호를 받아서 파싱)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseEiaJson_distance_obj(String service_url, String service_key, String pageNo,
			String code) throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo + ": code :" + code);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.envrnAffcSelfDgnssLocplcInfoInqireService.GetBeffatEnvrnExmntBeffatBsnsPlaceInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo + ": code :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : pageNo :" + pageNo + ": code :" + code);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : pageNo :" + pageNo + ": code :" + code);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 페이지 번호와 x,y 좌표를 받아서 조회
	// 환경영향평가 정보 서비스
	public static String parseEiaJson(String service_url, String service_key, String pageNo, String center_X,
			String center_Y) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&centerX=" + center_X + "&centerY=" + center_Y
				+ "&numOfRows=999" + "&pageNo=" + pageNo;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : center_X :" + center_X + ": center_Y :" + center_Y);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : center_X :" + center_X + ": center_Y :" + center_Y);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : center_X :" + center_X + ": center_Y :" + center_Y);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");

			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 페이지 번호와 x,y 좌표를 받아서 조회
	// 환경영향평가 정보 서비스
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseEiaJson_obj(String service_url, String service_key, String pageNo, String center_X,
			String center_Y) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&centerX=" + center_X + "&centerY=" + center_Y
				+ "&numOfRows=999" + "&pageNo=" + pageNo;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : center_X :" + center_X + ": center_Y :" + center_Y);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.envrnAffcEvlInfoInqireService.GetBsnsPlaceLnMyeonInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : center_X :" + center_X + ": center_Y :" + center_Y);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : center_X :" + center_X + ": center_Y :" + center_Y);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : center_X :" + center_X + ": center_Y :" + center_Y);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");

			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 상수도 정보 시스템 파싱 (년과 월, 페이지 번호를 받아서 파싱)
	public static String parseWatJson(String service_url, String service_key, String year, String month, String pageNo)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&year=" + year + "&month=" + month + "&pageNo="
				+ pageNo;

		URL url = new URL(urlstr);

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : year :" + year + ": month :" + month);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();

				System.out.println("JSON 요청 에러 : year :" + year + ": month :" + month);
				
				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : year :" + year + ": month :" + month);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 상수도 정보 시스템 파싱 (년과 월, 페이지 번호를 받아서 파싱)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWatJson_obj(String service_url, String service_key, String year, String month,
			String pageNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&year=" + year + "&month=" + month + "&pageNo="
				+ pageNo;

		URL url = new URL(urlstr);

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : year :" + year + ": month :" + month);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wat.monPurification_api.MonPurification") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : year :" + year + ": month :" + month);
							json ="{\"OPERATION\":\"MonPurification\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"1\",\"numberOfRows\":100},\"items\":[],\"measurementItems\":null},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";		
						}  
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : year :" + year + ": month :" + month);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : year :" + year + ": month :" + month);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 상수도 정보 시스템 파싱 (페이지 번호를 받아서 파싱)
	// 요청 형식이 동일한 경우 다른 시스템에서도 사용 가능 - 페이지 번호 외에는 요청 파라미터가 없는 경우
	public static String parseWatJson(String service_url, String service_key, String pageNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : pageNo :" + pageNo);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : pageNo :" + pageNo);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);

				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 상수도 정보 시스템 파싱 (페이지 번호를 받아서 파싱)
	// 요청 형식이 동일한 경우 다른 시스템에서도 사용 가능 - 페이지 번호 외에는 요청 파라미터가 없는 경우
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWatJson_obj(String service_url, String service_key, String pageNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.beffatStrtgySmallScaleDscssSttusInfoInqireService.GetBsnsStrtgySmallScaleDscssListInfoInqire") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\"}}}";
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDraftDsplayInfoInqireService.GetDraftPblancDsplayListInfoInqire") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDraftDsplayInfoInqireService.GetStrategyDraftPblancDsplayListInfoInqire") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDscssSttusInfoInqireService.GetDscssBsnsListInfoInqire") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						} else if(a[i].getClassName().indexOf("wat.fcltySvc_api.GetCwpFclty") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"operation\":\"getCwpFclty\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"0\",\"numberOfRows\":0},\"items\":[]},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						} else if(a[i].getClassName().indexOf("wat.fcltySvc_api.GetItkFclty") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"operation\":\"getItkFclty\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"0\",\"numberOfRows\":0},\"items\":[]},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						} else if(a[i].getClassName().indexOf("wat.fcltySvc_api.GetPressFclty") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"operation\":\"getPressFclty\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"0\",\"numberOfRows\":0},\"items\":[]},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						} else if(a[i].getClassName().indexOf("wri.droughtInfo.MultidamdidamCode") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
						} else if(a[i].getClassName().indexOf("wri.droughtInfo.Multidamdilist") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Indfltplt") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Waterfltplt") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						} else if(a[i].getClassName().indexOf("wrs.effluent.DamCode") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						} else if(a[i].getClassName().indexOf("wrs.waterQuality.SupplyLgldCodeList") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : pageNo :" + pageNo);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : pageNo :" + pageNo);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);

				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 파싱 (페이지 번호와 측정소 코드를 받아서 파싱)
	// 수질자동측정망 운영결과 DB
	public static String parsePriJson(String service_url, String service_key, String pageNo, String siteId,
			String ptNoList) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&siteId=" + siteId
				+ "&ptNoList=" + ptNoList + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : siteId :" + siteId + ": ptNoList :" + ptNoList);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : siteId :" + siteId + ": ptNoList :" + ptNoList);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : siteId :" + siteId + ": ptNoList :" + ptNoList);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 파싱 (페이지 번호와 측정소 코드를 받아서 파싱)
	// 수질자동측정망 운영결과 DB
	// JSONObject 버전
	public static JSONObject parsePriJson_obj(String service_url, String service_key, String pageNo, String siteId,
			String ptNoList) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&siteId=" + siteId
				+ "&ptNoList=" + ptNoList + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : siteId :" + siteId + ": ptNoList :" + ptNoList);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();

				if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : siteId :" + siteId + ": ptNoList :" + ptNoList);
				}

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 파싱 (페이지 번호와 측정소 코드, 측정년도, 측정월을 받아서 파싱)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	public static String parsePriJson_waterMeasuring(String service_url, String service_key, String pageNo,
			String... params) throws Exception {

		String ptNoList = "";
		String wmyrList = "";
		String wmodList = "";

		if (params.length == 1) {
			ptNoList = params[0];
		} else if (params.length == 2) {
			ptNoList = params[0];
			wmyrList = params[1];
		} else if (params.length == 3) {
			ptNoList = params[0];
			wmyrList = params[1];
			wmodList = params[2];
		}

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&ptNoList=" + ptNoList
				+ "&wmyrList=" + wmyrList + "&wmodList=" + wmodList + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList
								+ ": wmodList :" + wmodList);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList + ": wmodList :"
						+ wmodList);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList + ": wmodList :"
							+ wmodList);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 파싱 (페이지 번호와 측정소 코드, 측정년도, 측정월을 받아서 파싱)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parsePriJson_waterMeasuring_obj(String service_url, String service_key, String pageNo,
			String... params) throws Exception {

		String ptNoList = "";
		String wmyrList = "";
		String wmodList = "";

		if (params.length == 1) {
			ptNoList = params[0];
		} else if (params.length == 2) {
			ptNoList = params[0];
			wmyrList = params[1];
		} else if (params.length == 3) {
			ptNoList = params[0];
			wmyrList = params[1];
			wmodList = params[2];
		}

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&ptNoList=" + ptNoList
				+ "&wmyrList=" + wmyrList + "&wmodList=" + wmodList + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList
								+ ": wmodList :" + wmodList);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//getWaterMeasuringList, getWaterMeasuringListMavg 두 군데에서 이걸 같이 부른다....
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("avg") > -1){
							
							System.out.print("공공데이터 서버 비 JSON 응답  , ptNoList :" + params[0]);
							json ="{\"getWaterMeasuringListMavg\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
							
						} else {
							
							System.out.print("공공데이터 서버 비 JSON 응답  , ptNoList :" + params[0]);
							json ="{\"getWaterMeasuringList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
							
						}
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList + ": wmodList :"
						+ wmodList);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : ptNoList :" + ptNoList + ": wmyrList :" + wmyrList + ": wmodList :"
							+ wmodList);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 - 토양지하수 먹는물 공동시설 운영결과 파싱 (년도를 받아서 파싱, 필수값은 아님)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	public static String parsePriJson_drinkWater(String service_url, String service_key, String pageNo,
			String... params) throws Exception {

		String yyyy = "";

		if (params.length == 1) {
			yyyy = params[0];
		}

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&yyyy=" + yyyy
				+ "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : yyyy :" + yyyy);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : yyyy :" + yyyy);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : yyyy :" + yyyy);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 - 토양지하수 먹는물 공동시설 운영결과 파싱 (년도를 받아서 파싱, 필수값은 아님)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parsePriJson_drinkWater_obj(String service_url, String service_key, String pageNo,
			String... params) throws Exception {

		String yyyy = "";

		if (params.length == 1) {
			yyyy = params[0];
		}

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&yyyy=" + yyyy
				+ "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : yyyy :" + yyyy);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					System.out.print("공공데이터 서버 비 JSON 응답 ");
					json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : yyyy :" + yyyy);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : yyyy :" + yyyy);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 - 수질자동측정망 운영결과 DB 파싱 (년도를 받아서 파싱, 필수값은 아님)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	public static String parsePriJson_realTimeWater(String service_url, String service_key, String pageNo,
			String startDate, String endDate) throws Exception {

		int retry = 0;

		// api문서상의 요청 형식은 yyyyMMddHHmmss
		// 결과값은 날짜까지밖에 안 나오므로 통일 시켜 줌.. 어차피 검색 범위는 하루 단위
		startDate = startDate + "000000";
		endDate = endDate + "999999";

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&startDate=" + startDate
				+ "&endDate=" + endDate + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : startDate :" + startDate + ": endDate :" + endDate);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : startDate :" + startDate + ": endDate :" + endDate);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : startDate :" + startDate + ": endDate :" + endDate);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 수질 DB 정보 시스템 - 수질자동측정망 운영결과 DB 파싱 (년도를 받아서 파싱, 필수값은 아님)
	// 페이지 번호만 필수값 (값이 없으면 메서드를 부르는 쪽에서 공백값으로 치환)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parsePriJson_realTimeWater_obj(String service_url, String service_key, String pageNo,
			String startDate, String endDate) throws Exception {

		int retry = 0;

		// api문서상의 요청 형식은 yyyyMMddHHmmss
		// 결과값은 날짜까지밖에 안 나오므로 통일 시켜 줌.. 어차피 검색 범위는 하루 단위
		startDate = startDate + "000000";
		endDate = endDate + "999999";

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&startDate=" + startDate
				+ "&endDate=" + endDate + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : startDate :" + startDate + ": endDate :" + endDate);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					System.out.print("공공데이터 서버 비 JSON 응답 , startDate :" + startDate+", endDate :" + endDate);
					json ="{\"getRealTimeWaterQualityList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : startDate :" + startDate + ": endDate :" + endDate);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : startDate :" + startDate + ": endDate :" + endDate);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}
	
	
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	// 원래 wat 메서드를 썼지만 특정 json이 리턴되어야 하므로 따로 뺌
	public static JSONObject parsePriJson_radioActive_obj(String service_url, String service_key, String pageNo) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : pageNo :" + pageNo);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					System.out.print("공공데이터 서버 비 JSON 응답");
					json ="{\"getRadioActiveMaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : pageNo :" + pageNo);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : pageNo :" + pageNo);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);

				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}
	
	
	
	
	
	
	
	
	
	

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String stdt, String eddt)
			throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_obj(String service_url, String service_key, String pageNo, String stdt,
			String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.sihwavalue.Sihwavalue") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterinfos.Winfosdaywater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterinfos.Winfosweekwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String code, String stdt,
			String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_obj(String service_url, String service_key, String pageNo, String code,
			String stdt, String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.sluicePresentCondition.De") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wri.sluicePresentCondition.Hour") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wri.sluicePresentCondition.Mnt") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Dailindwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Dailwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						}
						
					}
					

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_month(String service_url, String service_key, String pageNo, String code,
			String stdt, String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_month_obj(String service_url, String service_key, String pageNo, String code,
			String stdt, String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wrs.dailwater.Dmntindstry") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Dmntwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Wikindwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.dailwater.Wikwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code + ": stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String tdate, String ldate,
			String vdate, String vtime) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_tdate = originFormat.parse(tdate);
		Date origin_ldate = originFormat.parse(ldate);
		Date origin_vdate = originFormat.parse(vdate);

		String parse_tdate = parseFormat.format(origin_tdate);
		String parse_ldate = parseFormat.format(origin_ldate);
		String parse_vdate = parseFormat.format(origin_vdate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&tdate=" + parse_tdate
				+ "&ldate=" + parse_ldate + "&vdate=" + parse_vdate + "&vtime=" + vtime + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
								+ ": vtime :" + vtime);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
						+ ": vtime :" + vtime);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
							+ ": vtime :" + vtime);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_obj(String service_url, String service_key, String pageNo, String tdate,
			String ldate, String vdate, String vtime) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_tdate = originFormat.parse(tdate);
		Date origin_ldate = originFormat.parse(ldate);
		Date origin_vdate = originFormat.parse(vdate);

		String parse_tdate = parseFormat.format(origin_tdate);
		String parse_ldate = parseFormat.format(origin_ldate);
		String parse_vdate = parseFormat.format(origin_vdate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&tdate=" + parse_tdate
				+ "&ldate=" + parse_ldate + "&vdate=" + parse_vdate + "&vtime=" + vtime + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
								+ ": vtime :" + vtime);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.multiFunctionBarrier.MultiFunctionBarrier") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
									+ ": vtime :" + vtime);
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";		
						} else if(a[i].getClassName().indexOf("wri.multiPoseDam.MultiPoseDam") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
									+ ": vtime :" + vtime);
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wri.waterDam.WaterDam") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
									+ ": vtime :" + vtime);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SRVICE\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						}
						
					}
					

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
						+ ": vtime :" + vtime);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : tdate :" + tdate + ": ldate :" + ldate + ": vdate :" + vdate
							+ ": vtime :" + vtime);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 우량관측소 코드를
	// 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String sdate, String stime,
			String edate, String etime, String excll) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll
				+ "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": excll :" + excll);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": excll :" + excll);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": excll :" + excll);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 우량관측소 코드를
	// 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_obj(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String excll) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll
				+ "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": excll :" + excll);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.excllncobsrvt.Mntrf") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": excll :" + excll);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}
					
					

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": excll :" + excll);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": excll :" + excll);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 우량관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_excll(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String excll) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll + "&damcode="
				+ damcode + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 우량관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_excll_obj(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String excll) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll + "&damcode="
				+ damcode + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.excllncobsrvt.Hourrf") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
									+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}
					

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": damcode :" + damcode + ": excll :" + excll);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 수위관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_wal(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String wal) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&wal=" + wal + "&damcode="
				+ damcode + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 수위관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_wal_obj(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String wal) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_sdate = originFormat.parse(sdate);
		Date origin_edate = originFormat.parse(edate);

		String parse_sdate = parseFormat.format(origin_sdate);
		String parse_edate = parseFormat.format(origin_edate);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
				+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&wal=" + wal + "&damcode="
				+ damcode + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
								+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wri.excllncobsrvt.Hourwal") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
									+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wri.excllncobsrvt.Mntwal") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
									+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}
					

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
						+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : sdate :" + sdate + ": stime :" + stime + ": edate :" + edate
							+ ": etime :" + etime + ": damcode :" + damcode + ": wal :" + wal);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// url과 서비스 키 이외 추가 파라미터 없이 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key;

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답");
						System.out.print("호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 ");

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 ");
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// url과 서비스 키 이외 추가 파라미터 없이 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWriJson_obj(String service_url, String service_key) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key;

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답");
						System.out.print("호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.envrnAffcEvlDecsnCnInfoInqireService.GetComCodeInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} else if(a[i].getClassName().indexOf("wri.dataPresent.DamCode") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";		
						} else if(a[i].getClassName().indexOf("wri.dataPresent.Mnt") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterinfos.Winfossgccode") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterinfos.Winfossitecode") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 ");

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 ");
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리
	// 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stDt, String stTm,
			String edDt, String edTm) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stDt = originFormat.parse(stDt);
		Date origin_edDt = originFormat.parse(edDt);

		String parse_stDt = parseFormat.format(origin_stDt);
		String parse_edDt = parseFormat.format(origin_edDt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stDt=" + parse_stDt
				+ "&stTm=" + stTm + "&edDt=" + parse_edDt + "&edTm=" + edTm + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt
								+ ": edTm :" + edTm);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println(
						"JSON 요청 에러 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);

				/*if (returnFlag.equals("Y")) {
					System.out.println(
							"JSON 요청 에러 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리
	// 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWrsJson_obj(String service_url, String service_key, String pageNo, String stDt,
			String stTm, String edDt, String edTm) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date origin_stDt = originFormat.parse(stDt);
		Date origin_edDt = originFormat.parse(edDt);

		String parse_stDt = parseFormat.format(origin_stDt);
		String parse_edDt = parseFormat.format(origin_edDt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stDt=" + parse_stDt
				+ "&stTm=" + stTm + "&edDt=" + parse_edDt + "&edTm=" + edTm + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt
								+ ": edTm :" + edTm);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wrs.waterFlux.WaterFluxList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterLevel.WaterLevelList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterPressure.WaterPressureList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterQuality.WaterQualityList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println(
						"JSON 요청 에러 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);

				/*if (returnFlag.equals("Y")) {
					System.out.println(
							"JSON 요청 에러 : stDt :" + stDt + ": stTm :" + stTm + ": edDt :" + edDt + ": edTm :" + edTm);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 구분코드 와 페이지 번호를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String code)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 구분코드 와 페이지 번호를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWrsJson_obj(String service_url, String service_key, String pageNo, String code)
			throws Exception {

		int retry = 0;

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : code :" + code);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();

					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("eia.envrnAffcEvlDecsnCnInfoInqireService.GetDecsnCnListInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : code :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";		
						} else if(a[i].getClassName().indexOf("eia.envrnAffcEvlDscssSttusInfoInqireService.GetDscssSttusDscssIngDetailInfoInqire") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : eiaCd :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterLevel.FcltyList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : eiaCd :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterPressure.FcltyList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : fcltyDivCode :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterQuality.FcltyList") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : fcltyDivCode :" + code);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}
					
				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : code :" + code);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : code :" + code);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stdt, String eddt)
			throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWrsJson_obj(String service_url, String service_key, String pageNo, String stdt,
			String eddt) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wrs.waterinfos.Winfosmonthwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작 날짜랑 끝 날짜, 지자체코드를 받아서 파싱, 날짜형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stdt, String eddt,
			String sgccd) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&sgccd=" + sgccd + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작 날짜랑 끝 날짜, 지자체코드를 받아서 파싱, 날짜형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWrsJson_obj(String service_url, String service_key, String pageNo, String stdt,
			String eddt, String sgccd) throws Exception {

		int retry = 0;

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		Date origin_stdt = originFormat.parse(stdt);
		Date origin_eddt = originFormat.parse(eddt);

		String parse_stdt = parseFormat.format(origin_stdt);
		String parse_eddt = parseFormat.format(origin_eddt);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
				+ "&eddt=" + parse_eddt + "&sgccd=" + sgccd + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wrs.waterinfos.Winfosmonthqtrwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} else if(a[i].getClassName().indexOf("wrs.waterinfos.Winfosmonthqtrwater") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": eddt :" + eddt + ": sgccd :" + sgccd);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작년도(yyyy)와 댐 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson_eff(String service_url, String service_key, String pageNo, String stdt,
			String damcd) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + stdt + "&damcd="
				+ damcd + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				// 2020.06.02 : 빈 Json을 리턴하도록 롤백
				/*if (json.indexOf("</") > -1) {

					returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": damcd :" + damcd);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();

				}*/

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": damcd :" + damcd);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": damcd :" + damcd);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 시작년도(yyyy)와 댐 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	// JSONObject 버전
	// 2020.06.05 : 빈 Json 리턴해서 스킵시키는 로직 넣음
	public static JSONObject parseWrsJson_eff_obj(String service_url, String service_key, String pageNo, String stdt,
			String damcd) throws Exception {

		int retry = 0;

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + stdt + "&damcd="
				+ damcd + "&numOfRows=999";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			//String returnFlag = "N";

			try {

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				// http error로 xml형태의 데이터가 나왔다면 에러를 발생시켜 재시도 로직으로. 재시도는 최대 5회
				if (json.indexOf("</") > -1) {

					/*returnFlag = "Y";

					StackTraceElement[] a = new Throwable().getStackTrace();

					for (int i = a.length - 1; i > 0; i--) {
						System.out.println("비정상 응답 : json :" + json);

						System.out.print("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": damcd :" + damcd);
						System.out.print(", 호출 클래스 - " + a[i].getClassName());
						System.out.print(", 메소드 - " + a[i].getMethodName());
						System.out.print(", 라인 - " + a[i].getLineNumber());

						System.out.println();
					}

					throw new Exception();*/
					
					StackTraceElement[] a = new Throwable().getStackTrace();
					
					//빈 값의 리턴 json이 달라져야 하므로 호출 클래스명으로 구분
					for (int i = a.length - 1; i > 0; i--) {
						
						if(a[i].getClassName().indexOf("wrs.effluent.DamEffluent") > -1){	
							System.out.println("공공데이터 서버 비 JSON 응답 : stdt :" + stdt + ": damcd :" + damcd);
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";		
						} 
						
					}

				}

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);

				return obj;

			} catch (Exception e) {
				e.printStackTrace();
				
				System.out.println("JSON 요청 에러 : stdt :" + stdt + ": damcd :" + damcd);

				/*if (returnFlag.equals("Y")) {
					System.out.println("JSON 요청 에러 : stdt :" + stdt + ": damcd :" + damcd);
				}*/

				urlconnection.disconnect();
				retry++;
				Thread.sleep(3000);
				System.out.println(retry + "번째 재시도..");
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 네이버 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseBlogJson_naver(String service_url, String naver_client_id, String naver_client_secret,
			String query, String job_dt, String start) throws Exception {

		int retry = 0;

		// utf8 인코딩
		query = encode_UTF8(query);

		String urlstr = service_url + query + "&start=" + start;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			try {

				Thread.sleep(3000);

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("X-Naver-Client-Id", naver_client_id);
				urlconnection.setRequestProperty("X-Naver-Client-Secret", naver_client_secret);

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream()));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("JSON 요청 에러 : query :" + query);
				urlconnection.disconnect();
				retry++;
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 다음 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseBlogJson_daum(String service_url, String daum_api_key, String query, String job_dt,
			String page) throws Exception {

		int retry = 0;

		// utf8 인코딩
		query = encode_UTF8(query);

		String urlstr = service_url + query + "&page=" + page;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			try {

				Thread.sleep(3000);

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Authorization", "KakaoAK " + daum_api_key);

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream()));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("JSON 요청 에러 : query :" + query);
				urlconnection.disconnect();
				retry++;
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 구글 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseJson_google(String service_url, String google_api_key, String google_api_cx, String query,
			String job_dt, String start) throws Exception {

		int retry = 0;

		// utf8 인코딩
		query = encode_UTF8(query);

		String urlstr = service_url + query + "&key=" + google_api_key + "&cx=" + google_api_cx + "&start=" + start;

		while (retry < 5) {

			String json = "";
			BufferedReader br = null;

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			try {

				Thread.sleep(3000);

				urlconnection.setRequestMethod("GET");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream()));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("JSON 요청 에러 : query :" + query);
				urlconnection.disconnect();
				retry++;
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

	// 상수도 정보 시스템 파싱 (페이지 번호를 받아서 파싱)
	// 요청 형식이 동일한 경우 다른 시스템에서도 사용 가능 - 페이지 번호 외에는 요청 파라미터가 없는 경우
	public static String parseSpcJson(String service_url, String service_key, String pageIndex) throws Exception {

		int retry = 0;

		String urlstr = service_url + pageIndex + "&accessKey=" + service_key + "&userId=bigdata";

		while (retry < 5) {

			BufferedReader br = null;
			String json = "";

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();

			try {

				Thread.sleep(3000);

				urlconnection.setRequestMethod("GET");
				urlconnection.setRequestProperty("Accept", "application/json");

				int responseCode = urlconnection.getResponseCode();

				if (responseCode == 200 || responseCode == 201) {
					br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
				} else {
					br = new BufferedReader(new InputStreamReader(urlconnection.getErrorStream(), "UTF-8"));
				}

				String line;

				while ((line = br.readLine()) != null) {
					json = json + line + "\n";
				}

				urlconnection.disconnect();

				return json;

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("JSON 요청 에러 : pageIndex :" + pageIndex);
				urlconnection.disconnect();
				retry++;
			}

		}

		System.out.println("재시도 회수 초과");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
