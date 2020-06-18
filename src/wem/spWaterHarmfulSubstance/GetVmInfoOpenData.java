package wem.spWaterHarmfulSubstance;

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

public class GetVmInfoOpenData {
	
	//특정수질유해물질 배출량 조사 시스템 - 정보공개 데이터 뷰
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {
			
			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			// sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";
			
			try {
				
				conn = DBConnection.getOraConnection("wem");

				String query = DBConnection.getScienceProperty("wem_oracle_wem01_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(100);

				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");

				rs.setFetchSize(100);
				
				if (args[0].equals("_tset")) {

					while (rs.next()) {

						String SSWTY_CD = " ";
						String STDR_YEAR = " ";

						SSWTY_CD = rs.getString(1);
						STDR_YEAR = rs.getString(2);

						System.out.println("SSWTY_CD::" + SSWTY_CD + "::STDR_YEAR::" + STDR_YEAR);

					}

					System.out.println("WEM_01 SELECT 프로세스 종료.");

				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						String SSWTY_CD = " ";
						String STDR_YEAR = " ";
						String BPLCNM = " ";
						String BIZRNO = " ";
						String SSWTY_YN = " ";
						String BPLC_ADDR = " ";
						String SIGUNGU_CD = " ";
						String SSWTY_NM = " ";
						String EMI_AMOUNT = " ";
						String EMI_CALC = " ";
						String WASWTR_DISC_TYPE_NM = " ";
						String SSWTY_SUM = " ";
						String BPLC_SUM = " ";
						String RNUM = " ";
						
						SSWTY_CD = JsonParser.colWrite_String_eic(rs.getString(1));
						STDR_YEAR = JsonParser.colWrite_String_eic(rs.getString(2));
						BPLCNM = JsonParser.colWrite_String_eic(rs.getString(3));
						BIZRNO = JsonParser.colWrite_String_eic(rs.getString(4));
						SSWTY_YN = JsonParser.colWrite_String_eic(rs.getString(5));
						BPLC_ADDR = JsonParser.colWrite_String_eic(rs.getString(6));
						SIGUNGU_CD = JsonParser.colWrite_String_eic(rs.getString(7));
						SSWTY_NM = JsonParser.colWrite_String_eic(rs.getString(8));
						EMI_AMOUNT = JsonParser.colWrite_String_eic(rs.getString(9));
						EMI_CALC = JsonParser.colWrite_String_eic(rs.getString(10));
						WASWTR_DISC_TYPE_NM = JsonParser.colWrite_String_eic(rs.getString(11));
						SSWTY_SUM = JsonParser.colWrite_String_eic(rs.getString(12));
						BPLC_SUM = JsonParser.colWrite_String_eic(rs.getString(13));
						RNUM = JsonParser.colWrite_String_eic(rs.getString(14));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(SSWTY_CD);
							pw.write("|^");
							pw.write(STDR_YEAR);
							pw.write("|^");
							pw.write(BPLCNM);
							pw.write("|^");
							pw.write(BIZRNO);
							pw.write("|^");
							pw.write(SSWTY_YN);
							pw.write("|^");
							pw.write(BPLC_ADDR);
							pw.write("|^");
							pw.write(SIGUNGU_CD);
							pw.write("|^");
							pw.write(SSWTY_NM);
							pw.write("|^");
							pw.write(EMI_AMOUNT);
							pw.write("|^");
							pw.write(EMI_CALC);
							pw.write("|^");
							pw.write(WASWTR_DISC_TYPE_NM);
							pw.write("|^");
							pw.write(SSWTY_SUM);
							pw.write("|^");
							pw.write(BPLC_SUM);
							pw.write("|^");
							pw.write(RNUM);							
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						// System.out.println("진행도 :::" +
						// Integer.toString(rs.getRow()) + "/" +
						// Integer.toString(rowCount) + " 건");
						
						if(rs.getRow() % 10000 == 0){
							System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
						}
						
		
						
					}
					
					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("WEM_01 SELECT 파일 생성 프로세스 종료.");
					
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
				
				// 쿼리에서 에러 발생시에는 종료로 빠진다.
				if (cf.equals("Y")) {
					System.exit(-1);
				}

			}
			
			
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
