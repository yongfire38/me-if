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
import common.TransSftp;

public class Excllcode {
	
	

	// 우량 관측정보 조회 서비스 - 우량관측소 코드 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필요한 파라미터는 댐 코드
		// 댐코드는 수문제원현황 코드조회 api에서 조회
		if (args.length == 1) {
			
			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("excllncobsrvt_excllcode_url");
			String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat");
			
			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("excllncobsrvtcode"); // 우량 관측소코드
					pw.write("|^");
					pw.write("obsrvtNm"); // 관측소이름
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			// step 2. 전체 파싱
			String json = "";

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer excllncobsrvtcode = new StringBuffer(" "); // 우량 관측소코드
			StringBuffer obsrvtNm = new StringBuffer(" "); // 관측소이름
			
			//파라미터 1개만 받으므로 환경영향평가 쪽 메서드 이용
			json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
			
			try {
				
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");
				JSONObject header = (JSONObject) response.get("header");
				

				String resultCode = header.get("resultCode").toString().trim();
				
				if (body.get("items") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
					
					JSONObject items = (JSONObject) body.get("items");
					
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
						resultSb.append(excllncobsrvtcode);
						resultSb.append("|^");
						resultSb.append(obsrvtNm);
						resultSb.append(System.getProperty("line.separator"));
						
					}
					
					
				} else if (resultCode.equals("03")) {
					System.out.println("data not exist!!");
				} else {
					System.out.println("parsing error!!");
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

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat", "WRI");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}