package sgs.soilUnderwaterSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;

public class ConnectionTest {

	// 토양지하수 정보시스템 - 커넥션 테스트
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection conn = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstm = null; // SQL 문을 나타내는 객체
		ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

		try {

			conn = DBConnection.getOraConnection("sgs");

			String query = DBConnection.getScienceProperty("sgs_oracle_sgs01_query");
			System.out.println("query :::" + query);

			pstm = conn.prepareStatement(query);
			pstm.setFetchSize(100);

			System.out.println("start query");
			rs = pstm.executeQuery();
			System.out.println("done query");

			rs.setFetchSize(100);

			while (rs.next()) {

				// 전체 레코드 개수만큼
				String col = " ";

				col = rs.getString(1);

				System.out.println("데이터::" + col);

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

	}

}
