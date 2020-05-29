package eia.envrnAffcEvlDecsnCnInfoInqireService;

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

public class GetDecsnCnListInfoInqire {

	// 환경영향평가 결정내용정보 서비스 - 결정내용 목록 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 실행시 필수 매개변수 구분 코드
				if (args.length == 1) {

					if (args[0].equals("S") || args[0].equals("E")) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser
								.getProperty("envrnAffcEvlDecsnCnInfoInqireService_getDecsnCnListInfoInqire_url");
						String service_key = JsonParser.getProperty("envrnAffcEvlDecsnCnInfoInqireService_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_45.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json = "{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\"}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWrsJson_obj(service_url, service_key, String.valueOf(pageNo), args[0]);
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

						for (int i = 1; i <= pageCount; i++) {
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							//공통 클래스로 로직 빼 놓음
							/*if(json.indexOf("</") > -1){
								json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
							}*/

							JSONObject obj = JsonParser.parseWrsJson_obj(service_url, service_key, String.valueOf(i), args[0]);
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
								
								String numOfRows_str = body.get("numOfRows").toString().trim();
								String totalCount_str = body.get("totalCount").toString().trim();
								
								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {
									
									String rnum = " "; // 넘버링
									String resultCd = " "; // 결정내용코드
									String discOrganNm = " "; // 협의기관명
									String bizNm = " "; // 사업명
									String drfopTmdt = " "; // 공람기간

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										rnum = JsonParser.colWrite_String(rnum, keyname, "rnum", items_jsonObject);
										resultCd = JsonParser.colWrite_String(resultCd, keyname, "resultCd", items_jsonObject);
										discOrganNm = JsonParser.colWrite_String(discOrganNm, keyname, "discOrganNm", items_jsonObject);
										bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
										drfopTmdt = JsonParser.colWrite_String(drfopTmdt, keyname, "drfopTmdt", items_jsonObject);

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
										pw.write(numOfRows_str); 
										pw.write("|^");
										pw.write(Integer.toString(i)); 
										pw.write("|^");
										pw.write(totalCount_str); 
										pw.write("|^");
										pw.write(rnum); 
										pw.write("|^");
										pw.write(resultCd); 
										pw.write("|^");
										pw.write(discOrganNm); 
										pw.write("|^");
										pw.write(bizNm); 
										pw.write("|^");
										pw.write(drfopTmdt); 
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}			

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String rnum = " "; // 넘버링
										String resultCd = " "; // 결정내용코드
										String discOrganNm = " "; // 협의기관명
										String bizNm = " "; // 사업명
										String drfopTmdt = " "; // 공람기간

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											rnum = JsonParser.colWrite_String(rnum, keyname, "rnum", item_obj);
											resultCd = JsonParser.colWrite_String(resultCd, keyname, "resultCd", item_obj);
											discOrganNm = JsonParser.colWrite_String(discOrganNm, keyname, "discOrganNm", item_obj);
											bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
											drfopTmdt = JsonParser.colWrite_String(drfopTmdt, keyname, "drfopTmdt", item_obj);

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
											pw.write(numOfRows_str); 
											pw.write("|^");
											pw.write(Integer.toString(i)); 
											pw.write("|^");
											pw.write(totalCount_str); 
											pw.write("|^");
											pw.write(rnum); 
											pw.write("|^");
											pw.write(resultCd); 
											pw.write("|^");
											pw.write(discOrganNm); 
											pw.write("|^");
											pw.write(bizNm); 
											pw.write("|^");
											pw.write(drfopTmdt); 
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

							System.out.println("진행도::::::" + i + "/" + pageCount);

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_45.dat", "EIA");

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
				System.out.println("type :" + args[0]);
			}



	}

}
