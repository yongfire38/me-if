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
				
				Thread.sleep(3000);
				
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

						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_body = (JSONObject) count_response.get("body");
						JSONObject count_header = (JSONObject) count_response.get("header");
						
						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();
						
						if(!(count_resultCode.equals("00"))){
							System.out.println("parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						}else{
							int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
							int totalCount = ((Long) count_body.get("totalCount")).intValue();

							pageCount = (totalCount / numOfRows) + 1;
						}
 

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer rnum = new StringBuffer(" "); // 정렬순서
					StringBuffer eiaCd = new StringBuffer(" "); // 환경영향평가코드
					StringBuffer eiaSeq = new StringBuffer(" "); // 환경영향평가고유번호
					StringBuffer bizNm = new StringBuffer(" "); // 사업명
					StringBuffer ccilOrganNm = new StringBuffer(" "); // 협의기관
					StringBuffer firstCtgCd = new StringBuffer(" "); // 현재단계
					StringBuffer stepChangeDt = new StringBuffer(" "); // 단계변경일

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");
							JSONObject items = (JSONObject) body.get("items");

							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

							String numOfRows_str = body.get("numOfRows").toString();
							String totalCount_str = body.get("totalCount").toString();

							if(!(resultCode.equals("00"))){
								System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							} else  if (body.get("items") instanceof String) {
								System.out.println("data not exist!!");
							} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(rnum, keyname, "rnum", item_obj);
										JsonParser.colWrite(eiaCd, keyname, "eiaCd", item_obj);
										JsonParser.colWrite(eiaSeq, keyname, "eiaSeq", item_obj);
										JsonParser.colWrite(bizNm, keyname, "bizNm", item_obj);
										JsonParser.colWrite(ccilOrganNm, keyname, "ccilOrganNm", item_obj);
										JsonParser.colWrite(firstCtgCd, keyname, "firstCtgCd", item_obj);
										JsonParser.colWrite(stepChangeDt, keyname, "stepChangeDt", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(resultCode);
									resultSb.append("|^");
									resultSb.append(resultMsg);
									resultSb.append("|^");
									resultSb.append(numOfRows_str);
									resultSb.append("|^");
									resultSb.append(Integer.toString(i));
									resultSb.append("|^");
									resultSb.append(totalCount_str);
									resultSb.append("|^");
									resultSb.append(rnum);
									resultSb.append("|^");
									resultSb.append(eiaCd);
									resultSb.append("|^");
									resultSb.append(eiaSeq);
									resultSb.append("|^");
									resultSb.append(bizNm);
									resultSb.append("|^");
									resultSb.append(ccilOrganNm);
									resultSb.append("|^");
									resultSb.append(firstCtgCd);
									resultSb.append("|^");
									resultSb.append(stepChangeDt);
									resultSb.append(System.getProperty("line.separator"));

								}

							} else {
								System.out.println("parsing error!!");
							}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						Thread.sleep(1000);

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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_41.dat", "EIA");

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
