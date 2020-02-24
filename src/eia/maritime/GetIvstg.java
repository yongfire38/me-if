package eia.maritime;

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

	// 해양환경정보조회 서비스 - 조사속성 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("maritime_getIvstg_url");
			String service_key = JsonParser.getProperty("maritime_service_key");

			// step 1.파일의 첫 행 작성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date thisDate = new Date();
			String strDate = format.format(thisDate);

			File file = new File(JsonParser.getProperty("file_path") + "MaritimeService_getIvstg_" + strDate + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("ivstgSpotNm"); // 조사지점명
				pw.write("|^");
				pw.write("ivstgGb"); // 조사구분
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
				pw.write("wlr"); // 수층구분
				pw.write("|^");
				pw.write("qltwtrCodVal"); // 해양수질_화학적산소요구량
				pw.write("|^");
				pw.write("qltwtrTocVal"); // 해양수질_총유기탄소
				pw.write("|^");
				pw.write("qltwtrDoVal"); // 해양수질_용존산소량
				pw.write("|^");
				pw.write("qltwtrTnVal"); // 총질소
				pw.write("|^");
				pw.write("qltwtrTpVal"); // 해양수질_총인
				pw.write("|^");
				pw.write("qltwtrEvlGrad"); // 해양수질평가지수 등급
				pw.write("|^");
				pw.write("qltwtrEnvrnGrad"); // 해양수질 환경기준등급
				pw.write("|^");
				pw.write("igntLossVal"); // 강열감량
				pw.write("|^");
				pw.write("destCodVal"); // 해양저질_화학적산소요구량
				pw.write("|^");
				pw.write("destTocVal"); // 해양저질_총유기탄소
				pw.write("|^");
				pw.write("destCrVal"); // 해양저질_크롬
				pw.write("|^");
				pw.write("destZnVal"); // 해양저질_아연
				pw.write("|^");
				pw.write("destCuVal"); // 해양저질_구리
				pw.write("|^");
				pw.write("destCdVal"); // 해양저질_카드뮴
				pw.write("|^");
				pw.write("destHgVal"); // 해양저질_수은
				pw.write("|^");
				pw.write("destAsVal"); // 해양저질_비소
				pw.write("|^");
				pw.write("destPbVal"); // 해양저질_납
				pw.write("|^");
				pw.write("destNiVal"); // 해양저질_니켈
				pw.write("|^");
				pw.write("destPcbVal"); // 해양저질_총 폴리염화비페닐
				pw.write("|^");
				pw.write("destTnVal"); // 해양저질_총질소
				pw.write("|^");
				pw.write("destTpVal"); // 해양저질_총인
				pw.write("|^");
				pw.write("destFeVal"); // 해양저질_철
				pw.write("|^");
				pw.write("destMnVal"); // 해양저질_망간
				pw.write("|^");
				pw.write("destAlVal"); // 해양저질_알루미늄
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
							String ivstgSpotNm_str = " "; // 조사지점명
							String ivstg_xcnts_str = " "; // X좌표
							String ivstg_ydnts_str = " "; // Y좌표

							if (ivstg.get("adres") != null) {
								ivstg_adres_str = ivstg.get("adres").toString();
							} else {
								ivstg_adres_str = " ";
							}

							if (ivstg.get("ivstgSpotNm") != null) {
								ivstgSpotNm_str = ivstg.get("ivstgSpotNm").toString();
							} else {
								ivstgSpotNm_str = " ";
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

								String ivstgOdr_str = " "; // 조사차수
								String ivstgBgnde_str = " "; // 조사시작일
								String ivstgEndde_str = " "; // 조사종료일

								if (ivstg.get("ivstgOdr") != null) {
									ivstgOdr_str = odr.get("ivstgOdr").toString();
								} else {
									ivstgOdr_str = " ";
								}

								if (ivstg.get("ivstgBgnde") != null) {
									ivstgBgnde_str = odr.get("ivstgBgnde").toString();
								} else {
									ivstgBgnde_str = " ";
								}

								if (ivstg.get("ivstgEndde") != null) {
									ivstgEndde_str = odr.get("ivstgEndde").toString();
								} else {
									ivstgEndde_str = " ";
								}

								JSONArray wlrs = (JSONArray) odr.get("wlrs");

								for (int y = 0; y < wlrs.size(); y++) {

									JSONObject wlr_Json = (JSONObject) wlrs.get(y);

									Set<String> key = wlr_Json.keySet();

									Iterator<String> iter = key.iterator();

									String wlr = " "; // 수층구분
									String qltwtrCodVal = " "; // 해양수질_화학적산소요구량
									String qltwtrTocVal = " "; // 해양수질_총유기탄소
									String qltwtrDoVal = " "; // 해양수질_용존산소량
									String qltwtrTnVal = " "; // 총질소
									String qltwtrTpVal = " "; // 해양수질_총인
									String qltwtrEvlGrad = " "; // 해양수질평가지수 등급
									String qltwtrEnvrnGrad = " "; // 해양수질 환경기준등급
									String igntLossVal = " "; // 강열감량
									String destCodVal = " "; // 해양저질_화학적산소요구량
									String destTocVal = " "; // 해양저질_총유기탄소
									String destCrVal = " "; // 해양저질_크롬
									String destZnVal = " "; // 해양저질_아연
									String destCuVal = " "; // 해양저질_구리
									String destCdVal = " "; // 해양저질_카드뮴
									String destHgVal = " "; // 해양저질_수은
									String destAsVal = " "; // 해양저질_비소
									String destPbVal = " "; // 해양저질_납
									String destNiVal = " "; // 해양저질_니켈
									String destPcbVal = " "; // 해양저질_총 폴리염화비페닐
									String destTnVal = " "; // 해양저질_총질소
									String destTpVal = " "; // 해양저질_총인
									String destFeVal = " "; // 해양저질_철
									String destMnVal = " "; // 해양저질_망간
									String destAlVal = " "; // 해양저질_알루미늄

									while (iter.hasNext()) {

										String keyname = iter.next();

										if (keyname.equals("wlr")) {
											wlr = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrCodVal")) {
											qltwtrCodVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrTocVal")) {
											qltwtrTocVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrDoVal")) {
											qltwtrDoVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrTnVal")) {
											qltwtrTnVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrTpVal")) {
											qltwtrTpVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrEvlGrad")) {
											qltwtrEvlGrad = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("qltwtrEnvrnGrad")) {
											qltwtrEnvrnGrad = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("igntLossVal")) {
											igntLossVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destCodVal")) {
											destCodVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destTocVal")) {
											destTocVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destCrVal")) {
											destCrVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destZnVal")) {
											destZnVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destCuVal")) {
											destCuVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destCdVal")) {
											destCdVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destHgVal")) {
											destHgVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destAsVal")) {
											destAsVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destPbVal")) {
											destPbVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destNiVal")) {
											destNiVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destPcbVal")) {
											destPcbVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destTnVal")) {
											destTnVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destTpVal")) {
											destTpVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destFeVal")) {
											destFeVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destMnVal")) {
											destMnVal = wlr_Json.get(keyname).toString();
										}
										if (keyname.equals("destAlVal")) {
											destAlVal = wlr_Json.get(keyname).toString();
										}

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(mgtNo); // 사업 코드
										pw.write("|^");
										pw.write(ivstgSpotNm_str); // 조사지점명
										pw.write("|^");
										pw.write(ivstgGb_str); // 조사구분
										pw.write("|^");
										pw.write(ivstg_adres_str); // 주소
										pw.write("|^");
										pw.write(ivstg_xcnts_str); // X좌표
										pw.write("|^");
										pw.write(ivstg_ydnts_str); // Y좌표
										pw.write("|^");
										pw.write(ivstgOdr_str); // 조사차수
										pw.write("|^");
										pw.write(ivstgBgnde_str); // 조사시작일
										pw.write("|^");
										pw.write(ivstgEndde_str); // 조사종료일
										pw.write("|^");
										pw.write(wlr); // 수층구분
										pw.write("|^");
										pw.write(qltwtrCodVal); // 해양수질_화학적산소요구량
										pw.write("|^");
										pw.write(qltwtrTocVal); // 해양수질_총유기탄소
										pw.write("|^");
										pw.write(qltwtrDoVal); // 해양수질_용존산소량
										pw.write("|^");
										pw.write(qltwtrTnVal); // 총질소
										pw.write("|^");
										pw.write(qltwtrTpVal); // 해양수질_총인
										pw.write("|^");
										pw.write(qltwtrEvlGrad); // 해양수질평가지수 등급
										pw.write("|^");
										pw.write(qltwtrEnvrnGrad); // 해양수질
																	// 환경기준등급
										pw.write("|^");
										pw.write(igntLossVal); // 강열감량
										pw.write("|^");
										pw.write(destCodVal); // 해양저질_화학적산소요구량
										pw.write("|^");
										pw.write(destTocVal); // 해양저질_총유기탄소
										pw.write("|^");
										pw.write(destCrVal); // 해양저질_크롬
										pw.write("|^");
										pw.write(destZnVal); // 해양저질_아연
										pw.write("|^");
										pw.write(destCuVal); // 해양저질_구리
										pw.write("|^");
										pw.write(destCdVal); // 해양저질_카드뮴
										pw.write("|^");
										pw.write(destHgVal); // 해양저질_수은
										pw.write("|^");
										pw.write(destAsVal); // 해양저질_비소
										pw.write("|^");
										pw.write(destPbVal); // 해양저질_납
										pw.write("|^");
										pw.write(destNiVal); // 해양저질_니켈
										pw.write("|^");
										pw.write(destPcbVal); // 해양저질_총 폴리염화비페닐
										pw.write("|^");
										pw.write(destTnVal); // 해양저질_총질소
										pw.write("|^");
										pw.write(destTpVal); // 해양저질_총인
										pw.write("|^");
										pw.write(destFeVal); // 해양저질_철
										pw.write("|^");
										pw.write(destMnVal); // 해양저질_망간
										pw.write("|^");
										pw.write(destAlVal); // 해양저질_알루미늄
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}

								}

							}
						}

					}

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					Session session = null;
					Channel channel = null;
					ChannelSftp channelSftp = null;
					File f = new File(JsonParser.getProperty("file_path") + "MaritimeService_getIvstg_" + strDate + ".dat");
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
