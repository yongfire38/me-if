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

public class GetAlgaeAlertCoordinate {
	
	//물정보포털 - 조류현황 지점 위,경도 좌표값
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		if (args.length == 1) {
			
			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			try {
				
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa01_query");
				System.out.println("query :::" + query);
				
				conn = DBConnection.getOraConnection("kwa");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();
				
				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();
				
				System.out.println("전체 건 수 :::" + Integer.toString(rowCount)+" 건");
				
				// 전체 레코드 개수만큼의 배열
				String[] alertPointCode = new String[rowCount]; //지점코드
				String[] latitude = new String[rowCount]; //위도
				String[] longgityde = new String[rowCount]; //경도
				String[] mobileUseYn = new String[rowCount]; //모바일사용여부
				String[] regId = new String[rowCount]; //등록자
				String[] regDt = new String[rowCount]; //등록일자
				String[] modId = new String[rowCount]; //수정자
				String[] modDt = new String[rowCount]; //수정일자
				
				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;
				
				if (args[0].equals("_tset")) {
					
					while (rs.next()) {
						
						alertPointCode[i] = rs.getString(1);
						latitude[i] = rs.getString(2);
						longgityde[i] = rs.getString(3);
						mobileUseYn[i] = rs.getString(4);
						regId[i] = rs.getString(5);
						regDt[i] = rs.getString(6);
						modId[i] = rs.getString(7);
						modDt[i] = rs.getString(8);
						
						System.out.println("alertPointCode::" + alertPointCode[i] + "::latitude::" + latitude[i] + "::longgityde::" + longgityde[i]
								+ "mobileUseYn::" + mobileUseYn[i] + "regId::" + regId[i] + "regDt::" + regDt[i] + "modId::" + modId[i] 
								+ "modDt::" + modDt[i]);
						
						i++;
						
					}
					
					System.out.println("KWA_01 SELECT 프로세스 종료.");	
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						alertPointCode[i] = JsonParser.colWrite_String_eic(rs.getString(1));
						latitude[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						longgityde[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						mobileUseYn[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						regId[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						regDt[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						modId[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						modDt[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(alertPointCode[i]);
							pw.write("|^");
							pw.write(latitude[i]);
							pw.write("|^");
							pw.write(longgityde[i]);
							pw.write("|^");
							pw.write(mobileUseYn[i]);
							pw.write("|^");
							pw.write(regId[i]);
							pw.write("|^");
							pw.write(regDt[i]);
							pw.write("|^");
							pw.write(modId[i]);
							pw.write("|^");
							pw.write(modDt[i]);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						i++;
						
					}
					
					if(file.exists()){
						System.out.println("파일이 생성되었습니다.");
					}else{
						System.out.println("파일이 생성되지 않았습니다.");
					}
					
					System.out.println("KWA_01 SELECT 파일 생성 프로세스 종료.");		
					
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
