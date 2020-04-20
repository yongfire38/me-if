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

public class Hourrf {

	// 우량 관측정보 조회 서비스 - 우량 시강우량 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd),
				// 조회종료 시각
				// 2자리, 댐 코드, 우량관측소 코드
				// 댐코드는 수문제원현황 코드조회 api에서 조회, 우량관측소 코드는 우량관측소 코드 조회 api에서 조회

				if (args.length == 6) {

					if (args[0].length() == 8 && args[1].length() == 2 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("excllncobsrvt_hourrf_url");
						String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_13.dat");

						try {
							
							PrintWriter pw = new PrintWriter(
									new BufferedWriter(new FileWriter(file, true)));

							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWriJson_excll(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3], args[4], args[5]);

						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");

						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
						} else {

							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;

						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						StringBuffer resultSb = new StringBuffer("");

						StringBuffer no = new StringBuffer(" "); // 순번
						StringBuffer obsrdt = new StringBuffer(" "); // 시간
						StringBuffer hourrf = new StringBuffer(" "); // 시강우량
						StringBuffer acmtlrf = new StringBuffer(" "); // 누적우량

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson_excll(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3], args[4], args[5]);

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

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

										JsonParser.colWrite(no, keyname, "no", items_jsonObject);
										JsonParser.colWrite(obsrdt, keyname, "obsrdt", items_jsonObject);
										JsonParser.colWrite(hourrf, keyname, "hourrf", items_jsonObject);
										JsonParser.colWrite(acmtlrf, keyname, "acmtlrf", items_jsonObject);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(args[2]);
									resultSb.append("|^");
									resultSb.append(args[3]);
									resultSb.append("|^");
									resultSb.append(args[4]);
									resultSb.append("|^");
									resultSb.append(args[5]);
									resultSb.append("|^");
									resultSb.append(no);
									resultSb.append("|^");
									resultSb.append(obsrdt);
									resultSb.append("|^");
									resultSb.append(hourrf);
									resultSb.append("|^");
									resultSb.append(acmtlrf);
									resultSb.append(System.getProperty("line.separator"));

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();

											JsonParser.colWrite(no, keyname, "no", item_obj);
											JsonParser.colWrite(obsrdt, keyname, "obsrdt", item_obj);
											JsonParser.colWrite(hourrf, keyname, "hourrf", item_obj);
											JsonParser.colWrite(acmtlrf, keyname, "acmtlrf", item_obj);

										}

										// 한번에 문자열 합침
										resultSb.append(args[0]);
										resultSb.append("|^");
										resultSb.append(args[1]);
										resultSb.append("|^");
										resultSb.append(args[2]);
										resultSb.append("|^");
										resultSb.append(args[3]);
										resultSb.append("|^");
										resultSb.append(args[4]);
										resultSb.append("|^");
										resultSb.append(args[5]);
										resultSb.append("|^");
										resultSb.append(no);
										resultSb.append("|^");
										resultSb.append(obsrdt);
										resultSb.append("|^");
										resultSb.append(hourrf);
										resultSb.append("|^");
										resultSb.append(acmtlrf);
										resultSb.append(System.getProperty("line.separator"));

									}

								} else {
									System.out.println("parsing error!!");
								}

							} else {
								System.out.println("parsing error!!");
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_13.dat", "WRI");

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


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("sdate :" + args[0] + ": stime :" + args[1] + ": edate :" + args[2] + ": etime :"
						+ args[3] + ": damcode :" + args[4] + ": excll :" + args[5]);
			}



	}

}
