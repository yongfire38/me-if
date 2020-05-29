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

import common.JsonParser;
//import common.TransSftp;

public class GetStrategyDraftPblancDsplaybtntOpinionDetailInfoInqire {

	// 환경영향평가 초안공람 정보 서비스 - 초안공람 전략영향평가 상세 정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 실행시 필수 매개변수 사전환경성검토 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDraftDsplayInfoInqireService_getStrategyDraftPblancDsplaybtntOpinionDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_38.dat");
					
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

					if (!(resultCode.equals("00"))) {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (response.get("body") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(response.get("body") instanceof String)) {

						JSONObject body = (JSONObject) response.get("body");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (body.get("item") instanceof JSONObject) {
							
							String perCd = " "; // 사전환경성검토 코드
							String bizNm = " "; // 사업명
							String approvOrganTeam = " "; // 승인기관
							String bizMoney = " "; // 사업비
							String bizSize = " "; // 사업규모
							String bizSizeDan = " "; // 사업규모 단위
							String benfBizmain = " "; // 사업시행자
							String embodEtcNm = " "; // 사업구분기타명칭
							String ccilJongCd = " "; // 협의종류
							String embodCd = " "; // 사업구분코드
							String embodCd2 = " "; // 사업구분 기타 명칭
							String ccilOrganCd = " "; // 협의기관코드
							String ctcMemNm = " "; // 협의기관 담당자
							String ctcMemTeam = " "; // 협의기관
																				// 담당부서
							String ctcMemEmail = " "; // 협의기관
																				// E-mail
							String ctcMemTel = " "; // 협의기관 전화번호
							String ctcMemFax = " "; // 협의기관
																			// Fax번호
							String bizAddrEtc = " "; // 소재지 주소1
							String bizAddrEtc2 = " "; // 소재지 주소2
							String drfopPclDt = " "; // 초안 공고일
							String drfopTmdtStartDt = " "; // 초안
																					// 공람기간
																					// 시작일
							String drfopTmdtEndDt = " "; // 초안공람기간
																					// 종료일
							String drfopExpDttm = " "; // 설명회 일시
							String drfopOpnStartDt = " "; // 의견제출기간
																					// 시작일
							String drfopOpnEndDt = " "; // 의견제출기간
																				// 종료일
							String drfopSite = " "; // 공람장소
							String drfopExpSite = " "; // 설명회 장소
							String drfopTeamNm = " "; // 부서명
							String drfopTel = " "; // 전화번호

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								perCd = JsonParser.colWrite_String(perCd, keyname, "perCd", items_jsonObject);
								bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
								approvOrganTeam = JsonParser.colWrite_String(approvOrganTeam, keyname, "approvOrganTeam", items_jsonObject);
								bizMoney = JsonParser.colWrite_String(bizMoney, keyname, "bizMoney", items_jsonObject);
								bizSize = JsonParser.colWrite_String(bizSize, keyname, "bizSize", items_jsonObject);
								bizSizeDan = JsonParser.colWrite_String(bizSizeDan, keyname, "bizSizeDan", items_jsonObject);
								benfBizmain = JsonParser.colWrite_String(benfBizmain, keyname, "benfBizmain", items_jsonObject);
								embodEtcNm = JsonParser.colWrite_String(embodEtcNm, keyname, "embodEtcNm", items_jsonObject);
								ccilJongCd = JsonParser.colWrite_String(ccilJongCd, keyname, "ccilJongCd", items_jsonObject);
								embodCd = JsonParser.colWrite_String(embodCd, keyname, "embodCd", items_jsonObject);
								embodCd2 = JsonParser.colWrite_String(embodCd2, keyname, "embodCd2", items_jsonObject);
								ccilOrganCd = JsonParser.colWrite_String(ccilOrganCd, keyname, "ccilOrganCd", items_jsonObject);
								ctcMemNm = JsonParser.colWrite_String(ctcMemNm, keyname, "ctcMemNm", items_jsonObject);
								ctcMemTeam = JsonParser.colWrite_String(ctcMemTeam, keyname, "ctcMemTeam", items_jsonObject);
								ctcMemEmail = JsonParser.colWrite_String(ctcMemEmail, keyname, "ctcMemEmail", items_jsonObject);
								ctcMemTel = JsonParser.colWrite_String(ctcMemTel, keyname, "ctcMemTel", items_jsonObject);
								ctcMemFax = JsonParser.colWrite_String(ctcMemFax, keyname, "ctcMemFax", items_jsonObject);
								bizAddrEtc = JsonParser.colWrite_String(bizAddrEtc, keyname, "bizAddrEtc", items_jsonObject);
								bizAddrEtc2 = JsonParser.colWrite_String(bizAddrEtc2, keyname, "bizAddrEtc2", items_jsonObject);
								drfopPclDt = JsonParser.colWrite_String(drfopPclDt, keyname, "drfopPclDt", items_jsonObject);
								drfopTmdtStartDt = JsonParser.colWrite_String(drfopTmdtStartDt, keyname, "drfopTmdtStartDt", items_jsonObject);
								drfopTmdtEndDt = JsonParser.colWrite_String(drfopTmdtEndDt, keyname, "drfopTmdtEndDt", items_jsonObject);
								drfopExpDttm = JsonParser.colWrite_String(drfopExpDttm, keyname, "drfopExpDttm", items_jsonObject);
								drfopOpnStartDt = JsonParser.colWrite_String(drfopOpnStartDt, keyname, "drfopOpnStartDt", items_jsonObject);
								drfopOpnEndDt = JsonParser.colWrite_String(drfopOpnEndDt, keyname, "drfopOpnEndDt", items_jsonObject);
								drfopSite = JsonParser.colWrite_String(drfopSite, keyname, "drfopSite", items_jsonObject);
								drfopExpSite = JsonParser.colWrite_String(drfopExpSite, keyname, "drfopExpSite", items_jsonObject);
								drfopTeamNm = JsonParser.colWrite_String(drfopTeamNm, keyname, "drfopTeamNm", items_jsonObject);
								drfopTel = JsonParser.colWrite_String(drfopTel, keyname, "drfopTel", items_jsonObject);

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(perCd); 
								pw.write("|^");
								pw.write(bizNm); 
								pw.write("|^");
								pw.write(approvOrganTeam); 
								pw.write("|^");
								pw.write(bizMoney); 
								pw.write("|^");
								pw.write(bizSize); 
								pw.write("|^");
								pw.write(bizSizeDan); 
								pw.write("|^");
								pw.write(benfBizmain); 
								pw.write("|^");
								pw.write(embodEtcNm); 
								pw.write("|^");
								pw.write(ccilJongCd); 
								pw.write("|^");
								pw.write(embodCd); 
								pw.write("|^");
								pw.write(embodCd2); 
								pw.write("|^");
								pw.write(ccilOrganCd);
								pw.write("|^");
								pw.write(ctcMemNm); 
								pw.write("|^");
								pw.write(ctcMemTeam); 
								pw.write("|^");
								pw.write(ctcMemEmail); 
								pw.write("|^");
								pw.write(ctcMemTel); 
								pw.write("|^");
								pw.write(ctcMemFax); 
								pw.write("|^");
								pw.write(bizAddrEtc); 
								pw.write("|^");
								pw.write(bizAddrEtc2); 
								pw.write("|^");
								pw.write(drfopPclDt); 
								pw.write("|^");
								pw.write(drfopTmdtStartDt); 
								pw.write("|^");
								pw.write(drfopTmdtEndDt); 
								pw.write("|^");
								pw.write(drfopExpDttm); 
								pw.write("|^");
								pw.write(drfopOpnStartDt); 
								pw.write("|^");
								pw.write(drfopOpnEndDt); 
								pw.write("|^");
								pw.write(drfopSite); 
								pw.write("|^");
								pw.write(drfopExpSite); 
								pw.write("|^");
								pw.write(drfopTeamNm); 
								pw.write("|^");
								pw.write(drfopTel); 
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}			

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {
								
								String perCd = " "; // 사전환경성검토 코드
								String bizNm = " "; // 사업명
								String approvOrganTeam = " "; // 승인기관
								String bizMoney = " "; // 사업비
								String bizSize = " "; // 사업규모
								String bizSizeDan = " "; // 사업규모 단위
								String benfBizmain = " "; // 사업시행자
								String embodEtcNm = " "; // 사업구분기타명칭
								String ccilJongCd = " "; // 협의종류
								String embodCd = " "; // 사업구분코드
								String embodCd2 = " "; // 사업구분 기타 명칭
								String ccilOrganCd = " "; // 협의기관코드
								String ctcMemNm = " "; // 협의기관 담당자
								String ctcMemTeam = " "; // 협의기관
																					// 담당부서
								String ctcMemEmail = " "; // 협의기관
																					// E-mail
								String ctcMemTel = " "; // 협의기관 전화번호
								String ctcMemFax = " "; // 협의기관
																				// Fax번호
								String bizAddrEtc = " "; // 소재지 주소1
								String bizAddrEtc2 = " "; // 소재지 주소2
								String drfopPclDt = " "; // 초안 공고일
								String drfopTmdtStartDt = " "; // 초안
																						// 공람기간
																						// 시작일
								String drfopTmdtEndDt = " "; // 초안공람기간
																						// 종료일
								String drfopExpDttm = " "; // 설명회 일시
								String drfopOpnStartDt = " "; // 의견제출기간
																						// 시작일
								String drfopOpnEndDt = " "; // 의견제출기간
																					// 종료일
								String drfopSite = " "; // 공람장소
								String drfopExpSite = " "; // 설명회 장소
								String drfopTeamNm = " "; // 부서명
								String drfopTel = " "; // 전화번호

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									perCd = JsonParser.colWrite_String(perCd, keyname, "perCd", item_obj);
									bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
									approvOrganTeam = JsonParser.colWrite_String(approvOrganTeam, keyname, "approvOrganTeam", item_obj);
									bizMoney = JsonParser.colWrite_String(bizMoney, keyname, "bizMoney", item_obj);
									bizSize = JsonParser.colWrite_String(bizSize, keyname, "bizSize", item_obj);
									bizSizeDan = JsonParser.colWrite_String(bizSizeDan, keyname, "bizSizeDan", item_obj);
									benfBizmain = JsonParser.colWrite_String(benfBizmain, keyname, "benfBizmain", item_obj);
									embodEtcNm = JsonParser.colWrite_String(embodEtcNm, keyname, "embodEtcNm", item_obj);
									ccilJongCd = JsonParser.colWrite_String(ccilJongCd, keyname, "ccilJongCd", item_obj);
									embodCd = JsonParser.colWrite_String(embodCd, keyname, "embodCd", item_obj);
									embodCd2 = JsonParser.colWrite_String(embodCd2, keyname, "embodCd2", item_obj);
									ccilOrganCd = JsonParser.colWrite_String(ccilOrganCd, keyname, "ccilOrganCd", item_obj);
									ctcMemNm = JsonParser.colWrite_String(ctcMemNm, keyname, "ctcMemNm", item_obj);
									ctcMemTeam = JsonParser.colWrite_String(ctcMemTeam, keyname, "ctcMemTeam", item_obj);
									ctcMemEmail = JsonParser.colWrite_String(ctcMemEmail, keyname, "ctcMemEmail", item_obj);
									ctcMemTel = JsonParser.colWrite_String(ctcMemTel, keyname, "ctcMemTel", item_obj);
									ctcMemFax = JsonParser.colWrite_String(ctcMemFax, keyname, "ctcMemFax", item_obj);
									bizAddrEtc = JsonParser.colWrite_String(bizAddrEtc, keyname, "bizAddrEtc", item_obj);
									bizAddrEtc2 = JsonParser.colWrite_String(bizAddrEtc2, keyname, "bizAddrEtc2", item_obj);
									drfopPclDt = JsonParser.colWrite_String(drfopPclDt, keyname, "drfopPclDt", item_obj);
									drfopTmdtStartDt = JsonParser.colWrite_String(drfopTmdtStartDt, keyname, "drfopTmdtStartDt", item_obj);
									drfopTmdtEndDt = JsonParser.colWrite_String(drfopTmdtEndDt, keyname, "drfopTmdtEndDt", item_obj);
									drfopExpDttm = JsonParser.colWrite_String(drfopExpDttm, keyname, "drfopExpDttm", item_obj);
									drfopOpnStartDt = JsonParser.colWrite_String(drfopOpnStartDt, keyname, "drfopOpnStartDt", item_obj);
									drfopOpnEndDt = JsonParser.colWrite_String(drfopOpnEndDt, keyname, "drfopOpnEndDt", item_obj);
									drfopSite = JsonParser.colWrite_String(drfopSite, keyname, "drfopSite", item_obj);
									drfopExpSite = JsonParser.colWrite_String(drfopExpSite, keyname, "drfopExpSite", item_obj);
									drfopTeamNm = JsonParser.colWrite_String(drfopTeamNm, keyname, "drfopTeamNm", item_obj);
									drfopTel = JsonParser.colWrite_String(drfopTel, keyname, "drfopTel", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(perCd); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(approvOrganTeam); 
									pw.write("|^");
									pw.write(bizMoney); 
									pw.write("|^");
									pw.write(bizSize); 
									pw.write("|^");
									pw.write(bizSizeDan); 
									pw.write("|^");
									pw.write(benfBizmain); 
									pw.write("|^");
									pw.write(embodEtcNm); 
									pw.write("|^");
									pw.write(ccilJongCd); 
									pw.write("|^");
									pw.write(embodCd); 
									pw.write("|^");
									pw.write(embodCd2); 
									pw.write("|^");
									pw.write(ccilOrganCd);
									pw.write("|^");
									pw.write(ctcMemNm); 
									pw.write("|^");
									pw.write(ctcMemTeam); 
									pw.write("|^");
									pw.write(ctcMemEmail); 
									pw.write("|^");
									pw.write(ctcMemTel); 
									pw.write("|^");
									pw.write(ctcMemFax); 
									pw.write("|^");
									pw.write(bizAddrEtc); 
									pw.write("|^");
									pw.write(bizAddrEtc2); 
									pw.write("|^");
									pw.write(drfopPclDt); 
									pw.write("|^");
									pw.write(drfopTmdtStartDt); 
									pw.write("|^");
									pw.write(drfopTmdtEndDt); 
									pw.write("|^");
									pw.write(drfopExpDttm); 
									pw.write("|^");
									pw.write(drfopOpnStartDt); 
									pw.write("|^");
									pw.write(drfopOpnEndDt); 
									pw.write("|^");
									pw.write(drfopSite); 
									pw.write("|^");
									pw.write(drfopExpSite); 
									pw.write("|^");
									pw.write(drfopTeamNm); 
									pw.write("|^");
									pw.write(drfopTel); 
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_38.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("perCd :" + args[0]);
			}


	}

}
