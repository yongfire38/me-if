package pri.waterquality;

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

public class GetRealTimeWaterQualityList_all {

	// 수질정보 DB 서비스 - 수질자동측정망 운영결과 DB
	// 전 데이터 파싱.. Connection reset 에러 발생..
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 3) {

			try {

				Thread.sleep(1000);

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("PRI_WaterQualityService_getRealTimeWaterQualityList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_03.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("resultCode"); // 결과코드
							pw.write("|^");
							pw.write("resultMsg"); // 결과메시지
							pw.write("|^");
							pw.write("rowno"); // 행번호
							pw.write("|^");
							pw.write("siteId"); // 조사지점번호
							pw.write("|^");
							pw.write("siteName"); // 조사지점명
							pw.write("|^");
							pw.write("msrDate"); // 조사시간
							pw.write("|^");
							pw.write("m01"); // 통신상태
							pw.write("|^");
							pw.write("m02"); // 수온1
							pw.write("|^");
							pw.write("m03"); // 수소이온농도
							pw.write("|^");
							pw.write("m04"); // 전기전도도1
							pw.write("|^");
							pw.write("m05"); // 용존산소1
							pw.write("|^");
							pw.write("m06"); // 총유기탄소1
							pw.write("|^");
							pw.write("m07"); // 임펄스
							pw.write("|^");
							pw.write("m08"); // 수조pH
							pw.write("|^");
							pw.write("m09"); // 수온
							pw.write("|^");
							pw.write("m10"); // 수조 산소량
							pw.write("|^");
							pw.write("m11"); // 활동여부
							pw.write("|^");
							pw.write("m12"); // 염화메틸렌
							pw.write("|^");
							pw.write("m13"); // 1.1.1-트리클로로에테인
							pw.write("|^");
							pw.write("m14"); // 벤젠
							pw.write("|^");
							pw.write("m15"); // 사염화탄소
							pw.write("|^");
							pw.write("m16"); // 트리클로로에틸렌
							pw.write("|^");
							pw.write("m17"); // 톨루엔
							pw.write("|^");
							pw.write("m18"); // 테트라클로로에틸렌
							pw.write("|^");
							pw.write("m19"); // 에틸벤젠
							pw.write("|^");
							pw.write("m20"); // m,p-자일렌
							pw.write("|^");
							pw.write("m21"); // o-자일렌
							pw.write("|^");
							pw.write("m22"); // [ECD]염화메틸렌
							pw.write("|^");
							pw.write("m23"); // [ECD]1.1.1-트리클로로에테인
							pw.write("|^");
							pw.write("m24"); // [ECD]사염화탄소
							pw.write("|^");
							pw.write("m25"); // [ECD]트리클로로에틸렌
							pw.write("|^");
							pw.write("m26"); // [ECD}테트라클로로에틸렌
							pw.write("|^");
							pw.write("m27"); // 총질소
							pw.write("|^");
							pw.write("m28"); // 총인
							pw.write("|^");
							pw.write("m29"); // 클로로필-a
							pw.write("|^");
							pw.write("m30"); // 투과도
							pw.write("|^");
							pw.write("m31"); // 임펄스(우)
							pw.write("|^");
							pw.write("m32"); // 임펄스(좌)
							pw.write("|^");
							pw.write("m33"); // 수조수온(우)
							pw.write("|^");
							pw.write("m34"); // 수조수온(좌)
							pw.write("|^");
							pw.write("m35"); // 인산염인
							pw.write("|^");
							pw.write("m36"); // 암모니아성질소
							pw.write("|^");
							pw.write("m37"); // 질산성질소
							pw.write("|^");
							pw.write("m38"); // 수온2
							pw.write("|^");
							pw.write("m39"); // 수소이온농도2
							pw.write("|^");
							pw.write("m40"); // 전기전도도2
							pw.write("|^");
							pw.write("m41"); // 용존산소2
							pw.write("|^");
							pw.write("m42"); // 실내온도
							pw.write("|^");
							pw.write("m43"); // UPS전압
							pw.write("|^");
							pw.write("m44"); // 출입문 상태
							pw.write("|^");
							pw.write("m45"); // 유속
							pw.write("|^");
							pw.write("m46"); // 유압
							pw.write("|^");
							pw.write("m47"); // 채수펌프(우)
							pw.write("|^");
							pw.write("m48"); // 채수펌프(좌)
							pw.write("|^");
							pw.write("m49"); // 여과장치
							pw.write("|^");
							pw.write("m50"); // 항온항습기
							pw.write("|^");
							pw.write("m51"); // 자탐기
							pw.write("|^");
							pw.write("m52"); // 실내습도
							pw.write("|^");
							pw.write("m53"); // 전원상태
							pw.write("|^");
							pw.write("m54"); // 일반채수기
							pw.write("|^");
							pw.write("m55"); // VOCs 채수기
							pw.write("|^");
							pw.write("m56"); // 자일렌
							pw.write("|^");
							pw.write("m57"); // 독성지수(좌)
							pw.write("|^");
							pw.write("m58"); // 유영속도(좌)
							pw.write("|^");
							pw.write("m59"); // 개체수(좌)
							pw.write("|^");
							pw.write("m60"); // 유영속도 분포지수(좌)
							pw.write("|^");
							pw.write("m61"); // 프렉탈 차수(좌)
							pw.write("|^");
							pw.write("m62"); // 시료온도(좌)
							pw.write("|^");
							pw.write("m63"); // 독성지수(우)
							pw.write("|^");
							pw.write("m64"); // 유영속도(우)
							pw.write("|^");
							pw.write("m65"); // 개체수(우)
							pw.write("|^");
							pw.write("m66"); // 유영속도 분포지수(우)
							pw.write("|^");
							pw.write("m67"); // 프렉탈 차수(우)
							pw.write("|^");
							pw.write("m68"); // 시료온도(우)
							pw.write("|^");
							pw.write("m69"); // 수온3
							pw.write("|^");
							pw.write("m70"); // 수소이온농도3
							pw.write("|^");
							pw.write("m71"); // 전기전도도3
							pw.write("|^");
							pw.write("m72"); // 용존산소3
							pw.write("|^");
							pw.write("m73"); // 탁도3
							pw.write("|^");
							pw.write("m74"); // 카드뮴
							pw.write("|^");
							pw.write("m75"); // 납
							pw.write("|^");
							pw.write("m76"); // 구리
							pw.write("|^");
							pw.write("m77"); // 아연
							pw.write("|^");
							pw.write("m78"); // 페놀
							pw.write("|^");
							pw.write("m79"); // 탁도1
							pw.write("|^");
							pw.write("m80"); // 탁도2
							pw.write("|^");
							pw.write("m81"); // 총유기탄소
							pw.write("|^");
							pw.write("m82"); // 수소가스노출
							pw.write("|^");
							pw.write("m83"); // 펌프수명
							pw.write("|^");
							pw.write("m84"); // 미생물 독성지수
							pw.write("|^");
							pw.write("m85"); // 전극(A)
							pw.write("|^");
							pw.write("m86"); // 전극(B)
							pw.write("|^");
							pw.write("m87"); // 조류 독성지수
							pw.write("|^");
							pw.write("m88"); // 조류 형광량(시료)
							pw.write("|^");
							pw.write("m89"); // 조류 최대형광량(시료)
							pw.write("|^");
							pw.write("m90"); // 조류 형광량(바탕시료)
							pw.write("|^");
							pw.write("m91"); // 조류 최대형광량(바탕시료)
							pw.write("|^");
							pw.write("m92"); // 조류 형광산출량(시료)
							pw.write("|^");
							pw.write("m93"); // 조류 형광산출량(바탕시료)
							pw.write("|^");
							pw.write("m94"); // 채수펌프 원격제어
							pw.write("|^");
							pw.write("m95"); // 강우량
							pw.write("|^");
							pw.write("m96"); // 저류수조수위
							pw.write("|^");
							pw.write("m97"); // 여과수조수위
							pw.write("|^");
							pw.write("m98"); // 필터유입압력
							pw.write("|^");
							pw.write("m99"); // 유량
							pw.write("|^");
							pw.write("m100"); // 페놀2
							pw.write("|^");
							pw.write("numOfRows"); // 한 페이지 결과 수
							pw.write("|^");
							pw.write("pageNo"); // 페이지 번호
							pw.write("|^");
							pw.write("totalCount"); // 전체 결과 수
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

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					JSONObject count_getRealTimeWaterQualityList = (JSONObject) count_obj
							.get("getRealTimeWaterQualityList");

					JSONObject count_header = (JSONObject) count_getRealTimeWaterQualityList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {

						int numOfRows = ((Long) count_getRealTimeWaterQualityList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getRealTimeWaterQualityList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;

					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer resultCode_col = new StringBuffer(" "); // 결과코드
					StringBuffer resultMsg_col = new StringBuffer(" "); // 결과메시지
					StringBuffer rowno = new StringBuffer(" "); // 순번
					StringBuffer siteId = new StringBuffer(" "); // 조사지점번호
					StringBuffer siteName = new StringBuffer(" "); // 조사지점명
					StringBuffer msrDate = new StringBuffer(" "); // 조사시간
					StringBuffer m01 = new StringBuffer(" "); // 통신상태
					StringBuffer m02 = new StringBuffer(" "); // 수온1
					StringBuffer m03 = new StringBuffer(" "); // 수소이온농도
					StringBuffer m04 = new StringBuffer(" "); // 전기전도도1
					StringBuffer m05 = new StringBuffer(" "); // 용존산소1
					StringBuffer m06 = new StringBuffer(" "); // 총유기탄소1
					StringBuffer m07 = new StringBuffer(" "); // 임펄스
					StringBuffer m08 = new StringBuffer(" "); // 수조pH
					StringBuffer m09 = new StringBuffer(" "); // 수온
					StringBuffer m10 = new StringBuffer(" "); // 수조 산소량
					StringBuffer m11 = new StringBuffer(" "); // 활동여부
					StringBuffer m12 = new StringBuffer(" "); // 염화메틸렌
					StringBuffer m13 = new StringBuffer(" "); // 1.1.1-트리클로로에테인
					StringBuffer m14 = new StringBuffer(" "); // 벤젠
					StringBuffer m15 = new StringBuffer(" "); // 사염화탄소
					StringBuffer m16 = new StringBuffer(" "); // 트리클로로에틸렌
					StringBuffer m17 = new StringBuffer(" "); // 톨루엔
					StringBuffer m18 = new StringBuffer(" "); // 테트라클로로에틸렌
					StringBuffer m19 = new StringBuffer(" "); // 에틸벤젠
					StringBuffer m20 = new StringBuffer(" "); // m,p-자일렌
					StringBuffer m21 = new StringBuffer(" "); // o-자일렌
					StringBuffer m22 = new StringBuffer(" "); // [ECD]염화메틸렌
					StringBuffer m23 = new StringBuffer(" "); // [ECD]1.1.1-트리클로로에테인
					StringBuffer m24 = new StringBuffer(" "); // [ECD]사염화탄소
					StringBuffer m25 = new StringBuffer(" "); // [ECD]트리클로로에틸렌
					StringBuffer m26 = new StringBuffer(" "); // [ECD}테트라클로로에틸렌
					StringBuffer m27 = new StringBuffer(" "); // 총질소
					StringBuffer m28 = new StringBuffer(" "); // 총인
					StringBuffer m29 = new StringBuffer(" "); // 클로로필-a
					StringBuffer m30 = new StringBuffer(" "); // 투과도
					StringBuffer m31 = new StringBuffer(" "); // 임펄스(우)
					StringBuffer m32 = new StringBuffer(" "); // 임펄스(좌)
					StringBuffer m33 = new StringBuffer(" "); // 수조수온(우)
					StringBuffer m34 = new StringBuffer(" "); // 수조수온(좌)
					StringBuffer m35 = new StringBuffer(" "); // 인산염인
					StringBuffer m36 = new StringBuffer(" "); // 암모니아성질소
					StringBuffer m37 = new StringBuffer(" "); // 질산성질소
					StringBuffer m38 = new StringBuffer(" "); // 수온2
					StringBuffer m39 = new StringBuffer(" "); // 수소이온농도2
					StringBuffer m40 = new StringBuffer(" "); // 전기전도도2
					StringBuffer m41 = new StringBuffer(" "); // 용존산소2
					StringBuffer m42 = new StringBuffer(" "); // 실내온도
					StringBuffer m43 = new StringBuffer(" "); // UPS전압
					StringBuffer m44 = new StringBuffer(" "); // 출입문 상태
					StringBuffer m45 = new StringBuffer(" "); // 유속
					StringBuffer m46 = new StringBuffer(" "); // 유압
					StringBuffer m47 = new StringBuffer(" "); // 채수펌프(우)
					StringBuffer m48 = new StringBuffer(" "); // 채수펌프(좌)
					StringBuffer m49 = new StringBuffer(" "); // 여과장치
					StringBuffer m50 = new StringBuffer(" "); // 항온항습기
					StringBuffer m51 = new StringBuffer(" "); // 자탐기
					StringBuffer m52 = new StringBuffer(" "); // 실내습도
					StringBuffer m53 = new StringBuffer(" "); // 전원상태
					StringBuffer m54 = new StringBuffer(" "); // 일반채수기
					StringBuffer m55 = new StringBuffer(" "); // VOCs 채수기
					StringBuffer m56 = new StringBuffer(" "); // 자일렌
					StringBuffer m57 = new StringBuffer(" "); // 독성지수(좌)
					StringBuffer m58 = new StringBuffer(" "); // 유영속도(좌)
					StringBuffer m59 = new StringBuffer(" "); // 개체수(좌)
					StringBuffer m60 = new StringBuffer(" "); // 유영속도 분포지수(좌)
					StringBuffer m61 = new StringBuffer(" "); // 프렉탈 차수(좌)
					StringBuffer m62 = new StringBuffer(" "); // 시료온도(좌)
					StringBuffer m63 = new StringBuffer(" "); // 독성지수(우)
					StringBuffer m64 = new StringBuffer(" "); // 유영속도(우)
					StringBuffer m65 = new StringBuffer(" "); // 개체수(우)
					StringBuffer m66 = new StringBuffer(" "); // 유영속도 분포지수(우)
					StringBuffer m67 = new StringBuffer(" "); // 프렉탈 차수(우)
					StringBuffer m68 = new StringBuffer(" "); // 시료온도(우)
					StringBuffer m69 = new StringBuffer(" "); // 수온3
					StringBuffer m70 = new StringBuffer(" "); // 수소이온농도3
					StringBuffer m71 = new StringBuffer(" "); // 전기전도도3
					StringBuffer m72 = new StringBuffer(" "); // 용존산소3
					StringBuffer m73 = new StringBuffer(" "); // 탁도3
					StringBuffer m74 = new StringBuffer(" "); // 카드뮴
					StringBuffer m75 = new StringBuffer(" "); // 납
					StringBuffer m76 = new StringBuffer(" "); // 구리
					StringBuffer m77 = new StringBuffer(" "); // 아연
					StringBuffer m78 = new StringBuffer(" "); // 페놀
					StringBuffer m79 = new StringBuffer(" "); // 탁도1
					StringBuffer m80 = new StringBuffer(" "); // 탁도2
					StringBuffer m81 = new StringBuffer(" "); // 총유기탄소
					StringBuffer m82 = new StringBuffer(" "); // 수소가스노출
					StringBuffer m83 = new StringBuffer(" "); // 펌프수명
					StringBuffer m84 = new StringBuffer(" "); // 미생물 독성지수
					StringBuffer m85 = new StringBuffer(" "); // 전극(A)
					StringBuffer m86 = new StringBuffer(" "); // 전극(B)
					StringBuffer m87 = new StringBuffer(" "); // 조류 독성지수
					StringBuffer m88 = new StringBuffer(" "); // 조류 형광량(시료)
					StringBuffer m89 = new StringBuffer(" "); // 조류 최대형광량(시료)
					StringBuffer m90 = new StringBuffer(" "); // 조류 형광량(바탕시료)
					StringBuffer m91 = new StringBuffer(" "); // 조류 최대형광량(바탕시료)
					StringBuffer m92 = new StringBuffer(" "); // 조류 형광산출량(시료)
					StringBuffer m93 = new StringBuffer(" "); // 조류 형광산출량(바탕시료)
					StringBuffer m94 = new StringBuffer(" "); // 채수펌프 원격제어
					StringBuffer m95 = new StringBuffer(" "); // 강우량
					StringBuffer m96 = new StringBuffer(" "); // 저류수조수위
					StringBuffer m97 = new StringBuffer(" "); // 여과수조수위
					StringBuffer m98 = new StringBuffer(" "); // 필터유입압력
					StringBuffer m99 = new StringBuffer(" "); // 유량
					StringBuffer m100 = new StringBuffer(" "); // 페놀2
					StringBuffer numOfRows = new StringBuffer(" "); // 한 페이지 결과
																	// 수
					StringBuffer pageNo_str = new StringBuffer(" "); // 페이지 번호
					StringBuffer totalCount = new StringBuffer(" "); // 전체 결과 수

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getRealTimeWaterQualityList = (JSONObject) obj.get("getRealTimeWaterQualityList");

						JSONObject header = (JSONObject) getRealTimeWaterQualityList.get("header");

						resultCode_col.setLength(0);
						resultCode_col.append(header.get("code").toString().trim()); // 결과
																						// 코드
						resultMsg_col.setLength(0);
						resultMsg_col.append(header.get("message").toString().trim()); // 결과
																						// 메시지
						numOfRows.setLength(0);
						numOfRows.append(getRealTimeWaterQualityList.get("numOfRows").toString().trim());

						pageNo_str.setLength(0);
						pageNo_str.append(String.valueOf(i).trim());

						totalCount.setLength(0);
						totalCount.append(getRealTimeWaterQualityList.get("totalCount").toString().trim());

						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {

							JSONArray items = (JSONArray) getRealTimeWaterQualityList.get("item");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(rowno, keyname, "ROWNO", item);
									JsonParser.colWrite(siteId, keyname, "SITE_ID", item);
									JsonParser.colWrite(siteName, keyname, "SITE_NAME", item);
									JsonParser.colWrite(msrDate, keyname, "MSR_DATE", item);
									JsonParser.colWrite(m01, keyname, "M01", item);
									JsonParser.colWrite(m02, keyname, "M02", item);
									JsonParser.colWrite(m03, keyname, "M03", item);
									JsonParser.colWrite(m04, keyname, "M04", item);
									JsonParser.colWrite(m05, keyname, "M05", item);
									JsonParser.colWrite(m06, keyname, "M06", item);
									JsonParser.colWrite(m07, keyname, "M07", item);
									JsonParser.colWrite(m08, keyname, "M08", item);
									JsonParser.colWrite(m09, keyname, "M09", item);
									JsonParser.colWrite(m10, keyname, "M10", item);
									JsonParser.colWrite(m11, keyname, "M11", item);
									JsonParser.colWrite(m12, keyname, "M12", item);
									JsonParser.colWrite(m13, keyname, "M13", item);
									JsonParser.colWrite(m14, keyname, "M14", item);
									JsonParser.colWrite(m15, keyname, "M15", item);
									JsonParser.colWrite(m16, keyname, "M16", item);
									JsonParser.colWrite(m17, keyname, "M17", item);
									JsonParser.colWrite(m18, keyname, "M18", item);
									JsonParser.colWrite(m19, keyname, "M19", item);
									JsonParser.colWrite(m20, keyname, "M20", item);
									JsonParser.colWrite(m21, keyname, "M21", item);
									JsonParser.colWrite(m22, keyname, "M22", item);
									JsonParser.colWrite(m23, keyname, "M23", item);
									JsonParser.colWrite(m24, keyname, "M24", item);
									JsonParser.colWrite(m25, keyname, "M25", item);
									JsonParser.colWrite(m26, keyname, "M26", item);
									JsonParser.colWrite(m27, keyname, "M27", item);
									JsonParser.colWrite(m28, keyname, "M28", item);
									JsonParser.colWrite(m29, keyname, "M29", item);
									JsonParser.colWrite(m30, keyname, "M30", item);
									JsonParser.colWrite(m31, keyname, "M31", item);
									JsonParser.colWrite(m32, keyname, "M32", item);
									JsonParser.colWrite(m33, keyname, "M33", item);
									JsonParser.colWrite(m34, keyname, "M34", item);
									JsonParser.colWrite(m35, keyname, "M35", item);
									JsonParser.colWrite(m36, keyname, "M36", item);
									JsonParser.colWrite(m37, keyname, "M37", item);
									JsonParser.colWrite(m38, keyname, "M38", item);
									JsonParser.colWrite(m39, keyname, "M39", item);
									JsonParser.colWrite(m40, keyname, "M40", item);
									JsonParser.colWrite(m41, keyname, "M41", item);
									JsonParser.colWrite(m42, keyname, "M42", item);
									JsonParser.colWrite(m43, keyname, "M43", item);
									JsonParser.colWrite(m44, keyname, "M44", item);
									JsonParser.colWrite(m45, keyname, "M45", item);
									JsonParser.colWrite(m46, keyname, "M46", item);
									JsonParser.colWrite(m47, keyname, "M47", item);
									JsonParser.colWrite(m48, keyname, "M48", item);
									JsonParser.colWrite(m49, keyname, "M49", item);
									JsonParser.colWrite(m50, keyname, "M50", item);
									JsonParser.colWrite(m51, keyname, "M51", item);
									JsonParser.colWrite(m52, keyname, "M52", item);
									JsonParser.colWrite(m53, keyname, "M53", item);
									JsonParser.colWrite(m54, keyname, "M54", item);
									JsonParser.colWrite(m55, keyname, "M55", item);
									JsonParser.colWrite(m56, keyname, "M56", item);
									JsonParser.colWrite(m57, keyname, "M57", item);
									JsonParser.colWrite(m58, keyname, "M58", item);
									JsonParser.colWrite(m59, keyname, "M59", item);
									JsonParser.colWrite(m60, keyname, "M60", item);
									JsonParser.colWrite(m61, keyname, "M61", item);
									JsonParser.colWrite(m62, keyname, "M62", item);
									JsonParser.colWrite(m63, keyname, "M63", item);
									JsonParser.colWrite(m64, keyname, "M64", item);
									JsonParser.colWrite(m65, keyname, "M65", item);
									JsonParser.colWrite(m66, keyname, "M66", item);
									JsonParser.colWrite(m67, keyname, "M67", item);
									JsonParser.colWrite(m68, keyname, "M68", item);
									JsonParser.colWrite(m69, keyname, "M69", item);
									JsonParser.colWrite(m70, keyname, "M70", item);
									JsonParser.colWrite(m71, keyname, "M71", item);
									JsonParser.colWrite(m72, keyname, "M72", item);
									JsonParser.colWrite(m73, keyname, "M73", item);
									JsonParser.colWrite(m74, keyname, "M74", item);
									JsonParser.colWrite(m75, keyname, "M75", item);
									JsonParser.colWrite(m76, keyname, "M76", item);
									JsonParser.colWrite(m77, keyname, "M77", item);
									JsonParser.colWrite(m78, keyname, "M78", item);
									JsonParser.colWrite(m79, keyname, "M79", item);
									JsonParser.colWrite(m80, keyname, "M80", item);
									JsonParser.colWrite(m81, keyname, "M81", item);
									JsonParser.colWrite(m82, keyname, "M82", item);
									JsonParser.colWrite(m83, keyname, "M83", item);
									JsonParser.colWrite(m84, keyname, "M84", item);
									JsonParser.colWrite(m85, keyname, "M85", item);
									JsonParser.colWrite(m86, keyname, "M86", item);
									JsonParser.colWrite(m87, keyname, "M87", item);
									JsonParser.colWrite(m88, keyname, "M88", item);
									JsonParser.colWrite(m89, keyname, "M89", item);
									JsonParser.colWrite(m90, keyname, "M90", item);
									JsonParser.colWrite(m91, keyname, "M91", item);
									JsonParser.colWrite(m92, keyname, "M92", item);
									JsonParser.colWrite(m93, keyname, "M93", item);
									JsonParser.colWrite(m94, keyname, "M94", item);
									JsonParser.colWrite(m95, keyname, "M95", item);
									JsonParser.colWrite(m96, keyname, "M96", item);
									JsonParser.colWrite(m97, keyname, "M97", item);
									JsonParser.colWrite(m98, keyname, "M98", item);
									JsonParser.colWrite(m99, keyname, "M99", item);
									JsonParser.colWrite(m100, keyname, "M100", item);
									JsonParser.colWrite(numOfRows, keyname, "numOfRows", item);
									JsonParser.colWrite(pageNo_str, keyname, "pageNo", item);
									JsonParser.colWrite(totalCount, keyname, "totalCount", item);

								}

								// 한번에 문자열 합침
								resultSb.append(resultCode_col);
								resultSb.append("|^");
								resultSb.append(resultMsg_col);
								resultSb.append("|^");
								resultSb.append(rowno);
								resultSb.append("|^");
								resultSb.append(siteId);
								resultSb.append("|^");
								resultSb.append(siteName);
								resultSb.append("|^");
								resultSb.append(msrDate);
								resultSb.append("|^");
								resultSb.append(m01);
								resultSb.append("|^");
								resultSb.append(m02);
								resultSb.append("|^");
								resultSb.append(m03);
								resultSb.append("|^");
								resultSb.append(m04);
								resultSb.append("|^");
								resultSb.append(m05);
								resultSb.append("|^");
								resultSb.append(m06);
								resultSb.append("|^");
								resultSb.append(m07);
								resultSb.append("|^");
								resultSb.append(m08);
								resultSb.append("|^");
								resultSb.append(m09);
								resultSb.append("|^");
								resultSb.append(m10);
								resultSb.append("|^");
								resultSb.append(m11);
								resultSb.append("|^");
								resultSb.append(m12);
								resultSb.append("|^");
								resultSb.append(m13);
								resultSb.append("|^");
								resultSb.append(m14);
								resultSb.append("|^");
								resultSb.append(m15);
								resultSb.append("|^");
								resultSb.append(m16);
								resultSb.append("|^");
								resultSb.append(m17);
								resultSb.append("|^");
								resultSb.append(m18);
								resultSb.append("|^");
								resultSb.append(m19);
								resultSb.append("|^");
								resultSb.append(m20);
								resultSb.append("|^");
								resultSb.append(m21);
								resultSb.append("|^");
								resultSb.append(m22);
								resultSb.append("|^");
								resultSb.append(m23);
								resultSb.append("|^");
								resultSb.append(m24);
								resultSb.append("|^");
								resultSb.append(m25);
								resultSb.append("|^");
								resultSb.append(m26);
								resultSb.append("|^");
								resultSb.append(m27);
								resultSb.append("|^");
								resultSb.append(m28);
								resultSb.append("|^");
								resultSb.append(m29);
								resultSb.append("|^");
								resultSb.append(m30);
								resultSb.append("|^");
								resultSb.append(m31);
								resultSb.append("|^");
								resultSb.append(m32);
								resultSb.append("|^");
								resultSb.append(m33);
								resultSb.append("|^");
								resultSb.append(m34);
								resultSb.append("|^");
								resultSb.append(m35);
								resultSb.append("|^");
								resultSb.append(m36);
								resultSb.append("|^");
								resultSb.append(m37);
								resultSb.append("|^");
								resultSb.append(m38);
								resultSb.append("|^");
								resultSb.append(m39);
								resultSb.append("|^");
								resultSb.append(m40);
								resultSb.append("|^");
								resultSb.append(m41);
								resultSb.append("|^");
								resultSb.append(m42);
								resultSb.append("|^");
								resultSb.append(m43);
								resultSb.append("|^");
								resultSb.append(m44);
								resultSb.append("|^");
								resultSb.append(m45);
								resultSb.append("|^");
								resultSb.append(m46);
								resultSb.append("|^");
								resultSb.append(m47);
								resultSb.append("|^");
								resultSb.append(m48);
								resultSb.append("|^");
								resultSb.append(m49);
								resultSb.append("|^");
								resultSb.append(m50);
								resultSb.append("|^");
								resultSb.append(m51);
								resultSb.append("|^");
								resultSb.append(m52);
								resultSb.append("|^");
								resultSb.append(m53);
								resultSb.append("|^");
								resultSb.append(m54);
								resultSb.append("|^");
								resultSb.append(m55);
								resultSb.append("|^");
								resultSb.append(m56);
								resultSb.append("|^");
								resultSb.append(m57);
								resultSb.append("|^");
								resultSb.append(m58);
								resultSb.append("|^");
								resultSb.append(m59);
								resultSb.append("|^");
								resultSb.append(m60);
								resultSb.append("|^");
								resultSb.append(m61);
								resultSb.append("|^");
								resultSb.append(m62);
								resultSb.append("|^");
								resultSb.append(m63);
								resultSb.append("|^");
								resultSb.append(m64);
								resultSb.append("|^");
								resultSb.append(m65);
								resultSb.append("|^");
								resultSb.append(m66);
								resultSb.append("|^");
								resultSb.append(m67);
								resultSb.append("|^");
								resultSb.append(m68);
								resultSb.append("|^");
								resultSb.append(m69);
								resultSb.append("|^");
								resultSb.append(m70);
								resultSb.append("|^");
								resultSb.append(m71);
								resultSb.append("|^");
								resultSb.append(m72);
								resultSb.append("|^");
								resultSb.append(m73);
								resultSb.append("|^");
								resultSb.append(m74);
								resultSb.append("|^");
								resultSb.append(m75);
								resultSb.append("|^");
								resultSb.append(m76);
								resultSb.append("|^");
								resultSb.append(m77);
								resultSb.append("|^");
								resultSb.append(m78);
								resultSb.append("|^");
								resultSb.append(m79);
								resultSb.append("|^");
								resultSb.append(m80);
								resultSb.append("|^");
								resultSb.append(m81);
								resultSb.append("|^");
								resultSb.append(m82);
								resultSb.append("|^");
								resultSb.append(m83);
								resultSb.append("|^");
								resultSb.append(m84);
								resultSb.append("|^");
								resultSb.append(m85);
								resultSb.append("|^");
								resultSb.append(m86);
								resultSb.append("|^");
								resultSb.append(m87);
								resultSb.append("|^");
								resultSb.append(m88);
								resultSb.append("|^");
								resultSb.append(m89);
								resultSb.append("|^");
								resultSb.append(m90);
								resultSb.append("|^");
								resultSb.append(m91);
								resultSb.append("|^");
								resultSb.append(m92);
								resultSb.append("|^");
								resultSb.append(m93);
								resultSb.append("|^");
								resultSb.append(m94);
								resultSb.append("|^");
								resultSb.append(m95);
								resultSb.append("|^");
								resultSb.append(m96);
								resultSb.append("|^");
								resultSb.append(m97);
								resultSb.append("|^");
								resultSb.append(m98);
								resultSb.append("|^");
								resultSb.append(m99);
								resultSb.append("|^");
								resultSb.append(m100);
								resultSb.append("|^");
								resultSb.append(numOfRows);
								resultSb.append("|^");
								resultSb.append(pageNo_str);
								resultSb.append("|^");
								resultSb.append(totalCount);
								resultSb.append(System.getProperty("line.separator"));
							}

						} else {
							System.out.println("parsing error!! :::" + resultCode_col.toString());
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						Thread.sleep(2500);

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

					// TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_03.dat", "PRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
