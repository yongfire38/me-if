package eia.envrnAffcEvlInfoInqireService;

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

public class GetBsnsPlaceLnMyeonInfoInqire {

	// 환경영향평가 정보 서비스 - 환경영향 평가서(사업지, 면) 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 3) {

			try {

				Thread.sleep(1000);

				// 요청 파라미터 x,y 좌표의 2개
				if (args.length == 2) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("envrnAffcEvlInfoInqireService_getBsnsPlaceLnMyeonInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlInfoInqireService_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_49.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {

							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
							pw.write("resultCode"); // 결과코드
							pw.write("|^");
							pw.write("resultMsg"); // 결과메시지
							pw.write("|^");
							pw.write("numOfRows"); // 한 페이지 결과 수
							pw.write("|^");
							pw.write("pageNo"); // 페이지 번호
							pw.write("|^");
							pw.write("totalCount"); // 전체 결과 수
							pw.write("|^");
							pw.write("rnum"); // 정렬순서
							pw.write("|^");
							pw.write("num"); // 고유번호
							pw.write("|^");
							pw.write("name"); // 사업명
							pw.write("|^");
							pw.write("centerx"); // 좌표 X
							pw.write("|^");
							pw.write("centery"); // 좌표 Y
							pw.write("|^");
							pw.write("distance"); // 반경
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

					json = JsonParser.parseEiaJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer rnum = new StringBuffer(" "); // 정렬순서
					StringBuffer num = new StringBuffer(" "); // 고유번호
					StringBuffer name = new StringBuffer(" "); // 사업명
					StringBuffer centerx = new StringBuffer(" "); // 좌표 X
					StringBuffer centery = new StringBuffer(" "); // 좌표 Y
					StringBuffer distance = new StringBuffer(" "); // 반경

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseEiaJson(service_url, service_key, String.valueOf(i), args[0], args[1]);

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						String numOfRows_str = body.get("numOfRows").toString().trim();
						String totalCount_str = body.get("totalCount").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00") && body.get("items") instanceof String) {
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

									JsonParser.colWrite(rnum, keyname, "rnum", items_jsonObject);
									JsonParser.colWrite(num, keyname, "num", items_jsonObject);
									JsonParser.colWrite(name, keyname, "name", items_jsonObject);
									JsonParser.colWrite(centerx, keyname, "centerx", items_jsonObject);
									JsonParser.colWrite(centery, keyname, "centery", items_jsonObject);
									JsonParser.colWrite(distance, keyname, "distance", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(resultCode);
								resultSb.append("|^");
								resultSb.append(resultMsg);
								resultSb.append("|^");
								resultSb.append(numOfRows_str);
								resultSb.append("|^");
								resultSb.append(Integer.toString(i));
								resultSb.append("|^");
								resultSb.append(totalCount_str);
								resultSb.append("|^");
								resultSb.append(rnum);
								resultSb.append("|^");
								resultSb.append(num);
								resultSb.append("|^");
								resultSb.append(name);
								resultSb.append("|^");
								resultSb.append(centerx);
								resultSb.append("|^");
								resultSb.append(centery);
								resultSb.append("|^");
								resultSb.append(distance);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(rnum, keyname, "rnum", item_obj);
										JsonParser.colWrite(num, keyname, "num", item_obj);
										JsonParser.colWrite(name, keyname, "name", item_obj);
										JsonParser.colWrite(centerx, keyname, "centerx", item_obj);
										JsonParser.colWrite(centery, keyname, "centery", item_obj);
										JsonParser.colWrite(distance, keyname, "distance", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(resultCode);
									resultSb.append("|^");
									resultSb.append(resultMsg);
									resultSb.append("|^");
									resultSb.append(numOfRows_str);
									resultSb.append("|^");
									resultSb.append(Integer.toString(i));
									resultSb.append("|^");
									resultSb.append(totalCount_str);
									resultSb.append("|^");
									resultSb.append(rnum);
									resultSb.append("|^");
									resultSb.append(num);
									resultSb.append("|^");
									resultSb.append(name);
									resultSb.append("|^");
									resultSb.append(centerx);
									resultSb.append("|^");
									resultSb.append(centery);
									resultSb.append("|^");
									resultSb.append(distance);
									resultSb.append(System.getProperty("line.separator"));

								}

							} else {
								System.out.println("parsing error!!");
							}
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

					// TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_49.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("centerx :" + args[0] + ": centery :" + args[1]);
			}

		}

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
