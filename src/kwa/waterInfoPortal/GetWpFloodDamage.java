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

public class GetWpFloodDamage {

	//물정보포털 - WP_태풍피해현황
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa07_query");
				System.out.println("query :::" + query);

				conn = DBConnection.getOraConnection("kwa");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] num = new String[rowCount]; //일련번호
				String[] tabNo = new String[rowCount]; //탭번호
				String[] rowNo = new String[rowCount]; //행번호
				String[] odr = new String[rowCount]; //정렬순서
				String[] colVal = new String[rowCount]; //내용
				String[] colSpan = new String[rowCount]; //병합갯수
				String[] rowSpan = new String[rowCount]; //행병합갯수
				String[] thYn = new String[rowCount]; //TH_YN
				String[] colEnd = new String[rowCount]; //컬럼구분
				String[] clsExist = new String[rowCount]; //문단구분

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						num[i] = rs.getString(1);
						tabNo[i] = rs.getString(2);
						rowNo[i] = rs.getString(3);
						odr[i] = rs.getString(4);
						colVal[i] = rs.getString(5);
						colSpan[i] = rs.getString(6);
						rowSpan[i] = rs.getString(7);
						thYn[i] = rs.getString(8);
						colEnd[i] = rs.getString(9);
						clsExist[i] = rs.getString(10);

						System.out.println("num::" + num[i] + "::tabNo::" + tabNo[i] + "::rowNo::" + rowNo[i]
								+ "odr::" + odr[i] + "colVal::" + colVal[i] + "colSpan::"
								+ colSpan[i] + "::rowSpan::" + rowSpan[i]+ "::thYn::" + thYn[i] + "::colEnd::" + colEnd[i] 
										+ "::clsExist::" + clsExist[i]);

						i++;

					}

					System.out.println("KWA_07 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						num[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						tabNo[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						rowNo[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						odr[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						colVal[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						colSpan[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						rowSpan[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						thYn[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						colEnd[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						clsExist[i] = JsonParser.colWrite_String_eic(rs.getString(10));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(num[i]);
							pw.write("|^");
							pw.write(tabNo[i]);
							pw.write("|^");
							pw.write(rowNo[i]);
							pw.write("|^");
							pw.write(odr[i]);
							pw.write("|^");
							pw.write(colVal[i]);
							pw.write("|^");
							pw.write(colSpan[i]);
							pw.write("|^");
							pw.write(rowSpan[i]);
							pw.write("|^");
							pw.write(thYn[i]);
							pw.write("|^");
							pw.write(colEnd[i]);
							pw.write("|^");
							pw.write(clsExist[i]);
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

					System.out.println("KWA_07 SELECT 파일 생성 프로세스 종료.");

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