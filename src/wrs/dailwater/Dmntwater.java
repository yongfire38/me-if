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

import common.JsonParser;
//import common.TransSftp;

public class Dmntwater {

	// 광역정수장 수질정보 조회 서비스 - 광역월간 수돗물 수질 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

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

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;

						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWriJson_month_obj(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1], args[2]);
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

							JSONObject obj = JsonParser.parseWriJson_month_obj(service_url, service_key, String.valueOf(i), args[0],
									args[1], args[2]);
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

								String numOfRows_str = body.get("numOfRows").toString();

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {
									
									String mesurede = " ";
									String item1 = " ";
									String item2 = " ";
									String item3 = " ";
									String item5 = " ";
									String item6 = " ";
									String item7 = " ";
									String item8 = " ";
									String item9 = " ";
									String item10 = " ";
									String item11 = " ";
									String item12 = " ";
									String item13 = " ";
									String item14 = " ";
									String item15 = " ";
									String item16 = " ";
									String item17 = " ";
									String item18 = " ";
									String item19 = " ";
									String item20 = " ";
									String item21 = " ";
									String item22 = " ";
									String item23 = " ";
									String item24 = " ";
									String item25 = " ";
									String item26 = " ";
									String item27 = " ";
									String item28 = " ";
									String item29 = " ";
									String item30 = " ";
									String item31 = " ";
									String item32 = " ";
									String item33 = " ";
									String item34 = " ";
									String item35 = " ";
									String item36 = " ";
									String item37 = " ";
									String item38 = " ";
									String item39 = " ";
									String item40 = " ";
									String item41 = " ";
									String item42 = " ";
									String item43 = " ";
									String item44 = " ";
									String item45 = " ";
									String item46 = " ";
									String item47 = " ";
									String item48 = " ";
									String item49 = " ";
									String item50 = " ";
									String item51 = " ";
									String item52 = " ";
									String item53 = " ";
									String item54 = " ";
									String item55 = " ";
									String item56 = " ";
									String item57 = " ";
									String item58 = " ";
									String item59 = " ";
									String item60 = " ";

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										mesurede = JsonParser.colWrite_String(mesurede, keyname, "mesurede", items_jsonObject);
										item1 = JsonParser.colWrite_String(item1, keyname, "item1", items_jsonObject);
										item2 = JsonParser.colWrite_String(item2, keyname, "item2", items_jsonObject);
										item3 = JsonParser.colWrite_String(item3, keyname, "item3", items_jsonObject);
										item5 = JsonParser.colWrite_String(item5, keyname, "item5", items_jsonObject);
										item6 = JsonParser.colWrite_String(item6, keyname, "item6", items_jsonObject);
										item7 = JsonParser.colWrite_String(item7, keyname, "item7", items_jsonObject);
										item8 = JsonParser.colWrite_String(item8, keyname, "item8", items_jsonObject);
										item9 = JsonParser.colWrite_String(item9, keyname, "item9", items_jsonObject);
										item10 = JsonParser.colWrite_String(item10, keyname, "item10", items_jsonObject);
										item11 = JsonParser.colWrite_String(item11, keyname, "item11", items_jsonObject);
										item12 = JsonParser.colWrite_String(item12, keyname, "item12", items_jsonObject);
										item13 = JsonParser.colWrite_String(item13, keyname, "item13", items_jsonObject);
										item14 = JsonParser.colWrite_String(item14, keyname, "item14", items_jsonObject);
										item15 = JsonParser.colWrite_String(item15, keyname, "item15", items_jsonObject);
										item16 = JsonParser.colWrite_String(item16, keyname, "item16", items_jsonObject);
										item17 = JsonParser.colWrite_String(item17, keyname, "item17", items_jsonObject);
										item18 = JsonParser.colWrite_String(item18, keyname, "item18", items_jsonObject);
										item19 = JsonParser.colWrite_String(item19, keyname, "item19", items_jsonObject);
										item20 = JsonParser.colWrite_String(item20, keyname, "item20", items_jsonObject);
										item21 = JsonParser.colWrite_String(item21, keyname, "item21", items_jsonObject);
										item22 = JsonParser.colWrite_String(item22, keyname, "item22", items_jsonObject);
										item23 = JsonParser.colWrite_String(item23, keyname, "item23", items_jsonObject);
										item24 = JsonParser.colWrite_String(item24, keyname, "item24", items_jsonObject);
										item25 = JsonParser.colWrite_String(item25, keyname, "item25", items_jsonObject);
										item26 = JsonParser.colWrite_String(item26, keyname, "item26", items_jsonObject);
										item27 = JsonParser.colWrite_String(item27, keyname, "item27", items_jsonObject);
										item28 = JsonParser.colWrite_String(item28, keyname, "item28", items_jsonObject);
										item29 = JsonParser.colWrite_String(item29, keyname, "item29", items_jsonObject);
										item30 = JsonParser.colWrite_String(item30, keyname, "item30", items_jsonObject);
										item31 = JsonParser.colWrite_String(item31, keyname, "item31", items_jsonObject);
										item32 = JsonParser.colWrite_String(item32, keyname, "item32", items_jsonObject);
										item33 = JsonParser.colWrite_String(item33, keyname, "item33", items_jsonObject);
										item34 = JsonParser.colWrite_String(item34, keyname, "item34", items_jsonObject);
										item35 = JsonParser.colWrite_String(item35, keyname, "item35", items_jsonObject);
										item36 = JsonParser.colWrite_String(item36, keyname, "item36", items_jsonObject);
										item37 = JsonParser.colWrite_String(item37, keyname, "item37", items_jsonObject);
										item38 = JsonParser.colWrite_String(item38, keyname, "item38", items_jsonObject);
										item39 = JsonParser.colWrite_String(item39, keyname, "item39", items_jsonObject);
										item40 = JsonParser.colWrite_String(item40, keyname, "item40", items_jsonObject);
										item41 = JsonParser.colWrite_String(item41, keyname, "item41", items_jsonObject);
										item42 = JsonParser.colWrite_String(item42, keyname, "item42", items_jsonObject);
										item43 = JsonParser.colWrite_String(item43, keyname, "item43", items_jsonObject);
										item44 = JsonParser.colWrite_String(item44, keyname, "item44", items_jsonObject);
										item45 = JsonParser.colWrite_String(item45, keyname, "item45", items_jsonObject);
										item46 = JsonParser.colWrite_String(item46, keyname, "item46", items_jsonObject);
										item47 = JsonParser.colWrite_String(item47, keyname, "item47", items_jsonObject);
										item48 = JsonParser.colWrite_String(item48, keyname, "item48", items_jsonObject);
										item49 = JsonParser.colWrite_String(item49, keyname, "item49", items_jsonObject);
										item50 = JsonParser.colWrite_String(item50, keyname, "item50", items_jsonObject);
										item51 = JsonParser.colWrite_String(item51, keyname, "item51", items_jsonObject);
										item52 = JsonParser.colWrite_String(item52, keyname, "item52", items_jsonObject);
										item53 = JsonParser.colWrite_String(item53, keyname, "item53", items_jsonObject);
										item54 = JsonParser.colWrite_String(item54, keyname, "item54", items_jsonObject);
										item55 = JsonParser.colWrite_String(item55, keyname, "item55", items_jsonObject);
										item56 = JsonParser.colWrite_String(item56, keyname, "item56", items_jsonObject);
										item57 = JsonParser.colWrite_String(item57, keyname, "item57", items_jsonObject);
										item58 = JsonParser.colWrite_String(item58, keyname, "item58", items_jsonObject);
										item59 = JsonParser.colWrite_String(item59, keyname, "item59", items_jsonObject);
										item60 = JsonParser.colWrite_String(item60, keyname, "item60", items_jsonObject);

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
										pw.write(mesurede);
										pw.write("|^");
										pw.write(item1);
										pw.write("|^");
										pw.write(item2);
										pw.write("|^");
										pw.write(item3);
										pw.write("|^");
										pw.write(item5);
										pw.write("|^");
										pw.write(item6);
										pw.write("|^");
										pw.write(item7);
										pw.write("|^");
										pw.write(item8);
										pw.write("|^");
										pw.write(item9);
										pw.write("|^");
										pw.write(item10);
										pw.write("|^");
										pw.write(item11);
										pw.write("|^");
										pw.write(item12);
										pw.write("|^");
										pw.write(item13);
										pw.write("|^");
										pw.write(item14);
										pw.write("|^");
										pw.write(item15);
										pw.write("|^");
										pw.write(item16);
										pw.write("|^");
										pw.write(item17);
										pw.write("|^");
										pw.write(item18);
										pw.write("|^");
										pw.write(item19);
										pw.write("|^");
										pw.write(item20);
										pw.write("|^");
										pw.write(item21);
										pw.write("|^");
										pw.write(item22);
										pw.write("|^");
										pw.write(item23);
										pw.write("|^");
										pw.write(item24);
										pw.write("|^");
										pw.write(item25);
										pw.write("|^");
										pw.write(item26);
										pw.write("|^");
										pw.write(item27);
										pw.write("|^");
										pw.write(item28);
										pw.write("|^");
										pw.write(item29);
										pw.write("|^");
										pw.write(item30);
										pw.write("|^");
										pw.write(item31);
										pw.write("|^");
										pw.write(item32);
										pw.write("|^");
										pw.write(item33);
										pw.write("|^");
										pw.write(item34);
										pw.write("|^");
										pw.write(item35);
										pw.write("|^");
										pw.write(item36);
										pw.write("|^");
										pw.write(item37);
										pw.write("|^");
										pw.write(item38);
										pw.write("|^");
										pw.write(item39);
										pw.write("|^");
										pw.write(item40);
										pw.write("|^");
										pw.write(item41);
										pw.write("|^");
										pw.write(item42);
										pw.write("|^");
										pw.write(item43);
										pw.write("|^");
										pw.write(item44);
										pw.write("|^");
										pw.write(item45);
										pw.write("|^");
										pw.write(item46);
										pw.write("|^");
										pw.write(item47);
										pw.write("|^");
										pw.write(item48);
										pw.write("|^");
										pw.write(item49);
										pw.write("|^");
										pw.write(item50);
										pw.write("|^");
										pw.write(item51);
										pw.write("|^");
										pw.write(item52);
										pw.write("|^");
										pw.write(item53);
										pw.write("|^");
										pw.write(item54);
										pw.write("|^");
										pw.write(item55);
										pw.write("|^");
										pw.write(item56);
										pw.write("|^");
										pw.write(item57);
										pw.write("|^");
										pw.write(item58);
										pw.write("|^");
										pw.write(item59);
										pw.write("|^");
										pw.write(item60);
										pw.write("|^");
										pw.write(numOfRows_str);
										pw.write("|^");
										pw.write(String.valueOf(i));
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String mesurede = " ";
										String item1 = " ";
										String item2 = " ";
										String item3 = " ";
										String item5 = " ";
										String item6 = " ";
										String item7 = " ";
										String item8 = " ";
										String item9 = " ";
										String item10 = " ";
										String item11 = " ";
										String item12 = " ";
										String item13 = " ";
										String item14 = " ";
										String item15 = " ";
										String item16 = " ";
										String item17 = " ";
										String item18 = " ";
										String item19 = " ";
										String item20 = " ";
										String item21 = " ";
										String item22 = " ";
										String item23 = " ";
										String item24 = " ";
										String item25 = " ";
										String item26 = " ";
										String item27 = " ";
										String item28 = " ";
										String item29 = " ";
										String item30 = " ";
										String item31 = " ";
										String item32 = " ";
										String item33 = " ";
										String item34 = " ";
										String item35 = " ";
										String item36 = " ";
										String item37 = " ";
										String item38 = " ";
										String item39 = " ";
										String item40 = " ";
										String item41 = " ";
										String item42 = " ";
										String item43 = " ";
										String item44 = " ";
										String item45 = " ";
										String item46 = " ";
										String item47 = " ";
										String item48 = " ";
										String item49 = " ";
										String item50 = " ";
										String item51 = " ";
										String item52 = " ";
										String item53 = " ";
										String item54 = " ";
										String item55 = " ";
										String item56 = " ";
										String item57 = " ";
										String item58 = " ";
										String item59 = " ";
										String item60 = " ";

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											mesurede = JsonParser.colWrite_String(mesurede, keyname, "mesurede", item_obj);
											item1 = JsonParser.colWrite_String(item1, keyname, "item1", item_obj);
											item2 = JsonParser.colWrite_String(item2, keyname, "item2", item_obj);
											item3 = JsonParser.colWrite_String(item3, keyname, "item3", item_obj);
											item5 = JsonParser.colWrite_String(item5, keyname, "item5", item_obj);
											item6 = JsonParser.colWrite_String(item6, keyname, "item6", item_obj);
											item7 = JsonParser.colWrite_String(item7, keyname, "item7", item_obj);
											item8 = JsonParser.colWrite_String(item8, keyname, "item8", item_obj);
											item9 = JsonParser.colWrite_String(item9, keyname, "item9", item_obj);
											item10 = JsonParser.colWrite_String(item10, keyname, "item10", item_obj);
											item11 = JsonParser.colWrite_String(item11, keyname, "item11", item_obj);
											item12 = JsonParser.colWrite_String(item12, keyname, "item12", item_obj);
											item13 = JsonParser.colWrite_String(item13, keyname, "item13", item_obj);
											item14 = JsonParser.colWrite_String(item14, keyname, "item14", item_obj);
											item15 = JsonParser.colWrite_String(item15, keyname, "item15", item_obj);
											item16 = JsonParser.colWrite_String(item16, keyname, "item16", item_obj);
											item17 = JsonParser.colWrite_String(item17, keyname, "item17", item_obj);
											item18 = JsonParser.colWrite_String(item18, keyname, "item18", item_obj);
											item19 = JsonParser.colWrite_String(item19, keyname, "item19", item_obj);
											item20 = JsonParser.colWrite_String(item20, keyname, "item20", item_obj);
											item21 = JsonParser.colWrite_String(item21, keyname, "item21", item_obj);
											item22 = JsonParser.colWrite_String(item22, keyname, "item22", item_obj);
											item23 = JsonParser.colWrite_String(item23, keyname, "item23", item_obj);
											item24 = JsonParser.colWrite_String(item24, keyname, "item24", item_obj);
											item25 = JsonParser.colWrite_String(item25, keyname, "item25", item_obj);
											item26 = JsonParser.colWrite_String(item26, keyname, "item26", item_obj);
											item27 = JsonParser.colWrite_String(item27, keyname, "item27", item_obj);
											item28 = JsonParser.colWrite_String(item28, keyname, "item28", item_obj);
											item29 = JsonParser.colWrite_String(item29, keyname, "item29", item_obj);
											item30 = JsonParser.colWrite_String(item30, keyname, "item30", item_obj);
											item31 = JsonParser.colWrite_String(item31, keyname, "item31", item_obj);
											item32 = JsonParser.colWrite_String(item32, keyname, "item32", item_obj);
											item33 = JsonParser.colWrite_String(item33, keyname, "item33", item_obj);
											item34 = JsonParser.colWrite_String(item34, keyname, "item34", item_obj);
											item35 = JsonParser.colWrite_String(item35, keyname, "item35", item_obj);
											item36 = JsonParser.colWrite_String(item36, keyname, "item36", item_obj);
											item37 = JsonParser.colWrite_String(item37, keyname, "item37", item_obj);
											item38 = JsonParser.colWrite_String(item38, keyname, "item38", item_obj);
											item39 = JsonParser.colWrite_String(item39, keyname, "item39", item_obj);
											item40 = JsonParser.colWrite_String(item40, keyname, "item40", item_obj);
											item41 = JsonParser.colWrite_String(item41, keyname, "item41", item_obj);
											item42 = JsonParser.colWrite_String(item42, keyname, "item42", item_obj);
											item43 = JsonParser.colWrite_String(item43, keyname, "item43", item_obj);
											item44 = JsonParser.colWrite_String(item44, keyname, "item44", item_obj);
											item45 = JsonParser.colWrite_String(item45, keyname, "item45", item_obj);
											item46 = JsonParser.colWrite_String(item46, keyname, "item46", item_obj);
											item47 = JsonParser.colWrite_String(item47, keyname, "item47", item_obj);
											item48 = JsonParser.colWrite_String(item48, keyname, "item48", item_obj);
											item49 = JsonParser.colWrite_String(item49, keyname, "item49", item_obj);
											item50 = JsonParser.colWrite_String(item50, keyname, "item50", item_obj);
											item51 = JsonParser.colWrite_String(item51, keyname, "item51", item_obj);
											item52 = JsonParser.colWrite_String(item52, keyname, "item52", item_obj);
											item53 = JsonParser.colWrite_String(item53, keyname, "item53", item_obj);
											item54 = JsonParser.colWrite_String(item54, keyname, "item54", item_obj);
											item55 = JsonParser.colWrite_String(item55, keyname, "item55", item_obj);
											item56 = JsonParser.colWrite_String(item56, keyname, "item56", item_obj);
											item57 = JsonParser.colWrite_String(item57, keyname, "item57", item_obj);
											item58 = JsonParser.colWrite_String(item58, keyname, "item58", item_obj);
											item59 = JsonParser.colWrite_String(item59, keyname, "item59", item_obj);
											item60 = JsonParser.colWrite_String(item60, keyname, "item60", item_obj);

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
											pw.write(mesurede);
											pw.write("|^");
											pw.write(item1);
											pw.write("|^");
											pw.write(item2);
											pw.write("|^");
											pw.write(item3);
											pw.write("|^");
											pw.write(item5);
											pw.write("|^");
											pw.write(item6);
											pw.write("|^");
											pw.write(item7);
											pw.write("|^");
											pw.write(item8);
											pw.write("|^");
											pw.write(item9);
											pw.write("|^");
											pw.write(item10);
											pw.write("|^");
											pw.write(item11);
											pw.write("|^");
											pw.write(item12);
											pw.write("|^");
											pw.write(item13);
											pw.write("|^");
											pw.write(item14);
											pw.write("|^");
											pw.write(item15);
											pw.write("|^");
											pw.write(item16);
											pw.write("|^");
											pw.write(item17);
											pw.write("|^");
											pw.write(item18);
											pw.write("|^");
											pw.write(item19);
											pw.write("|^");
											pw.write(item20);
											pw.write("|^");
											pw.write(item21);
											pw.write("|^");
											pw.write(item22);
											pw.write("|^");
											pw.write(item23);
											pw.write("|^");
											pw.write(item24);
											pw.write("|^");
											pw.write(item25);
											pw.write("|^");
											pw.write(item26);
											pw.write("|^");
											pw.write(item27);
											pw.write("|^");
											pw.write(item28);
											pw.write("|^");
											pw.write(item29);
											pw.write("|^");
											pw.write(item30);
											pw.write("|^");
											pw.write(item31);
											pw.write("|^");
											pw.write(item32);
											pw.write("|^");
											pw.write(item33);
											pw.write("|^");
											pw.write(item34);
											pw.write("|^");
											pw.write(item35);
											pw.write("|^");
											pw.write(item36);
											pw.write("|^");
											pw.write(item37);
											pw.write("|^");
											pw.write(item38);
											pw.write("|^");
											pw.write(item39);
											pw.write("|^");
											pw.write(item40);
											pw.write("|^");
											pw.write(item41);
											pw.write("|^");
											pw.write(item42);
											pw.write("|^");
											pw.write(item43);
											pw.write("|^");
											pw.write(item44);
											pw.write("|^");
											pw.write(item45);
											pw.write("|^");
											pw.write(item46);
											pw.write("|^");
											pw.write(item47);
											pw.write("|^");
											pw.write(item48);
											pw.write("|^");
											pw.write(item49);
											pw.write("|^");
											pw.write(item50);
											pw.write("|^");
											pw.write(item51);
											pw.write("|^");
											pw.write(item52);
											pw.write("|^");
											pw.write(item53);
											pw.write("|^");
											pw.write(item54);
											pw.write("|^");
											pw.write(item55);
											pw.write("|^");
											pw.write(item56);
											pw.write("|^");
											pw.write(item57);
											pw.write("|^");
											pw.write(item58);
											pw.write("|^");
											pw.write(item59);
											pw.write("|^");
											pw.write(item60);
											pw.write("|^");
											pw.write(numOfRows_str);
											pw.write("|^");
											pw.write(String.valueOf(i));
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

							//Thread.sleep(2000);

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
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() + "fcode :" + args[0] + ": stdt :" + args[1] + ": eddt :" + args[2]);
				System.exit(-1);
			}



	}

}
