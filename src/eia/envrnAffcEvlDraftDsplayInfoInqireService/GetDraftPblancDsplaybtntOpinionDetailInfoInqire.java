package eia.envrnAffcEvlDraftDsplayInfoInqireService;

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


public class GetDraftPblancDsplaybtntOpinionDetailInfoInqire {

	// 환경영향평가 초안공람 정보 서비스 - 초안공람 환경영향평가 목록 상세 정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 실행시 필수 매개변수 사전환경성검토 코드
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty(
					"envrnAffcEvlDraftDsplayInfoInqireService_getDraftPblancDsplaybtntOpinionDetailInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") +
					 "EIA/TIF_EIA_40.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("resultCode"); // 결과코드
					pw.write("|^");
					pw.write("resultMsg"); // 결과메세지
					pw.write("|^");
					pw.write("eiaCd"); // 환경영향평가코드
					pw.write("|^");
					pw.write("eiaSeq"); // 환경영향평가고유번호
					pw.write("|^");
					pw.write("bizNm"); // 사업명
					pw.write("|^");
					pw.write("bizGubunNm"); // 사업구분
					pw.write("|^");
					pw.write("bizmainNm"); // 사업자명
					pw.write("|^");
					pw.write("approvOrganNm"); // 승인기관명
					pw.write("|^");
					pw.write("drfopDt"); // 초안공고일
					pw.write("|^");
					pw.write("drfopStartDt"); // 초안공람기간 시작일
					pw.write("|^");
					pw.write("drfopEndDt"); // 초안공람기간 종료일
					pw.write("|^");
					pw.write("drfopSiteTxt"); // 공람장소
					pw.write("|^");
					pw.write("drfopExpSiteTxt"); // 설명회장소
					pw.write("|^");
					pw.write("drfopExpDttmTxt"); // 설명회일시
					pw.write("|^");
					pw.write("drfopSuggStartDt"); // 초안공람 의견제출 시작일
					pw.write("|^");
					pw.write("drfopSuggEndDt"); // 초안공람 의견제출 종료일
					pw.write("|^");
					pw.write("drfopTelTxt"); // 연락처
					pw.write("|^");
					pw.write("eiaAddrTxt"); // 사업지 주소
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
			StringBuffer bizmainNm = new StringBuffer(" "); // 사업자명
			StringBuffer approvOrganNm = new StringBuffer(" "); // 승인기관명
			StringBuffer drfopDt = new StringBuffer(" "); // 초안공고일
			StringBuffer drfopStartDt = new StringBuffer(" "); // 초안공람기간 시작일
			StringBuffer drfopEndDt = new StringBuffer(" "); // 초안공람기간 종료일
			StringBuffer drfopSiteTxt = new StringBuffer(" "); // 공람장소
			StringBuffer drfopExpSiteTxt = new StringBuffer(" "); // 설명회장소
			StringBuffer drfopExpDttmTxt = new StringBuffer(" "); // 설명회일시
			StringBuffer drfopSuggStartDt = new StringBuffer(" "); // 초안공람 의견제출
																	// 시작일
			StringBuffer drfopSuggEndDt = new StringBuffer(" "); // 초안공람 의견제출
																	// 종료일
			StringBuffer drfopTelTxt = new StringBuffer(" "); // 연락처
			StringBuffer eiaAddrTxt = new StringBuffer(" "); // 사업지 주소

