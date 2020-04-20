package wri.sluicePresentCondition;

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

public class Mnt {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문현황정보(10분)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 댐코드 1개, 날짜를 yyyymmdd로 2개 받는다. 파라미터 유효성 체크는 파싱 때 체크
				if (args.length == 3) {

					if (args[1].length() == 8 && args[2].length() == 8) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("sluicePresentCondition_mnt_url");
						String service_key = JsonParser.getProperty("sluicePresentCondition_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_04.dat");

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
						String numberOfRows_str = "";

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2]);

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
							numberOfRows_str = Integer.toString(numOfRows);

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						StringBuffer resultSb = new StringBuffer("");

						StringBuffer obsrdtmnt = new StringBuffer(" "); // 일시
						StringBuffer lowlevel = new StringBuffer(" "); // 댐수위
						StringBuffer rf = new StringBuffer(" "); // 강우량
						StringBuffer inflowqy = new StringBuffer(" "); // 유입량
						StringBuffer totdcwtrqy = new StringBuffer(" "); // 총방류량
						StringBuffer rsvwtqy = new StringBuffer(" "); // 저수량
						StringBuffer rsvwtrt = new StringBuffer(" "); // 저수율

						for (int i = 1; i <= pageCount; ++i) {

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2]);

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

										JsonParser.colWrite(obsrdtmnt, keyname, "obsrdtmnt", items_jsonObject);
										JsonParser.colWrite(lowlevel, keyname, "lowlevel", items_jsonObject);
										JsonParser.colWrite(rf, keyname, "rf", items_jsonObject);
										JsonParser.colWrite(inflowqy, keyname, "inflowqy", items_jsonObject);
										JsonParser.colWrite(totdcwtrqy, keyname, "totdcwtrqy", items_jsonObject);
										JsonParser.colWrite(rsvwtqy, keyname, "rsvwtqy", items_jsonObject);
										JsonParser.colWrite(rsvwtrt, keyname, "rsvwtrt", items_jsonObject);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(args[2]);
									resultSb.append("|^");
									resultSb.append(obsrdtmnt);
									resultSb.append("|^");
									resultSb.append(lowlevel);
									resultSb.append("|^");
									resultSb.append(rf);
									resultSb.append("|^");
									resultSb.append(inflowqy);
									resultSb.append("|^");
									resultSb.append(totdcwtrqy);
									resultSb.append("|^");
									resultSb.append(rsvwtqy);
									resultSb.append("|^");
									resultSb.append(rsvwtrt);
									resultSb.append("|^");
									resultSb.append(numberOfRows_str);
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

											JsonParser.colWrite(obsrdtmnt, keyname, "obsrdtmnt", item_obj);
											JsonParser.colWrite(lowlevel, keyname, "lowlevel", item_obj);
											JsonParser.colWrite(rf, keyname, "rf", item_obj);
											JsonParser.colWrite(inflowqy, keyname, "inflowqy", item_obj);
											JsonParser.colWrite(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
											JsonParser.colWrite(rsvwtqy, keyname, "rsvwtqy", item_obj);
											JsonParser.colWrite(rsvwtrt, keyname, "rsvwtrt", item_obj);

										}

										// 한번에 문자열 합침
										resultSb.append(args[0]);
										resultSb.append("|^");
										resultSb.append(args[1]);
										resultSb.append("|^");
										resultSb.append(args[2]);
										resultSb.append("|^");
										resultSb.append(obsrdtmnt);
										resultSb.append("|^");
										resultSb.append(lowlevel);
										resultSb.append("|^");
										resultSb.append(rf);
										resultSb.append("|^");
										resultSb.append(inflowqy);
										resultSb.append("|^");
										resultSb.append(totdcwtrqy);
										resultSb.append("|^");
										resultSb.append(rsvwtqy);
										resultSb.append("|^");
										resultSb.append(rsvwtrt);
										resultSb.append("|^");
										resultSb.append(numberOfRows_str);
										resultSb.append("|^");
										resultSb.append(String.valueOf(i));
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_04.dat", "WRI");

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
				System.out.println("damcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
			}


	}

}
