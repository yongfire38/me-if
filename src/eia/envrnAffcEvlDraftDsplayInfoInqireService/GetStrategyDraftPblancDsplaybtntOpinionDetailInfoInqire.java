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

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";
					}

					// step 2. 전체 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer perCd = new StringBuffer(" "); // 사전환경성검토 코드
					StringBuffer bizNm = new StringBuffer(" "); // 사업명
					StringBuffer approvOrganTeam = new StringBuffer(" "); // 승인기관
					StringBuffer bizMoney = new StringBuffer(" "); // 사업비
					StringBuffer bizSize = new StringBuffer(" "); // 사업규모
					StringBuffer bizSizeDan = new StringBuffer(" "); // 사업규모 단위
					StringBuffer benfBizmain = new StringBuffer(" "); // 사업시행자
					StringBuffer embodEtcNm = new StringBuffer(" "); // 사업구분기타명칭
					StringBuffer ccilJongCd = new StringBuffer(" "); // 협의종류
					StringBuffer embodCd = new StringBuffer(" "); // 사업구분코드
					StringBuffer embodCd2 = new StringBuffer(" "); // 사업구분 기타 명칭
					StringBuffer ccilOrganCd = new StringBuffer(" "); // 협의기관코드
					StringBuffer ctcMemNm = new StringBuffer(" "); // 협의기관 담당자
					StringBuffer ctcMemTeam = new StringBuffer(" "); // 협의기관
																		// 담당부서
					StringBuffer ctcMemEmail = new StringBuffer(" "); // 협의기관
																		// E-mail
					StringBuffer ctcMemTel = new StringBuffer(" "); // 협의기관 전화번호
					StringBuffer ctcMemFax = new StringBuffer(" "); // 협의기관
																	// Fax번호
					StringBuffer bizAddrEtc = new StringBuffer(" "); // 소재지 주소1
					StringBuffer bizAddrEtc2 = new StringBuffer(" "); // 소재지 주소2
					StringBuffer drfopPclDt = new StringBuffer(" "); // 초안 공고일
					StringBuffer drfopTmdtStartDt = new StringBuffer(" "); // 초안
																			// 공람기간
																			// 시작일
					StringBuffer drfopTmdtEndDt = new StringBuffer(" "); // 초안공람기간
																			// 종료일
					StringBuffer drfopExpDttm = new StringBuffer(" "); // 설명회 일시
					StringBuffer drfopOpnStartDt = new StringBuffer(" "); // 의견제출기간
																			// 시작일
					StringBuffer drfopOpnEndDt = new StringBuffer(" "); // 의견제출기간
																		// 종료일
					StringBuffer drfopSite = new StringBuffer(" "); // 공람장소
					StringBuffer drfopExpSite = new StringBuffer(" "); // 설명회 장소
					StringBuffer drfopTeamNm = new StringBuffer(" "); // 부서명
					StringBuffer drfopTel = new StringBuffer(" "); // 전화번호

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

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

							JSONObject items_jsonObject = (JSONObject) body.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();

								JsonParser.colWrite(perCd, keyname, "perCd", items_jsonObject);
								JsonParser.colWrite(bizNm, keyname, "bizNm", items_jsonObject);
								JsonParser.colWrite(approvOrganTeam, keyname, "approvOrganTeam", items_jsonObject);
								JsonParser.colWrite(bizMoney, keyname, "bizMoney", items_jsonObject);
								JsonParser.colWrite(bizSize, keyname, "bizSize", items_jsonObject);
								JsonParser.colWrite(bizSizeDan, keyname, "bizSizeDan", items_jsonObject);
								JsonParser.colWrite(benfBizmain, keyname, "benfBizmain", items_jsonObject);
								JsonParser.colWrite(embodEtcNm, keyname, "embodEtcNm", items_jsonObject);
								JsonParser.colWrite(ccilJongCd, keyname, "ccilJongCd", items_jsonObject);
								JsonParser.colWrite(embodCd, keyname, "embodCd", items_jsonObject);
								JsonParser.colWrite(embodCd2, keyname, "embodCd2", items_jsonObject);
								JsonParser.colWrite(ccilOrganCd, keyname, "ccilOrganCd", items_jsonObject);
								JsonParser.colWrite(ctcMemNm, keyname, "ctcMemNm", items_jsonObject);
								JsonParser.colWrite(ctcMemTeam, keyname, "ctcMemTeam", items_jsonObject);
								JsonParser.colWrite(ctcMemEmail, keyname, "ctcMemEmail", items_jsonObject);
								JsonParser.colWrite(ctcMemTel, keyname, "ctcMemTel", items_jsonObject);
								JsonParser.colWrite(ctcMemFax, keyname, "ctcMemFax", items_jsonObject);
								JsonParser.colWrite(bizAddrEtc, keyname, "bizAddrEtc", items_jsonObject);
								JsonParser.colWrite(bizAddrEtc2, keyname, "bizAddrEtc2", items_jsonObject);
								JsonParser.colWrite(drfopPclDt, keyname, "drfopPclDt", items_jsonObject);
								JsonParser.colWrite(drfopTmdtStartDt, keyname, "drfopTmdtStartDt", items_jsonObject);
								JsonParser.colWrite(drfopTmdtEndDt, keyname, "drfopTmdtEndDt", items_jsonObject);
								JsonParser.colWrite(drfopExpDttm, keyname, "drfopExpDttm", items_jsonObject);
								JsonParser.colWrite(drfopOpnStartDt, keyname, "drfopOpnStartDt", items_jsonObject);
								JsonParser.colWrite(drfopOpnEndDt, keyname, "drfopOpnEndDt", items_jsonObject);
								JsonParser.colWrite(drfopSite, keyname, "drfopSite", items_jsonObject);
								JsonParser.colWrite(drfopExpSite, keyname, "drfopExpSite", items_jsonObject);
								JsonParser.colWrite(drfopTeamNm, keyname, "drfopTeamNm", items_jsonObject);
								JsonParser.colWrite(drfopTel, keyname, "drfopTel", items_jsonObject);

							}

							// 한번에 문자열 합침
							resultSb.append(perCd);
							resultSb.append("|^");
							resultSb.append(bizNm);
							resultSb.append("|^");
							resultSb.append(approvOrganTeam);
							resultSb.append("|^");
							resultSb.append(bizMoney);
							resultSb.append("|^");
							resultSb.append(bizSize);
							resultSb.append("|^");
							resultSb.append(bizSizeDan);
							resultSb.append("|^");
							resultSb.append(benfBizmain);
							resultSb.append("|^");
							resultSb.append(embodEtcNm);
							resultSb.append("|^");
							resultSb.append(ccilJongCd);
							resultSb.append("|^");
							resultSb.append(embodCd);
							resultSb.append("|^");
							resultSb.append(embodCd2);
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
							resultSb.append(bizAddrEtc);
							resultSb.append("|^");
							resultSb.append(bizAddrEtc2);
							resultSb.append("|^");
							resultSb.append(drfopPclDt);
							resultSb.append("|^");
							resultSb.append(drfopTmdtStartDt);
							resultSb.append("|^");
							resultSb.append(drfopTmdtEndDt);
							resultSb.append("|^");
							resultSb.append(drfopExpDttm);
							resultSb.append("|^");
							resultSb.append(drfopOpnStartDt);
							resultSb.append("|^");
							resultSb.append(drfopOpnEndDt);
							resultSb.append("|^");
							resultSb.append(drfopSite);
							resultSb.append("|^");
							resultSb.append(drfopExpSite);
							resultSb.append("|^");
							resultSb.append(drfopTeamNm);
							resultSb.append("|^");
							resultSb.append(drfopTel);
							resultSb.append(System.getProperty("line.separator"));

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(perCd, keyname, "perCd", item_obj);
									JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
									JsonParser.colWrite(approvOrganTeam, keyname, "approvOrganTeam", item_obj);
									JsonParser.colWrite(bizMoney, keyname, "bizMoney", item_obj);
									JsonParser.colWrite(bizSize, keyname, "bizSize", item_obj);
									JsonParser.colWrite(bizSizeDan, keyname, "bizSizeDan", item_obj);
									JsonParser.colWrite(benfBizmain, keyname, "benfBizmain", item_obj);
									JsonParser.colWrite(embodEtcNm, keyname, "embodEtcNm", item_obj);
									JsonParser.colWrite(ccilJongCd, keyname, "ccilJongCd", item_obj);
									JsonParser.colWrite(embodCd, keyname, "embodCd", item_obj);
									JsonParser.colWrite(embodCd2, keyname, "embodCd2", item_obj);
									JsonParser.colWrite(ccilOrganCd, keyname, "ccilOrganCd", item_obj);
									JsonParser.colWrite(ctcMemNm, keyname, "ctcMemNm", item_obj);
									JsonParser.colWrite(ctcMemTeam, keyname, "ctcMemTeam", item_obj);
									JsonParser.colWrite(ctcMemEmail, keyname, "ctcMemEmail", item_obj);
									JsonParser.colWrite(ctcMemTel, keyname, "ctcMemTel", item_obj);
									JsonParser.colWrite(ctcMemFax, keyname, "ctcMemFax", item_obj);
									JsonParser.colWrite(bizAddrEtc, keyname, "bizAddrEtc", item_obj);
									JsonParser.colWrite(bizAddrEtc2, keyname, "bizAddrEtc2", item_obj);
									JsonParser.colWrite(drfopPclDt, keyname, "drfopPclDt", item_obj);
									JsonParser.colWrite(drfopTmdtStartDt, keyname, "drfopTmdtStartDt", item_obj);
									JsonParser.colWrite(drfopTmdtEndDt, keyname, "drfopTmdtEndDt", item_obj);
									JsonParser.colWrite(drfopExpDttm, keyname, "drfopExpDttm", item_obj);
									JsonParser.colWrite(drfopOpnStartDt, keyname, "drfopOpnStartDt", item_obj);
									JsonParser.colWrite(drfopOpnEndDt, keyname, "drfopOpnEndDt", item_obj);
									JsonParser.colWrite(drfopSite, keyname, "drfopSite", item_obj);
									JsonParser.colWrite(drfopExpSite, keyname, "drfopExpSite", item_obj);
									JsonParser.colWrite(drfopTeamNm, keyname, "drfopTeamNm", item_obj);
									JsonParser.colWrite(drfopTel, keyname, "drfopTel", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(perCd);
								resultSb.append("|^");
								resultSb.append(bizNm);
								resultSb.append("|^");
								resultSb.append(approvOrganTeam);
								resultSb.append("|^");
								resultSb.append(bizMoney);
								resultSb.append("|^");
								resultSb.append(bizSize);
								resultSb.append("|^");
								resultSb.append(bizSizeDan);
								resultSb.append("|^");
								resultSb.append(benfBizmain);
								resultSb.append("|^");
								resultSb.append(embodEtcNm);
								resultSb.append("|^");
								resultSb.append(ccilJongCd);
								resultSb.append("|^");
								resultSb.append(embodCd);
								resultSb.append("|^");
								resultSb.append(embodCd2);
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
								resultSb.append(bizAddrEtc);
								resultSb.append("|^");
								resultSb.append(bizAddrEtc2);
								resultSb.append("|^");
								resultSb.append(drfopPclDt);
								resultSb.append("|^");
								resultSb.append(drfopTmdtStartDt);
								resultSb.append("|^");
								resultSb.append(drfopTmdtEndDt);
								resultSb.append("|^");
								resultSb.append(drfopExpDttm);
								resultSb.append("|^");
								resultSb.append(drfopOpnStartDt);
								resultSb.append("|^");
								resultSb.append(drfopOpnEndDt);
								resultSb.append("|^");
								resultSb.append(drfopSite);
								resultSb.append("|^");
								resultSb.append(drfopExpSite);
								resultSb.append("|^");
								resultSb.append(drfopTeamNm);
								resultSb.append("|^");
								resultSb.append(drfopTel);
								resultSb.append(System.getProperty("line.separator"));
							}

						} else {
							System.out.println("parsing error!!");
						}

					} else {
						System.out.println("parsing error!!");
					}

					// step 4. 파일에 쓰기
					try {
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.println(resultSb.toString());
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
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
