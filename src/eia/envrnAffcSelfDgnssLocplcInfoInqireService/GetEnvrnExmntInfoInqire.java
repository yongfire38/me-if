package eia.envrnAffcSelfDgnssLocplcInfoInqireService;

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
import common.TransSftp;

public class GetEnvrnExmntInfoInqire {

	// 자가진단 소재지 정보 서비스 - 사전환경 영향평가서 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		// 필수 파라미터는 지분의 1개
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser
					.getProperty("envrnAffcSelfDgnssLocplcInfoInqireService_getEnvrnExmntInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcSelfDgnssLocplcInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_52.dat");

			try {

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				pw.write("resultCode"); // 결과코드
				pw.write("|^");
				pw.write("resultMsg"); // 결과메시지
				pw.write("|^");
				pw.write("pnu"); // PNU
				pw.write("|^");
				pw.write("jibun"); // 지분
				pw.write("|^");
				pw.write("centerx"); // 좌표 X
				pw.write("|^");
				pw.write("centery"); // 좌표 Y
				pw.println();
				pw.flush();
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			String json = "";

			json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
			

			// step 2. 전체 파싱

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer pnu = new StringBuffer(" "); // PNU
			StringBuffer jibun = new StringBuffer(" "); // 지분
			StringBuffer centerx = new StringBuffer(" "); // 좌표 X
			StringBuffer centery = new StringBuffer(" "); // 좌표 Y

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject header = (JSONObject) response.get("header");
				JSONObject body = (JSONObject) response.get("body");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if (body.get("items") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00")) {

					JSONObject items = (JSONObject) body.get("items");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite(pnu, keyname, "pnu", items_jsonObject);
							JsonParser.colWrite(jibun, keyname, "jibun", items_jsonObject);
							JsonParser.colWrite(centerx, keyname, "centerx", items_jsonObject);
							JsonParser.colWrite(centery, keyname, "centery", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(resultCode);
						resultSb.append("|^");
						resultSb.append(resultMsg);
						resultSb.append("|^");
						resultSb.append(pnu);
						resultSb.append("|^");
						resultSb.append(jibun);
						resultSb.append("|^");
						resultSb.append(centerx);
						resultSb.append("|^");
						resultSb.append(centery);
						resultSb.append(System.getProperty("line.separator"));

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(pnu, keyname, "pnu", item_obj);
								JsonParser.colWrite(jibun, keyname, "jibun", item_obj);
								JsonParser.colWrite(centerx, keyname, "centerx", item_obj);
								JsonParser.colWrite(centery, keyname, "centery", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(pnu);
							resultSb.append("|^");
							resultSb.append(jibun);
							resultSb.append("|^");
							resultSb.append(centerx);
							resultSb.append("|^");
							resultSb.append(centery);
							resultSb.append(System.getProperty("line.separator"));
						}

					} else {
						System.out.println("parsing error!!");
					}

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

			System.out.println("parsing complete!");

			// step 5. 대상 서버에 sftp로 보냄

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_52.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
