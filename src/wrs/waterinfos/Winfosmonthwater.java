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


public class Winfosmonthwater {

	

	// 지방정수장 수질정보 조회 서비스 - 지방상수도수질(월간)
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 요청 파라미터는 조회 시작일과 조회 종료일의 2개
		if (args.length == 2) {

			if (args[0].length() == 6 && args[1].length() == 6) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); // 시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("waterinfos_Winfosmonthwater_url");
				String service_key = JsonParser.getProperty("waterinfos_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(
						JsonParser.getProperty("file_path") + "WRS/TIF_WRS_14.dat");

				if(file.exists()){
					
					System.out.println("파일이 이미 존재하므로 이어쓰기..");
					
				} else {
				
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
						pw.write("sgcnm"); // 지자체명
						pw.write("|^");
						pw.write("sitenm"); // 정수장명
						pw.write("|^");
						pw.write("cltdt"); // 측정일자
						pw.write("|^");
						pw.write("data1"); // 일반세균
						pw.write("|^");
						pw.write("data2"); // 총대장균군
						pw.write("|^");
						pw.write("data3"); // 대장균(E.coil)
						pw.write("|^");
						pw.write("data4"); // 납(Pb)
						pw.write("|^");
						pw.write("data5"); // 불소(F)
						pw.write("|^");
						pw.write("data6"); // 비소
						pw.write("|^");
						pw.write("data7"); // 셀레늄(Se)
						pw.write("|^");
						pw.write("data8"); // 수은(Hg)
						pw.write("|^");
						pw.write("data9"); // 시안(CN)
						pw.write("|^");
						pw.write("data10"); // 크롬(Cr)
						pw.write("|^");
						pw.write("data11"); // 암모니아성질소
						pw.write("|^");
						pw.write("data12"); // 질산성질소
						pw.write("|^");
						pw.write("data13"); // 보론(B)
						pw.write("|^");
						pw.write("data14"); // 카드뮴(Cd)
						pw.write("|^");
						pw.write("data15"); // 페놀
						pw.write("|^");
						pw.write("data16"); // 총트리할로메탄(THMs)
						pw.write("|^");
						pw.write("data17"); // 클로로포름
						pw.write("|^");
						pw.write("data18"); // 다이아지논
						pw.write("|^");
						pw.write("data19"); // 파라티온
						pw.write("|^");
						pw.write("data20"); // 페니트로티온
						pw.write("|^");
						pw.write("data21"); // 카바릴
						pw.write("|^");
						pw.write("data22"); // 1,1,1-트리클로로에탄
						pw.write("|^");
						pw.write("data23"); // 테트라클로로에틸렌(PCE)
						pw.write("|^");
						pw.write("data24"); // 트리클로로에틸렌(TCE)
						pw.write("|^");
						pw.write("data25"); // 디클로로메탄
						pw.write("|^");
						pw.write("data26"); // 벤젠
						pw.write("|^");
						pw.write("data27"); // 톨루엔
						pw.write("|^");
						pw.write("data28"); // 에틸벤젠
						pw.write("|^");
						pw.write("data29"); // 크실렌
						pw.write("|^");
						pw.write("data30"); // 1,1디클로로에틸렌
						pw.write("|^");
						pw.write("data31"); // 사염화탄소
						pw.write("|^");
						pw.write("data32"); // 1,2-디브로모-3-클로로프로판
						pw.write("|^");
						pw.write("data33"); // 잔류염소
						pw.write("|^");
						pw.write("data34"); // 클로랄하이드레이트(CH)
						pw.write("|^");
						pw.write("data35"); // 디브로모아세토니트릴
						pw.write("|^");
						pw.write("data36"); // 디클로로아세토니트릴
						pw.write("|^");
						pw.write("data37"); // 트리클로로아세토니트릴
						pw.write("|^");
						pw.write("data38"); // 할로아세틱에시드(HAAs)
						pw.write("|^");
						pw.write("data39"); // 경도
						pw.write("|^");
						pw.write("data40"); // 과망간산칼륨소비량
						pw.write("|^");
						pw.write("data41"); // 냄새
						pw.write("|^");
						pw.write("data42"); // 맛
						pw.write("|^");
						pw.write("data43"); // 구리(Cu)
						pw.write("|^");
						pw.write("data44"); // 색도
						pw.write("|^");
						pw.write("data45"); // 세제(음이온계면활성제:ABS)
						pw.write("|^");
						pw.write("data46"); // 수소이온농도(pH)
						pw.write("|^");
						pw.write("data47"); // 아연(Zn)
						pw.write("|^");
						pw.write("data48"); // 염소이온
						pw.write("|^");
						pw.write("data49"); // 증발잔류물(Totalsolids)
						pw.write("|^");
						pw.write("data50"); // 철(Fe)
						pw.write("|^");
						pw.write("data51"); // 망간(Mn)
						pw.write("|^");
						pw.write("data52"); // 탁도(Turbidity)
						pw.write("|^");
						pw.write("data53"); // 황산이온
						pw.write("|^");
						pw.write("data54"); // 알루미늄(Al)
						pw.write("|^");
						pw.write("data55"); // 브로모디클로로메탄
						pw.write("|^");
						pw.write("data56"); // 디브로모클로로메탄
						pw.write("|^");
						pw.write("data57"); // 1,4-다이옥산
						pw.write("|^");
						pw.write("data58"); // 포름알데히드
						pw.write("|^");
						pw.write("data59"); // 브롬산염
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

				// yyyyMM이므로 기존 메서드 이용 불가능.. 하나 새로 만들어서 처리
				json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");

					int numOfRows = ((Long) body.get("numOfRows")).intValue();
					int totalCount = ((Long) body.get("totalCount")).intValue();

					pageCount = (totalCount / numOfRows) + 1;

				} catch (Exception e) {
					e.printStackTrace();
				}

				// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

				StringBuffer resultSb = new StringBuffer("");

				StringBuffer sgcnm = new StringBuffer(" "); // 지자체명
				StringBuffer sitenm = new StringBuffer(" "); // 정수장명
				StringBuffer cltdt = new StringBuffer(" "); // 측정일자
				StringBuffer data1 = new StringBuffer(" "); // 일반세균
				StringBuffer data2 = new StringBuffer(" "); // 총대장균군
				StringBuffer data3 = new StringBuffer(" "); // 대장균(E.coil)
				StringBuffer data4 = new StringBuffer(" "); // 납(Pb)
				StringBuffer data5 = new StringBuffer(" "); // 불소(F)
				StringBuffer data6 = new StringBuffer(" "); // 비소
				StringBuffer data7 = new StringBuffer(" "); // 셀레늄(Se)
				StringBuffer data8 = new StringBuffer(" "); // 수은(Hg)
				StringBuffer data9 = new StringBuffer(" "); // 시안(CN)
				StringBuffer data10 = new StringBuffer(" "); // 크롬(Cr)
				StringBuffer data11 = new StringBuffer(" "); // 암모니아성질소
				StringBuffer data12 = new StringBuffer(" "); // 질산성질소
				StringBuffer data13 = new StringBuffer(" "); // 보론(B)
				StringBuffer data14 = new StringBuffer(" "); // 카드뮴(Cd)
				StringBuffer data15 = new StringBuffer(" "); // 페놀
				StringBuffer data16 = new StringBuffer(" "); // 총트리할로메탄(THMs)
				StringBuffer data17 = new StringBuffer(" "); // 클로로포름
				StringBuffer data18 = new StringBuffer(" "); // 다이아지논
				StringBuffer data19 = new StringBuffer(" "); // 파라티온
				StringBuffer data20 = new StringBuffer(" "); // 페니트로티온
				StringBuffer data21 = new StringBuffer(" "); // 카바릴
				StringBuffer data22 = new StringBuffer(" "); // 1,1,1-트리클로로에탄
				StringBuffer data23 = new StringBuffer(" "); // 테트라클로로에틸렌(PCE)
				StringBuffer data24 = new StringBuffer(" "); // 트리클로로에틸렌(TCE)
				StringBuffer data25 = new StringBuffer(" "); // 디클로로메탄
				StringBuffer data26 = new StringBuffer(" "); // 벤젠
				StringBuffer data27 = new StringBuffer(" "); // 톨루엔
				StringBuffer data28 = new StringBuffer(" "); // 에틸벤젠
				StringBuffer data29 = new StringBuffer(" "); // 크실렌
				StringBuffer data30 = new StringBuffer(" "); // 1,1디클로로에틸렌
				StringBuffer data31 = new StringBuffer(" "); // 사염화탄소
				StringBuffer data32 = new StringBuffer(" "); // 1,2-디브로모-3-클로로프로판
				StringBuffer data33 = new StringBuffer(" "); // 잔류염소
				StringBuffer data34 = new StringBuffer(" "); // 클로랄하이드레이트(CH)
				StringBuffer data35 = new StringBuffer(" "); // 디브로모아세토니트릴
				StringBuffer data36 = new StringBuffer(" "); // 디클로로아세토니트릴
				StringBuffer data37 = new StringBuffer(" "); // 트리클로로아세토니트릴
				StringBuffer data38 = new StringBuffer(" "); // 할로아세틱에시드(HAAs)
				StringBuffer data39 = new StringBuffer(" "); // 경도
				StringBuffer data40 = new StringBuffer(" "); // 과망간산칼륨소비량
				StringBuffer data41 = new StringBuffer(" "); // 냄새
				StringBuffer data42 = new StringBuffer(" "); // 맛
				StringBuffer data43 = new StringBuffer(" "); // 구리(Cu)
				StringBuffer data44 = new StringBuffer(" "); // 색도
				StringBuffer data45 = new StringBuffer(" "); // 세제(음이온계면활성제:ABS)
				StringBuffer data46 = new StringBuffer(" "); // 수소이온농도(pH)
				StringBuffer data47 = new StringBuffer(" "); // 아연(Zn)
				StringBuffer data48 = new StringBuffer(" "); // 염소이온
				StringBuffer data49 = new StringBuffer(" "); // 증발잔류물(Totalsolids)
				StringBuffer data50 = new StringBuffer(" "); // 철(Fe)
				StringBuffer data51 = new StringBuffer(" "); // 망간(Mn)
				StringBuffer data52 = new StringBuffer(" "); // 탁도(Turbidity)
				StringBuffer data53 = new StringBuffer(" "); // 황산이온
				StringBuffer data54 = new StringBuffer(" "); // 알루미늄(Al)
				StringBuffer data55 = new StringBuffer(" "); // 브로모디클로로메탄
				StringBuffer data56 = new StringBuffer(" "); // 디브로모클로로메탄
				StringBuffer data57 = new StringBuffer(" "); // 1,4-다이옥산
				StringBuffer data58 = new StringBuffer(" "); // 포름알데히드
				StringBuffer data59 = new StringBuffer(" "); // 브롬산염

