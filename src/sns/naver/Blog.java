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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class Blog {

	// 네이버검색 - 블로그
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		try {

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

				String service_url = JsonParser.getProperty("naver_blog_url");
				String naver_client_id = JsonParser.getProperty("naver_client_id");
				String naver_client_secret = JsonParser.getProperty("naver_client_secret");

				// step 1.파일의 작성

				File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_101.dat");

				int pageCount = 0;

				String json = "";

				// step 2. 전체 페이지 파악을 위한 샘플 파싱
				json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret, args[0],
						job_dt, "1");

				// 테스트 출력
				// System.out.println(json);

				if (json.substring(0, 1).equals("<")) {
					// 정상 json 응답이 아님
					System.out.println("indexing parsing error!!::query::" + args[0]);

				} else {

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);

					String error = (String) count_obj.get("errorMessage");

					// 일 데이터 제한 체크. JSONObject error의 null 여부로 체크
					if (error != null) {
						System.out.println("errorMessage:::::" + error);
					} else if (error == null) {

						int display = ((Long) count_obj.get("display")).intValue();
						int totalCount = ((Long) count_obj.get("total")).intValue();

						// 2020.04.22 display값이 0인 경우 발견. 예외처리.
						if (display == 0) {
							display = 100;
						}

						pageCount = (totalCount / display) + 1;

						// step 3. 페이지 숫자만큼 반복하면서 파싱

						for (int i = 1; i <= pageCount; i++) {

							// 여기서의 i는 페이지 넘버가 아닌 조회 시작 위치값이므로 페이지 당 표시 가능 수인
							// 100만큼
							// 증가, 1000 초과값은 api가 지원하지 않음

							// 2020.04.20 : 데이터수가 너무 많다는 요청으로 200건까지만 떨어지도록 수정
							if (i % 100 == 1 && i <= 101) {

								System.out.println("페이지 검색 시작 위치는:::" + i);

								json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret,
										args[0], job_dt, Integer.toString(i));

								if (json.substring(0, 1).equals("<")) {
									// 정상 json이 아닌 xml 형식의 리턴
									json = "{\"start\": 1,\"display\": 100,\"total\": 1,\"items\": []}";
								}
								
								String postdate = " "; // 작성날짜
								String title = " ";// 블로그 제목
								String link = " ";// 블로그 링크
								String description = " ";// 블로그
																					// 내용
								String bloggername = " ";// 블로거
																					// 이름
								String bloggerlink = " ";// 블로거
																					// 링크

								// 테스트 출력
								// System.out.println(json);

								JSONParser parser = new JSONParser();
								JSONObject obj = (JSONObject) parser.parse(json);

								JSONArray items = (JSONArray) obj.get("items");

								for (int r = 0; r < items.size(); r++) {

									JSONObject item = (JSONObject) items.get(r);

									Set<String> key = item.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										if(keyname.equals("postdate")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												postdate = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(postdate);
												postdate = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (postdate.isEmpty()) {
													postdate = " ";
												}
											}else{
												postdate = " ";
											}
										}
										if(keyname.equals("title")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												title = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(title);
												title = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (title.isEmpty()) {
													title = " ";
												}
											}else{
												title = " ";
											}
										}
										if(keyname.equals("link")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												link = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(link);
												link = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (link.isEmpty()) {
													link = " ";
												}
											}else{
												link = " ";
											}
										}
										if(keyname.equals("description")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												description = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(description);
												description = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (description.isEmpty()) {
													description = " ";
												}
											}else{
												description = " ";
											}
										}
										if(keyname.equals("bloggername")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												bloggername = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(bloggername);
												bloggername = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (bloggername.isEmpty()) {
													bloggername = " ";
												}
											}else{
												bloggername = " ";
											}
										}
										if(keyname.equals("bloggerlink")) {
											if(!(JsonParser.isEmpty(item.get(keyname)))){
												bloggerlink = item.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
														.replace("'", "").replace("&#39;", "").replace("&#34;", "")
														.replaceAll("(\\s{2,}|\\t{2,})", " ");
												
												// 에러 유발자들인 emoji 제거..
												Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
												Matcher emoticonsMatcher = emoticons.matcher(bloggerlink);
												bloggerlink = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
														.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

												// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
												if (bloggerlink.isEmpty()) {
													bloggerlink = " ";
												}
											}else{
												bloggerlink = " ";
											}
										}

									}
									
									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write("'");
										pw.write(job_dt); // 시스템 일자 (파라미터로 준
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(args[0]); // 검색어
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(postdate.replace("'",""));
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(title.replace("'",""));
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(link.replace("'",""));
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(description.replace("'",""));
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(bloggername.replace("'",""));
										pw.write("'");
										pw.write("|^");
										pw.write("'");
										pw.write(bloggerlink.replace("'",""));
										pw.write("'");
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}	
								}

								System.out.println("진행도::::::" + i + "/" + pageCount);

								// Thread.sleep(1000);

							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_101.dat", "SNS");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					}

				}

			} else {
				System.out.println("파라미터 개수 에러!!");
				System.exit(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("mgtNo :" + args[0]);
		}

	}

}
