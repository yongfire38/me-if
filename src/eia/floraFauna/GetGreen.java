package eia.floraFauna;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
//import common.TransSftp;

public class GetGreen {

	// 동식물상 정보 조회 - 녹지자연도 속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		int retry = 0;

		while (retry++ < 10) {

			try {

				Thread.sleep(3000);

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("florafauna_getGreen_url");
					String service_key = JsonParser.getProperty("florafauna_service_key");

					// step 1.파일의 첫 행 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_29.dat");

					if (file.exists()) {

						System.out.println("파일이 이미 존재하므로 이어쓰기..");

					} else {

						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write("mgtNo"); // 사업 코드
							pw.write("|^");
							pw.write("bfeGreens0gAr"); // 사업시행 전 녹지자연도(면적)_0등급
							pw.write("|^");
							pw.write("bfeGreens1gAr"); // 사업시행 전 녹지자연도(면적)_1등급
							pw.write("|^");
							pw.write("bfeGreens2gAr"); // 사업시행 전 녹지자연도(면적)_2등급
							pw.write("|^");
							pw.write("bfeGreens3gAr"); // 사업시행 전 녹지자연도(면적)_3등급
							pw.write("|^");
							pw.write("bfeGreens4gAr"); // 사업시행 전 녹지자연도(면적)_4등급
							pw.write("|^");
							pw.write("bfeGreens5gAr"); // 사업시행 전 녹지자연도(면적)_5등급
							pw.write("|^");
							pw.write("bfeGreens6gAr"); // 사업시행 전 녹지자연도(면적)_6등급
							pw.write("|^");
							pw.write("bfeGreens7gAr"); // 사업시행 전 녹지자연도(면적)_7등급
							pw.write("|^");
							pw.write("bfeGreens8gAr"); // 사업시행 전 녹지자연도(면적)_8등급
							pw.write("|^");
							pw.write("bfeGreens9gAr"); // 사업시행 전 녹지자연도(면적)_9등급
							pw.write("|^");
							pw.write("bfeGreens10gAr"); // 사업시행 전 녹지자연도(면적)_10등급
							pw.write("|^");
							pw.write("aftGreens0gAr"); // 사업시행 후 녹지자연도(면적)_0등급
							pw.write("|^");
							pw.write("aftGreens1gAr"); // 사업시행 후 녹지자연도(면적)_1등급
							pw.write("|^");
							pw.write("aftGreens2gAr"); // 사업시행 후 녹지자연도(면적)_2등급
							pw.write("|^");
							pw.write("aftGreens3gAr"); // 사업시행 후 녹지자연도(면적)_3등급
							pw.write("|^");
							pw.write("aftGreens4gAr"); // 사업시행 후 녹지자연도(면적)_4등급
							pw.write("|^");
							pw.write("aftGreens5gAr"); // 사업시행 후 녹지자연도(면적)_5등급
							pw.write("|^");
							pw.write("aftGreens6gAr"); // 사업시행 후 녹지자연도(면적)_6등급
							pw.write("|^");
							pw.write("aftGreens7gAr"); // 사업시행 후 녹지자연도(면적)_7등급
							pw.write("|^");
							pw.write("aftGreens8gAr"); // 사업시행 후 녹지자연도(면적)_8등급
							pw.write("|^");
							pw.write("aftGreens9gAr"); // 사업시행 후 녹지자연도(면적)_9등급
							pw.write("|^");
							pw.write("aftGreens10gAr"); // 사업시행 후 녹지자연도(면적)_10등급
							pw.write("|^");
							pw.write("bfeGreens0gRt"); // 사업시행 전 녹지자연도(비율)_0등급
							pw.write("|^");
							pw.write("bfeGreens1gRt"); // 사업시행 전 녹지자연도(비율)_1등급
							pw.write("|^");
							pw.write("bfeGreens2gRt"); // 사업시행 전 녹지자연도(비율)_2등급
							pw.write("|^");
							pw.write("bfeGreens3gRt"); // 사업시행 전 녹지자연도(비율)_3등급
							pw.write("|^");
							pw.write("bfeGreens4gRt"); // 사업시행 전 녹지자연도(비율)_4등급
							pw.write("|^");
							pw.write("bfeGreens5gRt"); // 사업시행 전 녹지자연도(비율)_5등급
							pw.write("|^");
							pw.write("bfeGreens6gRt"); // 사업시행 전 녹지자연도(비율)_6등급
							pw.write("|^");
							pw.write("bfeGreens7gRt"); // 사업시행 전 녹지자연도(비율)_7등급
							pw.write("|^");
							pw.write("bfeGreens8gRt"); // 사업시행 전 녹지자연도(비율)_8등급
							pw.write("|^");
							pw.write("bfeGreens9gRt"); // 사업시행 전 녹지자연도(비율)_9등급
							pw.write("|^");
							pw.write("bfeGreens10gRt"); // 사업시행 전 녹지자연도(비율)_10등급
							pw.write("|^");
							pw.write("aftGreens0gRt"); // 사업시행 후 녹지자연도(비율)_0등급
							pw.write("|^");
							pw.write("aftGreens1gRt"); // 사업시행 후 녹지자연도(비율)_1등급
							pw.write("|^");
							pw.write("aftGreens2gRt"); // 사업시행 후 녹지자연도(비율)_2등급
							pw.write("|^");
							pw.write("aftGreens3gRt"); // 사업시행 후 녹지자연도(비율)_3등급
							pw.write("|^");
							pw.write("aftGreens4gRt"); // 사업시행 후 녹지자연도(비율)_4등급
							pw.write("|^");
							pw.write("aftGreens5gRt"); // 사업시행 후 녹지자연도(비율)_5등급
							pw.write("|^");
							pw.write("aftGreens6gRt"); // 사업시행 후 녹지자연도(비율)_6등급
							pw.write("|^");
							pw.write("aftGreens7gRt"); // 사업시행 후 녹지자연도(비율)_7등급
							pw.write("|^");
							pw.write("aftGreens8gRt"); // 사업시행 후 녹지자연도(비율)_8등급
							pw.write("|^");
							pw.write("aftGreens9gRt"); // 사업시행 후 녹지자연도(비율)_9등급
							pw.write("|^");
							pw.write("aftGreens10gRt"); // 사업시행 후 녹지자연도(비율)_10등급
							pw.write("|^");
							pw.write("damageWdptCnt"); // 훼손수목량
							pw.write("|^");
							pw.write("ruseTrnspntWdptCnt"); // 재활용 이식수목량
							pw.write("|^");
							pw.write("eclgy1gAr"); // 생태자연도분포(1등급)
							pw.write("|^");
							pw.write("eclgy2gAr"); // 생태자연도분포(2등급)
							pw.write("|^");
							pw.write("eclgy3gAr"); // 생태자연도분포(3등급)
							pw.write("|^");
							pw.write("ivstgInfo"); // 조사지점 및 조사경로
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					String json = "";

					json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);

					System.out.println("json:::" + json);

					// step 3.필요에 맞게 파싱

					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(json);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String bfeGreens0gAr = " "; // 사업시행 전 녹지자연도(면적)_0등급
						String bfeGreens1gAr = " "; // 사업시행 전 녹지자연도(면적)_1등급
						String bfeGreens2gAr = " "; // 사업시행 전 녹지자연도(면적)_2등급
						String bfeGreens3gAr = " "; // 사업시행 전 녹지자연도(면적)_3등급
						String bfeGreens4gAr = " "; // 사업시행 전 녹지자연도(면적)_4등급
						String bfeGreens5gAr = " "; // 사업시행 전 녹지자연도(면적)_5등급
						String bfeGreens6gAr = " "; // 사업시행 전 녹지자연도(면적)_6등급
						String bfeGreens7gAr = " "; // 사업시행 전 녹지자연도(면적)_7등급
						String bfeGreens8gAr = " "; // 사업시행 전 녹지자연도(면적)_8등급
						String bfeGreens9gAr = " "; // 사업시행 전 녹지자연도(면적)_9등급
						String bfeGreens10gAr = " "; // 사업시행 전 녹지자연도(면적)_10등급
						String aftGreens0gAr = " "; // 사업시행 후 녹지자연도(면적)_0등급
						String aftGreens1gAr = " "; // 사업시행 후 녹지자연도(면적)_1등급
						String aftGreens2gAr = " "; // 사업시행 후 녹지자연도(면적)_2등급
						String aftGreens3gAr = " "; // 사업시행 후 녹지자연도(면적)_3등급
						String aftGreens4gAr = " "; // 사업시행 후 녹지자연도(면적)_4등급
						String aftGreens5gAr = " "; // 사업시행 후 녹지자연도(면적)_5등급
						String aftGreens6gAr = " "; // 사업시행 후 녹지자연도(면적)_6등급
						String aftGreens7gAr = " "; // 사업시행 후 녹지자연도(면적)_7등급
						String aftGreens8gAr = " "; // 사업시행 후 녹지자연도(면적)_8등급
						String aftGreens9gAr = " "; // 사업시행 후 녹지자연도(면적)_9등급
						String aftGreens10gAr = " "; // 사업시행 후 녹지자연도(면적)_10등급
						String bfeGreens0gRt = " "; // 사업시행 전 녹지자연도(비율)_0등급
						String bfeGreens1gRt = " "; // 사업시행 전 녹지자연도(비율)_1등급
						String bfeGreens2gRt = " "; // 사업시행 전 녹지자연도(비율)_2등급
						String bfeGreens3gRt = " "; // 사업시행 전 녹지자연도(비율)_3등급
						String bfeGreens4gRt = " "; // 사업시행 전 녹지자연도(비율)_4등급
						String bfeGreens5gRt = " "; // 사업시행 전 녹지자연도(비율)_5등급
						String bfeGreens6gRt = " "; // 사업시행 전 녹지자연도(비율)_6등급
						String bfeGreens7gRt = " "; // 사업시행 전 녹지자연도(비율)_7등급
						String bfeGreens8gRt = " "; // 사업시행 전 녹지자연도(비율)_8등급
						String bfeGreens9gRt = " "; // 사업시행 전 녹지자연도(비율)_9등급
						String bfeGreens10gRt = " "; // 사업시행 전 녹지자연도(비율)_10등급
						String aftGreens0gRt = " "; // 사업시행 후 녹지자연도(비율)_0등급
						String aftGreens1gRt = " "; // 사업시행 후 녹지자연도(비율)_1등급
						String aftGreens2gRt = " "; // 사업시행 후 녹지자연도(비율)_2등급
						String aftGreens3gRt = " "; // 사업시행 후 녹지자연도(비율)_3등급
						String aftGreens4gRt = " "; // 사업시행 후 녹지자연도(비율)_4등급
						String aftGreens5gRt = " "; // 사업시행 후 녹지자연도(비율)_5등급
						String aftGreens6gRt = " "; // 사업시행 후 녹지자연도(비율)_6등급
						String aftGreens7gRt = " "; // 사업시행 후 녹지자연도(비율)_7등급
						String aftGreens8gRt = " "; // 사업시행 후 녹지자연도(비율)_8등급
						String aftGreens9gRt = " "; // 사업시행 후 녹지자연도(비율)_9등급
						String aftGreens10gRt = " "; // 사업시행 후 녹지자연도(비율)_10등급
						String damageWdptCnt = " "; // 훼손수목량
						String ruseTrnspntWdptCnt = " "; // 재활용 이식수목량
						String eclgy1gAr = " "; // 생태자연도분포(1등급)
						String eclgy2gAr = " "; // 생태자연도분포(2등급)
						String eclgy3gAr = " "; // 생태자연도분포(3등급)
						String ivstgInfo = " "; // 조사지점 및 조사경로

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("bfeGreens0gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens0gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens0gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens1gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens1gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens1gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens2gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens2gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens2gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens3gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens3gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens3gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens4gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens4gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens4gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens5gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens5gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens5gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens6gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens6gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens6gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens7gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens7gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens7gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens8gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens8gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens8gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens9gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens9gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens9gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens10gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens10gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens10gAr = " ";
								}
							}
							if (keyname.equals("aftGreens0gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens0gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens0gAr = " ";
								}
							}
							if (keyname.equals("aftGreens1gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens1gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens1gAr = " ";
								}
							}
							if (keyname.equals("aftGreens2gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens2gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens2gAr = " ";
								}
							}
							if (keyname.equals("aftGreens3gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens3gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens3gAr = " ";
								}
							}
							if (keyname.equals("aftGreens4gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens4gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens4gAr = " ";
								}
							}
							if (keyname.equals("aftGreens5gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens5gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens5gAr = " ";
								}
							}
							if (keyname.equals("aftGreens6gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens6gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens6gAr = " ";
								}
							}
							if (keyname.equals("aftGreens7gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens7gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens7gAr = " ";
								}
							}
							if (keyname.equals("aftGreens8gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens8gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens8gAr = " ";
								}
							}
							if (keyname.equals("aftGreens9gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens9gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens9gAr = " ";
								}
							}
							if (keyname.equals("aftGreens10gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens10gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens10gAr = " ";
								}
							}
							if (keyname.equals("bfeGreens0gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens0gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens0gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens1gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens1gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens1gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens2gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens2gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens2gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens3gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens3gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens3gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens4gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens4gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens4gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens5gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens5gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens5gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens6gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens6gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens6gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens7gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens7gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens7gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens8gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens8gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens8gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens9gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens9gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens9gRt = " ";
								}
							}
							if (keyname.equals("bfeGreens10gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									bfeGreens10gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									bfeGreens10gRt = " ";
								}
							}
							if (keyname.equals("aftGreens0gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens0gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens0gRt = " ";
								}
							}
							if (keyname.equals("aftGreens1gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens1gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens1gRt = " ";
								}
							}
							if (keyname.equals("aftGreens2gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens2gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens2gRt = " ";
								}
							}
							if (keyname.equals("aftGreens3gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens3gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens3gRt = " ";
								}
							}
							if (keyname.equals("aftGreens4gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens4gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens4gRt = " ";
								}
							}
							if (keyname.equals("aftGreens5gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens5gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens5gRt = " ";
								}
							}
							if (keyname.equals("aftGreens6gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens6gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens6gRt = " ";
								}
							}
							if (keyname.equals("aftGreens7gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens7gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens7gRt = " ";
								}
							}
							if (keyname.equals("aftGreens8gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens8gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens8gRt = " ";
								}
							}
							if (keyname.equals("aftGreens9gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens9gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens9gRt = " ";
								}
							}
							if (keyname.equals("aftGreens10gRt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									aftGreens10gRt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									aftGreens10gRt = " ";
								}
							}
							if (keyname.equals("damageWdptCnt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									damageWdptCnt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									damageWdptCnt = " ";
								}
							}
							if (keyname.equals("ruseTrnspntWdptCnt")) {
								if (!(body.get(keyname).toString().equals(""))) {
									ruseTrnspntWdptCnt = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									ruseTrnspntWdptCnt = " ";
								}
							}
							if (keyname.equals("eclgy1gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									eclgy1gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									eclgy1gAr = " ";
								}
							}
							if (keyname.equals("eclgy2gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									eclgy2gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									eclgy2gAr = " ";
								}
							}
							if (keyname.equals("eclgy3gAr")) {
								if (!(body.get(keyname).toString().equals(""))) {
									eclgy3gAr = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									eclgy3gAr = " ";
								}
							}
							if (keyname.equals("ivstgInfo")) {
								if (!(body.get(keyname).toString().equals(""))) {
									ivstgInfo = body.get(keyname).toString().trim().replace(" ", "");
								} else {
									ivstgInfo = " ";
								}
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(bfeGreens0gAr); // 사업시행 전 녹지자연도(면적)_0등급
							pw.write("|^");
							pw.write(bfeGreens1gAr); // 사업시행 전 녹지자연도(면적)_1등급
							pw.write("|^");
							pw.write(bfeGreens2gAr); // 사업시행 전 녹지자연도(면적)_2등급
							pw.write("|^");
							pw.write(bfeGreens3gAr); // 사업시행 전 녹지자연도(면적)_3등급
							pw.write("|^");
							pw.write(bfeGreens4gAr); // 사업시행 전 녹지자연도(면적)_4등급
							pw.write("|^");
							pw.write(bfeGreens5gAr); // 사업시행 전 녹지자연도(면적)_5등급
							pw.write("|^");
							pw.write(bfeGreens6gAr); // 사업시행 전 녹지자연도(면적)_6등급
							pw.write("|^");
							pw.write(bfeGreens7gAr); // 사업시행 전 녹지자연도(면적)_7등급
							pw.write("|^");
							pw.write(bfeGreens8gAr); // 사업시행 전 녹지자연도(면적)_8등급
							pw.write("|^");
							pw.write(bfeGreens9gAr); // 사업시행 전 녹지자연도(면적)_9등급
							pw.write("|^");
							pw.write(bfeGreens10gAr); // 사업시행 전 녹지자연도(면적)_10등급
							pw.write("|^");
							pw.write(aftGreens0gAr); // 사업시행 후 녹지자연도(면적)_0등급
							pw.write("|^");
							pw.write(aftGreens1gAr); // 사업시행 후 녹지자연도(면적)_1등급
							pw.write("|^");
							pw.write(aftGreens2gAr); // 사업시행 후 녹지자연도(면적)_2등급
							pw.write("|^");
							pw.write(aftGreens3gAr); // 사업시행 후 녹지자연도(면적)_3등급
							pw.write("|^");
							pw.write(aftGreens4gAr); // 사업시행 후 녹지자연도(면적)_4등급
							pw.write("|^");
							pw.write(aftGreens5gAr); // 사업시행 후 녹지자연도(면적)_5등급
							pw.write("|^");
							pw.write(aftGreens6gAr); // 사업시행 후 녹지자연도(면적)_6등급
							pw.write("|^");
							pw.write(aftGreens7gAr); // 사업시행 후 녹지자연도(면적)_7등급
							pw.write("|^");
							pw.write(aftGreens8gAr); // 사업시행 후 녹지자연도(면적)_8등급
							pw.write("|^");
							pw.write(aftGreens9gAr); // 사업시행 후 녹지자연도(면적)_9등급
							pw.write("|^");
							pw.write(aftGreens10gAr); // 사업시행 후 녹지자연도(면적)_10등급
							pw.write("|^");
							pw.write(bfeGreens0gRt); // 사업시행 전 녹지자연도(비율)_0등급
							pw.write("|^");
							pw.write(bfeGreens1gRt); // 사업시행 전 녹지자연도(비율)_1등급
							pw.write("|^");
							pw.write(bfeGreens2gRt); // 사업시행 전 녹지자연도(비율)_2등급
							pw.write("|^");
							pw.write(bfeGreens3gRt); // 사업시행 전 녹지자연도(비율)_3등급
							pw.write("|^");
							pw.write(bfeGreens4gRt); // 사업시행 전 녹지자연도(비율)_4등급
							pw.write("|^");
							pw.write(bfeGreens5gRt); // 사업시행 전 녹지자연도(비율)_5등급
							pw.write("|^");
							pw.write(bfeGreens6gRt); // 사업시행 전 녹지자연도(비율)_6등급
							pw.write("|^");
							pw.write(bfeGreens7gRt); // 사업시행 전 녹지자연도(비율)_7등급
							pw.write("|^");
							pw.write(bfeGreens8gRt); // 사업시행 전 녹지자연도(비율)_8등급
							pw.write("|^");
							pw.write(bfeGreens9gRt); // 사업시행 전 녹지자연도(비율)_9등급
							pw.write("|^");
							pw.write(bfeGreens10gRt); // 사업시행 전 녹지자연도(비율)_10등급
							pw.write("|^");
							pw.write(aftGreens0gRt); // 사업시행 후 녹지자연도(비율)_0등급
							pw.write("|^");
							pw.write(aftGreens1gRt); // 사업시행 후 녹지자연도(비율)_1등급
							pw.write("|^");
							pw.write(aftGreens2gRt); // 사업시행 후 녹지자연도(비율)_2등급
							pw.write("|^");
							pw.write(aftGreens3gRt); // 사업시행 후 녹지자연도(비율)_3등급
							pw.write("|^");
							pw.write(aftGreens4gRt); // 사업시행 후 녹지자연도(비율)_4등급
							pw.write("|^");
							pw.write(aftGreens5gRt); // 사업시행 후 녹지자연도(비율)_5등급
							pw.write("|^");
							pw.write(aftGreens6gRt); // 사업시행 후 녹지자연도(비율)_6등급
							pw.write("|^");
							pw.write(aftGreens7gRt); // 사업시행 후 녹지자연도(비율)_7등급
							pw.write("|^");
							pw.write(aftGreens8gRt); // 사업시행 후 녹지자연도(비율)_8등급
							pw.write("|^");
							pw.write(aftGreens9gRt); // 사업시행 후 녹지자연도(비율)_9등급
							pw.write("|^");
							pw.write(aftGreens10gRt); // 사업시행 후 녹지자연도(비율)_10등급
							pw.write("|^");
							pw.write(damageWdptCnt); // 훼손수목량
							pw.write("|^");
							pw.write(ruseTrnspntWdptCnt); // 재활용 이식수목량
							pw.write("|^");
							pw.write(eclgy1gAr); // 생태자연도분포(1등급)
							pw.write("|^");
							pw.write(eclgy2gAr); // 생태자연도분포(2등급)
							pw.write("|^");
							pw.write(eclgy3gAr); // 생태자연도분포(3등급)
							pw.write("|^");
							pw.write(ivstgInfo); // 조사지점 및 조사경로
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						// TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_29.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("parsing error!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
					}

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
