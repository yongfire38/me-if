package pri.waterquality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class GetWaterMeasuringList {

	// 수질정보 DB 서비스 - 물환경 수질측정망 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// '측정소 코드', '측정년도', '측정월'을 파라미터로 받음 (년도와 월은 필수는 아님)
				// 측정소 코드는 필수
				if (args.length > 0 && args.length <= 3) {
					
					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("PRI_WaterQualityService_getWaterMeasuringList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_01.dat");

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
					json = JsonParser.parsePriJson_waterMeasuring(service_url, service_key, String.valueOf(pageNo), args);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"getWaterMeasuringList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					JSONObject count_getWaterMeasuringList = (JSONObject) count_obj.get("getWaterMeasuringList");

					JSONObject count_header = (JSONObject) count_getWaterMeasuringList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_getWaterMeasuringList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getWaterMeasuringList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 3. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					

					for (int i = 1; i <= pageCount; i++) {

						// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
						json = JsonParser.parsePriJson_waterMeasuring(service_url, service_key, String.valueOf(i), args);
						
						System.out.println("json:::"+json);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"getWaterMeasuringList\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getWaterMeasuringList = (JSONObject) obj.get("getWaterMeasuringList");

						JSONObject header = (JSONObject) getWaterMeasuringList.get("header");

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
							String ptNo = " "; // 조사지점번호
							String ptNm = " "; // 조사지점명
							String addr = " "; // 조사지점 주소
							String orgNm = " "; // 조사기관명
							String wmyr = " "; // 측정년도
							String wmod = " "; // 측정월
							String wmwk = " "; // 검사회차
							String wmdep = " "; // 수심 (단위 : m)
							String lonDgr = " "; // 경도-도
							String lonMin = " "; // 경도-분
							String lonSec = " "; // 경도-초
							String latDgr = " "; // 위도-도
							String latMin = " "; // 위도-분
							String latSec = " "; // 위도-초
							String wmcymd = " "; // 검사일자
							String itemLvl = " "; // 측정값(수위) (단위
																			// : m)
							String itemAmnt = " "; // 측정값(유량)
																			// (단위 :
																			// ㎥/sec)
							String itemTemp = " "; // 측정값(수온)
																			// (단위 : ℃)
							String itemPh = " "; // 측정값(수소이온농도(pH))
							String itemDoc = " "; // 측정값(용존산소(DO))
																			// (단위 :
																			// ㎎/L)
							String itemBod = " "; // 측정값(생물화학적산소요구량(BOD))
																			// (단위 :
																			// ㎎/L)
							String itemCod = " "; // 측정값(화학적산소요구량(COD))
																			// (단위 :
																			// ㎎/L)
							String itemSs = " "; // 측정값(부유물질(SS))
																			// (단위 :
																			// ㎎/L)
							String itemTcoli = " "; // 측정값(총대장균군)(단위
																			// :
																			// 총대장균군수/100㎖)
							String itemTn = " "; // 측정값(총질소(T-N))
																			// (단위 :
																			// ㎎/L)
							String itemTp = " "; // 측정값(총인(T-P))
																			// (단위 :
																			// ㎎/L)
							String itemCd = " "; // 측정값(카드뮴(Cd))
																			// (단위 :
																			// ㎎/L)
							String itemCn = " "; // 측정값(시안(CN))
																			// (단위 :
																			// ㎎/L)
							String itemPb = " "; // 측정값(납(Pb))
																			// (단위 :
																			// ㎎/L)
							String itemCr6 = " "; // 측정값(6가크롬(Cr6+))
																			// (단위 :
																			// ㎎/L)
							String itemAs = " "; // 측정값(비소(As))
																			// (단위 :
																			// ㎎/L)
							String itemHg = " "; // 측정값(수은(Hg))
																			// (단위 :
																			// ㎎/L)
							String itemCu = " "; // 측정값(구리(Cu))
																			// (단위 :
																			// ㎎/L)
							String itemAbs = " "; // 측정값(음이온계면활성제(ABS))
																			// (단위 :
																			// ㎎/L)
							String itemPcb = " "; // 측정값(폴리클로리네이티드비페닐(PCB))
																			// (단위 :
																			// ㎎/L)
							String itemOp = " "; // 측정값(유기인) (단위
																			// : ㎎/L)
							String itemMn = " "; // 측정값(용해성
																			// 망간(Mn))
																			// (단위 :
																			// ㎎/L)
							String itemTrans = " "; // 측정값(투명도)
																			// (단위 :
																			// ㎎/L)
							String itemCloa = " "; // 측정값(클로로필-a(Chlorophyll-a))
																			// (단위 :
																			// ㎎/L)
							String itemCl = " "; // 측정값(염소이온(Cl-))
																			// (단위 :
																			// ㎎/L)
							String itemZn = " "; // 측정값(아연(Zn))
																			// (단위 :
																			// ㎎/L)
							String itemCr = " "; // 측정값(크롬(Cr))
																			// (단위 :
																			// ㎎/L)
							String itemFe = " "; // 측정값(용해성
																			// 철(Fe))
																			// (단위 :
																			// ㎎/L)
							String itemPhenol = " "; // 측정값(페놀류(phenols))
																				// (단위 :
																				// ㎎/L)
							String itemNhex = " "; // 측정값(노말헥산추출물질)
																			// (단위 :
																			// ㎎/L)
							String itemEc = " "; // 측정값(전기전도도(EC))
																			// (단위 :
																			// µS/㎝)
							String itemTce = " "; // 측정값(트리클로로에틸렌(TCE))
																			// (단위 :
																			// ㎎/L)
							String itemPce = " "; // 측정값(테트라클로로에틸렌(PCE))
																			// (단위 :
																			// ㎎/L)
							String itemNo3n = " "; // 측정값(질산성질소(NO3-N))
																			// (단위 :
																			// ㎎/L)
							String itemNh3n = " "; // 측정값(암모니아성
																			// 질소(NH3-N))
																			// (단위 :
																			// ㎎/L)
							String itemEcoli = " "; // 측정값(분원성대장균군)
																			// (단위 :
																			// 분원성대장균군수/100㎖)
							String itemPop = " "; // 측정값(인산염
																			// 인(PO4-P))
																			// (단위 :
																			// ㎎/L)
							String itemDtn = " "; // 측정값(용존총질소(DTN))
																			// (단위 :
																			// ㎎/L)
							String itemDtp = " "; // 측정값(용존총인(DTP))
																			// (단위 :
																			// ㎎/L)
							String itemFl = " "; // 측정값(불소(F))
																			// (단위 :
																			// ㎎/L)
							String itemCol = " "; // 측정값(색도) (단위
																			// : 도)
							String itemCcl4 = " "; // 측정값(사염화탄소)
																			// (단위 :
																			// ㎎/L)
							String itemDceth = " "; // 측정값(1,2-다이클로로에탄)
																			// (단위 :
																			// ㎎/L)
							String itemDcm = " "; // 측정값(다이클로로메탄)
																			// (단위 :
																			// ㎎/L)
							String itemBenzene = " "; // 측정값(벤젠)
																				// (단위 :
																				// ㎎/L)
							String itemChcl3 = " "; // 측정값(클로로포름)
																			// (단위 :
																			// ㎎/L)
							String itemToc = " "; // 측정값(총유기탄소(TOC))
																			// (단위 :
																			// ㎎/L)
							String itemDehp = " "; // 측정값(다이에틸헥실프탈레이트(DEHP))
																			// (단위 :
																			// ㎎/L)
							String itemAntimon = " "; // 측정값(안티몬(Sb))
																				// (단위 :
																				// ㎎/L)
							String itemDiox = " "; // 측정값(1,4-다이옥세인)
																			// (단위 :
																			// ㎎/L)
							String itemHcho = " "; // 측정값(포름알데히드)
																			// (단위 :
																			// ㎎/L)
							String itemHcb = " "; // 측정값(헥사클로로벤젠)
																			// (단위 :
																			// ㎎/L)
							String itemNi = " "; // 측정값(니켈) (단위
																			// : ㎎/L)
							String itemBa = " "; // 측정값(바륨) (단위
																			// : ㎎/L)
							String itemSe = " "; // 측정값(셀레늄) (단위
																			// : ㎎/L)
							String numOfRows = " "; // 한 페이지 결과
																			// 수
							String pageNo_str = " "; // 페이지 번호
							String totalCount = " "; // 전체 결과 수

							JSONArray items = (JSONArray) getWaterMeasuringList.get("item");

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
									if(keyname.equals("PT_NO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											ptNo = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ptNo = " ";
										}	
									}
									if(keyname.equals("PT_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											ptNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											ptNm = " ";
										}
									}
									if(keyname.equals("ADDR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											addr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											addr = " ";
										}
									}
									if(keyname.equals("ORG_NM")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											orgNm = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											orgNm = " ";
										}
									}
									if(keyname.equals("WMYR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											wmyr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											wmyr = " ";
										}
									}
									if(keyname.equals("WMOD")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											wmod = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											wmod = " ";
										}
									}
									if(keyname.equals("WMWK")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											wmwk = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											wmwk = " ";
										}
									}
									if(keyname.equals("WMDEP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											wmdep = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											wmdep = " ";
										}
									}
									if(keyname.equals("LON_DGR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											lonDgr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											lonDgr = " ";
										}
									}
									if(keyname.equals("LON_MIN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											lonMin = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											lonMin = " ";
										}
									}
									if(keyname.equals("LON_SEC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											lonSec = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											lonSec = " ";
										}
									}
									if(keyname.equals("LAT_DGR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											latDgr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											latDgr = " ";
										}
									}
									if(keyname.equals("LAT_MIN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											latMin = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											latMin = " ";
										}
									}
									if(keyname.equals("LAT_SEC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											latSec = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											latSec = " ";
										}
									}	
									if(keyname.equals("WMCYMD")) {
										
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											wmcymd = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											wmcymd = " ";
										}
										
										
										
										SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
										dateFormatParser.setLenient(false);

										try {
											
											 dateFormatParser.parse(wmcymd);
											 
										} catch (Exception e) {
											System.out.println("잘못된 날짜 형식이므로 빈 값으로 바꿉니다.");

											wmcymd = " ";

										}
	
									}
									if(keyname.equals("ITEM_LVL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemLvl = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemLvl = " ";
										}
									}
									if(keyname.equals("ITEM_AMNT")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemAmnt = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemAmnt = " ";
										}
									}
									if(keyname.equals("ITEM_TEMP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTemp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTemp = " ";
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
									if(keyname.equals("ITEM_DOC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDoc = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDoc = " ";
										}
									}
									if(keyname.equals("ITEM_BOD")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBod = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBod = " ";
										}
									}
									if(keyname.equals("ITEM_COD")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCod = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCod = " ";
										}
									}
									if(keyname.equals("ITEM_SS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemSs = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemSs = " ";
										}
									}
									if(keyname.equals("ITEM_TCOLI")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTcoli = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTcoli = " ";
										}
									}
									if(keyname.equals("ITEM_TN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTn = " ";
										}
									}
									if(keyname.equals("ITEM_TP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTp = " ";
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
									if(keyname.equals("ITEM_CN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCn = " ";
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
									if(keyname.equals("ITEM_CR6")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCr6 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCr6 = " ";
										}
									}
									if(keyname.equals("ITEM_AS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemAs = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemAs = " ";
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
									if(keyname.equals("ITEM_PCB")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPcb = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPcb = " ";
										}
									}
									if(keyname.equals("ITEM_OP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemOp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemOp = " ";
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
									if(keyname.equals("ITEM_TRANS")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemTrans = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemTrans = " ";
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
									if(keyname.equals("ITEM_CLOA")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCloa = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCloa = " ";
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
									if(keyname.equals("ITEM_ZN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemZn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemZn = " ";
										}
									}
									if(keyname.equals("ITEM_CR")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCr = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCr = " ";
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
									if(keyname.equals("ITEM_PHENOL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPhenol = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPhenol = " ";
										}
									}
									if(keyname.equals("ITEM_NHEX")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemNhex = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemNhex = " ";
										}
									}
									if(keyname.equals("ITEM_EC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemEc = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemEc = " ";
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
									if(keyname.equals("ITEM_PCE")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPce = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPce = " ";
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
									if(keyname.equals("ITEM_NH3N")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemNh3n = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemNh3n = " ";
										}
									}
									if(keyname.equals("ITEM_ECOLI")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemEcoli = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemEcoli = " ";
										}
									}
									if(keyname.equals("ITEM_POP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemPop = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemPop = " ";
										}
									}
									if(keyname.equals("ITEM_DTN")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDtn = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDtn = " ";
										}
									}
									if(keyname.equals("ITEM_DTP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDtp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDtp = " ";
										}
									}
									if(keyname.equals("ITEM_FL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemFl = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemFl = " ";
										}
									}
									if(keyname.equals("ITEM_COL")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemCol = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemCol = " ";
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
									if(keyname.equals("ITEM_DCETH")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDceth = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDceth = " ";
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
									if(keyname.equals("ITEM_CHCL3")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemChcl3 = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemChcl3 = " ";
										}
									}
									if(keyname.equals("ITEM_TOC")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemToc = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemToc = " ";
										}
									}
									if(keyname.equals("ITEM_DEHP")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDehp = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDehp = " ";
										}
									}
									if(keyname.equals("ITEM_ANTIMON")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemAntimon = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemAntimon = " ";
										}
									}
									if(keyname.equals("ITEM_DIOX")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemDiox = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemDiox = " ";
										}
									}
									if(keyname.equals("ITEM_HCHO")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemHcho = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemHcho = " ";
										}
									}
									if(keyname.equals("ITEM_HCB")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemHcb = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemHcb = " ";
										}
									}
									if(keyname.equals("ITEM_NI")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemNi = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemNi = " ";
										}
									}
									if(keyname.equals("ITEM_BA")) {
										if(!(JsonParser.isEmpty(item.get(keyname)))){
											itemBa = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
													.replaceAll("(\\s{2,}|\\t{2,})", " ");
										}else{
											itemBa = " ";
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

									pw.write(args[0]);
									pw.write("|^");
									pw.write(resultCode_col);
									pw.write("|^");
									pw.write(resultMsg_col);
									pw.write("|^");
									pw.write(rowno);
									pw.write("|^");
									pw.write(ptNo);
									pw.write("|^");
									pw.write(ptNm);
									pw.write("|^");
									pw.write(addr);
									pw.write("|^");
									pw.write(orgNm);
									pw.write("|^");
									pw.write(wmyr);
									pw.write("|^");
									pw.write(wmod);
									pw.write("|^");
									pw.write(wmwk);
									pw.write("|^");
									pw.write(wmdep);
									pw.write("|^");
									pw.write(lonDgr);
									pw.write("|^");
									pw.write(lonMin);
									pw.write("|^");
									pw.write(lonSec);
									pw.write("|^");
									pw.write(latDgr);
									pw.write("|^");
									pw.write(latMin);
									pw.write("|^");
									pw.write(latSec);
									pw.write("|^");
									pw.write(wmcymd);
									pw.write("|^");
									pw.write(itemLvl);
									pw.write("|^");
									pw.write(itemAmnt);
									pw.write("|^");
									pw.write(itemTemp);
									pw.write("|^");
									pw.write(itemPh);
									pw.write("|^");
									pw.write(itemDoc);
									pw.write("|^");
									pw.write(itemBod);
									pw.write("|^");
									pw.write(itemCod);
									pw.write("|^");
									pw.write(itemSs);
									pw.write("|^");
									pw.write(itemTcoli);
									pw.write("|^");
									pw.write(itemTn);
									pw.write("|^");
									pw.write(itemTp);
									pw.write("|^");
									pw.write(itemCd);
									pw.write("|^");
									pw.write(itemCn);
									pw.write("|^");
									pw.write(itemPb);
									pw.write("|^");
									pw.write(itemCr6);
									pw.write("|^");
									pw.write(itemAs);
									pw.write("|^");
									pw.write(itemHg);
									pw.write("|^");
									pw.write(itemCu);
									pw.write("|^");
									pw.write(itemAbs);
									pw.write("|^");
									pw.write(itemPcb);
									pw.write("|^");
									pw.write(itemOp);
									pw.write("|^");
									pw.write(itemMn);
									pw.write("|^");
									pw.write(itemTrans);
									pw.write("|^");
									pw.write(itemCloa);
									pw.write("|^");
									pw.write(itemCl);
									pw.write("|^");
									pw.write(itemZn);
									pw.write("|^");
									pw.write(itemCr);
									pw.write("|^");
									pw.write(itemFe);
									pw.write("|^");
									pw.write(itemPhenol);
									pw.write("|^");
									pw.write(itemNhex);
									pw.write("|^");
									pw.write(itemEc);
									pw.write("|^");
									pw.write(itemTce);
									pw.write("|^");
									pw.write(itemPce);
									pw.write("|^");
									pw.write(itemNo3n);
									pw.write("|^");
									pw.write(itemNh3n);
									pw.write("|^");
									pw.write(itemEcoli);
									pw.write("|^");
									pw.write(itemPop);
									pw.write("|^");
									pw.write(itemDtn);
									pw.write("|^");
									pw.write(itemDtp);
									pw.write("|^");
									pw.write(itemFl);
									pw.write("|^");
									pw.write(itemCol);
									pw.write("|^");
									pw.write(itemCcl4);
									pw.write("|^");
									pw.write(itemDceth);
									pw.write("|^");
									pw.write(itemDcm);
									pw.write("|^");
									pw.write(itemBenzene);
									pw.write("|^");
									pw.write(itemChcl3);
									pw.write("|^");
									pw.write(itemToc);
									pw.write("|^");
									pw.write(itemDehp);
									pw.write("|^");
									pw.write(itemAntimon);
									pw.write("|^");
									pw.write(itemDiox);
									pw.write("|^");
									pw.write(itemHcho);
									pw.write("|^");
									pw.write(itemHcb);
									pw.write("|^");
									pw.write(itemNi);
									pw.write("|^");
									pw.write(itemBa);
									pw.write("|^");
									pw.write(itemSe);
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_01.dat", "PRI");

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
