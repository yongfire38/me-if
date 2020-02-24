package eia.greenhouseGas;

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

public class GetInfo {

	final static Logger logger = Logger.getLogger(GetInfo.class);

	// 온실가스 정보조회 -개요 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("greenhouseGas_getInfo_url");
			String service_key = JsonParser.getProperty("greenhouseGas_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "GreenhouseGasService_getInfo_" + strDate + ".dat");

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
							gmenoCo2Val = body.get(keyname).toString();
						}
						if (keyname.equals("gmenoCh4Val")) {
							gmenoCh4Val = body.get(keyname).toString();
						}
						if (keyname.equals("gmenoN2oVal")) {
							gmenoN2oVal = body.get(keyname).toString();
						}
						if (keyname.equals("gmenoOtrVal")) {
							gmenoOtrVal = body.get(keyname).toString();
						}
						if (keyname.equals("umenoCo2Val")) {
							umenoCo2Val = body.get(keyname).toString();
						}
						if (keyname.equals("umenoCh4Val")) {
							umenoCh4Val = body.get(keyname).toString();
						}
						if (keyname.equals("umenoN2oVal")) {
							umenoN2oVal = body.get(keyname).toString();
						}
						if (keyname.equals("umenoOtrVal")) {
							umenoOtrVal = body.get(keyname).toString();
						}
						if (keyname.equals("rm")) {
							rm = body.get(keyname).toString();
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

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "GreenhouseGasService_getInfo_" + strDate + ".dat");
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
