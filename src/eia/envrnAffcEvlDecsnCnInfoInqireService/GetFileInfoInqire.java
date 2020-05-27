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
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class GetFileInfoInqire {

	// 환경영향평가 결정내용정보 서비스 - 결정내용 상세 정보를 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 실행시 결정내용 코드와 타입(1: 결정내용 파일정보 2: 초안공람 전략환경영향평가 초안파일 3: 초안공람
				// 환경영향평가
				// 초안파일) 2개 파라미터 필요
				if (args.length == 2) {

					if (args[1].equals("1") || args[1].equals("2") || args[1].equals("3")) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser
								.getProperty("envrnAffcEvlDecsnCnInfoInqireService_getFileInfoInqire_url");
						String service_key = JsonParser.getProperty("envrnAffcEvlDecsnCnInfoInqireService_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_47.dat");

						String json = "";

						json = JsonParser.parseEiaJson(service_url, service_key, args[0], args[1]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						}*/

						// step 2. 전체 파싱

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject header = (JSONObject) response.get("header");
						JSONObject body = (JSONObject) response.get("body");

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
								
								String fileSeq = " "; // 파일고유번호
								String fileVNm = " "; // 표출파일명
								String fileNm = " "; // 파일명
								String fileSize = " "; // 파일사이즈
								String fileExt = " "; // 파일 확장자
								String fileUrl = " "; // 파일 다운로드 경로

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									fileSeq = JsonParser.colWrite_String(fileSeq, keyname, "fileSeq", items_jsonObject);
									fileVNm = JsonParser.colWrite_String(fileVNm, keyname, "fileVNm", items_jsonObject);
									fileNm = JsonParser.colWrite_String(fileNm, keyname, "fileNm", items_jsonObject);
									fileSize = JsonParser.colWrite_String(fileSize, keyname, "fileSize", items_jsonObject);
									fileExt = JsonParser.colWrite_String(fileExt, keyname, "fileExt", items_jsonObject);
									fileUrl = JsonParser.colWrite_String(fileUrl, keyname, "fileUrl", items_jsonObject);

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
									pw.write(fileSeq); 
									pw.write("|^");
									pw.write(fileVNm); 
									pw.write("|^");
									pw.write(fileNm); 
									pw.write("|^");
									pw.write(fileSize); 
									pw.write("|^");
									pw.write(fileExt); 
									pw.write("|^");
									pw.write(fileUrl);  
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {
									
									String fileSeq = " "; // 파일고유번호
									String fileVNm = " "; // 표출파일명
									String fileNm = " "; // 파일명
									String fileSize = " "; // 파일사이즈
									String fileExt = " "; // 파일 확장자
									String fileUrl = " "; // 파일 다운로드 경로

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										fileSeq = JsonParser.colWrite_String(fileSeq, keyname, "fileSeq", item_obj);
										fileVNm = JsonParser.colWrite_String(fileVNm, keyname, "fileVNm", item_obj);
										fileNm = JsonParser.colWrite_String(fileNm, keyname, "fileNm", item_obj);
										fileSize = JsonParser.colWrite_String(fileSize, keyname, "fileSize", item_obj);
										fileExt = JsonParser.colWrite_String(fileExt, keyname, "fileExt", item_obj);
										fileUrl = JsonParser.colWrite_String(fileUrl, keyname, "fileUrl", item_obj);

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
										pw.write(fileSeq); 
										pw.write("|^");
										pw.write(fileVNm); 
										pw.write("|^");
										pw.write(fileNm); 
										pw.write("|^");
										pw.write(fileSize); 
										pw.write("|^");
										pw.write(fileExt); 
										pw.write("|^");
										pw.write(fileUrl);  
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_47.dat", "EIA");

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
				System.out.println("resultCd :" + args[0] + " : type :" + args[1]);
			}


	}

}