			try {
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				// JSONObject body = (JSONObject) response.get("body");

				JSONObject header = (JSONObject) response.get("header");

				String resultCode = header.get("resultCode").toString().trim();
				String resultMsg = header.get("resultMsg").toString().trim();

				if(!(resultCode.equals("00"))){
					System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
				} else if(response.get("body") instanceof String){
					System.out.println("data not exist!!");
				} else if (resultCode.equals("00") && !(response.get("body") instanceof String)) {

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
							JsonParser.colWrite(bizmainNm, keyname, "bizmainNm", items_jsonObject);
							JsonParser.colWrite(approvOrganNm, keyname, "approvOrganNm", items_jsonObject);
							JsonParser.colWrite(drfopDt, keyname, "drfopDt", items_jsonObject);
							JsonParser.colWrite(drfopStartDt, keyname, "drfopStartDt", items_jsonObject);
							JsonParser.colWrite(drfopEndDt, keyname, "drfopEndDt", items_jsonObject);
							JsonParser.colWrite(drfopSiteTxt, keyname, "drfopSiteTxt", items_jsonObject);
							JsonParser.colWrite(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", items_jsonObject);
							JsonParser.colWrite(drfopExpDttmTxt, keyname, "drfopExpDttmTxt", items_jsonObject);
							JsonParser.colWrite(drfopSuggStartDt, keyname, "drfopSuggStartDt", items_jsonObject);
							JsonParser.colWrite(drfopSuggEndDt, keyname, "drfopSuggEndDt", items_jsonObject);
							JsonParser.colWrite(drfopTelTxt, keyname, "drfopTelTxt", items_jsonObject);
							JsonParser.colWrite(eiaAddrTxt, keyname, "eiaAddrTxt", items_jsonObject);

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
						resultSb.append(bizmainNm);
						resultSb.append("|^");
						resultSb.append(approvOrganNm);
						resultSb.append("|^");
						resultSb.append(drfopDt);
						resultSb.append("|^");
						resultSb.append(drfopStartDt);
						resultSb.append("|^");
						resultSb.append(drfopEndDt);
						resultSb.append("|^");
						resultSb.append(drfopSiteTxt);
						resultSb.append("|^");
						resultSb.append(drfopExpSiteTxt);
						resultSb.append("|^");
						resultSb.append(drfopExpDttmTxt);
						resultSb.append("|^");
						resultSb.append(drfopSuggStartDt);
						resultSb.append("|^");
						resultSb.append(drfopSuggEndDt);
						resultSb.append("|^");
						resultSb.append(drfopTelTxt);
						resultSb.append("|^");
						resultSb.append(eiaAddrTxt);
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
								JsonParser.colWrite(bizmainNm, keyname, "bizmainNm", item_obj);
								JsonParser.colWrite(approvOrganNm, keyname, "approvOrganNm", item_obj);
								JsonParser.colWrite(drfopDt, keyname, "drfopDt", item_obj);
								JsonParser.colWrite(drfopStartDt, keyname, "drfopStartDt", item_obj);
								JsonParser.colWrite(drfopEndDt, keyname, "drfopEndDt", item_obj);
								JsonParser.colWrite(drfopSiteTxt, keyname, "drfopSiteTxt", item_obj);
								JsonParser.colWrite(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", item_obj);
								JsonParser.colWrite(drfopExpDttmTxt, keyname, "drfopExpDttmTxt", item_obj);
								JsonParser.colWrite(drfopSuggStartDt, keyname, "drfopSuggStartDt", item_obj);
								JsonParser.colWrite(drfopSuggEndDt, keyname, "drfopSuggEndDt", item_obj);
								JsonParser.colWrite(drfopTelTxt, keyname, "drfopTelTxt", item_obj);
								JsonParser.colWrite(eiaAddrTxt, keyname, "eiaAddrTxt", item_obj);

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
							resultSb.append(bizmainNm);
							resultSb.append("|^");
							resultSb.append(approvOrganNm);
							resultSb.append("|^");
							resultSb.append(drfopDt);
							resultSb.append("|^");
							resultSb.append(drfopStartDt);
							resultSb.append("|^");
							resultSb.append(drfopEndDt);
							resultSb.append("|^");
							resultSb.append(drfopSiteTxt);
							resultSb.append("|^");
							resultSb.append(drfopExpSiteTxt);
							resultSb.append("|^");
							resultSb.append(drfopExpDttmTxt);
							resultSb.append("|^");
							resultSb.append(drfopSuggStartDt);
							resultSb.append("|^");
							resultSb.append(drfopSuggEndDt);
							resultSb.append("|^");
							resultSb.append(drfopTelTxt);
							resultSb.append("|^");
							resultSb.append(eiaAddrTxt);
							resultSb.append(System.getProperty("line.separator"));
						}

					} else {
						System.out.println("parsing error!!");
					}

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

			 //TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_40.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
