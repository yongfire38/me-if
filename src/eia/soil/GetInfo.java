package eia.soil;

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

public class GetInfo {

	// 토양정보 서비스 - 조사속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("soil_getInfo_url");
					String service_key = JsonParser.getProperty("soil_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_20.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					/*if(json.indexOf("</") > -1){
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}*/

					// step 3.필요에 맞게 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
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
								String ivstg_ivstgSpotNm_str = " "; // 조사지점명
								String ivstg_xcnts_str = " "; // X좌표
								String ivstg_ydnts_str = " "; // Y좌표

								if (ivstg.get("adres") != null) {
									// 데이터 중 개행문자가 있어서 변환 처리
									ivstg_adres_str = ivstg.get("adres").toString().replaceAll("(\r\n|\r|\n|\n\r)",
											" ");
								} else {
									ivstg_adres_str = " ";
								}

								if (ivstg.get("ivstgSpotNm") != null) {
									ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString()
											.replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								} else {
									ivstg_ivstgSpotNm_str = " ";
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

									if (ivstg.get("ivstgOdr") != null) {
										ivstgOdr_str = odr.get("ivstgOdr").toString().trim();
									} else {
										ivstgOdr_str = " ";
									}

									if (ivstg.get("ivstgBgnde") != null) {
										ivstgBgnde_str = odr.get("ivstgBgnde").toString().trim();
									} else {
										ivstgBgnde_str = " ";
									}

									if (ivstg.get("ivstgEndde") != null) {
										ivstgEndde_str = odr.get("ivstgEndde").toString().trim();
									} else {
										ivstgEndde_str = " ";
									}

									JSONArray sdNms = (JSONArray) odr.get("sdNms");

									for (int y = 0; y < sdNms.size(); y++) {

										JSONObject sdNm_Json = (JSONObject) sdNms.get(y);

										Set<String> key = sdNm_Json.keySet();

										Iterator<String> iter = key.iterator();

										String sdNm = " "; // 토층구분(예 : 표토, 중토,
															// 심토 등)
										String slrDph = " "; // 토층별 심도
										String spoil = " "; // 준설토
										String cmdVal = " "; // 카드뮴
										String cuVal = " "; // 구리
										String asVal = " "; // 비소
										String hgVal = " "; // 수은
										String pbVal = " "; // 납
										String cr6Val = " "; // 6가크롬
										String znVal = " "; // 아연
										String niVal = " "; // 니켈
										String fVal = " "; // 불소
										String ugnhVal = " "; // 유기인화합물
										String pcbVal = " "; // 폴리클로리네이티드비페닐
										String cnVal = " "; // 시안
										String pnVal = " "; // 페놀
										String bzVal = " "; // 벤젠
										String tuVal = " "; // 톨루엔
										String ebVal = " "; // 에틸벤젠
										String xyVal = " "; // 크실렌
										String tphVal = " "; // 석유계총탄화수소
										String tceVal = " "; // 트리클로로에틸렌
										String pceVal = " "; // 테트라클로로에틸렌
										String bpVal = " "; // 벤조(a)피렌

										while (iter.hasNext()) {

											String keyname = iter.next();

											if (keyname.equals("sdNm")) {
												sdNm = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("slrDph")) {
												slrDph = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("spoil")) {
												spoil = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("cmdVal")) {
												cmdVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("cuVal")) {
												cuVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("asVal")) {
												asVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("hgVal")) {
												hgVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("pbVal")) {
												pbVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("cr6Val")) {
												cr6Val = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("znVal")) {
												znVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("niVal")) {
												niVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("fVal")) {
												fVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("ugnhVal")) {
												ugnhVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("pcbVal")) {
												pcbVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("cnVal")) {
												cnVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("pnVal")) {
												pnVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("bzVal")) {
												bzVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("tuVal")) {
												tuVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("ebVal")) {
												ebVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("xyVal")) {
												xyVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("tphVal")) {
												tphVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("tceVal")) {
												tceVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("pceVal")) {
												pceVal = sdNm_Json.get(keyname).toString().trim();
											}
											if (keyname.equals("bpVal")) {
												bpVal = sdNm_Json.get(keyname).toString().trim();
											}

										}

										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write(mgtNo); // 사업 코드
											pw.write("|^");
											pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
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
											pw.write(sdNm); // 토층구분(예 : 표토, 중토,
															// 심토
															// 등)
											pw.write("|^");
											pw.write(slrDph); // 토층별 심도
											pw.write("|^");
											pw.write(spoil); // 준설토
											pw.write("|^");
											pw.write(cmdVal); // 카드뮴
											pw.write("|^");
											pw.write(cuVal); // 구리
											pw.write("|^");
											pw.write(asVal); // 비소
											pw.write("|^");
											pw.write(hgVal); // 수은
											pw.write("|^");
											pw.write(pbVal); // 납
											pw.write("|^");
											pw.write(cr6Val); // 6가크롬(Cr6+)
											pw.write("|^");
											pw.write(znVal); // 아연
											pw.write("|^");
											pw.write(niVal); // 니켈
											pw.write("|^");
											pw.write(fVal); // 불소
											pw.write("|^");
											pw.write(ugnhVal); // 유기인화합물
											pw.write("|^");
											pw.write(pcbVal); // 폴리클로리네이티드비페닐
											pw.write("|^");
											pw.write(cnVal); // 시안
											pw.write("|^");
											pw.write(pnVal); // 페놀
											pw.write("|^");
											pw.write(bzVal); // 벤젠
											pw.write("|^");
											pw.write(tuVal); // 톨루엔
											pw.write("|^");
											pw.write(ebVal); // 에틸벤젠
											pw.write("|^");
											pw.write(xyVal); // 크실렌
											pw.write("|^");
											pw.write(tphVal); // 석유계총탄화수소
											pw.write("|^");
											pw.write(tceVal); // 트리클로로에틸렌
											pw.write("|^");
											pw.write(pceVal); // 테트라클로로에틸렌
											pw.write("|^");
											pw.write(bpVal); // 벤조(a)피렌
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_20.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
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
