package wri.droughtInfo;

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

public class MultidamdidamCode {

	final static Logger logger = Logger.getLogger(MultidamdidamCode.class);

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 다목적댐 가뭄정보 댐 코드 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 서비스 키만 요구함, 실행시 필수 매개변수 없음
		if (args.length == 0) {

			logger.info("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("droughtInfo_damcode_url");
			String service_key = JsonParser.getProperty("droughtInfo_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_02.dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("damcd"); // 댐코드
				pw.write("|^");
				pw.write("damnm"); // 댐명칭
				pw.write("|^");
				pw.write("seqno"); // 순번
				pw.println();
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
			String json = "";

			int pageNo = 0;
			int pageCount = 0;

			json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");

				int numOfRows = ((Long) body.get("numOfRows")).intValue();
				int totalCount = ((Long) body.get("totalCount")).intValue();

				pageCount = (totalCount / numOfRows) + 1;

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer damcdcrd = new StringBuffer(" "); // 댐코드
			StringBuffer damnm = new StringBuffer(" "); // 댐명칭
			StringBuffer seqno = new StringBuffer(" "); // 순번

			for (int i = 1; i <= pageCount; ++i) {

				json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
					JSONObject items = (JSONObject) body.get("items");

					String resultCode = header.get("resultCode").toString().trim();

					if (resultCode.equals("00")) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(damcdcrd, keyname, "damcode", item_obj);
								JsonParser.colWrite(damnm, keyname, "damnm", item_obj);
								JsonParser.colWrite(seqno, keyname, "seqno", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(damcdcrd);
							resultSb.append("|^");
							resultSb.append(damnm);
							resultSb.append("|^");
							resultSb.append(seqno);
							resultSb.append(System.getProperty("line.separator"));

						}

					} else if (resultCode.equals("03")) {
						logger.debug("data not exist!!");
					} else {
						logger.debug("parsing error!!");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				logger.info("진행도::::::" + i + "/" + pageCount);

				Thread.sleep(1000);

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

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_02.dat", "WRI");

			long end = System.currentTimeMillis();
			logger.info("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			logger.debug("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
