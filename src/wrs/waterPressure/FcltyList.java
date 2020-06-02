package wrs.waterPressure;

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

public class FcltyList {

	// 실시간 수도정보 압력 - 취수장, 정수장, 가압장 코드 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 검색 코드 (1:취수장, 2:정수장, 3:가압장)

				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("waterPressure_fcltyList_url");
					String service_key = JsonParser.getProperty("waterPressure_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_04.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;
					
					json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					if(json.indexOf("</") > -1){
						json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
					}

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if((count_resultCode.equals("03"))){
						System.out.println("data not exist!!");
					} else if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						//throw new Exception();
					}  else {

						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {
						
						json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						
						//이 api는 전체가 다 조회되어야 하므로 한번이라도 비정상응답이 되면 익셉션 발생 처리
						//공통 클래스로 로직 빼 놓음
						// 2020.06.02 : 빈 Json을 리턴하도록 롤백
						if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
							System.out.println("공공데이터 서버 측 비 Json 응답");
							//throw new Exception();
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							//throw new Exception();
						} else if ((resultCode.equals("00") && body.get("items") instanceof String)||(resultCode.equals("03"))) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

							JSONObject items = (JSONObject) body.get("items");

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {
								
								String fcltyMngNm = " "; // 시설관리명
								String sujCode = " "; // 사업장코드

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									fcltyMngNm = JsonParser.colWrite_String(fcltyMngNm, keyname, "fcltyMngNm", items_jsonObject);
									sujCode = JsonParser.colWrite_String(sujCode, keyname, "sujCode", items_jsonObject);

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(args[0]);
									pw.write("|^");
									pw.write(fcltyMngNm);
									pw.write("|^");
									pw.write(sujCode);
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {
									
									String fcltyMngNm = " "; // 시설관리명
									String sujCode = " "; // 사업장코드

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										fcltyMngNm = JsonParser.colWrite_String(fcltyMngNm, keyname, "fcltyMngNm", item_obj);
										sujCode = JsonParser.colWrite_String(sujCode, keyname, "sujCode", item_obj);

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(fcltyMngNm);
										pw.write("|^");
										pw.write(sujCode);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								}

							}

						} else {
							System.out.println("parsing error!!");
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(2000);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_04.dat", "WRS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() + "fcltyDivCode :" + args[0]);
				System.exit(-1);
			}

	

	}

}
