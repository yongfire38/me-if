package eia.ecoCycle;

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

	// 친환경적자원순환 정보 서비스 - 친환경적자원순환속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("ecoCycle_getInfo_url");
			String service_key = JsonParser.getProperty("ecoCycle_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "EcoCycle_getInfo_" + strDate + ".dat");

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
				pw.write("indswsteSarea"); // 사업장배출시설계 폐기물의 처리 현황_해역배출
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
							gmemodomwsteOcty = body.get(keyname).toString();
						}
						if (keyname.equals("umemoDomwsteOcty")) {
							umemoDomwsteOcty = body.get(keyname).toString();
						}
						if (keyname.equals("domwsteOcty")) {
							domwsteOcty = body.get(keyname).toString();
						}
						if (keyname.equals("cnstrcwsteBryng")) {
							cnstrcwsteBryng = body.get(keyname).toString();
						}
						if (keyname.equals("cnstrcwsteIncnr")) {
							cnstrcwsteIncnr = body.get(keyname).toString();
						}
						if (keyname.equals("cnstrcwsteRuse")) {
							cnstrcwsteRuse = body.get(keyname).toString();
						}
						if (keyname.equals("indswsteBryng")) {
							indswsteBryng = body.get(keyname).toString();
						}
						if (keyname.equals("indswsteIncnr")) {
							indswsteIncnr = body.get(keyname).toString();
						}
						if (keyname.equals("indswsteRuse")) {
							indswsteRuse = body.get(keyname).toString();
						}
						if (keyname.equals("indswsteSarea")) {
							indswsteSarea = body.get(keyname).toString();
						}
						if (keyname.equals("appnwsteBryng")) {
							appnwsteBryng = body.get(keyname).toString();
						}
						if (keyname.equals("appnwsteIncnr")) {
							appnwsteIncnr = body.get(keyname).toString();
						}
						if (keyname.equals("appnwsteRuse")) {
							appnwsteRuse = body.get(keyname).toString();
						}
						if (keyname.equals("appnwsteEtc")) {
							appnwsteEtc = body.get(keyname).toString();
						}
						if (keyname.equals("sggemdNm")) {
							sggemdNm = body.get(keyname).toString();
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

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "파일명_" + strDate + ".dat");
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
