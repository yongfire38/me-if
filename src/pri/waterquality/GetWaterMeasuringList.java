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

public class GetWaterMeasuringList {

	// 수질정보 DB 서비스 - 물환경 수질측정망 운영결과 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 5) {

			try {

				Thread.sleep(3000);

				// 측정소 코드를 받음
				if (args.length == 1) {
					
					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("PRI_WaterQualityService_getWaterMeasuringList_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_01.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("ptNoList"); // 검색조건 기관
							pw.write("|^");
							pw.write("resultCode"); // 결과코드
							pw.write("|^");
							pw.write("resultMsg"); // 결과메시지
							pw.write("|^");
							pw.write("rowno"); // 행번호
							pw.write("|^");
							pw.write("ptNo"); // 조사지점번호
							pw.write("|^");
							pw.write("ptNm"); // 조사지점명
							pw.write("|^");
							pw.write("addr"); // 조사지점 주소
							pw.write("|^");
							pw.write("orgNm"); // 조사기관명
							pw.write("|^");
							pw.write("wmyr"); // 측정년도
							pw.write("|^");
							pw.write("wmod"); // 측정월
							pw.write("|^");
							pw.write("wmwk"); // 검사회차
							pw.write("|^");
							pw.write("wmdep"); // 수심 (단위 : m)
							pw.write("|^");
							pw.write("lonDgr"); // 경도-도
							pw.write("|^");
							pw.write("lonMin"); // 경도-분
							pw.write("|^");
							pw.write("lonSec"); // 경도-초
							pw.write("|^");
							pw.write("latDgr"); // 위도-도
							pw.write("|^");
							pw.write("latMin"); // 위도-분
							pw.write("|^");
							pw.write("latSec"); // 위도-초
							pw.write("|^");
							pw.write("wmcymd"); // 검사일자
							pw.write("|^");
							pw.write("itemLvl"); // 측정값(수위) (단위 : m)
							pw.write("|^");
							pw.write("itemAmnt"); // 측정값(유량) (단위 : ㎥/sec)
							pw.write("|^");
							pw.write("itemTemp"); // 측정값(수온) (단위 : ℃)
							pw.write("|^");
							pw.write("itemPh"); // 측정값(수소이온농도(pH))
							pw.write("|^");
							pw.write("itemDoc"); // 측정값(용존산소(DO)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemBod"); // 측정값(생물화학적산소요구량(BOD)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemCod"); // 측정값(화학적산소요구량(COD)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemSs"); // 측정값(부유물질(SS)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemTcoli"); // 측정값(총대장균군)(단위 :
													// 총대장균군수/100㎖)
							pw.write("|^");
							pw.write("itemTn"); // 측정값(총질소(T-N)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemTp"); // 측정값(총인(T-P)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCd"); // 측정값(카드뮴(Cd)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCn"); // 측정값(시안(CN)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemPb"); // 측정값(납(Pb)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCr6"); // 측정값(6가크롬(Cr6+)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemAs"); // 측정값(비소(As)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemHg"); // 측정값(수은(Hg)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCu"); // 측정값(구리(Cu)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemAbs"); // 측정값(음이온계면활성제(ABS)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemPcb"); // 측정값(폴리클로리네이티드비페닐(PCB)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemOp"); // 측정값(유기인) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemMn"); // 측정값(용해성 망간(Mn)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemTrans"); // 측정값(투명도) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCloa"); // 측정값(클로로필-a(Chlorophyll-a))
													// (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCl"); // 측정값(염소이온(Cl-)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemZn"); // 측정값(아연(Zn)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCr"); // 측정값(크롬(Cr)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemFe"); // 측정값(용해성 철(Fe)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemPhenol"); // 측정값(페놀류(phenols)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemNhex"); // 측정값(노말헥산추출물질) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemEc"); // 측정값(전기전도도(EC)) (단위 : µS/㎝)
							pw.write("|^");
							pw.write("itemTce"); // 측정값(트리클로로에틸렌(TCE)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemPce"); // 측정값(테트라클로로에틸렌(PCE)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemNo3n"); // 측정값(질산성질소(NO3-N)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemNh3n"); // 측정값(암모니아성 질소(NH3-N)) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemEcoli"); // 측정값(분원성대장균군) (단위 :
													// 분원성대장균군수/100㎖)
							pw.write("|^");
							pw.write("itemPop"); // 측정값(인산염 인(PO4-P)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemDtn"); // 측정값(용존총질소(DTN)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemDtp"); // 측정값(용존총인(DTP)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemFl"); // 측정값(불소(F)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemCol"); // 측정값(색도) (단위 : 도)
							pw.write("|^");
							pw.write("itemCcl4"); // 측정값(사염화탄소) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemDceth"); // 측정값(1,2-다이클로로에탄) (단위 :
													// ㎎/L)
							pw.write("|^");
							pw.write("itemDcm"); // 측정값(다이클로로메탄) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemBenzene"); // 측정값(벤젠) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemChcl3"); // 측정값(클로로포름) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemToc"); // 측정값(총유기탄소(TOC)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemDehp"); // 측정값(다이에틸헥실프탈레이트(DEHP)) (단위
													// : ㎎/L)
							pw.write("|^");
							pw.write("itemAntimon"); // 측정값(안티몬(Sb)) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemDiox"); // 측정값(1,4-다이옥세인) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemHcho"); // 측정값(포름알데히드) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemHcb"); // 측정값(헥사클로로벤젠) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemNi"); // 측정값(니켈) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemBa"); // 측정값(바륨) (단위 : ㎎/L)
							pw.write("|^");
							pw.write("itemSe"); // 측정값(셀레늄) (단위 : ㎎/L)
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

					// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
					json = JsonParser.parsePriJson(service_url, service_key, String.valueOf(pageNo), "", args[0]);

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

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer resultCode_col = new StringBuffer(" "); // 결과코드
					StringBuffer resultMsg_col = new StringBuffer(" "); // 결과메시지
					StringBuffer rowno = new StringBuffer(" "); // 순번
					StringBuffer ptNo = new StringBuffer(" "); // 조사지점번호
					StringBuffer ptNm = new StringBuffer(" "); // 조사지점명
					StringBuffer addr = new StringBuffer(" "); // 조사지점 주소
					StringBuffer orgNm = new StringBuffer(" "); // 조사기관명
					StringBuffer wmyr = new StringBuffer(" "); // 측정년도
					StringBuffer wmod = new StringBuffer(" "); // 측정월
					StringBuffer wmwk = new StringBuffer(" "); // 검사회차
					StringBuffer wmdep = new StringBuffer(" "); // 수심 (단위 : m)
					StringBuffer lonDgr = new StringBuffer(" "); // 경도-도
					StringBuffer lonMin = new StringBuffer(" "); // 경도-분
					StringBuffer lonSec = new StringBuffer(" "); // 경도-초
					StringBuffer latDgr = new StringBuffer(" "); // 위도-도
					StringBuffer latMin = new StringBuffer(" "); // 위도-분
					StringBuffer latSec = new StringBuffer(" "); // 위도-초
					StringBuffer wmcymd = new StringBuffer(" "); // 검사일자
					StringBuffer itemLvl = new StringBuffer(" "); // 측정값(수위) (단위
																	// : m)
					StringBuffer itemAmnt = new StringBuffer(" "); // 측정값(유량)
																	// (단위 :
																	// ㎥/sec)
					StringBuffer itemTemp = new StringBuffer(" "); // 측정값(수온)
																	// (단위 : ℃)
					StringBuffer itemPh = new StringBuffer(" "); // 측정값(수소이온농도(pH))
					StringBuffer itemDoc = new StringBuffer(" "); // 측정값(용존산소(DO))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemBod = new StringBuffer(" "); // 측정값(생물화학적산소요구량(BOD))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCod = new StringBuffer(" "); // 측정값(화학적산소요구량(COD))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemSs = new StringBuffer(" "); // 측정값(부유물질(SS))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemTcoli = new StringBuffer(" "); // 측정값(총대장균군)(단위
																	// :
																	// 총대장균군수/100㎖)
					StringBuffer itemTn = new StringBuffer(" "); // 측정값(총질소(T-N))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemTp = new StringBuffer(" "); // 측정값(총인(T-P))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCd = new StringBuffer(" "); // 측정값(카드뮴(Cd))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCn = new StringBuffer(" "); // 측정값(시안(CN))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemPb = new StringBuffer(" "); // 측정값(납(Pb))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCr6 = new StringBuffer(" "); // 측정값(6가크롬(Cr6+))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemAs = new StringBuffer(" "); // 측정값(비소(As))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemHg = new StringBuffer(" "); // 측정값(수은(Hg))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCu = new StringBuffer(" "); // 측정값(구리(Cu))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemAbs = new StringBuffer(" "); // 측정값(음이온계면활성제(ABS))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemPcb = new StringBuffer(" "); // 측정값(폴리클로리네이티드비페닐(PCB))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemOp = new StringBuffer(" "); // 측정값(유기인) (단위
																	// : ㎎/L)
					StringBuffer itemMn = new StringBuffer(" "); // 측정값(용해성
																	// 망간(Mn))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemTrans = new StringBuffer(" "); // 측정값(투명도)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCloa = new StringBuffer(" "); // 측정값(클로로필-a(Chlorophyll-a))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCl = new StringBuffer(" "); // 측정값(염소이온(Cl-))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemZn = new StringBuffer(" "); // 측정값(아연(Zn))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCr = new StringBuffer(" "); // 측정값(크롬(Cr))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemFe = new StringBuffer(" "); // 측정값(용해성
																	// 철(Fe))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemPhenol = new StringBuffer(" "); // 측정값(페놀류(phenols))
																		// (단위 :
																		// ㎎/L)
					StringBuffer itemNhex = new StringBuffer(" "); // 측정값(노말헥산추출물질)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemEc = new StringBuffer(" "); // 측정값(전기전도도(EC))
																	// (단위 :
																	// µS/㎝)
					StringBuffer itemTce = new StringBuffer(" "); // 측정값(트리클로로에틸렌(TCE))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemPce = new StringBuffer(" "); // 측정값(테트라클로로에틸렌(PCE))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemNo3n = new StringBuffer(" "); // 측정값(질산성질소(NO3-N))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemNh3n = new StringBuffer(" "); // 측정값(암모니아성
																	// 질소(NH3-N))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemEcoli = new StringBuffer(" "); // 측정값(분원성대장균군)
																	// (단위 :
																	// 분원성대장균군수/100㎖)
					StringBuffer itemPop = new StringBuffer(" "); // 측정값(인산염
																	// 인(PO4-P))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDtn = new StringBuffer(" "); // 측정값(용존총질소(DTN))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDtp = new StringBuffer(" "); // 측정값(용존총인(DTP))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemFl = new StringBuffer(" "); // 측정값(불소(F))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemCol = new StringBuffer(" "); // 측정값(색도) (단위
																	// : 도)
					StringBuffer itemCcl4 = new StringBuffer(" "); // 측정값(사염화탄소)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDceth = new StringBuffer(" "); // 측정값(1,2-다이클로로에탄)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDcm = new StringBuffer(" "); // 측정값(다이클로로메탄)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemBenzene = new StringBuffer(" "); // 측정값(벤젠)
																		// (단위 :
																		// ㎎/L)
					StringBuffer itemChcl3 = new StringBuffer(" "); // 측정값(클로로포름)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemToc = new StringBuffer(" "); // 측정값(총유기탄소(TOC))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDehp = new StringBuffer(" "); // 측정값(다이에틸헥실프탈레이트(DEHP))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemAntimon = new StringBuffer(" "); // 측정값(안티몬(Sb))
																		// (단위 :
																		// ㎎/L)
					StringBuffer itemDiox = new StringBuffer(" "); // 측정값(1,4-다이옥세인)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemHcho = new StringBuffer(" "); // 측정값(포름알데히드)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemHcb = new StringBuffer(" "); // 측정값(헥사클로로벤젠)
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemNi = new StringBuffer(" "); // 측정값(니켈) (단위
																	// : ㎎/L)
					StringBuffer itemBa = new StringBuffer(" "); // 측정값(바륨) (단위
																	// : ㎎/L)
					StringBuffer itemSe = new StringBuffer(" "); // 측정값(셀레늄) (단위
																	// : ㎎/L)
					StringBuffer numOfRows = new StringBuffer(" "); // 한 페이지 결과
																	// 수
					StringBuffer pageNo_str = new StringBuffer(" "); // 페이지 번호
					StringBuffer totalCount = new StringBuffer(" "); // 전체 결과 수

					for (int i = 1; i <= pageCount; i++) {

						// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
						json = JsonParser.parsePriJson(service_url, service_key, String.valueOf(i), "", args[0]);

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getWaterMeasuringList = (JSONObject) obj.get("getWaterMeasuringList");

						JSONObject header = (JSONObject) getWaterMeasuringList.get("header");

						resultCode_col.setLength(0);
						resultCode_col.append(header.get("code").toString().trim()); // 결과
																						// 코드
						resultMsg_col.setLength(0);
						resultMsg_col.append(header.get("message").toString().trim()); // 결과
																						// 메시지
						numOfRows.setLength(0);
						numOfRows.append(getWaterMeasuringList.get("numOfRows").toString().trim());

						pageNo_str.setLength(0);
						pageNo_str.append(String.valueOf(i).trim());

						totalCount.setLength(0);
						totalCount.append(getWaterMeasuringList.get("totalCount").toString().trim());

						if (!(resultCode_col.toString().equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + resultCode_col.toString()
									+ "::resultMsg::" + resultMsg_col.toString());
						} else if (resultCode_col.toString().equals("00")) {

							JSONArray items = (JSONArray) getWaterMeasuringList.get("item");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(rowno, keyname, "ROWNO", item);
									JsonParser.colWrite(ptNo, keyname, "PT_NO", item);
									JsonParser.colWrite(ptNm, keyname, "PT_NM", item);
									JsonParser.colWrite(addr, keyname, "ADDR", item);
									JsonParser.colWrite(orgNm, keyname, "ORG_NM", item);
									JsonParser.colWrite(wmyr, keyname, "WMYR", item);
									JsonParser.colWrite(wmod, keyname, "WMOD", item);
									JsonParser.colWrite(wmwk, keyname, "WMWK", item);
									JsonParser.colWrite(wmdep, keyname, "WMDEP", item);
									JsonParser.colWrite(lonDgr, keyname, "LON_DGR", item);
									JsonParser.colWrite(lonMin, keyname, "LON_MIN", item);
									JsonParser.colWrite(lonSec, keyname, "LON_SEC", item);
									JsonParser.colWrite(latDgr, keyname, "LAT_DGR", item);
									JsonParser.colWrite(latMin, keyname, "LAT_MIN", item);
									JsonParser.colWrite(latSec, keyname, "LAT_SEC", item);
									JsonParser.colWrite_waterMeasuring(wmcymd, keyname, "WMCYMD", item);
									JsonParser.colWrite(itemLvl, keyname, "ITEM_LVL", item);
									JsonParser.colWrite(itemAmnt, keyname, "ITEM_AMNT", item);
									JsonParser.colWrite(itemTemp, keyname, "ITEM_TEMP", item);
									JsonParser.colWrite(itemPh, keyname, "ITEM_PH", item);
									JsonParser.colWrite(itemDoc, keyname, "ITEM_DOC", item);
									JsonParser.colWrite(itemBod, keyname, "ITEM_BOD", item);
									JsonParser.colWrite(itemCod, keyname, "ITEM_COD", item);
									JsonParser.colWrite(itemSs, keyname, "ITEM_SS", item);
									JsonParser.colWrite(itemTcoli, keyname, "ITEM_TCOLI", item);
									JsonParser.colWrite(itemTn, keyname, "ITEM_TN", item);
									JsonParser.colWrite(itemTp, keyname, "ITEM_TP", item);
									JsonParser.colWrite(itemCd, keyname, "ITEM_CD", item);
									JsonParser.colWrite(itemCn, keyname, "ITEM_CN", item);
									JsonParser.colWrite(itemPb, keyname, "ITEM_PB", item);
									JsonParser.colWrite(itemCr6, keyname, "ITEM_CR6", item);
									JsonParser.colWrite(itemAs, keyname, "ITEM_AS", item);
									JsonParser.colWrite(itemHg, keyname, "ITEM_HG", item);
									JsonParser.colWrite(itemCu, keyname, "ITEM_CU", item);
									JsonParser.colWrite(itemAbs, keyname, "ITEM_ABS", item);
									JsonParser.colWrite(itemPcb, keyname, "ITEM_PCB", item);
									JsonParser.colWrite(itemOp, keyname, "ITEM_OP", item);
									JsonParser.colWrite(itemMn, keyname, "ITEM_MN", item);
									JsonParser.colWrite(itemTrans, keyname, "ITEM_TRANS", item);
									JsonParser.colWrite(itemCloa, keyname, "ITEM_CLOA", item);
									JsonParser.colWrite(itemCl, keyname, "ITEM_CL", item);
									JsonParser.colWrite(itemZn, keyname, "ITEM_ZN", item);
									JsonParser.colWrite(itemCr, keyname, "ITEM_CR", item);
									JsonParser.colWrite(itemFe, keyname, "ITEM_FE", item);
									JsonParser.colWrite(itemPhenol, keyname, "ITEM_PHENOL", item);
									JsonParser.colWrite(itemNhex, keyname, "ITEM_NHEX", item);
									JsonParser.colWrite(itemEc, keyname, "ITEM_EC", item);
									JsonParser.colWrite(itemTce, keyname, "ITEM_TCE", item);
									JsonParser.colWrite(itemPce, keyname, "ITEM_PCE", item);
									JsonParser.colWrite(itemNo3n, keyname, "ITEM_NO3N", item);
									JsonParser.colWrite(itemNh3n, keyname, "ITEM_NH3N", item);
									JsonParser.colWrite(itemEcoli, keyname, "ITEM_ECOLI", item);
									JsonParser.colWrite(itemPop, keyname, "ITEM_POP", item);
									JsonParser.colWrite(itemDtn, keyname, "ITEM_DTN", item);
									JsonParser.colWrite(itemDtp, keyname, "ITEM_DTP", item);
									JsonParser.colWrite(itemFl, keyname, "ITEM_FL", item);
									JsonParser.colWrite(itemCol, keyname, "ITEM_COL", item);
									JsonParser.colWrite(itemCcl4, keyname, "ITEM_CCL4", item);
									JsonParser.colWrite(itemDceth, keyname, "ITEM_DCETH", item);
									JsonParser.colWrite(itemDcm, keyname, "ITEM_DCM", item);
									JsonParser.colWrite(itemBenzene, keyname, "ITEM_BENZENE", item);
									JsonParser.colWrite(itemChcl3, keyname, "ITEM_CHCL3", item);
									JsonParser.colWrite(itemToc, keyname, "ITEM_TOC", item);
									JsonParser.colWrite(itemDehp, keyname, "ITEM_DEHP", item);
									JsonParser.colWrite(itemAntimon, keyname, "ITEM_ANTIMON", item);
									JsonParser.colWrite(itemDiox, keyname, "ITEM_DIOX", item);
									JsonParser.colWrite(itemHcho, keyname, "ITEM_HCHO", item);
									JsonParser.colWrite(itemHcb, keyname, "ITEM_HCB", item);
									JsonParser.colWrite(itemNi, keyname, "ITEM_NI", item);
									JsonParser.colWrite(itemBa, keyname, "ITEM_BA", item);
									JsonParser.colWrite(itemSe, keyname, "ITEM_SE", item);
									JsonParser.colWrite(numOfRows, keyname, "numOfRows", item);
									JsonParser.colWrite(pageNo_str, keyname, "pageNo", item);
									JsonParser.colWrite(totalCount, keyname, "totalCount", item);

								}

								// 한번에 문자열 합침
								resultSb.append(args[0]);
								resultSb.append("|^");
								resultSb.append(resultCode_col);
								resultSb.append("|^");
								resultSb.append(resultMsg_col);
								resultSb.append("|^");
								resultSb.append(rowno);
								resultSb.append("|^");
								resultSb.append(ptNo);
								resultSb.append("|^");
								resultSb.append(ptNm);
								resultSb.append("|^");
								resultSb.append(addr);
								resultSb.append("|^");
								resultSb.append(orgNm);
								resultSb.append("|^");
								resultSb.append(wmyr);
								resultSb.append("|^");
								resultSb.append(wmod);
								resultSb.append("|^");
								resultSb.append(wmwk);
								resultSb.append("|^");
								resultSb.append(wmdep);
								resultSb.append("|^");
								resultSb.append(lonDgr);
								resultSb.append("|^");
								resultSb.append(lonMin);
								resultSb.append("|^");
								resultSb.append(lonSec);
								resultSb.append("|^");
								resultSb.append(latDgr);
								resultSb.append("|^");
								resultSb.append(latMin);
								resultSb.append("|^");
								resultSb.append(latSec);
								resultSb.append("|^");
								resultSb.append(wmcymd);
								resultSb.append("|^");
								resultSb.append(itemLvl);
								resultSb.append("|^");
								resultSb.append(itemAmnt);
								resultSb.append("|^");
								resultSb.append(itemTemp);
								resultSb.append("|^");
								resultSb.append(itemPh);
								resultSb.append("|^");
								resultSb.append(itemDoc);
								resultSb.append("|^");
								resultSb.append(itemBod);
								resultSb.append("|^");
								resultSb.append(itemCod);
								resultSb.append("|^");
								resultSb.append(itemSs);
								resultSb.append("|^");
								resultSb.append(itemTcoli);
								resultSb.append("|^");
								resultSb.append(itemTn);
								resultSb.append("|^");
								resultSb.append(itemTp);
								resultSb.append("|^");
								resultSb.append(itemCd);
								resultSb.append("|^");
								resultSb.append(itemCn);
								resultSb.append("|^");
								resultSb.append(itemPb);
								resultSb.append("|^");
								resultSb.append(itemCr6);
								resultSb.append("|^");
								resultSb.append(itemAs);
								resultSb.append("|^");
								resultSb.append(itemHg);
								resultSb.append("|^");
								resultSb.append(itemCu);
								resultSb.append("|^");
								resultSb.append(itemAbs);
								resultSb.append("|^");
								resultSb.append(itemPcb);
								resultSb.append("|^");
								resultSb.append(itemOp);
								resultSb.append("|^");
								resultSb.append(itemMn);
								resultSb.append("|^");
								resultSb.append(itemTrans);
								resultSb.append("|^");
								resultSb.append(itemCloa);
								resultSb.append("|^");
								resultSb.append(itemCl);
								resultSb.append("|^");
								resultSb.append(itemZn);
								resultSb.append("|^");
								resultSb.append(itemCr);
								resultSb.append("|^");
								resultSb.append(itemFe);
								resultSb.append("|^");
								resultSb.append(itemPhenol);
								resultSb.append("|^");
								resultSb.append(itemNhex);
								resultSb.append("|^");
								resultSb.append(itemEc);
								resultSb.append("|^");
								resultSb.append(itemTce);
								resultSb.append("|^");
								resultSb.append(itemPce);
								resultSb.append("|^");
								resultSb.append(itemNo3n);
								resultSb.append("|^");
								resultSb.append(itemNh3n);
								resultSb.append("|^");
								resultSb.append(itemEcoli);
								resultSb.append("|^");
								resultSb.append(itemPop);
								resultSb.append("|^");
								resultSb.append(itemDtn);
								resultSb.append("|^");
								resultSb.append(itemDtp);
								resultSb.append("|^");
								resultSb.append(itemFl);
								resultSb.append("|^");
								resultSb.append(itemCol);
								resultSb.append("|^");
								resultSb.append(itemCcl4);
								resultSb.append("|^");
								resultSb.append(itemDceth);
								resultSb.append("|^");
								resultSb.append(itemDcm);
								resultSb.append("|^");
								resultSb.append(itemBenzene);
								resultSb.append("|^");
								resultSb.append(itemChcl3);
								resultSb.append("|^");
								resultSb.append(itemToc);
								resultSb.append("|^");
								resultSb.append(itemDehp);
								resultSb.append("|^");
								resultSb.append(itemAntimon);
								resultSb.append("|^");
								resultSb.append(itemDiox);
								resultSb.append("|^");
								resultSb.append(itemHcho);
								resultSb.append("|^");
								resultSb.append(itemHcb);
								resultSb.append("|^");
								resultSb.append(itemNi);
								resultSb.append("|^");
								resultSb.append(itemBa);
								resultSb.append("|^");
								resultSb.append(itemSe);
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_01.dat", "PRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ptNoList :" + args[0]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
