package kwa.waterInfoPortal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;
import common.JsonParser;

public class GetAlgaeAlertHistory {

	//물정보포털 - 조류현황 이력
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			//sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {
				
				conn = DBConnection.getOraConnection("kwa");

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa03_query");
				System.out.println("query :::" + query);

				//conn.setAutoCommit(false);
				
				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(100);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(100);

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				/*rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();*/

				if (args[0].equals("_tset")) {

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String alertObject = " "; // 경보대상
						String alertPoint = " "; // 지점
						String alertInstitute = " "; // 발령기관
						String alertDate = " "; // 발령일
						String stage = " "; // 단계
						String etc = " "; // 비고
						String instId = " "; //입력일
						String instDate = " "; //입력일자  

						alertObject = rs.getString(1);
						alertPoint = rs.getString(2);
						alertInstitute = rs.getString(3);
						alertDate = rs.getString(4);
						stage = rs.getString(5);
						etc = rs.getString(6);
						instId = rs.getString(7);
						instDate = rs.getString(8);

						System.out.println("alertObject::" + alertObject + "::alertPoint::" + alertPoint + "::alertInstitute::" + alertInstitute
								+ "alertDate::" + alertDate + "stage::" + stage + "etc::"
								+ etc + "instId::" + instId + "instDate::" + instDate);

					}

					System.out.println("KWA_03 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String alertObject = " "; // 경보대상
						String alertPoint = " "; // 지점
						String alertInstitute = " "; // 발령기관
						String alertDate = " "; // 발령일
						String stage = " "; // 단계
						String etc = " "; // 비고
						String instId = " "; //입력일
						String instDate = " "; //입력일자  

						alertObject = JsonParser.colWrite_String_eic(rs.getString(1));
						alertPoint = JsonParser.colWrite_String_eic(rs.getString(2));
						alertInstitute = JsonParser.colWrite_String_eic(rs.getString(3));
						alertDate = JsonParser.colWrite_String_eic(rs.getString(4));
						stage = JsonParser.colWrite_String_eic(rs.getString(5));
						etc = JsonParser.colWrite_String_eic(rs.getString(6));
						instId = JsonParser.colWrite_String_eic(rs.getString(7));
						instDate = JsonParser.colWrite_String_eic(rs.getString(8));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(alertObject);
							pw.write("|^");
							pw.write(alertPoint);
							pw.write("|^");
							pw.write(alertInstitute);
							pw.write("|^");
							pw.write(alertDate);
							pw.write("|^");
							pw.write(stage);
							pw.write("|^");
							pw.write(etc);
							pw.write("|^");
							pw.write(instId);
							pw.write("|^");
							pw.write(instDate);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						//System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");

						if(rs.getRow() % 10000 == 0){
							System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
						}
					}

					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("KWA_03 SELECT 파일 생성 프로세스 종료.");

				}

			} catch (SQLException sqle) {

				System.out.println("SELECT문에서 예외 발생");
				
				cf = "Y";
				sqle.printStackTrace();

			} finally {
				// DB 연결을 종료한다.
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstm != null) {
						pstm.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
				
				//쿼리에서 에러 발생시에는 종료로 빠진다.
				if(cf.equals("Y")) {
					System.exit(-1);
				}

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
