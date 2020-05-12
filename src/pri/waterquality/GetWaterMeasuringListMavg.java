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

public class GetWaterMeasuringListMavg {

	// 수질정보 DB 서비스 - 물환경 수질측정망 운영결과 월평균 DB
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				

				// '측정소 코드', '측정년도', '측정월'을 파라미터로 받음 (년도와 월은 필수는 아님)
				// 자바 단 에러 때문에 측정소 코드는 필수로 받도록 함
				if (args.length > 0 && args.length <= 3) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser
							.getProperty("PRI_WaterQualityService_getWaterMeasuringListMavg_url");
					String service_key = JsonParser.getProperty("PRI_WaterQualityService_key");
					
					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_05.dat");

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

					// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
					json = JsonParser.parsePriJson_waterMeasuring(service_url, service_key, String.valueOf(pageNo), args);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"getWaterMeasuringListMavg\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					JSONObject count_getWaterMeasuringListMavgList = (JSONObject) count_obj
							.get("getWaterMeasuringListMavg");

					JSONObject count_header = (JSONObject) count_getWaterMeasuringListMavgList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						int numOfRows = ((Long) count_getWaterMeasuringListMavgList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getWaterMeasuringListMavgList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 3. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer resultCode_col = new StringBuffer(" "); // 결과코드
					StringBuffer resultMsg_col = new StringBuffer(" "); // 결과메시지
					StringBuffer rowno = new StringBuffer(" "); // 순번
					StringBuffer ptNo = new StringBuffer(" "); // 조사지점코드
					StringBuffer ptNm = new StringBuffer(" "); // 조사지점명
					StringBuffer wmyr = new StringBuffer(" "); // 측정년도
					StringBuffer wmod = new StringBuffer(" "); // 측정월
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
																	// (단위
																	// : ㎎/L)
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
																	// (단위
																	// : ㎎/L)
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
																	// (단위
																	// : ㎎/L)
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
																	// (단위
																	// :
																	// 분원성대장균군수/100㎖)
					StringBuffer itemPop = new StringBuffer(" "); // 측정값(인산염
																	// 인(PO4-P))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemDtn = new StringBuffer(" "); // 측정값(용존총질소(DTN))
																	// (단위
																	// : ㎎/L)
					StringBuffer itemDtp = new StringBuffer(" "); // 측정값(용존총인(DTP))
																	// (단위
																	// : ㎎/L)
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
																	// (단위
																	// : ㎎/L)
					StringBuffer itemDehp = new StringBuffer(" "); // 측정값(다이에틸헥실프탈레이트(DEHP))
																	// (단위 :
																	// ㎎/L)
					StringBuffer itemAntimon = new StringBuffer(" "); // 측정값(안티몬(Sb))
																		// (단위 :
																		// ㎎/L)
					StringBuffer itemDiox = new StringBuffer(" "); // 측정값(1,4-다이옥세인)
																	// (단위
																	// : ㎎/L)
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

						// 입력된 파라미터가  null 이라면 공백으로 들어가서 실행되어야 함
						json = JsonParser.parsePriJson_waterMeasuring(service_url, service_key, String.valueOf(i), args);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"getWaterMeasuringListMavg\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONObject getWaterMeasuringListMavgList = (JSONObject) obj.get("getWaterMeasuringListMavg");

						JSONObject header = (JSONObject) getWaterMeasuringListMavgList.get("header");

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
							numOfRows.append(getWaterMeasuringListMavgList.get("numOfRows").toString().trim());

							pageNo_str.setLength(0);
							pageNo_str.append(String.valueOf(i).trim());

							totalCount.setLength(0);
							totalCount.append(getWaterMeasuringListMavgList.get("totalCount").toString().trim());

							JSONArray items = (JSONArray) getWaterMeasuringListMavgList.get("item");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(rowno, keyname, "ROWNO", item);
									JsonParser.colWrite(ptNo, keyname, "PTNO", item);
									JsonParser.colWrite(ptNm, keyname, "PTNM", item);
									JsonParser.colWrite(wmyr, keyname, "WMYR", item);
									JsonParser.colWrite(wmod, keyname, "WMOD", item);
									JsonParser.colWrite(itemAmnt, keyname, "ITEMAMNT", item);
									JsonParser.colWrite(itemTemp, keyname, "ITEMTEMP", item);
									JsonParser.colWrite(itemPh, keyname, "ITEMPH", item);
									JsonParser.colWrite(itemDoc, keyname, "ITEMDOC", item);
									JsonParser.colWrite(itemBod, keyname, "ITEMBOD", item);
									JsonParser.colWrite(itemCod, keyname, "ITEMCOD", item);
									JsonParser.colWrite(itemSs, keyname, "ITEMSS", item);
									JsonParser.colWrite(itemTcoli, keyname, "ITEMTCOLI", item);
									JsonParser.colWrite(itemTn, keyname, "ITEMTN", item);
									JsonParser.colWrite(itemTp, keyname, "ITEMTP", item);
									JsonParser.colWrite(itemCd, keyname, "ITEMCD", item);
									JsonParser.colWrite(itemCn, keyname, "ITEMCN", item);
									JsonParser.colWrite(itemPb, keyname, "ITEMPB", item);
									JsonParser.colWrite(itemCr6, keyname, "ITEMCR6", item);
									JsonParser.colWrite(itemAs, keyname, "ITEMAS", item);
									JsonParser.colWrite(itemHg, keyname, "ITEMHG", item);
									JsonParser.colWrite(itemCu, keyname, "ITEMCU", item);
									JsonParser.colWrite(itemAbs, keyname, "ITEMABS", item);
									JsonParser.colWrite(itemPcb, keyname, "ITEMPCB", item);
									JsonParser.colWrite(itemOp, keyname, "ITEMOP", item);
									JsonParser.colWrite(itemMn, keyname, "ITEMMN", item);
									JsonParser.colWrite(itemTrans, keyname, "ITEMTRANS", item);
									JsonParser.colWrite(itemCloa, keyname, "ITEMCLOA", item);
									JsonParser.colWrite(itemCl, keyname, "ITEMCL", item);
									JsonParser.colWrite(itemZn, keyname, "ITEMZN", item);
									JsonParser.colWrite(itemCr, keyname, "ITEMCR", item);
									JsonParser.colWrite(itemFe, keyname, "ITEMFE", item);
									JsonParser.colWrite(itemPhenol, keyname, "ITEMPHENOL", item);
									JsonParser.colWrite(itemNhex, keyname, "ITEMNHEX", item);
									JsonParser.colWrite(itemEc, keyname, "ITEMEC", item);
									JsonParser.colWrite(itemTce, keyname, "ITEMTCE", item);
									JsonParser.colWrite(itemPce, keyname, "ITEMPCE", item);
									JsonParser.colWrite(itemNo3n, keyname, "ITEMNO3N", item);
									JsonParser.colWrite(itemNh3n, keyname, "ITEMNH3N", item);
									JsonParser.colWrite(itemEcoli, keyname, "ITEMECOLI", item);
									JsonParser.colWrite(itemPop, keyname, "ITEMPOP", item);
									JsonParser.colWrite(itemDtn, keyname, "ITEMDTN", item);
									JsonParser.colWrite(itemDtp, keyname, "ITEMDTP", item);
									JsonParser.colWrite(itemFl, keyname, "ITEMFL", item);
									JsonParser.colWrite(itemCol, keyname, "ITEMCOL", item);
									JsonParser.colWrite(itemCcl4, keyname, "ITEMCCL4", item);
									JsonParser.colWrite(itemDceth, keyname, "ITEMDCETH", item);
									JsonParser.colWrite(itemDcm, keyname, "ITEMDCM", item);
									JsonParser.colWrite(itemBenzene, keyname, "ITEMBENZENE", item);
									JsonParser.colWrite(itemChcl3, keyname, "ITEMCHCL3", item);
									JsonParser.colWrite(itemToc, keyname, "ITEMTOC", item);
									JsonParser.colWrite(itemDehp, keyname, "ITEMDEHP", item);
									JsonParser.colWrite(itemAntimon, keyname, "ITEMANTIMON", item);
									JsonParser.colWrite(itemDiox, keyname, "ITEMDIOX", item);
									JsonParser.colWrite(itemHcho, keyname, "ITEMHCHO", item);
									JsonParser.colWrite(itemHcb, keyname, "ITEMHCB", item);
									JsonParser.colWrite(itemNi, keyname, "ITEMNI", item);
									JsonParser.colWrite(itemBa, keyname, "ITEMBA", item);
									JsonParser.colWrite(itemSe, keyname, "ITEMSE", item);
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
								resultSb.append(wmyr);
								resultSb.append("|^");
								resultSb.append(wmod);
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

						//Thread.sleep(2500);
					}

					// step 4. 파일에 쓰기
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.println(resultSb.toString());
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_05.dat", "PRI");

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
