package eia.population;

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

	// 인구주거 정보 서비스 - 조사속성조회
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
					String service_url = JsonParser.getProperty("population_getIvstg_url");
					String service_key = JsonParser.getProperty("population_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_36.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("sggemdNm"); // 시군구읍면동 명칭
							pw.write("|^");
							pw.write("popltnYr"); // 년도
							pw.write("|^");
							pw.write("totPopltnMale"); // 인구수_남자
							pw.write("|^");
							pw.write("totPopltnFemale"); // 인구수_여자
							pw.write("|^");
							pw.write("totPopltn"); // 인구수_합계
							pw.write("|^");
							pw.write("totHshldCo"); // 총 가구수
							pw.write("|^");
							pw.write("totHouseCo"); // 총 주택수
							pw.write("|^");
							pw.write("houseSupplyRate"); // 주택보급율
							pw.write("|^");
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

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

						JSONArray sggemds = (JSONArray) body.get("sggemds");

						// 공통으로 갖는 사업 코드
						for (int i = 0; i < sggemds.size(); i++) {

							JSONObject sggemd = (JSONObject) sggemds.get(i);

							String sggemdNm_str = " "; // 시군구읍면동 명칭

							if (sggemd.get("sggemdNm") != null) {
								sggemdNm_str = sggemd.get("sggemdNm").toString().trim();
							} else {
								sggemdNm_str = " ";
							}

							JSONArray years = (JSONArray) sggemd.get("years");

							for (int r = 0; r < years.size(); r++) {

								JSONObject year = (JSONObject) years.get(r);

								Set<String> key = year.keySet();

								Iterator<String> iter = key.iterator();

								String popltnYr = " "; // 년도
								String totPopltnMale = " "; // 인구수_남자
								String totPopltnFemale = " "; // 인구수_여자
								String totPopltn = " "; // 인구수_합계
								String totHshldCo = " "; // 총 가구수
								String totHouseCo = " "; // 총 주택수
								String houseSupplyRate = " "; // 주택보급율

								while (iter.hasNext()) {

									String keyname = iter.next();

									if (keyname.equals("popltnYr")) {
										popltnYr = year.get(keyname).toString().trim();
									}
									if (keyname.equals("totPopltnMale")) {
										totPopltnMale = year.get(keyname).toString().trim();
									}
									if (keyname.equals("totPopltnFemale")) {
										totPopltnFemale = year.get(keyname).toString().trim();
									}
									if (keyname.equals("totPopltn")) {
										totPopltn = year.get(keyname).toString().trim();
									}
									if (keyname.equals("totHshldCo")) {
										totHshldCo = year.get(keyname).toString().trim();
									}
									if (keyname.equals("totHouseCo")) {
										totHouseCo = year.get(keyname).toString().trim();
									}
									if (keyname.equals("houseSupplyRate")) {
										houseSupplyRate = year.get(keyname).toString().trim();
									}

								}

								try {

									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(sggemdNm_str); // 시군구읍면동 명칭
									pw.write("|^");
									pw.write(popltnYr); // 년도
									pw.write("|^");
									pw.write(totPopltnMale); // 인구수_남자
									pw.write("|^");
									pw.write(totPopltnFemale); // 인구수_여자
									pw.write("|^");
									pw.write(totPopltn); // 인구수_합계
									pw.write("|^");
									pw.write(totHshldCo); // 총 가구수
									pw.write("|^");
									pw.write(totHouseCo); // 총 주택수
									pw.write("|^");
									pw.write(houseSupplyRate); // 주택보급율
									pw.write("|^");
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

							}

							// System.out.println("mgtNo :" + mgtNo);

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_36.dat", "EIA");

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
