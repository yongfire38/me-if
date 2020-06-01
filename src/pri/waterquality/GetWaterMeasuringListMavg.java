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

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

					int pageNo = 0;
					int pageCount = 0;

					// 물환경 수질측정망 운영결과 DB API에서는 siteId는 필요 없음
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					////공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"getWaterMeasuringListMavg\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
					}*/

					JSONObject count_obj = JsonParser.parsePriJson_waterMeasuring_obj(service_url, service_key, String.valueOf(pageNo), args);

					JSONObject count_getWaterMeasuringListMavgList = (JSONObject) count_obj
							.get("getWaterMeasuringListMavg");

					JSONObject count_header = (JSONObject) count_getWaterMeasuringListMavgList.get("header");
					String count_resultCode = count_header.get("code").toString().trim();
					String count_resultMsg = count_header.get("message").toString().trim();

					if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						throw new Exception();
					} else if (count_resultCode.equals("03")){
						pageCount = 1;
					} else {
						int numOfRows = ((Long) count_getWaterMeasuringListMavgList.get("numOfRows")).intValue();
						int totalCount = ((Long) count_getWaterMeasuringListMavgList.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 3. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱
					
					for (int i = 1; i <= pageCount; i++) {

						// 입력된 파라미터가  null 이라면 공백으로 들어가서 실행되어야 함
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						////공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"getWaterMeasuringListMavg\":{\"header\":{\"code\":\"03\",\"message\":\"NODATA_ERROR\"}}}";
						}*/

						JSONObject obj = JsonParser.parsePriJson_waterMeasuring_obj(service_url, service_key, String.valueOf(i), args);

						JSONObject getWaterMeasuringListMavgList = (JSONObject) obj.get("getWaterMeasuringListMavg");

						JSONObject header = (JSONObject) getWaterMeasuringListMavgList.get("header");

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
							
							String numOfRows = " "; // 한 페이지 결과수
							String pageNo_str = " "; // 페이지 번호
							String totalCount = " "; // 전체 결과 수	
							
							numOfRows = getWaterMeasuringListMavgList.get("numOfRows").toString().trim();

							pageNo_str = String.valueOf(i).trim();

							totalCount = getWaterMeasuringListMavgList.get("totalCount").toString().trim();

							JSONArray items = (JSONArray) getWaterMeasuringListMavgList.get("item");

							for (int r = 0; r < items.size(); r++) {
								
								String rowno = " "; // 순번
								String ptNo = " "; // 조사지점코드
								String ptNm = " "; // 조사지점명
								String wmyr = " "; // 측정년도
								String wmod = " "; // 측정월
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
								String itemSs = " ";// 측정값(부유물질(SS))
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
																				// (단위
																				// : ㎎/L)
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
								String itemOp = " ";// 측정값(유기인) (단위
																				// : ㎎/L)
								String itemMn = " "; // 측정값(용해성
																				// 망간(Mn))
																				// (단위
																				// : ㎎/L)
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
																				// (단위
																				// : ㎎/L)
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
																				// (단위
																				// :
																				// 분원성대장균군수/100㎖)
								String itemPop = " "; // 측정값(인산염
																				// 인(PO4-P))
																				// (단위 :
																				// ㎎/L)
								String itemDtn = " "; // 측정값(용존총질소(DTN))
																				// (단위
																				// : ㎎/L)
								String itemDtp = " "; // 측정값(용존총인(DTP))
																				// (단위
																				// : ㎎/L)
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
																				// (단위
																				// : ㎎/L)
								String itemDehp = " "; // 측정값(다이에틸헥실프탈레이트(DEHP))
																				// (단위 :
																				// ㎎/L)
								String itemAntimon = " "; // 측정값(안티몬(Sb))
																					// (단위 :
																					// ㎎/L)
								String itemDiox = " "; // 측정값(1,4-다이옥세인)
																				// (단위
																				// : ㎎/L)
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

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									rowno = JsonParser.colWrite_String(rowno, keyname, "ROWNO", item);
									ptNo = JsonParser.colWrite_String(ptNo, keyname, "PTNO", item);
									ptNm = JsonParser.colWrite_String(ptNm, keyname, "PTNM", item);
									wmyr = JsonParser.colWrite_String(wmyr, keyname, "WMYR", item);
									wmod = JsonParser.colWrite_String(wmod, keyname, "WMOD", item);
									itemAmnt = JsonParser.colWrite_String(itemAmnt, keyname, "ITEMAMNT", item);
									itemTemp = JsonParser.colWrite_String(itemTemp, keyname, "ITEMTEMP", item);
									itemPh = JsonParser.colWrite_String(itemPh, keyname, "ITEMPH", item);
									itemDoc = JsonParser.colWrite_String(itemDoc, keyname, "ITEMDOC", item);
									itemBod = JsonParser.colWrite_String(itemBod, keyname, "ITEMBOD", item);
									itemCod = JsonParser.colWrite_String(itemCod, keyname, "ITEMCOD", item);
									itemSs = JsonParser.colWrite_String(itemSs, keyname, "ITEMSS", item);
									itemTcoli = JsonParser.colWrite_String(itemTcoli, keyname, "ITEMTCOLI", item);
									itemTn = JsonParser.colWrite_String(itemTn, keyname, "ITEMTN", item);
									itemTp = JsonParser.colWrite_String(itemTp, keyname, "ITEMTP", item);
									itemCd = JsonParser.colWrite_String(itemCd, keyname, "ITEMCD", item);
									itemCn = JsonParser.colWrite_String(itemCn, keyname, "ITEMCN", item);
									itemPb = JsonParser.colWrite_String(itemPb, keyname, "ITEMPB", item);
									itemCr6 = JsonParser.colWrite_String(itemCr6, keyname, "ITEMCR6", item);
									itemAs = JsonParser.colWrite_String(itemAs, keyname, "ITEMAS", item);
									itemHg = JsonParser.colWrite_String(itemHg, keyname, "ITEMHG", item);
									itemCu = JsonParser.colWrite_String(itemCu, keyname, "ITEMCU", item);
									itemAbs = JsonParser.colWrite_String(itemAbs, keyname, "ITEMABS", item);
									itemPcb = JsonParser.colWrite_String(itemPcb, keyname, "ITEMPCB", item);
									itemOp = JsonParser.colWrite_String(itemOp, keyname, "ITEMOP", item);
									itemMn = JsonParser.colWrite_String(itemMn, keyname, "ITEMMN", item);
									itemTrans = JsonParser.colWrite_String(itemTrans, keyname, "ITEMTRANS", item);
									itemCloa = JsonParser.colWrite_String(itemCloa, keyname, "ITEMCLOA", item);
									itemCl = JsonParser.colWrite_String(itemCl, keyname, "ITEMCL", item);
									itemZn = JsonParser.colWrite_String(itemZn, keyname, "ITEMZN", item);
									itemCr = JsonParser.colWrite_String(itemCr, keyname, "ITEMCR", item);
									itemFe = JsonParser.colWrite_String(itemFe, keyname, "ITEMFE", item);
									itemPhenol = JsonParser.colWrite_String(itemPhenol, keyname, "ITEMPHENOL", item);
									itemNhex = JsonParser.colWrite_String(itemNhex, keyname, "ITEMNHEX", item);
									itemEc = JsonParser.colWrite_String(itemEc, keyname, "ITEMEC", item);
									itemTce = JsonParser.colWrite_String(itemTce, keyname, "ITEMTCE", item);
									itemPce = JsonParser.colWrite_String(itemPce, keyname, "ITEMPCE", item);
									itemNo3n = JsonParser.colWrite_String(itemNo3n, keyname, "ITEMNO3N", item);
									itemNh3n = JsonParser.colWrite_String(itemNh3n, keyname, "ITEMNH3N", item);
									itemEcoli = JsonParser.colWrite_String(itemEcoli, keyname, "ITEMECOLI", item);
									itemPop = JsonParser.colWrite_String(itemPop, keyname, "ITEMPOP", item);
									itemDtn = JsonParser.colWrite_String(itemDtn, keyname, "ITEMDTN", item);
									itemDtp = JsonParser.colWrite_String(itemDtp, keyname, "ITEMDTP", item);
									itemFl = JsonParser.colWrite_String(itemFl, keyname, "ITEMFL", item);
									itemCol = JsonParser.colWrite_String(itemCol, keyname, "ITEMCOL", item);
									itemCcl4 = JsonParser.colWrite_String(itemCcl4, keyname, "ITEMCCL4", item);
									itemDceth = JsonParser.colWrite_String(itemDceth, keyname, "ITEMDCETH", item);
									itemDcm = JsonParser.colWrite_String(itemDcm, keyname, "ITEMDCM", item);
									itemBenzene = JsonParser.colWrite_String(itemBenzene, keyname, "ITEMBENZENE", item);
									itemChcl3 = JsonParser.colWrite_String(itemChcl3, keyname, "ITEMCHCL3", item);
									itemToc = JsonParser.colWrite_String(itemToc, keyname, "ITEMTOC", item);
									itemDehp = JsonParser.colWrite_String(itemDehp, keyname, "ITEMDEHP", item);
									itemAntimon = JsonParser.colWrite_String(itemAntimon, keyname, "ITEMANTIMON", item);
									itemDiox = JsonParser.colWrite_String(itemDiox, keyname, "ITEMDIOX", item);
									itemHcho = JsonParser.colWrite_String(itemHcho, keyname, "ITEMHCHO", item);
									itemHcb = JsonParser.colWrite_String(itemHcb, keyname, "ITEMHCB", item);
									itemNi = JsonParser.colWrite_String(itemNi, keyname, "ITEMNI", item);
									itemBa = JsonParser.colWrite_String(itemBa, keyname, "ITEMBA", item);
									itemSe = JsonParser.colWrite_String(itemSe, keyname, "ITEMSE", item);
									numOfRows = JsonParser.colWrite_String(numOfRows, keyname, "numOfRows", item);
									pageNo_str = JsonParser.colWrite_String(pageNo_str, keyname, "pageNo", item);
									totalCount = JsonParser.colWrite_String(totalCount, keyname, "totalCount", item);

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
									pw.write(wmyr);
									pw.write("|^");
									pw.write(wmod);
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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "PRI/TIF_PRI_05.dat", "PRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", ptNoList :" + args[0]);
				System.exit(-1);
			}



	}

}
