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

import common.JsonParser;
//import common.TransSftp;

public class Mnt {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 수문제원현황정보
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		try {

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
					//String json = "";

					//json = JsonParser.parseWriJson(service_url, service_key);

					// 서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록
					// 처리
					// 원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if (json.indexOf("</") > -1) {
						System.out.print("공공데이터 서버 비 JSON 응답");
						json = "{\"response\":{\"header\":{\"resultCode\":\"03\",\"resultMsg\":\"NODATA_ERROR\"},\"body\":{\"items\":{\"item\":[]}}}}";
					}*/

					JSONObject obj = JsonParser.parseWriJson_obj(service_url, service_key);
					JSONObject response = (JSONObject) obj.get("response");

					JSONObject body = (JSONObject) response.get("body");
					JSONObject header = (JSONObject) response.get("header");
					JSONObject items = (JSONObject) body.get("items");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if((resultCode.equals("03"))){
						System.out.println("data not exist!!");
					} else if ((!(resultCode.equals("00")) && !(resultCode.equals("03")))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						//throw new Exception();
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
								
								competDe = JsonParser.colWrite_String(competDe, keyname, "competDe", item_obj);
								damFom = JsonParser.colWrite_String(damFom, keyname, "damFom", item_obj);
								damdgr = JsonParser.colWrite_String(damdgr, keyname, "damdgr", item_obj);
								damnm = JsonParser.colWrite_String(damnm, keyname, "damnm", item_obj);
								dgrar = JsonParser.colWrite_String(dgrar, keyname, "dgrar", item_obj);
								floodadjstCpcty = JsonParser.colWrite_String(floodadjstCpcty, keyname, "floodadjstCpcty", item_obj);
								floodseLmttWal = JsonParser.colWrite_String(floodseLmttWal, keyname, "floodseLmttWal", item_obj);
								fyerUswtrsuplyplanqy = JsonParser.colWrite_String(fyerUswtrsuplyplanqy, keyname, "fyerUswtrsuplyplanqy", item_obj);
								hg = JsonParser.colWrite_String(hg, keyname, "hg", item_obj);
								lowlevel = JsonParser.colWrite_String(lowlevel, keyname, "lowlevel", item_obj);
								lt = JsonParser.colWrite_String(lt, keyname, "lt", item_obj);
								nrmltAl = JsonParser.colWrite_String(nrmltAl, keyname, "nrmltAl", item_obj);
								ordtmFwal = JsonParser.colWrite_String(ordtmFwal, keyname, "ordtmFwal", item_obj);
								planFwal = JsonParser.colWrite_String(planFwal, keyname, "planFwal", item_obj);
								rsvwtAr = JsonParser.colWrite_String(rsvwtAr, keyname, "rsvwtAr", item_obj);
								strwrkDe = JsonParser.colWrite_String(strwrkDe, keyname, "strwrkDe", item_obj);
								totRsvwtcpcty = JsonParser.colWrite_String(totRsvwtcpcty, keyname, "totRsvwtcpcty", item_obj);
								validRsvwtcpcty = JsonParser.colWrite_String(validRsvwtcpcty, keyname, "validRsvwtcpcty", item_obj);
								vl = JsonParser.colWrite_String(vl, keyname, "vl", item_obj);
								wollyupvlAl = JsonParser.colWrite_String(wollyupvlAl, keyname, "wollyupvlAl", item_obj);

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

				// TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_07.dat", "WRI");

				long end = System.currentTimeMillis();
				System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

			} else {
				System.out.println("파라미터 개수 에러!!");
				System.exit(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName());
			System.exit(-1);
		}

	}

}
