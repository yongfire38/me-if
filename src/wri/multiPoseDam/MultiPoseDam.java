package wri.multiPoseDam;

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

public class MultiPoseDam {

	// 다목적댐 관리현황 - 다목적댐관리현황 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// 필요한 파라미터는 전일 날짜, 전년날짜, 검색날짜 (yyyyMMdd), 검색 시간(2자리)
				if (args.length == 4) {

					if (args[0].length() == 8 && args[1].length() == 8 && args[2].length() == 8
							&& args[3].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("multiPoseDam_url");
						String service_key = JsonParser.getProperty("multiPoseDam_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_09.dat");

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

						json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2], args[3]);

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

						StringBuffer suge = new StringBuffer(" "); // 수계
						StringBuffer damnm = new StringBuffer(" "); // 댐이름
						StringBuffer zerosevenhourprcptqy = new StringBuffer(" "); // 강우량(금일)
						StringBuffer prcptqy = new StringBuffer(" "); // 강우량(전일)
						StringBuffer pyacurf = new StringBuffer(" "); // 누계(금년)
						StringBuffer vyacurf = new StringBuffer(" "); // 누계(전년)
						StringBuffer oyaacurf = new StringBuffer(" "); // 누계(예년)
						StringBuffer inflowqy = new StringBuffer(" "); // 일유입량
						StringBuffer totdcwtrqy = new StringBuffer(" "); // 전일
																			// 방류량(본댐)
						StringBuffer totdcwtrqyjo = new StringBuffer(" "); // 전일
																			// 방류량(조정치)
						StringBuffer nowlowlevel = new StringBuffer(" "); // 저수위(현재)
						StringBuffer lastlowlevel = new StringBuffer(" "); // 저수위(전년)
						StringBuffer nyearlowlevel = new StringBuffer(" "); // 저수위(예년)
						StringBuffer nowrsvwtqy = new StringBuffer(" "); // 저수량(현재)
						StringBuffer lastrsvwtqy = new StringBuffer(" "); // 저수량(전년)
						StringBuffer nyearrsvwtqy = new StringBuffer(" "); // 저수량(예년)
						StringBuffer rsvwtrt = new StringBuffer(" "); // 현재저수율
						StringBuffer dvlpqyacmtlacmslt = new StringBuffer(" "); // 발전량(실적)
						StringBuffer dvlpqyacmtlplan = new StringBuffer(" "); // 발전량(계획)
						StringBuffer dvlpqyacmtlversus = new StringBuffer(" "); // 발전량(대비)
						StringBuffer dvlpqyfyerplan = new StringBuffer(" "); // 연간(계획)
						StringBuffer dvlpqyfyerversus = new StringBuffer(" "); // 연간(대비)

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2], args[3]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
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

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(suge, keyname, "suge", item_obj);
										JsonParser.colWrite(damnm, keyname, "damnm", item_obj);
										JsonParser.colWrite(zerosevenhourprcptqy, keyname, "zerosevenhourprcptqy",
												item_obj);
										JsonParser.colWrite(prcptqy, keyname, "prcptqy", item_obj);
										JsonParser.colWrite(pyacurf, keyname, "pyacurf", item_obj);
										JsonParser.colWrite(vyacurf, keyname, "vyacurf", item_obj);
										JsonParser.colWrite(oyaacurf, keyname, "oyaacurf", item_obj);
										JsonParser.colWrite(inflowqy, keyname, "inflowqy", item_obj);
										JsonParser.colWrite(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
										JsonParser.colWrite(totdcwtrqyjo, keyname, "totdcwtrqyjo", item_obj);
										JsonParser.colWrite(nowlowlevel, keyname, "nowlowlevel", item_obj);
										JsonParser.colWrite(lastlowlevel, keyname, "lastlowlevel", item_obj);
										JsonParser.colWrite(nyearlowlevel, keyname, "nyearlowlevel", item_obj);
										JsonParser.colWrite(nowrsvwtqy, keyname, "nowrsvwtqy", item_obj);
										JsonParser.colWrite(lastrsvwtqy, keyname, "lastrsvwtqy", item_obj);
										JsonParser.colWrite(nyearrsvwtqy, keyname, "nyearrsvwtqy", item_obj);
										JsonParser.colWrite(rsvwtrt, keyname, "rsvwtrt", item_obj);
										JsonParser.colWrite(dvlpqyacmtlacmslt, keyname, "dvlpqyacmtlacmslt", item_obj);
										JsonParser.colWrite(dvlpqyacmtlplan, keyname, "dvlpqyacmtlplan", item_obj);
										JsonParser.colWrite(dvlpqyacmtlversus, keyname, "dvlpqyacmtlversus", item_obj);
										JsonParser.colWrite(dvlpqyfyerplan, keyname, "dvlpqyfyerplan", item_obj);
										JsonParser.colWrite(dvlpqyfyerversus, keyname, "dvlpqyfyerversus", item_obj);

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
									resultSb.append(suge);
									resultSb.append("|^");
									resultSb.append(damnm);
									resultSb.append("|^");
									resultSb.append(zerosevenhourprcptqy);
									resultSb.append("|^");
									resultSb.append(prcptqy);
									resultSb.append("|^");
									resultSb.append(pyacurf);
									resultSb.append("|^");
									resultSb.append(vyacurf);
									resultSb.append("|^");
									resultSb.append(oyaacurf);
									resultSb.append("|^");
									resultSb.append(inflowqy);
									resultSb.append("|^");
									resultSb.append(totdcwtrqy);
									resultSb.append("|^");
									resultSb.append(totdcwtrqyjo);
									resultSb.append("|^");
									resultSb.append(nowlowlevel);
									resultSb.append("|^");
									resultSb.append(lastlowlevel);
									resultSb.append("|^");
									resultSb.append(nyearlowlevel);
									resultSb.append("|^");
									resultSb.append(nowrsvwtqy);
									resultSb.append("|^");
									resultSb.append(lastrsvwtqy);
									resultSb.append("|^");
									resultSb.append(nyearrsvwtqy);
									resultSb.append("|^");
									resultSb.append(rsvwtrt);
									resultSb.append("|^");
									resultSb.append(dvlpqyacmtlacmslt);
									resultSb.append("|^");
									resultSb.append(dvlpqyacmtlplan);
									resultSb.append("|^");
									resultSb.append(dvlpqyacmtlversus);
									resultSb.append("|^");
									resultSb.append(dvlpqyfyerplan);
									resultSb.append("|^");
									resultSb.append(dvlpqyfyerversus);
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_09.dat", "WRI");

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
						"tdate :" + args[0] + ": ldate :" + args[1] + ": vdate :" + args[2] + ": vtime :" + args[3]);
			}



	}

}
