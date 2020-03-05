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

public class Indfltplt {
	
	

	// 광역정수장 수질정보 조회 서비스 - 공업용 정수장 코드 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("dailwater_Indfltplt_url");
					String service_key = JsonParser.getProperty("dailwater_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_25.dat");

					try {
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.write("fltplt"); // 정수장코드
						pw.write("|^");
						pw.write("fltpltnm"); // 정수장명
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

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

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

					StringBuffer fltplt = new StringBuffer(" "); // 정수장코드
					StringBuffer fltpltnm = new StringBuffer(" "); // 정수장명

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

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

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(fltplt, keyname, "fltplt", item_obj);
										JsonParser.colWrite(fltpltnm, keyname, "fltpltnm", item_obj);
									}

									// 한번에 문자열 합침
									resultSb.append(fltplt);
									resultSb.append("|^");
									resultSb.append(fltpltnm);
									resultSb.append("|^");
									resultSb.append(numOfRows_str);
									resultSb.append("|^");
									resultSb.append(String.valueOf(i));
									resultSb.append(System.getProperty("line.separator"));

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

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_25.dat", "WRS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


	}

}
