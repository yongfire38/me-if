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
				
				conn = DBConnection.getOraConnection("kwa");

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa07_query");
				System.out.println("query :::" + query);

				conn.setAutoCommit(false);
				
				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(1);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(1);

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				/*rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();*/

				if (args[0].equals("_tset")) {

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String num = " "; //일련번호
						String tabNo = " "; //탭번호
						String rowNo = " "; //행번호
						String odr = " "; //정렬순서
						String colVal = " "; //내용
						String colSpan = " "; //병합갯수
						String rowSpan = " "; //행병합갯수
						String thYn = " "; //TH_YN
						String colEnd = " "; //컬럼구분
						String clsExist = " "; //문단구분

						num = rs.getString(1);
						tabNo = rs.getString(2);
						rowNo = rs.getString(3);
						odr = rs.getString(4);
						colVal = rs.getString(5);
						colSpan = rs.getString(6);
						rowSpan = rs.getString(7);
						thYn = rs.getString(8);
						colEnd = rs.getString(9);
						clsExist = rs.getString(10);

						System.out.println("num::" + num + "::tabNo::" + tabNo + "::rowNo::" + rowNo
								+ "odr::" + odr + "colVal::" + colVal + "colSpan::"
								+ colSpan + "::rowSpan::" + rowSpan+ "::thYn::" + thYn + "::colEnd::" + colEnd 
										+ "::clsExist::" + clsExist);

					}

					System.out.println("KWA_07 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String num = " "; //일련번호
						String tabNo = " "; //탭번호
						String rowNo = " "; //행번호
						String odr = " "; //정렬순서
						String colVal = " "; //내용
						String colSpan = " "; //병합갯수
						String rowSpan = " "; //행병합갯수
						String thYn = " "; //TH_YN
						String colEnd = " "; //컬럼구분
						String clsExist = " "; //문단구분

						num = JsonParser.colWrite_String_eic(rs.getString(1));
						tabNo = JsonParser.colWrite_String_eic(rs.getString(2));
						rowNo = JsonParser.colWrite_String_eic(rs.getString(3));
						odr = JsonParser.colWrite_String_eic(rs.getString(4));
						colVal = JsonParser.colWrite_String_eic(rs.getString(5));
						colSpan = JsonParser.colWrite_String_eic(rs.getString(6));
						rowSpan = JsonParser.colWrite_String_eic(rs.getString(7));
						thYn = JsonParser.colWrite_String_eic(rs.getString(8));
						colEnd = JsonParser.colWrite_String_eic(rs.getString(9));
						clsExist = JsonParser.colWrite_String_eic(rs.getString(10));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(num);
							pw.write("|^");
							pw.write(tabNo);
							pw.write("|^");
							pw.write(rowNo);
							pw.write("|^");
							pw.write(odr);
							pw.write("|^");
							pw.write(colVal);
							pw.write("|^");
							pw.write(colSpan);
							pw.write("|^");
							pw.write(rowSpan);
							pw.write("|^");
							pw.write(thYn);
							pw.write("|^");
							pw.write(colEnd);
							pw.write("|^");
							pw.write(clsExist);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						//System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
						System.out.println("진행도 :::" + Integer.toString(rs.getRow()) +"번째 줄");
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
