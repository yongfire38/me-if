package wri.excllncobsrvt;

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


public class Mntrf {

	

	// 우량 관측정보 조회 서비스 - 우량 분강우량 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd), 조회종료 시각
		// 2자리, 우량관측소 코드
		// 우량관측소 코드는 우량관측소 코드 조회 api에서 조회
		if (args.length == 5) {

			if (args[0].length() == 8 && args[1].length() == 2 && args[2].length() == 8 && args[3].length() == 2) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("excllncobsrvt_mntrf_url");
				String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_12.dat");

				if(file.exists()){
					
					System.out.println("파일이 이미 존재하므로 이어쓰기..");
					
				} else {
				
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
						pw.write("acmtlprcptqy"); // 누적우량 현저수량
						pw.write("|^");
						pw.write("no"); // 순번
						pw.write("|^");
						pw.write("obsrdtmnt"); // 시간
						pw.write("|^");
						pw.write("prcptqy"); // 우량
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

				json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1],
						args[2], args[3], args[4]);

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

				StringBuffer resultSb = new StringBuffer("");

				StringBuffer acmtlprcptqy = new StringBuffer(" "); // 누적우량 현저수량
				StringBuffer no = new StringBuffer(" "); // 순번
				StringBuffer obsrdtmnt = new StringBuffer(" "); // 시간
				StringBuffer prcptqy = new StringBuffer(" "); // 우량

				for (int i = 1; i <= pageCount; i++) {

					json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0], args[1],
							args[2], args[3], args[4]);

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						

						String resultCode = header.get("resultCode").toString().trim();
						
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

									JsonParser.colWrite(acmtlprcptqy, keyname, "acmtlprcptqy", items_jsonObject);
									JsonParser.colWrite(no, keyname, "no", items_jsonObject);
									JsonParser.colWrite(obsrdtmnt, keyname, "obsrdtmnt", items_jsonObject);
									JsonParser.colWrite(prcptqy, keyname, "prcptqy", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(acmtlprcptqy);
								resultSb.append("|^");
								resultSb.append(no);
								resultSb.append("|^");
								resultSb.append(obsrdtmnt);
								resultSb.append("|^");
								resultSb.append(prcptqy);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(acmtlprcptqy, keyname, "acmtlprcptqy", item_obj);
										JsonParser.colWrite(no, keyname, "no", item_obj);
										JsonParser.colWrite(obsrdtmnt, keyname, "obsrdtmnt", item_obj);
										JsonParser.colWrite(prcptqy, keyname, "prcptqy", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(acmtlprcptqy);
									resultSb.append("|^");
									resultSb.append(no);
									resultSb.append("|^");
									resultSb.append(obsrdtmnt);
									resultSb.append("|^");
									resultSb.append(prcptqy);
									resultSb.append(System.getProperty("line.separator"));

								}

							} else {
								System.out.println("parsing error!!");
							}

						} else if (resultCode.equals("03")) {
							System.out.println("data not exist!!");
						} else {
							System.out.println("parsing error!!");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					System.out.println("진행도::::::" + i + "/" + pageCount);

					Thread.sleep(1000);

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

				//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_12.dat", "WRI");

				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

			} else {
				System.out.println("파라미터 형식 에러!!");
				System.exit(-1);
			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
