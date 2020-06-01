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

import common.JsonParser;
//import common.TransSftp;

public class Hour {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문현황정보(시간)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 댐코드 1개, 날짜를 yyyymmdd로 2개 받는다. 파라미터 유효성 체크는 파싱 때 체크
				if (args.length == 3) {

					if (args[1].length() == 8 && args[2].length() == 8) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("sluicePresentCondition_hour_url");
						String service_key = JsonParser.getProperty("sluicePresentCondition_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_05.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						String numberOfRows_str = "";
						
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

						if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
							throw new Exception();
						} else if (count_resultCode.equals("03")){
							pageCount = 1;
						} else {
							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();
							numberOfRows_str = Integer.toString(numOfRows);

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; ++i) {
							
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

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {
									
									String obsrdt = " "; // 일시
									String lowlevel = " "; // 댐수위
									String rf = " "; // 강우량
									String inflowqy = " "; // 유입량
									String totdcwtrqy = " "; // 총방류량
									String rsvwtqy = " "; // 저수량
									String rsvwtrt = " "; // 저수율

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										obsrdt = JsonParser.colWrite_String(obsrdt, keyname, "obsrdt", items_jsonObject);
										lowlevel = JsonParser.colWrite_String(lowlevel, keyname, "lowlevel", items_jsonObject);
										rf = JsonParser.colWrite_String(rf, keyname, "rf", items_jsonObject);
										inflowqy = JsonParser.colWrite_String(inflowqy, keyname, "inflowqy", items_jsonObject);
										totdcwtrqy = JsonParser.colWrite_String(totdcwtrqy, keyname, "totdcwtrqy", items_jsonObject);
										rsvwtqy = JsonParser.colWrite_String(rsvwtqy, keyname, "rsvwtqy", items_jsonObject);
										rsvwtrt = JsonParser.colWrite_String(rsvwtrt, keyname, "rsvwtrt", items_jsonObject);

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
										pw.write(obsrdt);
										pw.write("|^");
										pw.write(lowlevel);
										pw.write("|^");
										pw.write(rf);
										pw.write("|^");
										pw.write(inflowqy);
										pw.write("|^");
										pw.write(totdcwtrqy);
										pw.write("|^");
										pw.write(rsvwtqy);
										pw.write("|^");
										pw.write(rsvwtrt);
										pw.write("|^");
										pw.write(numberOfRows_str);
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
										
										String obsrdt = " "; // 일시
										String lowlevel = " "; // 댐수위
										String rf = " "; // 강우량
										String inflowqy = " "; // 유입량
										String totdcwtrqy = " "; // 총방류량
										String rsvwtqy = " "; // 저수량
										String rsvwtrt = " "; // 저수율

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											obsrdt = JsonParser.colWrite_String(obsrdt, keyname, "obsrdt", item_obj);
											lowlevel = JsonParser.colWrite_String(lowlevel, keyname, "lowlevel", item_obj);
											rf = JsonParser.colWrite_String(rf, keyname, "rf", item_obj);
											inflowqy = JsonParser.colWrite_String(inflowqy, keyname, "inflowqy", item_obj);
											totdcwtrqy = JsonParser.colWrite_String(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
											rsvwtqy = JsonParser.colWrite_String(rsvwtqy, keyname, "rsvwtqy", item_obj);
											rsvwtrt = JsonParser.colWrite_String(rsvwtrt, keyname, "rsvwtrt", item_obj);

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
											pw.write(obsrdt);
											pw.write("|^");
											pw.write(lowlevel);
											pw.write("|^");
											pw.write(rf);
											pw.write("|^");
											pw.write(inflowqy);
											pw.write("|^");
											pw.write(totdcwtrqy);
											pw.write("|^");
											pw.write(rsvwtqy);
											pw.write("|^");
											pw.write(rsvwtrt);
											pw.write("|^");
											pw.write(numberOfRows_str);
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

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_05.dat", "WRI");

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
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() + "damcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
				System.exit(-1);
			}



	}

}
