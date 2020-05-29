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

import common.JsonParser;
//import common.TransSftp;

public class GetDscssSttusDscssOpinionDetailInfoInqire {

	@SuppressWarnings("unchecked")
	// 환경영향평가 협의현황 서비스 - 협의현황 상세 정보를 조회
	public static void main(String[] args) throws Exception {

			try {

				

				// 실행시 필수 매개변수 환경영향평가 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDscssSttusInfoInqireService_getDscssSttusDscssOpinionDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_42.dat");
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";
					}*/

					// step 2. 전체 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, args[0]);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						throw new Exception();
					} else if ((resultCode.equals("00") && response.get("body") instanceof String)||(resultCode.equals("03"))) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(response.get("body") instanceof String)) {

						JSONObject body = (JSONObject) response.get("body");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (body.get("item") instanceof JSONObject) {
							
							String eiaCd = " "; // 환경영향평가코드
							String eiaSeq = " "; // 환경영향평가고유번호
							String bizNm = " "; // 사업명
							String bizGubunNm = " "; // 사업구분
							String ccilOrganNm = " "; // 협의기관
							String ccilMemNm = " "; // 담당자
							String ccilMemEmail = " "; // 연락처

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								eiaCd = JsonParser.colWrite_String(eiaCd, keyname, "eiaCd", items_jsonObject);
								eiaSeq = JsonParser.colWrite_String(eiaSeq, keyname, "eiaSeq", items_jsonObject);
								bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
								bizGubunNm = JsonParser.colWrite_String(bizGubunNm, keyname, "bizGubunNm", items_jsonObject);
								ccilOrganNm = JsonParser.colWrite_String(ccilOrganNm, keyname, "ccilOrganNm", items_jsonObject);
								ccilMemNm = JsonParser.colWrite_String(ccilMemNm, keyname, "ccilMemNm", items_jsonObject);
								ccilMemEmail = JsonParser.colWrite_String(ccilMemEmail, keyname, "ccilMemEmail", items_jsonObject);

							}
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(eiaCd); 
								pw.write("|^");
								pw.write(eiaSeq); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(bizGubunNm); 
								pw.write("|^");
								pw.write(ccilOrganNm); 
								pw.write("|^");
								pw.write(ccilMemNm); 
								pw.write("|^");
								pw.write(ccilMemEmail);  
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
								String ccilOrganNm = " "; // 협의기관
								String ccilMemNm = " "; // 담당자
								String ccilMemEmail = " "; // 연락처

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									eiaCd = JsonParser.colWrite_String(eiaCd, keyname, "eiaCd", item_obj);
									eiaSeq = JsonParser.colWrite_String(eiaSeq, keyname, "eiaSeq", item_obj);
									bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
									bizGubunNm = JsonParser.colWrite_String(bizGubunNm, keyname, "bizGubunNm", item_obj);
									ccilOrganNm = JsonParser.colWrite_String(ccilOrganNm, keyname, "ccilOrganNm", item_obj);
									ccilMemNm = JsonParser.colWrite_String(ccilMemNm, keyname, "ccilMemNm", item_obj);
									ccilMemEmail = JsonParser.colWrite_String(ccilMemEmail, keyname, "ccilMemEmail", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(eiaCd); 
									pw.write("|^");
									pw.write(eiaSeq); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(bizGubunNm); 
									pw.write("|^");
									pw.write(ccilOrganNm); 
									pw.write("|^");
									pw.write(ccilMemNm); 
									pw.write("|^");
									pw.write(ccilMemEmail);  
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_42.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("eiaCd :" + args[0]);
				System.exit(-1);
			}


	}

}
