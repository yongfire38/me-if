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

				Thread.sleep(3000);

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

						try {
							
							PrintWriter pw = new PrintWriter(
									new BufferedWriter(new FileWriter(file, true)));

							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						String json = "";

						json = JsonParser.parseEiaJson(service_url, service_key, args[0], args[1]);

						// step 2. 전체 파싱

						StringBuffer resultSb = new StringBuffer("");

						StringBuffer fileSeq = new StringBuffer(" "); // 파일고유번호
						StringBuffer fileVNm = new StringBuffer(" "); // 표출파일명
						StringBuffer fileNm = new StringBuffer(" "); // 파일명
						StringBuffer fileSize = new StringBuffer(" "); // 파일사이즈
						StringBuffer fileExt = new StringBuffer(" "); // 파일 확장자
						StringBuffer fileUrl = new StringBuffer(" "); // 파일 다운로드
																		// 경로

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject header = (JSONObject) response.get("header");
						JSONObject body = (JSONObject) response.get("body");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00") && body.get("items") instanceof String) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
							
							

							JSONObject items = (JSONObject) body.get("items");

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(fileSeq, keyname, "fileSeq", items_jsonObject);
									JsonParser.colWrite(fileVNm, keyname, "fileVNm", items_jsonObject);
									JsonParser.colWrite(fileNm, keyname, "fileNm", items_jsonObject);
									JsonParser.colWrite(fileSize, keyname, "fileSize", items_jsonObject);
									JsonParser.colWrite(fileExt, keyname, "fileExt", items_jsonObject);
									JsonParser.colWrite(fileUrl, keyname, "fileUrl", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(args[0]);
								resultSb.append("|^");
								resultSb.append(args[1]);
								resultSb.append("|^");
								resultSb.append(resultCode);
								resultSb.append("|^");
								resultSb.append(resultMsg);
								resultSb.append("|^");
								resultSb.append(fileSeq);
								resultSb.append("|^");
								resultSb.append(fileVNm);
								resultSb.append("|^");
								resultSb.append(fileNm);
								resultSb.append("|^");
								resultSb.append(fileSize);
								resultSb.append("|^");
								resultSb.append(fileExt);
								resultSb.append("|^");
								resultSb.append(fileUrl);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(fileSeq, keyname, "fileSeq", item_obj);
										JsonParser.colWrite(fileVNm, keyname, "fileVNm", item_obj);
										JsonParser.colWrite(fileNm, keyname, "fileNm", item_obj);
										JsonParser.colWrite(fileSize, keyname, "fileSize", item_obj);
										JsonParser.colWrite(fileExt, keyname, "fileExt", item_obj);
										JsonParser.colWrite(fileUrl, keyname, "fileUrl", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(resultCode);
									resultSb.append("|^");
									resultSb.append(resultMsg);
									resultSb.append("|^");
									resultSb.append(fileSeq);
									resultSb.append("|^");
									resultSb.append(fileVNm);
									resultSb.append("|^");
									resultSb.append(fileNm);
									resultSb.append("|^");
									resultSb.append(fileSize);
									resultSb.append("|^");
									resultSb.append(fileExt);
									resultSb.append("|^");
									resultSb.append(fileUrl);
									resultSb.append(System.getProperty("line.separator"));

								}

							} else {
								System.out.println("parsing error!!");
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(resultSb.toString());
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
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
