package eia.envrnAffcSelfDgnssLocplcInfoInqireService;

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

public class GetSelfDgnssLocplcInfoLegaldongAdstrdManageInfoInqire {

	// 자가진단 소재지 정보 서비스 - 자가진단 소재지 정보(법정동, 행정동) 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 요청 파라미터는 주소,타입의 2개
				if (args.length == 2) {

					if (args[0].equals("1") || args[0].equals("2") || args[0].equals("3")) {
						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty(
								"envrnAffcSelfDgnssLocplcInfoInqireService_getSelfDgnssLocplcInfoLegaldongAdstrdManageInfoInqire_url");
						String service_key = JsonParser
								.getProperty("envrnAffcSelfDgnssLocplcInfoInqireService_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_50.dat");
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						}*/

						// step 2. 전체 파싱

						JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, args[0], args[1]);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject header = (JSONObject) response.get("header");
						JSONObject body = (JSONObject) response.get("body");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
							System.out.println(
									"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							throw new Exception();
						} else if ((resultCode.equals("00") && body.get("items") instanceof String)||(resultCode.equals("03"))) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
							

							String sido = " "; // 시도
							String sgg = " "; // 시/군/구
							String emd = " "; // 읍/면/동
							String ri = " ";  // 리

							JSONObject items = (JSONObject) body.get("items");

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									sido = JsonParser.colWrite_String(sido, keyname, "sido", items_jsonObject);
									sgg = JsonParser.colWrite_String(sgg, keyname, "sgg", items_jsonObject);
									emd = JsonParser.colWrite_String(emd, keyname, "emd", items_jsonObject);
									ri = JsonParser.colWrite_String(ri, keyname, "ri", items_jsonObject);

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(args[0]); 
									pw.write("|^");
									pw.write(args[1]); 
									pw.write("|^");
									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(sido); 
									pw.write("|^");
									pw.write(sgg); 
									pw.write("|^");
									pw.write(emd); 
									pw.write("|^");
									pw.write(ri);  
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										sido = JsonParser.colWrite_String(sido, keyname, "sido", item_obj);
										sgg = JsonParser.colWrite_String(sgg, keyname, "sgg", item_obj);
										emd = JsonParser.colWrite_String(emd, keyname, "emd", item_obj);
										ri = JsonParser.colWrite_String(ri, keyname, "ri", item_obj);

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]); 
										pw.write("|^");
										pw.write(args[1]); 
										pw.write("|^");
										pw.write(resultCode); 
										pw.write("|^");
										pw.write(resultMsg); 
										pw.write("|^");
										pw.write(sido); 
										pw.write("|^");
										pw.write(sgg); 
										pw.write("|^");
										pw.write(emd); 
										pw.write("|^");
										pw.write(ri);  
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

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_50.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (args[0].equals("4")) {
						System.out.println("data not exist!!");
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
				System.out.println("addr :" + args[0] + ": type :" + args[1]);
				System.exit(-1);
			}



	}

}
