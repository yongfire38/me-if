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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class Web {

	// 다음검색 - 웹문서
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

					String service_url = JsonParser.getProperty("daum_web_url");
					String daum_api_key = JsonParser.getProperty("daum_api_key");

					// step 1.파일의 작성

					File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_203.dat");

					// step 2. 전체 페이지 파악을 위한 샘플 파싱

					String json = "";

					json = JsonParser.parseBlogJson_daum(service_url, daum_api_key, args[0], job_dt, "1");

					// System.out.println("json::::::" + json);

					int pageCount = 0;

					JSONParser count_parser = new JSONParser();
					JSONObject count_obj = (JSONObject) count_parser.parse(json);
					JSONObject count_meta = (JSONObject) count_obj.get("meta");

					int display = 50; // api에서 지원하는 최대값이므로 그냥 하드코딩
					int totalCount = ((Long) count_meta.get("pageable_count")).intValue();
					
					if(totalCount > 200){
						totalCount = 200;
					}

					pageCount = (totalCount / display);
					
					if(pageCount <= 0){
						pageCount = 1;
					}

					// step 3. 페이지 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {

						// 블로그와 다른 건 url 하나 뿐이므로 블로그 쪽 메서드 사용 가능
						json = JsonParser.parseBlogJson_daum(service_url, daum_api_key, args[0], job_dt,
								Integer.toString(i));
						
						String title = " "; // 문서 제목
						String contents = " "; // 문서 본문 중 일부
						String url = " "; // 문서 url
						String datetime = " "; // 문서 글 작성시간

						JSONParser parser = new JSONParser();
						JSONObject obj = (JSONObject) parser.parse(json);

						JSONArray documents = (JSONArray) obj.get("documents");

						for (int r = 0; r < documents.size(); r++) {

							JSONObject document = (JSONObject) documents.get(r);

							Set<String> key = document.keySet();

							Iterator<String> iter = key.iterator();

							while (iter.hasNext()) {

								String keyname = iter.next();
								
								if(keyname.equals("title")) {
									if(!(JsonParser.isEmpty(document.get(keyname)))){
										title = document.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
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
								if(keyname.equals("contents")) {
									if(!(JsonParser.isEmpty(document.get(keyname)))){
										contents = document.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replace("'", "").replace("&#39;", "").replace("&#34;", "")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
										
										// 에러 유발자들인 emoji 제거..
										Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
										Matcher emoticonsMatcher = emoticons.matcher(contents);
										contents = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
												.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

										// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
										if (contents.isEmpty()) {
											contents = " ";
										}
									}else{
										contents = " ";
									}
								}
								if(keyname.equals("url")) {
									if(!(JsonParser.isEmpty(document.get(keyname)))){
										url = document.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replace("'", "").replace("&#39;", "").replace("&#34;", "")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
										
										// 에러 유발자들인 emoji 제거..
										Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
										Matcher emoticonsMatcher = emoticons.matcher(url);
										url = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
												.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

										// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
										if (url.isEmpty()) {
											url = " ";
										}
									}else{
										url = " ";
									}
								}
								if(keyname.equals("datetime")) {
									if(!(JsonParser.isEmpty(document.get(keyname)))){
										datetime = document.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ")
												.replace("'", "").replace("&#39;", "").replace("&#34;", "")
												.replaceAll("(\\s{2,}|\\t{2,})", " ");
										
										// 에러 유발자들인 emoji 제거..
										Pattern emoticons = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
										Matcher emoticonsMatcher = emoticons.matcher(datetime);
										datetime = emoticonsMatcher.replaceAll(" ").replaceAll("\\p{InEmoticons}+", "")
												.replaceAll("\\p{So}+", "").replaceAll("\\p{InMiscellaneousSymbolsAndPictographs}+", "");

										// 내용이 특수문자만으로 구성되어 있을 경우가 있으므로 특문제거 로직 완료 후 아무 것도 없으면 초기화
										if (datetime.isEmpty()) {
											datetime = " ";
										}
									}else{
										datetime = " ";
									}
								}	

							}
							
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(
										new BufferedWriter(new FileWriter(file, true)));

								pw.write("'");
								pw.write(Integer.toString((r + ((i-1) * 50)) +1)); // 행 번호
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(job_dt); // 시스템 일자 (파라미터로 준 경우는 입력값)
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(args[0]); // 검색어
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(title.replace("'",""));
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(contents.replace("'",""));
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(url.replace("'",""));
								pw.write("'");
								pw.write("|^");
								pw.write("'");
								pw.write(datetime.replace("'",""));
								pw.write("'");
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(1000);
					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_203.dat", "SNS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

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
