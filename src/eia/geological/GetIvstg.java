package eia.geological;

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

public class GetIvstg {

	final static Logger logger = Logger.getLogger(GetIvstg.class);

	// 지형지질 정보 조회 - 조사속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("geological_getIvstg_url");
			String service_key = JsonParser.getProperty("geological_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "GeologicalService_getIvstg_" + strDate + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("ivstgGb"); // 조사구분
				pw.write("|^");
				pw.write("ivstgSpotNm"); // 조사지점명
				pw.write("|^");
				pw.write("adres"); // 주소
				pw.write("|^");
				pw.write("xcnts"); // X좌표
				pw.write("|^");
				pw.write("ydnts"); // Y좌표
				pw.write("|^");
				pw.write("ivstgOdr"); // 조사차수
				pw.write("|^");
				pw.write("ivstgBgnde"); // 조사시작일
				pw.write("|^");
				pw.write("ivstgEndde"); // 조사종료일
				pw.write("|^");
				pw.write("ugrwal12hr"); // 지하수위(12시간)
				pw.write("|^");
				pw.write("ugrwal24hr"); // 지하수위(24시간)
				pw.write("|^");
				pw.write("exprResult"); // 표준관입시험
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

					JSONArray ivstgGbs = (JSONArray) body.get("ivstgGbs");

					// 공통으로 갖는 사업 코드
					for (int i = 0; i < ivstgGbs.size(); i++) {

						JSONObject ivstgGb_Json = (JSONObject) ivstgGbs.get(i);

						String ivstgGb_str = ivstgGb_Json.get("ivstgGb").toString(); // 조사구분

						JSONArray ivstgs = (JSONArray) ivstgGb_Json.get("ivstgs");

						for (int r = 0; r < ivstgs.size(); r++) {

							JSONObject ivstg = (JSONObject) ivstgs.get(r);

							String ivstg_adres_str = " "; // 주소
							String ivstg_ivstgSpotNm_str = " "; // 조사지점명
							String ivstg_xcnts_str = " "; // X좌표
							String ivstg_ydnts_str = " "; // Y좌표

							if (ivstg.get("adres") != null) {
								ivstg_adres_str = ivstg.get("adres").toString();
							} else {
								ivstg_adres_str = " ";
							}

							if (ivstg.get("ivstgSpotNm") != null) {
								ivstg_ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString();
							} else {
								ivstg_ivstgSpotNm_str = " ";
							}

							if (ivstg.get("xcnts") != null) {
								ivstg_xcnts_str = ivstg.get("xcnts").toString();
							} else {
								ivstg_xcnts_str = " ";
							}

							if (ivstg.get("ydnts") != null) {
								ivstg_ydnts_str = ivstg.get("ydnts").toString();
							} else {
								ivstg_ydnts_str = " ";
							}

							JSONArray odrs = (JSONArray) ivstg.get("odrs");

							for (int f = 0; f < odrs.size(); f++) {

								JSONObject odr = (JSONObject) odrs.get(f);

								Set<String> key = odr.keySet();

								Iterator<String> iter = key.iterator();

								String ivstgOdr = " "; // 조사차수
								String ivstgBgnde = " "; // 조사시작일
								String ivstgEndde = " "; // 조사종료일
								String ugrwal12hr = " "; // 지하수위(12시간)
								String ugrwal24hr = " "; // 지하수위(24시간)
								String exprResult = " "; // 표준관입시험

								while (iter.hasNext()) {

									String keyname = iter.next();

									if (keyname.equals("ivstgOdr")) {
										ivstgOdr = odr.get(keyname).toString();
									}
									if (keyname.equals("ivstgBgnde")) {
										ivstgBgnde = odr.get(keyname).toString();
									}
									if (keyname.equals("ivstgEndde")) {
										ivstgEndde = odr.get(keyname).toString();
									}
									if (keyname.equals("ugrwal12hr")) {
										ugrwal12hr = odr.get(keyname).toString();
									}
									if (keyname.equals("ugrwal24hr")) {
										ugrwal24hr = odr.get(keyname).toString();
									}
									if (keyname.equals("exprResult")) {
										exprResult = odr.get(keyname).toString();
									}

								}

								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

									pw.write(mgtNo); // 사업 코드
									pw.write("|^");
									pw.write(ivstgGb_str); // 조사구분
									pw.write("|^");
									pw.write(ivstg_ivstgSpotNm_str); // 조사지점명
									pw.write("|^");
									pw.write(ivstg_adres_str); // 주소
									pw.write("|^");
									pw.write(ivstg_xcnts_str); // X좌표
									pw.write("|^");
									pw.write(ivstg_ydnts_str); // Y좌표
									pw.write("|^");
									pw.write(ivstgOdr); // 조사차수
									pw.write("|^");
									pw.write(ivstgBgnde); // 조사시작일
									pw.write("|^");
									pw.write(ivstgEndde); // 조사종료일
									pw.write("|^");
									pw.write(ugrwal12hr); // 지하수위(12시간)
									pw.write("|^");
									pw.write(ugrwal24hr); // 지하수위(24시간)
									pw.write("|^");
									pw.write(exprResult); // 표준관입시험
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

							}

						}

					}

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "GeologicalService_getIvstg_" + strDate + ".dat");
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
