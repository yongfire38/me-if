package eia.noiseVibration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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

public class GetInfo {

	// 소음진동 정보 서비스 - 소음진동속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 5) {

			try {

				Thread.sleep(3000);

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("noiseVibration_getInfo_url");
					String service_key = JsonParser.getProperty("noiseVibration_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_34.dat");

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
						
						FileReader filereader = new FileReader(file);
						BufferedReader bufReader = new BufferedReader(filereader);
						
						// 내용이 없으면 헤더를 쓴다
						if ((bufReader.readLine()) == null) {

							System.out.println("빈 파일만 존재함.");

							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write("mgtNo"); // 사업 코드
								pw.write("|^");
								pw.write("ivstgSpotNm"); // 조사지점명
								pw.write("|^");
								pw.write("ivstgGb"); // 조사구분
								pw.write("|^");
								pw.write("adres"); // 주소
								pw.write("|^");
								pw.write("xcnts"); // X좌표
								pw.write("|^");
								pw.write("ydnts"); // Y좌표
								pw.write("|^");
								pw.write("ivstgOdr"); // 조사차수
								pw.write("|^");
								pw.write("ivstgBgnde"); // 조사시작일
								pw.write("|^");
								pw.write("ivstgEndde"); // 조사종료일
								pw.write("|^");
								pw.write("noiseVal"); // 소음
								pw.write("|^");
								pw.write("vibratVal"); // 진동
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("내용이 있는 파일이 이미 존재하므로 이어쓰기..");
						}

						bufReader.close();

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
									ivstg_adres_str = ivstg.get("adres").toString().trim();
								} else {
									ivstg_adres_str = " ";
								}

								if (ivstg.get("ivstgSpotNm") != null) {
									ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString().trim();
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

									Set<String> key = odr.keySet();

									Iterator<String> iter = key.iterator();

									String ivstgOdr = " "; // 조사차수
									String ivstgBgnde = " "; // 조사시작일
									String ivstgEndde = " "; // 조사종료일

									String noiseVal = " "; // 소음
									String vibratVal = " "; // 진동

									while (iter.hasNext()) {

										String keyname = iter.next();

										if (keyname.equals("ivstgOdr")) {
											ivstgOdr = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ivstgBgnde")) {
											ivstgBgnde = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("ivstgEndde")) {
											ivstgEndde = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("noiseVal")) {
											noiseVal = odr.get(keyname).toString().trim();
										}
										if (keyname.equals("vibratVal")) {
											vibratVal = odr.get(keyname).toString().trim();
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
										pw.write(ivstgOdr); // 조사차수
										pw.write("|^");
										pw.write(ivstgBgnde); // 조사시작일
										pw.write("|^");
										pw.write(ivstgEndde); // 조사종료일
										pw.write("|^");
										pw.write(noiseVal); // 소음
										pw.write("|^");
										pw.write(vibratVal); // 진동
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

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_34.dat", "EIA");

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

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
