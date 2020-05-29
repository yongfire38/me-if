package eia.envrnAffcEvlDecsnCnInfoInqireService;

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

public class GetDecsnCnIngbtntOpinionDetailInfoInqire {

	// 환경영향평가 결정내용정보 서비스 - 결정내용 상세 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 실행시 필수 결정내용 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDecsnCnInfoInqireService_getDecsnCnIngbtntOpinionDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDecsnCnInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_46.dat");
					
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
							
							String resultCd = " "; // 결정내용코드
							String bizNm = " "; // 사업명
							String approvOrganTeam = " "; // 승인기관
							String openPclDt = " "; // 공고일
							String openTmdtStartDt = " "; // 공람기간시작일
							String openTmdtEndDt = " "; // 공람기간종료일
							String openOpnEndDt = " "; // 의견종료일
							String openOpnStartDt = " "; // 의견시작일
							String openOpnEtc = " "; // 결정내용
							String openTeamNm = " "; // 부서명
							String bizManTxt = " "; // 사업자
							String discOrganNm = " "; // 협의기관

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								resultCd = JsonParser.colWrite_String(resultCd, keyname, "resultCd", items_jsonObject);
								bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
								approvOrganTeam = JsonParser.colWrite_String(approvOrganTeam, keyname, "approvOrganTeam", items_jsonObject);
								openPclDt = JsonParser.colWrite_String(openPclDt, keyname, "openPclDt", items_jsonObject);
								openTmdtStartDt = JsonParser.colWrite_String(openTmdtStartDt, keyname, "openTmdtStartDt", items_jsonObject);
								openTmdtEndDt = JsonParser.colWrite_String(openTmdtEndDt, keyname, "openTmdtEndDt", items_jsonObject);
								openOpnEndDt = JsonParser.colWrite_String(openOpnEndDt, keyname, "openOpnEndDt", items_jsonObject);
								openOpnStartDt = JsonParser.colWrite_String(openOpnStartDt, keyname, "openOpnStartDt", items_jsonObject);
								openOpnEtc = JsonParser.colWrite_String(openOpnEtc, keyname, "openOpnEtc", items_jsonObject);
								openTeamNm = JsonParser.colWrite_String(openTeamNm, keyname, "openTeamNm", items_jsonObject);
								bizManTxt = JsonParser.colWrite_String(bizManTxt, keyname, "bizManTxt", items_jsonObject);
								discOrganNm = JsonParser.colWrite_String(discOrganNm, keyname, "discOrganNm", items_jsonObject);

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(resultCode); 
								pw.write("|^");
								pw.write(resultMsg); 
								pw.write("|^");
								pw.write(resultCd); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(approvOrganTeam); 
								pw.write("|^");
								pw.write(openPclDt); 
								pw.write("|^");
								pw.write(openTmdtStartDt); 
								pw.write("|^");
								pw.write(openTmdtEndDt); 
								pw.write("|^");
								pw.write(openOpnEndDt); 
								pw.write("|^");
								pw.write(openOpnStartDt); 
								pw.write("|^");
								pw.write(openOpnEtc); 
								pw.write("|^");
								pw.write(openTeamNm); 
								pw.write("|^");
								pw.write(bizManTxt); 
								pw.write("|^");
								pw.write(discOrganNm);
								pw.println();
								pw.flush();
								pw.close();
								
							} catch (IOException e) {
								e.printStackTrace();
							}

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {
								
								String resultCd = " "; // 결정내용코드
								String bizNm = " "; // 사업명
								String approvOrganTeam = " "; // 승인기관
								String openPclDt = " "; // 공고일
								String openTmdtStartDt = " "; // 공람기간시작일
								String openTmdtEndDt = " "; // 공람기간종료일
								String openOpnEndDt = " "; // 의견종료일
								String openOpnStartDt = " "; // 의견시작일
								String openOpnEtc = " "; // 결정내용
								String openTeamNm = " "; // 부서명
								String bizManTxt = " "; // 사업자
								String discOrganNm = " "; // 협의기관

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									resultCd = JsonParser.colWrite_String(resultCd, keyname, "resultCd", item_obj);
									bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
									approvOrganTeam = JsonParser.colWrite_String(approvOrganTeam, keyname, "approvOrganTeam", item_obj);
									openPclDt = JsonParser.colWrite_String(openPclDt, keyname, "openPclDt", item_obj);
									openTmdtStartDt = JsonParser.colWrite_String(openTmdtStartDt, keyname, "openTmdtStartDt", item_obj);
									openTmdtEndDt = JsonParser.colWrite_String(openTmdtEndDt, keyname, "openTmdtEndDt", item_obj);
									openOpnEndDt = JsonParser.colWrite_String(openOpnEndDt, keyname, "openOpnEndDt", item_obj);
									openOpnStartDt = JsonParser.colWrite_String(openOpnStartDt, keyname, "openOpnStartDt", item_obj);
									openOpnEtc = JsonParser.colWrite_String(openOpnEtc, keyname, "openOpnEtc", item_obj);
									openTeamNm = JsonParser.colWrite_String(openTeamNm, keyname, "openTeamNm", item_obj);
									bizManTxt = JsonParser.colWrite_String(bizManTxt, keyname, "bizManTxt", item_obj);
									discOrganNm = JsonParser.colWrite_String(discOrganNm, keyname, "discOrganNm", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(resultCd); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(approvOrganTeam); 
									pw.write("|^");
									pw.write(openPclDt); 
									pw.write("|^");
									pw.write(openTmdtStartDt); 
									pw.write("|^");
									pw.write(openTmdtEndDt); 
									pw.write("|^");
									pw.write(openOpnEndDt); 
									pw.write("|^");
									pw.write(openOpnStartDt); 
									pw.write("|^");
									pw.write(openOpnEtc); 
									pw.write("|^");
									pw.write(openTeamNm); 
									pw.write("|^");
									pw.write(bizManTxt); 
									pw.write("|^");
									pw.write(discOrganNm);
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_46.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("resultCd :" + args[0]);
			}


	}

}
