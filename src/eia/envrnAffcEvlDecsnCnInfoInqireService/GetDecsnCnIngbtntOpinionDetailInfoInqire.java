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
import org.json.simple.parser.JSONParser;

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

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":\"\"}}";
					}

					// step 2. 전체 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer resultCd = new StringBuffer(" "); // 결정내용코드
					StringBuffer bizNm = new StringBuffer(" "); // 사업명
					StringBuffer approvOrganTeam = new StringBuffer(" "); // 승인기관
					StringBuffer openPclDt = new StringBuffer(" "); // 공고일
					StringBuffer openTmdtStartDt = new StringBuffer(" "); // 공람기간시작일
					StringBuffer openTmdtEndDt = new StringBuffer(" "); // 공람기간종료일
					StringBuffer openOpnEndDt = new StringBuffer(" "); // 의견종료일
					StringBuffer openOpnStartDt = new StringBuffer(" "); // 의견시작일
					StringBuffer openOpnEtc = new StringBuffer(" "); // 결정내용
					StringBuffer openTeamNm = new StringBuffer(" "); // 부서명
					StringBuffer bizManTxt = new StringBuffer(" "); // 사업자
					StringBuffer discOrganNm = new StringBuffer(" "); // 협의기관

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (resultCode.equals("00") && response.get("body") instanceof String) {
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

								JsonParser.colWrite(resultCd, keyname, "resultCd", items_jsonObject);
								JsonParser.colWrite(bizNm, keyname, "bizNm", items_jsonObject);
								JsonParser.colWrite(approvOrganTeam, keyname, "approvOrganTeam", items_jsonObject);
								JsonParser.colWrite(openPclDt, keyname, "openPclDt", items_jsonObject);
								JsonParser.colWrite(openTmdtStartDt, keyname, "openTmdtStartDt", items_jsonObject);
								JsonParser.colWrite(openTmdtEndDt, keyname, "openTmdtEndDt", items_jsonObject);
								JsonParser.colWrite(openOpnEndDt, keyname, "openOpnEndDt", items_jsonObject);
								JsonParser.colWrite(openOpnStartDt, keyname, "openOpnStartDt", items_jsonObject);
								JsonParser.colWrite(openOpnEtc, keyname, "openOpnEtc", items_jsonObject);
								JsonParser.colWrite(openTeamNm, keyname, "openTeamNm", items_jsonObject);
								JsonParser.colWrite(bizManTxt, keyname, "bizManTxt", items_jsonObject);
								JsonParser.colWrite(discOrganNm, keyname, "discOrganNm", items_jsonObject);

							}

							// 한번에 문자열 합침
							resultSb.append(resultCode);
							resultSb.append("|^");
							resultSb.append(resultMsg);
							resultSb.append("|^");
							resultSb.append(resultCd);
							resultSb.append("|^");
							resultSb.append(bizNm);
							resultSb.append("|^");
							resultSb.append(approvOrganTeam);
							resultSb.append("|^");
							resultSb.append(openPclDt);
							resultSb.append("|^");
							resultSb.append(openTmdtStartDt);
							resultSb.append("|^");
							resultSb.append(openTmdtEndDt);
							resultSb.append("|^");
							resultSb.append(openOpnEndDt);
							resultSb.append("|^");
							resultSb.append(openOpnStartDt);
							resultSb.append("|^");
							resultSb.append(openOpnEtc);
							resultSb.append("|^");
							resultSb.append(openTeamNm);
							resultSb.append("|^");
							resultSb.append(bizManTxt);
							resultSb.append("|^");
							resultSb.append(discOrganNm);
							resultSb.append(System.getProperty("line.separator"));

						} else if (body.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) body.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(resultCd, keyname, "resultCd", item_obj);
									JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
									JsonParser.colWrite(approvOrganTeam, keyname, "approvOrganTeam", item_obj);
									JsonParser.colWrite(openPclDt, keyname, "openPclDt", item_obj);
									JsonParser.colWrite(openTmdtStartDt, keyname, "openTmdtStartDt", item_obj);
									JsonParser.colWrite(openTmdtEndDt, keyname, "openTmdtEndDt", item_obj);
									JsonParser.colWrite(openOpnEndDt, keyname, "openOpnEndDt", item_obj);
									JsonParser.colWrite(openOpnStartDt, keyname, "openOpnStartDt", item_obj);
									JsonParser.colWrite(openOpnEtc, keyname, "openOpnEtc", item_obj);
									JsonParser.colWrite(openTeamNm, keyname, "openTeamNm", item_obj);
									JsonParser.colWrite(bizManTxt, keyname, "bizManTxt", item_obj);
									JsonParser.colWrite(discOrganNm, keyname, "discOrganNm", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(resultCode);
								resultSb.append("|^");
								resultSb.append(resultMsg);
								resultSb.append("|^");
								resultSb.append(resultCd);
								resultSb.append("|^");
								resultSb.append(bizNm);
								resultSb.append("|^");
								resultSb.append(approvOrganTeam);
								resultSb.append("|^");
								resultSb.append(openPclDt);
								resultSb.append("|^");
								resultSb.append(openTmdtStartDt);
								resultSb.append("|^");
								resultSb.append(openTmdtEndDt);
								resultSb.append("|^");
								resultSb.append(openOpnEndDt);
								resultSb.append("|^");
								resultSb.append(openOpnStartDt);
								resultSb.append("|^");
								resultSb.append(openOpnEtc);
								resultSb.append("|^");
								resultSb.append(openTeamNm);
								resultSb.append("|^");
								resultSb.append(bizManTxt);
								resultSb.append("|^");
								resultSb.append(discOrganNm);
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

						pw.write(resultSb.toString());
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
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
