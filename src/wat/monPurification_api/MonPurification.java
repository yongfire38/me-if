package wat.monPurification_api;

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

public class MonPurification {

	// 국가 상수도 정보 시스템 - 상수도 법정 수질정보 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필요한 파라미터는 년(4자리)과 월(2자리)의 2개
				if (args.length == 2) {

					if (args[0].length() == 4 && args[1].length() == 2) {

						System.out.println("firstLine start..");
						long start = System.currentTimeMillis(); // 시작시간

						// step 0.open api url과 서비스 키.
						String service_url = JsonParser.getProperty("monPurification_url");
						String service_key = JsonParser.getProperty("monPurification_service_key");

						// step 1.파일의 작성
						File file = new File(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_04.dat");

						// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱

						int pageNo = 0;
						int pageCount = 0;
						String numberOfRows_str = "";
						String totalCount_str = "";

						// 년과 월 입력이 필수사항. 페이지 당 데이터 개수는 100으로 고정됨(변경 불가)
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						/*if(json.indexOf("</") > -1){
							json ="{\"OPERATION\":\"MonPurification\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"1\",\"numberOfRows\":100},\"items\":[],\"measurementItems\":null},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						}*/

						JSONObject count_obj = JsonParser.parseWatJson_obj(service_url, service_key, args[0], args[1],
								String.valueOf(pageNo));
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_header = (JSONObject) count_response.get("header");
						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::"
									+ count_resultMsg);
						} else {
							
							JSONObject count_body = (JSONObject) count_response.get("body");
							JSONObject count_itemsInfo = (JSONObject) count_body.get("itemsInfo");

							// json 값에서 가져온 전체 데이터 캐수와 한 페이지 당 개수
							int totalCount = ((Long) count_itemsInfo.get("totalCount")).intValue();
							int numberOfRows = ((Long) count_itemsInfo.get("numberOfRows")).intValue();
							totalCount_str = Integer.toString(totalCount);
							numberOfRows_str = Integer.toString(numberOfRows);

							pageCount = (totalCount / numberOfRows) + 1;

							// System.out.println("pageCount:::::" + pageCount);
						}

						// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; ++i) {
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							//공통 클래스로 로직 빼 놓음
							/*if(json.indexOf("</") > -1){
								json ="{\"OPERATION\":\"MonPurification\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"1\",\"numberOfRows\":100},\"items\":[],\"measurementItems\":null},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
							}*/

							JSONObject obj = JsonParser.parseWatJson_obj(service_url, service_key, args[0], args[1],
									String.valueOf(i));
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

							if (!(resultCode.equals("00"))) {
								System.out.println(
										"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							} else if (resultCode.equals("00")) {

								JSONArray items = (JSONArray) body.get("items");

								for (int r = 0; r < items.size(); r++) {
									
									String RNUM = " "; // 순번
									String FIM_FCLT_NAM = " "; // 정수장명
									String FIM_REGN_CTY = " "; // 관리기관명
									String BRTC_NM = " "; // 시도명
									String SIGNGU_NM = " "; // 시군구명
									String TELNO = " "; // 관리기관전화번호
									String WCI_COLL_DAT = " "; // 채수일자
									String WCI_INORG_NAM = " "; // 검사기관명
									String TCC = " "; // 일반세균
									String TC = " "; // 총대장균군
									String EFC = " "; // 대장균/분원성대장균군
									String PB = " "; // 납
									String FL = " "; // 불소
									String AS = " "; // 비소
									String SE = " "; // 셀레늄
									String HG = " "; // 수은
									String CN = " "; // 시안
									String CR = " "; // 크롬
									String NHN = " "; // 암모니아성질소
									String NON = " "; // 질산성질소
									String CD = " "; // 카드뮴
									String BOR = " "; // 붕소
									String BRO = " "; // 브롬산염
									String D_URNM = " "; // 우라늄
									String PHEN = " "; // 페놀
									String DIA = " "; // 다이아지논
									String PARA = " "; // 파라티온
									String PENI = " "; // 페니트로티온
									String CBR = " "; // 카바릴
									String TCE = " "; // 1_1_1-트리클로로에탄
									String TTCE = " "; // 테트라클로로에틸렌
									String TCF = " "; // 트리클로로에틸렌
									String CC = " "; // 사염화탄소
									String DDE = " "; // 1_1-디클로로에틸렌
									String DCM = " "; // 디클로로메탄
									String BZ = " "; // 벤젠
									String TOL = " "; // 톨루엔
									String EB = " "; // 에틸벤젠
									String XYL = " "; // 크실렌
									String DBCP = " "; // 1_2-디브로모-3-클로로프로판
									String DIOX = " "; // 1_4-다이옥산
									String RC = " "; // 잔류염소
									String THMS = " "; // 총트리할로메탄
									String CF = " "; // 클로로포름
									String BDCM = " "; // 브로모디클로로메탄
									String DBCM = " "; // 디브로모클로로메탄
									String CH = " "; // 클로랄하이드레이트
									String DIT = " "; // 디브로모아세토니트릴
									String TRT = " "; // 디클로로아세토니트릴
									String TRL = " "; // 트리클로로아세토니트릴
									String HAS = " "; // 할로아세틱에시드
									String FOAH = " "; // 포름알데히드
									String HR = " "; // 경도
									String KMN = " "; // 과망간산칼륨소비량
									String ODOR = " "; // 냄새
									String TW = " "; // 맛
									String CU = " "; // 동
									String CW = " "; // 색도
									String DTG = " "; // 세제
									String PH = " "; // 수소이온농도
									String ZN = " "; // 아연
									String CL = " "; // 염소이온
									String RE = " "; // 증발잔류물
									String FE = " "; // 철
									String MN = " "; // 망간
									String TU = " "; // 탁도
									String SO = " "; // 황산이온
									String AL = " "; // 알루미늄
									String WCI_UPDATE_DAT = " "; // 데이터기준일자

									JSONObject item = (JSONObject) items.get(r);

									Set<String> key = item.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {
										
										String keyname = iter.next();
										
										RNUM = JsonParser.colWrite_String(RNUM, keyname, "RNUM", item);
										FIM_FCLT_NAM = JsonParser.colWrite_String(FIM_FCLT_NAM, keyname, "FIM_FCLT_NAM", item);
										FIM_REGN_CTY = JsonParser.colWrite_String(FIM_REGN_CTY, keyname, "FIM_REGN_CTY", item);
										BRTC_NM = JsonParser.colWrite_String(BRTC_NM, keyname, "BRTC_NM", item);
										SIGNGU_NM = JsonParser.colWrite_String(SIGNGU_NM, keyname, "SIGNGU_NM", item);
										TELNO = JsonParser.colWrite_String(TELNO, keyname, "TELNO", item);
										WCI_COLL_DAT = JsonParser.colWrite_String(WCI_COLL_DAT, keyname, "WCI_COLL_DAT", item);
										WCI_INORG_NAM = JsonParser.colWrite_String(WCI_INORG_NAM, keyname, "WCI_INORG_NAM", item);
										TCC = JsonParser.colWrite_String(TCC, keyname, "TCC", item);
										TC = JsonParser.colWrite_String(TC, keyname, "TC", item);
										EFC = JsonParser.colWrite_String(EFC, keyname, "EFC", item);
										PB = JsonParser.colWrite_String(PB, keyname, "PB", item);
										FL = JsonParser.colWrite_String(FL, keyname, "FL", item);
										AS = JsonParser.colWrite_String(AS, keyname, "AS", item);
										SE = JsonParser.colWrite_String(SE, keyname, "SE", item);
										HG = JsonParser.colWrite_String(HG, keyname, "HG", item);
										CN = JsonParser.colWrite_String(CN, keyname, "CN", item);
										CR = JsonParser.colWrite_String(CR, keyname, "CR", item);
										NHN = JsonParser.colWrite_String(NHN, keyname, "NHN", item);
										NON = JsonParser.colWrite_String(NON, keyname, "NON", item);
										CD = JsonParser.colWrite_String(CD, keyname, "CD", item);
										BOR = JsonParser.colWrite_String(BOR, keyname, "BOR", item);
										BRO = JsonParser.colWrite_String(BRO, keyname, "BRO", item);
										D_URNM = JsonParser.colWrite_String(D_URNM, keyname, "D-URNM", item);
										PHEN = JsonParser.colWrite_String(PHEN, keyname, "PHEN", item);
										DIA = JsonParser.colWrite_String(DIA, keyname, "DIA", item);
										PARA = JsonParser.colWrite_String(PARA, keyname, "PARA", item);
										PENI = JsonParser.colWrite_String(PENI, keyname, "PENI", item);
										CBR = JsonParser.colWrite_String(CBR, keyname, "CBR", item);
										TCE = JsonParser.colWrite_String(TCE, keyname, "TCE", item);
										TTCE = JsonParser.colWrite_String(TTCE, keyname, "TTCE", item);
										TCF = JsonParser.colWrite_String(TCF, keyname, "TCF", item);
										CC = JsonParser.colWrite_String(CC, keyname, "CC", item);
										DDE = JsonParser.colWrite_String(DDE, keyname, "DDE", item);
										DCM = JsonParser.colWrite_String(DCM, keyname, "DCM", item);
										BZ = JsonParser.colWrite_String(BZ, keyname, "BZ", item);
										TOL = JsonParser.colWrite_String(TOL, keyname, "TOL", item);
										EB = JsonParser.colWrite_String(EB, keyname, "EB", item);
										XYL = JsonParser.colWrite_String(XYL, keyname, "XYL", item);
										DBCP = JsonParser.colWrite_String(DBCP, keyname, "DBCP", item);
										DIOX = JsonParser.colWrite_String(DIOX, keyname, "DIOX", item);
										RC = JsonParser.colWrite_String(RC, keyname, "RC", item);
										THMS = JsonParser.colWrite_String(THMS, keyname, "THMS", item);
										CF = JsonParser.colWrite_String(CF, keyname, "CF", item);
										BDCM = JsonParser.colWrite_String(BDCM, keyname, "BDCM", item);
										DBCM = JsonParser.colWrite_String(DBCM, keyname, "DBCM", item);
										CH = JsonParser.colWrite_String(CH, keyname, "CH", item);
										DIT = JsonParser.colWrite_String(DIT, keyname, "DIT", item);
										TRT = JsonParser.colWrite_String(TRT, keyname, "TRT", item);
										TRL = JsonParser.colWrite_String(TRL, keyname, "TRL", item);
										HAS = JsonParser.colWrite_String(HAS, keyname, "HAS", item);
										FOAH = JsonParser.colWrite_String(FOAH, keyname, "FOAH", item);
										HR = JsonParser.colWrite_String(HR, keyname, "HR", item);
										KMN = JsonParser.colWrite_String(KMN, keyname, "KMN", item);
										ODOR = JsonParser.colWrite_String(ODOR, keyname, "ODOR", item);
										TW = JsonParser.colWrite_String(TW, keyname, "TW", item);
										CU = JsonParser.colWrite_String(CU, keyname, "CU", item);
										CW = JsonParser.colWrite_String(CW, keyname, "CW", item);
										DTG = JsonParser.colWrite_String(DTG, keyname, "DTG", item);
										PH = JsonParser.colWrite_String(PH, keyname, "PH", item);
										ZN = JsonParser.colWrite_String(ZN, keyname, "ZN", item);
										CL = JsonParser.colWrite_String(CL, keyname, "CL", item);
										RE = JsonParser.colWrite_String(RE, keyname, "RE", item);
										FE = JsonParser.colWrite_String(FE, keyname, "FE", item);
										MN = JsonParser.colWrite_String(MN, keyname, "MN", item);
										TU = JsonParser.colWrite_String(TU, keyname, "TU", item);
										SO = JsonParser.colWrite_String(SO, keyname, "SO", item);
										AL = JsonParser.colWrite_String(AL, keyname, "AL", item);
										WCI_UPDATE_DAT = JsonParser.colWrite_String(WCI_UPDATE_DAT, keyname, "WCI_UPDATE_DAT", item);

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(args[0]);
										pw.write("|^");
										pw.write(args[1]);
										pw.write("|^");
										pw.write(numberOfRows_str);
										pw.write("|^");
										pw.write(String.valueOf(i));
										pw.write("|^");
										pw.write(totalCount_str);
										pw.write("|^");
										pw.write(RNUM);
										pw.write("|^");
										pw.write(FIM_FCLT_NAM);
										pw.write("|^");
										pw.write(FIM_REGN_CTY);
										pw.write("|^");
										pw.write(BRTC_NM);
										pw.write("|^");
										pw.write(SIGNGU_NM);
										pw.write("|^");
										pw.write(TELNO);
										pw.write("|^");
										pw.write(WCI_COLL_DAT);
										pw.write("|^");
										pw.write(WCI_INORG_NAM);
										pw.write("|^");
										pw.write(TCC);
										pw.write("|^");
										pw.write(TC);
										pw.write("|^");
										pw.write(EFC);
										pw.write("|^");
										pw.write(PB);
										pw.write("|^");
										pw.write(FL);
										pw.write("|^");
										pw.write(AS);
										pw.write("|^");
										pw.write(SE);
										pw.write("|^");
										pw.write(HG);
										pw.write("|^");
										pw.write(CN);
										pw.write("|^");
										pw.write(CR);
										pw.write("|^");
										pw.write(NHN);
										pw.write("|^");
										pw.write(NON);
										pw.write("|^");
										pw.write(CD);
										pw.write("|^");
										pw.write(BOR);
										pw.write("|^");
										pw.write(BRO);
										pw.write("|^");
										pw.write(D_URNM);
										pw.write("|^");
										pw.write(PHEN);
										pw.write("|^");
										pw.write(DIA);
										pw.write("|^");
										pw.write(PARA);
										pw.write("|^");
										pw.write(PENI);
										pw.write("|^");
										pw.write(CBR);
										pw.write("|^");
										pw.write(TCE);
										pw.write("|^");
										pw.write(TTCE);
										pw.write("|^");
										pw.write(TCF);
										pw.write("|^");
										pw.write(CC);
										pw.write("|^");
										pw.write(DDE);
										pw.write("|^");
										pw.write(DCM);
										pw.write("|^");
										pw.write(BZ);
										pw.write("|^");
										pw.write(TOL);
										pw.write("|^");
										pw.write(EB);
										pw.write("|^");
										pw.write(XYL);
										pw.write("|^");
										pw.write(DBCP);
										pw.write("|^");
										pw.write(DIOX);
										pw.write("|^");
										pw.write(RC);
										pw.write("|^");
										pw.write(THMS);
										pw.write("|^");
										pw.write(CF);
										pw.write("|^");
										pw.write(BDCM);
										pw.write("|^");
										pw.write(DBCM);
										pw.write("|^");
										pw.write(CH);
										pw.write("|^");
										pw.write(DIT);
										pw.write("|^");
										pw.write(TRT);
										pw.write("|^");
										pw.write(TRL);
										pw.write("|^");
										pw.write(HAS);
										pw.write("|^");
										pw.write(FOAH);
										pw.write("|^");
										pw.write(HR);
										pw.write("|^");
										pw.write(KMN);
										pw.write("|^");
										pw.write(ODOR);
										pw.write("|^");
										pw.write(TW);
										pw.write("|^");
										pw.write(CU);
										pw.write("|^");
										pw.write(CW);
										pw.write("|^");
										pw.write(DTG);
										pw.write("|^");
										pw.write(PH);
										pw.write("|^");
										pw.write(ZN);
										pw.write("|^");
										pw.write(CL);
										pw.write("|^");
										pw.write(RE);
										pw.write("|^");
										pw.write(FE);
										pw.write("|^");
										pw.write(MN);
										pw.write("|^");
										pw.write(TU);
										pw.write("|^");
										pw.write(SO);
										pw.write("|^");
										pw.write(AL);
										pw.write("|^");
										pw.write(WCI_UPDATE_DAT);
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}	
									
								}

							} else {
								System.out.println("parsing error!!");
							}

							//Thread.sleep(1000);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_04.dat", "WAT");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else {
						System.out.println("파라미터 형식 에러!!");
						System.exit(-1);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("year :" + args[0] + ": month :" + args[1]);
			}



	}

}
