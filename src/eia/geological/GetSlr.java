package eia.geological;

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

public class GetSlr {

	// 지형지질 정보 조회 - 지층속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("geological_getSlr_url");
					String service_key = JsonParser.getProperty("geological_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_28.dat");

					try {
						
						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

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

					// System.out.println("json::::"+json);

					// step 3.필요에 맞게 파싱
					
					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {

						

						JSONArray ivstgs = (JSONArray) body.get("ivstgs");

						for (int r = 0; r < ivstgs.size(); r++) {

							JSONObject ivstg = (JSONObject) ivstgs.get(r);

							String ivstg_ivstgSpotNm_str = " "; // 조사지점명
							String ivstg_dllLc_str = " "; // 시추공위치
							String ivstg_xcnts_str = " "; // X좌표
							String ivstg_ydnts_str = " "; // Y좌표

							if (ivstg.get("ivstgSpotNm") != null) {
								ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString();
							} else {
								ivstg_ivstgSpotNm_str = " ";
							}

							if (ivstg.get("dllLc") != null) {
								ivstg_dllLc_str = ivstg.get("dllLc").toString();
							} else {
								ivstg_dllLc_str = " ";
							}

							if (ivstg.get("xcnts") != null) {
								ivstg_xcnts_str = ivstg.get("xcnts").toString();

								// 시분초 표기일 경우 drgree 표기로 전환
								if ((ivstg_xcnts_str.indexOf("°") > -1)) {
									ivstg_xcnts_str = JsonParser.dmsTodecimal_split(ivstg_xcnts_str);
								}
							} else {
								ivstg_xcnts_str = " ";
							}

							if (ivstg.get("ydnts") != null) {
								ivstg_ydnts_str = ivstg.get("ydnts").toString();

								// 시분초 표기일 경우 drgree 표기로 전환
								if ((ivstg_ydnts_str.indexOf("°") > -1)) {
									ivstg_ydnts_str = JsonParser.dmsTodecimal_split(ivstg_ydnts_str);
								}
							} else {
								ivstg_ydnts_str = " ";
							}

							JSONArray slrs = (JSONArray) ivstg.get("slrs");

							for (int f = 0; f < slrs.size(); f++) {

								JSONObject slr = (JSONObject) slrs.get(f);

								String slrNm_str = " "; // 지층명

								if (slr.get("slrNm") != null) {
									slrNm_str = slr.get("slrNm").toString();
								} else {
									slrNm_str = " ";
								}

								JSONArray slrDphs = (JSONArray) slr.get("slrDphs");

								for (int i = 0; i < slrDphs.size(); i++) {

									JSONObject slrDph_json = (JSONObject) slrDphs.get(i);

									Set<String> key = slrDph_json.keySet();

									Iterator<String> iter = key.iterator();

									String slrDph = " "; // 지층심도
									String slrThick = " "; // 지층두께
									String slrCn = " "; // 지층구성상태
									String nVal = " "; // N치(TCR/RQD)

									while (iter.hasNext()) {

										String keyname = iter.next();

										if (keyname.equals("slrDph")) {
											slrDph = slrDph_json.get(keyname).toString();
										}
										if (keyname.equals("slrThick")) {
											slrThick = slrDph_json.get(keyname).toString();
										}
										if (keyname.equals("slrCn")) {
											slrCn = slrDph_json.get(keyname).toString();
										}
										if (keyname.equals("nVal")) {
											nVal = slrDph_json.get(keyname).toString();
										}

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(mgtNo); // 사업 코드
										pw.write("|^");
										pw.write(ivstg_ivstgSpotNm_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 조사지점명
										pw.write("|^");
										pw.write(ivstg_dllLc_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 시추공위치
										pw.write("|^");
										pw.write(ivstg_xcnts_str); // X좌표
										pw.write("|^");
										pw.write(ivstg_ydnts_str); // Y좌표
										pw.write("|^");
										pw.write(slrNm_str.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 지층명
										pw.write("|^");
										pw.write(slrDph.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 지층심도
										pw.write("|^");
										pw.write(slrThick.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 지층두께
										pw.write("|^");
										pw.write(slrCn.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 지층구성상태
										pw.write("|^");
										pw.write(nVal.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // N치(TCR/RQD)
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								}

							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_28.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
						throw new Exception();
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
