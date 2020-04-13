package wat.fcltySvc_api;

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

public class GetCwpFclty {

	// 국가 상수도 정보 시스템 - 정수시설정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 서비스 키만 요구함, 실행시 추가 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("fcltySvc_getCwpFclty_url");
					String service_key = JsonParser.getProperty("fcltySvc_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_02.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("numOfRows"); // 한 페이지 결과 수
							pw.write("|^");
							pw.write("pageNo"); // 페이지 수
							pw.write("|^");
							pw.write("totalCount"); // 데이터 총 개수
							pw.write("|^");
							pw.write("RNUM"); // 순번
							pw.write("|^");
							pw.write("WILO_NAM"); // 수도구분
							pw.write("|^");
							pw.write("WBIZ_NAM"); // 수도사업자
							pw.write("|^");
							pw.write("FCLT_NAM"); // 정수장명
							pw.write("|^");
							pw.write("DTL_ADR"); // 정수장주소
							pw.write("|^");
							pw.write("TOT_VOL"); // 시설용량(㎥/일)
							pw.write("|^");
							pw.write("WSRC_NAM"); // 수원
							pw.write("|^");
							pw.write("PHONE_NUM"); // 전화번호
							pw.write("|^");
							pw.write("CWMETH_NAM"); // 정수방식
							pw.write("|^");
							pw.write("COMPL_DAT"); // 준공일
							pw.write("|^");
							pw.write("DINF_VOL"); // 소독제
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;
					String numberOfRows_str = "";
					String totalCount_str = "";

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_itemsInfo = (JSONObject) count_body.get("itemsInfo");

					JSONObject count_header = (JSONObject) count_response.get("header");
					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {
						// json 값에서 가져온 전체 데이터 캐수와 한 페이지 당 개수
						int totalCount = ((Long) count_itemsInfo.get("totalCount")).intValue();
						int numberOfRows = ((Long) count_itemsInfo.get("numberOfRows")).intValue();
						totalCount_str = Integer.toString(totalCount);
						numberOfRows_str = Integer.toString(numberOfRows);

						pageCount = (totalCount / numberOfRows) + 1;

						// System.out.println("pageCount:::::" + pageCount);
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer RNUM = new StringBuffer(" "); // 순번
					StringBuffer WILO_NAM = new StringBuffer(" "); // 수도구분
					StringBuffer WBIZ_NAM = new StringBuffer(" "); // 수도사업자
					StringBuffer FCLT_NAM = new StringBuffer(" "); // 정수장명
					StringBuffer DTL_ADR = new StringBuffer(" "); // 정수장주소
					StringBuffer TOT_VOL = new StringBuffer(" "); // 시설용량(㎥/일)
					StringBuffer WSRC_NAM = new StringBuffer(" "); // 수원
					StringBuffer PHONE_NUM = new StringBuffer(" "); // 전화번호
					StringBuffer CWMETH_NAM = new StringBuffer(" "); // 정수방식
					StringBuffer COMPL_DAT = new StringBuffer(" "); // 준공일
					StringBuffer DINF_VOL = new StringBuffer(" "); // 소독제

					for (int i = 1; i <= pageCount; ++i) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00")) {

							JSONArray items = (JSONArray) body.get("items");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(RNUM, keyname, "RNUM", item);
									JsonParser.colWrite(WILO_NAM, keyname, "WILO_NAM", item);
									JsonParser.colWrite(WBIZ_NAM, keyname, "WBIZ_NAM", item);
									JsonParser.colWrite(FCLT_NAM, keyname, "FCLT_NAM", item);
									JsonParser.colWrite(DTL_ADR, keyname, "DTL_ADR", item);
									JsonParser.colWrite(TOT_VOL, keyname, "TOT_VOL", item);
									JsonParser.colWrite(WSRC_NAM, keyname, "WSRC_NAM", item);
									JsonParser.colWrite(PHONE_NUM, keyname, "PHONE_NUM", item);
									JsonParser.colWrite(CWMETH_NAM, keyname, "CWMETH_NAM", item);
									JsonParser.colWrite(COMPL_DAT, keyname, "COMPL_DAT", item);
									JsonParser.colWrite(DINF_VOL, keyname, "DINF_VOL", item);

								}

								// 한번에 문자열 합침
								resultSb.append(numberOfRows_str);
								resultSb.append("|^");
								resultSb.append(String.valueOf(i));
								resultSb.append("|^");
								resultSb.append(totalCount_str);
								resultSb.append("|^");
								resultSb.append(RNUM);
								resultSb.append("|^");
								resultSb.append(WILO_NAM);
								resultSb.append("|^");
								resultSb.append(WBIZ_NAM);
								resultSb.append("|^");
								resultSb.append(FCLT_NAM);
								resultSb.append("|^");
								resultSb.append(DTL_ADR);
								resultSb.append("|^");
								resultSb.append(TOT_VOL);
								resultSb.append("|^");
								resultSb.append(WSRC_NAM);
								resultSb.append("|^");
								resultSb.append(PHONE_NUM);
								resultSb.append("|^");
								resultSb.append(CWMETH_NAM);
								resultSb.append("|^");
								resultSb.append(COMPL_DAT);
								resultSb.append("|^");
								resultSb.append(DINF_VOL);
								resultSb.append(System.getProperty("line.separator"));

							}

						} else {
							System.out.println("parsing error!!");
						}

						Thread.sleep(1000);

					}

					// step 4. 파일에 쓰기
					try {

						PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

						pw.write(resultSb.toString());
						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					// TransSftp.transSftp(JsonParser.getProperty("file_path") + "WAT/TIF_WAT_02.dat", "WAT");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
