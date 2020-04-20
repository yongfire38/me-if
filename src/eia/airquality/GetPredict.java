package eia.airquality;

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

public class GetPredict {

	// 대기질 정보 조회 - 조사속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				Thread.sleep(3000);

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("airquality_getPredict_url");
					String service_key = JsonParser.getProperty("airquality_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_03.dat");
					
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

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String greenArVal = " "; // 공원 및 녹지계획면적
						String gmenoPm10Val = " "; // 공사시 미세먼지(10) 배출량
						String gmenoPm25Val = " "; // 공사시 미세먼지(2.5) 배출량
						String gmenoNo2Val = " "; // 공사시 이산화질소 배출량
						String umenoPm10Val = " "; // 운영시 미세먼지(10) 배출량
						String umenoPm25Val = " "; // 운영시 미세먼지(2.5) 배출량
						String umenoNo2Val = " "; // 운영시 이산화질소 배출량
						String umenoSo2Val = " "; // 운영시 아황산가스 배출량
						String umenoCoVal = " "; // 운영시 일산화탄소 배출량
						String umenoHclVal = " "; // 운영시 염화수소(HCL) 배출(발생)량
						String umenoHgVal = " "; // 운영시 수은(Hg) 배출(발생)량
						String umenoNiVal = " "; // 운영시 니켈(Ni) 배출(발생)량
						String umenoCr6Val = " "; // 운영시 6가크롬(Cr6+) 배출(발생)량
						String umenoCdVal = " "; // 운영시 카드뮴(Cd) 배출(발생)량
						String umenoAsVal = " "; // 운영시 비소(As) 배출(발생)량
						String umenoBzVal = " "; // 운영시 벤젠 배출(발생)량
						String umenoHchoVal = " "; // 운영시 포름알데히드 배출(발생)량
						String umenoVcVal = " "; // 운영시 염화비닐 배출(발생)량
						String umenoDioxinVal = " "; // 운영시 다이옥신 배출(발생)량
						String umenoBeVal = " "; // 운영시 베릴륨(Be) 배출(발생)량
						String umenoEbVal = " "; // 운영시 에틸벤젠 배출(발생)량
						String umenoC6h14Val = " "; // 운영시 n-헥산 배출(발생)량
						String umenoC6h12Val = " "; // 운영시 시클로헥산 배출(발생)량
						String umenoDeVal = " "; // 운영시 1,-2디클로로에탄 배출(발생)량
						String umenoCfVal = " "; // 운영시 클로로포름 배출(발생)량
						String umenoTceVal = " "; // 운영시 트리클로로에틸렌 배출(발생)량
						String umenoCtVal = " "; // 운영시 사염화탄소 배출(발생)량
						String umenoHcnVal = " "; // 운영시 시안화수소 배출(발생)량

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("greenArVal")) {
								greenArVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoPm10Val")) {
								gmenoPm10Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoPm25Val")) {
								gmenoPm25Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoNo2Val")) {
								gmenoNo2Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoPm10Val")) {
								umenoPm10Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoPm25Val")) {
								umenoPm25Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoNo2Val")) {
								umenoNo2Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoSo2Val")) {
								umenoSo2Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCoVal")) {
								umenoCoVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoHclVal")) {
								umenoHclVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoHgVal")) {
								umenoHgVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoNiVal")) {
								umenoNiVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCr6Val")) {
								umenoCr6Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCdVal")) {
								umenoCdVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoAsVal")) {
								umenoAsVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoBzVal")) {
								umenoBzVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoHchoVal")) {
								umenoHchoVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoVcVal")) {
								umenoVcVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoDioxinVal")) {
								umenoDioxinVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoBeVal")) {
								umenoBeVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoEbVal")) {
								umenoEbVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoC6h14Val")) {
								umenoC6h14Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoC6h12Val")) {
								umenoC6h12Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoDeVal")) {
								umenoDeVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCfVal")) {
								umenoCfVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoTceVal")) {
								umenoTceVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCtVal")) {
								umenoCtVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoHcnVal")) {
								umenoHcnVal = body.get(keyname).toString().trim();
							}
						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(greenArVal); // 공원 및 녹지계획면적
							pw.write("|^");
							pw.write(gmenoPm10Val); // 공사시 미세먼지(10) 배출량
							pw.write("|^");
							pw.write(gmenoPm25Val); // 공사시 미세먼지(2.5) 배출량
							pw.write("|^");
							pw.write(gmenoNo2Val); // 공사시 이산화질소 배출량
							pw.write("|^");
							pw.write(umenoPm10Val); // 운영시 미세먼지(10) 배출량
							pw.write("|^");
							pw.write(umenoPm25Val); // 운영시 미세먼지(2.5) 배출량
							pw.write("|^");
							pw.write(umenoNo2Val); // 운영시 이산화질소 배출량
							pw.write("|^");
							pw.write(umenoSo2Val); // 운영시 아황산가스 배출량
							pw.write("|^");
							pw.write(umenoCoVal); // 운영시 일산화탄소 배출량
							pw.write("|^");
							pw.write(umenoHclVal); // 운영시 염화수소(HCL) 배출(발생)량
							pw.write("|^");
							pw.write(umenoHgVal); // 운영시 수은(Hg) 배출(발생)량
							pw.write("|^");
							pw.write(umenoNiVal); // 운영시 니켈(Ni) 배출(발생)량
							pw.write("|^");
							pw.write(umenoCr6Val); // 운영시 6가크롬(Cr6+) 배출(발생)량
							pw.write("|^");
							pw.write(umenoCdVal); // 운영시 카드뮴(Cd) 배출(발생)량
							pw.write("|^");
							pw.write(umenoAsVal); // 운영시 비소(As) 배출(발생)량
							pw.write("|^");
							pw.write(umenoBzVal); // 운영시 벤젠 배출(발생)량
							pw.write("|^");
							pw.write(umenoHchoVal); // 운영시 포름알데히드 배출(발생)량
							pw.write("|^");
							pw.write(umenoVcVal); // 운영시 염화비닐 배출(발생)량
							pw.write("|^");
							pw.write(umenoDioxinVal); // 운영시 다이옥신 배출(발생)량
							pw.write("|^");
							pw.write(umenoBeVal); // 운영시 베릴륨(Be) 배출(발생)량
							pw.write("|^");
							pw.write(umenoEbVal); // 운영시 에틸벤젠 배출(발생)량
							pw.write("|^");
							pw.write(umenoC6h14Val); // 운영시 n-헥산 배출(발생)량
							pw.write("|^");
							pw.write(umenoC6h12Val); // 운영시 시클로헥산 배출(발생)량
							pw.write("|^");
							pw.write(umenoDeVal); // 운영시 1,-2디클로로에탄 배출(발생)량
							pw.write("|^");
							pw.write(umenoCfVal); // 운영시 클로로포름 배출(발생)량
							pw.write("|^");
							pw.write(umenoTceVal); // 운영시 트리클로로에틸렌 배출(발생)량
							pw.write("|^");
							pw.write(umenoCtVal); // 운영시 사염화탄소 배출(발생)량
							pw.write("|^");
							pw.write(umenoHcnVal); // 운영시 시안화수소 배출(발생)량
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_03.dat", "EIA");
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
