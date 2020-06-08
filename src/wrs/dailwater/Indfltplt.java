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

public class Indfltplt {

	// 광역정수장 수질정보 조회 서비스 - 공업용 정수장 코드 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("dailwater_Indfltplt_url");
					String service_key = JsonParser.getProperty("dailwater_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_25.dat");	

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					//String json = "";

					int pageNo = 0;
					int pageCount = 0;
					
					//json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답");
						json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
					}*/

					JSONObject count_obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(pageNo));
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if((count_resultCode.equals("03"))){
						System.out.println("data not exist!!");
					} else if (count_resultCode.equals("00") && count_body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						//throw new Exception();
					} else {

						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;

					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {
						
						//json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						// 2020.06.02 : 빈 Json을 리턴하도록 롤백
						// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
						/*if(json.indexOf("</") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(i));
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							//throw new Exception();
						} else if (resultCode.equals("00") && body.get("items") instanceof String) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
							
							String numOfRows_str = body.get("numOfRows").toString();

							JSONObject items = (JSONObject) body.get("items");

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {
								
								String fltplt = " "; // 정수장코드
								String fltpltnm = " "; // 정수장명

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									fltplt = JsonParser.colWrite_String(fltplt, keyname, "fltplt", items_jsonObject);
									fltpltnm = JsonParser.colWrite_String(fltpltnm, keyname, "fltpltnm", items_jsonObject);

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(fltplt);
									pw.write("|^");
									pw.write(fltpltnm);
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
									
									String fltplt = " "; // 정수장코드
									String fltpltnm = " "; // 정수장명

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										fltplt = JsonParser.colWrite_String(fltplt, keyname, "fltplt", item_obj);
										fltpltnm = JsonParser.colWrite_String(fltpltnm, keyname, "fltpltnm", item_obj);

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(fltplt);
										pw.write("|^");
										pw.write(fltpltnm);
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

							}

						} else if (resultCode.equals("03")) {
							System.out.println("data not exist!!");
						} else {
							System.out.println("parsing error!!");
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(2000);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_25.dat", "WRS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName());
				System.exit(-1);
			}



	}

}