				for (int i = 1; i <= pageCount; i++) {

					json = JsonParser.parseWrsJson(service_url, service_key, String.valueOf(i), args[0], args[1]);

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();

						if (resultCode.equals("00")) {

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (items.get("item") instanceof JSONObject) {

								JSONObject items_jsonObject = (JSONObject) items.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(sgcnm, keyname, "sgcnm", items_jsonObject);
									JsonParser.colWrite(sitenm, keyname, "sitenm", items_jsonObject);
									JsonParser.colWrite(cltdt, keyname, "cltdt", items_jsonObject);
									JsonParser.colWrite(data1, keyname, "data1", items_jsonObject);
									JsonParser.colWrite(data2, keyname, "data2", items_jsonObject);
									JsonParser.colWrite(data3, keyname, "data3", items_jsonObject);
									JsonParser.colWrite(data4, keyname, "data4", items_jsonObject);
									JsonParser.colWrite(data5, keyname, "data5", items_jsonObject);
									JsonParser.colWrite(data6, keyname, "data6", items_jsonObject);
									JsonParser.colWrite(data7, keyname, "data7", items_jsonObject);
									JsonParser.colWrite(data8, keyname, "data8", items_jsonObject);
									JsonParser.colWrite(data9, keyname, "data9", items_jsonObject);
									JsonParser.colWrite(data10, keyname, "data10", items_jsonObject);
									JsonParser.colWrite(data11, keyname, "data11", items_jsonObject);
									JsonParser.colWrite(data12, keyname, "data12", items_jsonObject);
									JsonParser.colWrite(data13, keyname, "data13", items_jsonObject);
									JsonParser.colWrite(data14, keyname, "data14", items_jsonObject);
									JsonParser.colWrite(data15, keyname, "data15", items_jsonObject);
									JsonParser.colWrite(data16, keyname, "data16", items_jsonObject);
									JsonParser.colWrite(data17, keyname, "data17", items_jsonObject);
									JsonParser.colWrite(data18, keyname, "data18", items_jsonObject);
									JsonParser.colWrite(data19, keyname, "data19", items_jsonObject);
									JsonParser.colWrite(data20, keyname, "data20", items_jsonObject);
									JsonParser.colWrite(data21, keyname, "data21", items_jsonObject);
									JsonParser.colWrite(data22, keyname, "data22", items_jsonObject);
									JsonParser.colWrite(data23, keyname, "data23", items_jsonObject);
									JsonParser.colWrite(data24, keyname, "data24", items_jsonObject);
									JsonParser.colWrite(data25, keyname, "data25", items_jsonObject);
									JsonParser.colWrite(data26, keyname, "data26", items_jsonObject);
									JsonParser.colWrite(data27, keyname, "data27", items_jsonObject);
									JsonParser.colWrite(data28, keyname, "data28", items_jsonObject);
									JsonParser.colWrite(data29, keyname, "data29", items_jsonObject);
									JsonParser.colWrite(data30, keyname, "data30", items_jsonObject);
									JsonParser.colWrite(data31, keyname, "data31", items_jsonObject);
									JsonParser.colWrite(data32, keyname, "data32", items_jsonObject);
									JsonParser.colWrite(data33, keyname, "data33", items_jsonObject);
									JsonParser.colWrite(data34, keyname, "data34", items_jsonObject);
									JsonParser.colWrite(data35, keyname, "data35", items_jsonObject);
									JsonParser.colWrite(data36, keyname, "data36", items_jsonObject);
									JsonParser.colWrite(data37, keyname, "data37", items_jsonObject);
									JsonParser.colWrite(data38, keyname, "data38", items_jsonObject);
									JsonParser.colWrite(data39, keyname, "data39", items_jsonObject);
									JsonParser.colWrite(data40, keyname, "data40", items_jsonObject);
									JsonParser.colWrite(data41, keyname, "data41", items_jsonObject);
									JsonParser.colWrite(data42, keyname, "data42", items_jsonObject);
									JsonParser.colWrite(data43, keyname, "data43", items_jsonObject);
									JsonParser.colWrite(data44, keyname, "data44", items_jsonObject);
									JsonParser.colWrite(data45, keyname, "data45", items_jsonObject);
									JsonParser.colWrite(data46, keyname, "data46", items_jsonObject);
									JsonParser.colWrite(data47, keyname, "data47", items_jsonObject);
									JsonParser.colWrite(data48, keyname, "data48", items_jsonObject);
									JsonParser.colWrite(data49, keyname, "data49", items_jsonObject);
									JsonParser.colWrite(data50, keyname, "data50", items_jsonObject);
									JsonParser.colWrite(data51, keyname, "data51", items_jsonObject);
									JsonParser.colWrite(data52, keyname, "data52", items_jsonObject);
									JsonParser.colWrite(data53, keyname, "data53", items_jsonObject);
									JsonParser.colWrite(data54, keyname, "data54", items_jsonObject);
									JsonParser.colWrite(data55, keyname, "data55", items_jsonObject);
									JsonParser.colWrite(data56, keyname, "data56", items_jsonObject);
									JsonParser.colWrite(data57, keyname, "data57", items_jsonObject);
									JsonParser.colWrite(data58, keyname, "data58", items_jsonObject);
									JsonParser.colWrite(data59, keyname, "data59", items_jsonObject);

								}

								// 한번에 문자열 합침
								resultSb.append(sgcnm);
								resultSb.append("|^");
								resultSb.append(sitenm);
								resultSb.append("|^");
								resultSb.append(cltdt);
								resultSb.append("|^");
								resultSb.append(data1);
								resultSb.append("|^");
								resultSb.append(data2);
								resultSb.append("|^");
								resultSb.append(data3);
								resultSb.append("|^");
								resultSb.append(data4);
								resultSb.append("|^");
								resultSb.append(data5);
								resultSb.append("|^");
								resultSb.append(data6);
								resultSb.append("|^");
								resultSb.append(data7);
								resultSb.append("|^");
								resultSb.append(data8);
								resultSb.append("|^");
								resultSb.append(data9);
								resultSb.append("|^");
								resultSb.append(data10);
								resultSb.append("|^");
								resultSb.append(data11);
								resultSb.append("|^");
								resultSb.append(data12);
								resultSb.append("|^");
								resultSb.append(data13);
								resultSb.append("|^");
								resultSb.append(data14);
								resultSb.append("|^");
								resultSb.append(data15);
								resultSb.append("|^");
								resultSb.append(data16);
								resultSb.append("|^");
								resultSb.append(data17);
								resultSb.append("|^");
								resultSb.append(data18);
								resultSb.append("|^");
								resultSb.append(data19);
								resultSb.append("|^");
								resultSb.append(data20);
								resultSb.append("|^");
								resultSb.append(data21);
								resultSb.append("|^");
								resultSb.append(data22);
								resultSb.append("|^");
								resultSb.append(data23);
								resultSb.append("|^");
								resultSb.append(data24);
								resultSb.append("|^");
								resultSb.append(data25);
								resultSb.append("|^");
								resultSb.append(data26);
								resultSb.append("|^");
								resultSb.append(data27);
								resultSb.append("|^");
								resultSb.append(data28);
								resultSb.append("|^");
								resultSb.append(data29);
								resultSb.append("|^");
								resultSb.append(data30);
								resultSb.append("|^");
								resultSb.append(data31);
								resultSb.append("|^");
								resultSb.append(data32);
								resultSb.append("|^");
								resultSb.append(data33);
								resultSb.append("|^");
								resultSb.append(data34);
								resultSb.append("|^");
								resultSb.append(data35);
								resultSb.append("|^");
								resultSb.append(data36);
								resultSb.append("|^");
								resultSb.append(data37);
								resultSb.append("|^");
								resultSb.append(data38);
								resultSb.append("|^");
								resultSb.append(data39);
								resultSb.append("|^");
								resultSb.append(data40);
								resultSb.append("|^");
								resultSb.append(data41);
								resultSb.append("|^");
								resultSb.append(data42);
								resultSb.append("|^");
								resultSb.append(data43);
								resultSb.append("|^");
								resultSb.append(data44);
								resultSb.append("|^");
								resultSb.append(data45);
								resultSb.append("|^");
								resultSb.append(data46);
								resultSb.append("|^");
								resultSb.append(data47);
								resultSb.append("|^");
								resultSb.append(data48);
								resultSb.append("|^");
								resultSb.append(data49);
								resultSb.append("|^");
								resultSb.append(data50);
								resultSb.append("|^");
								resultSb.append(data51);
								resultSb.append("|^");
								resultSb.append(data52);
								resultSb.append("|^");
								resultSb.append(data53);
								resultSb.append("|^");
								resultSb.append(data54);
								resultSb.append("|^");
								resultSb.append(data55);
								resultSb.append("|^");
								resultSb.append(data56);
								resultSb.append("|^");
								resultSb.append(data57);
								resultSb.append("|^");
								resultSb.append(data58);
								resultSb.append("|^");
								resultSb.append(data59);
								resultSb.append(System.getProperty("line.separator"));

							} else if (items.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) items.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();

										JsonParser.colWrite(sgcnm, keyname, "sgcnm", item_obj);
										JsonParser.colWrite(sitenm, keyname, "sitenm", item_obj);
										JsonParser.colWrite(cltdt, keyname, "cltdt", item_obj);
										JsonParser.colWrite(data1, keyname, "data1", item_obj);
										JsonParser.colWrite(data2, keyname, "data2", item_obj);
										JsonParser.colWrite(data3, keyname, "data3", item_obj);
										JsonParser.colWrite(data4, keyname, "data4", item_obj);
										JsonParser.colWrite(data5, keyname, "data5", item_obj);
										JsonParser.colWrite(data6, keyname, "data6", item_obj);
										JsonParser.colWrite(data7, keyname, "data7", item_obj);
										JsonParser.colWrite(data8, keyname, "data8", item_obj);
										JsonParser.colWrite(data9, keyname, "data9", item_obj);
										JsonParser.colWrite(data10, keyname, "data10", item_obj);
										JsonParser.colWrite(data11, keyname, "data11", item_obj);
										JsonParser.colWrite(data12, keyname, "data12", item_obj);
										JsonParser.colWrite(data13, keyname, "data13", item_obj);
										JsonParser.colWrite(data14, keyname, "data14", item_obj);
										JsonParser.colWrite(data15, keyname, "data15", item_obj);
										JsonParser.colWrite(data16, keyname, "data16", item_obj);
										JsonParser.colWrite(data17, keyname, "data17", item_obj);
										JsonParser.colWrite(data18, keyname, "data18", item_obj);
										JsonParser.colWrite(data19, keyname, "data19", item_obj);
										JsonParser.colWrite(data20, keyname, "data20", item_obj);
										JsonParser.colWrite(data21, keyname, "data21", item_obj);
										JsonParser.colWrite(data22, keyname, "data22", item_obj);
										JsonParser.colWrite(data23, keyname, "data23", item_obj);
										JsonParser.colWrite(data24, keyname, "data24", item_obj);
										JsonParser.colWrite(data25, keyname, "data25", item_obj);
										JsonParser.colWrite(data26, keyname, "data26", item_obj);
										JsonParser.colWrite(data27, keyname, "data27", item_obj);
										JsonParser.colWrite(data28, keyname, "data28", item_obj);
										JsonParser.colWrite(data29, keyname, "data29", item_obj);
										JsonParser.colWrite(data30, keyname, "data30", item_obj);
										JsonParser.colWrite(data31, keyname, "data31", item_obj);
										JsonParser.colWrite(data32, keyname, "data32", item_obj);
										JsonParser.colWrite(data33, keyname, "data33", item_obj);
										JsonParser.colWrite(data34, keyname, "data34", item_obj);
										JsonParser.colWrite(data35, keyname, "data35", item_obj);
										JsonParser.colWrite(data36, keyname, "data36", item_obj);
										JsonParser.colWrite(data37, keyname, "data37", item_obj);
										JsonParser.colWrite(data38, keyname, "data38", item_obj);
										JsonParser.colWrite(data39, keyname, "data39", item_obj);
										JsonParser.colWrite(data40, keyname, "data40", item_obj);
										JsonParser.colWrite(data41, keyname, "data41", item_obj);
										JsonParser.colWrite(data42, keyname, "data42", item_obj);
										JsonParser.colWrite(data43, keyname, "data43", item_obj);
										JsonParser.colWrite(data44, keyname, "data44", item_obj);
										JsonParser.colWrite(data45, keyname, "data45", item_obj);
										JsonParser.colWrite(data46, keyname, "data46", item_obj);
										JsonParser.colWrite(data47, keyname, "data47", item_obj);
										JsonParser.colWrite(data48, keyname, "data48", item_obj);
										JsonParser.colWrite(data49, keyname, "data49", item_obj);
										JsonParser.colWrite(data50, keyname, "data50", item_obj);
										JsonParser.colWrite(data51, keyname, "data51", item_obj);
										JsonParser.colWrite(data52, keyname, "data52", item_obj);
										JsonParser.colWrite(data53, keyname, "data53", item_obj);
										JsonParser.colWrite(data54, keyname, "data54", item_obj);
										JsonParser.colWrite(data55, keyname, "data55", item_obj);
										JsonParser.colWrite(data56, keyname, "data56", item_obj);
										JsonParser.colWrite(data57, keyname, "data57", item_obj);
										JsonParser.colWrite(data58, keyname, "data58", item_obj);
										JsonParser.colWrite(data59, keyname, "data59", item_obj);

									}

