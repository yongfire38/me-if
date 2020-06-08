package eco.ecobank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;

public class GetWpMapEcologympArea {

	// 에코뱅크 - 생태자연도_면
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection conn = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstm = null; // SQL 문을 나타내는 객체
		ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

		try {

			Class.forName(DBConnection.getProperty("eco_post_driver"));
			conn = DBConnection.getOraConnection("eco");
			// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
			// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
			// String query = DBConnection.getProperty("");
			String query = "SELECT table_schema,table_name FROM information_schema.tables ORDER BY table_schema,table_name";
			System.out.println("query :::" + query);

			pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = pstm.executeQuery();

			// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
			rs.last();

			int rowCount = rs.getRow();

			System.out.println("전체 건 수 :::" + Integer.toString(rowCount) + " 건");

			// 전체 레코드 개수만큼의 배열
			String[] table_schema = new String[rowCount];
			String[] table_name = new String[rowCount];

			// 다시 처음부터 조회해야 하므로 커서는 초기화
			rs.beforeFirst();

			int i = 0;

			while (rs.next()) {

				table_schema[i] = rs.getString(1);
				table_name[i] = rs.getString(2);

				System.out.println("table_schema::" + table_schema[i] + "::table_name::" + table_name[i]);
				i++;
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
