package wri.droughtInfo;

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

public class Multidamdilist {

	// 수자원통합(WRIS)-운영통합시스템(댐보발전통합) - 다목적댐 가뭄정보 조회 서비스
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 5) {

			try {

				Thread.sleep(3000);

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("droughtInfo_url");
					String service_key = JsonParser.getProperty("droughtInfo_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_01.dat");

						try {
							//중복 이슈 문제로 요청 파라미터 없이 전체로 돌리는 경우는 매번 파일 새로 쓰게 수정
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));

							pw.write("seqno"); // 순번
							pw.write("|^");
							pw.write("damcdcrd"); // 댐코드
							pw.write("|^");
							pw.write("damnm"); // 댐명칭
							pw.write("|^");
							pw.write("obsymd"); // 측정일자
							pw.write("|^");
							pw.write("rsqtysum"); // 저수량합계(백만m3)
							pw.write("|^");
							pw.write("stagenow"); // 현재저수지가뭄단계명
							pw.write("|^");
							pw.write("limobsymd"); // 용수공급가능일
							pw.write("|^");
							pw.write("stage_1"); // 관심단계저수량
							pw.write("|^");
							pw.write("stage_2"); // 주의단계저수량
							pw.write("|^");
							pw.write("stage_3"); // 경계단계저수량
							pw.write("|^");
							pw.write("stage_4"); // 심각단계저수량
							pw.write("|^");
							pw.write("stage_0"); // 정상공급환원단계저수량
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 0으로 파싱
					String json = "";

					int pageNo = 0;
					int pageCount = 0;

					json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if (!(count_resultCode.equals("00"))) {
						System.out.println(
								"parsing error!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
					} else {

						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer seqno = new StringBuffer(" "); // 순번
					StringBuffer damcdcrd = new StringBuffer(" "); // 댐코드
					StringBuffer damnm = new StringBuffer(" "); // 댐명칭
					StringBuffer obsymd = new StringBuffer(" "); // 측정일자
					StringBuffer rsqtysum = new StringBuffer(" "); // 저수량합계(백만m3)
					StringBuffer stagenow = new StringBuffer(" "); // 현재저수지가뭄단계명
					StringBuffer limobsymd = new StringBuffer(" "); // 용수공급가능일
					StringBuffer stage_1 = new StringBuffer(" "); // 관심단계저수량
					StringBuffer stage_2 = new StringBuffer(" "); // 주의단계저수량
					StringBuffer stage_3 = new StringBuffer(" "); // 경계단계저수량
					StringBuffer stage_4 = new StringBuffer(" "); // 심각단계저수량
					StringBuffer stage_0 = new StringBuffer(" "); // 정상공급환원단계저수량

					for (int i = 1; i <= pageCount; ++i) {

						json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");
						JSONObject items = (JSONObject) body.get("items");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
						} else if (resultCode.equals("00")) {

							JSONArray items_jsonArray = (JSONArray) items.get("item");

							for (int r = 0; r < items_jsonArray.size(); r++) {

								JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

								Set<String> key = item_obj.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite(seqno, keyname, "seqno", item_obj);
									JsonParser.colWrite(damcdcrd, keyname, "damcdcrd", item_obj);
									JsonParser.colWrite(damnm, keyname, "damnm", item_obj);
									JsonParser.colWrite(obsymd, keyname, "obsymd", item_obj);
									JsonParser.colWrite(rsqtysum, keyname, "rsqtysum", item_obj);
									JsonParser.colWrite(stagenow, keyname, "stagenow", item_obj);
									JsonParser.colWrite(limobsymd, keyname, "limobsymd", item_obj);
									JsonParser.colWrite(stage_1, keyname, "stage_1", item_obj);
									JsonParser.colWrite(stage_2, keyname, "stage_2", item_obj);
									JsonParser.colWrite(stage_3, keyname, "stage_3", item_obj);
									JsonParser.colWrite(stage_4, keyname, "stage_4", item_obj);
									JsonParser.colWrite(stage_0, keyname, "stage_0", item_obj);

								}

								// 한번에 문자열 합침
								resultSb.append(seqno);
								resultSb.append("|^");
								resultSb.append(damcdcrd);
								resultSb.append("|^");
								resultSb.append(damnm);
								resultSb.append("|^");
								resultSb.append(obsymd);
								resultSb.append("|^");
								resultSb.append(rsqtysum);
								resultSb.append("|^");
								resultSb.append(stagenow);
								resultSb.append("|^");
								resultSb.append(limobsymd);
								resultSb.append("|^");
								resultSb.append(stage_1);
								resultSb.append("|^");
								resultSb.append(stage_2);
								resultSb.append("|^");
								resultSb.append(stage_3);
								resultSb.append("|^");
								resultSb.append(stage_4);
								resultSb.append("|^");
								resultSb.append(stage_0);
								resultSb.append(System.getProperty("line.separator"));

							}

						} else {
							System.out.println("parsing error!!");
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

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

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "WRI/TIF_WRI_01.dat", "WRI");

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
