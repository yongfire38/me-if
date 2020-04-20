package wrs.dailwater;

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

public class Dmntwater {

	// 광역정수장 수질정보 조회 서비스 - 광역월간 수돗물 수질 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				Thread.sleep(3000);

				// 요청 파라미터는 조회시작일(yyyyMMdd), 조회종료일(yyyyMMdd), 정수장 코드의 3개
				// 정수장 코드는 정수장 코드 조회 api에서 조회 가능
				if (args.length == 3) {

					if (args[1].length() == 6 && args[2].length() == 6) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("dailwater_Dmntwater_url");
						String service_key = JsonParser.getProperty("dailwater_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_22.dat");

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

						json = JsonParser.parseWriJson_month(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2]);

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

						StringBuffer mesurede = new StringBuffer("");
						StringBuffer item1 = new StringBuffer(" ");
						StringBuffer item2 = new StringBuffer(" ");
						StringBuffer item3 = new StringBuffer(" ");
						StringBuffer item5 = new StringBuffer(" ");
						StringBuffer item6 = new StringBuffer(" ");
						StringBuffer item7 = new StringBuffer(" ");
						StringBuffer item8 = new StringBuffer(" ");
						StringBuffer item9 = new StringBuffer(" ");
						StringBuffer item10 = new StringBuffer(" ");
						StringBuffer item11 = new StringBuffer(" ");
						StringBuffer item12 = new StringBuffer(" ");
						StringBuffer item13 = new StringBuffer(" ");
						StringBuffer item14 = new StringBuffer(" ");
						StringBuffer item15 = new StringBuffer(" ");
						StringBuffer item16 = new StringBuffer(" ");
						StringBuffer item17 = new StringBuffer(" ");
						StringBuffer item18 = new StringBuffer(" ");
						StringBuffer item19 = new StringBuffer(" ");
						StringBuffer item20 = new StringBuffer(" ");
						StringBuffer item21 = new StringBuffer(" ");
						StringBuffer item22 = new StringBuffer(" ");
						StringBuffer item23 = new StringBuffer(" ");
						StringBuffer item24 = new StringBuffer(" ");
						StringBuffer item25 = new StringBuffer(" ");
						StringBuffer item26 = new StringBuffer(" ");
						StringBuffer item27 = new StringBuffer(" ");
						StringBuffer item28 = new StringBuffer(" ");
						StringBuffer item29 = new StringBuffer(" ");
						StringBuffer item30 = new StringBuffer(" ");
						StringBuffer item31 = new StringBuffer(" ");
						StringBuffer item32 = new StringBuffer(" ");
						StringBuffer item33 = new StringBuffer(" ");
						StringBuffer item34 = new StringBuffer(" ");
						StringBuffer item35 = new StringBuffer(" ");
						StringBuffer item36 = new StringBuffer(" ");
						StringBuffer item37 = new StringBuffer(" ");
						StringBuffer item38 = new StringBuffer(" ");
						StringBuffer item39 = new StringBuffer(" ");
						StringBuffer item40 = new StringBuffer(" ");
						StringBuffer item41 = new StringBuffer(" ");
						StringBuffer item42 = new StringBuffer(" ");
						StringBuffer item43 = new StringBuffer(" ");
						StringBuffer item44 = new StringBuffer(" ");
						StringBuffer item45 = new StringBuffer(" ");
						StringBuffer item46 = new StringBuffer(" ");
						StringBuffer item47 = new StringBuffer(" ");
						StringBuffer item48 = new StringBuffer(" ");
						StringBuffer item49 = new StringBuffer(" ");
						StringBuffer item50 = new StringBuffer(" ");
						StringBuffer item51 = new StringBuffer(" ");
						StringBuffer item52 = new StringBuffer(" ");
						StringBuffer item53 = new StringBuffer(" ");
						StringBuffer item54 = new StringBuffer(" ");
						StringBuffer item55 = new StringBuffer(" ");
						StringBuffer item56 = new StringBuffer(" ");
						StringBuffer item57 = new StringBuffer(" ");
						StringBuffer item58 = new StringBuffer(" ");
						StringBuffer item59 = new StringBuffer(" ");
						StringBuffer item60 = new StringBuffer(" ");

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWriJson_month(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2]);

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

							String numOfRows_str = body.get("numOfRows").toString();

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

										JsonParser.colWrite(mesurede, keyname, "mesurede", items_jsonObject);
										JsonParser.colWrite(item1, keyname, "item1", items_jsonObject);
										JsonParser.colWrite(item2, keyname, "item2", items_jsonObject);
										JsonParser.colWrite(item3, keyname, "item3", items_jsonObject);
										JsonParser.colWrite(item5, keyname, "item5", items_jsonObject);
										JsonParser.colWrite(item6, keyname, "item6", items_jsonObject);
										JsonParser.colWrite(item7, keyname, "item7", items_jsonObject);
										JsonParser.colWrite(item8, keyname, "item8", items_jsonObject);
										JsonParser.colWrite(item9, keyname, "item9", items_jsonObject);
										JsonParser.colWrite(item10, keyname, "item10", items_jsonObject);
										JsonParser.colWrite(item11, keyname, "item11", items_jsonObject);
										JsonParser.colWrite(item12, keyname, "item12", items_jsonObject);
										JsonParser.colWrite(item13, keyname, "item13", items_jsonObject);
										JsonParser.colWrite(item14, keyname, "item14", items_jsonObject);
										JsonParser.colWrite(item15, keyname, "item15", items_jsonObject);
										JsonParser.colWrite(item16, keyname, "item16", items_jsonObject);
										JsonParser.colWrite(item17, keyname, "item17", items_jsonObject);
										JsonParser.colWrite(item18, keyname, "item18", items_jsonObject);
										JsonParser.colWrite(item19, keyname, "item19", items_jsonObject);
										JsonParser.colWrite(item20, keyname, "item20", items_jsonObject);
										JsonParser.colWrite(item21, keyname, "item21", items_jsonObject);
										JsonParser.colWrite(item22, keyname, "item22", items_jsonObject);
										JsonParser.colWrite(item23, keyname, "item23", items_jsonObject);
										JsonParser.colWrite(item24, keyname, "item24", items_jsonObject);
										JsonParser.colWrite(item25, keyname, "item25", items_jsonObject);
										JsonParser.colWrite(item26, keyname, "item26", items_jsonObject);
										JsonParser.colWrite(item27, keyname, "item27", items_jsonObject);
										JsonParser.colWrite(item28, keyname, "item28", items_jsonObject);
										JsonParser.colWrite(item29, keyname, "item29", items_jsonObject);
										JsonParser.colWrite(item30, keyname, "item30", items_jsonObject);
										JsonParser.colWrite(item31, keyname, "item31", items_jsonObject);
										JsonParser.colWrite(item32, keyname, "item32", items_jsonObject);
										JsonParser.colWrite(item33, keyname, "item33", items_jsonObject);
										JsonParser.colWrite(item34, keyname, "item34", items_jsonObject);
										JsonParser.colWrite(item35, keyname, "item35", items_jsonObject);
										JsonParser.colWrite(item36, keyname, "item36", items_jsonObject);
										JsonParser.colWrite(item37, keyname, "item37", items_jsonObject);
										JsonParser.colWrite(item38, keyname, "item38", items_jsonObject);
										JsonParser.colWrite(item39, keyname, "item39", items_jsonObject);
										JsonParser.colWrite(item40, keyname, "item40", items_jsonObject);
										JsonParser.colWrite(item41, keyname, "item41", items_jsonObject);
										JsonParser.colWrite(item42, keyname, "item42", items_jsonObject);
										JsonParser.colWrite(item43, keyname, "item43", items_jsonObject);
										JsonParser.colWrite(item44, keyname, "item44", items_jsonObject);
										JsonParser.colWrite(item45, keyname, "item45", items_jsonObject);
										JsonParser.colWrite(item46, keyname, "item46", items_jsonObject);
										JsonParser.colWrite(item47, keyname, "item47", items_jsonObject);
										JsonParser.colWrite(item48, keyname, "item48", items_jsonObject);
										JsonParser.colWrite(item49, keyname, "item49", items_jsonObject);
										JsonParser.colWrite(item50, keyname, "item50", items_jsonObject);
										JsonParser.colWrite(item51, keyname, "item51", items_jsonObject);
										JsonParser.colWrite(item52, keyname, "item52", items_jsonObject);
										JsonParser.colWrite(item53, keyname, "item53", items_jsonObject);
										JsonParser.colWrite(item54, keyname, "item54", items_jsonObject);
										JsonParser.colWrite(item55, keyname, "item55", items_jsonObject);
										JsonParser.colWrite(item56, keyname, "item56", items_jsonObject);
										JsonParser.colWrite(item57, keyname, "item57", items_jsonObject);
										JsonParser.colWrite(item58, keyname, "item58", items_jsonObject);
										JsonParser.colWrite(item59, keyname, "item59", items_jsonObject);
										JsonParser.colWrite(item60, keyname, "item60", items_jsonObject);

									}

									// 한번에 문자열 합침
									resultSb.append(args[0]);
									resultSb.append("|^");
									resultSb.append(args[1]);
									resultSb.append("|^");
									resultSb.append(args[2]);
									resultSb.append("|^");
									resultSb.append(mesurede);
									resultSb.append("|^");
									resultSb.append(item1);
									resultSb.append("|^");
									resultSb.append(item2);
									resultSb.append("|^");
									resultSb.append(item3);
									resultSb.append("|^");
									resultSb.append(item5);
									resultSb.append("|^");
									resultSb.append(item6);
									resultSb.append("|^");
									resultSb.append(item7);
									resultSb.append("|^");
									resultSb.append(item8);
									resultSb.append("|^");
									resultSb.append(item9);
									resultSb.append("|^");
									resultSb.append(item10);
									resultSb.append("|^");
									resultSb.append(item11);
									resultSb.append("|^");
									resultSb.append(item12);
									resultSb.append("|^");
									resultSb.append(item13);
									resultSb.append("|^");
									resultSb.append(item14);
									resultSb.append("|^");
									resultSb.append(item15);
									resultSb.append("|^");
									resultSb.append(item16);
									resultSb.append("|^");
									resultSb.append(item17);
									resultSb.append("|^");
									resultSb.append(item18);
									resultSb.append("|^");
									resultSb.append(item19);
									resultSb.append("|^");
									resultSb.append(item20);
									resultSb.append("|^");
									resultSb.append(item21);
									resultSb.append("|^");
									resultSb.append(item22);
									resultSb.append("|^");
									resultSb.append(item23);
									resultSb.append("|^");
									resultSb.append(item24);
									resultSb.append("|^");
									resultSb.append(item25);
									resultSb.append("|^");
									resultSb.append(item26);
									resultSb.append("|^");
									resultSb.append(item27);
									resultSb.append("|^");
									resultSb.append(item28);
									resultSb.append("|^");
									resultSb.append(item29);
									resultSb.append("|^");
									resultSb.append(item30);
									resultSb.append("|^");
									resultSb.append(item31);
									resultSb.append("|^");
									resultSb.append(item32);
									resultSb.append("|^");
									resultSb.append(item33);
									resultSb.append("|^");
									resultSb.append(item34);
									resultSb.append("|^");
									resultSb.append(item35);
									resultSb.append("|^");
									resultSb.append(item36);
									resultSb.append("|^");
									resultSb.append(item37);
									resultSb.append("|^");
									resultSb.append(item38);
									resultSb.append("|^");
									resultSb.append(item39);
									resultSb.append("|^");
									resultSb.append(item40);
									resultSb.append("|^");
									resultSb.append(item41);
									resultSb.append("|^");
									resultSb.append(item42);
									resultSb.append("|^");
									resultSb.append(item43);
									resultSb.append("|^");
									resultSb.append(item44);
									resultSb.append("|^");
									resultSb.append(item45);
									resultSb.append("|^");
									resultSb.append(item46);
									resultSb.append("|^");
									resultSb.append(item47);
									resultSb.append("|^");
									resultSb.append(item48);
									resultSb.append("|^");
									resultSb.append(item49);
									resultSb.append("|^");
									resultSb.append(item50);
									resultSb.append("|^");
									resultSb.append(item51);
									resultSb.append("|^");
									resultSb.append(item52);
									resultSb.append("|^");
									resultSb.append(item53);
									resultSb.append("|^");
									resultSb.append(item54);
									resultSb.append("|^");
									resultSb.append(item55);
									resultSb.append("|^");
									resultSb.append(item56);
									resultSb.append("|^");
									resultSb.append(item57);
									resultSb.append("|^");
									resultSb.append(item58);
									resultSb.append("|^");
									resultSb.append(item59);
									resultSb.append("|^");
									resultSb.append(item60);
									resultSb.append("|^");
									resultSb.append(numOfRows_str);
									resultSb.append("|^");
									resultSb.append(String.valueOf(i));
									resultSb.append(System.getProperty("line.separator"));

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();

											JsonParser.colWrite(mesurede, keyname, "mesurede", item_obj);
											JsonParser.colWrite(item1, keyname, "item1", item_obj);
											JsonParser.colWrite(item2, keyname, "item2", item_obj);
											JsonParser.colWrite(item3, keyname, "item3", item_obj);
											JsonParser.colWrite(item5, keyname, "item5", item_obj);
											JsonParser.colWrite(item6, keyname, "item6", item_obj);
											JsonParser.colWrite(item7, keyname, "item7", item_obj);
											JsonParser.colWrite(item8, keyname, "item8", item_obj);
											JsonParser.colWrite(item9, keyname, "item9", item_obj);
											JsonParser.colWrite(item10, keyname, "item10", item_obj);
											JsonParser.colWrite(item11, keyname, "item11", item_obj);
											JsonParser.colWrite(item12, keyname, "item12", item_obj);
											JsonParser.colWrite(item13, keyname, "item13", item_obj);
											JsonParser.colWrite(item14, keyname, "item14", item_obj);
											JsonParser.colWrite(item15, keyname, "item15", item_obj);
											JsonParser.colWrite(item16, keyname, "item16", item_obj);
											JsonParser.colWrite(item17, keyname, "item17", item_obj);
											JsonParser.colWrite(item18, keyname, "item18", item_obj);
											JsonParser.colWrite(item19, keyname, "item19", item_obj);
											JsonParser.colWrite(item20, keyname, "item20", item_obj);
											JsonParser.colWrite(item21, keyname, "item21", item_obj);
											JsonParser.colWrite(item22, keyname, "item22", item_obj);
											JsonParser.colWrite(item23, keyname, "item23", item_obj);
											JsonParser.colWrite(item24, keyname, "item24", item_obj);
											JsonParser.colWrite(item25, keyname, "item25", item_obj);
											JsonParser.colWrite(item26, keyname, "item26", item_obj);
											JsonParser.colWrite(item27, keyname, "item27", item_obj);
											JsonParser.colWrite(item28, keyname, "item28", item_obj);
											JsonParser.colWrite(item29, keyname, "item29", item_obj);
											JsonParser.colWrite(item30, keyname, "item30", item_obj);
											JsonParser.colWrite(item31, keyname, "item31", item_obj);
											JsonParser.colWrite(item32, keyname, "item32", item_obj);
											JsonParser.colWrite(item33, keyname, "item33", item_obj);
											JsonParser.colWrite(item34, keyname, "item34", item_obj);
											JsonParser.colWrite(item35, keyname, "item35", item_obj);
											JsonParser.colWrite(item36, keyname, "item36", item_obj);
											JsonParser.colWrite(item37, keyname, "item37", item_obj);
											JsonParser.colWrite(item38, keyname, "item38", item_obj);
											JsonParser.colWrite(item39, keyname, "item39", item_obj);
											JsonParser.colWrite(item40, keyname, "item40", item_obj);
											JsonParser.colWrite(item41, keyname, "item41", item_obj);
											JsonParser.colWrite(item42, keyname, "item42", item_obj);
											JsonParser.colWrite(item43, keyname, "item43", item_obj);
											JsonParser.colWrite(item44, keyname, "item44", item_obj);
											JsonParser.colWrite(item45, keyname, "item45", item_obj);
											JsonParser.colWrite(item46, keyname, "item46", item_obj);
											JsonParser.colWrite(item47, keyname, "item47", item_obj);
											JsonParser.colWrite(item48, keyname, "item48", item_obj);
											JsonParser.colWrite(item49, keyname, "item49", item_obj);
											JsonParser.colWrite(item50, keyname, "item50", item_obj);
											JsonParser.colWrite(item51, keyname, "item51", item_obj);
											JsonParser.colWrite(item52, keyname, "item52", item_obj);
											JsonParser.colWrite(item53, keyname, "item53", item_obj);
											JsonParser.colWrite(item54, keyname, "item54", item_obj);
											JsonParser.colWrite(item55, keyname, "item55", item_obj);
											JsonParser.colWrite(item56, keyname, "item56", item_obj);
											JsonParser.colWrite(item57, keyname, "item57", item_obj);
											JsonParser.colWrite(item58, keyname, "item58", item_obj);
											JsonParser.colWrite(item59, keyname, "item59", item_obj);
											JsonParser.colWrite(item60, keyname, "item60", item_obj);

										}

										// 한번에 문자열 합침
										resultSb.append(args[0]);
										resultSb.append("|^");
										resultSb.append(args[1]);
										resultSb.append("|^");
										resultSb.append(args[2]);
										resultSb.append("|^");
										resultSb.append(mesurede);
										resultSb.append("|^");
										resultSb.append(item1);
										resultSb.append("|^");
										resultSb.append(item2);
										resultSb.append("|^");
										resultSb.append(item3);
										resultSb.append("|^");
										resultSb.append(item5);
										resultSb.append("|^");
										resultSb.append(item6);
										resultSb.append("|^");
										resultSb.append(item7);
										resultSb.append("|^");
										resultSb.append(item8);
										resultSb.append("|^");
										resultSb.append(item9);
										resultSb.append("|^");
										resultSb.append(item10);
										resultSb.append("|^");
										resultSb.append(item11);
										resultSb.append("|^");
										resultSb.append(item12);
										resultSb.append("|^");
										resultSb.append(item13);
										resultSb.append("|^");
										resultSb.append(item14);
										resultSb.append("|^");
										resultSb.append(item15);
										resultSb.append("|^");
										resultSb.append(item16);
										resultSb.append("|^");
										resultSb.append(item17);
										resultSb.append("|^");
										resultSb.append(item18);
										resultSb.append("|^");
										resultSb.append(item19);
										resultSb.append("|^");
										resultSb.append(item20);
										resultSb.append("|^");
										resultSb.append(item21);
										resultSb.append("|^");
										resultSb.append(item22);
										resultSb.append("|^");
										resultSb.append(item23);
										resultSb.append("|^");
										resultSb.append(item24);
										resultSb.append("|^");
										resultSb.append(item25);
										resultSb.append("|^");
										resultSb.append(item26);
										resultSb.append("|^");
										resultSb.append(item27);
										resultSb.append("|^");
										resultSb.append(item28);
										resultSb.append("|^");
										resultSb.append(item29);
										resultSb.append("|^");
										resultSb.append(item30);
										resultSb.append("|^");
										resultSb.append(item31);
										resultSb.append("|^");
										resultSb.append(item32);
										resultSb.append("|^");
										resultSb.append(item33);
										resultSb.append("|^");
										resultSb.append(item34);
										resultSb.append("|^");
										resultSb.append(item35);
										resultSb.append("|^");
										resultSb.append(item36);
										resultSb.append("|^");
										resultSb.append(item37);
										resultSb.append("|^");
										resultSb.append(item38);
										resultSb.append("|^");
										resultSb.append(item39);
										resultSb.append("|^");
										resultSb.append(item40);
										resultSb.append("|^");
										resultSb.append(item41);
										resultSb.append("|^");
										resultSb.append(item42);
										resultSb.append("|^");
										resultSb.append(item43);
										resultSb.append("|^");
										resultSb.append(item44);
										resultSb.append("|^");
										resultSb.append(item45);
										resultSb.append("|^");
										resultSb.append(item46);
										resultSb.append("|^");
										resultSb.append(item47);
										resultSb.append("|^");
										resultSb.append(item48);
										resultSb.append("|^");
										resultSb.append(item49);
										resultSb.append("|^");
										resultSb.append(item50);
										resultSb.append("|^");
										resultSb.append(item51);
										resultSb.append("|^");
										resultSb.append(item52);
										resultSb.append("|^");
										resultSb.append(item53);
										resultSb.append("|^");
										resultSb.append(item54);
										resultSb.append("|^");
										resultSb.append(item55);
										resultSb.append("|^");
										resultSb.append(item56);
										resultSb.append("|^");
										resultSb.append(item57);
										resultSb.append("|^");
										resultSb.append(item58);
										resultSb.append("|^");
										resultSb.append(item59);
										resultSb.append("|^");
										resultSb.append(item60);
										resultSb.append("|^");
										resultSb.append(numOfRows_str);
										resultSb.append("|^");
										resultSb.append(String.valueOf(i));
										resultSb.append(System.getProperty("line.separator"));

									}

								} else {
									System.out.println("parsing error!!");
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_22.dat", "WRS");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else {
						System.out.println("파라미터 개수 에러!!");
						System.exit(-1);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("fcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
			}



	}

}
