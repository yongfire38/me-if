package wrs.waterinfos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
import common.TransSftp;

public class Winfossitecode {

	final static Logger logger = Logger.getLogger(Winfossitecode.class);

	// 지방정수장 수질정보 조회 서비스 - 지방상수도 정수장코드 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터는 지자체 코드
		// 지자체코드 조회 api에서 조회 가능

		if (args.length == 0) {

			logger.info("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("waterinfos_Winfossitecode_url");
			String service_key = JsonParser.getProperty("waterinfos_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_11.dat");

			try {

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				pw.write("sitecd"); // 정수장코드
				pw.write("|^");
				pw.write("sitenm"); // 정수장명
				pw.println();
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			// step 2. 전체 파싱
			String json = "";

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer sitecd = new StringBuffer(" "); // 정수장코드
			StringBuffer sitenm = new StringBuffer(" "); // 정수장명

			// 추가 파라미터가 없으므로 기존 메서드 이용
			json = JsonParser.parseWriJson(service_url, service_key);

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject header = (JSONObject) response.get("header");
				JSONObject items = (JSONObject) body.get("items");

				String resultCode = header.get("resultCode").toString().trim();

				if (resultCode.equals("00")) {

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite(sitecd, keyname, "sitecd", items_jsonObject);
							JsonParser.colWrite(sitenm, keyname, "sitenm", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(sitecd);
						resultSb.append("|^");
						resultSb.append(sitenm);
						resultSb.append(System.getProperty("line.separator"));

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(sitecd, keyname, "sitecd", item_obj);
								JsonParser.colWrite(sitenm, keyname, "sitenm", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(sitecd);
							resultSb.append("|^");
							resultSb.append(sitenm);
							resultSb.append(System.getProperty("line.separator"));

						}

					} else {
						logger.debug("parsing error!!");
					}

				} else if (resultCode.equals("03")) {
					logger.debug("data not exist!!");
				} else {
					logger.debug("parsing error!!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 4. 파일에 쓰기
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write(resultSb.toString());
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.info("parsing complete!");

			// step 5. 대상 서버에 sftp로 보냄

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_11.dat", "WRS");

			long end = System.currentTimeMillis();
			logger.info("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			logger.debug("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
