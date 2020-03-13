package sns.daum;

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

public class Cafe {

	// 다음검색 - 블로그
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		if (args.length == 1) {
			
			System.out.println("firstLine start..");
			long start = System.currentTimeMillis(); // 시작시간
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd");

			String job_dt = dayTime.format(new Date(start));

			// step 0.open api url과 서비스 키.

			String service_url = JsonParser.getProperty("daum_cafe_url");
			String daum_api_key = JsonParser.getProperty("daum_api_key");

			// step 1.파일의 첫 행 작성

			File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_202.dat");

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
					pw.write("title"); // 카페 글 제목
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("contents"); // 카페 글 내용
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("url"); // 카페 글 url
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("cafename"); // 카페 이름
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("thumbnail"); // 카페 글 대표 썸네일
					pw.write("'");
					pw.write("|^");
					pw.write("'");
					pw.write("datetime"); // 카페 글 작성시간
					pw.write("'");
					pw.println();
					pw.flush();
					pw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			// step 2. 전체 페이지 파악을 위한 샘플 파싱

			String json = "";

			json = JsonParser.parseBlogJson_daum(service_url, daum_api_key, args[0], "1");

			// System.out.println("json::::::" + json);

			int pageCount = 0;

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject meta = (JSONObject) obj.get("meta");

				int display = 50; // api에서 지원하는 최대값이므로 그냥 하드코딩
				int totalCount = ((Long) meta.get("pageable_count")).intValue();

				pageCount = (totalCount / display) + 1;

			} catch (Exception e) {
				e.printStackTrace();
			}

			StringBuffer resultSb = new StringBuffer("");

			StringBuffer title = new StringBuffer(" "); // 카페 제목
			StringBuffer contents = new StringBuffer(" "); // 카페 글 요약
			StringBuffer url = new StringBuffer(" "); // 카페 글 url
			StringBuffer cafename = new StringBuffer(" "); // 카페 이름
			StringBuffer thumbnail = new StringBuffer(" "); // 대표썸네일
			StringBuffer datetime = new StringBuffer(" "); // 카페 글 작성일시

			// step 3. 페이지 숫자만큼 반복하면서 파싱

			for (int i = 1; i <= pageCount; i++) {

				// 블로그와 다른 건 url 하나 뿐이므로 블로그 쪽 메서드 사용 가능
				json = JsonParser.parseBlogJson_daum(service_url, daum_api_key, args[0], Integer.toString(i));

				try {

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);

					JSONArray documents = (JSONArray) obj.get("documents");

					for (int r = 0; r < documents.size(); r++) {

						JSONObject document = (JSONObject) documents.get(r);

						Set<String> key = document.keySet();

						Iterator<String> iter = key.iterator();

						while (iter.hasNext()) {

							String keyname = iter.next();

							JsonParser.colWrite_sns(title, keyname, "title", document);
							JsonParser.colWrite_sns(contents, keyname, "contents", document);
							JsonParser.colWrite_sns(url, keyname, "url", document);
							JsonParser.colWrite_sns(cafename, keyname, "cafename", document);
							JsonParser.colWrite_sns(thumbnail, keyname, "thumbnail", document);
							JsonParser.colWrite_sns(datetime, keyname, "datetime", document);

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
						resultSb.append(contents);
						resultSb.append("'");
						resultSb.append("|^");
						resultSb.append("'");
						resultSb.append(url);
						resultSb.append("'");
						resultSb.append("|^");
						resultSb.append("'");
						resultSb.append(cafename);
						resultSb.append("'");
						resultSb.append("|^");
						resultSb.append("'");
						resultSb.append(thumbnail);
						resultSb.append("'");
						resultSb.append("|^");
						resultSb.append("'");
						resultSb.append(datetime);
						resultSb.append("'");
						resultSb.append(System.getProperty("line.separator"));

					}

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("parsing error!");
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

			TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_202.dat", "SNS");

			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

		
	}

}
