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


public class GetDscssSttusDscssIngDetailInfoInqire {

	@SuppressWarnings("unchecked")
	// 환경영향평가 협의현황 서비스 - 협의현황 상세 진행 현황을 조회
	public static void main(String[] args) throws Exception {

		// 실행시 필수 매개변수 환경영향평가 코드
		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser
					.getProperty("envrnAffcEvlDscssSttusInfoInqireService_getDscssSttusDscssIngDetailInfoInqire_url");
			String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_43.dat");

			if(file.exists()){
				
				System.out.println("파일이 이미 존재하므로 이어쓰기..");
				
			} else {
			
				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("stateNm"); // 단계명
					pw.write("|^");
					pw.write("applyDt"); // 접수일
					pw.write("|^");
					pw.write("reviExaDt"); // 검토의뢰일
					pw.write("|^");
					pw.write("resApplyDt"); // 검토결과접수일
					pw.write("|^");
					pw.write("resReplyDt"); // 통보일
					pw.write("|^");
					pw.write("rtnDt"); // 반려일
					pw.write("|^");
					pw.write("wdwlDt"); // 취하
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
			
			// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
			String json = "";

			int pageNo = 0;
			int pageCount = 0;

			json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0]);

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				JSONObject body = (JSONObject) response.get("body");

				int numOfRows = ((Long) body.get("numOfRows")).intValue();
				int totalCount = ((Long) body.get("totalCount")).intValue();

				pageCount = (totalCount / numOfRows) + 1;

			} catch (Exception e) {
				e.printStackTrace();
			}

			// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

			for (int i = 1; i <= pageCount; i++) {

				json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0]);

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					
					if (body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					}

					else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

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

								if (keyname.equals("stateNm")) {
									stateNm = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("applyDt")) {
									applyDt = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("reviExaDt")) {
									reviExaDt = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("resApplyDt")) {
									resApplyDt = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("resReplyDt")) {
									resReplyDt = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("rtnDt")) {
									rtnDt = items_jsonObject.get(keyname).toString().trim();
								}
								if (keyname.equals("wdwlDt")) {
									wdwlDt = items_jsonObject.get(keyname).toString().trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

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

									if (keyname.equals("stateNm")) {
										stateNm = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("applyDt")) {
										applyDt = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("reviExaDt")) {
										reviExaDt = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("resApplyDt")) {
										resApplyDt = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("resReplyDt")) {
										resReplyDt = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("rtnDt")) {
										rtnDt = item_obj.get(keyname).toString().trim();
									}
									if (keyname.equals("wdwlDt")) {
										wdwlDt = item_obj.get(keyname).toString().trim();
									}

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

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

				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("진행도::::::" + i + "/" + pageCount);

				Thread.sleep(1000);

			}

			System.out.println("parsing complete!");

			// step 5. 대상 서버에 sftp로 보냄

			//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_43.dat", "EIA");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
