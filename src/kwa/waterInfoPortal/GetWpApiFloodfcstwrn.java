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

public class GetWpApiFloodfcstwrn {

	//물정보포털 - 홍수예경보 발령
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa04_query");
				System.out.println("query :::" + query);

				conn = DBConnection.getOraConnection("kwa");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] ancdt = new String[rowCount]; //발표일시
				String[] ancnm = new String[rowCount]; //발표자
				String[] fctdt = new String[rowCount]; //수위 도달 예상일시
				String[] fcthgt = new String[rowCount]; //예상 수위표수위
				String[] fctsealvl = new String[rowCount]; //예상 해발수위
				String[] kind = new String[rowCount]; //홍수예보 종류
				String[] no = new String[rowCount]; //홍수예보 번호
				String[] obsnm = new String[rowCount]; //지점
				String[] prntdt = new String[rowCount]; //기존 발령일시
				String[] rfrnc2 = new String[rowCount]; //비고
				String[] rvrnm = new String[rowCount]; //강명
				String[] sttcurcng = new String[rowCount]; //변동상황
				String[] sttcurdt = new String[rowCount]; //현재 일시
				String[] sttcurhgt = new String[rowCount]; //현재 수위표순위
				String[] sttcursealvl = new String[rowCount]; //현재 해발수위
				String[] sttfctdt = new String[rowCount]; //예상 일시
				String[] sttfcthgt = new String[rowCount]; //예상 수위표순위
				String[] sttfctsealvl = new String[rowCount]; //예상 해발수위
				String[] sttnm = new String[rowCount]; //관측소코드
				String[] wrnaranm = new String[rowCount]; //주위 지역
				String[] wrnrvrnm = new String[rowCount]; //주위 강명
				String[] rgsdt = new String[rowCount]; //등록일자
				String[] upddt = new String[rowCount]; //수정일자  

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						ancdt[i] = rs.getString(1);
						ancnm[i] = rs.getString(2);
						fctdt[i] = rs.getString(3);
						fcthgt[i] = rs.getString(4);
						fctsealvl[i] = rs.getString(5);
						kind[i] = rs.getString(6);
						no[i] = rs.getString(7);
						obsnm[i] = rs.getString(8);
						prntdt[i] = rs.getString(9);
						rfrnc2[i] = rs.getString(10);
						rvrnm[i] = rs.getString(11);
						sttcurcng[i] = rs.getString(12);
						sttcurdt[i] = rs.getString(13);
						sttcurhgt[i] = rs.getString(14);
						sttcursealvl[i] = rs.getString(15);
						sttfctdt[i] = rs.getString(16);
						sttfcthgt[i] = rs.getString(17);
						sttfctsealvl[i] = rs.getString(18);
						sttnm[i] = rs.getString(19);
						wrnaranm[i] = rs.getString(20);
						wrnrvrnm[i] = rs.getString(21);
						rgsdt[i] = rs.getString(22);
						upddt[i] = rs.getString(23);

						System.out.println("ancdt::" + ancdt[i] + "::ancnm::" + ancnm[i] + "::fctdt::" + fctdt[i]
								+ "fcthgt::" + fcthgt[i] + "fctsealvl::" + fctsealvl[i] + "kind::"
								+ kind[i] + "no::" + no[i] + "obsnm::" + obsnm[i] + "prntdt::" + prntdt[i] + "rfrnc2::" + rfrnc2[i]
								+ "rvrnm::" + rvrnm[i] + "sttcurcng::" + sttcurcng[i] + "sttcurdt::" + sttcurdt[i] + "sttcurhgt::" + sttcurhgt[i]
								+ "sttcursealvl::" + sttcursealvl[i] + "sttfctdt::" + sttfctdt[i] + "sttfcthgt::" + sttfcthgt[i] + "sttfctsealvl::" + sttfctsealvl[i]
								+ "sttnm::" + sttnm[i] + "wrnaranm::" + wrnaranm[i] + "wrnrvrnm::" + wrnrvrnm[i] + "rgsdt::" + rgsdt[i] + "upddt::" + upddt[i]);

						i++;

					}

					System.out.println("KWA_04 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						ancdt[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						ancnm[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						fctdt[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						fcthgt[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						fctsealvl[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						kind[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						no[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						obsnm[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						prntdt[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						rfrnc2[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						rvrnm[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						sttcurcng[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						sttcurdt[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						sttcurhgt[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						sttcursealvl[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						sttfctdt[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						sttfcthgt[i] = JsonParser.colWrite_String_eic(rs.getString(17));
						sttfctsealvl[i] = JsonParser.colWrite_String_eic(rs.getString(18));
						sttnm[i] = JsonParser.colWrite_String_eic(rs.getString(19));
						wrnaranm[i] = JsonParser.colWrite_String_eic(rs.getString(20));
						wrnrvrnm[i] = JsonParser.colWrite_String_eic(rs.getString(21));
						rgsdt[i] = JsonParser.colWrite_String_eic(rs.getString(22));
						upddt[i] = JsonParser.colWrite_String_eic(rs.getString(23));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(ancdt[i]);
							pw.write("|^");
							pw.write(ancnm[i]);
							pw.write("|^");
							pw.write(fctdt[i]);
							pw.write("|^");
							pw.write(fcthgt[i]);
							pw.write("|^");
							pw.write(fctsealvl[i]);
							pw.write("|^");
							pw.write(kind[i]);
							pw.write("|^");
							pw.write(no[i]);
							pw.write("|^");
							pw.write(obsnm[i]);
							pw.write("|^");
							pw.write(prntdt[i]);
							pw.write("|^");
							pw.write(rfrnc2[i]);
							pw.write("|^");
							pw.write(rvrnm[i]);
							pw.write("|^");
							pw.write(sttcurcng[i]);
							pw.write("|^");
							pw.write(sttcurdt[i]);
							pw.write("|^");
							pw.write(sttcurhgt[i]);
							pw.write("|^");
							pw.write(sttcursealvl[i]);
							pw.write("|^");
							pw.write(sttfctdt[i]);
							pw.write("|^");
							pw.write(sttfcthgt[i]);
							pw.write("|^");
							pw.write(sttfctsealvl[i]);
							pw.write("|^");
							pw.write(sttnm[i]);
							pw.write("|^");
							pw.write(wrnaranm[i]);
							pw.write("|^");
							pw.write(wrnrvrnm[i]);
							pw.write("|^");
							pw.write(rgsdt[i]);
							pw.write("|^");
							pw.write(upddt[i]);
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

					System.out.println("KWA_04 SELECT 파일 생성 프로세스 종료.");

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
