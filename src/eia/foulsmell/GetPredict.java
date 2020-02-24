package eia.foulsmell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import common.JsonParser;

public class GetPredict {
	
	final static Logger logger = Logger.getLogger(GetPredict.class);

	// 악취정보 서비스 -예측속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					logger.info("firstLine start..");
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("foulsmell_getPredict_url");
					String service_key = JsonParser.getProperty("foulsmell_service_key");

					// step 1.파일의 첫 행 작성
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date thisDate = new Date();
					String strDate = format.format(thisDate);

					File file = new File(JsonParser.getProperty("file_path") + "FoulsmellService_getPredict_" + strDate + ".dat");

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

					String json = "";

					json = JsonParser.parseJson(service_url, service_key, mgtNo);

					// step 3.필요에 맞게 파싱

					try {

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
						JSONObject header = (JSONObject) response.get("header");
						JSONObject body = (JSONObject) response.get("body");

						String resultCode = header.get("resultCode").toString();

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
									umenoCmpndBsmlVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoNh3Val")) {
									umenoNh3Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh4sVal")) {
									umenoCh4sVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoH2sVal")) {
									umenoH2sVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3sch3Val")) {
									umenoCh3sch3Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3ssch3Val")) {
									umenoCh3ssch3Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh33nVal")) {
									umenoCh33nVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC2h4oVal")) {
									umenoC2h4oVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC8h8Val")) {
									umenoC8h8Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC3h6oVal")) {
									umenoC3h6oVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3ch22choVal")) {
									umenoCh3ch22choVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3ch23choVal")) {
									umenoCh3ch23choVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh32chch2choVal")) {
									umenoCh32chch2choVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC7h8Val")) {
									umenoC7h8Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC8h10Val")) {
									umenoC8h10Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC4h8oVal")) {
									umenoC4h8oVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC6h12oVal")) {
									umenoC6h12oVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC6h12o2Val")) {
									umenoC6h12o2Val = body.get(keyname).toString();
								}
								if (keyname.equals("umenoC2h5coohVal")) {
									umenoC2h5coohVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3ch22choohVal")) {
									umenoCh3ch22choohVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh3ch23coohVal")) {
									umenoCh3ch23coohVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh32chch2coohVal")) {
									umenoCh32chch2coohVal = body.get(keyname).toString();
								}
								if (keyname.equals("umenoCh32chch2ohVal")) {
									umenoCh32chch2ohVal = body.get(keyname).toString();
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

							
							
							logger.info("parsing complete!");

							// step 5. 대상 서버에 sftp로 보냄

							Session session = null;
							Channel channel = null;
							ChannelSftp channelSftp = null;
							File f = new File(JsonParser.getProperty("file_path") + "FoulsmellService_getPredict_" + strDate + ".dat");
							FileInputStream in = null;

							logger.info("preparing the host information for sftp.");

							try {

								JSch jsch = new JSch();
								session = jsch.getSession("agntuser", "172.29.129.11", 28);
								session.setPassword("Dpdlwjsxm1@");

								// host 연결
								java.util.Properties config = new java.util.Properties();
								config.put("StrictHostKeyChecking", "no");
								session.setConfig(config);
								session.connect();

								// sftp 채널 연결
								channel = session.openChannel("sftp");
								channel.connect();

								// 파일 업로드 처리
								channelSftp = (ChannelSftp) channel;

								logger.info("=> Connected to host");
								in = new FileInputStream(f);

								// channelSftp.cd("/data1/if_data/WEI"); //as-is, 연계서버에
								// 떨어지는 위치
								channelSftp.cd(JsonParser.getProperty("dest_path")); // test

								String fileName = f.getName();
								channelSftp.put(in, fileName);

								logger.info("=> Uploaded : " + f.getPath());

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {

									in.close();

									// sftp 채널을 닫음
									channelSftp.exit();

									// 채널 연결 해제
									channel.disconnect();

									// 호스트 세션 종료
									session.disconnect();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							logger.info("sftp transfer complete!");

						} else if (resultCode.equals("03")) {
							logger.debug("data not exist!! mgtNo :" + mgtNo);
						} else {
							logger.debug("parsing error!! mgtNo :" + mgtNo);
						}

					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("mgtNo :" + mgtNo);
					}

				} else {
					logger.debug("파라미터 개수 에러!!");
					System.exit(-1);
				}


	}

}
