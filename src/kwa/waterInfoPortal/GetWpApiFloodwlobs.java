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

public class GetWpApiFloodwlobs {

	//물정보포털 - 수위관측소 제원정보
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa05_query");
				System.out.println("query :::" + query);

				conn = DBConnection.getOraConnection("kwa");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();

				System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

				// 전체 레코드 개수만큼의 배열
				String[] wlobscd = new String[rowCount]; //수위관측소 코드
				String[] obsnm = new String[rowCount]; //수위관측소 명칭
				String[] agcnm = new String[rowCount]; //관할기관
				String[] addr = new String[rowCount]; //주소
				String[] etcaddr = new String[rowCount]; //나머지 주소
				String[] lon = new String[rowCount]; //경도(세계측지계)
				String[] lat = new String[rowCount]; //위도(세계측지계)
				String[] gdt = new String[rowCount]; //영점표고(EL.m)
				String[] wrnwl = new String[rowCount]; //주의보 수위(m)
				String[] almwl = new String[rowCount]; //경보수위(m)
				String[] pfh = new String[rowCount]; //계획홍수위(m)
				String[] rgsdt = new String[rowCount]; //등록일자
				String[] upddt = new String[rowCount]; //수정일자

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						wlobscd[i] = rs.getString(1);
						obsnm[i] = rs.getString(2);
						agcnm[i] = rs.getString(3);
						addr[i] = rs.getString(4);
						etcaddr[i] = rs.getString(5);
						lon[i] = rs.getString(6);
						lat[i] = rs.getString(7);
						gdt[i] = rs.getString(8);
						wrnwl[i] = rs.getString(9);
						almwl[i] = rs.getString(10);
						pfh[i] = rs.getString(11);
						rgsdt[i] = rs.getString(12);
						upddt[i] = rs.getString(13);

						System.out.println("wlobscd::" + wlobscd[i] + "::obsnm::" + obsnm[i] + "::agcnm::" + agcnm[i]
								+ "addr::" + addr[i] + "etcaddr::" + etcaddr[i] + "lon::"
								+ lon[i] + "lat::" + lat[i] + "gdt::" + gdt[i] + "wrnwl::" + wrnwl[i] + "almwl::" + almwl[i]
								+ "pfh::" + pfh[i] + "rgsdt::" + rgsdt[i] + "upddt::" + upddt[i]);

						i++;

					}

					System.out.println("KWA_05 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						wlobscd[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						obsnm[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						agcnm[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						addr[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						etcaddr[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						lon[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						lat[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						gdt[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						wrnwl[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						almwl[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						pfh[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						rgsdt[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						upddt[i] = JsonParser.colWrite_String_eic(rs.getString(13));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(wlobscd[i]);
							pw.write("|^");
							pw.write(obsnm[i]);
							pw.write("|^");
							pw.write(agcnm[i]);
							pw.write("|^");
							pw.write(addr[i]);
							pw.write("|^");
							pw.write(etcaddr[i]);
							pw.write("|^");
							pw.write(lon[i]);
							pw.write("|^");
							pw.write(lat[i]);
							pw.write("|^");
							pw.write(gdt[i]);
							pw.write("|^");
							pw.write(wrnwl[i]);
							pw.write("|^");
							pw.write(almwl[i]);
							pw.write("|^");
							pw.write(pfh[i]);
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

					System.out.println("KWA_05 SELECT 파일 생성 프로세스 종료.");

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
