package wrs.effluent;

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

public class DamEffluent {

	// 다목적댐 방류수 수질 조회 서비스 - 다목적댐 방류수 수질 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 요청 파라미터는 조회 시작 년도(yyyy), 댐코드의 2개
				if (args.length == 2) {

					if (args[0].length() == 4) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("effluent_DamEffluent_url");
						String service_key = JsonParser.getProperty("effluent_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWrsJson_eff_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1]);
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

							JSONObject obj = JsonParser.parseWrsJson_eff_obj(service_url, service_key, String.valueOf(i), args[0],
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
									
									String srymd = " "; // 측정월
									String dgr = " "; // 수온(℃)
									String ph = " "; // pH
									String do1 = " "; // DO(mg/L)
									String bod = " "; // BOD(mg/L)
									String cod = " "; // COD(mg/L)
									String ss = " "; // SS(mg/L)
									String tn = " "; // T-N(mg/L)
									String tp = " "; // T-P(mg/L)
									String pop = " "; // PO4-P(mg/L)
									String td = " "; // 탁도(NTU)
									String ec = " "; // 전기전도도
									String seqno = " "; // 순번

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										srymd = JsonParser.colWrite_String(srymd, keyname, "srymd", items_jsonObject);
										dgr = JsonParser.colWrite_String(dgr, keyname, "dgr", items_jsonObject);
										ph = JsonParser.colWrite_String(ph, keyname, "ph", items_jsonObject);
										do1 = JsonParser.colWrite_String(do1, keyname, "do1", items_jsonObject);
										bod = JsonParser.colWrite_String(bod, keyname, "bod", items_jsonObject);
										cod = JsonParser.colWrite_String(cod, keyname, "cod", items_jsonObject);
										ss = JsonParser.colWrite_String(ss, keyname, "ss", items_jsonObject);
										tn = JsonParser.colWrite_String(tn, keyname, "tn", items_jsonObject);
										tp = JsonParser.colWrite_String(tp, keyname, "tp", items_jsonObject);
										pop = JsonParser.colWrite_String(pop, keyname, "pop", items_jsonObject);
										td = JsonParser.colWrite_String(td, keyname, "td", items_jsonObject);
										ec = JsonParser.colWrite_String(ec, keyname, "ec", items_jsonObject);
										seqno = JsonParser.colWrite_String(seqno, keyname, "seqno", items_jsonObject);
										
									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(resultCode);
										pw.write("|^");
										pw.write(resultMsg);
										pw.write("|^");
										pw.write(srymd);
										pw.write("|^");
										pw.write(dgr);
										pw.write("|^");
										pw.write(ph);
										pw.write("|^");
										pw.write(do1);
										pw.write("|^");
										pw.write(bod);
										pw.write("|^");
										pw.write(cod);
										pw.write("|^");
										pw.write(ss);
										pw.write("|^");
										pw.write(tn);
										pw.write("|^");
										pw.write(tp);
										pw.write("|^");
										pw.write(pop);
										pw.write("|^");
										pw.write(td);
										pw.write("|^");
										pw.write(ec);
										pw.write("|^");
										pw.write(seqno);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String srymd = " "; // 측정월
										String dgr = " "; // 수온(℃)
										String ph = " "; // pH
										String do1 = " "; // DO(mg/L)
										String bod = " "; // BOD(mg/L)
										String cod = " "; // COD(mg/L)
										String ss = " "; // SS(mg/L)
										String tn = " "; // T-N(mg/L)
										String tp = " "; // T-P(mg/L)
										String pop = " "; // PO4-P(mg/L)
										String td = " "; // 탁도(NTU)
										String ec = " "; // 전기전도도
										String seqno = " "; // 순번

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											srymd = JsonParser.colWrite_String(srymd, keyname, "srymd", item_obj);
											dgr = JsonParser.colWrite_String(dgr, keyname, "dgr", item_obj);
											ph = JsonParser.colWrite_String(ph, keyname, "ph", item_obj);
											do1 = JsonParser.colWrite_String(do1, keyname, "do1", item_obj);
											bod = JsonParser.colWrite_String(bod, keyname, "bod", item_obj);
											cod = JsonParser.colWrite_String(cod, keyname, "cod", item_obj);
											ss = JsonParser.colWrite_String(ss, keyname, "ss", item_obj);
											tn = JsonParser.colWrite_String(tn, keyname, "tn", item_obj);
											tp = JsonParser.colWrite_String(tp, keyname, "tp", item_obj);
											pop = JsonParser.colWrite_String(pop, keyname, "pop", item_obj);
											td = JsonParser.colWrite_String(td, keyname, "td", item_obj);
											ec = JsonParser.colWrite_String(ec, keyname, "ec", item_obj);
											seqno = JsonParser.colWrite_String(seqno, keyname, "seqno", item_obj);

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(args[0]);
											pw.write("|^");
											pw.write(args[1]);
											pw.write("|^");
											pw.write(resultCode);
											pw.write("|^");
											pw.write(resultMsg);
											pw.write("|^");
											pw.write(srymd);
											pw.write("|^");
											pw.write(dgr);
											pw.write("|^");
											pw.write(ph);
											pw.write("|^");
											pw.write(do1);
											pw.write("|^");
											pw.write(bod);
											pw.write("|^");
											pw.write(cod);
											pw.write("|^");
											pw.write(ss);
											pw.write("|^");
											pw.write(tn);
											pw.write("|^");
											pw.write(tp);
											pw.write("|^");
											pw.write(pop);
											pw.write("|^");
											pw.write(td);
											pw.write("|^");
											pw.write(ec);
											pw.write("|^");
											pw.write(seqno);
											pw.println();
											pw.flush();
											pw.close();

										} catch (IOException e) {
											e.printStackTrace();
										}

									}

								}

							} else {
								System.out.println("parsing error!!");
							}

							System.out.println("진행도::::::" + i + "/" + pageCount);

							Thread.sleep(2000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16.dat", "WRS");

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
				System.out.println("stdt :" + args[0] + ": damcd :" + args[1]);
			}



	}

}
