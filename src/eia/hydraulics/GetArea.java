package eia.hydraulics;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import common.JsonParser;

public class GetArea {

	final static Logger logger = Logger.getLogger(GetArea.class);

	// 수리수문정보 서비스 - 면적사업 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("hydraulics_getArea_url");
			String service_key = JsonParser.getProperty("hydraulics_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "HydraulicsService_getArea_" + strDate + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("id"); // 아이디
				pw.write("|^");
				pw.write("dgrNm"); // 소유역
				pw.write("|^");
				pw.write("ar"); // 면적
				pw.write("|^");
				pw.write("bfeFloodqy"); // 홍수유출량 개발전
				pw.write("|^");
				pw.write("midFloodqy"); // 홍수유출량 개발중
				pw.write("|^");
				pw.write("aftFloodqy"); // 홍수유출량 개발후
				pw.write("|^");
				pw.write("floodqyFq"); // 홍수유출량 산정빈도
				pw.write("|^");
				pw.write("nowPktmFloodqy"); // 영구저류지의 홍수량 저감효과 개발전 첨두홍수량
				pw.write("|^");
				pw.write("bfefcltyPktmFloodqy"); // 영구저류지의 홍수량 저감효과 개발후 저류지 설치 전
													// 첨두홍수량
				pw.write("|^");
				pw.write("aftfcltyPktmFloodqy"); // 영구저류지의 홍수량 저감효과 개발후 저류지 설치 후
													// 첨두홍수량
				pw.write("|^");
				pw.write("pktmFloodqyFq"); // 산정빈도
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

					JSONArray ids = (JSONArray) body.get("ids");

					// id 배열 안
					for (int i = 0; i < ids.size(); i++) {

						JSONObject id_Json = (JSONObject) ids.get(i);

						Set<String> key = id_Json.keySet();

						Iterator<String> iter = key.iterator();

						String id = " "; // 아이디
						String dgrNm = " "; // 소유역
						String ar = " "; // 면적
						String bfeFloodqy = " "; // 홍수유출량 개발전
						String midFloodqy = " "; // 홍수유출량 개발중
						String aftFloodqy = " "; // 홍수유출량 개발후
						String floodqyFq = " "; // 홍수유출량 산정빈도
						String nowPktmFloodqy = " "; // 영구저류지의 홍수량 저감효과 개발전 첨두홍수량
						String bfefcltyPktmFloodqy = " "; // 영구저류지의 홍수량 저감효과 개발후
															// 저류지 설치 전 첨두홍수량
						String aftfcltyPktmFloodqy = " "; // 영구저류지의 홍수량 저감효과 개발후
															// 저류지 설치 후 첨두홍수량
						String pktmFloodqyFq = " "; // 산정빈도

						while (iter.hasNext()) {

							String keyname = iter.next();

							if (keyname.equals("id")) {
								id = id_Json.get(keyname).toString();
							}
							if (keyname.equals("dgrNm")) {
								dgrNm = id_Json.get(keyname).toString();
							}
							if (keyname.equals("ar")) {
								ar = id_Json.get(keyname).toString();
							}
							if (keyname.equals("bfeFloodqy")) {
								bfeFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("midFloodqy")) {
								midFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("aftFloodqy")) {
								aftFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("floodqyFq")) {
								floodqyFq = id_Json.get(keyname).toString();
							}
							if (keyname.equals("nowPktmFloodqy")) {
								nowPktmFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("bfefcltyPktmFloodqy")) {
								bfefcltyPktmFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("aftfcltyPktmFloodqy")) {
								aftfcltyPktmFloodqy = id_Json.get(keyname).toString();
							}
							if (keyname.equals("pktmFloodqyFq")) {
								pktmFloodqyFq = id_Json.get(keyname).toString();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(id); // 아이디
							pw.write("|^");
							pw.write(dgrNm); // 소유역
							pw.write("|^");
							pw.write(ar); // 면적
							pw.write("|^");
							pw.write(bfeFloodqy); // 홍수유출량 개발전
							pw.write("|^");
							pw.write(midFloodqy); // 홍수유출량 개발중
							pw.write("|^");
							pw.write(aftFloodqy); // 홍수유출량 개발후
							pw.write("|^");
							pw.write(floodqyFq); // 홍수유출량 산정빈도
							pw.write("|^");
							pw.write(nowPktmFloodqy); // 영구저류지의 홍수량 저감효과 개발전
														// 첨두홍수량
							pw.write("|^");
							pw.write(bfefcltyPktmFloodqy); // 영구저류지의 홍수량 저감효과
															// 개발후 저류지 설치 전
															// 첨두홍수량
							pw.write("|^");
							pw.write(aftfcltyPktmFloodqy); // 영구저류지의 홍수량 저감효과
															// 개발후 저류지 설치 후
															// 첨두홍수량
							pw.write("|^");
							pw.write(pktmFloodqyFq); // 산정빈도
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "HydraulicsService_getArea_" + strDate + ".dat");
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
