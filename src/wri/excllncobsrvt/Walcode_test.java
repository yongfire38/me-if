package wri.excllncobsrvt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;

public class Walcode_test {

	// 우량 관측정보 조회 서비스 - 수위관측소 코드 조회
	// 수문제원현황정보 - 댐 코드 전체를 받아 돌리면서 처리
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		System.out.println("firstLine start..");
		long start = System.currentTimeMillis(); // 시작시간

		// step 0.open api url과 서비스 키.
		String service_url = JsonParser.getProperty("excllncobsrvt_walcode_url");
		String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

		// step 1.파일의 작성
		File file = new File("TIF_WRI_17.dat");

		try {
			
			PrintWriter pw = new PrintWriter(
					new BufferedWriter(new FileWriter(file, true)));

			pw.flush();
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//댘코드 전체 파싱해서 맵에 넣기
		
		String damcode_json = "";

		String damcode_service_url = JsonParser.getProperty("dataPresent_damcode_url");
		String damcode_service_key = JsonParser.getProperty("dataPresent_service_key");
		damcode_json = JsonParser.parseWriJson(damcode_service_url, damcode_service_key);

		
		HashMap<String, String> damcodeMap = new HashMap<String, String>();
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(damcode_json);
			JSONObject response = (JSONObject) obj.get("response");

			JSONObject body = (JSONObject) response.get("body");
			JSONObject header = (JSONObject) response.get("header");
			JSONObject items = (JSONObject) body.get("items");

			String resultCode = header.get("resultCode").toString().trim();
			String resultMsg = header.get("resultMsg").toString().trim();
			
			if(!(resultCode.equals("00"))){
				System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
			} else if (resultCode.equals("00")) {
				
				

				JSONArray items_jsonArray = (JSONArray) items.get("item");

				for (int r = 0; r < items_jsonArray.size(); r++) {

					JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

					Set<String> key = item_obj.keySet();

					Iterator<String> iter = key.iterator();

					String damcode = " "; // 댐코드
					String damnm = " "; // 댐이름

					while (iter.hasNext()) {

						String keyname = iter.next();

						if (keyname.equals("damcode")) {
							damcode = item_obj.get(keyname).toString().trim();
						}
						if (keyname.equals("damnm")) {
							damnm = item_obj.get(keyname).toString().trim();
						}

					}

					damcodeMap.put(damcode, damnm);	

				}

			} else {
				System.out.println("parsing error!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String json = "";

		StringBuffer resultSb = new StringBuffer("");

		StringBuffer walobsrvtcode = new StringBuffer(" "); // 수위 관측소코드
		StringBuffer obsrvtNm = new StringBuffer(" "); // 관측소이름
		
		Set<String> k = damcodeMap.keySet();
		Iterator<String> itr = k.iterator();
		
		while(itr.hasNext()){
			
			// 파라미터 1개만 받으므로 환경영향평가 쪽 메서드 이용
			json = JsonParser.parseEiaJson(service_url, service_key, itr.next().toString());
			
			
			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject header = (JSONObject) response.get("header");
				

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if(!(resultCode.equals("00"))){
					System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
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

							JsonParser.colWrite(walobsrvtcode, keyname, "walobsrvtcode", items_jsonObject);
							JsonParser.colWrite(obsrvtNm, keyname, "obsrvtNm", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(args[0]);
						resultSb.append("|^");
						resultSb.append(walobsrvtcode);
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

								JsonParser.colWrite(walobsrvtcode, keyname, "walobsrvtcode", item_obj);
								JsonParser.colWrite(obsrvtNm, keyname, "obsrvtNm", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(itr.next().toString());
							resultSb.append("|^");
							resultSb.append(walobsrvtcode);
							resultSb.append("|^");
							resultSb.append(obsrvtNm);
							resultSb.append(System.getProperty("line.separator"));

					}

					}

				} else {
					System.out.println("parsing error!!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			
			Thread.sleep(2500);
		}
		
		System.out.println("parsing complete!");

		long end = System.currentTimeMillis();
		System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

	}

}
