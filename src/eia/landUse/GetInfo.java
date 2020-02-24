package eia.landUse;

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

	// 토지이용정보 서비스 - 개요속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("landuse_getInfo_url");
			String service_key = JsonParser.getProperty("landuse_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "LanduseService_getInfo_" + strDate + ".dat");

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
				pw.write("envrnprtcareaDstnc"); // 환경관련 용도지역 지구 구역과의 이격거리
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
				pw.write("envrnEvlRttdr1gar"); // 환경평가등급(개발제한구역)별 면적(1등급)
				pw.write("|^");
				pw.write("envrnEvlRttdr2gar"); // 환경평가등급(개발제한구역)별 면적(2등급)
				pw.write("|^");
				pw.write("envrnEvlRttdr3gar"); // 환경평가등급(개발제한구역)별 면적(3등급)
				pw.write("|^");
				pw.write("presvIcllnAr"); // 식생보전3등급(녹지자연도 7등급)과 급경사지(20도) 중첩 면적
				pw.write("|^");
				pw.write("presvIcllnRate"); // 식생보전3등급(녹지자연도 7등급)과 급경사지(20도) 보전
											// 비율
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
					String envrnprtcareaDstnc = " "; // 환경관련 용도지역 지구 구역과의 이격거리
					String envrnEvl1gar = " "; // 국토환경성평가등급별 면적(1등급)
					String envrnEvl2gar = " "; // 국토환경성평가등급별 면적(2등급)
					String envrnEvl3gar = " "; // 국토환경성평가등급별 면적(3등급)
					String envrnEvl4gar = " "; // 국토환경성평가등급별 면적(4등급)
					String envrnEvl5gar = " "; // 국토환경성평가등급별 면적(5등급)
					String envrnEvlRttdr1gar = " "; // 환경평가등급(개발제한구역)별 면적(1등급)
					String envrnEvlRttdr2gar = " "; // 환경평가등급(개발제한구역)별 면적(2등급)
					String envrnEvlRttdr3gar = " "; // 환경평가등급(개발제한구역)별 면적(3등급)
					String presvIcllnAr = " "; // 식생보전3등급(녹지자연도 7등급)과 급경사지(20도)
												// 중첩 면적
					String presvIcllnRate = " "; // 식생보전3등급(녹지자연도 7등급)과 급경사지(20도)
												// 보전 비율

					while (iter.hasNext()) {
						String keyname = iter.next();

						if (keyname.equals("eclgyArRate")) {
							eclgyArRate = body.get(keyname).toString();
						}
						if (keyname.equals("planEclgyArRate")) {
							planEclgyArRate = body.get(keyname).toString();
						}
						if (keyname.equals("devlopRttdrRelisAr")) {
							devlopRttdrRelisAr = body.get(keyname).toString();
						}
						if (keyname.equals("lnParkAr")) {
							lnParkAr = body.get(keyname).toString();
						}
						if (keyname.equals("lnParkRate")) {
							lnParkRate = body.get(keyname).toString();
						}
						if (keyname.equals("lnGreensAr")) {
							lnGreensAr = body.get(keyname).toString();
						}
						if (keyname.equals("lnGreensRate")) {
							lnGreensRate = body.get(keyname).toString();
						}
						if (keyname.equals("lnPgreensAr")) {
							lnPgreensAr = body.get(keyname).toString();
						}
						if (keyname.equals("lnPgreensRate")) {
							lnPgreensRate = body.get(keyname).toString();
						}
						if (keyname.equals("lnBgreensAr")) {
							lnBgreensAr = body.get(keyname).toString();
						}
						if (keyname.equals("lnBgreensRate")) {
							lnBgreensRate = body.get(keyname).toString();
						}
						if (keyname.equals("envrnprtcareaDstnc")) {
							envrnprtcareaDstnc = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvl1gar")) {
							envrnEvl1gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvl2gar")) {
							envrnEvl2gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvl3gar")) {
							envrnEvl3gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvl4gar")) {
							envrnEvl4gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvl5gar")) {
							envrnEvl5gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvlRttdr1gar")) {
							envrnEvlRttdr1gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvlRttdr2gar")) {
							envrnEvlRttdr2gar = body.get(keyname).toString();
						}
						if (keyname.equals("envrnEvlRttdr3gar")) {
							envrnEvlRttdr3gar = body.get(keyname).toString();
						}
						if (keyname.equals("presvIcllnAr")) {
							presvIcllnAr = body.get(keyname).toString();
						}
						if (keyname.equals("presvIcllnRate")) {
							presvIcllnRate = body.get(keyname).toString();
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
						pw.write(envrnprtcareaDstnc); // 환경관련 용도지역 지구 구역과의 이격거리
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
						pw.write(envrnEvlRttdr1gar); // 환경평가등급(개발제한구역)별 면적(1등급)
						pw.write("|^");
						pw.write(envrnEvlRttdr2gar); // 환경평가등급(개발제한구역)별 면적(2등급)
						pw.write("|^");
						pw.write(envrnEvlRttdr3gar); // 환경평가등급(개발제한구역)별 면적(3등급)
						pw.write("|^");
						pw.write(presvIcllnAr); // 식생보전3등급(녹지자연도 7등급)과 급경사지(20도)
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

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "LanduseService_getInfo_" + strDate + ".dat");
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
