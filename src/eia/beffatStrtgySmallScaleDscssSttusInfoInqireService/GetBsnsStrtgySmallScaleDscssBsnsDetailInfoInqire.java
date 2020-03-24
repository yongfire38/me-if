package eia.beffatStrtgySmallScaleDscssSttusInfoInqireService;

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


public class GetBsnsStrtgySmallScaleDscssBsnsDetailInfoInqire {

	// 사전/전략/소규모협의현황 정보 서비스 - 사전/전략/소규모 협의사업 상세 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// 필수 요청 파라미터 사전환경성검토코드 1개
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty(
					"beffatStrtgySmallScaleDscssSttusInfoInqireService_getBsnsStrtgySmallScaleDscssBsnsDetailInfoInqire_url");
			String service_key = JsonParser
					.getProperty("beffatStrtgySmallScaleDscssSttusInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_54.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메시지
					pw.write("|^");
					pw.write("perCd"); // 사전환경성검토코드
					pw.write("|^");
					pw.write("bizNm"); // 사업명
					pw.write("|^");
					pw.write("ccilOrganCd"); // 협의기관
					pw.write("|^");
					pw.write("ctcMemNm"); // 담당자
					pw.write("|^");
					pw.write("ctcMemTeam"); // 담당부서
					pw.write("|^");
					pw.write("ctcMemEmail"); // E-mail
					pw.write("|^");
					pw.write("ctcMemTel"); // 전화번호
					pw.write("|^");
					pw.write("ctcMemFax"); // Fax 번호
					pw.write("|^");
					pw.write("ccilResFl"); // 협의결과

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

			StringBuffer perCd = new StringBuffer(" "); // 사전환경성검토코드
			StringBuffer bizNm = new StringBuffer(" "); // 사업명
			StringBuffer ccilOrganCd = new StringBuffer(" "); // 협의기관
			StringBuffer ctcMemNm = new StringBuffer(" "); // 담당자
			StringBuffer ctcMemTeam = new StringBuffer(" "); // 담당부서
			StringBuffer ctcMemEmail = new StringBuffer(" "); // E-mail
			StringBuffer ctcMemTel = new StringBuffer(" "); // 전화번호
			StringBuffer ctcMemFax = new StringBuffer(" "); // Fax 번호
			StringBuffer ccilResFl = new StringBuffer(" "); // 협의결과

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
				} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

					JSONObject items = (JSONObject) body.get("items");

					// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
					if (items.get("item") instanceof JSONObject) {

						JSONObject items_jsonObject = (JSONObject) items.get("item");

						Set<String> key = items_jsonObject.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite(perCd, keyname, "perCd", items_jsonObject);
							JsonParser.colWrite(bizNm, keyname, "bizNm", items_jsonObject);
							JsonParser.colWrite(ccilOrganCd, keyname, "ccilOrganCd", items_jsonObject);
							JsonParser.colWrite(ctcMemNm, keyname, "ctcMemNm", items_jsonObject);
							JsonParser.colWrite(ctcMemTeam, keyname, "ctcMemTeam", items_jsonObject);
							JsonParser.colWrite(ctcMemEmail, keyname, "ctcMemEmail", items_jsonObject);
							JsonParser.colWrite(ctcMemTel, keyname, "ctcMemTel", items_jsonObject);
							JsonParser.colWrite(ctcMemFax, keyname, "ctcMemFax", items_jsonObject);
							JsonParser.colWrite(ccilResFl, keyname, "ccilResFl", items_jsonObject);

						}

						// 한번에 문자열 합침
						resultSb.append(resultCode);
						resultSb.append("|^");
						resultSb.append(resultMsg);
						resultSb.append("|^");
						resultSb.append(perCd);
						resultSb.append("|^");
						resultSb.append(bizNm);
						resultSb.append("|^");
						resultSb.append(ccilOrganCd);
						resultSb.append("|^");
						resultSb.append(ctcMemNm);
						resultSb.append("|^");
						resultSb.append(ctcMemTeam);
						resultSb.append("|^");
						resultSb.append(ctcMemEmail);
						resultSb.append("|^");
						resultSb.append(ctcMemTel);
						resultSb.append("|^");
						resultSb.append(ctcMemFax);
						resultSb.append("|^");
						resultSb.append(ccilResFl);
						resultSb.append(System.getProperty("line.separator"));

					} else if (items.get("item") instanceof JSONArray) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(perCd, keyname, "perCd", item_obj);
								JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
								JsonParser.colWrite(ccilOrganCd, keyname, "ccilOrganCd", item_obj);
								JsonParser.colWrite(ctcMemNm, keyname, "ctcMemNm", item_obj);
								JsonParser.colWrite(ctcMemTeam, keyname, "ctcMemTeam", item_obj);
								JsonParser.colWrite(ctcMemEmail, keyname, "ctcMemEmail", item_obj);
								JsonParser.colWrite(ctcMemTel, keyname, "ctcMemTel", item_obj);
								JsonParser.colWrite(ctcMemFax, keyname, "ctcMemFax", item_obj);
								JsonParser.colWrite(ccilResFl, keyname, "ccilResFl", item_obj);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(perCd);
							resultSb.append("|^");
							resultSb.append(bizNm);
							resultSb.append("|^");
							resultSb.append(ccilOrganCd);
							resultSb.append("|^");
							resultSb.append(ctcMemNm);
							resultSb.append("|^");
							resultSb.append(ctcMemTeam);
							resultSb.append("|^");
							resultSb.append(ctcMemEmail);
							resultSb.append("|^");
							resultSb.append(ctcMemTel);
							resultSb.append("|^");
							resultSb.append(ctcMemFax);
							resultSb.append("|^");
							resultSb.append(ccilResFl);
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

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_54.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
