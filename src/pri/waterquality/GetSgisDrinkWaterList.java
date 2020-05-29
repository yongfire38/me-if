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

public class GetSgisDrinkWaterList {

	// 수질정보 DB 서비스 - 토양지하수 먹는물 공동시설 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// '연도'를 매개변수로 받음 (필수는 아님)
				// 전체 대상으로 돌리면 커넥션 에러 가능성이 큼... 연도를 필수로 하면 문제는 없지만 가변적으로 받도록 처리(되도록 연도는 받도록)
				if (args.length <= 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("PRI_WaterQualityService_getSgisDrinkWaterList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_02.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

					int pageNo = 0;
					int pageCount = 0;
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}*/

					JSONObject count_obj = JsonParser.parsePriJson_drinkWater_obj(service_url, service_key, String.valueOf(pageNo), args);

					JSONObject count_getSgisDrinkWaterList = (JSONObject) count_obj.get("getSgisDrinkWaterList");

					JSONObject count_header = (JSONObject) count_getSgisDrinkWaterList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						throw new Exception();
					} else {
						int numOfRows = ((Long) count_getSgisDrinkWaterList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getSgisDrinkWaterList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					

					for (int i = 1; i <= pageCount; i++) {
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}*/

						JSONObject obj = JsonParser.parsePriJson_drinkWater_obj(service_url, service_key, String.valueOf(i), args);

						JSONObject getSgisDrinkWaterList = (JSONObject) obj.get("getSgisDrinkWaterList");

						JSONObject header = (JSONObject) getSgisDrinkWaterList.get("header");
						
						String resultCode_col = " "; // 결과코드
						String resultMsg_col = " "; // 결과메시지

						resultCode_col = header.get("code").toString().trim(); // 결과
																						// 코드
						resultMsg_col = header.get("message").toString().trim(); // 결과
																						// 메시지
						
						if((resultCode_col.equals("03"))){
							System.out.println("data not exist!!");
						} else if ((!(resultCode_col.equals("00")) && !(resultCode_col.equals("03")))) {
							System.out.println(
									"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode_col + "::resultMsg::" + resultCode_col);
							throw new Exception();
						} else if (resultCode_col.toString().equals("00")) {
							
							String numOfRows =  " "; // 한 페이지 결과 수
							String pageNo_str =  " "; // 페이지 번호
							String totalCount =  " "; // 전체 결과 수
							
							numOfRows = getSgisDrinkWaterList.get("numOfRows").toString().trim();

							pageNo_str = String.valueOf(i).trim();

							totalCount = getSgisDrinkWaterList.get("totalCount").toString().trim();

							JSONArray items = (JSONArray) getSgisDrinkWaterList.get("item");

							for (int r = 0; r < items.size(); r++) {
								
								String rowno =  " "; // 순번
								String legacyCodeNo =  " "; // 지점번호
								String spotNm =  " "; // 지점명
								String spotStdCode =  " "; // 지점표준코드
								String infoCreatlnsttNm =  " "; // 정보생성기관명칭
								String doNm =  " "; // 시도
								String ctyNm =  " "; // 시군구
								String adres =  " "; // 주소
								String admcode =  " "; // 법정동코드
								String ablAt =  " "; // 폐지여부
								String ablDe =  " "; // 폐지일자
								String dayAvg =  " "; // 1일평균이용자수
								String charge =  " "; // 담당자
								String insDate =  " "; // 설치일자
								String delYn =  " "; // 삭제여부(추정)
								String office =  " "; // 담당자명
								String officeTel =  " "; // 담당자연락처
								String buildingNo =  " "; // 건물번호
								String locJibun =  " "; // 소재지_지번
								String commt =  " "; // 비고
								String yyyy =  " "; // 년
								String period =  " "; // 분기
								String samp_date =  " "; // 채수일자
								String inspCheck =  " "; // 검사여부
								String unlnspDesc =  " "; // 미검사사유
								String insp_date =  " "; // 검사일자
								String acceptYn =  " "; // 적합여부
								String suit =  " "; // 적합
								String unsuit =  " "; // 부적합
								String itemGenbaclow =  " "; // 일반세균저온
								String itemGenbacmid =  " "; // 일반세균중온
								String itemTotbac =  " "; // 총대장균군
								String itemBac =  " "; // 대장균
								String itemFestr =  " "; // 분원성대장균군
								String itemBranfungus =  " "; // 분원성연쇄상구균
								String itemGrgungus =  " "; // 녹농균
								String itemSalmol =  " "; // 살모넬라
								String itemSegel =  " "; // 쉬겔라
								String itemSulfungus =  " "; // 아황산환원혐기성포자형성균
								String itemYersinia =  " "; // 여시니아균
								String itemPb =  " "; // 납
								String itemF =  " "; // 불소
								String itemGas =  " "; // 비소
								String itemSe =  " "; // 셀레늄
								String itemHg =  " "; // 수은
								String itemCn =  " "; // 시안
								String itemCr6 =  " "; // 크롬
								String itemNo3am =  " "; // 암모니아성질소
								String itemNo3n =  " "; // 질산성질소
								String itemCd =  " "; // 카드뮴
								String itemBoron =  " "; // 보론
								String itemBro3 =  " "; // 브론산염
								String itemUran =  " "; // 우라늄
								String itemPhenol =  " "; // 페놀
								String itemDiazn =  " "; // 다이아지논
								String itemParat =  " "; // 파라티온
								String itemPenitro = " "; // 페니트로티온
								String itemCarbaryl =  " "; // 카바릴
								String itemTcet =  " "; // 1.1.1-트리클로로에탄
								String itemTece =  " "; // 테트라클로로에틸렌
								String itemTce =  " "; // 트리클로로에틸렌
								String itemDcm =  " "; // 디클로로메탄
								String itemBenzene =  " "; // 벤젠
								String itemToluene =  " "; // 톨루엔
								String itemEtilben =  " "; // 에틸벤젠
								String itemXylene =  " "; // 자일렌
								String itemDce =  " "; // 1.1디클로로에틸렌
								String itemCcl4 =  " "; // 사염화탄소
								String itemDbcp =  " "; // 1,2-디브로모-3-클로로프로판
								String itemC4h8o2 =  " "; // 1.4-다이옥산
								String itemGradient =  " "; // 경도
								String itemKmn =  " "; // 과망간산칼륨소비량
								String itemSmell =  " "; // 냄새
								String itemColor =  " "; // 색도
								String itemCu =  " "; // 동
								String itemAbs =  " "; // 세제(음이온계면활성제)
								String itemPh =  " "; // 수소이온농도
								String itemZn =  " "; // 아연
								String itemCl =  " "; // 염소이온
								String itemFe =  " "; // 철
								String itemMn =  " "; // 망간
								String itemMuddy =  " "; // 탁도
								String itemSo42 =  " "; // 황산이온
								String itemAl =  " "; // 알루미늄
								String inspRst =  " "; // 부적합항목
								String failDesc =  " "; // 부적합시 조치사항

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									rowno = JsonParser.colWrite_String(rowno, keyname, "ROWNO", item);
									legacyCodeNo = JsonParser.colWrite_String(legacyCodeNo, keyname, "LEGACY_CODE_NO", item);
									spotNm = JsonParser.colWrite_String(spotNm, keyname, "SPOT_NM", item);
									spotStdCode = JsonParser.colWrite_String(spotStdCode, keyname, "SPOT_STD_CODE", item);
									infoCreatlnsttNm = JsonParser.colWrite_String(infoCreatlnsttNm, keyname, "INFO_CREAT_INSTT_NM", item);
									doNm = JsonParser.colWrite_String(doNm, keyname, "DO_NM", item);
									ctyNm = JsonParser.colWrite_String(ctyNm, keyname, "CTY_NM", item);
									adres = JsonParser.colWrite_String(adres, keyname, "ADRES", item);
									admcode = JsonParser.colWrite_String(admcode, keyname, "ADMCODE", item);
									ablAt = JsonParser.colWrite_String(ablAt, keyname, "ABL_AT", item);
									ablDe = JsonParser.colWrite_String(ablDe, keyname, "ABL_DE", item);
									dayAvg = JsonParser.colWrite_String(dayAvg, keyname, "DAY_AVG", item);
									charge = JsonParser.colWrite_String(charge, keyname, "CHARGE", item);
									insDate = JsonParser.colWrite_String(insDate, keyname, "INS_DATE", item);
									office = JsonParser.colWrite_String(office, keyname, "OFFICE", item);
									officeTel = JsonParser.colWrite_String(officeTel, keyname, "OFFICE_TEL", item);
									buildingNo = JsonParser.colWrite_String(buildingNo, keyname, "BUILDING_NO", item);
									locJibun = JsonParser.colWrite_String(locJibun, keyname, "LOC_JIBUN", item);
									commt = JsonParser.colWrite_String(commt, keyname, "COMMT", item);
									yyyy = JsonParser.colWrite_String(yyyy, keyname, "YYYY", item);
									period = JsonParser.colWrite_String(period, keyname, "PERIOD", item);
									samp_date = JsonParser.colWrite_String(samp_date, keyname, "SAMP_DATE", item);
									inspCheck = JsonParser.colWrite_String(inspCheck, keyname, "INSP_CHECK", item);
									unlnspDesc = JsonParser.colWrite_String(unlnspDesc, keyname, "UN_INSP_DESC", item);
									insp_date = JsonParser.colWrite_String(insp_date, keyname, "INSP_DATE", item);
									acceptYn = JsonParser.colWrite_String(acceptYn, keyname, "ACCEPT_YN", item);
									suit = JsonParser.colWrite_String(suit, keyname, "SUIT", item);
									unsuit = JsonParser.colWrite_String(unsuit, keyname, "UNSUIT", item);
									itemGenbaclow = JsonParser.colWrite_String(itemGenbaclow, keyname, "ITEM_GENBACLOW", item);
									itemGenbacmid = JsonParser.colWrite_String(itemGenbacmid, keyname, "ITEM_GENBACMID", item);
									itemTotbac = JsonParser.colWrite_String(itemTotbac, keyname, "ITEM_TOTBAC", item);
									itemBac = JsonParser.colWrite_String(itemBac, keyname, "ITEM_BAC", item);
									itemFestr = JsonParser.colWrite_String(itemFestr, keyname, "ITEM_FESTR", item);
									itemBranfungus = JsonParser.colWrite_String(itemBranfungus, keyname, "ITEM_BRANFUNGUS", item);
									itemGrgungus = JsonParser.colWrite_String(itemGrgungus, keyname, "ITEM_GRGUNGUS", item);
									itemSalmol = JsonParser.colWrite_String(itemSalmol, keyname, "ITEM_SALMOL", item);
									itemSegel = JsonParser.colWrite_String(itemSegel, keyname, "ITEM_SEGEL", item);
									itemSulfungus = JsonParser.colWrite_String(itemSulfungus, keyname, "ITEM_SULFUNGUS", item);
									itemYersinia = JsonParser.colWrite_String(itemYersinia, keyname, "ITEM_YERSINIA", item);
									itemPb = JsonParser.colWrite_String(itemPb, keyname, "ITEM_PB", item);
									itemF = JsonParser.colWrite_String(itemF, keyname, "ITEM_F", item);
									itemGas = JsonParser.colWrite_String(itemGas, keyname, "ITEM_GAS", item);
									itemSe = JsonParser.colWrite_String(itemSe, keyname, "ITEM_SE", item);
									itemHg = JsonParser.colWrite_String(itemHg, keyname, "ITEM_HG", item);
									itemCn = JsonParser.colWrite_String(itemCn, keyname, "ITEM_CN", item);
									itemCr6 = JsonParser.colWrite_String(itemCr6, keyname, "ITEM_CR6", item);
									itemNo3am = JsonParser.colWrite_String(itemNo3am, keyname, "ITEM_NO3AM", item);
									itemNo3n = JsonParser.colWrite_String(itemNo3n, keyname, "ITEM_NO3N", item);
									itemCd = JsonParser.colWrite_String(itemCd, keyname, "ITEM_CD", item);
									itemBoron = JsonParser.colWrite_String(itemBoron, keyname, "ITEM_BORON", item);
									itemBro3 = JsonParser.colWrite_String(itemBro3, keyname, "ITEM_BRO3", item);
									itemUran = JsonParser.colWrite_String(itemUran, keyname, "ITEM_URAN", item);
									itemPhenol = JsonParser.colWrite_String(itemPhenol, keyname, "ITEM_PHENOL", item);
									itemDiazn = JsonParser.colWrite_String(itemDiazn, keyname, "ITEM_DIAZN", item);
									itemParat = JsonParser.colWrite_String(itemParat, keyname, "ITEM_PARAT", item);
									itemPenitro = JsonParser.colWrite_String(itemPenitro, keyname, "ITEM_PENITRO", item);
									itemCarbaryl = JsonParser.colWrite_String(itemCarbaryl, keyname, "ITEM_CARBARYL", item);
									itemTcet = JsonParser.colWrite_String(itemTcet, keyname, "ITEM_TCET", item);
									itemTece = JsonParser.colWrite_String(itemTece, keyname, "ITEM_TECE", item);
									itemTce = JsonParser.colWrite_String(itemTce, keyname, "ITEM_TCE", item);
									itemDcm = JsonParser.colWrite_String(itemDcm, keyname, "ITEM_DCM", item);
									itemBenzene = JsonParser.colWrite_String(itemBenzene, keyname, "ITEM_BENZENE", item);
									itemToluene = JsonParser.colWrite_String(itemToluene, keyname, "ITEM_TOLUENE", item);
									itemEtilben = JsonParser.colWrite_String(itemEtilben, keyname, "ITEM_ETILBEN", item);
									itemXylene = JsonParser.colWrite_String(itemXylene, keyname, "ITEM_XYLENE", item);
									itemDce = JsonParser.colWrite_String(itemDce, keyname, "ITEM_DCE", item);
									itemCcl4 = JsonParser.colWrite_String(itemCcl4, keyname, "ITEM_CCL4", item);
									itemDbcp = JsonParser.colWrite_String(itemDbcp, keyname, "ITEM_DBCP", item);
									itemC4h8o2 = JsonParser.colWrite_String(itemC4h8o2, keyname, "ITEM_C4H8O2", item);
									itemGradient = JsonParser.colWrite_String(itemGradient, keyname, "ITEM_GRADIENT", item);
									itemKmn = JsonParser.colWrite_String(itemKmn, keyname, "ITEM_KMN", item);
									itemSmell = JsonParser.colWrite_String(itemSmell, keyname, "ITEM_SMELL", item);
									itemColor = JsonParser.colWrite_String(itemColor, keyname, "ITEM_COLOR", item);
									itemCu = JsonParser.colWrite_String(itemCu, keyname, "ITEM_CU", item);
									itemAbs = JsonParser.colWrite_String(itemAbs, keyname, "ITEM_ABS", item);
									itemPh = JsonParser.colWrite_String(itemPh, keyname, "ITEM_PH", item);
									itemZn = JsonParser.colWrite_String(itemZn, keyname, "ITEM_ZN", item);
									itemCl = JsonParser.colWrite_String(itemCl, keyname, "ITEM_CL", item);
									itemFe = JsonParser.colWrite_String(itemFe, keyname, "ITEM_FE", item);
									itemMn = JsonParser.colWrite_String(itemMn, keyname, "ITEM_MN", item);
									itemMuddy = JsonParser.colWrite_String(itemMuddy, keyname, "ITEM_MUDDY", item);
									itemSo42 = JsonParser.colWrite_String(itemSo42, keyname, "ITEM_SO42", item);
									itemAl = JsonParser.colWrite_String(itemAl, keyname, "ITEM_AL", item);
									inspRst = JsonParser.colWrite_String(inspRst, keyname, "INSP_RST", item);
									failDesc = JsonParser.colWrite_String(failDesc, keyname, "FAIL_DESC", item);
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
									pw.write(legacyCodeNo);
									pw.write("|^");
									pw.write(spotNm);
									pw.write("|^");
									pw.write(spotStdCode);
									pw.write("|^");
									pw.write(infoCreatlnsttNm);
									pw.write("|^");
									pw.write(doNm);
									pw.write("|^");
									pw.write(ctyNm);
									pw.write("|^");
									pw.write(adres);
									pw.write("|^");
									pw.write(admcode);
									pw.write("|^");
									pw.write(ablAt);
									pw.write("|^");
									pw.write(ablDe);
									pw.write("|^");
									pw.write(dayAvg);
									pw.write("|^");
									pw.write(charge);
									pw.write("|^");
									pw.write(insDate);
									pw.write("|^");
									pw.write(delYn);
									pw.write("|^");
									pw.write(office);
									pw.write("|^");
									pw.write(officeTel);
									pw.write("|^");
									pw.write(buildingNo);
									pw.write("|^");
									pw.write(locJibun);
									pw.write("|^");
									pw.write(commt);
									pw.write("|^");
									pw.write(yyyy);
									pw.write("|^");
									pw.write(period);
									pw.write("|^");
									pw.write(samp_date);
									pw.write("|^");
									pw.write(inspCheck);
									pw.write("|^");
									pw.write(unlnspDesc);
									pw.write("|^");
									pw.write(insp_date);
									pw.write("|^");
									pw.write(acceptYn);
									pw.write("|^");
									pw.write(suit);
									pw.write("|^");
									pw.write(unsuit);
									pw.write("|^");
									pw.write(itemGenbaclow);
									pw.write("|^");
									pw.write(itemGenbacmid);
									pw.write("|^");
									pw.write(itemTotbac);
									pw.write("|^");
									pw.write(itemBac);
									pw.write("|^");
									pw.write(itemFestr);
									pw.write("|^");
									pw.write(itemBranfungus);
									pw.write("|^");
									pw.write(itemGrgungus);
									pw.write("|^");
									pw.write(itemSalmol);
									pw.write("|^");
									pw.write(itemSegel);
									pw.write("|^");
									pw.write(itemSulfungus);
									pw.write("|^");
									pw.write(itemYersinia);
									pw.write("|^");
									pw.write(itemPb);
									pw.write("|^");
									pw.write(itemF);
									pw.write("|^");
									pw.write(itemGas);
									pw.write("|^");
									pw.write(itemSe);
									pw.write("|^");
									pw.write(itemHg);
									pw.write("|^");
									pw.write(itemCn);
									pw.write("|^");
									pw.write(itemCr6);
									pw.write("|^");
									pw.write(itemNo3am);
									pw.write("|^");
									pw.write(itemNo3n);
									pw.write("|^");
									pw.write(itemCd);
									pw.write("|^");
									pw.write(itemBoron);
									pw.write("|^");
									pw.write(itemBro3);
									pw.write("|^");
									pw.write(itemUran);
									pw.write("|^");
									pw.write(itemPhenol);
									pw.write("|^");
									pw.write(itemDiazn);
									pw.write("|^");
									pw.write(itemParat);
									pw.write("|^");
									pw.write(itemPenitro);
									pw.write("|^");
									pw.write(itemCarbaryl);
									pw.write("|^");
									pw.write(itemTcet);
									pw.write("|^");
									pw.write(itemTece);
									pw.write("|^");
									pw.write(itemTce);
									pw.write("|^");
									pw.write(itemDcm);
									pw.write("|^");
									pw.write(itemBenzene);
									pw.write("|^");
									pw.write(itemToluene);
									pw.write("|^");
									pw.write(itemEtilben);
									pw.write("|^");
									pw.write(itemXylene);
									pw.write("|^");
									pw.write(itemDce);
									pw.write("|^");
									pw.write(itemCcl4);
									pw.write("|^");
									pw.write(itemDbcp);
									pw.write("|^");
									pw.write(itemC4h8o2);
									pw.write("|^");
									pw.write(itemGradient);
									pw.write("|^");
									pw.write(itemKmn);
									pw.write("|^");
									pw.write(itemSmell);
									pw.write("|^");
									pw.write(itemColor);
									pw.write("|^");
									pw.write(itemCu);
									pw.write("|^");
									pw.write(itemAbs);
									pw.write("|^");
									pw.write(itemPh);
									pw.write("|^");
									pw.write(itemZn);
									pw.write("|^");
									pw.write(itemCl);
									pw.write("|^");
									pw.write(itemFe);
									pw.write("|^");
									pw.write(itemMn);
									pw.write("|^");
									pw.write(itemMuddy);
									pw.write("|^");
									pw.write(itemSo42);
									pw.write("|^");
									pw.write(itemAl);
									pw.write("|^");
									pw.write(inspRst);
									pw.write("|^");
									pw.write(failDesc);
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

						//Thread.sleep(4500);

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
				System.exit(-1);
			}



	}

}
