package eia.envrnAffcEvlDscssSttusInfoInqireService;

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


public class GetDscssSttusDscssOpinionDetailInfoInqire {

	@SuppressWarnings("unchecked")
	// 환경영향평가 협의현황 서비스 - 협의현황 상세 정보를 조회
	public static void main(String[] args) throws Exception {

		// 실행시 필수 매개변수 환경영향평가 코드
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty(
					"envrnAffcEvlDscssSttusInfoInqireService_getDscssSttusDscssOpinionDetailInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_42.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("eiaCd"); // 환경영향평가코드
					pw.write("|^");
					pw.write("eiaSeq"); // 환경영향평가고유번호
					pw.write("|^");
					pw.write("bizNm"); // 사업명
					pw.write("|^");
					pw.write("bizGubunNm"); // 사업구분
					pw.write("|^");
					pw.write("ccilOrganNm"); // 협의기관
					pw.write("|^");
					pw.write("ccilMemNm"); // 담당자
					pw.write("|^");
					pw.write("ccilMemEmail"); // 연락처
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			String json = "";

			json = JsonParser.parseEiaJson(service_url, service_key, args[0]);

			// step 2. 전체 파싱

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer eiaCd = new StringBuffer(" "); // 환경영향평가코드
			StringBuffer eiaSeq = new StringBuffer(" "); // 환경영향평가고유번호
			StringBuffer bizNm = new StringBuffer(" "); // 사업명
			StringBuffer bizGubunNm = new StringBuffer(" "); // 사업구분
			StringBuffer ccilOrganNm = new StringBuffer(" "); // 협의기관
			StringBuffer ccilMemNm = new StringBuffer(" "); // 담당자
			StringBuffer ccilMemEmail = new StringBuffer(" "); // 연락처

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject header = (JSONObject) response.get("header");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if (response.get("body") instanceof String) {
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00")) {

					JSONObject body = (JSONObject) response.get("body");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (body.get("item") instanceof JSONObject) {

						JSONObject items_jsonObject = (JSONObject) body.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite(eiaCd, keyname, "eiaCd", items_jsonObject);
							JsonParser.colWrite(eiaSeq, keyname, "eiaSeq", items_jsonObject);
							JsonParser.colWrite(bizNm, keyname, "bizNm", items_jsonObject);
							JsonParser.colWrite(bizGubunNm, keyname, "bizGubunNm", items_jsonObject);
							JsonParser.colWrite(ccilOrganNm, keyname, "ccilOrganNm", items_jsonObject);
							JsonParser.colWrite(ccilMemNm, keyname, "ccilMemNm", items_jsonObject);
							JsonParser.colWrite(ccilMemEmail, keyname, "ccilMemEmail", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(eiaCd);
						resultSb.append("|^");
						resultSb.append(eiaSeq);
						resultSb.append("|^");
						resultSb.append(bizNm);
						resultSb.append("|^");
						resultSb.append(bizGubunNm);
						resultSb.append("|^");
						resultSb.append(ccilOrganNm);
						resultSb.append("|^");
						resultSb.append(ccilMemNm);
						resultSb.append("|^");
						resultSb.append(ccilMemEmail);
						resultSb.append(System.getProperty("line.separator"));

					} else if (body.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) body.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(eiaCd, keyname, "eiaCd", item_obj);
								JsonParser.colWrite(eiaSeq, keyname, "eiaSeq", item_obj);
								JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
								JsonParser.colWrite(bizGubunNm, keyname, "bizGubunNm", item_obj);
								JsonParser.colWrite(ccilOrganNm, keyname, "ccilOrganNm", item_obj);
								JsonParser.colWrite(ccilMemNm, keyname, "ccilMemNm", item_obj);
								JsonParser.colWrite(ccilMemEmail, keyname, "ccilMemEmail", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(eiaCd);
							resultSb.append("|^");
							resultSb.append(eiaSeq);
							resultSb.append("|^");
							resultSb.append(bizNm);
							resultSb.append("|^");
							resultSb.append(bizGubunNm);
							resultSb.append("|^");
							resultSb.append(ccilOrganNm);
							resultSb.append("|^");
							resultSb.append(ccilMemNm);
							resultSb.append("|^");
							resultSb.append(ccilMemEmail);
							resultSb.append(System.getProperty("line.separator"));

						}

					} else {
						System.out.println("parsing error!!");
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

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_42.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
