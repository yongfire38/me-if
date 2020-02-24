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

public class GetRiver {

	final static Logger logger = Logger.getLogger(GetRiver.class);

	// 수리수문정보 서비스 - 하천사업 속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("hydraulics_getRiver_url");
			String service_key = JsonParser.getProperty("hydraulics_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "HydraulicsService_getRiver_" + strDate + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("id"); // 아이디
				pw.write("|^");
				pw.write("dtlclfc"); // 사업세분류
				pw.write("|^");
				pw.write("lc"); // 사업위치
				pw.write("|^");
				pw.write("totalEt"); // 총연장
				pw.write("|^");
				pw.write("dstrcNm"); // 지구구분
				pw.write("|^");
				pw.write("gubun1"); // 사업구분1 (사업내용구분)
				pw.write("|^");
				pw.write("gubun2"); // 사업구분2 (사업내용구분)
				pw.write("|^");
				pw.write("gubun3"); // 사업구분3 (사업내용구분)
				pw.write("|^");
				pw.write("co"); // 개수
				pw.write("|^");
				pw.write("et"); // 연장
				pw.write("|^");
				pw.write("ar"); // 면적
				pw.write("|^");
				pw.write("vl"); // 부피
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
						String dtlclfc = " "; // 사업세분류
						String lc = " "; // 사업위치
						String totalEt = " "; // 총연장
						String dstrcNm = " "; // 지구구분
						String gubun1 = " "; // 사업구분1 (사업내용구분)
						String gubun2 = " "; // 사업구분2 (사업내용구분)
						String gubun3 = " "; // 사업구분3 (사업내용구분)
						String co = " "; // 개수
						String et = " "; // 연장
						String ar = " "; // 면적
						String vl = " "; // 부피

						while (iter.hasNext()) {

							String keyname = iter.next();

							if (keyname.equals("id")) {
								id = id_Json.get(keyname).toString();
							}
							if (keyname.equals("dtlclfc")) {
								dtlclfc = id_Json.get(keyname).toString();
							}
							if (keyname.equals("lc")) {
								lc = id_Json.get(keyname).toString();
							}
							if (keyname.equals("totalEt")) {
								totalEt = id_Json.get(keyname).toString();
							}
							if (keyname.equals("dstrcNm")) {
								dstrcNm = id_Json.get(keyname).toString();
							}
							if (keyname.equals("gubun1")) {
								gubun1 = id_Json.get(keyname).toString();
							}
							if (keyname.equals("gubun2")) {
								gubun2 = id_Json.get(keyname).toString();
							}
							if (keyname.equals("gubun3")) {
								gubun3 = id_Json.get(keyname).toString();
							}
							if (keyname.equals("co")) {
								co = id_Json.get(keyname).toString();
							}
							if (keyname.equals("et")) {
								et = id_Json.get(keyname).toString();
							}
							if (keyname.equals("ar")) {
								ar = id_Json.get(keyname).toString();
							}
							if (keyname.equals("vl")) {
								vl = id_Json.get(keyname).toString();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(id); // 아이디
							pw.write("|^");
							pw.write(dtlclfc); // 사업세분류
							pw.write("|^");
							pw.write(lc); // 사업위치
							pw.write("|^");
							pw.write(totalEt); // 총연장
							pw.write("|^");
							pw.write(dstrcNm); // 지구구분
							pw.write("|^");
							pw.write(gubun1); // 사업구분1 (사업내용구분)
							pw.write("|^");
							pw.write(gubun2); // 사업구분2 (사업내용구분)
							pw.write("|^");
							pw.write(gubun3); // 사업구분3 (사업내용구분)
							pw.write("|^");
							pw.write(co); // 개수
							pw.write("|^");
							pw.write(et); // 연장
							pw.write("|^");
							pw.write(ar); // 면적
							pw.write("|^");
							pw.write(vl); // 부피
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
					File f = new File(JsonParser.getProperty("file_path") + "HydraulicsService_getRiver_" + strDate + ".dat");
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
