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
import common.TransSftp;

public class GetGreen {

	

	// 동식물상 정보 조회 - 녹지자연도 속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			System.out.println("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("florafauna_getGreen_url");
			String service_key = JsonParser.getProperty("florafauna_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_29_" + mgtNo + ".dat");

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

			String json = "";

			json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);

			// step 3.필요에 맞게 파싱

			try {

				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(json);
				JSONObject response = (JSONObject) obj.get("response");

				// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
				JSONObject header = (JSONObject) response.get("header");
				JSONObject body = (JSONObject) response.get("body");

				String resultCode = header.get("resultCode").toString().trim();

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
							bfeGreens0gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens1gAr")) {
							bfeGreens1gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens2gAr")) {
							bfeGreens2gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens3gAr")) {
							bfeGreens3gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens4gAr")) {
							bfeGreens4gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens5gAr")) {
							bfeGreens5gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens6gAr")) {
							bfeGreens6gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens7gAr")) {
							bfeGreens7gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens8gAr")) {
							bfeGreens8gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens9gAr")) {
							bfeGreens9gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens10gAr")) {
							bfeGreens10gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens0gAr")) {
							aftGreens0gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens1gAr")) {
							aftGreens1gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens2gAr")) {
							aftGreens2gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens3gAr")) {
							aftGreens3gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens4gAr")) {
							aftGreens4gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens5gAr")) {
							aftGreens5gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens6gAr")) {
							aftGreens6gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens7gAr")) {
							aftGreens7gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens8gAr")) {
							aftGreens8gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens9gAr")) {
							aftGreens9gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens10gAr")) {
							aftGreens10gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens0gRt")) {
							bfeGreens0gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens1gRt")) {
							bfeGreens1gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens2gRt")) {
							bfeGreens2gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens3gRt")) {
							bfeGreens3gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens4gRt")) {
							bfeGreens4gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens5gRt")) {
							bfeGreens5gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens6gRt")) {
							bfeGreens6gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens7gRt")) {
							bfeGreens7gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens8gRt")) {
							bfeGreens8gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens9gRt")) {
							bfeGreens9gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("bfeGreens10gRt")) {
							bfeGreens10gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens0gRt")) {
							aftGreens0gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens1gRt")) {
							aftGreens1gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens2gRt")) {
							aftGreens2gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens3gRt")) {
							aftGreens3gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens4gRt")) {
							aftGreens4gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens5gRt")) {
							aftGreens5gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens6gRt")) {
							aftGreens6gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens7gRt")) {
							aftGreens7gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens8gRt")) {
							aftGreens8gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens9gRt")) {
							aftGreens9gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("aftGreens10gRt")) {
							aftGreens10gRt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("damageWdptCnt")) {
							damageWdptCnt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("ruseTrnspntWdptCnt")) {
							ruseTrnspntWdptCnt = body.get(keyname).toString().trim();
						}
						if (keyname.equals("eclgy1gAr")) {
							eclgy1gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("eclgy2gAr")) {
							eclgy2gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("eclgy3gAr")) {
							eclgy3gAr = body.get(keyname).toString().trim();
						}
						if (keyname.equals("ivstgInfo")) {
							ivstgInfo = body.get(keyname).toString().trim();
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

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_29_" + mgtNo + ".dat", "EIA");

				} else if (resultCode.equals("03")) {
					System.out.println("data not exist!! mgtNo :" + mgtNo);
				} else {
					System.out.println("parsing error!! mgtNo :" + mgtNo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("mgtNo :" + mgtNo);
			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
