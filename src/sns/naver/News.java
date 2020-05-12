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

public class News {

	// 네이버검색 - 뉴스
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

					String service_url = JsonParser.getProperty("naver_news_url");
					String naver_client_id = JsonParser.getProperty("naver_client_id");
					String naver_client_secret = JsonParser.getProperty("naver_client_secret");

					// step 1.파일의 작성

					File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_104.dat");

					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					int pageCount = 0;

					String json = "";

					// step 2. 전체 페이지 파악을 위한 샘플 파싱
					json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret, args[0],
							job_dt, "1");
					
					// 테스트 출력
					// System.out.println(json);

					if(json.substring(0, 1).equals("<")) {
						//정상 json 응답이 아님
						System.out.println("indexing parsing error!!::query::" +args[0]);
						
					} else {
						
						JSONParser count_parser = new JSONParser();
						JSONObject count_obj = (JSONObject) count_parser.parse(json);
						
						String error = (String) count_obj.get("errorMessage");
						
						//일 데이터 제한 체크. JSONObject error의 null 여부로 체크
						if(error != null){
							System.out.println("errorMessage:::::"+error);
						} else if(error == null){
							int display = ((Long) count_obj.get("display")).intValue();
							int totalCount = ((Long) count_obj.get("total")).intValue();
							
							//2020.04.22 display값이 0인 경우 발견. 예외처리.
							if(display == 0){
								display = 100;
							}
							
							pageCount = (totalCount / display) + 1;

							StringBuffer resultSb = new StringBuffer("");

							StringBuffer title = new StringBuffer(" "); // 뉴스 제목
							StringBuffer originallink = new StringBuffer(" "); // 뉴스원본
																				// 링크
							StringBuffer link = new StringBuffer(" "); // 네이버 뉴스 링크
							StringBuffer description = new StringBuffer(" "); // 뉴스 내용
							StringBuffer pubDate = new StringBuffer(" "); // 제공일시
							
							// step 3. 페이지 숫자만큼 반복하면서 파싱

							for (int i = 1; i <= pageCount; i++) {

								// 여기서의 i는 페이지 넘버가 아닌 조회 시작 위치값이므로 페이지 당 표시 가능 수인 100만큼
								// 증가, 1000
								// 초과값은 api가 지원하지 않음
								
								//2020.04.20 : 데이터수가 너무 많다는 요청으로 200건까지만 떨어지도록 수정
								if (i % 100 == 1 && i <= 101) {

									System.out.println("페이지 검색 시작 위치는:::" + i);

									json = JsonParser.parseBlogJson_naver(service_url, naver_client_id, naver_client_secret,
											args[0], job_dt, Integer.toString(i));
									
									if(json.substring(0, 1).equals("<")) {
										//정상 json이 아닌 xml 형식의 리턴
										json = "{\"start\": 1,\"display\": 100,\"total\": 1,\"items\": []}";
									}

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
											JsonParser.colWrite_sns(originallink, keyname, "originallink", item);
											JsonParser.colWrite_sns(link, keyname, "link", item);
											JsonParser.colWrite_sns(description, keyname, "description", item);
											JsonParser.colWrite_sns(pubDate, keyname, "pubDate", item);

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
										resultSb.append(originallink);
										resultSb.append("'");
										resultSb.append("|^");
										resultSb.append("'");
										resultSb.append(link);
										resultSb.append("'");
										resultSb.append("|^");
										resultSb.append("'");
										resultSb.append(description);
										resultSb.append("'");
										resultSb.append("|^");
										resultSb.append("'");
										resultSb.append(pubDate);
										resultSb.append("'");
										resultSb.append(System.getProperty("line.separator"));

									}

									System.out.println("진행도::::::" + i + "/" + pageCount);

									//Thread.sleep(1000);
								}

							}
							
							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.println(resultSb.toString());
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

							System.out.println("parsing complete!");

							// step 5. 대상 서버에 sftp로 보냄

							//TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_104.dat", "SNS");

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
