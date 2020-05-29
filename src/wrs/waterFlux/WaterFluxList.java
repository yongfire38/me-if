package wrs.waterFlux;

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

public class WaterFluxList {

	// 실시간 수도정보 유량(시간) - 1시간 유량정보 조회
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
						String service_url = JsonParser.getProperty("waterFlux_waterFluxList_url");
						String service_key = JsonParser.getProperty("waterFlux_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_01.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWrsJson_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");

						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
							throw new Exception();
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
								json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
							}*/

							JSONObject obj = JsonParser.parseWrsJson_obj(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
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
									
									String no = " "; // 번호
									String occrrncDt = " "; // 발생일시
									String fcltyNm = " "; // 시설관리명
									String fcltyMngNo = " "; // 시설관리번호
									String dataVal = " "; // 유량
									String itemUnit = " "; // 측정단위
									String dataItemDesc = " "; // 자료
																						// 수집
																						// TAG
																						// 설명
									String dataItemTagsn = " "; // 태그SN
									String dataItemDiv = " "; // 데이터항목구분

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										no = JsonParser.colWrite_String(no, keyname, "no", items_jsonObject);
										occrrncDt = JsonParser.colWrite_String(occrrncDt, keyname, "occrrncDt", items_jsonObject);
										fcltyNm = JsonParser.colWrite_String(fcltyNm, keyname, "fcltyNm", items_jsonObject);
										fcltyMngNo = JsonParser.colWrite_String(fcltyMngNo, keyname, "fcltyMngNo", items_jsonObject);
										dataVal = JsonParser.colWrite_String(dataVal, keyname, "dataVal", items_jsonObject);
										itemUnit = JsonParser.colWrite_String(itemUnit, keyname, "itemUnit", items_jsonObject);
										dataItemDesc = JsonParser.colWrite_String(dataItemDesc, keyname, "dataItemDesc", items_jsonObject);
										dataItemTagsn = JsonParser.colWrite_String(dataItemTagsn, keyname, "dataItemTagsn", items_jsonObject);
										dataItemDiv = JsonParser.colWrite_String(dataItemDiv, keyname, "dataItemDiv", items_jsonObject);

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
										pw.write(args[3]);
										pw.write("|^");
										pw.write(no);
										pw.write("|^");
										pw.write(occrrncDt);
										pw.write("|^");
										pw.write(fcltyNm);
										pw.write("|^");
										pw.write(fcltyMngNo);
										pw.write("|^");
										pw.write(dataVal);
										pw.write("|^");
										pw.write(itemUnit);
										pw.write("|^");
										pw.write(dataItemDesc);
										pw.write("|^");
										pw.write(dataItemTagsn);
										pw.write("|^");
										pw.write(dataItemDiv);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String no = " "; // 번호
										String occrrncDt = " "; // 발생일시
										String fcltyNm = " "; // 시설관리명
										String fcltyMngNo = " "; // 시설관리번호
										String dataVal = " "; // 유량
										String itemUnit = " "; // 측정단위
										String dataItemDesc = " "; // 자료
																							// 수집
																							// TAG
																							// 설명
										String dataItemTagsn = " "; // 태그SN
										String dataItemDiv = " "; // 데이터항목구분

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											no = JsonParser.colWrite_String(no, keyname, "no", item_obj);
											occrrncDt = JsonParser.colWrite_String(occrrncDt, keyname, "occrrncDt", item_obj);
											fcltyNm = JsonParser.colWrite_String(fcltyNm, keyname, "fcltyNm", item_obj);
											fcltyMngNo = JsonParser.colWrite_String(fcltyMngNo, keyname, "fcltyMngNo", item_obj);
											dataVal = JsonParser.colWrite_String(dataVal, keyname, "dataVal", item_obj);
											itemUnit = JsonParser.colWrite_String(itemUnit, keyname, "itemUnit", item_obj);
											dataItemDesc = JsonParser.colWrite_String(dataItemDesc, keyname, "dataItemDesc", item_obj);
											dataItemTagsn = JsonParser.colWrite_String(dataItemTagsn, keyname, "dataItemTagsn", item_obj);
											dataItemDiv = JsonParser.colWrite_String(dataItemDiv, keyname, "dataItemDiv", item_obj);

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
											pw.write(args[3]);
											pw.write("|^");
											pw.write(no);
											pw.write("|^");
											pw.write(occrrncDt);
											pw.write("|^");
											pw.write(fcltyNm);
											pw.write("|^");
											pw.write(fcltyMngNo);
											pw.write("|^");
											pw.write(dataVal);
											pw.write("|^");
											pw.write(itemUnit);
											pw.write("|^");
											pw.write(dataItemDesc);
											pw.write("|^");
											pw.write(dataItemTagsn);
											pw.write("|^");
											pw.write(dataItemDiv);
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

							Thread.sleep(2000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_01.dat", "WRS");

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
				System.exit(-1);
			}


	}

}
