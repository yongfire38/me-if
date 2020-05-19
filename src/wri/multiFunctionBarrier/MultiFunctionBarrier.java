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


			try {

				

				// 필요한 파라미터는 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 8 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("multiFunctionBarrier_url");
						String service_key = JsonParser.getProperty("multiFunctionBarrier_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_11.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
						}

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

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
							}

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
									
									String suge = " "; // 수계
									String brrernm = " "; // 보이름
									String rf = " "; // 강우량 금일
									String prcptqy = " "; // 강우량 전일
									String nowprcptqy = " "; // 누계
																						// 금년
									String lastprcptqy = " "; // 누계
																						// 전년
									String nyearprcptqy = " "; // 누계
																						// 예년
									String inflowqy = " "; // 전입유입량
									String totdcwtrqy = " "; // 방류량
																						// 계
									String fishway = " "; // 방류량 어도
									String dvlpdcwtrqy = " "; // 방류량
																						// 소수력
									String spilldcwtrqy = " "; // 방류량
																						// 가동보
									String etcdcwtrqyone = " "; // 방류량
																						// 고정보
									String lowlevel = " "; // 수위
																					// 금일현재
									String rsvwtqy = " "; // 저수현황
																					// 현저수량
									String rsvwtrt = " "; // 저수현황
																					// 저수율
									String managewalrsvwtqy = " "; // 저수현황
																							// 관리수위
																							// 저수량

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										suge = JsonParser.colWrite_String(suge, keyname, "suge", item_obj);
										brrernm = JsonParser.colWrite_String(brrernm, keyname, "brrernm", item_obj);
										rf = JsonParser.colWrite_String(rf, keyname, "rf", item_obj);
										prcptqy = JsonParser.colWrite_String(prcptqy, keyname, "prcptqy", item_obj);
										nowprcptqy = JsonParser.colWrite_String(nowprcptqy, keyname, "nowprcptqy", item_obj);
										lastprcptqy = JsonParser.colWrite_String(lastprcptqy, keyname, "lastprcptqy", item_obj);
										nyearprcptqy = JsonParser.colWrite_String(nyearprcptqy, keyname, "nyearprcptqy", item_obj);
										inflowqy = JsonParser.colWrite_String(inflowqy, keyname, "inflowqy", item_obj);
										totdcwtrqy = JsonParser.colWrite_String(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
										fishway = JsonParser.colWrite_String(fishway, keyname, "fishway", item_obj);
										dvlpdcwtrqy = JsonParser.colWrite_String(dvlpdcwtrqy, keyname, "dvlpdcwtrqy", item_obj);
										spilldcwtrqy = JsonParser.colWrite_String(spilldcwtrqy, keyname, "spilldcwtrqy", item_obj);
										etcdcwtrqyone = JsonParser.colWrite_String(etcdcwtrqyone, keyname, "etcdcwtrqyone", item_obj);
										lowlevel = JsonParser.colWrite_String(lowlevel, keyname, "lowlevel", item_obj);
										rsvwtqy = JsonParser.colWrite_String(rsvwtqy, keyname, "rsvwtqy", item_obj);
										rsvwtrt = JsonParser.colWrite_String(rsvwtrt, keyname, "rsvwtrt", item_obj);
										managewalrsvwtqy = JsonParser.colWrite_String(managewalrsvwtqy, keyname, "managewalrsvwtqy", item_obj);

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(args[2]);
										pw.write("|^");
										pw.write(args[3]);
										pw.write("|^");
										pw.write(suge);
										pw.write("|^");
										pw.write(brrernm);
										pw.write("|^");
										pw.write(rf);
										pw.write("|^");
										pw.write(prcptqy);
										pw.write("|^");
										pw.write(nowprcptqy);
										pw.write("|^");
										pw.write(lastprcptqy);
										pw.write("|^");
										pw.write(nyearprcptqy);
										pw.write("|^");
										pw.write(inflowqy);
										pw.write("|^");
										pw.write(totdcwtrqy);
										pw.write("|^");
										pw.write(fishway);
										pw.write("|^");
										pw.write(dvlpdcwtrqy);
										pw.write("|^");
										pw.write(spilldcwtrqy);
										pw.write("|^");
										pw.write(etcdcwtrqyone);
										pw.write("|^");
										pw.write(lowlevel);
										pw.write("|^");
										pw.write(rsvwtqy);
										pw.write("|^");
										pw.write(rsvwtrt);
										pw.write("|^");
										pw.write(managewalrsvwtqy);
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

							System.out.println("진행도::::::" + i + "/" + pageCount);

							//Thread.sleep(1000);

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


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(
						"tdate :" + args[0] + ": ldate :" + args[1] + ": vdate :" + args[2] + ": vtime :" + args[3]);
			}



	}

}
