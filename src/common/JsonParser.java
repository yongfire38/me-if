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

public class JsonParser {

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

				sb.setLength(0);
				sb.append(content);
			} else {
				sb.setLength(0);
				sb.append(" ");
			}

		}

		return sb;
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
		dms = dms.replace(" ", "").replace(".","");
		
		//남반구 좌표가 올 것 같진 않으므로 방위도 날린다 (앞에 붙었다 뒤에 붙었다 불규칙해서 판단이 어려움)
		dms = dms.replaceAll("[a-z|A-Z]", "");

		String[] dms_inds = dms.split("[°|^|'|′|’|“|″|`|\"]");

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
	public static String parseEiaJson(String service_url, String service_key, String mgtNo) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + mgtNo + "&serviceKey=" + service_key;

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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 환경영향평가 파싱 (타입 구분자와 코드 하나를 파라미터로 받아서 파싱)
	public static String parseEiaJson(String service_url, String service_key, String code, String type) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&type=" + type;
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 환경영향평가 파싱 (반경 수치와 페이지 번호를 받아서 파싱)
	public static String parseEiaJson_distance(String service_url, String service_key, String pageNo, String code) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 페이지 번호와 x,y 좌표를 받아서 조회
	// 환경영향평가 정보 서비스
	public static String parseEiaJson(String service_url, String service_key, String pageNo, String center_X,
			String center_Y) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&centerX=" + center_X + "&centerY=" + center_Y
				+ "&numOfRows=999" + "&pageNo=" + pageNo;

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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 상수도 정보 시스템 파싱 (년과 월, 페이지 번호를 받아서 파싱)
	public static String parseWatJson(String service_url, String service_key, String year, String month,
			String pageNo) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&year=" + year + "&month=" + month + "&pageNo="
				+ pageNo;
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 상수도 정보 시스템 파싱 (페이지 번호를 받아서 파싱)
	// 요청 형식이 동일한 경우 다른 시스템에서도 사용 가능 - 페이지 번호 외에는 요청 파라미터가 없는 경우
	public static String parseWatJson(String service_url, String service_key, String pageNo) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 수질 DB 정보 시스템 파싱 (페이지 번호와 측정소 코드를 받아서 파싱)
	// 수질자동측정망 운영결과 DB
	public static String parsePriJson(String service_url, String service_key, String pageNo, String siteId,
			String ptNoList) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&siteId=" + siteId
				+ "&ptNoList=" + ptNoList + "&numOfRows=999";
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String stdt, String eddt) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_stdt = originFormat.parse(stdt);
			Date origin_eddt = originFormat.parse(eddt);

			String parse_stdt = parseFormat.format(origin_stdt);
			String parse_eddt = parseFormat.format(origin_eddt);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
					+ "&eddt=" + parse_eddt + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymmdd
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String code, String stdt,
			String eddt) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_stdt = originFormat.parse(stdt);
			Date origin_eddt = originFormat.parse(eddt);

			String parse_stdt = parseFormat.format(origin_stdt);
			String parse_eddt = parseFormat.format(origin_eddt);

			String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt="
					+ parse_stdt + "&eddt=" + parse_eddt + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 코드 1개와 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_month(String service_url, String service_key, String pageNo, String code,
			String stdt, String eddt) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		try {

			Date origin_stdt = originFormat.parse(stdt);
			Date origin_eddt = originFormat.parse(eddt);

			String parse_stdt = parseFormat.format(origin_stdt);
			String parse_eddt = parseFormat.format(origin_eddt);

			String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt="
					+ parse_stdt + "&eddt=" + parse_eddt + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String tdate, String ldate,
			String vdate, String vtime) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_tdate = originFormat.parse(tdate);
			Date origin_ldate = originFormat.parse(ldate);
			Date origin_vdate = originFormat.parse(vdate);

			String parse_tdate = parseFormat.format(origin_tdate);
			String parse_ldate = parseFormat.format(origin_ldate);
			String parse_vdate = parseFormat.format(origin_vdate);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&tdate=" + parse_tdate
					+ "&ldate=" + parse_ldate + "&vdate=" + parse_vdate + "&vtime=" + vtime + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 우량관측소 코드를
	// 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key, String pageNo, String sdate, String stime,
			String edate, String etime, String excll) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_sdate = originFormat.parse(sdate);
			Date origin_edate = originFormat.parse(edate);

			String parse_sdate = parseFormat.format(origin_sdate);
			String parse_edate = parseFormat.format(origin_edate);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
					+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll
					+ "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 우량관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_excll(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String excll) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_sdate = originFormat.parse(sdate);
			Date origin_edate = originFormat.parse(edate);

			String parse_sdate = parseFormat.format(origin_sdate);
			String parse_edate = parseFormat.format(origin_edate);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
					+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&excll=" + excll + "&damcode="
					+ damcode + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리, 댐코드, 수위관측소
	// 코드를 받아서 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson_wal(String service_url, String service_key, String pageNo, String sdate,
			String stime, String edate, String etime, String damcode, String wal) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_sdate = originFormat.parse(sdate);
			Date origin_edate = originFormat.parse(edate);

			String parse_sdate = parseFormat.format(origin_sdate);
			String parse_edate = parseFormat.format(origin_edate);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&sdate=" + parse_sdate
					+ "&stime=" + stime + "&edate=" + parse_edate + "&etime=" + etime + "&wal=" + wal + "&damcode="
					+ damcode + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// url과 서비스 키 이외 추가 파라미터 없이 파싱
	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합)
	public static String parseWriJson(String service_url, String service_key) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key;
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각 2자리
	// 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stDt, String stTm,
			String edDt, String edTm) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {

			Date origin_stDt = originFormat.parse(stDt);
			Date origin_edDt = originFormat.parse(edDt);

			String parse_stDt = parseFormat.format(origin_stDt);
			String parse_edDt = parseFormat.format(origin_edDt);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stDt=" + parse_stDt
					+ "&stTm=" + stTm + "&edDt=" + parse_edDt + "&edTm=" + edTm + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 구분코드 와 페이지 번호를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String code) {

		BufferedReader br = null;
		String json = "";

		String urlstr = service_url + code + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&numOfRows=999";
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 시작 날짜랑 끝 날짜를 받아서 파싱, 형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stdt, String eddt) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		try {

			Date origin_stdt = originFormat.parse(stdt);
			Date origin_eddt = originFormat.parse(eddt);

			String parse_stdt = parseFormat.format(origin_stdt);
			String parse_eddt = parseFormat.format(origin_eddt);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
					+ "&eddt=" + parse_eddt + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 시작 날짜랑 끝 날짜, 지자체코드를 받아서 파싱, 날짜형식은 yyyymm
	// 수자원통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String stdt, String eddt,
			String sgccd) {

		BufferedReader br = null;
		String json = "";

		SimpleDateFormat originFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM");

		try {

			Date origin_stdt = originFormat.parse(stdt);
			Date origin_eddt = originFormat.parse(eddt);

			String parse_stdt = parseFormat.format(origin_stdt);
			String parse_eddt = parseFormat.format(origin_eddt);

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + parse_stdt
					+ "&eddt=" + parse_eddt + "&sgccd=" + sgccd + "&numOfRows=999";
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
				// System.out.println(json);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 시작년도(yyyy)와 댐 코드를 받아서 파싱
	// 수도통합(WIS)-운영통합시스템(댐보발전통합)
	public static String parseWrsJson_eff(String service_url, String service_key, String pageNo, String stdt,
			String damcd) {

		BufferedReader br = null;
		String json = "";

		// System.out.println("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&stdt=" + stdt + "&damcd="
				+ damcd + "&numOfRows=999";
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
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 네이버 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseBlogJson_naver(String service_url, String naver_client_id, String naver_client_secret,
			String query, String job_dt, String start) {

		BufferedReader br = null;

		// utf8 인코딩
		query = encode_UTF8(query);

		String json = "";

		String urlstr = service_url + query + "&start=" + start;

		try {

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
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

			// 테스트 출력
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 다음 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseBlogJson_daum(String service_url, String daum_api_key, String query, String job_dt,
			String page) {

		BufferedReader br = null;

		// utf8 인코딩
		query = encode_UTF8(query);

		String json = "";

		String urlstr = service_url + query + "&page=" + page;

		try {

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
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

			// 테스트 출력
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	// 구글 블로그 파싱 (검색어 하나를 파라미터로 받아서 파싱)
	public static String parseJson_google(String service_url, String google_api_key, String google_api_cx, String query,
			String job_dt, String start) {

		BufferedReader br = null;

		// utf8 인코딩
		query = encode_UTF8(query);

		String json = "";

		String urlstr = service_url + query + "&key=" + google_api_key + "&cx=" + google_api_cx + "&start=" + start;

		try {

			URL url = new URL(urlstr);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
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

			// 테스트 출력
			// System.out.println(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

}
