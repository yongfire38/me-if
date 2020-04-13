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
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class DamEffluent {

	// 다목적댐 방류수 수질 조회 서비스 - 다목적댐 방류수 수질 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 요청 파라미터는 조회 시작 년도(yyyy), 댐코드의 2개
				if (args.length == 2) {

					if (args[0].length() == 4) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("effluent_DamEffluent_url");
						String service_key = JsonParser.getProperty("effluent_service_key");

						// step 1.파일의 첫 행 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16.dat");

						if (file.exists()) {

							System.out.println("파일이 이미 존재하므로 이어쓰기..");

						} else {

							try {

								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write("stdt"); // 검색년도
								pw.write("|^");
								pw.write("damcd"); // 댐코드
								pw.write("|^");
								pw.write("resultCode"); // 결과코드
								pw.write("|^");
								pw.write("resultMsg"); // 결과메세지
								pw.write("|^");
								pw.write("srymd"); // 측정월
								pw.write("|^");
								pw.write("dgr"); // 수온(℃)
								pw.write("|^");
								pw.write("ph"); // pH
								pw.write("|^");
								pw.write("do1"); // DO(mg/L)
								pw.write("|^");
								pw.write("bod"); // BOD(mg/L)
								pw.write("|^");
								pw.write("cod"); // COD(mg/L)
								pw.write("|^");
								pw.write("ss"); // SS(mg/L)
								pw.write("|^");
								pw.write("tn"); // T-N(mg/L)
								pw.write("|^");
								pw.write("tp"); // T-P(mg/L)
								pw.write("|^");
								pw.write("pop"); // PO4-P(mg/L)
								pw.write("|^");
								pw.write("td"); // 탁도(NTU)
								pw.write("|^");
								pw.write("ec"); // 전기전도도
								pw.write("|^");
								pw.write("seqno"); // 순번
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						json = JsonParser.parseWrsJson_eff(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1]);

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

						StringBuffer srymd = new StringBuffer(" "); // 측정월
						StringBuffer dgr = new StringBuffer(" "); // 수온(℃)
						StringBuffer ph = new StringBuffer(" "); // pH
						StringBuffer do1 = new StringBuffer(" "); // DO(mg/L)
						StringBuffer bod = new StringBuffer(" "); // BOD(mg/L)
						StringBuffer cod = new StringBuffer(" "); // COD(mg/L)
						StringBuffer ss = new StringBuffer(" "); // SS(mg/L)
						StringBuffer tn = new StringBuffer(" "); // T-N(mg/L)
						StringBuffer tp = new StringBuffer(" "); // T-P(mg/L)
						StringBuffer pop = new StringBuffer(" "); // PO4-P(mg/L)
						StringBuffer td = new StringBuffer(" "); // 탁도(NTU)
						StringBuffer ec = new StringBuffer(" "); // 전기전도도
						StringBuffer seqno = new StringBuffer(" "); // 순번

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWrsJson_eff(service_url, service_key, String.valueOf(i), args[0],
									args[1]);

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

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(srymd, keyname, "srymd", items_jsonObject);
										JsonParser.colWrite(dgr, keyname, "dgr", items_jsonObject);
										JsonParser.colWrite(ph, keyname, "ph", items_jsonObject);
										JsonParser.colWrite(do1, keyname, "do1", items_jsonObject);
										JsonParser.colWrite(bod, keyname, "bod", items_jsonObject);
										JsonParser.colWrite(cod, keyname, "cod", items_jsonObject);
										JsonParser.colWrite(ss, keyname, "ss", items_jsonObject);
										JsonParser.colWrite(tn, keyname, "tn", items_jsonObject);
										JsonParser.colWrite(tp, keyname, "tp", items_jsonObject);
										JsonParser.colWrite(pop, keyname, "pop", items_jsonObject);
										JsonParser.colWrite(td, keyname, "td", items_jsonObject);
										JsonParser.colWrite(ec, keyname, "ec", items_jsonObject);
										JsonParser.colWrite(seqno, keyname, "seqno", items_jsonObject);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(resultCode);
									resultSb.append("|^");
									resultSb.append(resultMsg);
									resultSb.append("|^");
									resultSb.append(srymd);
									resultSb.append("|^");
									resultSb.append(dgr);
									resultSb.append("|^");
									resultSb.append(ph);
									resultSb.append("|^");
									resultSb.append(do1);
									resultSb.append("|^");
									resultSb.append(bod);
									resultSb.append("|^");
									resultSb.append(cod);
									resultSb.append("|^");
									resultSb.append(ss);
									resultSb.append("|^");
									resultSb.append(tn);
									resultSb.append("|^");
									resultSb.append(tp);
									resultSb.append("|^");
									resultSb.append(pop);
									resultSb.append("|^");
									resultSb.append(td);
									resultSb.append("|^");
									resultSb.append(ec);
									resultSb.append("|^");
									resultSb.append(seqno);
									resultSb.append(System.getProperty("line.separator"));

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();

											JsonParser.colWrite(srymd, keyname, "srymd", item_obj);
											JsonParser.colWrite(dgr, keyname, "dgr", item_obj);
											JsonParser.colWrite(ph, keyname, "ph", item_obj);
											JsonParser.colWrite(do1, keyname, "do1", item_obj);
											JsonParser.colWrite(bod, keyname, "bod", item_obj);
											JsonParser.colWrite(cod, keyname, "cod", item_obj);
											JsonParser.colWrite(ss, keyname, "ss", item_obj);
											JsonParser.colWrite(tn, keyname, "tn", item_obj);
											JsonParser.colWrite(tp, keyname, "tp", item_obj);
											JsonParser.colWrite(pop, keyname, "pop", item_obj);
											JsonParser.colWrite(td, keyname, "td", item_obj);
											JsonParser.colWrite(ec, keyname, "ec", item_obj);
											JsonParser.colWrite(seqno, keyname, "seqno", item_obj);

										}

										// 한번에 문자열 합침
										resultSb.append(args[0]);
										resultSb.append("|^");
										resultSb.append(args[1]);
										resultSb.append("|^");
										resultSb.append(resultCode);
										resultSb.append("|^");
										resultSb.append(resultMsg);
										resultSb.append("|^");
										resultSb.append(srymd);
										resultSb.append("|^");
										resultSb.append(dgr);
										resultSb.append("|^");
										resultSb.append(ph);
										resultSb.append("|^");
										resultSb.append(do1);
										resultSb.append("|^");
										resultSb.append(bod);
										resultSb.append("|^");
										resultSb.append(cod);
										resultSb.append("|^");
										resultSb.append(ss);
										resultSb.append("|^");
										resultSb.append(tn);
										resultSb.append("|^");
										resultSb.append(tp);
										resultSb.append("|^");
										resultSb.append(pop);
										resultSb.append("|^");
										resultSb.append(td);
										resultSb.append("|^");
										resultSb.append(ec);
										resultSb.append("|^");
										resultSb.append(seqno);
										resultSb.append(System.getProperty("line.separator"));

									}

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

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_16.dat", "WRS");

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

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("stdt :" + args[0] + ": damcd :" + args[1]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
