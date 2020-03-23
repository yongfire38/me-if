package wri.sihwavalue;

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


public class Sihwavalue {

	

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 시화조력 수문정보
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 날짜를 yyyymmdd로 2개 받는다, 파라미터 유효성 체크는 파싱 때 체크
		if (args.length == 2) {

			if (args[0].length() == 8 && args[1].length() == 8) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("sihwavalue_url");
				String service_key = JsonParser.getProperty("sihwavalue_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_03.dat");

				if(file.exists()){
					
					System.out.println("파일이 이미 존재하므로 이어쓰기..");
					
				} else {
				
					try {
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.write("stdt"); // 조회시작일자
						pw.write("|^");
						pw.write("eddt"); // 조회종료일자
						pw.write("|^");
						pw.write("bidno"); // 번호
						pw.write("|^");
						pw.write("obsdt"); // 일자
						pw.write("|^");
						pw.write("lakeRwl"); // 호소위(EL.m)
						pw.write("|^");
						pw.write("seaRwl"); // 해수위(EL.m)
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

				json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);

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

				StringBuffer bidno = new StringBuffer(" "); // 번호
				StringBuffer obsdt = new StringBuffer(" "); // 일자
				StringBuffer lakeRwl = new StringBuffer(" "); // 호소위(EL.m)
				StringBuffer seaRwl = new StringBuffer(" "); // 해수위(EL.m)

				for (int i = 1; i <= pageCount; ++i) {

					json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0], args[1]);

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();

						if (resultCode.equals("00")) {

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(bidno, keyname, "bidno", items_jsonObject);
									JsonParser.colWrite(obsdt, keyname, "obsdt", items_jsonObject);
									JsonParser.colWrite(lakeRwl, keyname, "lakeRwl", items_jsonObject);
									JsonParser.colWrite(seaRwl, keyname, "seaRwl", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(args[0]);
								resultSb.append("|^");
								resultSb.append(args[1]);
								resultSb.append("|^");
								resultSb.append(bidno);
								resultSb.append("|^");
								resultSb.append(obsdt);
								resultSb.append("|^");
								resultSb.append(lakeRwl);
								resultSb.append("|^");
								resultSb.append(seaRwl);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(bidno, keyname, "bidno", item_obj);
										JsonParser.colWrite(obsdt, keyname, "obsdt", item_obj);
										JsonParser.colWrite(lakeRwl, keyname, "lakeRwl", item_obj);
										JsonParser.colWrite(seaRwl, keyname, "seaRwl", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(bidno);
									resultSb.append("|^");
									resultSb.append(obsdt);
									resultSb.append("|^");
									resultSb.append(lakeRwl);
									resultSb.append("|^");
									resultSb.append(seaRwl);
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

				//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_03.dat", "WRI");

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
