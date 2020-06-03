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
//import common.TransSftp;

public class GetDscssSttusDscssChngIngDetailInfoInqire {

	@SuppressWarnings("unchecked")
	// 환경영향평가 협의현황 서비스 - 협의현황 상세 변경협의 진행 현황 정보를 조회
	public static void main(String[] args) throws Exception {

			try {

				// 실행시 필수 매개변수 환경영향평가 코드
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDscssSttusInfoInqireService_getDscssSttusDscssChngIngDetailInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_44.dat");
					
					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, args[0]);

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답, eiaCd :" + args[0]);
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
					}

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						//throw new Exception();
					} else if (resultCode.equals("00") && body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
						
						

						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							String stateNm = " "; // 단계명
							String applyDt = " "; // 접수일
							String reviExaDt = " "; // 검토의뢰일
							String resApplyDt = " "; // 검토결과접수일
							String resReplyDt = " "; // 통보일
							String rtnDt = " "; // 반려일
							String wdwlDt = " "; // 취하

							while (iter.hasNext()) {

								String keyname = iter.next();

								stateNm = JsonParser.colWrite_String(stateNm, keyname, "stateNm", items_jsonObject);
								applyDt = JsonParser.colWrite_String(applyDt, keyname, "applyDt", items_jsonObject);
								reviExaDt = JsonParser.colWrite_String(reviExaDt, keyname, "reviExaDt", items_jsonObject);
								resApplyDt = JsonParser.colWrite_String(resApplyDt, keyname, "resApplyDt", items_jsonObject);
								resReplyDt = JsonParser.colWrite_String(resReplyDt, keyname, "resReplyDt", items_jsonObject);
								rtnDt = JsonParser.colWrite_String(rtnDt, keyname, "rtnDt", items_jsonObject);
								wdwlDt = JsonParser.colWrite_String(wdwlDt, keyname, "wdwlDt", items_jsonObject);

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(args[0]); // 환경영향평가 코드
								pw.write("|^");
								pw.write(stateNm); // 단계명
								pw.write("|^");
								pw.write(applyDt); // 접수일
								pw.write("|^");
								pw.write(reviExaDt); // 검토의뢰일
								pw.write("|^");
								pw.write(resApplyDt); // 검토결과접수일
								pw.write("|^");
								pw.write(resReplyDt); // 통보일
								pw.write("|^");
								pw.write(rtnDt); // 반려일
								pw.write("|^");
								pw.write(wdwlDt); // 취하
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						} else if (items.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								String stateNm = " "; // 단계명
								String applyDt = " "; // 접수일
								String reviExaDt = " "; // 검토의뢰일
								String resApplyDt = " "; // 검토결과접수일
								String resReplyDt = " "; // 통보일
								String rtnDt = " "; // 반려일
								String wdwlDt = " "; // 취하

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									stateNm = JsonParser.colWrite_String(stateNm, keyname, "stateNm", item_obj);
									applyDt = JsonParser.colWrite_String(applyDt, keyname, "applyDt", item_obj);
									reviExaDt = JsonParser.colWrite_String(reviExaDt, keyname, "reviExaDt", item_obj);
									resApplyDt = JsonParser.colWrite_String(resApplyDt, keyname, "resApplyDt", item_obj);
									resReplyDt = JsonParser.colWrite_String(resReplyDt, keyname, "resReplyDt", item_obj);
									rtnDt = JsonParser.colWrite_String(rtnDt, keyname, "rtnDt", item_obj);
									wdwlDt = JsonParser.colWrite_String(wdwlDt, keyname, "wdwlDt", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(args[0]); // 환경영향평가 코드
									pw.write("|^");
									pw.write(stateNm); // 단계명
									pw.write("|^");
									pw.write(applyDt); // 접수일
									pw.write("|^");
									pw.write(reviExaDt); // 검토의뢰일
									pw.write("|^");
									pw.write(resApplyDt); // 검토결과접수일
									pw.write("|^");
									pw.write(resReplyDt); // 통보일
									pw.write("|^");
									pw.write(rtnDt); // 반려일
									pw.write("|^");
									pw.write(wdwlDt); // 취하
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

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_44.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", eiaCd :" + args[0]);
				System.exit(-1);
			}


	}

}
