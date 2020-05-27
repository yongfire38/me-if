package pri.waterquality;

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

public class GetRadioActiveMaterList {

	// 수질정보 DB 서비스 - 방사성물질측정망 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음(전체 데이터 수가 많지 않으므로..)
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("PRI_WaterQualityService_getRadioActiveMaterList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_04.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"getRadioActiveMaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}*/

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					JSONObject count_getRadioActiveMaterList = (JSONObject) count_obj.get("getRadioActiveMaterList");

					JSONObject count_header = (JSONObject) count_getRadioActiveMaterList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_getRadioActiveMaterList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getRadioActiveMaterList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;

					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"getRadioActiveMaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}*/

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getRadioActiveMaterList = (JSONObject) obj.get("getRadioActiveMaterList");

						JSONObject header = (JSONObject) getRadioActiveMaterList.get("header");
						
						String resultCode_col = " "; // 결과코드
						String resultMsg_col = " "; // 결과메시지

						resultCode_col = header.get("code").toString().trim(); // 결과
																						// 코드
						resultMsg_col = header.get("message").toString().trim(); // 결과
																						// 메시지

						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {

							String numOfRows = " "; // 한 페이지 결과 수
							String pageNo_str = " "; // 페이지 번호
							String totalCount = " "; // 전체 결과 수
							
							numOfRows = getRadioActiveMaterList.get("numOfRows").toString().trim();

							pageNo_str = String.valueOf(i).trim();

							totalCount = getRadioActiveMaterList.get("totalCount").toString().trim();

							JSONArray items = (JSONArray) getRadioActiveMaterList.get("item");

							for (int r = 0; r < items.size(); r++) {
								
								String rn = " "; // 행번호
								String ptNo = " "; // 조사지점코드
								String ptNm = " "; // 조사지점명
								String wmcymd = " "; // 채취일
								String act1 = " "; // 측정값 Cs-134(세슘)
								String act2 = " "; // 측정값 Cs-137(세슘)
								String act3 = " "; // 측정값 I-131(요드)

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									rn = JsonParser.colWrite_String(rn, keyname, "RN", item);
									ptNo = JsonParser.colWrite_String(ptNo, keyname, "PT_NO", item);
									ptNm = JsonParser.colWrite_String(ptNm, keyname, "PT_NM", item);
									wmcymd = JsonParser.colWrite_String(wmcymd, keyname, "WMCYMD", item);
									act1 = JsonParser.colWrite_String(act1, keyname, "ACT1", item);
									act2 = JsonParser.colWrite_String(act2, keyname, "ACT2", item);
									act3 = JsonParser.colWrite_String(act3, keyname, "ACT3", item);
									numOfRows = JsonParser.colWrite_String(numOfRows, keyname, "numOfRows", item);
									pageNo_str = JsonParser.colWrite_String(pageNo_str, keyname, "pageNo", item);
									totalCount = JsonParser.colWrite_String(totalCount, keyname, "totalCount", item);

								}
								
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode_col); 
									pw.write("|^");
									pw.write(resultMsg_col); 
									pw.write("|^");
									pw.write(rn); 
									pw.write("|^");
									pw.write(ptNo); 
									pw.write("|^");
									pw.write(ptNm); 
									pw.write("|^");
									pw.write(wmcymd); 
									pw.write("|^");
									pw.write(act1); 
									pw.write("|^");
									pw.write(act2); 
									pw.write("|^");
									pw.write(act3); 
									pw.write("|^");
									pw.write(numOfRows); 
									pw.write("|^");
									pw.write(pageNo_str); 
									pw.write("|^");
									pw.write(totalCount); 
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			

							}

						} else {
							System.out.println("parsing error!! :::" + resultCode_col.toString());
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(2500);
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_04.dat", "PRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
			}



	}

}
