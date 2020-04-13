package eia.landUse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class GetInfo {

	// 토지이용정보 서비스 - 개요속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 3) {

			try {

				Thread.sleep(1000);

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("landuse_getInfo_url");
					String service_key = JsonParser.getProperty("landuse_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_17.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("eclgyArRate"); // 현재생태면적률
							pw.write("|^");
							pw.write("planEclgyArRate"); // 계획 생태면적률
							pw.write("|^");
							pw.write("devlopRttdrRelisAr"); // 개발제한구역 해제면적
							pw.write("|^");
							pw.write("lnParkAr"); // 토지이용계획_공원면적
							pw.write("|^");
							pw.write("lnParkRate"); // 토지이용계획_공원비율
							pw.write("|^");
							pw.write("lnGreensAr"); // 토지이용계획_녹지면적
							pw.write("|^");
							pw.write("lnGreensRate"); // 토지이용계획_녹지비율
							pw.write("|^");
							pw.write("lnPgreensAr"); // 토지이용계획_보존녹지면적
							pw.write("|^");
							pw.write("lnPgreensRate"); // 토지이용계획_보존녹지비율
							pw.write("|^");
							pw.write("lnBgreensAr"); // 토지이용계획_완충녹지면적
							pw.write("|^");
							pw.write("lnBgreensRate"); // 토지이용계획_완충녹지비율
							pw.write("|^");
							pw.write("envrnprtcareaDstnc"); // 환경관련 용도지역 지구 구역과의
															// 이격거리
							pw.write("|^");
							pw.write("envrnEvl1gar"); // 국토환경성평가등급별 면적(1등급)
							pw.write("|^");
							pw.write("envrnEvl2gar"); // 국토환경성평가등급별 면적(2등급)
							pw.write("|^");
							pw.write("envrnEvl3gar"); // 국토환경성평가등급별 면적(3등급)
							pw.write("|^");
							pw.write("envrnEvl4gar"); // 국토환경성평가등급별 면적(4등급)
							pw.write("|^");
							pw.write("envrnEvl5gar"); // 국토환경성평가등급별 면적(5등급)
							pw.write("|^");
							pw.write("envrnEvlRttdr1gar"); // 환경평가등급(개발제한구역)별
															// 면적(1등급)
							pw.write("|^");
							pw.write("envrnEvlRttdr2gar"); // 환경평가등급(개발제한구역)별
															// 면적(2등급)
							pw.write("|^");
							pw.write("envrnEvlRttdr3gar"); // 환경평가등급(개발제한구역)별
															// 면적(3등급)
							pw.write("|^");
							pw.write("presvIcllnAr"); // 식생보전3등급(녹지자연도 7등급)과
														// 급경사지(20도) 중첩 면적
							pw.write("|^");
							pw.write("presvIcllnRate"); // 식생보전3등급(녹지자연도 7등급)과
														// 급경사지(20도) 보전
														// 비율
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

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String eclgyArRate = " "; // 현재생태면적률
						String planEclgyArRate = " "; // 계획 생태면적률
						String devlopRttdrRelisAr = " "; // 개발제한구역 해제면적
						String lnParkAr = " "; // 토지이용계획_공원면적
						String lnParkRate = " "; // 토지이용계획_공원비율
						String lnGreensAr = " "; // 토지이용계획_녹지면적
						String lnGreensRate = " "; // 토지이용계획_녹지비율
						String lnPgreensAr = " "; // 토지이용계획_보존녹지면적
						String lnPgreensRate = " "; // 토지이용계획_보존녹지비율
						String lnBgreensAr = " "; // 토지이용계획_완충녹지면적
						String lnBgreensRate = " "; // 토지이용계획_완충녹지비율
						String envrnprtcareaDstnc = " "; // 환경관련 용도지역 지구 구역과의
															// 이격거리
						String envrnEvl1gar = " "; // 국토환경성평가등급별 면적(1등급)
						String envrnEvl2gar = " "; // 국토환경성평가등급별 면적(2등급)
						String envrnEvl3gar = " "; // 국토환경성평가등급별 면적(3등급)
						String envrnEvl4gar = " "; // 국토환경성평가등급별 면적(4등급)
						String envrnEvl5gar = " "; // 국토환경성평가등급별 면적(5등급)
						String envrnEvlRttdr1gar = " "; // 환경평가등급(개발제한구역)별
														// 면적(1등급)
						String envrnEvlRttdr2gar = " "; // 환경평가등급(개발제한구역)별
														// 면적(2등급)
						String envrnEvlRttdr3gar = " "; // 환경평가등급(개발제한구역)별
														// 면적(3등급)
						String presvIcllnAr = " "; // 식생보전3등급(녹지자연도 7등급)과
													// 급경사지(20도)
													// 중첩 면적
						String presvIcllnRate = " "; // 식생보전3등급(녹지자연도 7등급)과
														// 급경사지(20도)
														// 보전 비율

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("eclgyArRate")) {
								eclgyArRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("planEclgyArRate")) {
								planEclgyArRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("devlopRttdrRelisAr")) {
								devlopRttdrRelisAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnParkAr")) {
								lnParkAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnParkRate")) {
								lnParkRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnGreensAr")) {
								lnGreensAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnGreensRate")) {
								lnGreensRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnPgreensAr")) {
								lnPgreensAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnPgreensRate")) {
								lnPgreensRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnBgreensAr")) {
								lnBgreensAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("lnBgreensRate")) {
								lnBgreensRate = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnprtcareaDstnc")) {
								envrnprtcareaDstnc = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvl1gar")) {
								envrnEvl1gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvl2gar")) {
								envrnEvl2gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvl3gar")) {
								envrnEvl3gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvl4gar")) {
								envrnEvl4gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvl5gar")) {
								envrnEvl5gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvlRttdr1gar")) {
								envrnEvlRttdr1gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvlRttdr2gar")) {
								envrnEvlRttdr2gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("envrnEvlRttdr3gar")) {
								envrnEvlRttdr3gar = body.get(keyname).toString().trim();
							}
							if (keyname.equals("presvIcllnAr")) {
								presvIcllnAr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("presvIcllnRate")) {
								presvIcllnRate = body.get(keyname).toString().trim();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(eclgyArRate); // 현재생태면적률
							pw.write("|^");
							pw.write(planEclgyArRate); // 계획 생태면적률
							pw.write("|^");
							pw.write(devlopRttdrRelisAr); // 개발제한구역 해제면적
							pw.write("|^");
							pw.write(lnParkAr); // 토지이용계획_공원면적
							pw.write("|^");
							pw.write(lnParkRate); // 토지이용계획_공원비율
							pw.write("|^");
							pw.write(lnGreensAr); // 토지이용계획_녹지면적
							pw.write("|^");
							pw.write(lnGreensRate); // 토지이용계획_녹지비율
							pw.write("|^");
							pw.write(lnPgreensAr); // 토지이용계획_보존녹지면적
							pw.write("|^");
							pw.write(lnPgreensRate); // 토지이용계획_보존녹지비율
							pw.write("|^");
							pw.write(lnBgreensAr); // 토지이용계획_완충녹지면적
							pw.write("|^");
							pw.write(lnBgreensRate); // 토지이용계획_완충녹지비율
							pw.write("|^");
							pw.write(envrnprtcareaDstnc); // 환경관련 용도지역 지구 구역과의
															// 이격거리
							pw.write("|^");
							pw.write(envrnEvl1gar); // 국토환경성평가등급별 면적(1등급)
							pw.write("|^");
							pw.write(envrnEvl2gar); // 국토환경성평가등급별 면적(2등급)
							pw.write("|^");
							pw.write(envrnEvl3gar); // 국토환경성평가등급별 면적(3등급)
							pw.write("|^");
							pw.write(envrnEvl4gar); // 국토환경성평가등급별 면적(4등급)
							pw.write("|^");
							pw.write(envrnEvl5gar); // 국토환경성평가등급별 면적(5등급)
							pw.write("|^");
							pw.write(envrnEvlRttdr1gar); // 환경평가등급(개발제한구역)별
															// 면적(1등급)
							pw.write("|^");
							pw.write(envrnEvlRttdr2gar); // 환경평가등급(개발제한구역)별
															// 면적(2등급)
							pw.write("|^");
							pw.write(envrnEvlRttdr3gar); // 환경평가등급(개발제한구역)별
															// 면적(3등급)
							pw.write("|^");
							pw.write(presvIcllnAr); // 식생보전3등급(녹지자연도 7등급)과
													// 급경사지(20도)
													// 중첩 면적
							pw.write("|^");
							pw.write(presvIcllnRate); // 식생보전3등급(녹지자연도 7등급)과
														// 급경사지(20도) 보전 비율
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_17.dat", "EIA");

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

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
