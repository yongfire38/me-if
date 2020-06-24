package tmd.waterQualityTotalInfoSys;

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

public class GetExmWqEvalIf {

	// 수질총량 정보시스템 - 수질형황 목표수질 및 평가수질 TAB 화면 수정
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			// sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {

				conn = DBConnection.getOraConnection("tmd");

				String query = DBConnection.getScienceProperty("tmd_oracle_tmd01_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(100);

				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");

				rs.setFetchSize(100);

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						String WATERDGR_CD = " ";
						String TA_LEVEL = " ";

						WATERDGR_CD = rs.getString(1);
						TA_LEVEL = rs.getString(2);

						System.out.println("WATERDGR_CD::" + WATERDGR_CD + "::TA_LEVEL::" + TA_LEVEL);

					}

					System.out.println("TMD_01 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						String WATERDGR_CD = " "; // 수계코드
						String WATERDGR_NM = " "; // 수계명칭
						String TA_LEVEL = " "; // 총량단계
						String UNITDGR_CD = " "; // 단위유역코드
						String UNITDGR_NM = " "; // 단위유역명칭
						String P_ITEM = " "; // 대상물질
						String TARGET_WQ = " "; // 목표수질

						WATERDGR_CD = JsonParser.colWrite_String_eic(rs.getString(1));
						WATERDGR_NM = JsonParser.colWrite_String_eic(rs.getString(2));
						TA_LEVEL = JsonParser.colWrite_String_eic(rs.getString(3));
						UNITDGR_CD = JsonParser.colWrite_String_eic(rs.getString(4));
						UNITDGR_NM = JsonParser.colWrite_String_eic(rs.getString(5));
						P_ITEM = JsonParser.colWrite_String_eic(rs.getString(6));
						TARGET_WQ = JsonParser.colWrite_String_eic(rs.getString(7));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(WATERDGR_CD);
							pw.write("|^");
							pw.write(WATERDGR_NM);
							pw.write("|^");
							pw.write(TA_LEVEL);
							pw.write("|^");
							pw.write(UNITDGR_CD);
							pw.write("|^");
							pw.write(UNITDGR_NM);
							pw.write("|^");
							pw.write(P_ITEM);
							pw.write("|^");
							pw.write(TARGET_WQ);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						// System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
						if(rs.getRow() % 10000 == 0){
							System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
						}

					}

					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("TMD_01 SELECT 파일 생성 프로세스 종료.");

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
