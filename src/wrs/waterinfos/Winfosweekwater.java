package wrs.waterinfos;

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

public class Winfosweekwater {

	// 지방정수장 수질정보 조회 서비스 - 지방상수도수질(주간)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 요청 파라미터는 조회 시작일과 조회 종료일의 2개
				if (args.length == 2) {

					if (args[0].length() == 8 && args[1].length() == 8) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("waterinfos_Winfosweekwater_url");
						String service_key = JsonParser.getProperty("waterinfos_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_13.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;

						// 기존 메서드 이용 가능
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1]);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");

						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
							throw new Exception();
						} else if (count_resultCode.equals("03")){
							pageCount = 1;
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

							JSONObject obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(i), args[0],
									args[1]);
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
									
									String sgcnm = " "; // 지자체명
									String sitenm = " "; // 정수장명
									String cltdt = " "; // 측정일자
									String data1 = " "; // 일반세균
									String data2 = " "; // 총대장균군
									String data3 = " "; // 대장균/분원성대장균
									String data4 = " "; // 암모니아성질소
									String data5 = " "; // 질산성질소
									String data6 = " "; // 과망간산칼륨소비량
									String data7 = " "; // 증발잔류물

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										sgcnm = JsonParser.colWrite_String(sgcnm, keyname, "sgcnm", items_jsonObject);
										sitenm = JsonParser.colWrite_String(sitenm, keyname, "sitenm", items_jsonObject);
										cltdt = JsonParser.colWrite_String(cltdt, keyname, "cltdt", items_jsonObject);
										data1 = JsonParser.colWrite_String(data1, keyname, "data1", items_jsonObject);
										data2 = JsonParser.colWrite_String(data2, keyname, "data2", items_jsonObject);
										data3 = JsonParser.colWrite_String(data3, keyname, "data3", items_jsonObject);
										data4 = JsonParser.colWrite_String(data4, keyname, "data4", items_jsonObject);
										data5 = JsonParser.colWrite_String(data5, keyname, "data5", items_jsonObject);
										data6 = JsonParser.colWrite_String(data6, keyname, "data6", items_jsonObject);
										data7 = JsonParser.colWrite_String(data7, keyname, "data7", items_jsonObject);

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(sgcnm);
										pw.write("|^");
										pw.write(sitenm);
										pw.write("|^");
										pw.write(cltdt);
										pw.write("|^");
										pw.write(data1);
										pw.write("|^");
										pw.write(data2);
										pw.write("|^");
										pw.write(data3);
										pw.write("|^");
										pw.write(data4);
										pw.write("|^");
										pw.write(data5);
										pw.write("|^");
										pw.write(data6);
										pw.write("|^");
										pw.write(data7);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String sgcnm = " "; // 지자체명
										String sitenm = " "; // 정수장명
										String cltdt = " "; // 측정일자
										String data1 = " "; // 일반세균
										String data2 = " "; // 총대장균군
										String data3 = " "; // 대장균/분원성대장균
										String data4 = " "; // 암모니아성질소
										String data5 = " "; // 질산성질소
										String data6 = " "; // 과망간산칼륨소비량
										String data7 = " "; // 증발잔류물

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											sgcnm = JsonParser.colWrite_String(sgcnm, keyname, "sgcnm", item_obj);
											sitenm = JsonParser.colWrite_String(sitenm, keyname, "sitenm", item_obj);
											cltdt = JsonParser.colWrite_String(cltdt, keyname, "cltdt", item_obj);
											data1 = JsonParser.colWrite_String(data1, keyname, "data1", item_obj);
											data2 = JsonParser.colWrite_String(data2, keyname, "data2", item_obj);
											data3 = JsonParser.colWrite_String(data3, keyname, "data3", item_obj);
											data4 = JsonParser.colWrite_String(data4, keyname, "data4", item_obj);
											data5 = JsonParser.colWrite_String(data5, keyname, "data5", item_obj);
											data6 = JsonParser.colWrite_String(data6, keyname, "data6", item_obj);
											data7 = JsonParser.colWrite_String(data7, keyname, "data7", item_obj);

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(args[0]);
											pw.write("|^");
											pw.write(args[1]);
											pw.write("|^");
											pw.write(sgcnm);
											pw.write("|^");
											pw.write(sitenm);
											pw.write("|^");
											pw.write(cltdt);
											pw.write("|^");
											pw.write(data1);
											pw.write("|^");
											pw.write(data2);
											pw.write("|^");
											pw.write(data3);
											pw.write("|^");
											pw.write(data4);
											pw.write("|^");
											pw.write(data5);
											pw.write("|^");
											pw.write(data6);
											pw.write("|^");
											pw.write(data7);
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_13.dat", "WRS");

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
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() + "stdt :" + args[0] + ": eddt :" + args[1]);
				System.exit(-1);
			}


	}

}
