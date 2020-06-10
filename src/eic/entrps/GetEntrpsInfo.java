package eic.entrps;

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

public class GetEntrpsInfo {

	//환경영향평가 경력 관리 시스템 - 환경영향평가업체정보
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체

			try {

				// SQL 문장을 만들고 만약 문장이 질의어(SELECT문)라면
				// 그 결과를 담을 ResulSet 객체를 준비한 후 실행시킨다.
				// String quary = "SELECT * FROM CMS.V_ENTRPS_INFO WHERE REGIST_NO IS NOT NULL";
				String query = DBConnection.getProperty("eic_oracle_table_query");
				System.out.println("query :::" + query);

				conn = DBConnection.getOraConnection("eic");
				pstm = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = pstm.executeQuery();

				// 전체 레코드 수를 구하기 위해 커서를 마지막으로 이동
				rs.last();

				int rowCount = rs.getRow();
				
				System.out.println("전체 건 수 :::" + Integer.toString(rowCount)+" 건");

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				if (args[0].equals("_tset")) {

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String registNo = " "; // 등록번호
						String insttNm = " "; // 관할청명
						String spcsSeNm = " "; // 종구분명
						String entrpsNm = " "; // 업체명
						String rprntNm = " "; // 대표자명
						String heofZip = " "; // 주사무소우편번호
						String heofAdres1 = " "; // 주사무소주소
						String heofDetilAdres2 = " "; // 주사무소상세주소
						String heofPreTelno1 = " "; // 주사무소앞전화번호
						String heofMidTelno2 = " "; // 주사무소중간전화번호
						String heofRrTelno3 = " "; // 주사무소뒤전화번호
						String lapZip = " "; // 실험실우편번호
						String lapAdres1 = " "; // 실험실주소
						String lapDetilAdres2 = " "; // 실험실상세주소
						String lapPreTelno1 = " "; // 실험실앞전화번호
						String lapMidTelno2 = " "; // 실험실중간전화번호
						String lapRrTelno3 = " "; // 실험실뒤전화번호
						String evlChrgDeptZip = " "; // 평가담당부서우편번호
						String evlChrgDeptAdres1 = " "; // 평가담당부서주소
						String evlChrgDeptDetilAdres2 = " "; // 평가담당부서상세주소
						String evlChrgDeptPreTelno1 = " "; // 평가담당부서앞전화번호
						String evlChrgDeptMidTelno2 = " "; // 평가담당부서중간전화번호
						String evlChrgDeptRrTelno3 = " "; // 평가담당부서뒤전화번호
						String evlBsnsRegistDt = " "; // 평가사업등록일자
						String mpwHoldCnt = " "; // 보유기술자수
						String spcssSeNm = " "; // 휴_폐업구분명

						registNo = rs.getString(1);
						insttNm = rs.getString(2);
						spcsSeNm = rs.getString(3);
						entrpsNm = rs.getString(4);
						rprntNm = rs.getString(5);
						heofZip = rs.getString(6);
						heofAdres1 = rs.getString(7);
						heofDetilAdres2 = rs.getString(8);
						heofPreTelno1 = rs.getString(9);
						heofMidTelno2 = rs.getString(10);
						heofRrTelno3 = rs.getString(11);
						lapZip = rs.getString(12);
						lapAdres1 = rs.getString(13);
						lapDetilAdres2 = rs.getString(14);
						lapPreTelno1 = rs.getString(15);
						lapMidTelno2 = rs.getString(16);
						lapRrTelno3 = rs.getString(17);
						evlChrgDeptZip = rs.getString(18);
						evlChrgDeptAdres1 = rs.getString(19);
						evlChrgDeptDetilAdres2 = rs.getString(20);
						evlChrgDeptPreTelno1 = rs.getString(21);
						evlChrgDeptMidTelno2 = rs.getString(22);
						evlChrgDeptRrTelno3 = rs.getString(23);
						evlBsnsRegistDt = rs.getString(24);
						mpwHoldCnt = rs.getString(25);
						spcssSeNm = rs.getString(26);

						System.out.println("registNo::" + registNo + "::insttNm::" + insttNm + "::spcsSeNm::"
								+ spcsSeNm + "::entrpsNm::" + entrpsNm + "::rprntNm::" + rprntNm
								+ "::heofZip::" + heofZip + "::heofAdres1::" + heofAdres1 + "::heofDetilAdres2::"
								+ heofDetilAdres2 + "::heofPreTelno1::" + heofPreTelno1 + "::heofMidTelno2::"
								+ heofMidTelno2 + "::heofRrTelno3::" + heofRrTelno3 + "::lapZip::" + lapZip
								+ "::lapAdres1::" + lapAdres1 + "::lapDetilAdres2::" + lapDetilAdres2
								+ "::lapPreTelno1::" + lapPreTelno1 + "::lapMidTelno2::" + lapMidTelno2
								+ "::lapRrTelno3::" + lapRrTelno3 + "::evlChrgDeptZip::" + evlChrgDeptZip
								+ "::evlChrgDeptAdres1::" + evlChrgDeptAdres1 + "::evlChrgDeptDetilAdres2::"
								+ evlChrgDeptDetilAdres2 + "::evlChrgDeptPreTelno1::" + evlChrgDeptPreTelno1
								+ "::evlChrgDeptMidTelno2::" + evlChrgDeptMidTelno2 + "::evlChrgDeptRrTelno3::"
								+ evlChrgDeptRrTelno3 + "::evlBsnsRegistDt::" + evlBsnsRegistDt + "::mpwHoldCnt::"
								+ mpwHoldCnt + "::spcssSeNm::" + spcssSeNm);

					}
					
					System.out.println("EIC SELECT 프로세스 종료.");	

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {
						
						// 전체 레코드 개수만큼
						String registNo = " "; // 등록번호
						String insttNm = " "; // 관할청명
						String spcsSeNm = " "; // 종구분명
						String entrpsNm = " "; // 업체명
						String rprntNm = " "; // 대표자명
						String heofZip = " "; // 주사무소우편번호
						String heofAdres1 = " "; // 주사무소주소
						String heofDetilAdres2 = " "; // 주사무소상세주소
						String heofPreTelno1 = " "; // 주사무소앞전화번호
						String heofMidTelno2 = " "; // 주사무소중간전화번호
						String heofRrTelno3 = " "; // 주사무소뒤전화번호
						String lapZip = " "; // 실험실우편번호
						String lapAdres1 = " "; // 실험실주소
						String lapDetilAdres2 = " "; // 실험실상세주소
						String lapPreTelno1 = " "; // 실험실앞전화번호
						String lapMidTelno2 = " "; // 실험실중간전화번호
						String lapRrTelno3 = " "; // 실험실뒤전화번호
						String evlChrgDeptZip = " "; // 평가담당부서우편번호
						String evlChrgDeptAdres1 = " "; // 평가담당부서주소
						String evlChrgDeptDetilAdres2 = " "; // 평가담당부서상세주소
						String evlChrgDeptPreTelno1 = " "; // 평가담당부서앞전화번호
						String evlChrgDeptMidTelno2 = " "; // 평가담당부서중간전화번호
						String evlChrgDeptRrTelno3 = " "; // 평가담당부서뒤전화번호
						String evlBsnsRegistDt = " "; // 평가사업등록일자
						String mpwHoldCnt = " "; // 보유기술자수
						String spcssSeNm = " "; // 휴_폐업구분명

						registNo =  JsonParser.colWrite_String_eic(rs.getString(1));
						insttNm = JsonParser.colWrite_String_eic(rs.getString(2));
						spcsSeNm = JsonParser.colWrite_String_eic(rs.getString(3));
						entrpsNm = JsonParser.colWrite_String_eic(rs.getString(4));
						rprntNm = JsonParser.colWrite_String_eic(rs.getString(5));
						heofZip = JsonParser.colWrite_String_eic(rs.getString(6));
						heofAdres1 = JsonParser.colWrite_String_eic(rs.getString(7));
						heofDetilAdres2 = JsonParser.colWrite_String_eic(rs.getString(8));
						heofPreTelno1 = JsonParser.colWrite_String_eic(rs.getString(9));
						heofMidTelno2 = JsonParser.colWrite_String_eic(rs.getString(10));
						heofRrTelno3 = JsonParser.colWrite_String_eic(rs.getString(11));
						lapZip = JsonParser.colWrite_String_eic(rs.getString(12));
						lapAdres1 = JsonParser.colWrite_String_eic(rs.getString(13));
						lapDetilAdres2 = JsonParser.colWrite_String_eic(rs.getString(14));
						lapPreTelno1 = JsonParser.colWrite_String_eic(rs.getString(15));
						lapMidTelno2 = JsonParser.colWrite_String_eic(rs.getString(16));
						lapRrTelno3 = JsonParser.colWrite_String_eic(rs.getString(17));
						evlChrgDeptZip = JsonParser.colWrite_String_eic(rs.getString(18));
						evlChrgDeptAdres1 = JsonParser.colWrite_String_eic(rs.getString(19));
						evlChrgDeptDetilAdres2 = JsonParser.colWrite_String_eic(rs.getString(20));
						evlChrgDeptPreTelno1 = JsonParser.colWrite_String_eic(rs.getString(21));
						evlChrgDeptMidTelno2 = JsonParser.colWrite_String_eic(rs.getString(22));
						evlChrgDeptRrTelno3 = JsonParser.colWrite_String_eic(rs.getString(23));
						evlBsnsRegistDt = JsonParser.colWrite_String_eic(rs.getString(24));
						mpwHoldCnt = JsonParser.colWrite_String_eic(rs.getString(25));
						spcssSeNm = JsonParser.colWrite_String_eic(rs.getString(26));

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(registNo);
							pw.write("|^");
							pw.write(insttNm);
							pw.write("|^");
							pw.write(spcsSeNm);
							pw.write("|^");
							pw.write(entrpsNm);
							pw.write("|^");
							pw.write(rprntNm);
							pw.write("|^");
							pw.write(heofZip);
							pw.write("|^");
							pw.write(heofAdres1);
							pw.write("|^");
							pw.write(heofDetilAdres2);
							pw.write("|^");
							pw.write(heofPreTelno1);
							pw.write("|^");
							pw.write(heofMidTelno2);
							pw.write("|^");
							pw.write(heofRrTelno3);
							pw.write("|^");
							pw.write(lapZip);
							pw.write("|^");
							pw.write(lapAdres1);
							pw.write("|^");
							pw.write(lapDetilAdres2);
							pw.write("|^");
							pw.write(lapPreTelno1);
							pw.write("|^");
							pw.write(lapMidTelno2);
							pw.write("|^");
							pw.write(lapRrTelno3);
							pw.write("|^");
							pw.write(evlChrgDeptZip);
							pw.write("|^");
							pw.write(evlChrgDeptAdres1);
							pw.write("|^");
							pw.write(evlChrgDeptDetilAdres2);
							pw.write("|^");
							pw.write(evlChrgDeptPreTelno1);
							pw.write("|^");
							pw.write(evlChrgDeptMidTelno2);
							pw.write("|^");
							pw.write(evlChrgDeptRrTelno3);
							pw.write("|^");
							pw.write(evlBsnsRegistDt);
							pw.write("|^");
							pw.write(mpwHoldCnt);
							pw.write("|^");
							pw.write(spcssSeNm);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}


					}
					
					
					if(file.exists()){
						System.out.println("파일이 생성되었습니다.");
					}else{
						System.out.println("파일이 생성되지 않았습니다.");
					}
					
					System.out.println("EIC SELECT 파일 생성 프로세스 종료.");		
					
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
