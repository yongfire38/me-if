package wrs.dailwater;

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
import common.TransSftp;

public class Dailindwater {

	

	// 광역정수장 수질정보 조회 서비스 - 광역일일 공업용 수돗물 수질 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터는 조회시작일(yyyyMMdd), 조회종료일(yyyyMMdd), 정수장 코드의 3개
		// 정수장 코드는 정수장 코드 조회 api에서 조회 가능
		if (args.length == 3) {

			if (args[1].length() == 8 && args[2].length() == 8) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("dailwater_Dailindwater_url");
				String service_key = JsonParser.getProperty("dailwater_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_19.dat");

				if(file.exists()){
					
					System.out.println("파일이 이미 존재하므로 이어쓰기..");
					
				} else {
				
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
						pw.write("mesurede"); // 측정시간
						pw.write("|^");
						pw.write("item1"); // 수온 원수
						pw.write("|^");
						pw.write("item2"); // 수온 침전수
						pw.write("|^");
						pw.write("item3"); // pH(-) 원수
						pw.write("|^");
						pw.write("item4"); // pH(-) 침전수
						pw.write("|^");
						pw.write("item5"); // 탁도(NTU) 원수
						pw.write("|^");
						pw.write("item6"); // 탁도(NTU) 침전수
						pw.write("|^");
						pw.write("item7"); // 전기전도도(㎲/㎝) 원수
						pw.write("|^");
						pw.write("item8"); // 전기전도도(㎲/㎝) 침전수
						pw.write("|^");
						pw.write("item9"); // 알칼리도(mg/L) 원수
						pw.write("|^");
						pw.write("item10"); // 알칼리도(mg/L) 침전수
						pw.write("|^");
						pw.write("numOfRows"); // 줄수
						pw.write("|^");
						pw.write("pageNo"); // 페이지번호
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
						args[2]);

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

				StringBuffer mesurede = new StringBuffer(" "); // 측정시간
				StringBuffer item1 = new StringBuffer(" "); // 수온 원수
				StringBuffer item2 = new StringBuffer(" "); // 수온 침전수
				StringBuffer item3 = new StringBuffer(" "); // pH(-) 원수
				StringBuffer item4 = new StringBuffer(" "); // pH(-) 침전수
				StringBuffer item5 = new StringBuffer(" "); // 탁도(NTU) 원수
				StringBuffer item6 = new StringBuffer(" "); // 탁도(NTU) 침전수
				StringBuffer item7 = new StringBuffer(" "); // 전기전도도(㎲/㎝) 원수
				StringBuffer item8 = new StringBuffer(" "); // 전기전도도(㎲/㎝) 침전수
				StringBuffer item9 = new StringBuffer(" "); // 알칼리도(mg/L) 원수
				StringBuffer item10 = new StringBuffer(" "); // 알칼리도(mg/L) 침전수

				for (int i = 1; i <= pageCount; i++) {

					json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0], args[1],
							args[2]);

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();

						String numOfRows_str = body.get("numOfRows").toString();

						if (resultCode.equals("00")) {

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(mesurede, keyname, "mesurede", items_jsonObject);
									JsonParser.colWrite(item1, keyname, "item1", items_jsonObject);
									JsonParser.colWrite(item2, keyname, "item2", items_jsonObject);
									JsonParser.colWrite(item3, keyname, "item3", items_jsonObject);
									JsonParser.colWrite(item4, keyname, "item4", items_jsonObject);
									JsonParser.colWrite(item5, keyname, "item5", items_jsonObject);
									JsonParser.colWrite(item6, keyname, "item6", items_jsonObject);
									JsonParser.colWrite(item7, keyname, "item7", items_jsonObject);
									JsonParser.colWrite(item8, keyname, "item8", items_jsonObject);
									JsonParser.colWrite(item9, keyname, "item9", items_jsonObject);
									JsonParser.colWrite(item10, keyname, "item10", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(mesurede);
								resultSb.append("|^");
								resultSb.append(item1);
								resultSb.append("|^");
								resultSb.append(item2);
								resultSb.append("|^");
								resultSb.append(item3);
								resultSb.append("|^");
								resultSb.append(item4);
								resultSb.append("|^");
								resultSb.append(item5);
								resultSb.append("|^");
								resultSb.append(item6);
								resultSb.append("|^");
								resultSb.append(item7);
								resultSb.append("|^");
								resultSb.append(item8);
								resultSb.append("|^");
								resultSb.append(item9);
								resultSb.append("|^");
								resultSb.append(item10);
								resultSb.append("|^");
								resultSb.append(numOfRows_str);
								resultSb.append("|^");
								resultSb.append(String.valueOf(i));
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(mesurede, keyname, "mesurede", item_obj);
										JsonParser.colWrite(item1, keyname, "item1", item_obj);
										JsonParser.colWrite(item2, keyname, "item2", item_obj);
										JsonParser.colWrite(item3, keyname, "item3", item_obj);
										JsonParser.colWrite(item4, keyname, "item4", item_obj);
										JsonParser.colWrite(item5, keyname, "item5", item_obj);
										JsonParser.colWrite(item6, keyname, "item6", item_obj);
										JsonParser.colWrite(item7, keyname, "item7", item_obj);
										JsonParser.colWrite(item8, keyname, "item8", item_obj);
										JsonParser.colWrite(item9, keyname, "item9", item_obj);
										JsonParser.colWrite(item10, keyname, "item10", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(mesurede);
									resultSb.append("|^");
									resultSb.append(item1);
									resultSb.append("|^");
									resultSb.append(item2);
									resultSb.append("|^");
									resultSb.append(item3);
									resultSb.append("|^");
									resultSb.append(item4);
									resultSb.append("|^");
									resultSb.append(item5);
									resultSb.append("|^");
									resultSb.append(item6);
									resultSb.append("|^");
									resultSb.append(item7);
									resultSb.append("|^");
									resultSb.append(item8);
									resultSb.append("|^");
									resultSb.append(item9);
									resultSb.append("|^");
									resultSb.append(item10);
									resultSb.append("|^");
									resultSb.append(numOfRows_str);
									resultSb.append("|^");
									resultSb.append(String.valueOf(i));
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

				TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_19.dat", "WRS");

				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
				
				// step 6. 원본 파일은 삭제
				if(file.exists()){
					if(file.delete()){
						System.out.println("원본파일 삭제 처리 완료");
					}else{
						System.out.println("원본 파일 삭제 처리 실패");
					}
					
				} else {
					System.out.println("파일이 존재하지 않습니다.");
				}

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