									// 한번에 문자열 합침
									resultSb.append(sgcnm);
									resultSb.append("|^");
									resultSb.append(sitenm);
									resultSb.append("|^");
									resultSb.append(cltdt);
									resultSb.append("|^");
									resultSb.append(data1);
									resultSb.append("|^");
									resultSb.append(data2);
									resultSb.append("|^");
									resultSb.append(data3);
									resultSb.append("|^");
									resultSb.append(data4);
									resultSb.append("|^");
									resultSb.append(data5);
									resultSb.append("|^");
									resultSb.append(data6);
									resultSb.append("|^");
									resultSb.append(data7);
									resultSb.append("|^");
									resultSb.append(data8);
									resultSb.append("|^");
									resultSb.append(data9);
									resultSb.append("|^");
									resultSb.append(data10);
									resultSb.append("|^");
									resultSb.append(data11);
									resultSb.append("|^");
									resultSb.append(data12);
									resultSb.append("|^");
									resultSb.append(data13);
									resultSb.append("|^");
									resultSb.append(data14);
									resultSb.append("|^");
									resultSb.append(data15);
									resultSb.append("|^");
									resultSb.append(data16);
									resultSb.append("|^");
									resultSb.append(data17);
									resultSb.append("|^");
									resultSb.append(data18);
									resultSb.append("|^");
									resultSb.append(data19);
									resultSb.append("|^");
									resultSb.append(data20);
									resultSb.append("|^");
									resultSb.append(data21);
									resultSb.append("|^");
									resultSb.append(data22);
									resultSb.append("|^");
									resultSb.append(data23);
									resultSb.append("|^");
									resultSb.append(data24);
									resultSb.append("|^");
									resultSb.append(data25);
									resultSb.append("|^");
									resultSb.append(data26);
									resultSb.append("|^");
									resultSb.append(data27);
									resultSb.append("|^");
									resultSb.append(data28);
									resultSb.append("|^");
									resultSb.append(data29);
									resultSb.append("|^");
									resultSb.append(data30);
									resultSb.append("|^");
									resultSb.append(data31);
									resultSb.append("|^");
									resultSb.append(data32);
									resultSb.append("|^");
									resultSb.append(data33);
									resultSb.append("|^");
									resultSb.append(data34);
									resultSb.append("|^");
									resultSb.append(data35);
									resultSb.append("|^");
									resultSb.append(data36);
									resultSb.append("|^");
									resultSb.append(data37);
									resultSb.append("|^");
									resultSb.append(data38);
									resultSb.append("|^");
									resultSb.append(data39);
									resultSb.append("|^");
									resultSb.append(data40);
									resultSb.append("|^");
									resultSb.append(data41);
									resultSb.append("|^");
									resultSb.append(data42);
									resultSb.append("|^");
									resultSb.append(data43);
									resultSb.append("|^");
									resultSb.append(data44);
									resultSb.append("|^");
									resultSb.append(data45);
									resultSb.append("|^");
									resultSb.append(data46);
									resultSb.append("|^");
									resultSb.append(data47);
									resultSb.append("|^");
									resultSb.append(data48);
									resultSb.append("|^");
									resultSb.append(data49);
									resultSb.append("|^");
									resultSb.append(data50);
									resultSb.append("|^");
									resultSb.append(data51);
									resultSb.append("|^");
									resultSb.append(data52);
									resultSb.append("|^");
									resultSb.append(data53);
									resultSb.append("|^");
									resultSb.append(data54);
									resultSb.append("|^");
									resultSb.append(data55);
									resultSb.append("|^");
									resultSb.append(data56);
									resultSb.append("|^");
									resultSb.append(data57);
									resultSb.append("|^");
									resultSb.append(data58);
									resultSb.append("|^");
									resultSb.append(data59);
									resultSb.append(System.getProperty("line.separator"));

								}

							} else {
								System.out.println("parsing error!!");
							}

						} else if (resultCode.equals("03")) {
							System.out.println("data not exist!!");
						} else {
							System.out.println("parsing error!!");
						}

					} catch (Exception e) {
						e.printStackTrace();
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

	}

}
