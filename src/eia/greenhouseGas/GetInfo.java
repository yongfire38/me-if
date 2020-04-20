package eia.greenhouseGas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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

	// 온실가스 정보조회 -개요 속성 조회
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
					String service_url = JsonParser.getProperty("greenhouseGas_getInfo_url");
					String service_key = JsonParser.getProperty("greenhouseGas_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_04.dat");

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
								pw.write("gmenoCo2Val"); // 공사시_이산화탄소
								pw.write("|^");
								pw.write("gmenoCh4Val"); // 공사시_메탄
								pw.write("|^");
								pw.write("gmenoN2oVal"); // 공사시_아산화질소
								pw.write("|^");
								pw.write("gmenoOtrVal"); // 공사시 그외 배출량
								pw.write("|^");
								pw.write("umenoCo2Val"); // 운영시_이산화탄소
								pw.write("|^");
								pw.write("umenoCh4Val"); // 운영시_메탄
								pw.write("|^");
								pw.write("umenoN2oVal"); // 운영시_아산화질소
								pw.write("|^");
								pw.write("umenoOtrVal"); // 운영시 그외 배출량
								pw.write("|^");
								pw.write("rm"); // 비고
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

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String gmenoCo2Val = " "; // 공사시_이산화탄소
						String gmenoCh4Val = " "; // 공사시_메탄
						String gmenoN2oVal = " "; // 공사시_아산화질소
						String gmenoOtrVal = " "; // 공사시 그외 배출량
						String umenoCo2Val = " "; // 운영시_이산화탄소
						String umenoCh4Val = " "; // 운영시_메탄
						String umenoN2oVal = " "; // 운영시_아산화질소
						String umenoOtrVal = " "; // 운영시 그외 배출량
						String rm = " "; // 비고

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("gmenoCo2Val")) {
								gmenoCo2Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoCh4Val")) {
								gmenoCh4Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoN2oVal")) {
								gmenoN2oVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("gmenoOtrVal")) {
								gmenoOtrVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCo2Val")) {
								umenoCo2Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoCh4Val")) {
								umenoCh4Val = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoN2oVal")) {
								umenoN2oVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("umenoOtrVal")) {
								umenoOtrVal = body.get(keyname).toString().trim();
							}
							if (keyname.equals("rm")) {
								rm = body.get(keyname).toString().trim();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업코드
							pw.write("|^");
							pw.write(gmenoCo2Val); // 공사시_이산화탄소
							pw.write("|^");
							pw.write(gmenoCh4Val); // 공사시_메탄
							pw.write("|^");
							pw.write(gmenoN2oVal); // 공사시_아산화질소
							pw.write("|^");
							pw.write(gmenoOtrVal); // 공사시 그외 배출량
							pw.write("|^");
							pw.write(umenoCo2Val); // 운영시_이산화탄소
							pw.write("|^");
							pw.write(umenoCh4Val); // 운영시_메탄
							pw.write("|^");
							pw.write(umenoN2oVal); // 운영시_아산화질소
							pw.write("|^");
							pw.write(umenoOtrVal); // 운영시 그외 배출량
							pw.write("|^");
							pw.write(rm); // 비고
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_04.dat", "EIA");

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
