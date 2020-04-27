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

public class GetSgisDrinkWaterList {

	// 수질정보 DB 서비스 - 토양지하수 먹는물 공동시설 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("PRI_WaterQualityService_getSgisDrinkWaterList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_02.dat");
					
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

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					JSONObject count_getSgisDrinkWaterList = (JSONObject) count_obj.get("getSgisDrinkWaterList");

					JSONObject count_header = (JSONObject) count_getSgisDrinkWaterList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_getSgisDrinkWaterList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getSgisDrinkWaterList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer resultCode_col = new StringBuffer(" "); // 결과코드
					StringBuffer resultMsg_col = new StringBuffer(" "); // 결과메시지
					StringBuffer rowno = new StringBuffer(" "); // 순번
					StringBuffer legacyCodeNo = new StringBuffer(" "); // 지점번호
					StringBuffer spotNm = new StringBuffer(" "); // 지점명
					StringBuffer spotStdCode = new StringBuffer(" "); // 지점표준코드
					StringBuffer infoCreatlnsttNm = new StringBuffer(" "); // 정보생성기관명칭
					StringBuffer doNm = new StringBuffer(" "); // 시도
					StringBuffer ctyNm = new StringBuffer(" "); // 시군구
					StringBuffer adres = new StringBuffer(" "); // 주소
					StringBuffer admcode = new StringBuffer(" "); // 법정동코드
					StringBuffer ablAt = new StringBuffer(" "); // 폐지여부
					StringBuffer ablDe = new StringBuffer(" "); // 폐지일자
					StringBuffer dayAvg = new StringBuffer(" "); // 1일평균이용자수
					StringBuffer charge = new StringBuffer(" "); // 담당자
					StringBuffer insDate = new StringBuffer(" "); // 설치일자
					StringBuffer delYn = new StringBuffer(" "); // 삭제여부(추정)
					StringBuffer office = new StringBuffer(" "); // 담당자명
					StringBuffer officeTel = new StringBuffer(" "); // 담당자연락처
					StringBuffer buildingNo = new StringBuffer(" "); // 건물번호
					StringBuffer locJibun = new StringBuffer(" "); // 소재지_지번
					StringBuffer commt = new StringBuffer(" "); // 비고
					StringBuffer yyyy = new StringBuffer(" "); // 년
					StringBuffer period = new StringBuffer(" "); // 분기
					StringBuffer samp_date = new StringBuffer(" "); // 채수일자
					StringBuffer inspCheck = new StringBuffer(" "); // 검사여부
					StringBuffer unlnspDesc = new StringBuffer(" "); // 미검사사유
					StringBuffer insp_date = new StringBuffer(" "); // 검사일자
					StringBuffer acceptYn = new StringBuffer(" "); // 적합여부
					StringBuffer suit = new StringBuffer(" "); // 적합
					StringBuffer unsuit = new StringBuffer(" "); // 부적합
					StringBuffer itemGenbaclow = new StringBuffer(" "); // 일반세균저온
					StringBuffer itemGenbacmid = new StringBuffer(" "); // 일반세균중온
					StringBuffer itemTotbac = new StringBuffer(" "); // 총대장균군
					StringBuffer itemBac = new StringBuffer(" "); // 대장균
					StringBuffer itemFestr = new StringBuffer(" "); // 분원성대장균군
					StringBuffer itemBranfungus = new StringBuffer(" "); // 분원성연쇄상구균
					StringBuffer itemGrgungus = new StringBuffer(" "); // 녹농균
					StringBuffer itemSalmol = new StringBuffer(" "); // 살모넬라
					StringBuffer itemSegel = new StringBuffer(" "); // 쉬겔라
					StringBuffer itemSulfungus = new StringBuffer(" "); // 아황산환원혐기성포자형성균
					StringBuffer itemYersinia = new StringBuffer(" "); // 여시니아균
					StringBuffer itemPb = new StringBuffer(" "); // 납
					StringBuffer itemF = new StringBuffer(" "); // 불소
					StringBuffer itemGas = new StringBuffer(" "); // 비소
					StringBuffer itemSe = new StringBuffer(" "); // 셀레늄
					StringBuffer itemHg = new StringBuffer(" "); // 수은
					StringBuffer itemCn = new StringBuffer(" "); // 시안
					StringBuffer itemCr6 = new StringBuffer(" "); // 크롬
					StringBuffer itemNo3am = new StringBuffer(" "); // 암모니아성질소
					StringBuffer itemNo3n = new StringBuffer(" "); // 질산성질소
					StringBuffer itemCd = new StringBuffer(" "); // 카드뮴
					StringBuffer itemBoron = new StringBuffer(" "); // 보론
					StringBuffer itemBro3 = new StringBuffer(" "); // 브론산염
					StringBuffer itemUran = new StringBuffer(" "); // 우라늄
					StringBuffer itemPhenol = new StringBuffer(" "); // 페놀
					StringBuffer itemDiazn = new StringBuffer(" "); // 다이아지논
					StringBuffer itemParat = new StringBuffer(" "); // 파라티온
					StringBuffer itemPenitro = new StringBuffer(" "); // 페니트로티온
					StringBuffer itemCarbaryl = new StringBuffer(" "); // 카바릴
					StringBuffer itemTcet = new StringBuffer(" "); // 1.1.1-트리클로로에탄
					StringBuffer itemTece = new StringBuffer(" "); // 테트라클로로에틸렌
					StringBuffer itemTce = new StringBuffer(" "); // 트리클로로에틸렌
					StringBuffer itemDcm = new StringBuffer(" "); // 디클로로메탄
					StringBuffer itemBenzene = new StringBuffer(" "); // 벤젠
					StringBuffer itemToluene = new StringBuffer(" "); // 톨루엔
					StringBuffer itemEtilben = new StringBuffer(" "); // 에틸벤젠
					StringBuffer itemXylene = new StringBuffer(" "); // 자일렌
					StringBuffer itemDce = new StringBuffer(" "); // 1.1디클로로에틸렌
					StringBuffer itemCcl4 = new StringBuffer(" "); // 사염화탄소
					StringBuffer itemDbcp = new StringBuffer(" "); // 1,2-디브로모-3-클로로프로판
					StringBuffer itemC4h8o2 = new StringBuffer(" "); // 1.4-다이옥산
					StringBuffer itemGradient = new StringBuffer(" "); // 경도
					StringBuffer itemKmn = new StringBuffer(" "); // 과망간산칼륨소비량
					StringBuffer itemSmell = new StringBuffer(" "); // 냄새
					StringBuffer itemColor = new StringBuffer(" "); // 색도
					StringBuffer itemCu = new StringBuffer(" "); // 동
					StringBuffer itemAbs = new StringBuffer(" "); // 세제(음이온계면활성제)
					StringBuffer itemPh = new StringBuffer(" "); // 수소이온농도
					StringBuffer itemZn = new StringBuffer(" "); // 아연
					StringBuffer itemCl = new StringBuffer(" "); // 염소이온
					StringBuffer itemFe = new StringBuffer(" "); // 철
					StringBuffer itemMn = new StringBuffer(" "); // 망간
					StringBuffer itemMuddy = new StringBuffer(" "); // 탁도
					StringBuffer itemSo42 = new StringBuffer(" "); // 황산이온
					StringBuffer itemAl = new StringBuffer(" "); // 알루미늄
					StringBuffer inspRst = new StringBuffer(" "); // 부적합항목
					StringBuffer failDesc = new StringBuffer(" "); // 부적합시 조치사항
					StringBuffer numOfRows = new StringBuffer(" "); // 한 페이지 결과
																	// 수
					StringBuffer pageNo_str = new StringBuffer(" "); // 페이지 번호
					StringBuffer totalCount = new StringBuffer(" "); // 전체 결과 수

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getSgisDrinkWaterList = (JSONObject) obj.get("getSgisDrinkWaterList");

