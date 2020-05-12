package wrs.waterLevel;

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

public class WaterLevelList {

	// 실시간 수도정보 수위(시간) - 1시간 수위정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd),
				// 조회종료 시각
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 2 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("waterLevel_waterLevelList_url");
						String service_key = JsonParser.getProperty("waterLevel_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_05.dat");

						try {
							
							PrintWriter pw = new PrintWriter(
									new BufferedWriter(new FileWriter(file, true)));

							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
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

						if (!(count_resultCode.equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
						} else {

							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						StringBuffer resultSb = new StringBuffer("");

						StringBuffer no = new StringBuffer(" "); // 번호
						StringBuffer occrrncDt = new StringBuffer(" "); // 발생일시
						StringBuffer fcltyNm = new StringBuffer(" "); // 시설관리명
						StringBuffer fcltyMngNo = new StringBuffer(" "); // 시설관리번호
						StringBuffer dataVal = new StringBuffer(" "); // 수위
						StringBuffer itemUnit = new StringBuffer(" "); // 측정단위
						StringBuffer dataItemDesc = new StringBuffer(" "); // 자료
																			// 수집
																			// TAG
																			// 설명
						StringBuffer dataItemTagsn = new StringBuffer(" "); // 태그SN
						StringBuffer dataItemDiv = new StringBuffer(" "); // 데이터항목구분

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
							}

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

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

										JsonParser.colWrite(no, keyname, "no", items_jsonObject);
										JsonParser.colWrite(occrrncDt, keyname, "occrrncDt", items_jsonObject);
										JsonParser.colWrite(fcltyNm, keyname, "fcltyNm", items_jsonObject);
										JsonParser.colWrite(fcltyMngNo, keyname, "fcltyMngNo", items_jsonObject);
										JsonParser.colWrite(dataVal, keyname, "dataVal", items_jsonObject);
										JsonParser.colWrite(itemUnit, keyname, "itemUnit", items_jsonObject);
										JsonParser.colWrite(dataItemDesc, keyname, "dataItemDesc", items_jsonObject);
										JsonParser.colWrite(dataItemTagsn, keyname, "dataItemTagsn", items_jsonObject);
										JsonParser.colWrite(dataItemDiv, keyname, "dataItemDiv", items_jsonObject);
									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(args[2]);
									resultSb.append("|^");
									resultSb.append(args[3]);
									resultSb.append("|^");
									resultSb.append(no);
									resultSb.append("|^");
									resultSb.append(occrrncDt);
									resultSb.append("|^");
									resultSb.append(fcltyNm);
									resultSb.append("|^");
									resultSb.append(fcltyMngNo);
									resultSb.append("|^");
									resultSb.append(dataVal);
									resultSb.append("|^");
									resultSb.append(itemUnit);
									resultSb.append("|^");
									resultSb.append(dataItemDesc);
									resultSb.append("|^");
									resultSb.append(dataItemTagsn);
									resultSb.append("|^");
									resultSb.append(dataItemDiv);
									resultSb.append(System.getProperty("line.separator"));

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();

											JsonParser.colWrite(no, keyname, "no", item_obj);
											JsonParser.colWrite(occrrncDt, keyname, "occrrncDt", item_obj);
											JsonParser.colWrite(fcltyNm, keyname, "fcltyNm", item_obj);
											JsonParser.colWrite(fcltyMngNo, keyname, "fcltyMngNo", item_obj);
											JsonParser.colWrite(dataVal, keyname, "dataVal", item_obj);
											JsonParser.colWrite(itemUnit, keyname, "itemUnit", item_obj);
											JsonParser.colWrite(dataItemDesc, keyname, "dataItemDesc", item_obj);
											JsonParser.colWrite(dataItemTagsn, keyname, "dataItemTagsn", item_obj);
											JsonParser.colWrite(dataItemDiv, keyname, "dataItemDiv", item_obj);
										}

										// 한번에 문자열 합침
										resultSb.append(args[0]);
										resultSb.append("|^");
										resultSb.append(args[1]);
										resultSb.append("|^");
										resultSb.append(args[2]);
										resultSb.append("|^");
										resultSb.append(args[3]);
										resultSb.append("|^");
										resultSb.append(no);
										resultSb.append("|^");
										resultSb.append(occrrncDt);
										resultSb.append("|^");
										resultSb.append(fcltyNm);
										resultSb.append("|^");
										resultSb.append(fcltyMngNo);
										resultSb.append("|^");
										resultSb.append(dataVal);
										resultSb.append("|^");
										resultSb.append(itemUnit);
										resultSb.append("|^");
										resultSb.append(dataItemDesc);
										resultSb.append("|^");
										resultSb.append(dataItemTagsn);
										resultSb.append("|^");
										resultSb.append(dataItemDiv);
										resultSb.append(System.getProperty("line.separator"));

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

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.println(resultSb.toString());
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_05.dat", "WRS");

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
				System.out.println(
						"stDt :" + args[0] + ": stTm :" + args[1] + ": edDt :" + args[2] + ": edTm :" + args[3]);
			}



	}

}
