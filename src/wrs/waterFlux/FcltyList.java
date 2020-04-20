package wrs.waterFlux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class FcltyList {

	// 실시간 수도정보 유량 - 취수장, 정수장, 가압장 코드 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 검색 코드 (1:취수장, 2:정수장, 3:가압장, 4:배수지)

				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("waterFlux_fcltyList_url");
					String service_key = JsonParser.getProperty("waterFlux_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_02.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					// step 2. 전체 파싱
					String json = "";

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer fcltyMngNm = new StringBuffer(" "); // 시설관리명
					StringBuffer sujCode = new StringBuffer(" "); // 사업장코드

					// 파라미터 1개만 받으므로 환경영향평가 쪽 메서드 이용
					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (resultCode.equals("00") && body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
						
						

						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(fcltyMngNm, keyname, "fcltyMngNm", items_jsonObject);
								JsonParser.colWrite(sujCode, keyname, "sujCode", items_jsonObject);

							}

							// 한번에 문자열 합침
							resultSb.append(args[0]);
							resultSb.append("|^");
							resultSb.append(fcltyMngNm);
							resultSb.append("|^");
							resultSb.append(sujCode);
							resultSb.append(System.getProperty("line.separator"));

						} else if (items.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(fcltyMngNm, keyname, "fcltyMngNm", item_obj);
									JsonParser.colWrite(sujCode, keyname, "sujCode", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(args[0]);
								resultSb.append("|^");
								resultSb.append(fcltyMngNm);
								resultSb.append("|^");
								resultSb.append(sujCode);
								resultSb.append(System.getProperty("line.separator"));

							}

						}

					} else {
						System.out.println("parsing error!!");
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

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_02.dat", "WRS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("fcltyDivCode :" + args[0]);
			}

	}

}
