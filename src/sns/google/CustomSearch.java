package sns.google;

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
import common.TransSftp;

public class CustomSearch {

	// 구글 - 커스텀 검색
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		if (args.length == 1) {

			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd");

			String job_dt = dayTime.format(new Date(start));

			// step 0.open api url과 서비스 키.

			String service_url = JsonParser.getProperty("google_customsearch_url");
			String google_api_key = JsonParser.getProperty("google_api_key");
			String google_api_cx = JsonParser.getProperty("google_api_cx");

			// step 1.파일의 첫 행 작성

			File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_301.dat");

			if (file.exists()) {

				System.out.println("파일이 이미 존재하므로 이어쓰기..");

			} else {

				try {

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
					pw.write("'");
					pw.write("job_dt"); // 시스템 일자
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("query"); // 검색어
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("title"); // 문서 제목
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("snippet"); // 문서 내용
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("link"); // 링크
					pw.write("'");
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			String json = "";

			json = JsonParser.parseJson_google(service_url, google_api_key, google_api_cx, args[0], "1");

			//System.out.println("json::::" + json);

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer title = new StringBuffer(" "); // 문서 제목
			StringBuffer snippet = new StringBuffer(" "); // 문서 내용
			StringBuffer link = new StringBuffer(" "); // 링크

			// step 3. 페이지 숫자만큼 반복하면서 파싱
			// 구글의 검색 api는 한 페이지 당 건수는 10, 검색 최대값은 100건으로 고정되어 있음

			for (int i = 1; i <= 100; i++) {

				// 여기서의 i는 페이지 넘버가 아닌 조회 시작 위치값이므로 페이지 당 표시 가능 수인 10만큼 증가, 100
				// 초과값은 api가 지원하지 않음
				if (i % 10 == 1 && i <= 100) {

					System.out.println("페이지 검색 시작 위치는:::" + i);

					json = JsonParser.parseJson_google(service_url, google_api_key, google_api_cx, args[0],
							Integer.toString(i));

					try {

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
								JsonParser.colWrite_sns(snippet, keyname, "snippet", item);
								JsonParser.colWrite_sns(link, keyname, "link", item);

							}

							// 한번에 문자열 합침
							resultSb.append("'");
							resultSb.append(job_dt); // 시스템 일자
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
							resultSb.append(snippet);
							resultSb.append("'");
							resultSb.append("|^");
							resultSb.append("'");
							resultSb.append(link);
							resultSb.append("'");
							resultSb.append(System.getProperty("line.separator"));

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					System.out.println("진행도::::::" + i + "/" + 100);

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

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_301.dat", "SNS");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}