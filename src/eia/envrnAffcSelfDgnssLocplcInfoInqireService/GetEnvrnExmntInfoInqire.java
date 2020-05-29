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

public class GetEnvrnExmntInfoInqire {

	// 자가진단 소재지 정보 서비스 - PNU,지번, 중심점, X좌표, Y좌표 정보를 조회하는 기능제공
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필수 파라미터는 지분의 1개
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("envrnAffcSelfDgnssLocplcInfoInqireService_getEnvrnExmntInfoInqire_url");
					String service_key = JsonParser
							.getProperty("envrnAffcSelfDgnssLocplcInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_52.dat");
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
					}*/

					// step 2. 전체 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, args[0]);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						throw new Exception();
					} else if ((resultCode.equals("00") && body.get("items") instanceof String)||(resultCode.equals("03"))) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
						
						String pnu = " "; // PNU
						String jibun = " "; // 지분
						String centerx = " "; // 좌표 X
						String centery = " "; // 좌표 Y

						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								pnu = JsonParser.colWrite_String(pnu, keyname, "pnu", items_jsonObject);
								jibun = JsonParser.colWrite_String(jibun, keyname, "jibun", items_jsonObject);
								centerx = JsonParser.colWrite_String(centerx, keyname, "centerx", items_jsonObject);
								centery = JsonParser.colWrite_String(centery, keyname, "centery", items_jsonObject);

							}
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(args[0]); 
								pw.write("|^");
								pw.write(resultCode); 
								pw.write("|^");
								pw.write(resultMsg); 
								pw.write("|^");
								pw.write(pnu); 
								pw.write("|^");
								pw.write(jibun); 
								pw.write("|^");
								pw.write(centerx); 
								pw.write("|^");
								pw.write(centery); 
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
									
									pnu = JsonParser.colWrite_String(pnu, keyname, "pnu", item_obj);
									jibun = JsonParser.colWrite_String(jibun, keyname, "jibun", item_obj);
									centerx = JsonParser.colWrite_String(centerx, keyname, "centerx", item_obj);
									centery = JsonParser.colWrite_String(centery, keyname, "centery", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(args[0]); 
									pw.write("|^");
									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(pnu); 
									pw.write("|^");
									pw.write(jibun); 
									pw.write("|^");
									pw.write(centerx); 
									pw.write("|^");
									pw.write(centery); 
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

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_52.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("buBun :" + args[0]);
			}



	}

}
