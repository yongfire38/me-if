package wri.sluicePresentCondition;

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

public class Mnt {

	final static Logger logger = Logger.getLogger(Mnt.class);

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문현황정보(10분)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 댐코드 1개, 날짜를 yyyymmdd로 2개 받는다. 파라미터 유효성 체크는 파싱 때 체크
		if (args.length == 3) {

			if (args[1].length() == 8 && args[2].length() == 8) {

				logger.info("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("sluicePresentCondition_mnt_url");
				String service_key = JsonParser.getProperty("sluicePresentCondition_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_04.dat");

				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					pw.write("obsrdtmnt"); // 일시
					pw.write("|^");
					pw.write("lowlevel"); // 댐수위
					pw.write("|^");
					pw.write("rf"); // 강우량
					pw.write("|^");
					pw.write("inflowqy"); // 유입량
					pw.write("|^");
					pw.write("totdcwtrqy"); // 총방류량
					pw.write("|^");
					pw.write("rsvwtqy"); // 저수량
					pw.write("|^");
					pw.write("rsvwtrt"); // 저수율
					pw.write("|^");
					pw.write("numOfRows"); // 줄수
					pw.write("|^");
					pw.write("pageNo"); // 페이지번호
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
				String numberOfRows_str = "";

				json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1],
						args[2]);

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");

					int numOfRows = ((Long) body.get("numOfRows")).intValue();
					int totalCount = ((Long) body.get("totalCount")).intValue();
					numberOfRows_str = Integer.toString(numOfRows);

					pageCount = (totalCount / numOfRows) + 1;

				} catch (Exception e) {
					e.printStackTrace();
				}

				// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

				StringBuffer resultSb = new StringBuffer("");

				StringBuffer obsrdtmnt = new StringBuffer(" "); // 일시
				StringBuffer lowlevel = new StringBuffer(" "); // 댐수위
				StringBuffer rf = new StringBuffer(" "); // 강우량
				StringBuffer inflowqy = new StringBuffer(" "); // 유입량
				StringBuffer totdcwtrqy = new StringBuffer(" "); // 총방류량
				StringBuffer rsvwtqy = new StringBuffer(" "); // 저수량
				StringBuffer rsvwtrt = new StringBuffer(" "); // 저수율

				for (int i = 1; i <= pageCount; ++i) {

					json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0], args[1],
							args[2]);

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

									JsonParser.colWrite(obsrdtmnt, keyname, "obsrdtmnt", item_obj);
									JsonParser.colWrite(lowlevel, keyname, "lowlevel", item_obj);
									JsonParser.colWrite(rf, keyname, "rf", item_obj);
									JsonParser.colWrite(inflowqy, keyname, "inflowqy", item_obj);
									JsonParser.colWrite(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
									JsonParser.colWrite(rsvwtqy, keyname, "rsvwtqy", item_obj);
									JsonParser.colWrite(rsvwtrt, keyname, "rsvwtrt", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(obsrdtmnt);
								resultSb.append("|^");
								resultSb.append(lowlevel);
								resultSb.append("|^");
								resultSb.append(rf);
								resultSb.append("|^");
								resultSb.append(inflowqy);
								resultSb.append("|^");
								resultSb.append(totdcwtrqy);
								resultSb.append("|^");
								resultSb.append(rsvwtqy);
								resultSb.append("|^");
								resultSb.append(rsvwtrt);
								resultSb.append("|^");
								resultSb.append(numberOfRows_str);
								resultSb.append("|^");
								resultSb.append(String.valueOf(i));
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

				TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_04.dat", "WRI");

				long end = System.currentTimeMillis();
				logger.info("실행 시간 : " + (end - start) / 1000.0 + "초");

			} else {
				logger.debug("파라미터 형식 에러!!");
				System.exit(-1);
			}

		} else {
			logger.debug("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
