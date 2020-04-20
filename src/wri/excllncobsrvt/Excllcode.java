package wri.excllncobsrvt;

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

public class Excllcode {

	// 우량 관측정보 조회 서비스 - 우량관측소 코드 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 댐 코드
				// 댐코드는 수문제원현황 코드조회 api에서 조회
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("excllncobsrvt_excllcode_url");
					String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat");

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

					StringBuffer excllncobsrvtcode = new StringBuffer(" "); // 우량
																			// 관측소코드
					StringBuffer obsrvtNm = new StringBuffer(" "); // 관측소이름

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

								JsonParser.colWrite(excllncobsrvtcode, keyname, "excllncobsrvtcode", items_jsonObject);
								JsonParser.colWrite(obsrvtNm, keyname, "obsrvtNm", items_jsonObject);

							}

							// 한번에 문자열 합침
							resultSb.append(args[0]);
							resultSb.append("|^");
							resultSb.append(excllncobsrvtcode);
							resultSb.append("|^");
							resultSb.append(obsrvtNm);
							resultSb.append(System.getProperty("line.separator"));

						} else if (items.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(excllncobsrvtcode, keyname, "excllncobsrvtcode", item_obj);
									JsonParser.colWrite(obsrvtNm, keyname, "obsrvtNm", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(args[0]);
								resultSb.append("|^");
								resultSb.append(excllncobsrvtcode);
								resultSb.append("|^");
								resultSb.append(obsrvtNm);
								resultSb.append(System.getProperty("line.separator"));

							}

						}

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!!");
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat", "WRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("damcode :" + args[0]);
			}



	}

}
