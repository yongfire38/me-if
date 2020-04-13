package wri.multiFunctionBarrier;

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

public class MultiFunctionBarrier {

	// 다기능보 관리현황 - 다기능보 관리현황 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 필요한 파라미터는 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 8 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("multiFunctionBarrier_url");
						String service_key = JsonParser.getProperty("multiFunctionBarrier_service_key");

						// step 1.파일의 첫 행 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_11.dat");

						if (file.exists()) {

							System.out.println("파일이 이미 존재하므로 이어쓰기..");

						} else {

							try {

								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write("tdate"); // 전일 날짜
								pw.write("|^");
								pw.write("ldate"); // 현재년도 마지막 날짜
								pw.write("|^");
								pw.write("vdate"); // 조회날짜
								pw.write("|^");
								pw.write("vtime"); // 조회시간
								pw.write("|^");
								pw.write("suge"); // 수계
								pw.write("|^");
								pw.write("brrernm"); // 보이름
								pw.write("|^");
								pw.write("rf"); // 강우량 금일
								pw.write("|^");
								pw.write("prcptqy"); // 강우량 전일
								pw.write("|^");
								pw.write("nowprcptqy"); // 누계 금년
								pw.write("|^");
								pw.write("lastprcptqy"); // 누계 전년
								pw.write("|^");
								pw.write("nyearprcptqy"); // 누계 예년
								pw.write("|^");
								pw.write("inflowqy"); // 전입유입량
								pw.write("|^");
								pw.write("totdcwtrqy"); // 방류량 계
								pw.write("|^");
								pw.write("fishway"); // 방류량 어도
								pw.write("|^");
								pw.write("dvlpdcwtrqy"); // 방류량 소수력
								pw.write("|^");
								pw.write("spilldcwtrqy"); // 방류량 가동보
								pw.write("|^");
								pw.write("etcdcwtrqyone"); // 방류량 고정보
								pw.write("|^");
								pw.write("lowlevel"); // 수위 금일현재
								pw.write("|^");
								pw.write("rsvwtqy"); // 저수현황 현저수량
								pw.write("|^");
								pw.write("rsvwtrt"); // 저수현황 저수율
								pw.write("|^");
								pw.write("managewalrsvwtqy"); // 저수현황 관리수위 저수량
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

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);

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

						StringBuffer suge = new StringBuffer(" "); // 수계
						StringBuffer brrernm = new StringBuffer(" "); // 보이름
						StringBuffer rf = new StringBuffer(" "); // 강우량 금일
						StringBuffer prcptqy = new StringBuffer(" "); // 강우량 전일
						StringBuffer nowprcptqy = new StringBuffer(" "); // 누계
																			// 금년
						StringBuffer lastprcptqy = new StringBuffer(" "); // 누계
																			// 전년
						StringBuffer nyearprcptqy = new StringBuffer(" "); // 누계
																			// 예년
						StringBuffer inflowqy = new StringBuffer(" "); // 전입유입량
						StringBuffer totdcwtrqy = new StringBuffer(" "); // 방류량
																			// 계
						StringBuffer fishway = new StringBuffer(" "); // 방류량 어도
						StringBuffer dvlpdcwtrqy = new StringBuffer(" "); // 방류량
																			// 소수력
						StringBuffer spilldcwtrqy = new StringBuffer(" "); // 방류량
																			// 가동보
						StringBuffer etcdcwtrqyone = new StringBuffer(" "); // 방류량
																			// 고정보
						StringBuffer lowlevel = new StringBuffer(" "); // 수위
																		// 금일현재
						StringBuffer rsvwtqy = new StringBuffer(" "); // 저수현황
																		// 현저수량
						StringBuffer rsvwtrt = new StringBuffer(" "); // 저수현황
																		// 저수율
						StringBuffer managewalrsvwtqy = new StringBuffer(" "); // 저수현황
																				// 관리수위
																				// 저수량

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);

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

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(suge, keyname, "suge", item_obj);
										JsonParser.colWrite(brrernm, keyname, "brrernm", item_obj);
										JsonParser.colWrite(rf, keyname, "rf", item_obj);
										JsonParser.colWrite(prcptqy, keyname, "prcptqy", item_obj);
										JsonParser.colWrite(nowprcptqy, keyname, "nowprcptqy", item_obj);
										JsonParser.colWrite(lastprcptqy, keyname, "lastprcptqy", item_obj);
										JsonParser.colWrite(nyearprcptqy, keyname, "nyearprcptqy", item_obj);
										JsonParser.colWrite(inflowqy, keyname, "inflowqy", item_obj);
										JsonParser.colWrite(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
										JsonParser.colWrite(fishway, keyname, "fishway", item_obj);
										JsonParser.colWrite(dvlpdcwtrqy, keyname, "dvlpdcwtrqy", item_obj);
										JsonParser.colWrite(spilldcwtrqy, keyname, "spilldcwtrqy", item_obj);
										JsonParser.colWrite(etcdcwtrqyone, keyname, "etcdcwtrqyone", item_obj);
										JsonParser.colWrite(lowlevel, keyname, "lowlevel", item_obj);
										JsonParser.colWrite(rsvwtqy, keyname, "rsvwtqy", item_obj);
										JsonParser.colWrite(rsvwtrt, keyname, "rsvwtrt", item_obj);
										JsonParser.colWrite(managewalrsvwtqy, keyname, "managewalrsvwtqy", item_obj);

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
									resultSb.append(suge);
									resultSb.append("|^");
									resultSb.append(brrernm);
									resultSb.append("|^");
									resultSb.append(rf);
									resultSb.append("|^");
									resultSb.append(prcptqy);
									resultSb.append("|^");
									resultSb.append(nowprcptqy);
									resultSb.append("|^");
									resultSb.append(lastprcptqy);
									resultSb.append("|^");
									resultSb.append(nyearprcptqy);
									resultSb.append("|^");
									resultSb.append(inflowqy);
									resultSb.append("|^");
									resultSb.append(totdcwtrqy);
									resultSb.append("|^");
									resultSb.append(fishway);
									resultSb.append("|^");
									resultSb.append(dvlpdcwtrqy);
									resultSb.append("|^");
									resultSb.append(spilldcwtrqy);
									resultSb.append("|^");
									resultSb.append(etcdcwtrqyone);
									resultSb.append("|^");
									resultSb.append(lowlevel);
									resultSb.append("|^");
									resultSb.append(rsvwtqy);
									resultSb.append("|^");
									resultSb.append(rsvwtrt);
									resultSb.append("|^");
									resultSb.append(managewalrsvwtqy);
									resultSb.append(System.getProperty("line.separator"));

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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_11.dat", "WRI");

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

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(
						"tdate :" + args[0] + ": ldate :" + args[1] + ": vdate :" + args[2] + ": vtime :" + args[3]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
