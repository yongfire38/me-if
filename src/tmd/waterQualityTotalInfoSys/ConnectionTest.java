package tmd.waterQualityTotalInfoSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;

public class ConnectionTest {

	// 수질총량정보 시스템 - 커넥션 테스트
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection conn = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstm = null; // SQL 문을 나타내는 객체
		ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
		
		//sql 쿼리 에러시 로그 확인용 변수
		String cf = "N";
		
		try {
			
			conn = DBConnection.getOraConnection("tmd");
			
			String query = DBConnection.getScienceProperty("tmd_oracle_tmd00_query");
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

	}

}
