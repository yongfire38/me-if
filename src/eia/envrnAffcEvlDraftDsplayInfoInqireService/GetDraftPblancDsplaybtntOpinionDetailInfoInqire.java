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


			try {

				

				// 실행시 필수 매개변수 사전환경성검토 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDraftDsplayInfoInqireService_getDraftPblancDsplaybtntOpinionDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_40.dat");

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";
					}

					// step 2. 전체 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					// JSONObject body = (JSONObject) response.get("body");

					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (response.get("body") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(response.get("body") instanceof String)) {

						JSONObject body = (JSONObject) response.get("body");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (body.get("item") instanceof JSONObject) {
							
							String eiaCd = " "; // 환경영향평가코드
							String eiaSeq = " "; // 환경영향평가고유번호
							String bizNm = " "; // 사업명
							String bizGubunNm = " "; // 사업구분
							String bizmainNm = " "; // 사업자명
							String approvOrganNm = " "; // 승인기관명
							String drfopDt = " "; // 초안공고일
							String drfopStartDt = " "; // 초안공람기간
																				// 시작일
							String drfopEndDt = " "; // 초안공람기간
																				// 종료일
							String drfopSiteTxt = " "; // 공람장소
							String drfopExpSiteTxt = " "; // 설명회장소
							String drfopExpDttmTxt = " "; // 설명회일시
							String drfopSuggStartDt = " "; // 초안공람
																					// 의견제출
																					// 시작일
							String drfopSuggEndDt =" "; // 초안공람
																					// 의견제출
																					// 종료일
							String drfopTelTxt = " "; // 연락처
							String eiaAddrTxt = " "; // 사업지 주소

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								eiaCd = JsonParser.colWrite_String(eiaCd, keyname, "eiaCd", items_jsonObject);
								eiaSeq = JsonParser.colWrite_String(eiaSeq, keyname, "eiaSeq", items_jsonObject);
								bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
								bizGubunNm = JsonParser.colWrite_String(bizGubunNm, keyname, "bizGubunNm", items_jsonObject);
								bizmainNm = JsonParser.colWrite_String(bizmainNm, keyname, "bizmainNm", items_jsonObject);
								approvOrganNm = JsonParser.colWrite_String(approvOrganNm, keyname, "approvOrganNm", items_jsonObject);
								drfopDt = JsonParser.colWrite_String(drfopDt, keyname, "drfopDt", items_jsonObject);
								drfopStartDt = JsonParser.colWrite_String(drfopStartDt, keyname, "drfopStartDt", items_jsonObject);
								drfopEndDt = JsonParser.colWrite_String(drfopEndDt, keyname, "drfopEndDt", items_jsonObject);
								drfopSiteTxt = JsonParser.colWrite_String(drfopSiteTxt, keyname, "drfopSiteTxt", items_jsonObject);
								drfopExpSiteTxt = JsonParser.colWrite_String(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", items_jsonObject);
								drfopExpDttmTxt = JsonParser.colWrite_String(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", items_jsonObject);
								drfopSuggStartDt = JsonParser.colWrite_String(drfopSuggStartDt, keyname, "drfopSuggStartDt", items_jsonObject);
								drfopSuggEndDt = JsonParser.colWrite_String(drfopSuggEndDt, keyname, "drfopSuggEndDt", items_jsonObject);
								drfopTelTxt = JsonParser.colWrite_String(drfopTelTxt, keyname, "drfopTelTxt", items_jsonObject);
								eiaAddrTxt = JsonParser.colWrite_String(eiaAddrTxt, keyname, "eiaAddrTxt", items_jsonObject);

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(resultCode); 
								pw.write("|^");
								pw.write(resultMsg); 
								pw.write("|^");
								pw.write(eiaCd); 
								pw.write("|^");
								pw.write(eiaSeq); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(bizGubunNm); 
								pw.write("|^");
								pw.write(bizmainNm); 
								pw.write("|^");
								pw.write(approvOrganNm); 
								pw.write("|^");
								pw.write(drfopDt); 
								pw.write("|^");
								pw.write(drfopStartDt); 
								pw.write("|^");
								pw.write(drfopEndDt); 
								pw.write("|^");
								pw.write(drfopSiteTxt); 
								pw.write("|^");
								pw.write(drfopExpSiteTxt); 
								pw.write("|^");
								pw.write(drfopExpDttmTxt); 
								pw.write("|^");
								pw.write(drfopSuggStartDt); 
								pw.write("|^");
								pw.write(drfopSuggEndDt); 
								pw.write("|^");
								pw.write(drfopTelTxt); 
								pw.write("|^");
								pw.write(eiaAddrTxt); 
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}			

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {
								
								String eiaCd = " "; // 환경영향평가코드
								String eiaSeq = " "; // 환경영향평가고유번호
								String bizNm = " "; // 사업명
								String bizGubunNm = " "; // 사업구분
								String bizmainNm = " "; // 사업자명
								String approvOrganNm = " "; // 승인기관명
								String drfopDt = " "; // 초안공고일
								String drfopStartDt = " "; // 초안공람기간
																					// 시작일
								String drfopEndDt = " "; // 초안공람기간
																					// 종료일
								String drfopSiteTxt = " "; // 공람장소
								String drfopExpSiteTxt = " "; // 설명회장소
								String drfopExpDttmTxt = " "; // 설명회일시
								String drfopSuggStartDt = " "; // 초안공람
																						// 의견제출
																						// 시작일
								String drfopSuggEndDt =" "; // 초안공람
																						// 의견제출
																						// 종료일
								String drfopTelTxt = " "; // 연락처
								String eiaAddrTxt = " "; // 사업지 주소

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									eiaCd = JsonParser.colWrite_String(eiaCd, keyname, "eiaCd", item_obj);
									eiaSeq = JsonParser.colWrite_String(eiaSeq, keyname, "eiaSeq", item_obj);
									bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
									bizGubunNm = JsonParser.colWrite_String(bizGubunNm, keyname, "bizGubunNm", item_obj);
									bizmainNm = JsonParser.colWrite_String(bizmainNm, keyname, "bizmainNm", item_obj);
									approvOrganNm = JsonParser.colWrite_String(approvOrganNm, keyname, "approvOrganNm", item_obj);
									drfopDt = JsonParser.colWrite_String(drfopDt, keyname, "drfopDt", item_obj);
									drfopStartDt = JsonParser.colWrite_String(drfopStartDt, keyname, "drfopStartDt", item_obj);
									drfopEndDt = JsonParser.colWrite_String(drfopEndDt, keyname, "drfopEndDt", item_obj);
									drfopSiteTxt = JsonParser.colWrite_String(drfopSiteTxt, keyname, "drfopSiteTxt", item_obj);
									drfopExpSiteTxt = JsonParser.colWrite_String(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", item_obj);
									drfopExpDttmTxt = JsonParser.colWrite_String(drfopExpSiteTxt, keyname, "drfopExpSiteTxt", item_obj);
									drfopSuggStartDt = JsonParser.colWrite_String(drfopSuggStartDt, keyname, "drfopSuggStartDt", item_obj);
									drfopSuggEndDt = JsonParser.colWrite_String(drfopSuggEndDt, keyname, "drfopSuggEndDt", item_obj);
									drfopTelTxt = JsonParser.colWrite_String(drfopTelTxt, keyname, "drfopTelTxt", item_obj);
									eiaAddrTxt = JsonParser.colWrite_String(eiaAddrTxt, keyname, "eiaAddrTxt", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(eiaCd); 
									pw.write("|^");
									pw.write(eiaSeq); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(bizGubunNm); 
									pw.write("|^");
									pw.write(bizmainNm); 
									pw.write("|^");
									pw.write(approvOrganNm); 
									pw.write("|^");
									pw.write(drfopDt); 
									pw.write("|^");
									pw.write(drfopStartDt); 
									pw.write("|^");
									pw.write(drfopEndDt); 
									pw.write("|^");
									pw.write(drfopSiteTxt); 
									pw.write("|^");
									pw.write(drfopExpSiteTxt); 
									pw.write("|^");
									pw.write(drfopExpDttmTxt); 
									pw.write("|^");
									pw.write(drfopSuggStartDt); 
									pw.write("|^");
									pw.write(drfopSuggEndDt); 
									pw.write("|^");
									pw.write(drfopTelTxt); 
									pw.write("|^");
									pw.write(eiaAddrTxt); 
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			
							}

						} else {
							System.out.println("parsing error!!");
						}

					} else {
						System.out.println("parsing error!!");
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

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("eiaCd :" + args[0]);
			}


	}

}
