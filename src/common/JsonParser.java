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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

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

	// 파싱한 데이터를 StringBuffer에 씀(null 체크, trim처리와 줄바꿈 없애는 것도 같이)
	public static StringBuffer colWrite(StringBuffer sb, String keyname, String chkCol, JSONObject item) {

		if (keyname.equals(chkCol)) {
			if (!(JsonParser.isEmpty(item.get(keyname)))) {
				sb.setLength(0);
				sb.append(item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " "));
			} else {
				sb.setLength(0);
				sb.append(" ");
			}

		}

		return sb;
	}

	// 환경영향평가 파싱 (사업코드 하나를 파라미터로 받아서 파싱)
	public static String parseEiaJson(String service_url, String service_key, String mgtNo) {

		BufferedReader br = null;
		String json = "";

		// logger.info("mgtNo :" + mgtNo);

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
			// logger.info(json);

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

		// logger.info("mgtNo :" + mgtNo);

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
			// logger.info(json);

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

		// logger.info("mgtNo :" + mgtNo);

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
			// logger.info(json);

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

		// logger.info("mgtNo :" + mgtNo);

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
			// logger.info(json);

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
				// logger.info(json);

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

			String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&damcode=" + code
					+ "&stdt=" + parse_stdt + "&eddt=" + parse_eddt + "&numOfRows=999";
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
				// logger.info(json);

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
				// logger.info(json);

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
				// logger.info(json);

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
				// logger.info(json);

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

		// logger.info("mgtNo :" + mgtNo);

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
			// logger.info(json);

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
				// logger.info(json);

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
	public static String parseWrsJson(String service_url, String service_key, String pageNo, String fcltyDivCode) {

		BufferedReader br = null;
		String json = "";

		// logger.info("mgtNo :" + mgtNo);

		String urlstr = service_url + "&serviceKey=" + service_key + "&pageNo=" + pageNo + "&fcltyDivCode="+ fcltyDivCode +"&numOfRows=999";
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
