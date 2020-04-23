package eia.maritime;

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

public class GetIvstg {

	// 해양환경정보조회 서비스 - 조사속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("maritime_getIvstg_url");
					String service_key = JsonParser.getProperty("maritime_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_14.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					if(json.indexOf("</") > -1){
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}

					// step 3.필요에 맞게 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {
						
						

						JSONArray ivstgGbs = (JSONArray) body.get("ivstgGbs");

						// 공통으로 갖는 사업 코드
						for (int i = 0; i < ivstgGbs.size(); i++) {

							JSONObject ivstgGb_Json = (JSONObject) ivstgGbs.get(i);

							String ivstgGb_str = ivstgGb_Json.get("ivstgGb").toString().trim(); // 조사구분

							JSONArray ivstgs = (JSONArray) ivstgGb_Json.get("ivstgs");

							for (int r = 0; r < ivstgs.size(); r++) {

								JSONObject ivstg = (JSONObject) ivstgs.get(r);

								String ivstg_adres_str = " "; // 주소
								String ivstgSpotNm_str = " "; // 조사지점명
								String ivstg_xcnts_str = " "; // X좌표
								String ivstg_ydnts_str = " "; // Y좌표

								if (ivstg.get("adres") != null) {
									ivstg_adres_str = ivstg.get("adres").toString().trim();
								} else {
									ivstg_adres_str = " ";
								}

								if (ivstg.get("ivstgSpotNm") != null) {
									ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
								} else {
									ivstgSpotNm_str = " ";
								}

								if (ivstg.get("xcnts") != null) {
									ivstg_xcnts_str = ivstg.get("xcnts").toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((ivstg_xcnts_str.indexOf("°") > -1)) {
										ivstg_xcnts_str = JsonParser.dmsTodecimal_split(ivstg_xcnts_str);
									}

								} else {
									ivstg_xcnts_str = " ";
								}

								if (ivstg.get("ydnts") != null) {
									ivstg_ydnts_str = ivstg.get("ydnts").toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((ivstg_ydnts_str.indexOf("°") > -1)) {
										ivstg_ydnts_str = JsonParser.dmsTodecimal_split(ivstg_ydnts_str);
									}

								} else {
									ivstg_ydnts_str = " ";
								}

								JSONArray odrs = (JSONArray) ivstg.get("odrs");

								for (int f = 0; f < odrs.size(); f++) {

									JSONObject odr = (JSONObject) odrs.get(f);

									String ivstgOdr_str = " "; // 조사차수
									String ivstgBgnde_str = " "; // 조사시작일
									String ivstgEndde_str = " "; // 조사종료일
									
									if (odr.get("ivstgOdr") != null) {
										ivstgOdr_str = odr.get("ivstgOdr").toString().trim();
									} else {
										ivstgOdr_str = " ";
									}

									if (odr.get("ivstgBgnde") != null) {
										ivstgBgnde_str = odr.get("ivstgBgnde").toString().trim();
									} else {
										ivstgBgnde_str = " ";
									}

									if (odr.get("ivstgEndde") != null) {
										ivstgEndde_str = odr.get("ivstgEndde").toString().trim();
									} else {
										ivstgEndde_str = " ";
									}

									JSONArray wlrs = (JSONArray) odr.get("wlrs");

									for (int y = 0; y < wlrs.size(); y++) {

										JSONObject wlr_Json = (JSONObject) wlrs.get(y);

										Set<String> key = wlr_Json.keySet();

										Iterator<String> iter = key.iterator();

										String wlr = " "; // 수층구분
										String qltwtrCodVal = " "; // 해양수질_화학적산소요구량
										String qltwtrTocVal = " "; // 해양수질_총유기탄소
										String qltwtrDoVal = " "; // 해양수질_용존산소량
										String qltwtrTnVal = " "; // 총질소
										String qltwtrTpVal = " "; // 해양수질_총인
										String qltwtrEvlGrad = " "; // 해양수질평가지수
																	// 등급
										String qltwtrEnvrnGrad = " "; // 해양수질
																		// 환경기준등급
										String igntLossVal = " "; // 강열감량
										String destCodVal = " "; // 해양저질_화학적산소요구량
										String destTocVal = " "; // 해양저질_총유기탄소
										String destCrVal = " "; // 해양저질_크롬
										String destZnVal = " "; // 해양저질_아연
										String destCuVal = " "; // 해양저질_구리
										String destCdVal = " "; // 해양저질_카드뮴
										String destHgVal = " "; // 해양저질_수은
										String destAsVal = " "; // 해양저질_비소
										String destPbVal = " "; // 해양저질_납
										String destNiVal = " "; // 해양저질_니켈
										String destPcbVal = " "; // 해양저질_총
																	// 폴리염화비페닐
										String destTnVal = " "; // 해양저질_총질소
										String destTpVal = " "; // 해양저질_총인
										String destFeVal = " "; // 해양저질_철
										String destMnVal = " "; // 해양저질_망간
										String destAlVal = " "; // 해양저질_알루미늄

										while (iter.hasNext()) {

											String keyname = iter.next();

											if (keyname.equals("wlr")) {
												wlr = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrCodVal")) {
												qltwtrCodVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrTocVal")) {
												qltwtrTocVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrDoVal")) {
												qltwtrDoVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrTnVal")) {
												qltwtrTnVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrTpVal")) {
												qltwtrTpVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrEvlGrad")) {
												qltwtrEvlGrad = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("qltwtrEnvrnGrad")) {
												qltwtrEnvrnGrad = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("igntLossVal")) {
												igntLossVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destCodVal")) {
												destCodVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destTocVal")) {
												destTocVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destCrVal")) {
												destCrVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destZnVal")) {
												destZnVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destCuVal")) {
												destCuVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destCdVal")) {
												destCdVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destHgVal")) {
												destHgVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destAsVal")) {
												destAsVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destPbVal")) {
												destPbVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destNiVal")) {
												destNiVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destPcbVal")) {
												destPcbVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destTnVal")) {
												destTnVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destTpVal")) {
												destTpVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destFeVal")) {
												destFeVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destMnVal")) {
												destMnVal = wlr_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("destAlVal")) {
												destAlVal = wlr_Json.get(keyname).toString().trim();
											}

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(mgtNo); // 사업 코드
											pw.write("|^");
											pw.write(ivstgSpotNm_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조사지점명
											pw.write("|^");
											pw.write(ivstgGb_str); // 조사구분
											pw.write("|^");
											pw.write(ivstg_adres_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 주소
											pw.write("|^");
											pw.write(ivstg_xcnts_str); // X좌표
											pw.write("|^");
											pw.write(ivstg_ydnts_str); // Y좌표
											pw.write("|^");
											pw.write(ivstgOdr_str); // 조사차수
											pw.write("|^");
											pw.write(ivstgBgnde_str); // 조사시작일
											pw.write("|^");
											pw.write(ivstgEndde_str); // 조사종료일
											pw.write("|^");
											pw.write(wlr); // 수층구분
											pw.write("|^");
											pw.write(qltwtrCodVal); // 해양수질_화학적산소요구량
											pw.write("|^");
											pw.write(qltwtrTocVal); // 해양수질_총유기탄소
											pw.write("|^");
											pw.write(qltwtrDoVal); // 해양수질_용존산소량
											pw.write("|^");
											pw.write(qltwtrTnVal); // 총질소
											pw.write("|^");
											pw.write(qltwtrTpVal); // 해양수질_총인
											pw.write("|^");
											pw.write(qltwtrEvlGrad); // 해양수질평가지수
																		// 등급
											pw.write("|^");
											pw.write(qltwtrEnvrnGrad); // 해양수질
																		// 환경기준등급
											pw.write("|^");
											pw.write(igntLossVal); // 강열감량
											pw.write("|^");
											pw.write(destCodVal); // 해양저질_화학적산소요구량
											pw.write("|^");
											pw.write(destTocVal); // 해양저질_총유기탄소
											pw.write("|^");
											pw.write(destCrVal); // 해양저질_크롬
											pw.write("|^");
											pw.write(destZnVal); // 해양저질_아연
											pw.write("|^");
											pw.write(destCuVal); // 해양저질_구리
											pw.write("|^");
											pw.write(destCdVal); // 해양저질_카드뮴
											pw.write("|^");
											pw.write(destHgVal); // 해양저질_수은
											pw.write("|^");
											pw.write(destAsVal); // 해양저질_비소
											pw.write("|^");
											pw.write(destPbVal); // 해양저질_납
											pw.write("|^");
											pw.write(destNiVal); // 해양저질_니켈
											pw.write("|^");
											pw.write(destPcbVal); // 해양저질_총
																	// 폴리염화비페닐
											pw.write("|^");
											pw.write(destTnVal); // 해양저질_총질소
											pw.write("|^");
											pw.write(destTpVal); // 해양저질_총인
											pw.write("|^");
											pw.write(destFeVal); // 해양저질_철
											pw.write("|^");
											pw.write(destMnVal); // 해양저질_망간
											pw.write("|^");
											pw.write(destAlVal); // 해양저질_알루미늄
											pw.println();
											pw.flush();
											pw.close();

										} catch (IOException e) {
											e.printStackTrace();
										}

									}

								}
							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_14.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}



	}

}
