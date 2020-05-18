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
//import common.TransSftp;

public class CustomSearch {

	// 구글 - 커스텀 검색
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

					String service_url = JsonParser.getProperty("google_customsearch_url");
					String google_api_key = JsonParser.getProperty("google_api_key");
					String google_api_cx = JsonParser.getProperty("google_api_cx");

					// step 1.파일의 작성

					File file = new File(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_301.dat");

					String json = "";

					json = JsonParser.parseJson_google(service_url, google_api_key, google_api_cx, args[0], job_dt,
							"1");

					// System.out.println("json::::" + json);

					// step 3. 페이지 숫자만큼 반복하면서 파싱
					// 구글의 검색 api는 한 페이지 당 건수는 10, 검색 최대값은 100건으로 고정되어 있음

					for (int i = 1; i <= 100; i++) {

						// 여기서의 i는 페이지 넘버가 아닌 조회 시작 위치값이므로 페이지 당 표시 가능 수인 10만큼
						// 증가, 100
						// 초과값은 api가 지원하지 않음
						if (i % 10 == 1 && i <= 100) {

							System.out.println("페이지 검색 시작 위치는:::" + i);

							json = JsonParser.parseJson_google(service_url, google_api_key, google_api_cx, args[0],
									job_dt, Integer.toString(i));
							
							String title = " "; // 문서 제목
							String snippet = " "; // 문서 내용
							String link = " "; // 링크
							
							//System.out.println("json::::"+json);

							JSONParser parser = new JSONParser();
							JSONObject obj = (JSONObject) parser.parse(json);
							
							JSONObject error = (JSONObject)obj.get("error");
							
							//일 데이터 제한 체크. JSONObject error의 null 여부로 체크
							if(error != null){
								
								String status =	error.get("status").toString();
								System.out.println("status:::::"+status);
								//System.exit(-1);

							} else if(error == null){
								
								JSONArray items = (JSONArray) obj.get("items");
								
								if(items == null){
									
									System.out.println("status:::::NO_DATA_ERROR");
									//System.exit(-1);

								} else {
									for (int r = 0; r < items.size(); r++) {

										JSONObject item = (JSONObject) items.get(r);

										Set<String> key = item.keySet();

										Iterator<String> iter = key.iterator();

										while (iter.hasNext()) {

											String keyname = iter.next();
											
											if(keyname.equals("title")) {
												if(!(JsonParser.isEmpty(item.get(keyname)))){
													title = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													title = " ";
												}
											}
											if(keyname.equals("snippet")) {
												if(!(JsonParser.isEmpty(item.get(keyname)))){
													snippet = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													snippet = " ";
												}
											}
											if(keyname.equals("link")) {
												if(!(JsonParser.isEmpty(item.get(keyname)))){
													link = item.get(keyname).toString().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ")
															.replaceAll("(\\s{2,}|\\t{2,})", " ");
												}else{
													link = " ";
												}
											}

										}
										
										// step 4. 파일에 쓰기
										try {
											PrintWriter pw = new PrintWriter(
													new BufferedWriter(new FileWriter(file, true)));

											pw.write("'");
											pw.write(job_dt); // 시스템 일자 (파라미터로 준 경우는
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
											pw.write(snippet.replace("'",""));
											pw.write("'");
											pw.write("|^");
											pw.write("'");
											pw.write(link.replace("'",""));
											pw.write("'");
											pw.println();
											pw.flush();
											pw.close();

										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
	
							}

							System.out.println("진행도::::::" + i + "/" + 100);

							//Thread.sleep(1000);
							
						}

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "SNS/TIF_SNS_301.dat", "SNS");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("query :" + args[0]);
			}



	}

}
