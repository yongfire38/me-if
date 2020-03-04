package wrs.effluent;

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

public class DamEffluent {

	final static Logger logger = Logger.getLogger(DamEffluent.class);

	// 다목적댐 방류수 수질 조회 서비스 - 다목적댐 방류수 수질 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터는 조회 시작 년도(yyyy), 댐코드의 2개
		if (args.length == 2) {

			if (args[0].length() == 4) {

				logger.info("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("effluent_DamEffluent_url");
				String service_key = JsonParser.getProperty("effluent_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(
						JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16_" + args[0] + "_" + args[1] + ".dat");

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메세지
					pw.write("|^");
					pw.write("srymd"); // 측정월
					pw.write("|^");
					pw.write("dgr"); // 수온(℃)
					pw.write("|^");
					pw.write("ph"); // pH
					pw.write("|^");
					pw.write("do1"); // DO(mg/L)
					pw.write("|^");
					pw.write("bod"); // BOD(mg/L)
					pw.write("|^");
					pw.write("cod"); // COD(mg/L)
					pw.write("|^");
					pw.write("ss"); // SS(mg/L)
					pw.write("|^");
					pw.write("tn"); // T-N(mg/L)
					pw.write("|^");
					pw.write("tp"); // T-P(mg/L)
					pw.write("|^");
					pw.write("pop"); // PO4-P(mg/L)
					pw.write("|^");
					pw.write("td"); // 탁도(NTU)
					pw.write("|^");
					pw.write("ec"); // 전기전도도
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

				json = JsonParser.parseWrsJson_eff(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);

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

				StringBuffer srymd = new StringBuffer(" "); // 측정월
				StringBuffer dgr = new StringBuffer(" "); // 수온(℃)
				StringBuffer ph = new StringBuffer(" "); // pH
				StringBuffer do1 = new StringBuffer(" "); // DO(mg/L)
				StringBuffer bod = new StringBuffer(" "); // BOD(mg/L)
				StringBuffer cod = new StringBuffer(" "); // COD(mg/L)
				StringBuffer ss = new StringBuffer(" "); // SS(mg/L)
				StringBuffer tn = new StringBuffer(" "); // T-N(mg/L)
				StringBuffer tp = new StringBuffer(" "); // T-P(mg/L)
				StringBuffer pop = new StringBuffer(" "); // PO4-P(mg/L)
				StringBuffer td = new StringBuffer(" "); // 탁도(NTU)
				StringBuffer ec = new StringBuffer(" "); // 전기전도도
				StringBuffer seqno = new StringBuffer(" "); // 순번

				for (int i = 1; i <= pageCount; i++) {

					json = JsonParser.parseWrsJson_eff(service_url, service_key, String.valueOf(i), args[0], args[1]);

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (resultCode.equals("00")) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(srymd, keyname, "srymd", item_obj);
									JsonParser.colWrite(dgr, keyname, "dgr", item_obj);
									JsonParser.colWrite(ph, keyname, "ph", item_obj);
									JsonParser.colWrite(do1, keyname, "do1", item_obj);
									JsonParser.colWrite(bod, keyname, "bod", item_obj);
									JsonParser.colWrite(cod, keyname, "cod", item_obj);
									JsonParser.colWrite(ss, keyname, "ss", item_obj);
									JsonParser.colWrite(tn, keyname, "tn", item_obj);
									JsonParser.colWrite(tp, keyname, "tp", item_obj);
									JsonParser.colWrite(pop, keyname, "pop", item_obj);
									JsonParser.colWrite(td, keyname, "td", item_obj);
									JsonParser.colWrite(ec, keyname, "ec", item_obj);
									JsonParser.colWrite(seqno, keyname, "seqno", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(resultCode);
								resultSb.append("|^");
								resultSb.append(resultMsg);
								resultSb.append("|^");
								resultSb.append(srymd);
								resultSb.append("|^");
								resultSb.append(dgr);
								resultSb.append("|^");
								resultSb.append(ph);
								resultSb.append("|^");
								resultSb.append(do1);
								resultSb.append("|^");
								resultSb.append(bod);
								resultSb.append("|^");
								resultSb.append(cod);
								resultSb.append("|^");
								resultSb.append(ss);
								resultSb.append("|^");
								resultSb.append(tn);
								resultSb.append("|^");
								resultSb.append(tp);
								resultSb.append("|^");
								resultSb.append(pop);
								resultSb.append("|^");
								resultSb.append(td);
								resultSb.append("|^");
								resultSb.append(ec);
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

				TransSftp.transSftp(
						JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16_" + args[0] + "_" + args[1] + ".dat",
						"WRS");

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
