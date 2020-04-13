package sns.naver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class Webkr {

	// 네이버검색 - 웹문서
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				if (args.length == 1 || args.length == 2) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd");

					String job_dt = dayTime.format(new Date(start));

					// 파라미터를 하나만 넣으면 검색어 (작업일은 시스템 날짜), 2개 넣으면 검색어 & 작업일(수동으로
					// 입력한)
					if (args.length == 2) {
						job_dt = args[1];
					}

					// step 0.open api url과 서비스 키.

					String service_url = JsonParser.getProperty("naver_webkr_url");
					String naver_client_id = JsonParser.getProperty("naver_client_id");
					String naver_client_secret = JsonParser.getProperty("naver_client_secret");

					// step 1.파일의 첫 행 작성

					File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_103.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {

							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
							pw.write("'");
							pw.write("job_dt"); // 시스템 일자 (파라미터로 준 경우는 입력값)
							pw.write("'");
							pw.write("|^");
							pw.write("'");
							pw.write("query"); // 검색어
							pw.write("'");
							pw.write("|^");
							pw.write("'");
							pw.write("title"); // 웹문서 제목
							pw.write("'");
							pw.write("|^");
							pw.write("'");
							pw.write("link"); // 웹문서 링크
							pw.write("'");
							pw.write("|^");
							pw.write("'");
							pw.write("description"); // 웹문서 내용
							pw.write("'");
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					String json = "";

					// step 2. 전체 페이지 파악을 위한 샘플 파싱
					json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret, args[0],
							job_dt, "1");

					int pageCount = 0;

					// 테스트 출력
					// System.out.println(json);

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					int display = ((Long) count_obj.get("display")).intValue();
					int totalCount = ((Long) count_obj.get("total")).intValue();

					pageCount = (totalCount / display) + 1;

					StringBuffer resultSb = new StringBuffer("");

					StringBuffer title = new StringBuffer(" "); // 웹문서 제목
					StringBuffer link = new StringBuffer(" "); // 웹문서 링크
					StringBuffer description = new StringBuffer(" "); // 웹문서 내용

					// step 3. 페이지 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {

						// 여기서의 i는 페이지 넘버가 아닌 조회 시작 위치값이므로 페이지 당 표시 가능 수인 30만큼
						// 증가, 1000
						// 초과값은 api가 지원하지 않음
						if (i % 30 == 1 && i <= 1000) {

							System.out.println("페이지 검색 시작 위치는:::" + i);

							json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret,
									args[0], job_dt, Integer.toString(i));

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);

							JSONArray items = (JSONArray) obj.get("items");

							for (int r = 0; r < items.size(); r++) {

								JSONObject item = (JSONObject) items.get(r);

								Set<String> key = item.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();

									JsonParser.colWrite_sns(title, keyname, "title", item);
									JsonParser.colWrite_sns(link, keyname, "link", item);
									JsonParser.colWrite_sns(description, keyname, "description", item);

								}

								// 한번에 문자열 합침
								resultSb.append("'");
								resultSb.append(job_dt); // 시스템 일자 (파라미터로 준 경우는
															// 입력값)
								resultSb.append("'");
								resultSb.append("|^");
								resultSb.append("'");
								resultSb.append(args[0]); // 검색어
								resultSb.append("'");
								resultSb.append("|^");
								resultSb.append("'");
								resultSb.append(title);
								resultSb.append("'");
								resultSb.append("|^");
								resultSb.append("'");
								resultSb.append(link);
								resultSb.append("'");
								resultSb.append("|^");
								resultSb.append("'");
								resultSb.append(description);
								resultSb.append("'");
								resultSb.append(System.getProperty("line.separator"));

							}

							System.out.println("진행도::::::" + i + "/" + pageCount);

							Thread.sleep(1000);
						}

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

					// TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_103.dat", "SNS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

				return; // 작업 성공시 리턴

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + args[0]);
			}

		}
		
		System.out.println("최대 재시도 회수를 초과하였습니다.");

		throw new Exception(); // 최대 재시도 횟수를 넘기면 직접 예외 발생

	}

}
