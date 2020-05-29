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

import common.JsonParser;
//import common.TransSftp;

public class Dailindwater {

	// 광역정수장 수질정보 조회 서비스 - 광역일일 공업용 수돗물 수질 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 요청 파라미터는 조회시작일(yyyyMMdd), 조회종료일(yyyyMMdd), 정수장 코드의 3개
				// 정수장 코드는 정수장 코드 조회 api에서 조회 가능
				if (args.length == 3) {

					if (args[1].length() == 8 && args[2].length() == 8) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("dailwater_Dailindwater_url");
						String service_key = JsonParser.getProperty("dailwater_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_19.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2]);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");

						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
							throw new Exception();
						} else {

							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; i++) {

							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							//공통 클래스로 로직 빼 놓음
							/*if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
							}*/

							JSONObject obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2]);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

							if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
								System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
								throw new Exception();
							} else if ((resultCode.equals("00") && body.get("items") instanceof String)||(resultCode.equals("03"))) {
								System.out.println("data not exist!!");
							} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
								
								String numOfRows_str = body.get("numOfRows").toString();

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {
									
									String mesurede = " "; // 측정시간
									String item1 = " "; // 수온 원수
									String item2 = " "; // 수온 침전수
									String item3 = " "; // pH(-) 원수
									String item4 = " "; // pH(-) 침전수
									String item5 = " "; // 탁도(NTU)
																				// 원수
									String item6 = " "; // 탁도(NTU)
																				// 침전수
									String item7 = " "; // 전기전도도(㎲/㎝)
																				// 원수
									String item8 = " "; // 전기전도도(㎲/㎝)
																				// 침전수
									String item9 = " "; // 알칼리도(mg/L)
																				// 원수
									String item10 = " "; // 알칼리도(mg/L)
																					// 침전수

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										mesurede = JsonParser.colWrite_String(mesurede, keyname, "mesurede", items_jsonObject);
										item1 = JsonParser.colWrite_String(item1, keyname, "item1", items_jsonObject);
										item2 = JsonParser.colWrite_String(item2, keyname, "item2", items_jsonObject);
										item3 = JsonParser.colWrite_String(item3, keyname, "item3", items_jsonObject);
										item4 = JsonParser.colWrite_String(item4, keyname, "item4", items_jsonObject);
										item5 = JsonParser.colWrite_String(item5, keyname, "item5", items_jsonObject);
										item6 = JsonParser.colWrite_String(item6, keyname, "item6", items_jsonObject);
										item7 = JsonParser.colWrite_String(item7, keyname, "item7", items_jsonObject);
										item8 = JsonParser.colWrite_String(item8, keyname, "item8", items_jsonObject);
										item9 = JsonParser.colWrite_String(item9, keyname, "item9", items_jsonObject);
										item10 = JsonParser.colWrite_String(item10, keyname, "item10", items_jsonObject);

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
										pw.write(mesurede);
										pw.write("|^");
										pw.write(item1);
										pw.write("|^");
										pw.write(item2);
										pw.write("|^");
										pw.write(item3);
										pw.write("|^");
										pw.write(item4);
										pw.write("|^");
										pw.write(item5);
										pw.write("|^");
										pw.write(item6);
										pw.write("|^");
										pw.write(item7);
										pw.write("|^");
										pw.write(item8);
										pw.write("|^");
										pw.write(item9);
										pw.write("|^");
										pw.write(item10);
										pw.write("|^");
										pw.write(numOfRows_str);
										pw.write("|^");
										pw.write(String.valueOf(i));
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String mesurede = " "; // 측정시간
										String item1 = " "; // 수온 원수
										String item2 = " "; // 수온 침전수
										String item3 = " "; // pH(-) 원수
										String item4 = " "; // pH(-) 침전수
										String item5 = " "; // 탁도(NTU)
																					// 원수
										String item6 = " "; // 탁도(NTU)
																					// 침전수
										String item7 = " "; // 전기전도도(㎲/㎝)
																					// 원수
										String item8 = " "; // 전기전도도(㎲/㎝)
																					// 침전수
										String item9 = " "; // 알칼리도(mg/L)
																					// 원수
										String item10 = " "; // 알칼리도(mg/L)
																						// 침전수

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											mesurede = JsonParser.colWrite_String(mesurede, keyname, "mesurede", item_obj);
											item1 = JsonParser.colWrite_String(item1, keyname, "item1", item_obj);
											item2 = JsonParser.colWrite_String(item2, keyname, "item2", item_obj);
											item3 = JsonParser.colWrite_String(item3, keyname, "item3", item_obj);
											item4 = JsonParser.colWrite_String(item4, keyname, "item4", item_obj);
											item5 = JsonParser.colWrite_String(item5, keyname, "item5", item_obj);
											item6 = JsonParser.colWrite_String(item6, keyname, "item6", item_obj);
											item7 = JsonParser.colWrite_String(item7, keyname, "item7", item_obj);
											item8 = JsonParser.colWrite_String(item8, keyname, "item8", item_obj);
											item9 = JsonParser.colWrite_String(item9, keyname, "item9", item_obj);
											item10 = JsonParser.colWrite_String(item10, keyname, "item10", item_obj);

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
											pw.write(mesurede);
											pw.write("|^");
											pw.write(item1);
											pw.write("|^");
											pw.write(item2);
											pw.write("|^");
											pw.write(item3);
											pw.write("|^");
											pw.write(item4);
											pw.write("|^");
											pw.write(item5);
											pw.write("|^");
											pw.write(item6);
											pw.write("|^");
											pw.write(item7);
											pw.write("|^");
											pw.write(item8);
											pw.write("|^");
											pw.write(item9);
											pw.write("|^");
											pw.write(item10);
											pw.write("|^");
											pw.write(numOfRows_str);
											pw.write("|^");
											pw.write(String.valueOf(i));
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

							System.out.println("진행도::::::" + i + "/" + pageCount);

							Thread.sleep(2000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_19.dat", "WRS");

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
				System.out.println("fcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
			}


	}

}
