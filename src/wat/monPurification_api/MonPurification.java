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
						String json = "";

						int pageNo = 0;
						int pageCount = 0;
						String numberOfRows_str = "";
						String totalCount_str = "";

						// 년과 월 입력이 필수사항. 페이지 당 데이터 개수는 100으로 고정됨(변경 불가)
						json = JsonParser.parseWatJson(service_url, service_key, args[0], args[1],
								String.valueOf(pageNo));
						
						System.out.println(json);
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						if(json.indexOf("</") > -1){
							json ="{\"OPERATION\":\"MonPurification\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"1\",\"numberOfRows\":100},\"items\":[],\"measurementItems\":null},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
						}

						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						JSONObject count_response = (JSONObject) count_obj.get("response");

						JSONObject count_header = (JSONObject) count_response.get("header");
						String count_resultCode = count_header.get("resultCode").toString().trim();
						String count_resultMsg = count_header.get("resultMsg").toString().trim();

						if (!(count_resultCode.equals("00"))) {
							System.out.println("parsing error!!::resultCode::" + count_resultCode + "::resultMsg::"
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

							json = JsonParser.parseWatJson(service_url, service_key, args[0], args[1],
									String.valueOf(i));
							
							//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
							//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
							if(json.indexOf("</") > -1){
								json ="{\"OPERATION\":\"MonPurification\",\"response\":{\"body\":{\"itemsInfo\":{\"totalCount\":0,\"pageNo\":\"1\",\"numberOfRows\":100},\"items\":[],\"measurementItems\":null},\"header\":{\"resultMsg\":\"NODATA_ERROR\",\"resultCode\":\"03\"}}}";
							}

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							JSONObject response = (JSONObject) obj.get("response");

							JSONObject body = (JSONObject) response.get("body");
							JSONObject header = (JSONObject) response.get("header");

							String resultCode = header.get("resultCode").toString().trim();
							String resultMsg = header.get("resultMsg").toString().trim();

							if (!(resultCode.equals("00"))) {
								System.out.println(
										"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							} else if (resultCode.equals("00")) {
								
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

								JSONArray items = (JSONArray) body.get("items");

								for (int r = 0; r < items.size(); r++) {

									JSONObject item = (JSONObject) items.get(r);

									Set<String> key = item.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {
										String keyname = iter.next();
										
										if(keyname.equals("RNUM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												RNUM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												RNUM = " ";
											}
										}
										if(keyname.equals("FIM_FCLT_NAM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												FIM_FCLT_NAM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												FIM_FCLT_NAM = " ";
											}
										}
										if(keyname.equals("FIM_REGN_CTY")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												FIM_REGN_CTY = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												FIM_REGN_CTY = " ";
											}
										}
										if(keyname.equals("BRTC_NM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												BRTC_NM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												BRTC_NM = " ";
											}
										}
										if(keyname.equals("SIGNGU_NM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												SIGNGU_NM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												SIGNGU_NM = " ";
											}
										}
										if(keyname.equals("TELNO")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TELNO = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TELNO = " ";
											}
										}
										if(keyname.equals("WCI_COLL_DAT")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												WCI_COLL_DAT = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												WCI_COLL_DAT = " ";
											}
										}
										if(keyname.equals("WCI_INORG_NAM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												WCI_INORG_NAM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												WCI_INORG_NAM = " ";
											}
										}
										if(keyname.equals("TCC")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TCC = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TCC = " ";
											}
										}
										if(keyname.equals("TC")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TC = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TC = " ";
											}
										}
										if(keyname.equals("EFC")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												EFC = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												EFC = " ";
											}
										}
										if(keyname.equals("PB")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												PB = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												PB = " ";
											}
										}
										if(keyname.equals("FL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												FL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												FL = " ";
											}
										}
										if(keyname.equals("AS")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												AS = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												AS = " ";
											}
										}
										if(keyname.equals("SE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												SE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												SE = " ";
											}
										}
										if(keyname.equals("HG")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												HG = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												HG = " ";
											}
										}
										if(keyname.equals("CN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CN = " ";
											}
										}
										if(keyname.equals("CR")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CR = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CR = " ";
											}
										}
										if(keyname.equals("NHN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												NHN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												NHN = " ";
											}
										}
										if(keyname.equals("NON")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												NON = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												NON = " ";
											}
										}
										if(keyname.equals("CD")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CD = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CD = " ";
											}
										}
										if(keyname.equals("BOR")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												BOR = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												BOR = " ";
											}
										}
										if(keyname.equals("BRO")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												BRO = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												BRO = " ";
											}
										}
										if(keyname.equals("D-URNM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												D_URNM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												D_URNM = " ";
											}
										}
										if(keyname.equals("PHEN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												PHEN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												PHEN = " ";
											}
										}
										if(keyname.equals("DIA")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DIA = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DIA = " ";
											}
										}
										if(keyname.equals("PARA")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												PARA = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												PARA = " ";
											}
										}
										if(keyname.equals("PENI")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												PENI = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												PENI = " ";
											}
										}
										if(keyname.equals("CBR")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CBR = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CBR = " ";
											}
										}
										if(keyname.equals("TCE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TCE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TCE = " ";
											}
										}
										if(keyname.equals("TTCE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TTCE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TTCE = " ";
											}
										}
										if(keyname.equals("TCF")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TCF = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TCF = " ";
											}
										}
										if(keyname.equals("CC")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CC = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CC = " ";
											}
										}
										if(keyname.equals("DDE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DDE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DDE = " ";
											}
										}
										if(keyname.equals("DCM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DCM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DCM = " ";
											}
										}
										if(keyname.equals("BZ")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												BZ = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												BZ = " ";
											}
										}
										if(keyname.equals("TOL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TOL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TOL = " ";
											}
										}
										if(keyname.equals("EB")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												EB = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												EB = " ";
											}
										}
										if(keyname.equals("XYL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												XYL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												XYL = " ";
											}
										}
										if(keyname.equals("DIOX")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DIOX = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DIOX = " ";
											}
										}
										if(keyname.equals("RC")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												RC = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												RC = " ";
											}
										}
										if(keyname.equals("THMS")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												THMS = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												THMS = " ";
											}
										}
										if(keyname.equals("CF")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CF = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CF = " ";
											}
										}
										if(keyname.equals("BDCM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												BDCM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												BDCM = " ";
											}
										}
										if(keyname.equals("DBCM")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DBCM = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DBCM = " ";
											}
										}
										if(keyname.equals("CH")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CH = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CH = " ";
											}
										}
										if(keyname.equals("DIT")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DIT = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DIT = " ";
											}
										}
										if(keyname.equals("TRT")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TRT = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TRT = " ";
											}
										}
										if(keyname.equals("TRL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TRL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TRL = " ";
											}
										}
										if(keyname.equals("HAS")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												HAS = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												HAS = " ";
											}
										}
										if(keyname.equals("FOAH")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												FOAH = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												FOAH = " ";
											}
										}
										if(keyname.equals("HR")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												HR = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												HR = " ";
											}
										}
										if(keyname.equals("KMN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												KMN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												KMN = " ";
											}
										}
										if(keyname.equals("ODOR")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												ODOR = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												ODOR = " ";
											}
										}
										if(keyname.equals("TW")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TW = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TW = " ";
											}
										}
										if(keyname.equals("CU")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CU = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CU = " ";
											}
										}
										if(keyname.equals("CW")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CW = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CW = " ";
											}
										}
										if(keyname.equals("DTG")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												DTG = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												DTG = " ";
											}
										}
										if(keyname.equals("PH")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												PH = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												PH = " ";
											}
										}
										if(keyname.equals("ZN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												ZN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												ZN = " ";
											}
										}
										if(keyname.equals("CL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												CL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												CL = " ";
											}
										}
										if(keyname.equals("RE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												RE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												RE = " ";
											}
										}
										if(keyname.equals("FE")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												FE = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												FE = " ";
											}
										}
										if(keyname.equals("MN")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												MN = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												MN = " ";
											}
										}
										if(keyname.equals("TU")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												TU = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												TU = " ";
											}
										}
										if(keyname.equals("SO")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												SO = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												SO = " ";
											}
										}
										if(keyname.equals("AL")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												AL = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												AL = " ";
											}
										}
										if(keyname.equals("WCI_UPDATE_DAT")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												WCI_UPDATE_DAT = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
											}else{
												WCI_UPDATE_DAT = " ";
											}
										}

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
