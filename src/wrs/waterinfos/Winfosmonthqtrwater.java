package wrs.waterinfos;

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

public class Winfosmonthqtrwater {

	

	// 지방정수장 수질정보 조회 서비스 - 지방상수도수질(상수원수)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터는 조회 시작일과 조회 종료일, 지자체코드의 3개
		if (args.length == 3) {

			if (args[0].length() == 6 && args[1].length() == 6) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("waterinfos_Winfosmonthqtrwater_url");
				String service_key = JsonParser.getProperty("waterinfos_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_15.dat");

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("sgcnm"); // 지자체명
					pw.write("|^");
					pw.write("sitenm"); // 지점명
					pw.write("|^");
					pw.write("cltdt"); // 측정일자
					pw.write("|^");
					pw.write("data1"); // 수소이온농도(pH)
					pw.write("|^");
					pw.write("data2"); // BOD(호소수:COD)
					pw.write("|^");
					pw.write("data3"); // 부유물질(SS)
					pw.write("|^");
					pw.write("data4"); // 용존산소량(DO)
					pw.write("|^");
					pw.write("data5"); // 총대장균군(원수)
					pw.write("|^");
					pw.write("data6"); // 분원성대장균군수(원수-CFU)
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

				// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
				String json = "";

				int pageNo = 0;
				int pageCount = 0;

				json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1],
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

				StringBuffer sgcnm = new StringBuffer(" "); // 지자체명
				StringBuffer sitenm = new StringBuffer(" "); // 지점명
				StringBuffer cltdt = new StringBuffer(" "); // 측정일자
				StringBuffer data1 = new StringBuffer(" "); // 수소이온농도(pH)
				StringBuffer data2 = new StringBuffer(" "); // BOD(호소수:COD)
				StringBuffer data3 = new StringBuffer(" "); // 부유물질(SS)
				StringBuffer data4 = new StringBuffer(" "); // 용존산소량(DO)
				StringBuffer data5 = new StringBuffer(" "); // 총대장균군(원수)
				StringBuffer data6 = new StringBuffer(" "); // 분원성대장균군수(원수-CFU)

				for (int i = 1; i <= pageCount; i++) {

					json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0], args[1],
							args[2]);

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

									JsonParser.colWrite(sgcnm, keyname, "sgcnm", items_jsonObject);
									JsonParser.colWrite(sitenm, keyname, "sitenm", items_jsonObject);
									JsonParser.colWrite(cltdt, keyname, "cltdt", items_jsonObject);
									JsonParser.colWrite(data1, keyname, "data1", items_jsonObject);
									JsonParser.colWrite(data2, keyname, "data2", items_jsonObject);
									JsonParser.colWrite(data3, keyname, "data3", items_jsonObject);
									JsonParser.colWrite(data4, keyname, "data4", items_jsonObject);
									JsonParser.colWrite(data5, keyname, "data5", items_jsonObject);
									JsonParser.colWrite(data6, keyname, "data6", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(sgcnm);
								resultSb.append("|^");
								resultSb.append(sitenm);
								resultSb.append("|^");
								resultSb.append(cltdt);
								resultSb.append("|^");
								resultSb.append(data1);
								resultSb.append("|^");
								resultSb.append(data2);
								resultSb.append("|^");
								resultSb.append(data3);
								resultSb.append("|^");
								resultSb.append(data4);
								resultSb.append("|^");
								resultSb.append(data5);
								resultSb.append("|^");
								resultSb.append(data6);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {
								
								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(sgcnm, keyname, "sgcnm", item_obj);
										JsonParser.colWrite(sitenm, keyname, "sitenm", item_obj);
										JsonParser.colWrite(cltdt, keyname, "cltdt", item_obj);
										JsonParser.colWrite(data1, keyname, "data1", item_obj);
										JsonParser.colWrite(data2, keyname, "data2", item_obj);
										JsonParser.colWrite(data3, keyname, "data3", item_obj);
										JsonParser.colWrite(data4, keyname, "data4", item_obj);
										JsonParser.colWrite(data5, keyname, "data5", item_obj);
										JsonParser.colWrite(data6, keyname, "data6", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(sgcnm);
									resultSb.append("|^");
									resultSb.append(sitenm);
									resultSb.append("|^");
									resultSb.append(cltdt);
									resultSb.append("|^");
									resultSb.append(data1);
									resultSb.append("|^");
									resultSb.append(data2);
									resultSb.append("|^");
									resultSb.append(data3);
									resultSb.append("|^");
									resultSb.append(data4);
									resultSb.append("|^");
									resultSb.append(data5);
									resultSb.append("|^");
									resultSb.append(data6);
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

				TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_15.dat", "WRS");

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
