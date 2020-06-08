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

public class Excllcode {

	// 우량 관측정보 조회 서비스 - 우량관측소 코드 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 댐 코드
				// 댐코드는 수문제원현황 코드조회 api에서 조회
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("excllncobsrvt_excllcode_url");
					String service_key = JsonParser.getProperty("excllncobsrvt_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat");

					// step 2. 전체 파싱
					//String json = "";

					// 파라미터 1개만 받으므로 환경영향평가 쪽 메서드 이용
					//json = JsonParser.parseEiaJson(service_url, service_key, args[0]);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답, damcode :" + args[0]);
						json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
					}*/

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, args[0]);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						//throw new Exception();
					} else if (resultCode.equals("00") && body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
						
						JSONObject items = (JSONObject) body.get("items");

						// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
						if (items.get("item") instanceof JSONObject) {
							
							String excllncobsrvtcode = " "; // 우량관측소코드
							String obsrvtNm = " "; // 관측소이름

							JSONObject items_jsonObject = (JSONObject) items.get("item");

							Set<String> key = items_jsonObject.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								excllncobsrvtcode = JsonParser.colWrite_String(excllncobsrvtcode, keyname, "excllncobsrvtcode", items_jsonObject);
								obsrvtNm = JsonParser.colWrite_String(obsrvtNm, keyname, "obsrvtNm", items_jsonObject);

							}
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write(args[0]);
								pw.write("|^");
								pw.write(excllncobsrvtcode);
								pw.write("|^");
								pw.write(obsrvtNm);
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						} else if (items.get("item") instanceof JSONArray) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {
								
								String excllncobsrvtcode = " "; // 우량관측소코드
								String obsrvtNm = " "; // 관측소이름

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									excllncobsrvtcode = JsonParser.colWrite_String(excllncobsrvtcode, keyname, "excllncobsrvtcode", item_obj);
									obsrvtNm = JsonParser.colWrite_String(obsrvtNm, keyname, "obsrvtNm", item_obj);

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(args[0]);
									pw.write("|^");
									pw.write(excllncobsrvtcode);
									pw.write("|^");
									pw.write(obsrvtNm);
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

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_16.dat", "WRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", damcode :" + args[0]);
				System.exit(-1);
			}



	}

}
