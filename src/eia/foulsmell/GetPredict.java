package eia.foulsmell;

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


public class GetPredict {
	
	

	// 악취정보 서비스 -예측속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("foulsmell_getPredict_url");
					String service_key = JsonParser.getProperty("foulsmell_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_06.dat");

					if(file.exists()){
						
						System.out.println("파일이 이미 존재하므로 이어쓰기..");
						
					} else {
					
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("umenoCmpndBsmlVal"); // 운영시 복합악취 배출(발생)량
							pw.write("|^");
							pw.write("umenoNh3Val"); // 운영시 암모니아 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh4sVal"); // 운영시 메틸메르캅탄 배출(발생)량
							pw.write("|^");
							pw.write("umenoH2sVal"); // 운영시 황화수소 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3sch3Val"); // 운영시 다이메틸설파이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3ssch3Val"); // 운영시 다이메틸다이설파이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh33nVal"); // 운영시 트라이메틸아민 배출(발생)량
							pw.write("|^");
							pw.write("umenoC2h4oVal"); // 운영시 아세트알데하이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoC8h8Val"); // 운영시 스타이렌(스티렌) 배출(발생)량
							pw.write("|^");
							pw.write("umenoC3h6oVal"); // 운영시 프로피온알데하이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3ch22choVal"); // 운영시 뷰틸알데하이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3ch23choVal"); // 운영시 n-발레르알데하이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh32chch2choVal"); // 운영시 i-발레르알데하이드 배출(발생)량
							pw.write("|^");
							pw.write("umenoC7h8Val"); // 운영시 톨루엔 배출(발생)량
							pw.write("|^");
							pw.write("umenoC8h10Val"); // 운영시 자일렌 배출(발생)량
							pw.write("|^");
							pw.write("umenoC4h8oVal"); // 운영시 메틸에틸케톤 배출(발생)량
							pw.write("|^");
							pw.write("umenoC6h12oVal"); // 운영시 메틸아이소뷰틸케톤 배출(발생)량
							pw.write("|^");
							pw.write("umenoC6h12o2Val"); // 운영시 뷰틸아세테이트 배출(발생)량
							pw.write("|^");
							pw.write("umenoC2h5coohVal"); // 운영시 프로피온산 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3ch22choohVal"); // 운영시 n-뷰틸산 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh3ch23coohVal"); // 운영시 n-발레르산 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh32chch2coohVal"); // 운영시 i-발레르산 배출(발생)량
							pw.write("|^");
							pw.write("umenoCh32chch2ohVal"); // 운영시 i-뷰틸알코올 배출(발생)량
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

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
						JSONObject header = (JSONObject) response.get("header");
						JSONObject body = (JSONObject) response.get("body");

						String resultCode = header.get("resultCode").toString().trim();

						if (resultCode.equals("00")) {

							Set<String> key = body.keySet();

							Iterator<String> iter = key.iterator();

							String umenoCmpndBsmlVal = " "; // 운영시 복합악취 배출(발생)량
							String umenoNh3Val = " "; // 운영시 암모니아 배출(발생)량
							String umenoCh4sVal = " "; // 운영시 메틸메르캅탄 배출(발생)량
							String umenoH2sVal = " "; // 운영시 황화수소 배출(발생)량
							String umenoCh3sch3Val = " "; // 운영시 다이메틸설파이드 배출(발생)량
							String umenoCh3ssch3Val = " "; // 운영시 다이메틸다이설파이드 배출(발생)량
							String umenoCh33nVal = " "; // 운영시 트라이메틸아민 배출(발생)량
							String umenoC2h4oVal = " "; // 운영시 아세트알데하이드 배출(발생)량
							String umenoC8h8Val = " "; // 운영시 스타이렌(스티렌) 배출(발생)량
							String umenoC3h6oVal = " "; // 운영시 프로피온알데하이드 배출(발생)량
							String umenoCh3ch22choVal = " "; // 운영시 뷰틸알데하이드 배출(발생)량
							String umenoCh3ch23choVal = " "; // 운영시 n-발레르알데하이드 배출(발생)량
							String umenoCh32chch2choVal = " "; // 운영시 i-발레르알데하이드 배출(발생)량
							String umenoC7h8Val = " "; // 운영시 톨루엔 배출(발생)량
							String umenoC8h10Val = " "; // 운영시 자일렌 배출(발생)량
							String umenoC4h8oVal = " "; // 운영시 메틸에틸케톤 배출(발생)량
							String umenoC6h12oVal = " "; // 운영시 메틸아이소뷰틸케톤 배출(발생)량
							String umenoC6h12o2Val = " "; // 운영시 뷰틸아세테이트 배출(발생)량
							String umenoC2h5coohVal = " "; // 운영시 프로피온산 배출(발생)량
							String umenoCh3ch22choohVal = " "; // 운영시 n-뷰틸산 배출(발생)량
							String umenoCh3ch23coohVal = " "; // 운영시 n-발레르산 배출(발생)량
							String umenoCh32chch2coohVal = " "; // 운영시 i-발레르산 배출(발생)량
							String umenoCh32chch2ohVal = " "; // 운영시 i-뷰틸알코올 배출(발생)량

							while (iter.hasNext()) {

								String keyname = iter.next();

								if (keyname.equals("umenoCmpndBsmlVal")) {
									umenoCmpndBsmlVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoNh3Val")) {
									umenoNh3Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh4sVal")) {
									umenoCh4sVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoH2sVal")) {
									umenoH2sVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3sch3Val")) {
									umenoCh3sch3Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3ssch3Val")) {
									umenoCh3ssch3Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh33nVal")) {
									umenoCh33nVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC2h4oVal")) {
									umenoC2h4oVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC8h8Val")) {
									umenoC8h8Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC3h6oVal")) {
									umenoC3h6oVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3ch22choVal")) {
									umenoCh3ch22choVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3ch23choVal")) {
									umenoCh3ch23choVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh32chch2choVal")) {
									umenoCh32chch2choVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC7h8Val")) {
									umenoC7h8Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC8h10Val")) {
									umenoC8h10Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC4h8oVal")) {
									umenoC4h8oVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC6h12oVal")) {
									umenoC6h12oVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC6h12o2Val")) {
									umenoC6h12o2Val = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoC2h5coohVal")) {
									umenoC2h5coohVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3ch22choohVal")) {
									umenoCh3ch22choohVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh3ch23coohVal")) {
									umenoCh3ch23coohVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh32chch2coohVal")) {
									umenoCh32chch2coohVal = body.get(keyname).toString().trim();
								}
								if (keyname.equals("umenoCh32chch2ohVal")) {
									umenoCh32chch2ohVal = body.get(keyname).toString().trim();
								}
							}

							// step 4. 파일에 쓰기

							try {

								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(mgtNo); // 사업 코드
								pw.write("|^");
								pw.write(umenoCmpndBsmlVal); // 운영시 복합악취 배출(발생)량
								pw.write("|^");
								pw.write(umenoNh3Val); // 운영시 암모니아 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh4sVal); // 운영시 메틸메르캅탄 배출(발생)량
								pw.write("|^");
								pw.write(umenoH2sVal); // 운영시 황화수소 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3sch3Val); // 운영시 다이메틸설파이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3ssch3Val); // 운영시 다이메틸다이설파이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh33nVal); // 운영시 트라이메틸아민 배출(발생)량
								pw.write("|^");
								pw.write(umenoC2h4oVal); // 운영시 아세트알데하이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoC8h8Val); // 운영시 스타이렌(스티렌) 배출(발생)량
								pw.write("|^");
								pw.write(umenoC3h6oVal); // 운영시 프로피온알데하이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3ch22choVal); // 운영시 뷰틸알데하이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3ch23choVal); // 운영시 n-발레르알데하이드 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh32chch2choVal); // 운영시 i-발레르알데하이드
																// 배출(발생)량
								pw.write("|^");
								pw.write(umenoC7h8Val); // 운영시 톨루엔 배출(발생)량
								pw.write("|^");
								pw.write(umenoC8h10Val); // 운영시 자일렌 배출(발생)량
								pw.write("|^");
								pw.write(umenoC4h8oVal); // 운영시 메틸에틸케톤 배출(발생)량
								pw.write("|^");
								pw.write(umenoC6h12oVal); // 운영시 메틸아이소뷰틸케톤 배출(발생)량
								pw.write("|^");
								pw.write(umenoC6h12o2Val); // 운영시 뷰틸아세테이트 배출(발생)량
								pw.write("|^");
								pw.write(umenoC2h5coohVal); // 운영시 프로피온산 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3ch22choohVal); // 운영시 n-뷰틸산 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh3ch23coohVal); // 운영시 n-발레르산 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh32chch2coohVal); // 운영시 i-발레르산 배출(발생)량
								pw.write("|^");
								pw.write(umenoCh32chch2ohVal); // 운영시 i-뷰틸알코올 배출(발생)량
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

							
							
							System.out.println("parsing complete!");

							// step 5. 대상 서버에 sftp로 보냄

							//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_06.dat", "EIA");
							
							long end = System.currentTimeMillis();
							System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
							
						} else if (resultCode.equals("03")) {
							System.out.println("data not exist!! mgtNo :" + mgtNo);
						} else {
							System.out.println("parsing error!! mgtNo :" + mgtNo);
						}

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("mgtNo :" + mgtNo);
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


	}

}
