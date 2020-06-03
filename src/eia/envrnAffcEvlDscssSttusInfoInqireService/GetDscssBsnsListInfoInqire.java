package eia.envrnAffcEvlDscssSttusInfoInqireService;

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


public class GetDscssBsnsListInfoInqire {

	@SuppressWarnings("unchecked")
	// 환경영향평가 협의현황 서비스 - 협의사업 목록 정보를 조회
	public static void main(String[] args) throws Exception {
			
			try{
				
				
				
				// 실행시 필요 매개 변수는 페이지 번호 외에는 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("envrnAffcEvlDscssSttusInfoInqireService_getDscssBsnsListInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDscssSttusInfoInqireService_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_41.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;
					
					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답");
						json = "{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\"}}}";
					}

						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");
						
						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();
						
						if((count_resultCode.equals("03"))){
							System.out.println("data not exist!!");
						} else if((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))){
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
							//throw new Exception();
						} else{
							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;
						}
 

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {
						
						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						// 2020.06.02 : 빈 Json을 리턴하도록 롤백
						if(json.indexOf("</") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						}

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");
							
							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

							if(!(resultCode.equals("00"))){
								System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
								//throw new Exception();
							} else  if (body.get("items") instanceof String) {
								System.out.println("data not exist!!");
							} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

								String numOfRows_str = body.get("numOfRows").toString();
								String totalCount_str = body.get("totalCount").toString();
								
								JSONObject items = (JSONObject) body.get("items");

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {
									
									String rnum = " "; // 정렬순서
									String eiaCd = " "; // 환경영향평가코드
									String eiaSeq = " "; // 환경영향평가고유번호
									String bizNm = " "; // 사업명
									String ccilOrganNm = " "; // 협의기관
									String firstCtgCd = " "; // 현재단계
									String stepChangeDt = " "; // 단계변경일

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										rnum = JsonParser.colWrite_String(rnum, keyname, "rnum", item_obj);
										eiaCd = JsonParser.colWrite_String(eiaCd, keyname, "eiaCd", item_obj);
										eiaSeq = JsonParser.colWrite_String(eiaSeq, keyname, "eiaSeq", item_obj);
										bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
										ccilOrganNm = JsonParser.colWrite_String(ccilOrganNm, keyname, "ccilOrganNm", item_obj);
										firstCtgCd = JsonParser.colWrite_String(firstCtgCd, keyname, "firstCtgCd", item_obj);
										stepChangeDt = JsonParser.colWrite_String(stepChangeDt, keyname, "stepChangeDt", item_obj);

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

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
										pw.write(eiaCd); 
										pw.write("|^");
										pw.write(eiaSeq); 
										pw.write("|^");
										pw.write(bizNm); 
										pw.write("|^");
										pw.write(ccilOrganNm); 
										pw.write("|^");
										pw.write(firstCtgCd); 
										pw.write("|^");
										pw.write(stepChangeDt); 
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

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(1000);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_41.dat", "EIA");

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
