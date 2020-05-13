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


			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("PRI_WaterQualityService_getRealTimeWaterQualityList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_03.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"getRealTimeWaterQualityList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}

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

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"getRealTimeWaterQualityList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getRealTimeWaterQualityList = (JSONObject) obj.get("getRealTimeWaterQualityList");

						JSONObject header = (JSONObject) getRealTimeWaterQualityList.get("header");
						
						String resultCode_col = " "; // 결과코드
						String resultMsg_col = " "; // 결과메시지

						resultCode_col = header.get("code").toString().trim(); // 결과
																						// 코드
						resultMsg_col = header.get("message").toString().trim(); // 결과
																						// 메시지

						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {
							
							String rowno = " "; // 순번
							String siteId = " "; // 조사지점번호
							String siteName = " "; // 조사지점명
							String msrDate = " "; // 조사시간
							String m01 = " "; // 통신상태
							String m02 = " "; // 수온1
							String m03 = " "; // 수소이온농도
							String m04 = " "; // 전기전도도1
							String m05 = " "; // 용존산소1
							String m06 = " "; // 총유기탄소1
							String m07 = " "; // 임펄스
							String m08 = " "; // 수조pH
							String m09 = " "; // 수온
							String m10 = " "; // 수조 산소량
							String m11 = " "; // 활동여부
							String m12 = " "; // 염화메틸렌
							String m13 = " "; // 1.1.1-트리클로로에테인
							String m14 = " "; // 벤젠
							String m15 = " "; // 사염화탄소
							String m16 = " "; // 트리클로로에틸렌
							String m17 = " "; // 톨루엔
							String m18 = " "; // 테트라클로로에틸렌
							String m19 = " "; // 에틸벤젠
							String m20 = " "; // m,p-자일렌
							String m21 = " "; // o-자일렌
							String m22 = " "; // [ECD]염화메틸렌
							String m23 = " "; // [ECD]1.1.1-트리클로로에테인
							String m24 = " "; // [ECD]사염화탄소
							String m25 = " "; // [ECD]트리클로로에틸렌
							String m26 = " "; // [ECD}테트라클로로에틸렌
							String m27 = " "; // 총질소
							String m28 = " "; // 총인
							String m29 = " "; // 클로로필-a
							String m30 = " "; // 투과도
							String m31 = " "; // 임펄스(우)
							String m32 = " "; // 임펄스(좌)
							String m33 = " "; // 수조수온(우)
							String m34 = " "; // 수조수온(좌)
							String m35 = " "; // 인산염인
							String m36 = " "; // 암모니아성질소
							String m37 = " "; // 질산성질소
							String m38 = " "; // 수온2
							String m39 = " "; // 수소이온농도2
							String m40 = " "; // 전기전도도2
							String m41 = " "; // 용존산소2
							String m42 = " "; // 실내온도
							String m43 = " "; // UPS전압
							String m44 = " "; // 출입문 상태
							String m45 = " "; // 유속
							String m46 = " "; // 유압
							String m47 = " "; // 채수펌프(우)
							String m48 = " "; // 채수펌프(좌)
							String m49 = " "; // 여과장치
							String m50 = " "; // 항온항습기
							String m51 = " "; // 자탐기
							String m52 = " "; // 실내습도
							String m53 = " "; // 전원상태
							String m54 = " "; // 일반채수기
							String m55 = " "; // VOCs 채수기
							String m56 = " "; // 자일렌
							String m57 = " "; // 독성지수(좌)
							String m58 = " "; // 유영속도(좌)
							String m59 = " "; // 개체수(좌)
							String m60 = " "; // 유영속도 분포지수(좌)
							String m61 = " "; // 프렉탈 차수(좌)
							String m62 = " "; // 시료온도(좌)
							String m63 = " "; // 독성지수(우)
							String m64 = " "; // 유영속도(우)
							String m65 = " "; // 개체수(우)
							String m66 = " "; // 유영속도 분포지수(우)
							String m67 = " "; // 프렉탈 차수(우)
							String m68 = " "; // 시료온도(우)
							String m69 = " "; // 수온3
							String m70 = " "; // 수소이온농도3
							String m71 = " "; // 전기전도도3
							String m72 = " "; // 용존산소3
							String m73 = " "; // 탁도3
							String m74 = " "; // 카드뮴
							String m75 = " "; // 납
							String m76 = " "; // 구리
							String m77 = " "; // 아연
							String m78 = " "; // 페놀
							String m79 = " "; // 탁도1
							String m80 = " "; // 탁도2
							String m81 = " "; // 총유기탄소
							String m82 = " "; // 수소가스노출
							String m83 = " "; // 펌프수명
							String m84 = " "; // 미생물 독성지수
							String m85 = " "; // 전극(A)
							String m86 = " "; // 전극(B)
							String m87 = " "; // 조류 독성지수
							String m88 = " "; // 조류 형광량(시료)
							String m89 = " "; // 조류 최대형광량(시료)
							String m90 = " "; // 조류 형광량(바탕시료)
							String m91 = " "; // 조류 최대형광량(바탕시료)
							String m92 = " "; // 조류 형광산출량(시료)
							String m93 = " "; // 조류 형광산출량(바탕시료)
							String m94 = " "; // 채수펌프 원격제어
							String m95 = " "; // 강우량
							String m96 = " "; // 저류수조수위
							String m97 = " "; // 여과수조수위
							String m98 = " "; // 필터유입압력
							String m99 = " "; // 유량
							String m100 = " "; // 페놀2
							String numOfRows = " "; // 한 페이지 결과
																			// 수
							String pageNo_str = " "; // 페이지 번호
							String totalCount = " "; // 전체 결과 수

							JSONArray items = (JSONArray) getRealTimeWaterQualityList.get("item");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									if(keyname.equals("ROWNO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											rowno = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											rowno = " ";
										}
									}
									if(keyname.equals("SITE_ID")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											siteId = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											siteId = " ";
										}
									}
									if(keyname.equals("SITE_NAME")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											siteName = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											siteName = " ";
										}
									}
									if(keyname.equals("MSR_DATE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											msrDate = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											msrDate = " ";
										}
									}
									if(keyname.equals("M01")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m01 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m01 = " ";
										}
									}
									if(keyname.equals("M02")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m02 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m02 = " ";
										}
									}
									if(keyname.equals("M03")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m03 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m03 = " ";
										}
									}
									if(keyname.equals("M04")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m04 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m04 = " ";
										}
									}
									if(keyname.equals("M05")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m05 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m05 = " ";
										}
									}
									if(keyname.equals("M06")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m06 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m06 = " ";
										}
									}
									if(keyname.equals("M07")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m07 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m07 = " ";
										}
									}
									if(keyname.equals("M08")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m08 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m08 = " ";
										}
									}
									if(keyname.equals("M09")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m09 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m09 = " ";
										}
									}
									if(keyname.equals("M10")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m10 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m10 = " ";
										}
									}
									if(keyname.equals("M11")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m11 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m11 = " ";
										}
									}
									if(keyname.equals("M12")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m12 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m12 = " ";
										}
									}
									if(keyname.equals("M13")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m13 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m13 = " ";
										}
									}
									if(keyname.equals("M14")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m14 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m14 = " ";
										}
									}
									if(keyname.equals("M15")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m15 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m15 = " ";
										}
									}
									if(keyname.equals("M16")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m16 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m16 = " ";
										}
									}
									if(keyname.equals("M17")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m17 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m17 = " ";
										}
									}
									if(keyname.equals("M18")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m18 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m18 = " ";
										}
									}
									if(keyname.equals("M19")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m19 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m19 = " ";
										}
									}
									if(keyname.equals("M20")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m20 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m20 = " ";
										}
									}
									if(keyname.equals("M21")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m21 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m21 = " ";
										}
									}
									if(keyname.equals("M22")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m22 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m22 = " ";
										}
									}
									if(keyname.equals("M23")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m23 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m23 = " ";
										}
									}
									if(keyname.equals("M24")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m24 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m24 = " ";
										}
									}
									if(keyname.equals("M25")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m25 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m25 = " ";
										}
									}
									if(keyname.equals("M26")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m26 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m26 = " ";
										}
									}
									if(keyname.equals("M27")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m27 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m27 = " ";
										}
									}
									if(keyname.equals("M28")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m28 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m28 = " ";
										}
									}
									if(keyname.equals("M29")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m29 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m29 = " ";
										}
									}
									if(keyname.equals("M30")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m30 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m30 = " ";
										}
									}
									if(keyname.equals("M31")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m31 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m31 = " ";
										}
									}
									if(keyname.equals("M32")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m32 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m32 = " ";
										}
									}
									if(keyname.equals("M33")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m33 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m33 = " ";
										}
									}
									if(keyname.equals("M34")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m34 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m34 = " ";
										}
									}
									if(keyname.equals("M35")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m35 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m35 = " ";
										}
									}
									if(keyname.equals("M36")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m36 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m36 = " ";
										}
									}
									if(keyname.equals("M37")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m37 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m37 = " ";
										}
									}
									if(keyname.equals("M38")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m38 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m38 = " ";
										}
									}
									if(keyname.equals("M39")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m39 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m39 = " ";
										}
									}
									if(keyname.equals("M40")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m40 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m40 = " ";
										}
									}
									if(keyname.equals("M41")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m41 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m41 = " ";
										}
									}
									if(keyname.equals("M42")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m42 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m42 = " ";
										}
									}
									if(keyname.equals("M43")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m43 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m43 = " ";
										}
									}
									if(keyname.equals("M44")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m44 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m44 = " ";
										}
									}
									if(keyname.equals("M45")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m45 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m45 = " ";
										}
									}
									if(keyname.equals("M46")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m46 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m46 = " ";
										}
									}
									if(keyname.equals("M47")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m47 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m47 = " ";
										}
									}
									if(keyname.equals("M48")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m48 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m48 = " ";
										}
									}
									if(keyname.equals("M49")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m49 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m49 = " ";
										}
									}
									if(keyname.equals("M50")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m50 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m50 = " ";
										}
									}
									if(keyname.equals("M51")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m51 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m51 = " ";
										}
									}
									if(keyname.equals("M52")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m52 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m52 = " ";
										}
									}
									if(keyname.equals("M53")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m53 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m53 = " ";
										}
									}
									if(keyname.equals("M54")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m54 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m54 = " ";
										}
									}
									if(keyname.equals("M55")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m55 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m55 = " ";
										}
									}
									if(keyname.equals("M56")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m56 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m56 = " ";
										}
									}
									if(keyname.equals("M57")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m57 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m57 = " ";
										}
									}
									if(keyname.equals("M58")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m58 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m58 = " ";
										}
									}
									if(keyname.equals("M59")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m59 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m59 = " ";
										}
									}
									if(keyname.equals("M60")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m60 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m60 = " ";
										}
									}
									if(keyname.equals("M61")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m61 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m61 = " ";
										}
									}
									if(keyname.equals("M62")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m62 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m62 = " ";
										}
									}
									if(keyname.equals("M63")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m63 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m63 = " ";
										}
									}
									if(keyname.equals("M64")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m64 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m64 = " ";
										}
									}
									if(keyname.equals("M65")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m65 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m65 = " ";
										}
									}
									if(keyname.equals("M66")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m66 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m66 = " ";
										}
									}
									if(keyname.equals("M67")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m67 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m67 = " ";
										}
									}
									if(keyname.equals("M68")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m68 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m68 = " ";
										}
									}
									if(keyname.equals("M69")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m69 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m69 = " ";
										}
									}
									if(keyname.equals("M70")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m70 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m70 = " ";
										}
									}
									if(keyname.equals("M71")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m71 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m71 = " ";
										}
									}
									if(keyname.equals("M72")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m72 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m72 = " ";
										}
									}
									if(keyname.equals("M73")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m73 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m73 = " ";
										}
									}
									if(keyname.equals("M74")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m74 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m74 = " ";
										}
									}
									if(keyname.equals("M75")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m75 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m75 = " ";
										}
									}
									if(keyname.equals("M76")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m76 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m76 = " ";
										}
									}
									if(keyname.equals("M77")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m77 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m77 = " ";
										}
									}
									if(keyname.equals("M78")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m78 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m78 = " ";
										}
									}
									if(keyname.equals("M79")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m79 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m79 = " ";
										}
									}
									if(keyname.equals("M80")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m80 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m80 = " ";
										}
									}
									if(keyname.equals("M81")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m81 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m81 = " ";
										}
									}
									if(keyname.equals("M82")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m82 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m82 = " ";
										}
									}
									if(keyname.equals("M83")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m83 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m83 = " ";
										}
									}
									if(keyname.equals("M84")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m84 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m84 = " ";
										}
									}
									if(keyname.equals("M85")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m85 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m85 = " ";
										}
									}
									if(keyname.equals("M86")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m86 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m86 = " ";
										}
									}
									if(keyname.equals("M87")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m87 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m87 = " ";
										}
									}
									if(keyname.equals("M88")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m88 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m88 = " ";
										}
									}
									if(keyname.equals("M89")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m89 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m89 = " ";
										}
									}
									if(keyname.equals("M90")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m90 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m90 = " ";
										}
									}
									if(keyname.equals("M91")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m91 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m91 = " ";
										}
									}
									if(keyname.equals("M92")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m92 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m92 = " ";
										}
									}
									if(keyname.equals("M93")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m93 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m93 = " ";
										}
									}
									if(keyname.equals("M94")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m94 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m94 = " ";
										}
									}
									if(keyname.equals("M95")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m95 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m95 = " ";
										}
									}
									if(keyname.equals("M96")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m96 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m96 = " ";
										}
									}
									if(keyname.equals("M97")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m97 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m97 = " ";
										}
									}
									if(keyname.equals("M98")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m98 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m98 = " ";
										}
									}
									if(keyname.equals("M99")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m99 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m99 = " ";
										}
									}
									if(keyname.equals("M100")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											m100 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											m100 = " ";
										}
									}
									if(keyname.equals("numOfRows")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											numOfRows = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											numOfRows = " ";
										}
									}
									if(keyname.equals("pageNo")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											pageNo_str = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											pageNo_str = " ";
										}
									}
									if(keyname.equals("totalCount")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											totalCount = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											totalCount = " ";
										}
									}

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
			}



	}

}