						JSONObject header = (JSONObject) getSgisDrinkWaterList.get("header");

						resultCode_col.setLength(0);
						resultCode_col.append(header.get("code").toString().trim()); // 결과
																						// 코드
						resultMsg_col.setLength(0);
						resultMsg_col.append(header.get("message").toString().trim()); // 결과
																						// 메시지
						
						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {
							
							numOfRows.setLength(0);
							numOfRows.append(getSgisDrinkWaterList.get("numOfRows").toString().trim());

							pageNo_str.setLength(0);
							pageNo_str.append(String.valueOf(i).trim());

							totalCount.setLength(0);
							totalCount.append(getSgisDrinkWaterList.get("totalCount").toString().trim());

							JSONArray items = (JSONArray) getSgisDrinkWaterList.get("item");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(rowno, keyname, "ROWNO", item);
									JsonParser.colWrite(legacyCodeNo, keyname, "LEGACY_CODE_NO", item);
									JsonParser.colWrite(spotNm, keyname, "SPOT_NM", item);
									JsonParser.colWrite(spotStdCode, keyname, "SPOT_STD_CODE", item);
									JsonParser.colWrite(infoCreatlnsttNm, keyname, "INFO_CREAT_INSTT_NM", item);
									JsonParser.colWrite(doNm, keyname, "DO_NM", item);
									JsonParser.colWrite(ctyNm, keyname, "CTY_NM", item);
									JsonParser.colWrite(adres, keyname, "ADRES", item);
									JsonParser.colWrite(admcode, keyname, "ADMCODE", item);
									JsonParser.colWrite(ablAt, keyname, "ABL_AT", item);
									JsonParser.colWrite(ablDe, keyname, "ABL_DE", item);
									JsonParser.colWrite(dayAvg, keyname, "DAY_AVG", item);
									JsonParser.colWrite(charge, keyname, "CHARGE", item);
									JsonParser.colWrite_waterMeasuring(insDate, keyname, "INS_DATE", item);
									JsonParser.colWrite(delYn, keyname, "DEL_YN", item);
									JsonParser.colWrite(office, keyname, "OFFICE", item);
									JsonParser.colWrite(officeTel, keyname, "OFFICE_TEL", item);
									JsonParser.colWrite(buildingNo, keyname, "BUILDING_NO", item);
									JsonParser.colWrite(locJibun, keyname, "LOC_JIBUN", item);
									JsonParser.colWrite(commt, keyname, "COMMT", item);
									JsonParser.colWrite(yyyy, keyname, "YYYY", item);
									JsonParser.colWrite(period, keyname, "PERIOD", item);
									JsonParser.colWrite(samp_date, keyname, "SAMP_DATE", item);
									JsonParser.colWrite(inspCheck, keyname, "INSP_CHECK", item);
									JsonParser.colWrite(unlnspDesc, keyname, "UN_INSP_DESC", item);
									JsonParser.colWrite(insp_date, keyname, "INSP_DATE", item);
									JsonParser.colWrite(acceptYn, keyname, "ACCEPT_YN", item);
									JsonParser.colWrite(suit, keyname, "SUIT", item);
									JsonParser.colWrite(unsuit, keyname, "UNSUIT", item);
									JsonParser.colWrite(itemGenbaclow, keyname, "ITEM_GENBACLOW", item);
									JsonParser.colWrite(itemGenbacmid, keyname, "ITEM_GENBACMID", item);
									JsonParser.colWrite(itemTotbac, keyname, "ITEM_TOTBAC", item);
									JsonParser.colWrite(itemBac, keyname, "ITEM_BAC", item);
									JsonParser.colWrite(itemFestr, keyname, "ITEM_FESTR", item);
									JsonParser.colWrite(itemBranfungus, keyname, "ITEM_BRANFUNGUS", item);
									JsonParser.colWrite(itemGrgungus, keyname, "ITEM_GRGUNGUS", item);
									JsonParser.colWrite(itemSalmol, keyname, "ITEM_SALMOL", item);
									JsonParser.colWrite(itemSegel, keyname, "ITEM_SEGEL", item);
									JsonParser.colWrite(itemSulfungus, keyname, "ITEM_SULFUNGUS", item);
									JsonParser.colWrite(itemYersinia, keyname, "ITEM_YERSINIA", item);
									JsonParser.colWrite(itemPb, keyname, "ITEM_PB", item);
									JsonParser.colWrite(itemF, keyname, "ITEM_F", item);
									JsonParser.colWrite(itemGas, keyname, "ITEM_GAS", item);
									JsonParser.colWrite(itemSe, keyname, "ITEM_SE", item);
									JsonParser.colWrite(itemHg, keyname, "ITEM_HG", item);
									JsonParser.colWrite(itemCn, keyname, "ITEM_CN", item);
									JsonParser.colWrite(itemCr6, keyname, "ITEM_CR6", item);
									JsonParser.colWrite(itemNo3am, keyname, "ITEM_NO3AM", item);
									JsonParser.colWrite(itemNo3n, keyname, "ITEM_NO3N", item);
									JsonParser.colWrite(itemCd, keyname, "ITEM_CD", item);
									JsonParser.colWrite(itemBoron, keyname, "ITEM_BORON", item);
									JsonParser.colWrite(itemBro3, keyname, "ITEM_BRO3", item);
									JsonParser.colWrite(itemUran, keyname, "ITEM_URAN", item);
									JsonParser.colWrite(itemPhenol, keyname, "ITEM_PHENOL", item);
									JsonParser.colWrite(itemDiazn, keyname, "ITEM_DIAZN", item);
									JsonParser.colWrite(itemParat, keyname, "ITEM_PARAT", item);
									JsonParser.colWrite(itemPenitro, keyname, "ITEM_PENITRO", item);
									JsonParser.colWrite(itemCarbaryl, keyname, "ITEM_CARBARYL", item);
									JsonParser.colWrite(itemTcet, keyname, "ITEM_TCET", item);
									JsonParser.colWrite(itemTece, keyname, "ITEM_TECE", item);
									JsonParser.colWrite(itemTce, keyname, "ITEM_TCE", item);
									JsonParser.colWrite(itemDcm, keyname, "ITEM_DCM", item);
									JsonParser.colWrite(itemBenzene, keyname, "ITEM_BENZENE", item);
									JsonParser.colWrite(itemToluene, keyname, "ITEM_TOLUENE", item);
									JsonParser.colWrite(itemEtilben, keyname, "ITEM_ETILBEN", item);
									JsonParser.colWrite(itemXylene, keyname, "ITEM_XYLENE", item);
									JsonParser.colWrite(itemDce, keyname, "ITEM_DCE", item);
									JsonParser.colWrite(itemCcl4, keyname, "ITEM_CCL4", item);
									JsonParser.colWrite(itemDbcp, keyname, "ITEM_DBCP", item);
									JsonParser.colWrite(itemC4h8o2, keyname, "ITEM_C4H8O2", item);
									JsonParser.colWrite(itemGradient, keyname, "ITEM_GRADIENT", item);
									JsonParser.colWrite(itemKmn, keyname, "ITEM_KMN", item);
									JsonParser.colWrite(itemSmell, keyname, "ITEM_SMELL", item);
									JsonParser.colWrite(itemColor, keyname, "ITEM_COLOR", item);
									JsonParser.colWrite(itemCu, keyname, "ITEM_CU", item);
									JsonParser.colWrite(itemAbs, keyname, "ITEM_ABS", item);
									JsonParser.colWrite(itemPh, keyname, "ITEM_PH", item);
									JsonParser.colWrite(itemZn, keyname, "ITEM_ZN", item);
									JsonParser.colWrite(itemCl, keyname, "ITEM_CL", item);
									JsonParser.colWrite(itemFe, keyname, "ITEM_FE", item);
									JsonParser.colWrite(itemMn, keyname, "ITEM_MN", item);
									JsonParser.colWrite(itemMuddy, keyname, "ITEM_MUDDY", item);
									JsonParser.colWrite(itemSo42, keyname, "ITEM_SO42", item);
									JsonParser.colWrite(itemAl, keyname, "ITEM_AL", item);
									JsonParser.colWrite(inspRst, keyname, "INSP_RST", item);
									JsonParser.colWrite(failDesc, keyname, "FAIL_DESC", item);
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
								resultSb.append(legacyCodeNo);
								resultSb.append("|^");
								resultSb.append(spotNm);
								resultSb.append("|^");
								resultSb.append(spotStdCode);
								resultSb.append("|^");
								resultSb.append(infoCreatlnsttNm);
								resultSb.append("|^");
								resultSb.append(doNm);
								resultSb.append("|^");
								resultSb.append(ctyNm);
								resultSb.append("|^");
								resultSb.append(adres);
								resultSb.append("|^");
								resultSb.append(admcode);
								resultSb.append("|^");
								resultSb.append(ablAt);
								resultSb.append("|^");
								resultSb.append(ablDe);
								resultSb.append("|^");
								resultSb.append(dayAvg);
								resultSb.append("|^");
								resultSb.append(charge);
								resultSb.append("|^");
								resultSb.append(insDate);
								resultSb.append("|^");
								resultSb.append(delYn);
								resultSb.append("|^");
								resultSb.append(office);
								resultSb.append("|^");
								resultSb.append(officeTel);
								resultSb.append("|^");
								resultSb.append(buildingNo);
								resultSb.append("|^");
								resultSb.append(locJibun);
								resultSb.append("|^");
								resultSb.append(commt);
								resultSb.append("|^");
								resultSb.append(yyyy);
								resultSb.append("|^");
								resultSb.append(period);
								resultSb.append("|^");
								resultSb.append(samp_date);
								resultSb.append("|^");
								resultSb.append(inspCheck);
								resultSb.append("|^");
								resultSb.append(unlnspDesc);
								resultSb.append("|^");
								resultSb.append(insp_date);
								resultSb.append("|^");
								resultSb.append(acceptYn);
								resultSb.append("|^");
								resultSb.append(suit);
								resultSb.append("|^");
								resultSb.append(unsuit);
								resultSb.append("|^");
								resultSb.append(itemGenbaclow);
								resultSb.append("|^");
								resultSb.append(itemGenbacmid);
								resultSb.append("|^");
								resultSb.append(itemTotbac);
								resultSb.append("|^");
								resultSb.append(itemBac);
								resultSb.append("|^");
								resultSb.append(itemFestr);
								resultSb.append("|^");
								resultSb.append(itemBranfungus);
								resultSb.append("|^");
								resultSb.append(itemGrgungus);
								resultSb.append("|^");
								resultSb.append(itemSalmol);
								resultSb.append("|^");
								resultSb.append(itemSegel);
								resultSb.append("|^");
								resultSb.append(itemSulfungus);
								resultSb.append("|^");
								resultSb.append(itemYersinia);
								resultSb.append("|^");
								resultSb.append(itemPb);
								resultSb.append("|^");
								resultSb.append(itemF);
								resultSb.append("|^");
								resultSb.append(itemGas);
								resultSb.append("|^");
								resultSb.append(itemSe);
								resultSb.append("|^");
								resultSb.append(itemHg);
								resultSb.append("|^");
								resultSb.append(itemCn);
								resultSb.append("|^");
								resultSb.append(itemCr6);
								resultSb.append("|^");
								resultSb.append(itemNo3am);
								resultSb.append("|^");
								resultSb.append(itemNo3n);
								resultSb.append("|^");
								resultSb.append(itemCd);
								resultSb.append("|^");
								resultSb.append(itemBoron);
								resultSb.append("|^");
								resultSb.append(itemBro3);
								resultSb.append("|^");
								resultSb.append(itemUran);
								resultSb.append("|^");
								resultSb.append(itemPhenol);
								resultSb.append("|^");
								resultSb.append(itemDiazn);
								resultSb.append("|^");
								resultSb.append(itemParat);
								resultSb.append("|^");
								resultSb.append(itemPenitro);
								resultSb.append("|^");
								resultSb.append(itemCarbaryl);
								resultSb.append("|^");
								resultSb.append(itemTcet);
								resultSb.append("|^");
								resultSb.append(itemTece);
								resultSb.append("|^");
								resultSb.append(itemTce);
								resultSb.append("|^");
								resultSb.append(itemDcm);
								resultSb.append("|^");
								resultSb.append(itemBenzene);
								resultSb.append("|^");
								resultSb.append(itemToluene);
								resultSb.append("|^");
								resultSb.append(itemEtilben);
								resultSb.append("|^");
								resultSb.append(itemXylene);
								resultSb.append("|^");
								resultSb.append(itemDce);
								resultSb.append("|^");
								resultSb.append(itemCcl4);
								resultSb.append("|^");
								resultSb.append(itemDbcp);
								resultSb.append("|^");
								resultSb.append(itemC4h8o2);
								resultSb.append("|^");
								resultSb.append(itemGradient);
								resultSb.append("|^");
								resultSb.append(itemKmn);
								resultSb.append("|^");
								resultSb.append(itemSmell);
								resultSb.append("|^");
								resultSb.append(itemColor);
								resultSb.append("|^");
								resultSb.append(itemCu);
								resultSb.append("|^");
								resultSb.append(itemAbs);
								resultSb.append("|^");
								resultSb.append(itemPh);
								resultSb.append("|^");
								resultSb.append(itemZn);
								resultSb.append("|^");
								resultSb.append(itemCl);
								resultSb.append("|^");
								resultSb.append(itemFe);
								resultSb.append("|^");
								resultSb.append(itemMn);
								resultSb.append("|^");
								resultSb.append(itemMuddy);
								resultSb.append("|^");
								resultSb.append(itemSo42);
								resultSb.append("|^");
								resultSb.append(itemAl);
								resultSb.append("|^");
								resultSb.append(inspRst);
								resultSb.append("|^");
								resultSb.append(failDesc);
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

						Thread.sleep(4500);

					}

					// step 4. 파일에 쓰기
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));

						pw.write(resultSb.toString());
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_02.dat", "PRI");

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
