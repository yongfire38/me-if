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
import org.json.simple.parser.JSONParser;

import common.JsonParser;
import common.TransSftp;

public class MonPurification {

	

	// 국가 상수도 정보 시스템 - 상수도 법정 수질정보 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필요한 파라미터는 년(4자리)과 월(2자리)의 2개
		if (args.length == 2) {

			if (args[0].length() == 4 && args[1].length() == 2) {

				System.out.println("firstLine start..");
				long start = System.currentTimeMillis(); //시작시간

				// step 0.open api url과 서비스 키.
				String service_url = JsonParser.getProperty("monPurification_url");
				String service_key = JsonParser.getProperty("monPurification_service_key");

				// step 1.파일의 첫 행 작성
				File file = new File(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_04.dat");

				if(file.exists()){
					
					System.out.println("파일이 이미 존재하므로 이어쓰기..");
					
				} else {
				
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
						pw.write("numOfRows"); // 한 페이지 결과 수
						pw.write("|^");
						pw.write("pageNo"); // 페이지 수
						pw.write("|^");
						pw.write("totalCount"); // 데이터 총 개수
						pw.write("|^");
						pw.write("RNUM"); // 순번
						pw.write("|^");
						pw.write("FCLT_NAM"); // 정수장명
						pw.write("|^");
						pw.write("REGN_CTY"); // 관리기관명
						pw.write("|^");
						pw.write("BRTC_NM"); // 시도명
						pw.write("|^");
						pw.write("SIGNGU_NM"); // 시군구명
						pw.write("|^");
						pw.write("TELNO"); // 관리기관전화번호
						pw.write("|^");
						pw.write("COLL_DAT"); // 채수일자
						pw.write("|^");
						pw.write("INORG_NAM"); // 검사기관명
						pw.write("|^");
						pw.write("TCC"); // 일반세균
						pw.write("|^");
						pw.write("TC"); // 총대장균군
						pw.write("|^");
						pw.write("EFC"); // 대장균/분원성대장균군
						pw.write("|^");
						pw.write("PB"); // 납
						pw.write("|^");
						pw.write("FL"); // 불소
						pw.write("|^");
						pw.write("AS"); // 비소
						pw.write("|^");
						pw.write("SE"); // 셀레늄
						pw.write("|^");
						pw.write("HG"); // 수은
						pw.write("|^");
						pw.write("CN"); // 시안
						pw.write("|^");
						pw.write("CR"); // 크롬
						pw.write("|^");
						pw.write("NHN"); // 암모니아성질소
						pw.write("|^");
						pw.write("NON"); // 질산성질소
						pw.write("|^");
						pw.write("CD"); // 카드뮴
						pw.write("|^");
						pw.write("BOR"); // 붕소
						pw.write("|^");
						pw.write("BRO"); // 브롬산염
						pw.write("|^");
						pw.write("D_URNM"); // 우라늄
						pw.write("|^");
						pw.write("PHEN"); // 페놀
						pw.write("|^");
						pw.write("DIA"); // 다이아지논
						pw.write("|^");
						pw.write("PARA"); // 파라티온
						pw.write("|^");
						pw.write("PENI"); // 페니트로티온
						pw.write("|^");
						pw.write("CBR"); // 카바릴
						pw.write("|^");
						pw.write("TCE"); // 1_1_1-트리클로로에탄
						pw.write("|^");
						pw.write("TTCE"); // 테트라클로로에틸렌
						pw.write("|^");
						pw.write("TCF"); // 트리클로로에틸렌
						pw.write("|^");
						pw.write("CC"); // 사염화탄소
						pw.write("|^");
						pw.write("DDE"); // 1_1-디클로로에틸렌
						pw.write("|^");
						pw.write("DCM"); // 디클로로메탄
						pw.write("|^");
						pw.write("BZ"); // 벤젠
						pw.write("|^");
						pw.write("TOL"); // 톨루엔
						pw.write("|^");
						pw.write("EB"); // 에틸벤젠
						pw.write("|^");
						pw.write("XYL"); // 크실렌
						pw.write("|^");
						pw.write("DBCP"); // 1_2-디브로모-3-클로로프로판
						pw.write("|^");
						pw.write("DIOX"); // 1_4-다이옥산
						pw.write("|^");
						pw.write("RC"); // 잔류염소
						pw.write("|^");
						pw.write("THMS"); // 총트리할로메탄
						pw.write("|^");
						pw.write("CF"); // 클로로포름
						pw.write("|^");
						pw.write("BDCM"); // 브로모디클로로메탄
						pw.write("|^");
						pw.write("DBCM"); // 디브로모클로로메탄
						pw.write("|^");
						pw.write("CH"); // 클로랄하이드레이트
						pw.write("|^");
						pw.write("DIT"); // 디브로모아세토니트릴
						pw.write("|^");
						pw.write("TRT"); // 디클로로아세토니트릴
						pw.write("|^");
						pw.write("TRL"); // 트리클로로아세토니트릴
						pw.write("|^");
						pw.write("HAS"); // 할로아세틱에시드
						pw.write("|^");
						pw.write("FOAH"); // 포름알데히드
						pw.write("|^");
						pw.write("HR"); // 경도
						pw.write("|^");
						pw.write("KMN"); // 과망간산칼륨소비량
						pw.write("|^");
						pw.write("ODOR"); // 냄새
						pw.write("|^");
						pw.write("TW"); // 맛
						pw.write("|^");
						pw.write("CU"); // 동
						pw.write("|^");
						pw.write("CW"); // 색도
						pw.write("|^");
						pw.write("DTG"); // 세제
						pw.write("|^");
						pw.write("PH"); // 수소이온농도
						pw.write("|^");
						pw.write("ZN"); // 아연
						pw.write("|^");
						pw.write("CL"); // 염소이온
						pw.write("|^");
						pw.write("RE"); // 증발잔류물
						pw.write("|^");
						pw.write("FE"); // 철
						pw.write("|^");
						pw.write("MN"); // 망간
						pw.write("|^");
						pw.write("TU"); // 탁도
						pw.write("|^");
						pw.write("SO"); // 황산이온
						pw.write("|^");
						pw.write("AL"); // 알루미늄
						pw.write("|^");
						pw.write("UPDATE_DAT"); // 데이터기준일자
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
				String numberOfRows_str = "";
				String totalCount_str = "";

				//년과 월 입력이 필수사항. 페이지 당 데이터 개수는 100으로 고정됨(변경 불가)
				json = JsonParser.parseWatJson(service_url, service_key, args[0], args[1], String.valueOf(pageNo));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject itemsInfo = (JSONObject) body.get("itemsInfo");

					// json 값에서 가져온 전체 데이터 캐수와 한 페이지 당 개수
					int totalCount = ((Long) itemsInfo.get("totalCount")).intValue();
					int numberOfRows = ((Long) itemsInfo.get("numberOfRows")).intValue();
					totalCount_str = Integer.toString(totalCount);
					numberOfRows_str = Integer.toString(numberOfRows);

					pageCount = (totalCount / numberOfRows) + 1;

					// System.out.println("pageCount:::::" + pageCount);

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱
				
				StringBuffer resultSb = new StringBuffer("");
				
				StringBuffer RNUM = new StringBuffer(" "); // 순번
				StringBuffer FIM_FCLT_NAM = new StringBuffer(" "); // 정수장명
				StringBuffer FIM_REGN_CTY = new StringBuffer(" "); // 관리기관명
				StringBuffer BRTC_NM = new StringBuffer(" "); // 시도명
				StringBuffer SIGNGU_NM = new StringBuffer(" "); // 시군구명
				StringBuffer TELNO = new StringBuffer(" "); // 관리기관전화번호
				StringBuffer WCI_COLL_DAT = new StringBuffer(" "); // 채수일자
				StringBuffer WCI_INORG_NAM = new StringBuffer(" "); // 검사기관명
				StringBuffer TCC = new StringBuffer(" "); // 일반세균
				StringBuffer TC = new StringBuffer(" "); // 총대장균군
				StringBuffer EFC = new StringBuffer(" "); // 대장균/분원성대장균군
				StringBuffer PB = new StringBuffer(" "); // 납
				StringBuffer FL = new StringBuffer(" "); // 불소
				StringBuffer AS = new StringBuffer(" "); // 비소
				StringBuffer SE = new StringBuffer(" "); // 셀레늄
				StringBuffer HG = new StringBuffer(" "); // 수은
				StringBuffer CN = new StringBuffer(" "); // 시안
				StringBuffer CR = new StringBuffer(" "); // 크롬
				StringBuffer NHN = new StringBuffer(" "); // 암모니아성질소
				StringBuffer NON = new StringBuffer(" "); // 질산성질소
				StringBuffer CD = new StringBuffer(" "); // 카드뮴
				StringBuffer BOR = new StringBuffer(" "); // 붕소
				StringBuffer BRO = new StringBuffer(" "); // 브롬산염
				StringBuffer D_URNM = new StringBuffer(" "); // 우라늄
				StringBuffer PHEN = new StringBuffer(" "); // 페놀
				StringBuffer DIA = new StringBuffer(" "); // 다이아지논
				StringBuffer PARA = new StringBuffer(" "); // 파라티온
				StringBuffer PENI = new StringBuffer(" "); // 페니트로티온
				StringBuffer CBR = new StringBuffer(" "); // 카바릴
				StringBuffer TCE = new StringBuffer(" "); // 1_1_1-트리클로로에탄
				StringBuffer TTCE = new StringBuffer(" "); // 테트라클로로에틸렌
				StringBuffer TCF = new StringBuffer(" "); // 트리클로로에틸렌
				StringBuffer CC = new StringBuffer(" "); // 사염화탄소
				StringBuffer DDE = new StringBuffer(" "); // 1_1-디클로로에틸렌
				StringBuffer DCM = new StringBuffer(" "); // 디클로로메탄
				StringBuffer BZ = new StringBuffer(" "); // 벤젠
				StringBuffer TOL = new StringBuffer(" "); // 톨루엔
				StringBuffer EB = new StringBuffer(" "); // 에틸벤젠
				StringBuffer XYL = new StringBuffer(" "); // 크실렌
				StringBuffer DBCP = new StringBuffer(" "); // 1_2-디브로모-3-클로로프로판
				StringBuffer DIOX = new StringBuffer(" "); // 1_4-다이옥산
				StringBuffer RC = new StringBuffer(" "); // 잔류염소
				StringBuffer THMS = new StringBuffer(" "); // 총트리할로메탄
				StringBuffer CF = new StringBuffer(" "); // 클로로포름
				StringBuffer BDCM = new StringBuffer(" "); // 브로모디클로로메탄
				StringBuffer DBCM = new StringBuffer(" "); // 디브로모클로로메탄
				StringBuffer CH = new StringBuffer(" "); // 클로랄하이드레이트
				StringBuffer DIT = new StringBuffer(" "); // 디브로모아세토니트릴
				StringBuffer TRT = new StringBuffer(" "); // 디클로로아세토니트릴
				StringBuffer TRL = new StringBuffer(" "); // 트리클로로아세토니트릴
				StringBuffer HAS = new StringBuffer(" "); // 할로아세틱에시드
				StringBuffer FOAH = new StringBuffer(" "); // 포름알데히드
				StringBuffer HR = new StringBuffer(" "); // 경도
				StringBuffer KMN = new StringBuffer(" "); // 과망간산칼륨소비량
				StringBuffer ODOR = new StringBuffer(" "); // 냄새
				StringBuffer TW = new StringBuffer(" "); // 맛
				StringBuffer CU = new StringBuffer(" "); // 동
				StringBuffer CW = new StringBuffer(" "); // 색도
				StringBuffer DTG = new StringBuffer(" "); // 세제
				StringBuffer PH = new StringBuffer(" "); // 수소이온농도
				StringBuffer ZN = new StringBuffer(" "); // 아연
				StringBuffer CL = new StringBuffer(" "); // 염소이온
				StringBuffer RE = new StringBuffer(" "); // 증발잔류물
				StringBuffer FE = new StringBuffer(" "); // 철
				StringBuffer MN = new StringBuffer(" "); // 망간
				StringBuffer TU = new StringBuffer(" "); // 탁도
				StringBuffer SO = new StringBuffer(" "); // 황산이온
				StringBuffer AL = new StringBuffer(" "); // 알루미늄
				StringBuffer WCI_UPDATE_DAT = new StringBuffer(" "); // 데이터기준일자
				
				for (int i = 1; i <= pageCount; ++i) {

					json = JsonParser.parseWatJson(service_url, service_key, args[0], args[1], String.valueOf(i));

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();

						if (resultCode.equals("00")) {

							JSONArray items = (JSONArray) body.get("items");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {
									String keyname = iter.next();

									JsonParser.colWrite(RNUM, keyname, "RNUM", item);
									JsonParser.colWrite(FIM_FCLT_NAM, keyname, "FIM_FCLT_NAM", item);
									JsonParser.colWrite(FIM_REGN_CTY, keyname, "FIM_REGN_CTY", item);
									JsonParser.colWrite(BRTC_NM, keyname, "BRTC_NM", item);
									JsonParser.colWrite(SIGNGU_NM, keyname, "SIGNGU_NM", item);
									JsonParser.colWrite(TELNO, keyname, "TELNO", item);
									JsonParser.colWrite(WCI_COLL_DAT, keyname, "WCI_COLL_DAT", item);
									JsonParser.colWrite(WCI_INORG_NAM, keyname, "WCI_INORG_NAM", item);
									JsonParser.colWrite(TCC, keyname, "TCC", item);
									JsonParser.colWrite(TC, keyname, "TC", item);
									JsonParser.colWrite(EFC, keyname, "EFC", item);
									JsonParser.colWrite(PB, keyname, "PB", item);
									JsonParser.colWrite(FL, keyname, "FL", item);
									JsonParser.colWrite(AS, keyname, "AS", item);
									JsonParser.colWrite(SE, keyname, "SE", item);
									JsonParser.colWrite(HG, keyname, "HG", item);
									JsonParser.colWrite(CN, keyname, "CN", item);
									JsonParser.colWrite(CR, keyname, "CR", item);
									JsonParser.colWrite(NHN, keyname, "NHN", item);
									JsonParser.colWrite(NON, keyname, "NON", item);
									JsonParser.colWrite(CD, keyname, "CD", item);
									JsonParser.colWrite(BOR, keyname, "BOR", item);
									JsonParser.colWrite(BRO, keyname, "BRO", item);
									JsonParser.colWrite(D_URNM, keyname, "D-URNM", item);
									JsonParser.colWrite(PHEN, keyname, "PHEN", item);
									JsonParser.colWrite(DIA, keyname, "DIA", item);
									JsonParser.colWrite(PARA, keyname, "PARA", item);
									JsonParser.colWrite(PENI, keyname, "PENI", item);
									JsonParser.colWrite(CBR, keyname, "CBR", item);
									JsonParser.colWrite(TCE, keyname, "TCE", item);
									JsonParser.colWrite(TTCE, keyname, "TTCE", item);
									JsonParser.colWrite(TCF, keyname, "TCF", item);
									JsonParser.colWrite(CC, keyname, "CC", item);
									JsonParser.colWrite(DDE, keyname, "DDE", item);
									JsonParser.colWrite(DCM, keyname, "DCM", item);
									JsonParser.colWrite(BZ, keyname, "BZ", item);
									JsonParser.colWrite(TOL, keyname, "TOL", item);
									JsonParser.colWrite(EB, keyname, "EB", item);
									JsonParser.colWrite(XYL, keyname, "XYL", item);
									JsonParser.colWrite(DIOX, keyname, "DIOX", item);
									JsonParser.colWrite(RC, keyname, "RC", item);
									JsonParser.colWrite(THMS, keyname, "THMS", item);
									JsonParser.colWrite(CF, keyname, "CF", item);
									JsonParser.colWrite(BDCM, keyname, "BDCM", item);
									JsonParser.colWrite(DBCM, keyname, "DBCM", item);
									JsonParser.colWrite(CH, keyname, "CH", item);
									JsonParser.colWrite(DIT, keyname, "DIT", item);
									JsonParser.colWrite(TRT, keyname, "TRT", item);
									JsonParser.colWrite(TRL, keyname, "TRL", item);
									JsonParser.colWrite(HAS, keyname, "HAS", item);
									JsonParser.colWrite(FOAH, keyname, "FOAH", item);
									JsonParser.colWrite(HR, keyname, "HR", item);
									JsonParser.colWrite(KMN, keyname, "KMN", item);
									JsonParser.colWrite(ODOR, keyname, "ODOR", item);
									JsonParser.colWrite(TW, keyname, "TW", item);
									JsonParser.colWrite(CU, keyname, "CU", item);
									JsonParser.colWrite(CW, keyname, "CW", item);
									JsonParser.colWrite(DTG, keyname, "DTG", item);
									JsonParser.colWrite(PH, keyname, "PH", item);
									JsonParser.colWrite(ZN, keyname, "ZN", item);
									JsonParser.colWrite(CL, keyname, "CL", item);
									JsonParser.colWrite(RE, keyname, "RE", item);
									JsonParser.colWrite(FE, keyname, "FE", item);
									JsonParser.colWrite(MN, keyname, "MN", item);
									JsonParser.colWrite(TU, keyname, "TU", item);
									JsonParser.colWrite(SO, keyname, "SO", item);
									JsonParser.colWrite(AL, keyname, "AL", item);
									JsonParser.colWrite(WCI_UPDATE_DAT, keyname, "WCI_UPDATE_DAT", item);
									
								

								}
								
								//한번에 문자열 합침
								resultSb.append(numberOfRows_str);
								resultSb.append("|^");
								resultSb.append(String.valueOf(i));
								resultSb.append("|^");
								resultSb.append(totalCount_str);
								resultSb.append("|^");
								resultSb.append(RNUM);
								resultSb.append("|^");
								resultSb.append(FIM_FCLT_NAM);
								resultSb.append("|^");
								resultSb.append(FIM_REGN_CTY);
								resultSb.append("|^");
								resultSb.append(BRTC_NM);
								resultSb.append("|^");
								resultSb.append(SIGNGU_NM);
								resultSb.append("|^");
								resultSb.append(TELNO);
								resultSb.append("|^");
								resultSb.append(WCI_COLL_DAT);
								resultSb.append("|^");
								resultSb.append(WCI_INORG_NAM);
								resultSb.append("|^");
								resultSb.append(TCC);
								resultSb.append("|^");
								resultSb.append(TC);
								resultSb.append("|^");
								resultSb.append(EFC);
								resultSb.append("|^");
								resultSb.append(PB);
								resultSb.append("|^");
								resultSb.append(FL);
								resultSb.append("|^");
								resultSb.append(AS);
								resultSb.append("|^");
								resultSb.append(SE);
								resultSb.append("|^");
								resultSb.append(HG);
								resultSb.append("|^");
								resultSb.append(CN);
								resultSb.append("|^");
								resultSb.append(CR);
								resultSb.append("|^");
								resultSb.append(NHN);
								resultSb.append("|^");
								resultSb.append(NON);
								resultSb.append("|^");
								resultSb.append(CD);
								resultSb.append("|^");
								resultSb.append(BOR);
								resultSb.append("|^");
								resultSb.append(BRO);
								resultSb.append("|^");
								resultSb.append(D_URNM);
								resultSb.append("|^");
								resultSb.append(PHEN);
								resultSb.append("|^");
								resultSb.append(DIA);
								resultSb.append("|^");
								resultSb.append(PARA);
								resultSb.append("|^");
								resultSb.append(PENI);
								resultSb.append("|^");
								resultSb.append(CBR);
								resultSb.append("|^");
								resultSb.append(TCE);
								resultSb.append("|^");
								resultSb.append(TTCE);
								resultSb.append("|^");
								resultSb.append(TCF);
								resultSb.append("|^");
								resultSb.append(CC);
								resultSb.append("|^");
								resultSb.append(DDE);
								resultSb.append("|^");
								resultSb.append(DCM);
								resultSb.append("|^");
								resultSb.append(BZ);
								resultSb.append("|^");
								resultSb.append(TOL);
								resultSb.append("|^");
								resultSb.append(EB);
								resultSb.append("|^");
								resultSb.append(XYL);
								resultSb.append("|^");
								resultSb.append(DBCP);
								resultSb.append("|^");
								resultSb.append(DIOX);
								resultSb.append("|^");
								resultSb.append(RC);
								resultSb.append("|^");
								resultSb.append(THMS);
								resultSb.append("|^");
								resultSb.append(CF);
								resultSb.append("|^");
								resultSb.append(BDCM);
								resultSb.append("|^");
								resultSb.append(DBCM);
								resultSb.append("|^");
								resultSb.append(CH);
								resultSb.append("|^");
								resultSb.append(DIT);
								resultSb.append("|^");
								resultSb.append(TRT);
								resultSb.append("|^");
								resultSb.append(TRL);
								resultSb.append("|^");
								resultSb.append(HAS);
								resultSb.append("|^");
								resultSb.append(FOAH);
								resultSb.append("|^");
								resultSb.append(HR);
								resultSb.append("|^");
								resultSb.append(KMN);
								resultSb.append("|^");
								resultSb.append(ODOR);
								resultSb.append("|^");
								resultSb.append(TW);
								resultSb.append("|^");
								resultSb.append(CU);
								resultSb.append("|^");
								resultSb.append(CW);
								resultSb.append("|^");
								resultSb.append(DTG);
								resultSb.append("|^");
								resultSb.append(PH);
								resultSb.append("|^");
								resultSb.append(ZN);
								resultSb.append("|^");
								resultSb.append(CL);
								resultSb.append("|^");
								resultSb.append(RE);
								resultSb.append("|^");
								resultSb.append(FE);
								resultSb.append("|^");
								resultSb.append(MN);
								resultSb.append("|^");
								resultSb.append(TU);
								resultSb.append("|^");
								resultSb.append(SO);
								resultSb.append("|^");
								resultSb.append(AL);
								resultSb.append("|^");
								resultSb.append(WCI_UPDATE_DAT);
								resultSb.append(System.getProperty("line.separator"));

							}

						} else if (resultCode.equals("03")) {
							System.out.println("data not exist!!");
						} else {
							System.out.println("parsing error!!");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					Thread.sleep(1000);

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

				TransSftp.transSftp(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_04.dat", "WAT");
				
				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + ( end - start )/1000.0 +"초");

			} else {
				System.out.println("파라미터 형식 에러!!");
				System.exit(-1);
			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}