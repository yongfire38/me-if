package eia.ecoCycle;

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

	// 친환경적자원순환 정보 서비스 - 친환경적자원순환속성조회
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
					String service_url = JsonParser.getProperty("ecoCycle_getInfo_url");
					String service_key = JsonParser.getProperty("ecoCycle_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_33.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("gmemodomwsteOcty"); // 공사시 생활폐기물 발생량
							pw.write("|^");
							pw.write("umemoDomwsteOcty"); // 운영시 생활폐기물 발생량
							pw.write("|^");
							pw.write("domwsteOcty"); // 생활폐기물 발생량
							pw.write("|^");
							pw.write("cnstrcwsteBryng"); // 건설폐기물 처리현황_매립
							pw.write("|^");
							pw.write("cnstrcwsteIncnr"); // 건설폐기물 처리현황_소각
							pw.write("|^");
							pw.write("cnstrcwsteRuse"); // 건설폐기물 처리현황_재활용
							pw.write("|^");
							pw.write("indswsteBryng"); // 사업장배출시설계 폐기물의 처리 현황_매립
							pw.write("|^");
							pw.write("indswsteIncnr"); // 사업장배출시설계 폐기물의 처리 현황_소각
							pw.write("|^");
							pw.write("indswsteRuse"); // 사업장배출시설계 폐기물의 처리 현황_재활용
							pw.write("|^");
							pw.write("indswsteSarea"); // 사업장배출시설계 폐기물의 처리
														// 현황_해역배출
							pw.write("|^");
							pw.write("appnwsteBryng"); // 사업장 지정 폐기물의 처리 현황_매립
							pw.write("|^");
							pw.write("appnwsteIncnr"); // 사업장 지정 폐기물의 처리 현황_소각
							pw.write("|^");
							pw.write("appnwsteRuse"); // 사업장 지정 폐기물의 처리 현황_재활용
							pw.write("|^");
							pw.write("appnwsteEtc"); // 사업장 지정 폐기물의 처리 현황_기타
							pw.write("|^");
							pw.write("sggemdNm"); // 시군구읍면동명칭
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

						String gmemodomwsteOcty = " "; // 공사시 생활폐기물 발생량
						String umemoDomwsteOcty = " "; // 운영시 생활폐기물 발생량
						String domwsteOcty = " "; // 생활폐기물 발생량
						String cnstrcwsteBryng = " "; // 건설폐기물 처리현황_매립
						String cnstrcwsteIncnr = " "; // 건설폐기물 처리현황_소각
						String cnstrcwsteRuse = " "; // 건설폐기물 처리현황_재활용
						String indswsteBryng = " "; // 사업장배출시설계 폐기물의 처리 현황_매립
						String indswsteIncnr = " "; // 사업장배출시설계 폐기물의 처리 현황_소각
						String indswsteRuse = " "; // 사업장배출시설계 폐기물의 처리 현황_재활용
						String indswsteSarea = " "; // 사업장배출시설계 폐기물의 처리 현황_해역배출
						String appnwsteBryng = " "; // 사업장 지정 폐기물의 처리 현황_매립
						String appnwsteIncnr = " "; // 사업장 지정 폐기물의 처리 현황_소각
						String appnwsteRuse = " "; // 사업장 지정 폐기물의 처리 현황_재활용
						String appnwsteEtc = " "; // 사업장 지정 폐기물의 처리 현황_기타
						String sggemdNm = " "; // 시군구읍면동명칭

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("gmemodomwsteOcty")) {
								gmemodomwsteOcty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umemoDomwsteOcty")) {
								umemoDomwsteOcty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("domwsteOcty")) {
								domwsteOcty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("cnstrcwsteBryng")) {
								cnstrcwsteBryng = body.get(keyname).toString().trim();
							}
							if (keyname.equals("cnstrcwsteIncnr")) {
								cnstrcwsteIncnr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("cnstrcwsteRuse")) {
								cnstrcwsteRuse = body.get(keyname).toString().trim();
							}
							if (keyname.equals("indswsteBryng")) {
								indswsteBryng = body.get(keyname).toString().trim();
							}
							if (keyname.equals("indswsteIncnr")) {
								indswsteIncnr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("indswsteRuse")) {
								indswsteRuse = body.get(keyname).toString().trim();
							}
							if (keyname.equals("indswsteSarea")) {
								indswsteSarea = body.get(keyname).toString().trim();
							}
							if (keyname.equals("appnwsteBryng")) {
								appnwsteBryng = body.get(keyname).toString().trim();
							}
							if (keyname.equals("appnwsteIncnr")) {
								appnwsteIncnr = body.get(keyname).toString().trim();
							}
							if (keyname.equals("appnwsteRuse")) {
								appnwsteRuse = body.get(keyname).toString().trim();
							}
							if (keyname.equals("appnwsteEtc")) {
								appnwsteEtc = body.get(keyname).toString().trim();
							}
							if (keyname.equals("sggemdNm")) {
								sggemdNm = body.get(keyname).toString().trim();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(gmemodomwsteOcty); // 공사시 생활폐기물 발생량
							pw.write("|^");
							pw.write(umemoDomwsteOcty); // 운영시 생활폐기물 발생량
							pw.write("|^");
							pw.write(domwsteOcty); // 생활폐기물 발생량
							pw.write("|^");
							pw.write(cnstrcwsteBryng); // 건설폐기물 처리현황_매립
							pw.write("|^");
							pw.write(cnstrcwsteIncnr); // 건설폐기물 처리현황_소각
							pw.write("|^");
							pw.write(cnstrcwsteRuse); // 건설폐기물 처리현황_재활용
							pw.write("|^");
							pw.write(indswsteBryng); // 사업장배출시설계 폐기물의 처리 현황_매립
							pw.write("|^");
							pw.write(indswsteIncnr); // 사업장배출시설계 폐기물의 처리 현황_소각
							pw.write("|^");
							pw.write(indswsteRuse); // 사업장배출시설계 폐기물의 처리 현황_재활용
							pw.write("|^");
							pw.write(indswsteSarea); // 사업장배출시설계 폐기물의 처리 현황_해역배출
							pw.write("|^");
							pw.write(appnwsteBryng); // 사업장 지정 폐기물의 처리 현황_매립
							pw.write("|^");
							pw.write(appnwsteIncnr); // 사업장 지정 폐기물의 처리 현황_소각
							pw.write("|^");
							pw.write(appnwsteRuse); // 사업장 지정 폐기물의 처리 현황_재활용
							pw.write("|^");
							pw.write(appnwsteEtc); // 사업장 지정 폐기물의 처리 현황_기타
							pw.write("|^");
							pw.write(sggemdNm); // 시군구읍면동명칭
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_33.dat", "EIA");
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
