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
				
				conn = DBConnection.getOraConnection("kwa");
				
				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				String query = DBConnection.getProperty("kwa_oracle_kwa01_query");
				System.out.println("query :::" + query);
				
				conn.setAutoCommit(false);
				
				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(1);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(1);

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				/*rs.last();

				int rowCount = rs.getRow();
				
				System.out.println("전체 건 수 :::" + Integer.toString(rowCount)+" 건");
				
				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();*/
				
				if (args[0].equals("_tset")) {
					
					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String alertPointCode = " "; //지점코드
						String latitude = " "; //위도
						String longgityde = " "; //경도
						String mobileUseYn = " "; //모바일사용여부
						String regId = " "; //등록자
						String regDt = " "; //등록일자
						String modId = " "; //수정자
						String modDt = " "; //수정일자
						
						alertPointCode = rs.getString(1);
						latitude = rs.getString(2);
						longgityde = rs.getString(3);
						mobileUseYn = rs.getString(4);
						regId = rs.getString(5);
						regDt = rs.getString(6);
						modId = rs.getString(7);
						modDt = rs.getString(8);
						
						System.out.println("alertPointCode::" + alertPointCode + "::latitude::" + latitude + "::longgityde::" + longgityde
								+ "mobileUseYn::" + mobileUseYn + "regId::" + regId + "regDt::" + regDt + "modId::" + modId 
								+ "modDt::" + modDt);
						
					}
					
					System.out.println("KWA_01 SELECT 프로세스 종료.");	
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String alertPointCode = " "; //지점코드
						String latitude = " "; //위도
						String longgityde = " "; //경도
						String mobileUseYn = " "; //모바일사용여부
						String regId = " "; //등록자
						String regDt = " "; //등록일자
						String modId = " "; //수정자
						String modDt = " "; //수정일자
						
						alertPointCode = JsonParser.colWrite_String_eic(rs.getString(1));
						latitude = JsonParser.colWrite_String_eic(rs.getString(2));
						longgityde = JsonParser.colWrite_String_eic(rs.getString(3));
						mobileUseYn = JsonParser.colWrite_String_eic(rs.getString(4));
						regId = JsonParser.colWrite_String_eic(rs.getString(5));
						regDt = JsonParser.colWrite_String_eic(rs.getString(6));
						modId = JsonParser.colWrite_String_eic(rs.getString(7));
						modDt = JsonParser.colWrite_String_eic(rs.getString(8));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(alertPointCode);
							pw.write("|^");
							pw.write(latitude);
							pw.write("|^");
							pw.write(longgityde);
							pw.write("|^");
							pw.write(mobileUseYn);
							pw.write("|^");
							pw.write(regId);
							pw.write("|^");
							pw.write(regDt);
							pw.write("|^");
							pw.write(modId);
							pw.write("|^");
							pw.write(modDt);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						//System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
						System.out.println("진행도 :::" + Integer.toString(rs.getRow()) +"번째 줄");
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
