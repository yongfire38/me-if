package sgs.soilUnderwaterSys;

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

public class Get_V_All_G04_01 {

	//토양지하수 정보시스템 - 골프장 농약
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

if (args.length == 1) {
			
			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			try {
				
				conn = DBConnection.getOraConnection("sgs");
				
				String query = DBConnection.getScienceProperty("sgs_oracle_sgs04_query");
				System.out.println("query :::" + query);
				
				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(100);
				
				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");
				
				rs.setFetchSize(100);
				
				if (args[0].equals("_tset")) {
					
					while (rs.next()) {
						
						String STD_SPOT_CODE = " ";
						String YEAR = " ";
						
						STD_SPOT_CODE = rs.getString(1);
						YEAR = rs.getString(2);
						
						System.out.println("STD_SPOT_CODE::" + STD_SPOT_CODE + "::YEAR::" + YEAR);
						
						
					}
					
					System.out.println("SGS_04 SELECT 프로세스 종료.");	
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						String SPOT_STD_CODE = " ";
						String YEAR = " ";
						String GOLFCLUB_NAME = " ";
						String ADDRESS = " ";
						String HOLE_CNT = " ";
						String SIDO = " ";
						String REG_YEAR = " ";
						String GOLF_TYPE_NAME = " ";
						String PES_GROUND = " ";
						String TOTAL_GROUND = " ";
						String NONE_GROUND = " ";
						String SPRAY_GROUND = " ";
						String REAL_SUPPLY = " ";
						String HA_REAL = " ";
						String HA_SPRAY = " ";
						String THA_REAL = " ";
						String THA_SPRAY = " ";
						
						SPOT_STD_CODE = JsonParser.colWrite_String_eic(rs.getString(1));
						YEAR = JsonParser.colWrite_String_eic(rs.getString(2));
						GOLFCLUB_NAME = JsonParser.colWrite_String_eic(rs.getString(3));
						ADDRESS = JsonParser.colWrite_String_eic(rs.getString(4));
						HOLE_CNT = JsonParser.colWrite_String_eic(rs.getString(5));
						SIDO = JsonParser.colWrite_String_eic(rs.getString(6));
						REG_YEAR = JsonParser.colWrite_String_eic(rs.getString(7));
						GOLF_TYPE_NAME = JsonParser.colWrite_String_eic(rs.getString(8));
						PES_GROUND = JsonParser.colWrite_String_eic(rs.getString(9));
						TOTAL_GROUND = JsonParser.colWrite_String_eic(rs.getString(10));
						NONE_GROUND = JsonParser.colWrite_String_eic(rs.getString(11));
						SPRAY_GROUND = JsonParser.colWrite_String_eic(rs.getString(12));
						REAL_SUPPLY = JsonParser.colWrite_String_eic(rs.getString(13));
						HA_REAL = JsonParser.colWrite_String_eic(rs.getString(14));
						HA_SPRAY = JsonParser.colWrite_String_eic(rs.getString(15));
						THA_REAL = JsonParser.colWrite_String_eic(rs.getString(16));
						THA_SPRAY = JsonParser.colWrite_String_eic(rs.getString(17));
						
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(SPOT_STD_CODE);
							pw.write("|^");
							pw.write(YEAR);
							pw.write("|^");
							pw.write(GOLFCLUB_NAME);
							pw.write("|^");
							pw.write(ADDRESS);
							pw.write("|^");
							pw.write(HOLE_CNT);
							pw.write("|^");
							pw.write(SIDO);
							pw.write("|^");
							pw.write(REG_YEAR);
							pw.write("|^");
							pw.write(GOLF_TYPE_NAME);
							pw.write("|^");
							pw.write(PES_GROUND);
							pw.write("|^");
							pw.write(TOTAL_GROUND);
							pw.write("|^");
							pw.write(NONE_GROUND);
							pw.write("|^");
							pw.write(SPRAY_GROUND);
							pw.write("|^");
							pw.write(REAL_SUPPLY);
							pw.write("|^");
							pw.write(HA_REAL);
							pw.write("|^");
							pw.write(HA_SPRAY);
							pw.write("|^");
							pw.write(THA_REAL);
							pw.write("|^");
							pw.write(THA_SPRAY);
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
					
					System.out.println("SGS_04 SELECT 파일 생성 프로세스 종료.");
					
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
