package wri.excllncobsrvt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class Hourrf_test {

	// 우량 관측정보 조회 서비스 - 우량 시강우량 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd),
				// 조회종료 시각
				// 2자리, 댐 코드, 우량관측소 코드
				// 댐코드는 수문제원현황 코드조회 api에서 조회, 우량관측소 코드는 우량관측소 코드 조회 api에서 조회

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("excllncobsrvt_hourrf_url");
						String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

						// step 1.파일의 작성
						File file = new File("TIF_WRI_13_2101210.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 1으로 파싱
						String json = "";

						int pageNo = 1;
						int pageCount = 0;
						
						List<String>damCodeList = new ArrayList<String>();
						
						//damCodeList.add("2403201");
						//damCodeList.add("2011602");
						//damCodeList.add("1007601");
						//damCodeList.add("3012601");
						//damCodeList.add("1001210");
						//damCodeList.add("2009602");
						//damCodeList.add("2503220");
						//damCodeList.add("1021701");
						//damCodeList.add("2008101");
						//damCodeList.add("2009601");
						//damCodeList.add("2022510");
						//damCodeList.add("2018110");
						//damCodeList.add("1302210");
						//damCodeList.add("2014601");
						//damCodeList.add("2201231");
						//damCodeList.add("2201230");
						//damCodeList.add("3008110");
						//damCodeList.add("3008611");
						//damCodeList.add("2021110");
						//damCodeList.add("3012602");
						//damCodeList.add("3203110");
						//damCodeList.add("3303110");
						//damCodeList.add("2201220");
						//damCodeList.add("2007601");
						//damCodeList.add("2301210");
						//damCodeList.add("4001110");
						//damCodeList.add("3010601");
						//damCodeList.add("1012110");
						//damCodeList.add("4105210");
						//damCodeList.add("5004601");
						damCodeList.add("2101210");
						/*damCodeList.add("2001110");
						damCodeList.add("2001611");
						damCodeList.add("1007602");
						damCodeList.add("2503210");
						damCodeList.add("2012210");
						damCodeList.add("3001110");
						damCodeList.add("2021210");
						damCodeList.add("1007603");
						damCodeList.add("2002110");
						damCodeList.add("2002610");
						damCodeList.add("5101110");
						damCodeList.add("4007110");
						damCodeList.add("4104610");
						damCodeList.add("4204612");
						damCodeList.add("5004602");
						damCodeList.add("2017601");
						damCodeList.add("1003110");
						damCodeList.add("1003611");
						damCodeList.add("2011601");
						damCodeList.add("5002201");
						damCodeList.add("1009710");
						damCodeList.add("2015110");
						damCodeList.add("2018611");
						damCodeList.add("2014602");
						damCodeList.add("1006110");*/

						List<String>excllCodeList = new ArrayList<String>();
						
						excllCodeList.add("1001434");
						excllCodeList.add("1002446");
						excllCodeList.add("1001436");
						excllCodeList.add("1002448");
						excllCodeList.add("1001430");
						excllCodeList.add("1002442");
						excllCodeList.add("1004424");
						excllCodeList.add("1003412");
						excllCodeList.add("4007450");
						excllCodeList.add("1002444");
						excllCodeList.add("1001432");
						excllCodeList.add("1004422");
						excllCodeList.add("2018440");
						excllCodeList.add("3007487");
						excllCodeList.add("1001438");
						excllCodeList.add("3008450");
						excllCodeList.add("9000042");
						excllCodeList.add("9000040");
						excllCodeList.add("9000005");
						excllCodeList.add("1004420");
						excllCodeList.add("9000006");
						excllCodeList.add("9000168");
						excllCodeList.add("9000047");
						excllCodeList.add("1002440");
						excllCodeList.add("9000004");
						excllCodeList.add("9000045");
						excllCodeList.add("9000167");
						excllCodeList.add("2018430");
						excllCodeList.add("1001422");
						excllCodeList.add("1002434");
						excllCodeList.add("4009460");
						excllCodeList.add("1002436");
						excllCodeList.add("1001420");
						excllCodeList.add("1002432");
						excllCodeList.add("4105402");
						excllCodeList.add("3005430");
						excllCodeList.add("3003450");
						excllCodeList.add("1002438");
						excllCodeList.add("1001426");
						excllCodeList.add("1004418");
						excllCodeList.add("9000031");
						excllCodeList.add("9000234");
						excllCodeList.add("9000232");
						excllCodeList.add("2018435");
						excllCodeList.add("9000233");
						excllCodeList.add("9000032");
						excllCodeList.add("4007470");
						excllCodeList.add("1001456");
						excllCodeList.add("1003438");
						excllCodeList.add("1001458");
						excllCodeList.add("1003436");
						excllCodeList.add("1001452");
						excllCodeList.add("4007474");
						excllCodeList.add("2018460");
						excllCodeList.add("1003434");
						excllCodeList.add("4007472");
						excllCodeList.add("9000018");
						excllCodeList.add("9000019");
						excllCodeList.add("1003432");
						excllCodeList.add("3008430");
						excllCodeList.add("3005440");
						excllCodeList.add("9000140");
						excllCodeList.add("3004430");
						excllCodeList.add("9000180");
						excllCodeList.add("1003430");
						excllCodeList.add("1001450");
						excllCodeList.add("2018425");
						excllCodeList.add("1003426");
						excllCodeList.add("1001444");
						excllCodeList.add("1003424");
						excllCodeList.add("2018455");
						excllCodeList.add("1001446");
						excllCodeList.add("1003422");
						excllCodeList.add("1001440");
						excllCodeList.add("9000007");
						excllCodeList.add("1003420");
						excllCodeList.add("1001442");
						excllCodeList.add("3007475");
						excllCodeList.add("3004440");
						excllCodeList.add("3007470");
						excllCodeList.add("1001448");
						excllCodeList.add("3008440");
						excllCodeList.add("1003428");
						excllCodeList.add("3003431");
						excllCodeList.add("3003430");
						excllCodeList.add("9000016");
						excllCodeList.add("9000017");
						
						
						for(int i = 0; i <= damCodeList.size(); i++){
							
							
							
							for(int r = 0; r <= excllCodeList.size(); r++){
								
								System.out.println("damCode::"+damCodeList.get(i)+"::::::"+i);
								
								System.out.println("excllCodeList::"+excllCodeList.get(r)+"::::::"+r);
								
								json = JsonParser.parseWriJson_excll(service_url, service_key, String.valueOf(pageNo), "20190101",
										"00", "20191231", "24", damCodeList.get(i), excllCodeList.get(r));
								
								//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
								//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
								/*if(json.indexOf("</") > -1){
									json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
								}*/

								JSONParser count_parser = new JSONParser();
								JSONObject count_obj = (JSONObject) count_parser.parse(json);
								JSONObject count_response = (JSONObject) count_obj.get("response");

								JSONObject count_body = (JSONObject) count_response.get("body");
								JSONObject count_header = (JSONObject) count_response.get("header");

								String count_resultCode = count_header.get("resultCode").toString().trim();
								String count_resultMsg = count_header.get("resultMsg").toString().trim();

								if (!(count_resultCode.equals("00"))) {
									System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
											+ count_resultMsg);
								} else {

									int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
									int totalCount = ((Long) count_body.get("totalCount")).intValue();

									pageCount = (totalCount / numOfRows) + 1;

								}

								// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

								for (int q = 1; q <= pageCount; q++) {

									json = JsonParser.parseWriJson_excll(service_url, service_key, String.valueOf(q), "20190101",
											"00", "20191231", "24", damCodeList.get(i), excllCodeList.get(r));
									
									//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
									//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
									/*if(json.indexOf("</") > -1){
										json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
									}*/

									JSONParser parser = new JSONParser();
									JSONObject obj = (JSONObject) parser.parse(json);
									JSONObject response = (JSONObject) obj.get("response");

									JSONObject body = (JSONObject) response.get("body");
									JSONObject header = (JSONObject) response.get("header");

									String resultCode = header.get("resultCode").toString().trim();
									String resultMsg = header.get("resultMsg").toString().trim();

									if (!(resultCode.equals("00"))) {
										System.out.println(
												"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
									} else if (resultCode.equals("00") && body.get("items") instanceof String) {
										System.out.println("data not exist!!");
									} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

										JSONObject items = (JSONObject) body.get("items");

										// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
										if (items.get("item") instanceof JSONObject) {
											
											String no = " "; // 순번
											String obsrdt = " "; // 시간
											String hourrf = " "; // 시강우량
											String acmtlrf = " "; // 누적우량

											JSONObject items_jsonObject = (JSONObject) items.get("item");

											Set<String> key = items_jsonObject.keySet();

											Iterator<String> iter = key.iterator();

											while (iter.hasNext()) {

												String keyname = iter.next();
												
												no = JsonParser.colWrite_String(no, keyname, "no", items_jsonObject);
												obsrdt = JsonParser.colWrite_String(obsrdt, keyname, "obsrdt", items_jsonObject);
												hourrf = JsonParser.colWrite_String(hourrf, keyname, "hourrf", items_jsonObject);
												acmtlrf = JsonParser.colWrite_String(acmtlrf, keyname, "acmtlrf", items_jsonObject);

											}
											
											// step 4. 파일에 쓰기
											try {
												PrintWriter pw = new PrintWriter(
														new BufferedWriter(new FileWriter(file, true)));

												pw.write("20190101");
												pw.write("|^");
												pw.write("00");
												pw.write("|^");
												pw.write("20191231");
												pw.write("|^");
												pw.write("24");
												pw.write("|^");
												pw.write(damCodeList.get(i));
												pw.write("|^");
												pw.write(excllCodeList.get(r));
												pw.write("|^");
												pw.write(no);
												pw.write("|^");
												pw.write(obsrdt);
												pw.write("|^");
												pw.write(hourrf);
												pw.write("|^");
												pw.write(acmtlrf);
												pw.println();
												pw.flush();
												pw.close();

											} catch (IOException e) {
												e.printStackTrace();
											}

										} else if (items.get("item") instanceof JSONArray) {

											JSONArray items_jsonArray = (JSONArray) items.get("item");

											for (int x = 0; x < items_jsonArray.size(); x++) {
												
												String no = " "; // 순번
												String obsrdt = " "; // 시간
												String hourrf = " "; // 시강우량
												String acmtlrf = " "; // 누적우량

												JSONObject item_obj = (JSONObject) items_jsonArray.get(x);

												Set<String> key = item_obj.keySet();

												Iterator<String> iter = key.iterator();

												while (iter.hasNext()) {

													String keyname = iter.next();
													
													no = JsonParser.colWrite_String(no, keyname, "no", item_obj);
													obsrdt = JsonParser.colWrite_String(obsrdt, keyname, "obsrdt", item_obj);
													hourrf = JsonParser.colWrite_String(hourrf, keyname, "hourrf", item_obj);
													acmtlrf = JsonParser.colWrite_String(acmtlrf, keyname, "acmtlrf", item_obj);

												}

												// step 4. 파일에 쓰기
												try {
													PrintWriter pw = new PrintWriter(
															new BufferedWriter(new FileWriter(file, true)));

													pw.write("20190101");
													pw.write("|^");
													pw.write("00");
													pw.write("|^");
													pw.write("20191231");
													pw.write("|^");
													pw.write("24");
													pw.write("|^");
													pw.write(damCodeList.get(i));
													pw.write("|^");
													pw.write(excllCodeList.get(r));
													pw.write("|^");
													pw.write(no);
													pw.write("|^");
													pw.write(obsrdt);
													pw.write("|^");
													pw.write(hourrf);
													pw.write("|^");
													pw.write(acmtlrf);
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

									} else {
										System.out.println("parsing error!!");
									}

									System.out.println("진행도::::::" + q + "/" + pageCount);

									//Thread.sleep(1000);

								}
		
								
							}
							
						}


						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_13.dat", "WRI");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");


			} catch (Exception e) {
				e.printStackTrace();
				/*System.out.println("sdate :" + args[0] + ": stime :" + args[1] + ": edate :" + args[2] + ": etime :"
						+ args[3] + ": damcode :" + args[4] + ": excll :" + args[5]);*/
				System.exit(-1);
			}



	}

}
