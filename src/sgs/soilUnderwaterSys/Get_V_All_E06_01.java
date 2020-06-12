package sgs.soilUnderwaterSys;

import java.sql.SQLException;

import common.DBConnection;

public class Get_V_All_E06_01 {

	//토양지하수 정보시스템 - 먹는물 공동시설
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {
			
			/*Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
*/			
			String query = DBConnection.getScienceProperty("kwa_oracle_kwa01_query");
			System.out.println("query :::" + query);
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
