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
			
			//sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {
				
				conn = DBConnection.getOraConnection("kwa");

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa04_query");
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
						String ancdt = " "; //발표일시
						String ancnm = " "; //발표자
						String fctdt = " "; //수위 도달 예상일시
						String fcthgt = " "; //예상 수위표수위
						String fctsealvl = " "; //예상 해발수위
						String kind = " "; //홍수예보 종류
						String no = " "; //홍수예보 번호
						String obsnm = " "; //지점
						String prntdt = " "; //기존 발령일시
						String rfrnc2 = " "; //비고
						String rvrnm = " "; //강명
						String sttcurcng = " "; //변동상황
						String sttcurdt = " "; //현재 일시
						String sttcurhgt = " "; //현재 수위표순위
						String sttcursealvl = " "; //현재 해발수위
						String sttfctdt = " "; //예상 일시
						String sttfcthgt = " "; //예상 수위표순위
						String sttfctsealvl = " "; //예상 해발수위
						String sttnm = " "; //관측소코드
						String wrnaranm = " "; //주위 지역
						String wrnrvrnm = " "; //주위 강명
						String rgsdt = " "; //등록일자
						String upddt = " "; //수정일자  

						ancdt = rs.getString(1);
						ancnm = rs.getString(2);
						fctdt = rs.getString(3);
						fcthgt = rs.getString(4);
						fctsealvl = rs.getString(5);
						kind = rs.getString(6);
						no = rs.getString(7);
						obsnm = rs.getString(8);
						prntdt = rs.getString(9);
						rfrnc2 = rs.getString(10);
						rvrnm = rs.getString(11);
						sttcurcng = rs.getString(12);
						sttcurdt = rs.getString(13);
						sttcurhgt = rs.getString(14);
						sttcursealvl = rs.getString(15);
						sttfctdt = rs.getString(16);
						sttfcthgt = rs.getString(17);
						sttfctsealvl = rs.getString(18);
						sttnm = rs.getString(19);
						wrnaranm = rs.getString(20);
						wrnrvrnm = rs.getString(21);
						rgsdt = rs.getString(22);
						upddt = rs.getString(23);

						System.out.println("ancdt::" + ancdt + "::ancnm::" + ancnm + "::fctdt::" + fctdt
								+ "fcthgt::" + fcthgt + "fctsealvl::" + fctsealvl + "kind::"
								+ kind + "no::" + no + "obsnm::" + obsnm + "prntdt::" + prntdt + "rfrnc2::" + rfrnc2
								+ "rvrnm::" + rvrnm + "sttcurcng::" + sttcurcng + "sttcurdt::" + sttcurdt + "sttcurhgt::" + sttcurhgt
								+ "sttcursealvl::" + sttcursealvl + "sttfctdt::" + sttfctdt + "sttfcthgt::" + sttfcthgt + "sttfctsealvl::" + sttfctsealvl
								+ "sttnm::" + sttnm + "wrnaranm::" + wrnaranm + "wrnrvrnm::" + wrnrvrnm + "rgsdt::" + rgsdt + "upddt::" + upddt);


					}

					System.out.println("KWA_04 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String ancdt = " "; //발표일시
						String ancnm = " "; //발표자
						String fctdt = " "; //수위 도달 예상일시
						String fcthgt = " "; //예상 수위표수위
						String fctsealvl = " "; //예상 해발수위
						String kind = " "; //홍수예보 종류
						String no = " "; //홍수예보 번호
						String obsnm = " "; //지점
						String prntdt = " "; //기존 발령일시
						String rfrnc2 = " "; //비고
						String rvrnm = " "; //강명
						String sttcurcng = " "; //변동상황
						String sttcurdt = " "; //현재 일시
						String sttcurhgt = " "; //현재 수위표순위
						String sttcursealvl = " "; //현재 해발수위
						String sttfctdt = " "; //예상 일시
						String sttfcthgt = " "; //예상 수위표순위
						String sttfctsealvl = " "; //예상 해발수위
						String sttnm = " "; //관측소코드
						String wrnaranm = " "; //주위 지역
						String wrnrvrnm = " "; //주위 강명
						String rgsdt = " "; //등록일자
						String upddt = " "; //수정일자  

						ancdt = JsonParser.colWrite_String_eic(rs.getString(1));
						ancnm = JsonParser.colWrite_String_eic(rs.getString(2));
						fctdt = JsonParser.colWrite_String_eic(rs.getString(3));
						fcthgt = JsonParser.colWrite_String_eic(rs.getString(4));
						fctsealvl = JsonParser.colWrite_String_eic(rs.getString(5));
						kind = JsonParser.colWrite_String_eic(rs.getString(6));
						no = JsonParser.colWrite_String_eic(rs.getString(7));
						obsnm = JsonParser.colWrite_String_eic(rs.getString(8));
						prntdt = JsonParser.colWrite_String_eic(rs.getString(9));
						rfrnc2 = JsonParser.colWrite_String_eic(rs.getString(10));
						rvrnm = JsonParser.colWrite_String_eic(rs.getString(11));
						sttcurcng = JsonParser.colWrite_String_eic(rs.getString(12));
						sttcurdt = JsonParser.colWrite_String_eic(rs.getString(13));
						sttcurhgt = JsonParser.colWrite_String_eic(rs.getString(14));
						sttcursealvl = JsonParser.colWrite_String_eic(rs.getString(15));
						sttfctdt = JsonParser.colWrite_String_eic(rs.getString(16));
						sttfcthgt = JsonParser.colWrite_String_eic(rs.getString(17));
						sttfctsealvl = JsonParser.colWrite_String_eic(rs.getString(18));
						sttnm = JsonParser.colWrite_String_eic(rs.getString(19));
						wrnaranm = JsonParser.colWrite_String_eic(rs.getString(20));
						wrnrvrnm = JsonParser.colWrite_String_eic(rs.getString(21));
						rgsdt = JsonParser.colWrite_String_eic(rs.getString(22));
						upddt = JsonParser.colWrite_String_eic(rs.getString(23));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(ancdt);
							pw.write("|^");
							pw.write(ancnm);
							pw.write("|^");
							pw.write(fctdt);
							pw.write("|^");
							pw.write(fcthgt);
							pw.write("|^");
							pw.write(fctsealvl);
							pw.write("|^");
							pw.write(kind);
							pw.write("|^");
							pw.write(no);
							pw.write("|^");
							pw.write(obsnm);
							pw.write("|^");
							pw.write(prntdt);
							pw.write("|^");
							pw.write(rfrnc2);
							pw.write("|^");
							pw.write(rvrnm);
							pw.write("|^");
							pw.write(sttcurcng);
							pw.write("|^");
							pw.write(sttcurdt);
							pw.write("|^");
							pw.write(sttcurhgt);
							pw.write("|^");
							pw.write(sttcursealvl);
							pw.write("|^");
							pw.write(sttfctdt);
							pw.write("|^");
							pw.write(sttfcthgt);
							pw.write("|^");
							pw.write(sttfctsealvl);
							pw.write("|^");
							pw.write(sttnm);
							pw.write("|^");
							pw.write(wrnaranm);
							pw.write("|^");
							pw.write(wrnrvrnm);
							pw.write("|^");
							pw.write(rgsdt);
							pw.write("|^");
							pw.write(upddt);
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

					System.out.println("KWA_04 SELECT 파일 생성 프로세스 종료.");

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
