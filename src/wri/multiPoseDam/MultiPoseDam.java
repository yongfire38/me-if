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

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(pageNo), args[0],
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
								json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
							}*/

							JSONObject obj = JsonParser.parseWriJson_obj(service_url, service_key, String.valueOf(i), args[0],
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

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {
									
									String suge = " "; // 수계
									String damnm = " "; // 댐이름
									String zerosevenhourprcptqy = " "; // 강우량(금일)
									String prcptqy = " "; // 강우량(전일)
									String pyacurf = " "; // 누계(금년)
									String vyacurf = " "; // 누계(전년)
									String oyaacurf = " "; // 누계(예년)
									String inflowqy = " "; // 일유입량
									String totdcwtrqy = " "; // 전일
																						// 방류량(본댐)
									String totdcwtrqyjo = " "; // 전일
																						// 방류량(조정치)
									String nowlowlevel = " "; // 저수위(현재)
									String lastlowlevel = " "; // 저수위(전년)
									String nyearlowlevel = " "; // 저수위(예년)
									String nowrsvwtqy = " "; // 저수량(현재)
									String lastrsvwtqy = " "; // 저수량(전년)
									String nyearrsvwtqy = " "; // 저수량(예년)
									String rsvwtrt = " "; // 현재저수율
									String dvlpqyacmtlacmslt = " "; // 발전량(실적)
									String dvlpqyacmtlplan = " "; // 발전량(계획)
									String dvlpqyacmtlversus = " "; // 발전량(대비)
									String dvlpqyfyerplan = " "; // 연간(계획)
									String dvlpqyfyerversus = " "; // 연간(대비)

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										suge = JsonParser.colWrite_String(suge, keyname, "suge", item_obj);
										damnm = JsonParser.colWrite_String(damnm, keyname, "damnm", item_obj);
										zerosevenhourprcptqy = JsonParser.colWrite_String(zerosevenhourprcptqy, keyname, "zerosevenhourprcptqy", item_obj);
										prcptqy = JsonParser.colWrite_String(prcptqy, keyname, "prcptqy", item_obj);
										pyacurf = JsonParser.colWrite_String(pyacurf, keyname, "pyacurf", item_obj);
										vyacurf = JsonParser.colWrite_String(vyacurf, keyname, "vyacurf", item_obj);
										oyaacurf = JsonParser.colWrite_String(oyaacurf, keyname, "oyaacurf", item_obj);
										inflowqy = JsonParser.colWrite_String(inflowqy, keyname, "inflowqy", item_obj);
										totdcwtrqy = JsonParser.colWrite_String(totdcwtrqy, keyname, "totdcwtrqy", item_obj);
										totdcwtrqyjo = JsonParser.colWrite_String(totdcwtrqyjo, keyname, "totdcwtrqyjo", item_obj);
										nowlowlevel = JsonParser.colWrite_String(nowlowlevel, keyname, "nowlowlevel", item_obj);
										lastlowlevel = JsonParser.colWrite_String(lastlowlevel, keyname, "lastlowlevel", item_obj);
										nyearlowlevel = JsonParser.colWrite_String(nyearlowlevel, keyname, "nyearlowlevel", item_obj);
										nowrsvwtqy = JsonParser.colWrite_String(nowrsvwtqy, keyname, "nowrsvwtqy", item_obj);
										lastrsvwtqy = JsonParser.colWrite_String(lastrsvwtqy, keyname, "lastrsvwtqy", item_obj);
										nyearrsvwtqy = JsonParser.colWrite_String(nyearrsvwtqy, keyname, "nyearrsvwtqy", item_obj);
										rsvwtrt = JsonParser.colWrite_String(rsvwtrt, keyname, "rsvwtrt", item_obj);
										dvlpqyacmtlacmslt = JsonParser.colWrite_String(dvlpqyacmtlacmslt, keyname, "dvlpqyacmtlacmslt", item_obj);
										dvlpqyacmtlplan = JsonParser.colWrite_String(dvlpqyacmtlplan, keyname, "dvlpqyacmtlplan", item_obj);
										dvlpqyacmtlversus = JsonParser.colWrite_String(dvlpqyacmtlversus, keyname, "dvlpqyacmtlversus", item_obj);
										dvlpqyfyerplan = JsonParser.colWrite_String(dvlpqyfyerplan, keyname, "dvlpqyfyerplan", item_obj);
										dvlpqyfyerversus = JsonParser.colWrite_String(dvlpqyfyerversus, keyname, "dvlpqyfyerversus", item_obj);

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
										pw.write(suge);
										pw.write("|^");
										pw.write(damnm);
										pw.write("|^");
										pw.write(zerosevenhourprcptqy);
										pw.write("|^");
										pw.write(prcptqy);
										pw.write("|^");
										pw.write(pyacurf);
										pw.write("|^");
										pw.write(vyacurf);
										pw.write("|^");
										pw.write(oyaacurf);
										pw.write("|^");
										pw.write(inflowqy);
										pw.write("|^");
										pw.write(totdcwtrqy);
										pw.write("|^");
										pw.write(totdcwtrqyjo);
										pw.write("|^");
										pw.write(nowlowlevel);
										pw.write("|^");
										pw.write(lastlowlevel);
										pw.write("|^");
										pw.write(nyearlowlevel);
										pw.write("|^");
										pw.write(nowrsvwtqy);
										pw.write("|^");
										pw.write(lastrsvwtqy);
										pw.write("|^");
										pw.write(nyearrsvwtqy);
										pw.write("|^");
										pw.write(rsvwtrt);
										pw.write("|^");
										pw.write(dvlpqyacmtlacmslt);
										pw.write("|^");
										pw.write(dvlpqyacmtlplan);
										pw.write("|^");
										pw.write(dvlpqyacmtlversus);
										pw.write("|^");
										pw.write(dvlpqyfyerplan);
										pw.write("|^");
										pw.write(dvlpqyfyerversus);
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
