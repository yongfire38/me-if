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
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parsePriJson_drinkWater(service_url, service_key, String.valueOf(pageNo), args);
					
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

					

					for (int i = 1; i <= pageCount; i++) {

						json = JsonParser.parsePriJson_drinkWater(service_url, service_key, String.valueOf(i), args);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"getSgisDrinkWaterList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getSgisDrinkWaterList = (JSONObject) obj.get("getSgisDrinkWaterList");

						JSONObject header = (JSONObject) getSgisDrinkWaterList.get("header");
						
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
							String numOfRows =  " "; // 한 페이지 결과 수
							String pageNo_str =  " "; // 페이지 번호
							String totalCount =  " "; // 전체 결과 수
							
							numOfRows = getSgisDrinkWaterList.get("numOfRows").toString().trim();

							pageNo_str = String.valueOf(i).trim();

							totalCount = getSgisDrinkWaterList.get("totalCount").toString().trim();

							JSONArray items = (JSONArray) getSgisDrinkWaterList.get("item");

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
									if(keyname.equals("LEGACY_CODE_NO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											legacyCodeNo = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											legacyCodeNo = " ";
										}
									}
									if(keyname.equals("SPOT_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											spotNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											spotNm = " ";
										}
									}
									if(keyname.equals("SPOT_STD_CODE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											spotStdCode = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											spotStdCode = " ";
										}
									}
									if(keyname.equals("INFO_CREAT_INSTT_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											infoCreatlnsttNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											infoCreatlnsttNm = " ";
										}
									}
									if(keyname.equals("DO_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											doNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											doNm = " ";
										}
									}
									if(keyname.equals("CTY_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											ctyNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ctyNm = " ";
										}
									}
									if(keyname.equals("ADRES")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											adres = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											adres = " ";
										}
									}
									if(keyname.equals("ADMCODE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											admcode = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											admcode = " ";
										}
									}
									if(keyname.equals("ABL_AT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											ablAt = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ablAt = " ";
										}
									}
									if(keyname.equals("ABL_DE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											ablDe = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ablDe = " ";
										}
									}
									if(keyname.equals("DAY_AVG")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											dayAvg = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											dayAvg = " ";
										}
									}
									if(keyname.equals("CHARGE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											charge = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											charge = " ";
										}
									}
									if(keyname.equals("INS_DATE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											insDate = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											insDate = " ";
										}
									}
									if(keyname.equals("OFFICE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											office = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											office = " ";
										}
									}
									if(keyname.equals("OFFICE_TEL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											officeTel = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											officeTel = " ";
										}
									}
									if(keyname.equals("BUILDING_NO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											buildingNo = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											buildingNo = " ";
										}
									}
									if(keyname.equals("LOC_JIBUN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											locJibun = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											locJibun = " ";
										}
									}
									if(keyname.equals("COMMT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											commt = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											commt = " ";
										}
									}
									if(keyname.equals("YYYY")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											yyyy = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											yyyy = " ";
										}
									}
									if(keyname.equals("PERIOD")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											period = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											period = " ";
										}
									}
									if(keyname.equals("SAMP_DATE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											samp_date = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											samp_date = " ";
										}
									}
									if(keyname.equals("INSP_CHECK")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											inspCheck = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											inspCheck = " ";
										}
									}
									if(keyname.equals("UN_INSP_DESC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											unlnspDesc = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											unlnspDesc = " ";
										}
									}
									if(keyname.equals("INSP_DATE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											insp_date = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											insp_date = " ";
										}
									}
									if(keyname.equals("ACCEPT_YN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											acceptYn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											acceptYn = " ";
										}
									}
									if(keyname.equals("SUIT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											suit = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											suit = " ";
										}
									}
									if(keyname.equals("UNSUIT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											unsuit = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											unsuit = " ";
										}
									}
									if(keyname.equals("ITEM_GENBACLOW")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemGenbaclow = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemGenbaclow = " ";
										}
									}
									if(keyname.equals("ITEM_GENBACMID")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemGenbacmid = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemGenbacmid = " ";
										}
									}
									if(keyname.equals("ITEM_TOTBAC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTotbac = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTotbac = " ";
										}
									}
									if(keyname.equals("ITEM_BAC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBac = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBac = " ";
										}
									}
									if(keyname.equals("ITEM_FESTR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemFestr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemFestr = " ";
										}
									}
									if(keyname.equals("ITEM_BRANFUNGUS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBranfungus = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBranfungus = " ";
										}
									}
									if(keyname.equals("ITEM_GRGUNGUS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemGrgungus = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemGrgungus = " ";
										}
									}
									if(keyname.equals("ITEM_SALMOL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSalmol = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSalmol = " ";
										}
									}
									if(keyname.equals("ITEM_SEGEL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSegel = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSegel = " ";
										}
									}
									if(keyname.equals("ITEM_SULFUNGUS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSulfungus = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSulfungus = " ";
										}
									}
									if(keyname.equals("ITEM_YERSINIA")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemYersinia = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemYersinia = " ";
										}
									}
									if(keyname.equals("ITEM_PB")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPb = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPb = " ";
										}
									}
									if(keyname.equals("ITEM_F")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemF = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemF = " ";
										}
									}
									if(keyname.equals("ITEM_GAS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemGas = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemGas = " ";
										}
									}
									if(keyname.equals("ITEM_SE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSe = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSe = " ";
										}
									}
									if(keyname.equals("ITEM_HG")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemHg = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemHg = " ";
										}
									}
									if(keyname.equals("ITEM_CN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCn = " ";
										}
									}
									if(keyname.equals("ITEM_CR6")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCr6 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCr6 = " ";
										}
									}
									if(keyname.equals("ITEM_NO3AM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemNo3am = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemNo3am = " ";
										}
									}
									if(keyname.equals("ITEM_NO3N")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemNo3n = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemNo3n = " ";
										}
									}
									if(keyname.equals("ITEM_CD")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCd = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCd = " ";
										}
									}
									if(keyname.equals("ITEM_BORON")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBoron = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBoron = " ";
										}
									}
									if(keyname.equals("ITEM_BRO3")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBro3 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBro3 = " ";
										}
									}
									if(keyname.equals("ITEM_URAN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemUran = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemUran = " ";
										}
									}
									if(keyname.equals("ITEM_PHENOL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPhenol = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPhenol = " ";
										}
									}
									if(keyname.equals("ITEM_DIAZN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDiazn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDiazn = " ";
										}
									}
									if(keyname.equals("ITEM_PARAT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemParat = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemParat = " ";
										}
									}
									if(keyname.equals("ITEM_PENITRO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPenitro = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPenitro = " ";
										}
									}
									if(keyname.equals("ITEM_CARBARYL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCarbaryl = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCarbaryl = " ";
										}
									}
									if(keyname.equals("ITEM_TCET")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTcet = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTcet = " ";
										}
									}
									if(keyname.equals("ITEM_TECE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTece = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTece = " ";
										}
									}
									if(keyname.equals("ITEM_TCE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTce = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTce = " ";
										}
									}
									if(keyname.equals("ITEM_DCM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDcm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDcm = " ";
										}
									}
									if(keyname.equals("ITEM_BENZENE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBenzene = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBenzene = " ";
										}
									}
									if(keyname.equals("ITEM_TOLUENE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemToluene = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemToluene = " ";
										}
									}
									if(keyname.equals("ITEM_ETILBEN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemEtilben = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemEtilben = " ";
										}
									}
									if(keyname.equals("ITEM_XYLENE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemXylene = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemXylene = " ";
										}
									}
									if(keyname.equals("ITEM_DCE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDce = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDce = " ";
										}
									}
									if(keyname.equals("ITEM_CCL4")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCcl4 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCcl4 = " ";
										}
									}
									if(keyname.equals("ITEM_DBCP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDbcp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDbcp = " ";
										}
									}
									if(keyname.equals("ITEM_C4H8O2")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemC4h8o2 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemC4h8o2 = " ";
										}
									}
									if(keyname.equals("ITEM_GRADIENT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemGradient = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemGradient = " ";
										}
									}
									if(keyname.equals("ITEM_KMN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemKmn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemKmn = " ";
										}
									}
									if(keyname.equals("ITEM_SMELL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSmell = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSmell = " ";
										}
									}
									if(keyname.equals("ITEM_COLOR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemColor = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemColor = " ";
										}
									}
									if(keyname.equals("ITEM_CU")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCu = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCu = " ";
										}
									}
									if(keyname.equals("ITEM_ABS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemAbs = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemAbs = " ";
										}
									}
									if(keyname.equals("ITEM_PH")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPh = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPh = " ";
										}
									}
									if(keyname.equals("ITEM_ZN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemZn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemZn = " ";
										}
									}
									if(keyname.equals("ITEM_CL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCl = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCl = " ";
										}
									}
									if(keyname.equals("ITEM_FE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemFe = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemFe = " ";
										}
									}
									if(keyname.equals("ITEM_MN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemMn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemMn = " ";
										}
									}
									if(keyname.equals("ITEM_MUDDY")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemMuddy = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemMuddy = " ";
										}
									}
									if(keyname.equals("ITEM_SO42")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSo42 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSo42 = " ";
										}
									}
									if(keyname.equals("ITEM_AL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemAl = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemAl = " ";
										}
									}
									if(keyname.equals("INSP_RST")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											inspRst = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											inspRst = " ";
										}
									}
									if(keyname.equals("FAIL_DESC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											failDesc = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											failDesc = " ";
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
			}



	}

}
