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

import common.JsonParser;
//import common.TransSftp;

public class GetRealTimeWaterQualityList {

	// 수질정보 DB 서비스 - 수질자동측정망 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// '시작일자', '종료일자'를 파라미터로 받음 (둘 다 필수값)
				if (args.length == 2) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("PRI_WaterQualityService_getRealTimeWaterQualityList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_03.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

					int pageNo = 0;
					int pageCount = 0;

					// 수질자동측정망 운영결과 DB API에서는 ptNoList는 필요 없음
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"getRealTimeWaterQualityList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}*/

					JSONObject count_obj = JsonParser.parsePriJson_realTimeWater_obj(service_url, service_key, String.valueOf(pageNo), args[0], args[1]);

					JSONObject count_getRealTimeWaterQualityList = (JSONObject) count_obj
							.get("getRealTimeWaterQualityList");

					JSONObject count_header = (JSONObject) count_getRealTimeWaterQualityList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_getRealTimeWaterQualityList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getRealTimeWaterQualityList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					

					for (int i = 1; i <= pageCount; i++) {

						// 수질자동측정망 운영결과 DB API에서는 ptNoList는 필요 없음
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"getRealTimeWaterQualityList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}*/

						JSONObject obj = JsonParser.parsePriJson_realTimeWater_obj(service_url, service_key, String.valueOf(i), args[0], args[1]);

						JSONObject getRealTimeWaterQualityList = (JSONObject) obj.get("getRealTimeWaterQualityList");

						JSONObject header = (JSONObject) getRealTimeWaterQualityList.get("header");
						
						String resultCode_col = " "; // 결과코드
						String resultMsg_col = " "; // 결과메시지

						resultCode_col = header.get("code").toString().trim(); // 결과
																						// 코드
						resultMsg_col = header.get("message").toString().trim(); // 결과
																						// 메시지
						
						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {
							
							String  numOfRows = " "; // 한 페이지 결과수
							String  pageNo_str = " "; // 페이지 번호
							String  totalCount = " "; // 전체 결과 수
							
							numOfRows = getRealTimeWaterQualityList.get("numOfRows").toString().trim();

							pageNo_str = String.valueOf(i).trim();

							totalCount = getRealTimeWaterQualityList.get("totalCount").toString().trim();

							JSONArray items = (JSONArray) getRealTimeWaterQualityList.get("item");

							for (int r = 0; r < items.size(); r++) {
								
								String  rowno = " "; // 순번
								String  siteId = " "; // 조사지점번호
								String  siteName = " "; // 조사지점명
								String  msrDate = " "; // 조사시간
								String  m01 = " "; // 통신상태
								String  m02 = " "; // 수온1
								String  m03 = " "; // 수소이온농도
								String  m04 = " "; // 전기전도도1
								String  m05 = " "; // 용존산소1
								String  m06 = " "; // 총유기탄소1
								String  m07 = " "; // 임펄스
								String  m08 = " "; // 수조pH
								String  m09 = " "; // 수온
								String  m10 = " "; // 수조 산소량
								String  m11 = " "; // 활동여부
								String  m12 = " "; // 염화메틸렌
								String  m13 = " "; // 1.1.1-트리클로로에테인
								String  m14 = " "; // 벤젠
								String  m15 = " "; // 사염화탄소
								String  m16 = " "; // 트리클로로에틸렌
								String  m17 = " "; // 톨루엔
								String  m18 = " "; // 테트라클로로에틸렌
								String  m19 = " "; // 에틸벤젠
								String  m20 = " "; // m,p-자일렌
								String  m21 = " "; // o-자일렌
								String  m22 = " "; // [ECD]염화메틸렌
								String  m23 = " "; // [ECD]1.1.1-트리클로로에테인
								String  m24 = " "; // [ECD]사염화탄소
								String  m25 = " "; // [ECD]트리클로로에틸렌
								String  m26 = " "; // [ECD}테트라클로로에틸렌
								String  m27 = " "; // 총질소
								String  m28 = " "; // 총인
								String  m29 = " "; // 클로로필-a
								String  m30 = " "; // 투과도
								String  m31 = " "; // 임펄스(우)
								String  m32 = " "; // 임펄스(좌)
								String  m33 = " "; // 수조수온(우)
								String  m34 = " "; // 수조수온(좌)
								String  m35 = " "; // 인산염인
								String  m36 = " "; // 암모니아성질소
								String  m37 = " "; // 질산성질소
								String  m38 = " "; // 수온2
								String  m39 = " "; // 수소이온농도2
								String  m40 = " "; // 전기전도도2
								String  m41 = " "; // 용존산소2
								String  m42 = " "; // 실내온도
								String  m43 = " "; // UPS전압
								String  m44 = " "; // 출입문 상태
								String  m45 = " "; // 유속
								String  m46 = " "; // 유압
								String  m47 = " "; // 채수펌프(우)
								String  m48 = " "; // 채수펌프(좌)
								String  m49 = " "; // 여과장치
								String  m50 = " "; // 항온항습기
								String  m51 = " "; // 자탐기
								String  m52 = " "; // 실내습도
								String  m53 = " "; // 전원상태
								String  m54 = " "; // 일반채수기
								String  m55 = " "; // VOCs 채수기
								String  m56 = " "; // 자일렌
								String  m57 = " "; // 독성지수(좌)
								String  m58 = " "; // 유영속도(좌)
								String  m59 = " "; // 개체수(좌)
								String  m60 = " "; // 유영속도 분포지수(좌)
								String  m61 = " "; // 프렉탈 차수(좌)
								String  m62 = " "; // 시료온도(좌)
								String  m63 = " "; // 독성지수(우)
								String  m64 = " "; // 유영속도(우)
								String  m65 = " "; // 개체수(우)
								String  m66 = " "; // 유영속도 분포지수(우)
								String  m67 = " "; // 프렉탈 차수(우)
								String  m68 = " "; // 시료온도(우)
								String  m69 = " "; // 수온3
								String  m70 = " "; // 수소이온농도3
								String  m71 = " "; // 전기전도도3
								String  m72 = " "; // 용존산소3
								String  m73 = " "; // 탁도3
								String  m74 = " "; // 카드뮴
								String  m75 = " "; // 납
								String  m76 = " "; // 구리
								String  m77 = " "; // 아연
								String  m78 = " "; // 페놀
								String  m79 = " "; // 탁도1
								String  m80 = " "; // 탁도2
								String  m81 = " "; // 총유기탄소
								String  m82 = " "; // 수소가스노출
								String  m83 = " "; // 펌프수명
								String  m84 = " "; // 미생물 독성지수
								String  m85 = " "; // 전극(A)
								String  m86 = " "; // 전극(B)
								String  m87 = " "; // 조류 독성지수
								String  m88 = " "; // 조류 형광량(시료)
								String  m89 = " "; // 조류 최대형광량(시료)
								String  m90 = " "; // 조류 형광량(바탕시료)
								String  m91 = " "; // 조류 최대형광량(바탕시료)
								String  m92 = " "; // 조류 형광산출량(시료)
								String  m93 = " "; // 조류 형광산출량(바탕시료)
								String  m94 = " "; // 채수펌프 원격제어
								String  m95 = " "; // 강우량
								String  m96 = " "; // 저류수조수위
								String  m97 = " "; // 여과수조수위
								String  m98 = " "; // 필터유입압력
								String  m99 = " "; // 유량
								String  m100 = " "; // 페놀2

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									rowno = JsonParser.colWrite_String(rowno, keyname, "ROWNO", item);
									siteId = JsonParser.colWrite_String(siteId, keyname, "SITE_ID", item);
									siteName = JsonParser.colWrite_String(siteName, keyname, "SITE_NAME", item);
									msrDate = JsonParser.colWrite_String(msrDate, keyname, "MSR_DATE", item);
									m01 = JsonParser.colWrite_String(m01, keyname, "M01", item);
									m02 = JsonParser.colWrite_String(m02, keyname, "M02", item);
									m03 = JsonParser.colWrite_String(m03, keyname, "M03", item);
									m04 = JsonParser.colWrite_String(m04, keyname, "M04", item);
									m05 = JsonParser.colWrite_String(m05, keyname, "M05", item);
									m06 = JsonParser.colWrite_String(m06, keyname, "M06", item);
									m07 = JsonParser.colWrite_String(m07, keyname, "M07", item);
									m08 = JsonParser.colWrite_String(m08, keyname, "M08", item);
									m09 = JsonParser.colWrite_String(m09, keyname, "M09", item);
									m10 = JsonParser.colWrite_String(m10, keyname, "M10", item);
									m11 = JsonParser.colWrite_String(m11, keyname, "M11", item);
									m12 = JsonParser.colWrite_String(m12, keyname, "M12", item);
									m13 = JsonParser.colWrite_String(m13, keyname, "M13", item);
									m14 = JsonParser.colWrite_String(m14, keyname, "M14", item);
									m15 = JsonParser.colWrite_String(m15, keyname, "M15", item);
									m16 = JsonParser.colWrite_String(m16, keyname, "M16", item);
									m17 = JsonParser.colWrite_String(m17, keyname, "M17", item);
									m18 = JsonParser.colWrite_String(m18, keyname, "M18", item);
									m19 = JsonParser.colWrite_String(m19, keyname, "M19", item);
									m20 = JsonParser.colWrite_String(m20, keyname, "M20", item);
									m21 = JsonParser.colWrite_String(m21, keyname, "M21", item);
									m22 = JsonParser.colWrite_String(m22, keyname, "M22", item);
									m23 = JsonParser.colWrite_String(m23, keyname, "M23", item);
									m24 = JsonParser.colWrite_String(m24, keyname, "M24", item);
									m25 = JsonParser.colWrite_String(m25, keyname, "M25", item);
									m26 = JsonParser.colWrite_String(m26, keyname, "M26", item);
									m27 = JsonParser.colWrite_String(m27, keyname, "M27", item);
									m28 = JsonParser.colWrite_String(m28, keyname, "M28", item);
									m29 = JsonParser.colWrite_String(m29, keyname, "M29", item);
									m30 = JsonParser.colWrite_String(m30, keyname, "M30", item);
									m31 = JsonParser.colWrite_String(m31, keyname, "M31", item);
									m32 = JsonParser.colWrite_String(m32, keyname, "M32", item);
									m33 = JsonParser.colWrite_String(m33, keyname, "M33", item);
									m34 = JsonParser.colWrite_String(m34, keyname, "M34", item);
									m35 = JsonParser.colWrite_String(m35, keyname, "M35", item);
									m36 = JsonParser.colWrite_String(m36, keyname, "M36", item);
									m37 = JsonParser.colWrite_String(m37, keyname, "M37", item);
									m38 = JsonParser.colWrite_String(m38, keyname, "M38", item);
									m39 = JsonParser.colWrite_String(m39, keyname, "M39", item);
									m40 = JsonParser.colWrite_String(m40, keyname, "M40", item);
									m41 = JsonParser.colWrite_String(m41, keyname, "M41", item);
									m42 = JsonParser.colWrite_String(m42, keyname, "M42", item);
									m43 = JsonParser.colWrite_String(m43, keyname, "M43", item);
									m44 = JsonParser.colWrite_String(m44, keyname, "M44", item);
									m45 = JsonParser.colWrite_String(m45, keyname, "M45", item);
									m46 = JsonParser.colWrite_String(m46, keyname, "M46", item);
									m47 = JsonParser.colWrite_String(m47, keyname, "M47", item);
									m48 = JsonParser.colWrite_String(m48, keyname, "M48", item);
									m49 = JsonParser.colWrite_String(m49, keyname, "M49", item);
									m50 = JsonParser.colWrite_String(m50, keyname, "M50", item);
									m51 = JsonParser.colWrite_String(m51, keyname, "M51", item);
									m52 = JsonParser.colWrite_String(m52, keyname, "M52", item);
									m53 = JsonParser.colWrite_String(m53, keyname, "M53", item);
									m54 = JsonParser.colWrite_String(m54, keyname, "M54", item);
									m55 = JsonParser.colWrite_String(m55, keyname, "M55", item);
									m56 = JsonParser.colWrite_String(m56, keyname, "M56", item);
									m57 = JsonParser.colWrite_String(m57, keyname, "M57", item);
									m58 = JsonParser.colWrite_String(m58, keyname, "M58", item);
									m59 = JsonParser.colWrite_String(m59, keyname, "M59", item);
									m60 = JsonParser.colWrite_String(m60, keyname, "M60", item);
									m61 = JsonParser.colWrite_String(m61, keyname, "M61", item);
									m62 = JsonParser.colWrite_String(m62, keyname, "M62", item);
									m63 = JsonParser.colWrite_String(m63, keyname, "M63", item);
									m64 = JsonParser.colWrite_String(m64, keyname, "M64", item);
									m65 = JsonParser.colWrite_String(m65, keyname, "M65", item);
									m66 = JsonParser.colWrite_String(m66, keyname, "M66", item);
									m67 = JsonParser.colWrite_String(m67, keyname, "M67", item);
									m68 = JsonParser.colWrite_String(m68, keyname, "M68", item);
									m69 = JsonParser.colWrite_String(m69, keyname, "M69", item);
									m70 = JsonParser.colWrite_String(m70, keyname, "M70", item);
									m71 = JsonParser.colWrite_String(m71, keyname, "M71", item);
									m72 = JsonParser.colWrite_String(m72, keyname, "M72", item);
									m73 = JsonParser.colWrite_String(m73, keyname, "M73", item);
									m74 = JsonParser.colWrite_String(m74, keyname, "M74", item);
									m75 = JsonParser.colWrite_String(m75, keyname, "M75", item);
									m76 = JsonParser.colWrite_String(m76, keyname, "M76", item);
									m77 = JsonParser.colWrite_String(m77, keyname, "M77", item);
									m78 = JsonParser.colWrite_String(m78, keyname, "M78", item);
									m79 = JsonParser.colWrite_String(m79, keyname, "M79", item);
									m80 = JsonParser.colWrite_String(m80, keyname, "M80", item);
									m81 = JsonParser.colWrite_String(m81, keyname, "M81", item);
									m82 = JsonParser.colWrite_String(m82, keyname, "M82", item);
									m83 = JsonParser.colWrite_String(m83, keyname, "M83", item);
									m84 = JsonParser.colWrite_String(m84, keyname, "M84", item);
									m85 = JsonParser.colWrite_String(m85, keyname, "M85", item);
									m86 = JsonParser.colWrite_String(m86, keyname, "M86", item);
									m87 = JsonParser.colWrite_String(m87, keyname, "M87", item);
									m88 = JsonParser.colWrite_String(m88, keyname, "M88", item);
									m89 = JsonParser.colWrite_String(m89, keyname, "M89", item);
									m90 = JsonParser.colWrite_String(m90, keyname, "M90", item);
									m91 = JsonParser.colWrite_String(m91, keyname, "M91", item);
									m92 = JsonParser.colWrite_String(m92, keyname, "M92", item);
									m93 = JsonParser.colWrite_String(m93, keyname, "M93", item);
									m94 = JsonParser.colWrite_String(m94, keyname, "M94", item);
									m95 = JsonParser.colWrite_String(m95, keyname, "M95", item);
									m96 = JsonParser.colWrite_String(m96, keyname, "M96", item);
									m97 = JsonParser.colWrite_String(m97, keyname, "M97", item);
									m98 = JsonParser.colWrite_String(m98, keyname, "M98", item);
									m99 = JsonParser.colWrite_String(m99, keyname, "M99", item);
									m100 = JsonParser.colWrite_String(m100, keyname, "M100", item);
									numOfRows = JsonParser.colWrite_String(numOfRows, keyname, "numOfRows", item);
									pageNo_str = JsonParser.colWrite_String(pageNo_str, keyname, "pageNo", item);
									totalCount = JsonParser.colWrite_String(totalCount, keyname, "totalCount", item);

								}
								
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode_col); 
									pw.write("|^");
									pw.write(resultMsg_col); 
									pw.write("|^");
									pw.write(rowno); 
									pw.write("|^");
									pw.write(siteId); 
									pw.write("|^");
									pw.write(siteName); 
									pw.write("|^");
									pw.write(msrDate); 
									pw.write("|^");
									pw.write(m01);
									pw.write("|^");
									pw.write(m02);
									pw.write("|^");
									pw.write(m03);
									pw.write("|^");
									pw.write(m04);
									pw.write("|^");
									pw.write(m05);
									pw.write("|^");
									pw.write(m06);
									pw.write("|^");
									pw.write(m07);
									pw.write("|^");
									pw.write(m08);
									pw.write("|^");
									pw.write(m09);
									pw.write("|^");
									pw.write(m10);
									pw.write("|^");
									pw.write(m11);
									pw.write("|^");
									pw.write(m12);
									pw.write("|^");
									pw.write(m13);
									pw.write("|^");
									pw.write(m14);
									pw.write("|^");
									pw.write(m15);
									pw.write("|^");
									pw.write(m16);
									pw.write("|^");
									pw.write(m17);
									pw.write("|^");
									pw.write(m18);
									pw.write("|^");
									pw.write(m19);
									pw.write("|^");
									pw.write(m20);
									pw.write("|^");
									pw.write(m21);
									pw.write("|^");
									pw.write(m22);
									pw.write("|^");
									pw.write(m23);
									pw.write("|^");
									pw.write(m24);
									pw.write("|^");
									pw.write(m25);
									pw.write("|^");
									pw.write(m26);
									pw.write("|^");
									pw.write(m27);
									pw.write("|^");
									pw.write(m28);
									pw.write("|^");
									pw.write(m29);
									pw.write("|^");
									pw.write(m30);
									pw.write("|^");
									pw.write(m31);
									pw.write("|^");
									pw.write(m32);
									pw.write("|^");
									pw.write(m33);
									pw.write("|^");
									pw.write(m34);
									pw.write("|^");
									pw.write(m35);
									pw.write("|^");
									pw.write(m36);
									pw.write("|^");
									pw.write(m37);
									pw.write("|^");
									pw.write(m38);
									pw.write("|^");
									pw.write(m39);
									pw.write("|^");
									pw.write(m40);
									pw.write("|^");
									pw.write(m41);
									pw.write("|^");
									pw.write(m42);
									pw.write("|^");
									pw.write(m43);
									pw.write("|^");
									pw.write(m44);
									pw.write("|^");
									pw.write(m45);
									pw.write("|^");
									pw.write(m46);
									pw.write("|^");
									pw.write(m47);
									pw.write("|^");
									pw.write(m48);
									pw.write("|^");
									pw.write(m49);
									pw.write("|^");
									pw.write(m50);
									pw.write("|^");
									pw.write(m51);
									pw.write("|^");
									pw.write(m52);
									pw.write("|^");
									pw.write(m53);
									pw.write("|^");
									pw.write(m54);
									pw.write("|^");
									pw.write(m55);
									pw.write("|^");
									pw.write(m56);
									pw.write("|^");
									pw.write(m57);
									pw.write("|^");
									pw.write(m58);
									pw.write("|^");
									pw.write(m59);
									pw.write("|^");
									pw.write(m60);
									pw.write("|^");
									pw.write(m61);
									pw.write("|^");
									pw.write(m62);
									pw.write("|^");
									pw.write(m63);
									pw.write("|^");
									pw.write(m64);
									pw.write("|^");
									pw.write(m65);
									pw.write("|^");
									pw.write(m66);
									pw.write("|^");
									pw.write(m67);
									pw.write("|^");
									pw.write(m68);
									pw.write("|^");
									pw.write(m69);
									pw.write("|^");
									pw.write(m70);
									pw.write("|^");
									pw.write(m71);
									pw.write("|^");
									pw.write(m72);
									pw.write("|^");
									pw.write(m73);
									pw.write("|^");
									pw.write(m74);
									pw.write("|^");
									pw.write(m75);
									pw.write("|^");
									pw.write(m76);
									pw.write("|^");
									pw.write(m77);
									pw.write("|^");
									pw.write(m78);
									pw.write("|^");
									pw.write(m79);
									pw.write("|^");
									pw.write(m80);
									pw.write("|^");
									pw.write(m81);
									pw.write("|^");
									pw.write(m82);
									pw.write("|^");
									pw.write(m83);
									pw.write("|^");
									pw.write(m84);
									pw.write("|^");
									pw.write(m85);
									pw.write("|^");
									pw.write(m86);
									pw.write("|^");
									pw.write(m87);
									pw.write("|^");
									pw.write(m88);
									pw.write("|^");
									pw.write(m89);
									pw.write("|^");
									pw.write(m90);
									pw.write("|^");
									pw.write(m91);
									pw.write("|^");
									pw.write(m92);
									pw.write("|^");
									pw.write(m93);
									pw.write("|^");
									pw.write(m94);
									pw.write("|^");
									pw.write(m95);
									pw.write("|^");
									pw.write(m96);
									pw.write("|^");
									pw.write(m97);
									pw.write("|^");
									pw.write(m98);
									pw.write("|^");
									pw.write(m99);
									pw.write("|^");
									pw.write(m100);
									pw.write("|^");
									pw.write(numOfRows);
									pw.write("|^");
									pw.write(pageNo_str);
									pw.write("|^");
									pw.write(totalCount);
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			

							}

						} else {
							System.out.println("parsing error!! :::" + resultCode_col.toString());
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(2500);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_03.dat", "PRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ptNoList :" + args[0]);
			}


	}

}