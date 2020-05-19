package wrs.waterinfos;

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

public class Winfosmonthwater {

	// 지방정수장 수질정보 조회 서비스 - 지방상수도수질(월간)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 요청 파라미터는 조회 시작일과 조회 종료일의 2개
				if (args.length == 2) {

					if (args[0].length() == 6 && args[1].length() == 6) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("waterinfos_Winfosmonthwater_url");
						String service_key = JsonParser.getProperty("waterinfos_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_14.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
						String json = "";

						int pageNo = 0;
						int pageCount = 0;

						// yyyyMM이므로 기존 메서드 이용 불가능.. 하나 새로 만들어서 처리
						json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0],
								args[1]);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
						}

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

						for (int i = 1; i <= pageCount; i++) {

							json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0],
									args[1]);
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\",\"numOfRows\":10,\"pageNo\":1,\"totalCount\":0}}}";
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
							} else if (body.get("items") instanceof String) {
								System.out.println("data not exist!!");
							} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {

								JSONObject items = (JSONObject) body.get("items");

								// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
								if (items.get("item") instanceof JSONObject) {
									
									String sgcnm = " "; // 지자체명
									String sitenm = " "; // 정수장명
									String cltdt = " "; // 측정일자
									String data1 = " "; // 일반세균
									String data2 = " "; // 총대장균군
									String data3 = " "; // 대장균(E.coil)
									String data4 = " "; // 납(Pb)
									String data5 = " "; // 불소(F)
									String data6 = " "; // 비소
									String data7 = " "; // 셀레늄(Se)
									String data8 = " "; // 수은(Hg)
									String data9 = " "; // 시안(CN)
									String data10 = " "; // 크롬(Cr)
									String data11 = " "; // 암모니아성질소
									String data12 = " "; // 질산성질소
									String data13 = " "; // 보론(B)
									String data14 = " "; // 카드뮴(Cd)
									String data15 = " "; // 페놀
									String data16 = " "; // 총트리할로메탄(THMs)
									String data17 = " "; // 클로로포름
									String data18 = " "; // 다이아지논
									String data19 = " "; // 파라티온
									String data20 = " "; // 페니트로티온
									String data21 = " "; // 카바릴
									String data22 = " "; // 1,1,1-트리클로로에탄
									String data23 = " "; // 테트라클로로에틸렌(PCE)
									String data24 = " "; // 트리클로로에틸렌(TCE)
									String data25 = " "; // 디클로로메탄
									String data26 = " "; // 벤젠
									String data27 = " "; // 톨루엔
									String data28 = " "; // 에틸벤젠
									String data29 = " "; // 크실렌
									String data30 = " "; // 1,1디클로로에틸렌
									String data31 = " "; // 사염화탄소
									String data32 = " "; // 1,2-디브로모-3-클로로프로판
									String data33 = " "; // 잔류염소
									String data34 = " "; // 클로랄하이드레이트(CH)
									String data35 = " "; // 디브로모아세토니트릴
									String data36 = " "; // 디클로로아세토니트릴
									String data37 = " "; // 트리클로로아세토니트릴
									String data38 = " "; // 할로아세틱에시드(HAAs)
									String data39 = " "; // 경도
									String data40 = " "; // 과망간산칼륨소비량
									String data41 = " "; // 냄새
									String data42 = " "; // 맛
									String data43 = " "; // 구리(Cu)
									String data44 = " "; // 색도
									String data45 = " "; // 세제(음이온계면활성제:ABS)
									String data46 = " "; // 수소이온농도(pH)
									String data47 = " "; // 아연(Zn)
									String data48 = " "; // 염소이온
									String data49 = " "; // 증발잔류물(Totalsolids)
									String data50 = " "; // 철(Fe)
									String data51 = " "; // 망간(Mn)
									String data52 = " "; // 탁도(Turbidity)
									String data53 = " "; // 황산이온
									String data54 = " "; // 알루미늄(Al)
									String data55 = " "; // 브로모디클로로메탄
									String data56 = " "; // 디브로모클로로메탄
									String data57 = " "; // 1,4-다이옥산
									String data58 = " "; // 포름알데히드
									String data59 = " "; // 브롬산염

									JSONObject items_jsonObject = (JSONObject) items.get("item");

									Set<String> key = items_jsonObject.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										sgcnm = JsonParser.colWrite_String(sgcnm, keyname, "sgcnm", items_jsonObject);
										sitenm = JsonParser.colWrite_String(sitenm, keyname, "sitenm", items_jsonObject);
										cltdt = JsonParser.colWrite_String(cltdt, keyname, "cltdt", items_jsonObject);
										data1 = JsonParser.colWrite_String(data1, keyname, "data1", items_jsonObject);
										data2 = JsonParser.colWrite_String(data2, keyname, "data2", items_jsonObject);
										data3 = JsonParser.colWrite_String(data3, keyname, "data3", items_jsonObject);
										data4 = JsonParser.colWrite_String(data4, keyname, "data4", items_jsonObject);
										data5 = JsonParser.colWrite_String(data5, keyname, "data5", items_jsonObject);
										data6 = JsonParser.colWrite_String(data6, keyname, "data6", items_jsonObject);
										data7 = JsonParser.colWrite_String(data7, keyname, "data7", items_jsonObject);
										data8 = JsonParser.colWrite_String(data8, keyname, "data8", items_jsonObject);
										data9 = JsonParser.colWrite_String(data9, keyname, "data9", items_jsonObject);
										data10 = JsonParser.colWrite_String(data10, keyname, "data10", items_jsonObject);
										data11 = JsonParser.colWrite_String(data11, keyname, "data11", items_jsonObject);
										data12 = JsonParser.colWrite_String(data12, keyname, "data12", items_jsonObject);
										data13 = JsonParser.colWrite_String(data13, keyname, "data13", items_jsonObject);
										data14 = JsonParser.colWrite_String(data14, keyname, "data14", items_jsonObject);
										data15 = JsonParser.colWrite_String(data15, keyname, "data15", items_jsonObject);
										data16 = JsonParser.colWrite_String(data16, keyname, "data16", items_jsonObject);
										data17 = JsonParser.colWrite_String(data17, keyname, "data17", items_jsonObject);
										data18 = JsonParser.colWrite_String(data18, keyname, "data18", items_jsonObject);
										data19 = JsonParser.colWrite_String(data19, keyname, "data19", items_jsonObject);
										data20 = JsonParser.colWrite_String(data20, keyname, "data20", items_jsonObject);
										data21 = JsonParser.colWrite_String(data21, keyname, "data21", items_jsonObject);
										data22 = JsonParser.colWrite_String(data22, keyname, "data22", items_jsonObject);
										data23 = JsonParser.colWrite_String(data23, keyname, "data23", items_jsonObject);
										data24 = JsonParser.colWrite_String(data24, keyname, "data24", items_jsonObject);
										data25 = JsonParser.colWrite_String(data25, keyname, "data25", items_jsonObject);
										data26 = JsonParser.colWrite_String(data26, keyname, "data26", items_jsonObject);
										data27 = JsonParser.colWrite_String(data27, keyname, "data27", items_jsonObject);
										data28 = JsonParser.colWrite_String(data28, keyname, "data28", items_jsonObject);
										data29 = JsonParser.colWrite_String(data29, keyname, "data29", items_jsonObject);
										data30 = JsonParser.colWrite_String(data30, keyname, "data30", items_jsonObject);
										data31 = JsonParser.colWrite_String(data31, keyname, "data31", items_jsonObject);
										data32 = JsonParser.colWrite_String(data32, keyname, "data32", items_jsonObject);
										data33 = JsonParser.colWrite_String(data33, keyname, "data33", items_jsonObject);
										data34 = JsonParser.colWrite_String(data34, keyname, "data34", items_jsonObject);
										data35 = JsonParser.colWrite_String(data35, keyname, "data35", items_jsonObject);
										data36 = JsonParser.colWrite_String(data36, keyname, "data36", items_jsonObject);
										data37 = JsonParser.colWrite_String(data37, keyname, "data37", items_jsonObject);
										data38 = JsonParser.colWrite_String(data38, keyname, "data38", items_jsonObject);
										data39 = JsonParser.colWrite_String(data39, keyname, "data39", items_jsonObject);
										data40 = JsonParser.colWrite_String(data40, keyname, "data40", items_jsonObject);
										data41 = JsonParser.colWrite_String(data41, keyname, "data41", items_jsonObject);
										data42 = JsonParser.colWrite_String(data42, keyname, "data42", items_jsonObject);
										data43 = JsonParser.colWrite_String(data43, keyname, "data43", items_jsonObject);
										data44 = JsonParser.colWrite_String(data44, keyname, "data44", items_jsonObject);
										data45 = JsonParser.colWrite_String(data45, keyname, "data45", items_jsonObject);
										data46 = JsonParser.colWrite_String(data46, keyname, "data46", items_jsonObject);
										data47 = JsonParser.colWrite_String(data47, keyname, "data47", items_jsonObject);
										data48 = JsonParser.colWrite_String(data48, keyname, "data48", items_jsonObject);
										data49 = JsonParser.colWrite_String(data49, keyname, "data49", items_jsonObject);
										data50 = JsonParser.colWrite_String(data50, keyname, "data50", items_jsonObject);
										data51 = JsonParser.colWrite_String(data51, keyname, "data51", items_jsonObject);
										data52 = JsonParser.colWrite_String(data52, keyname, "data52", items_jsonObject);
										data53 = JsonParser.colWrite_String(data53, keyname, "data53", items_jsonObject);
										data54 = JsonParser.colWrite_String(data54, keyname, "data54", items_jsonObject);
										data55 = JsonParser.colWrite_String(data55, keyname, "data55", items_jsonObject);
										data56 = JsonParser.colWrite_String(data56, keyname, "data56", items_jsonObject);
										data57 = JsonParser.colWrite_String(data57, keyname, "data57", items_jsonObject);
										data58 = JsonParser.colWrite_String(data58, keyname, "data58", items_jsonObject);
										data59 = JsonParser.colWrite_String(data59, keyname, "data59", items_jsonObject);

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(sgcnm);
										pw.write("|^");
										pw.write(sitenm);
										pw.write("|^");
										pw.write(cltdt);
										pw.write("|^");
										pw.write(data1);
										pw.write("|^");
										pw.write(data2);
										pw.write("|^");
										pw.write(data3);
										pw.write("|^");
										pw.write(data4);
										pw.write("|^");
										pw.write(data5);
										pw.write("|^");
										pw.write(data6);
										pw.write("|^");
										pw.write(data7);
										pw.write("|^");
										pw.write(data8);
										pw.write("|^");
										pw.write(data9);
										pw.write("|^");
										pw.write(data10);
										pw.write("|^");
										pw.write(data11);
										pw.write("|^");
										pw.write(data12);
										pw.write("|^");
										pw.write(data13);
										pw.write("|^");
										pw.write(data14);
										pw.write("|^");
										pw.write(data15);
										pw.write("|^");
										pw.write(data16);
										pw.write("|^");
										pw.write(data17);
										pw.write("|^");
										pw.write(data18);
										pw.write("|^");
										pw.write(data19);
										pw.write("|^");
										pw.write(data20);
										pw.write("|^");
										pw.write(data21);
										pw.write("|^");
										pw.write(data22);
										pw.write("|^");
										pw.write(data23);
										pw.write("|^");
										pw.write(data24);
										pw.write("|^");
										pw.write(data25);
										pw.write("|^");
										pw.write(data26);
										pw.write("|^");
										pw.write(data27);
										pw.write("|^");
										pw.write(data28);
										pw.write("|^");
										pw.write(data29);
										pw.write("|^");
										pw.write(data30);
										pw.write("|^");
										pw.write(data31);
										pw.write("|^");
										pw.write(data32);
										pw.write("|^");
										pw.write(data33);
										pw.write("|^");
										pw.write(data34);
										pw.write("|^");
										pw.write(data35);
										pw.write("|^");
										pw.write(data36);
										pw.write("|^");
										pw.write(data37);
										pw.write("|^");
										pw.write(data38);
										pw.write("|^");
										pw.write(data39);
										pw.write("|^");
										pw.write(data40);
										pw.write("|^");
										pw.write(data41);
										pw.write("|^");
										pw.write(data42);
										pw.write("|^");
										pw.write(data43);
										pw.write("|^");
										pw.write(data44);
										pw.write("|^");
										pw.write(data45);
										pw.write("|^");
										pw.write(data46);
										pw.write("|^");
										pw.write(data47);
										pw.write("|^");
										pw.write(data48);
										pw.write("|^");
										pw.write(data49);
										pw.write("|^");
										pw.write(data50);
										pw.write("|^");
										pw.write(data51);
										pw.write("|^");
										pw.write(data52);
										pw.write("|^");
										pw.write(data53);
										pw.write("|^");
										pw.write(data54);
										pw.write("|^");
										pw.write(data55);
										pw.write("|^");
										pw.write(data56);
										pw.write("|^");
										pw.write(data57);
										pw.write("|^");
										pw.write(data58);
										pw.write("|^");
										pw.write(data59);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								} else if (items.get("item") instanceof JSONArray) {

									JSONArray items_jsonArray = (JSONArray) items.get("item");

									for (int r = 0; r < items_jsonArray.size(); r++) {
										
										String sgcnm = " "; // 지자체명
										String sitenm = " "; // 정수장명
										String cltdt = " "; // 측정일자
										String data1 = " "; // 일반세균
										String data2 = " "; // 총대장균군
										String data3 = " "; // 대장균(E.coil)
										String data4 = " "; // 납(Pb)
										String data5 = " "; // 불소(F)
										String data6 = " "; // 비소
										String data7 = " "; // 셀레늄(Se)
										String data8 = " "; // 수은(Hg)
										String data9 = " "; // 시안(CN)
										String data10 = " "; // 크롬(Cr)
										String data11 = " "; // 암모니아성질소
										String data12 = " "; // 질산성질소
										String data13 = " "; // 보론(B)
										String data14 = " "; // 카드뮴(Cd)
										String data15 = " "; // 페놀
										String data16 = " "; // 총트리할로메탄(THMs)
										String data17 = " "; // 클로로포름
										String data18 = " "; // 다이아지논
										String data19 = " "; // 파라티온
										String data20 = " "; // 페니트로티온
										String data21 = " "; // 카바릴
										String data22 = " "; // 1,1,1-트리클로로에탄
										String data23 = " "; // 테트라클로로에틸렌(PCE)
										String data24 = " "; // 트리클로로에틸렌(TCE)
										String data25 = " "; // 디클로로메탄
										String data26 = " "; // 벤젠
										String data27 = " "; // 톨루엔
										String data28 = " "; // 에틸벤젠
										String data29 = " "; // 크실렌
										String data30 = " "; // 1,1디클로로에틸렌
										String data31 = " "; // 사염화탄소
										String data32 = " "; // 1,2-디브로모-3-클로로프로판
										String data33 = " "; // 잔류염소
										String data34 = " "; // 클로랄하이드레이트(CH)
										String data35 = " "; // 디브로모아세토니트릴
										String data36 = " "; // 디클로로아세토니트릴
										String data37 = " "; // 트리클로로아세토니트릴
										String data38 = " "; // 할로아세틱에시드(HAAs)
										String data39 = " "; // 경도
										String data40 = " "; // 과망간산칼륨소비량
										String data41 = " "; // 냄새
										String data42 = " "; // 맛
										String data43 = " "; // 구리(Cu)
										String data44 = " "; // 색도
										String data45 = " "; // 세제(음이온계면활성제:ABS)
										String data46 = " "; // 수소이온농도(pH)
										String data47 = " "; // 아연(Zn)
										String data48 = " "; // 염소이온
										String data49 = " "; // 증발잔류물(Totalsolids)
										String data50 = " "; // 철(Fe)
										String data51 = " "; // 망간(Mn)
										String data52 = " "; // 탁도(Turbidity)
										String data53 = " "; // 황산이온
										String data54 = " "; // 알루미늄(Al)
										String data55 = " "; // 브로모디클로로메탄
										String data56 = " "; // 디브로모클로로메탄
										String data57 = " "; // 1,4-다이옥산
										String data58 = " "; // 포름알데히드
										String data59 = " "; // 브롬산염

										JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

										Set<String> key = item_obj.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											sgcnm = JsonParser.colWrite_String(sgcnm, keyname, "sgcnm", item_obj);
											sitenm = JsonParser.colWrite_String(sitenm, keyname, "sitenm", item_obj);
											cltdt = JsonParser.colWrite_String(cltdt, keyname, "cltdt", item_obj);
											data1 = JsonParser.colWrite_String(data1, keyname, "data1", item_obj);
											data2 = JsonParser.colWrite_String(data2, keyname, "data2", item_obj);
											data3 = JsonParser.colWrite_String(data3, keyname, "data3", item_obj);
											data4 = JsonParser.colWrite_String(data4, keyname, "data4", item_obj);
											data5 = JsonParser.colWrite_String(data5, keyname, "data5", item_obj);
											data6 = JsonParser.colWrite_String(data6, keyname, "data6", item_obj);
											data7 = JsonParser.colWrite_String(data7, keyname, "data7", item_obj);
											data8 = JsonParser.colWrite_String(data8, keyname, "data8", item_obj);
											data9 = JsonParser.colWrite_String(data9, keyname, "data9", item_obj);
											data10 = JsonParser.colWrite_String(data10, keyname, "data10", item_obj);
											data11 = JsonParser.colWrite_String(data11, keyname, "data11", item_obj);
											data12 = JsonParser.colWrite_String(data12, keyname, "data12", item_obj);
											data13 = JsonParser.colWrite_String(data13, keyname, "data13", item_obj);
											data14 = JsonParser.colWrite_String(data14, keyname, "data14", item_obj);
											data15 = JsonParser.colWrite_String(data15, keyname, "data15", item_obj);
											data16 = JsonParser.colWrite_String(data16, keyname, "data16", item_obj);
											data17 = JsonParser.colWrite_String(data17, keyname, "data17", item_obj);
											data18 = JsonParser.colWrite_String(data18, keyname, "data18", item_obj);
											data19 = JsonParser.colWrite_String(data19, keyname, "data19", item_obj);
											data20 = JsonParser.colWrite_String(data20, keyname, "data20", item_obj);
											data21 = JsonParser.colWrite_String(data21, keyname, "data21", item_obj);
											data22 = JsonParser.colWrite_String(data22, keyname, "data22", item_obj);
											data23 = JsonParser.colWrite_String(data23, keyname, "data23", item_obj);
											data24 = JsonParser.colWrite_String(data24, keyname, "data24", item_obj);
											data25 = JsonParser.colWrite_String(data25, keyname, "data25", item_obj);
											data26 = JsonParser.colWrite_String(data26, keyname, "data26", item_obj);
											data27 = JsonParser.colWrite_String(data27, keyname, "data27", item_obj);
											data28 = JsonParser.colWrite_String(data28, keyname, "data28", item_obj);
											data29 = JsonParser.colWrite_String(data29, keyname, "data29", item_obj);
											data30 = JsonParser.colWrite_String(data30, keyname, "data30", item_obj);
											data31 = JsonParser.colWrite_String(data31, keyname, "data31", item_obj);
											data32 = JsonParser.colWrite_String(data32, keyname, "data32", item_obj);
											data33 = JsonParser.colWrite_String(data33, keyname, "data33", item_obj);
											data34 = JsonParser.colWrite_String(data34, keyname, "data34", item_obj);
											data35 = JsonParser.colWrite_String(data35, keyname, "data35", item_obj);
											data36 = JsonParser.colWrite_String(data36, keyname, "data36", item_obj);
											data37 = JsonParser.colWrite_String(data37, keyname, "data37", item_obj);
											data38 = JsonParser.colWrite_String(data38, keyname, "data38", item_obj);
											data39 = JsonParser.colWrite_String(data39, keyname, "data39", item_obj);
											data40 = JsonParser.colWrite_String(data40, keyname, "data40", item_obj);
											data41 = JsonParser.colWrite_String(data41, keyname, "data41", item_obj);
											data42 = JsonParser.colWrite_String(data42, keyname, "data42", item_obj);
											data43 = JsonParser.colWrite_String(data43, keyname, "data43", item_obj);
											data44 = JsonParser.colWrite_String(data44, keyname, "data44", item_obj);
											data45 = JsonParser.colWrite_String(data45, keyname, "data45", item_obj);
											data46 = JsonParser.colWrite_String(data46, keyname, "data46", item_obj);
											data47 = JsonParser.colWrite_String(data47, keyname, "data47", item_obj);
											data48 = JsonParser.colWrite_String(data48, keyname, "data48", item_obj);
											data49 = JsonParser.colWrite_String(data49, keyname, "data49", item_obj);
											data50 = JsonParser.colWrite_String(data50, keyname, "data50", item_obj);
											data51 = JsonParser.colWrite_String(data51, keyname, "data51", item_obj);
											data52 = JsonParser.colWrite_String(data52, keyname, "data52", item_obj);
											data53 = JsonParser.colWrite_String(data53, keyname, "data53", item_obj);
											data54 = JsonParser.colWrite_String(data54, keyname, "data54", item_obj);
											data55 = JsonParser.colWrite_String(data55, keyname, "data55", item_obj);
											data56 = JsonParser.colWrite_String(data56, keyname, "data56", item_obj);
											data57 = JsonParser.colWrite_String(data57, keyname, "data57", item_obj);
											data58 = JsonParser.colWrite_String(data58, keyname, "data58", item_obj);
											data59 = JsonParser.colWrite_String(data59, keyname, "data59", item_obj);

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(args[0]);
											pw.write("|^");
											pw.write(args[1]);
											pw.write("|^");
											pw.write(sgcnm);
											pw.write("|^");
											pw.write(sitenm);
											pw.write("|^");
											pw.write(cltdt);
											pw.write("|^");
											pw.write(data1);
											pw.write("|^");
											pw.write(data2);
											pw.write("|^");
											pw.write(data3);
											pw.write("|^");
											pw.write(data4);
											pw.write("|^");
											pw.write(data5);
											pw.write("|^");
											pw.write(data6);
											pw.write("|^");
											pw.write(data7);
											pw.write("|^");
											pw.write(data8);
											pw.write("|^");
											pw.write(data9);
											pw.write("|^");
											pw.write(data10);
											pw.write("|^");
											pw.write(data11);
											pw.write("|^");
											pw.write(data12);
											pw.write("|^");
											pw.write(data13);
											pw.write("|^");
											pw.write(data14);
											pw.write("|^");
											pw.write(data15);
											pw.write("|^");
											pw.write(data16);
											pw.write("|^");
											pw.write(data17);
											pw.write("|^");
											pw.write(data18);
											pw.write("|^");
											pw.write(data19);
											pw.write("|^");
											pw.write(data20);
											pw.write("|^");
											pw.write(data21);
											pw.write("|^");
											pw.write(data22);
											pw.write("|^");
											pw.write(data23);
											pw.write("|^");
											pw.write(data24);
											pw.write("|^");
											pw.write(data25);
											pw.write("|^");
											pw.write(data26);
											pw.write("|^");
											pw.write(data27);
											pw.write("|^");
											pw.write(data28);
											pw.write("|^");
											pw.write(data29);
											pw.write("|^");
											pw.write(data30);
											pw.write("|^");
											pw.write(data31);
											pw.write("|^");
											pw.write(data32);
											pw.write("|^");
											pw.write(data33);
											pw.write("|^");
											pw.write(data34);
											pw.write("|^");
											pw.write(data35);
											pw.write("|^");
											pw.write(data36);
											pw.write("|^");
											pw.write(data37);
											pw.write("|^");
											pw.write(data38);
											pw.write("|^");
											pw.write(data39);
											pw.write("|^");
											pw.write(data40);
											pw.write("|^");
											pw.write(data41);
											pw.write("|^");
											pw.write(data42);
											pw.write("|^");
											pw.write(data43);
											pw.write("|^");
											pw.write(data44);
											pw.write("|^");
											pw.write(data45);
											pw.write("|^");
											pw.write(data46);
											pw.write("|^");
											pw.write(data47);
											pw.write("|^");
											pw.write(data48);
											pw.write("|^");
											pw.write(data49);
											pw.write("|^");
											pw.write(data50);
											pw.write("|^");
											pw.write(data51);
											pw.write("|^");
											pw.write(data52);
											pw.write("|^");
											pw.write(data53);
											pw.write("|^");
											pw.write(data54);
											pw.write("|^");
											pw.write(data55);
											pw.write("|^");
											pw.write(data56);
											pw.write("|^");
											pw.write(data57);
											pw.write("|^");
											pw.write(data58);
											pw.write("|^");
											pw.write(data59);
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

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRS/TIF_WRS_14.dat", "WRS");

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
				System.out.println("stdt :" + args[0] + ": eddt :" + args[1]);
			}



	}

}
