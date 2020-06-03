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

			try {

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa03_query");
				System.out.println("query :::" + query);

				conn = DBConnection.getOraConnection("kwa");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] alertObject = new String[rowCount]; // 경보대상
				String[] alertPoint = new String[rowCount]; // 지점
				String[] alertInstitute = new String[rowCount]; // 발령기관
				String[] alertDate = new String[rowCount]; // 발령일
				String[] stage = new String[rowCount]; // 단계
				String[] etc = new String[rowCount]; // 비고
				String[] instId = new String[rowCount]; //입력일
				String[] instDate = new String[rowCount]; //입력일자  

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						alertObject[i] = rs.getString(1);
						alertPoint[i] = rs.getString(2);
						alertInstitute[i] = rs.getString(3);
						alertDate[i] = rs.getString(4);
						stage[i] = rs.getString(5);
						etc[i] = rs.getString(6);
						instId[i] = rs.getString(7);
						instDate[i] = rs.getString(8);

						System.out.println("alertObject::" + alertObject[i] + "::alertPoint::" + alertPoint[i] + "::alertInstitute::" + alertInstitute[i]
								+ "alertDate::" + alertDate[i] + "stage::" + stage[i] + "etc::"
								+ etc[i] + "instId::" + instId[i] + "instDate::" + instDate[i]);

						i++;

					}

					System.out.println("KWA_03 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						alertObject[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						alertPoint[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						alertInstitute[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						alertDate[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						stage[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						etc[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						instId[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						instDate[i] = JsonParser.colWrite_String_eic(rs.getString(8));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(alertObject[i]);
							pw.write("|^");
							pw.write(alertPoint[i]);
							pw.write("|^");
							pw.write(alertInstitute[i]);
							pw.write("|^");
							pw.write(alertDate[i]);
							pw.write("|^");
							pw.write(stage[i]);
							pw.write("|^");
							pw.write(etc[i]);
							pw.write("|^");
							pw.write(instId[i]);
							pw.write("|^");
							pw.write(instDate[i]);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						i++;

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

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
