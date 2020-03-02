package eia.geological;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.JsonParser;
import common.TransSftp;

public class GetAl {

	final static Logger logger = Logger.getLogger(GetAl.class);

	// 지형지질 정보 조회 - 표고속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
		if (args.length == 1) {

			logger.info("firstLine start..");
			String mgtNo = args[0];

			// step 0.open api url과 서비스 키.
			String service_url = JsonParser.getProperty("geological_getAl_url");
			String service_key = JsonParser.getProperty("geological_service_key");

			// step 1.파일의 첫 행 작성
			File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_26_" + mgtNo + ".dat");

			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				pw.write("mgtNo"); // 사업 코드
				pw.write("|^");
				pw.write("alGrad"); // 사업지구 표고구분
				pw.write("|^");
				pw.write("alAreaRate"); // 표고별 면적비율
				pw.write("|^");
				pw.write("alArea"); // 표고별 면적
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

					JSONArray als = (JSONArray) body.get("als");

					for (int i = 0; i < als.size(); i++) {

						JSONObject al = (JSONObject) als.get(i);

						Set<String> key = al.keySet();

						Iterator<String> iter = key.iterator();

						String alGrad = " "; // 사업지구 표고구분
						String alAreaRate = " "; // 표고별 면적비율
						String alArea = " "; // 표고별 면적

						while (iter.hasNext()) {

							String keyname = iter.next();

							if (keyname.equals("alGrad")) {
								alGrad = al.get(keyname).toString().trim();
							}
							if (keyname.equals("alAreaRate")) {
								alAreaRate = al.get(keyname).toString().trim();
							}
							if (keyname.equals("alArea")) {
								alArea = al.get(keyname).toString().trim();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(alGrad); // 사업지구 표고구분
							pw.write("|^");
							pw.write(alAreaRate); // 표고별 면적비율
							pw.write("|^");
							pw.write(alArea); // 표고별 면적
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					logger.info("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_26_" + mgtNo + ".dat", "EIA");

				} else if (resultCode.equals("03")) {
					logger.debug("data not exist!! mgtNo :" + mgtNo);
				} else {
					logger.debug("parsing error!! mgtNo :" + mgtNo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("mgtNo :" + mgtNo);
			}

		} else {
			logger.debug("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
