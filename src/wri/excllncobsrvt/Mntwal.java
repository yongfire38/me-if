package wri.excllncobsrvt;

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

public class Mntwal {

	// 우량 관측정보 조회 서비스 - 수위 분강우량 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 조회시작일 (yyyyMMdd), 조회시작시각 2자리, 조회 종료일 (yyyyMMdd),
				// 조회종료 시각
				// 2자리, 댐 코드, 수위관측소 코드
				// 댐코드는 수문제원현황 코드조회 api에서 조회, 수위관측소 코드는 수위관측소 코드 조회 api에서 조회

				// 추가 파라미터 (선택) : 파일 경로, 있으면 파라미터에 입력된 경로대로 파일 작성, 없으면 기존 로직 그대로 정해진 경로에 파일 작성
				if (args.length == 6 || args.length == 7) {

					if (args[0].length() == 8 && args[1].length() == 2 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("excllncobsrvt_mntwal_url");
						String service_key = JsonParser.getProperty("excllncobsrvt_service_key");
						
						File file = null;

						// step 1.파일의 작성
						// 파일 경로 파라미터 유무에 따라 달라짐 
						if(args.length == 6){
							 file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_14.dat");
						} else if(args.length == 7){
							file = new File(args[6]);
						}

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWriJson_wal_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3], args[4], args[5]);
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

							JSONObject obj = JsonParser.parseWriJson_wal_obj(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3], args[4], args[5]);
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
									
									String no = " "; // 순번
									String obsrdtmnt = " "; // 시간
									String flux = " "; // 수위
									String wal = " "; // 우량

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										no = JsonParser.colWrite_String(no, keyname, "no", items_jsonObject);
										obsrdtmnt = JsonParser.colWrite_String(obsrdtmnt, keyname, "obsrdtmnt", items_jsonObject);
										flux = JsonParser.colWrite_String(flux, keyname, "flux", items_jsonObject);
										wal = JsonParser.colWrite_String(wal, keyname, "wal", items_jsonObject);

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
										pw.write(args[4]);
										pw.write("|^");
										pw.write(args[5]);
										pw.write("|^");
										pw.write(no);
										pw.write("|^");
										pw.write(obsrdtmnt);
										pw.write("|^");
										pw.write(flux);
										pw.write("|^");
										pw.write(wal);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String no = " "; // 순번
										String obsrdtmnt = " "; // 시간
										String flux = " "; // 수위
										String wal = " "; // 우량

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											no = JsonParser.colWrite_String(no, keyname, "no", item_obj);
											obsrdtmnt = JsonParser.colWrite_String(obsrdtmnt, keyname, "obsrdtmnt", item_obj);
											flux = JsonParser.colWrite_String(flux, keyname, "flux", item_obj);
											wal = JsonParser.colWrite_String(wal, keyname, "wal", item_obj);

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
											pw.write(args[4]);
											pw.write("|^");
											pw.write(args[5]);
											pw.write("|^");
											pw.write(no);
											pw.write("|^");
											pw.write(obsrdtmnt);
											pw.write("|^");
											pw.write(flux);
											pw.write("|^");
											pw.write(wal);
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

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_14.dat", "WRI");

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
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() + "sdate :" + args[0] + ": stime :" + args[1] + ": edate :" + args[2] + ": etime :"
						+ args[3] + ": damcode :" + args[4] + ": walcode :" + args[5]);
				System.exit(-1);
			}


	}

}
