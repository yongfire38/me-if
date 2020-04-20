package wri.dataPresent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class Mnt {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문제원현황정보
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


			try {

				Thread.sleep(3000);

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("dataPresent_mnt_url");
					String service_key = JsonParser.getProperty("dataPresent_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_07.dat");

						

					// step 2. 파싱
					String json = "";

					json = JsonParser.parseWriJson(service_url, service_key);

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
					JSONObject items = (JSONObject) body.get("items");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (!(resultCode.equals("00"))) {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
					} else if (resultCode.equals("00")) {

						JSONArray items_jsonArray = (JSONArray) items.get("item");

						for (int r = 0; r < items_jsonArray.size(); r++) {

							JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

							Set<String> key = item_obj.keySet();

							Iterator<String> iter = key.iterator();

							String competDe = " "; // 완료일
							String damFom = " "; // 형식
							String damdgr = " "; // 하천
							String damnm = " "; // 댐이름
							String dgrar = " "; // 유역면적
							String floodadjstCpcty = " "; // 홍수조절용량
							String floodseLmttWal = " "; // 상시만수위
							String fyerUswtrsuplyplanqy = " "; // 연간용수공급량
							String hg = " "; // 높이(m)
							String lowlevel = " "; // 저수위
							String lt = " "; // 길이(m)
							String nrmltAl = " "; // 정상표고
							String ordtmFwal = " "; // 홍수기제한수위
							String planFwal = " "; // 계획홍수위
							String rsvwtAr = " "; // 저수면적
							String strwrkDe = " "; // 사업기간
							String totRsvwtcpcty = " "; // 총저수용량
							String validRsvwtcpcty = " "; // 유효저수용량
							String vl = " "; // 체적
							String wollyupvlAl = " "; // 월류정표고

							while (iter.hasNext()) {

								String keyname = iter.next();

								if (keyname.equals("competDe")) {
									competDe = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("damFom")) {
									damFom = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("damdgr")) {
									damdgr = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("damnm")) {
									damnm = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("dgrar")) {
									dgrar = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("floodadjstCpcty")) {
									floodadjstCpcty = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("floodseLmttWal")) {
									floodseLmttWal = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("fyerUswtrsuplyplanqy")) {
									fyerUswtrsuplyplanqy = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("hg")) {
									hg = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("lowlevel")) {
									lowlevel = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("lt")) {
									lt = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("nrmltAl")) {
									nrmltAl = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("ordtmFwal")) {
									ordtmFwal = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("planFwal")) {
									planFwal = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("rsvwtAr")) {
									rsvwtAr = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("strwrkDe")) {
									strwrkDe = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("totRsvwtcpcty")) {
									totRsvwtcpcty = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("validRsvwtcpcty")) {
									validRsvwtcpcty = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("vl")) {
									vl = item_obj.get(keyname).toString().trim();
								}
								if (keyname.equals("wollyupvlAl")) {
									wollyupvlAl = item_obj.get(keyname).toString().trim();
								}

							}

							try {

								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(competDe); // 완료일
								pw.write("|^");
								pw.write(damFom); // 형식
								pw.write("|^");
								pw.write(damdgr); // 하천
								pw.write("|^");
								pw.write(damnm); // 댐이름
								pw.write("|^");
								pw.write(dgrar); // 유역면적
								pw.write("|^");
								pw.write(floodadjstCpcty); // 홍수조절용량
								pw.write("|^");
								pw.write(floodseLmttWal); // 상시만수위
								pw.write("|^");
								pw.write(fyerUswtrsuplyplanqy); // 연간용수공급량
								pw.write("|^");
								pw.write(hg); // 높이(m)
								pw.write("|^");
								pw.write(lowlevel); // 저수위
								pw.write("|^");
								pw.write(lt); // 길이(m)
								pw.write("|^");
								pw.write(nrmltAl); // 정상표고
								pw.write("|^");
								pw.write(ordtmFwal); // 홍수기제한수위
								pw.write("|^");
								pw.write(planFwal); // 계획홍수위
								pw.write("|^");
								pw.write(rsvwtAr); // 저수면적
								pw.write("|^");
								pw.write(strwrkDe); // 사업기간
								pw.write("|^");
								pw.write(totRsvwtcpcty); // 총저수용량
								pw.write("|^");
								pw.write(validRsvwtcpcty); // 유효저수용량
								pw.write("|^");
								pw.write(vl); // 체적
								pw.write("|^");
								pw.write(wollyupvlAl); // 월류정표고
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

					} else {
						System.out.println("parsing error!!");
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_07.dat", "WRI");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


	}

}
