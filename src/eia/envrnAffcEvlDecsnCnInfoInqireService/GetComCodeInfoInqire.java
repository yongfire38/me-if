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

public class GetComCodeInfoInqire {

	// 환경영향평가 결정내용정보 서비스 - 공통코드 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 요청 파라미터 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("envrnAffcEvlDecsnCnInfoInqireService_getComCodeInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDecsnCnInfoInqireService_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_48.dat");
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
					}*/

					// step 2. 전체 파싱
					JSONObject obj = JsonParser.parseWriJson_obj(service_url, service_key);
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

						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {
							
							String comCd = " "; // 공통코드
							String jongCd = " "; // 코드종류
							String cdNm = " "; // 코드명
							String gubunFl = " "; // 구분상태
							String sortSeq = " "; // 소팅 일련번호

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								comCd = JsonParser.colWrite_String(comCd, keyname, "comCd", items_jsonObject);
								jongCd = JsonParser.colWrite_String(jongCd, keyname, "jongCd", items_jsonObject);
								cdNm = JsonParser.colWrite_String(cdNm, keyname, "cdNm", items_jsonObject);
								gubunFl = JsonParser.colWrite_String(gubunFl, keyname, "gubunFl", items_jsonObject);
								sortSeq = JsonParser.colWrite_String(sortSeq, keyname, "sortSeq", items_jsonObject);
								
							}
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(resultCode); 
								pw.write("|^");
								pw.write(resultMsg); 
								pw.write("|^");
								pw.write(comCd); 
								pw.write("|^");
								pw.write(jongCd); 
								pw.write("|^");
								pw.write(cdNm); 
								pw.write("|^");
								pw.write(gubunFl); 
								pw.write("|^");
								pw.write(sortSeq); 
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}			

							

						} else if (items.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {
								
								String comCd = " "; // 공통코드
								String jongCd = " "; // 코드종류
								String cdNm = " "; // 코드명
								String gubunFl = " "; // 구분상태
								String sortSeq = " "; // 소팅 일련번호

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									comCd = JsonParser.colWrite_String(comCd, keyname, "comCd", item_obj);
									jongCd = JsonParser.colWrite_String(jongCd, keyname, "jongCd", item_obj);
									cdNm = JsonParser.colWrite_String(cdNm, keyname, "cdNm", item_obj);
									gubunFl = JsonParser.colWrite_String(gubunFl, keyname, "gubunFl", item_obj);
									sortSeq = JsonParser.colWrite_String(sortSeq, keyname, "sortSeq", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(comCd); 
									pw.write("|^");
									pw.write(jongCd); 
									pw.write("|^");
									pw.write(cdNm); 
									pw.write("|^");
									pw.write(gubunFl); 
									pw.write("|^");
									pw.write(sortSeq); 
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_48.dat", "EIA");

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
