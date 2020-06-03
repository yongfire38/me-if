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

				// 전체 레코드 개수만큼의 배열
				String[] registNo = new String[rowCount]; // 등록번호
				String[] insttNm = new String[rowCount]; // 관할청명
				String[] spcsSeNm = new String[rowCount]; // 종구분명
				String[] entrpsNm = new String[rowCount]; // 업체명
				String[] rprntNm = new String[rowCount]; // 대표자명
				String[] heofZip = new String[rowCount]; // 주사무소우편번호
				String[] heofAdres1 = new String[rowCount]; // 주사무소주소
				String[] heofDetilAdres2 = new String[rowCount]; // 주사무소상세주소
				String[] heofPreTelno1 = new String[rowCount]; // 주사무소앞전화번호
				String[] heofMidTelno2 = new String[rowCount]; // 주사무소중간전화번호
				String[] heofRrTelno3 = new String[rowCount]; // 주사무소뒤전화번호
				String[] lapZip = new String[rowCount]; // 실험실우편번호
				String[] lapAdres1 = new String[rowCount]; // 실험실주소
				String[] lapDetilAdres2 = new String[rowCount]; // 실험실상세주소
				String[] lapPreTelno1 = new String[rowCount]; // 실험실앞전화번호
				String[] lapMidTelno2 = new String[rowCount]; // 실험실중간전화번호
				String[] lapRrTelno3 = new String[rowCount]; // 실험실뒤전화번호
				String[] evlChrgDeptZip = new String[rowCount]; // 평가담당부서우편번호
				String[] evlChrgDeptAdres1 = new String[rowCount]; // 평가담당부서주소
				String[] evlChrgDeptDetilAdres2 = new String[rowCount]; // 평가담당부서상세주소
				String[] evlChrgDeptPreTelno1 = new String[rowCount]; // 평가담당부서앞전화번호
				String[] evlChrgDeptMidTelno2 = new String[rowCount]; // 평가담당부서중간전화번호
				String[] evlChrgDeptRrTelno3 = new String[rowCount]; // 평가담당부서뒤전화번호
				String[] evlBsnsRegistDt = new String[rowCount]; // 평가사업등록일자
				String[] mpwHoldCnt = new String[rowCount]; // 보유기술자수
				String[] spcssSeNm = new String[rowCount]; // 휴_폐업구분명

				// 다시 처음부터 조회해야 하므로 커서는 초기화
				rs.beforeFirst();

				int i = 0;

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						registNo[i] = rs.getString(1);
						insttNm[i] = rs.getString(2);
						spcsSeNm[i] = rs.getString(3);
						entrpsNm[i] = rs.getString(4);
						rprntNm[i] = rs.getString(5);
						heofZip[i] = rs.getString(6);
						heofAdres1[i] = rs.getString(7);
						heofDetilAdres2[i] = rs.getString(8);
						heofPreTelno1[i] = rs.getString(9);
						heofMidTelno2[i] = rs.getString(10);
						heofRrTelno3[i] = rs.getString(11);
						lapZip[i] = rs.getString(12);
						lapAdres1[i] = rs.getString(13);
						lapDetilAdres2[i] = rs.getString(14);
						lapPreTelno1[i] = rs.getString(15);
						lapMidTelno2[i] = rs.getString(16);
						lapRrTelno3[i] = rs.getString(17);
						evlChrgDeptZip[i] = rs.getString(18);
						evlChrgDeptAdres1[i] = rs.getString(19);
						evlChrgDeptDetilAdres2[i] = rs.getString(20);
						evlChrgDeptPreTelno1[i] = rs.getString(21);
						evlChrgDeptMidTelno2[i] = rs.getString(22);
						evlChrgDeptRrTelno3[i] = rs.getString(23);
						evlBsnsRegistDt[i] = rs.getString(24);
						mpwHoldCnt[i] = rs.getString(25);
						spcssSeNm[i] = rs.getString(26);

						System.out.println("registNo::" + registNo[i] + "::insttNm::" + insttNm[i] + "::spcsSeNm::"
								+ spcsSeNm[i] + "::entrpsNm::" + entrpsNm[i] + "::rprntNm::" + rprntNm[i]
								+ "::heofZip::" + heofZip[i] + "::heofAdres1::" + heofAdres1[i] + "::heofDetilAdres2::"
								+ heofDetilAdres2[i] + "::heofPreTelno1::" + heofPreTelno1[i] + "::heofMidTelno2::"
								+ heofMidTelno2[i] + "::heofRrTelno3::" + heofRrTelno3[i] + "::lapZip::" + lapZip[i]
								+ "::lapAdres1::" + lapAdres1[i] + "::lapDetilAdres2::" + lapDetilAdres2[i]
								+ "::lapPreTelno1::" + lapPreTelno1[i] + "::lapMidTelno2::" + lapMidTelno2[i]
								+ "::lapRrTelno3::" + lapRrTelno3[i] + "::evlChrgDeptZip::" + evlChrgDeptZip[i]
								+ "::evlChrgDeptAdres1::" + evlChrgDeptAdres1[i] + "::evlChrgDeptDetilAdres2::"
								+ evlChrgDeptDetilAdres2[i] + "::evlChrgDeptPreTelno1::" + evlChrgDeptPreTelno1[i]
								+ "::evlChrgDeptMidTelno2::" + evlChrgDeptMidTelno2[i] + "::evlChrgDeptRrTelno3::"
								+ evlChrgDeptRrTelno3[i] + "::evlBsnsRegistDt::" + evlBsnsRegistDt[i] + "::mpwHoldCnt::"
								+ mpwHoldCnt[i] + "::spcssSeNm::" + spcssSeNm[i]);

						i++;

					}
					
					System.out.println("EIC SELECT 프로세스 종료.");	

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						registNo[i] =  JsonParser.colWrite_String_eic(rs.getString(1));
						insttNm[i] = JsonParser.colWrite_String_eic(rs.getString(2));
						spcsSeNm[i] = JsonParser.colWrite_String_eic(rs.getString(3));
						entrpsNm[i] = JsonParser.colWrite_String_eic(rs.getString(4));
						rprntNm[i] = JsonParser.colWrite_String_eic(rs.getString(5));
						heofZip[i] = JsonParser.colWrite_String_eic(rs.getString(6));
						heofAdres1[i] = JsonParser.colWrite_String_eic(rs.getString(7));
						heofDetilAdres2[i] = JsonParser.colWrite_String_eic(rs.getString(8));
						heofPreTelno1[i] = JsonParser.colWrite_String_eic(rs.getString(9));
						heofMidTelno2[i] = JsonParser.colWrite_String_eic(rs.getString(10));
						heofRrTelno3[i] = JsonParser.colWrite_String_eic(rs.getString(11));
						lapZip[i] = JsonParser.colWrite_String_eic(rs.getString(12));
						lapAdres1[i] = JsonParser.colWrite_String_eic(rs.getString(13));
						lapDetilAdres2[i] = JsonParser.colWrite_String_eic(rs.getString(14));
						lapPreTelno1[i] = JsonParser.colWrite_String_eic(rs.getString(15));
						lapMidTelno2[i] = JsonParser.colWrite_String_eic(rs.getString(16));
						lapRrTelno3[i] = JsonParser.colWrite_String_eic(rs.getString(17));
						evlChrgDeptZip[i] = JsonParser.colWrite_String_eic(rs.getString(18));
						evlChrgDeptAdres1[i] = JsonParser.colWrite_String_eic(rs.getString(19));
						evlChrgDeptDetilAdres2[i] = JsonParser.colWrite_String_eic(rs.getString(20));
						evlChrgDeptPreTelno1[i] = JsonParser.colWrite_String_eic(rs.getString(21));
						evlChrgDeptMidTelno2[i] = JsonParser.colWrite_String_eic(rs.getString(22));
						evlChrgDeptRrTelno3[i] = JsonParser.colWrite_String_eic(rs.getString(23));
						evlBsnsRegistDt[i] = JsonParser.colWrite_String_eic(rs.getString(24));
						mpwHoldCnt[i] = JsonParser.colWrite_String_eic(rs.getString(25));
						spcssSeNm[i] = JsonParser.colWrite_String_eic(rs.getString(26));

						/*System.out.println("registNo::" + registNo[i] + "::insttNm::" + insttNm[i] + "::spcsSeNm::"
								+ spcsSeNm[i] + "::entrpsNm::" + entrpsNm[i] + "::rprntNm::" + rprntNm[i]);*/

						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(registNo[i]);
							pw.write("|^");
							pw.write(insttNm[i]);
							pw.write("|^");
							pw.write(spcsSeNm[i]);
							pw.write("|^");
							pw.write(entrpsNm[i]);
							pw.write("|^");
							pw.write(rprntNm[i]);
							pw.write("|^");
							pw.write(heofZip[i]);
							pw.write("|^");
							pw.write(heofAdres1[i]);
							pw.write("|^");
							pw.write(heofDetilAdres2[i]);
							pw.write("|^");
							pw.write(heofPreTelno1[i]);
							pw.write("|^");
							pw.write(heofMidTelno2[i]);
							pw.write("|^");
							pw.write(heofRrTelno3[i]);
							pw.write("|^");
							pw.write(lapZip[i]);
							pw.write("|^");
							pw.write(lapAdres1[i]);
							pw.write("|^");
							pw.write(lapDetilAdres2[i]);
							pw.write("|^");
							pw.write(lapPreTelno1[i]);
							pw.write("|^");
							pw.write(lapMidTelno2[i]);
							pw.write("|^");
							pw.write(lapRrTelno3[i]);
							pw.write("|^");
							pw.write(evlChrgDeptZip[i]);
							pw.write("|^");
							pw.write(evlChrgDeptAdres1[i]);
							pw.write("|^");
							pw.write(evlChrgDeptDetilAdres2[i]);
							pw.write("|^");
							pw.write(evlChrgDeptPreTelno1[i]);
							pw.write("|^");
							pw.write(evlChrgDeptMidTelno2[i]);
							pw.write("|^");
							pw.write(evlChrgDeptRrTelno3[i]);
							pw.write("|^");
							pw.write(evlBsnsRegistDt[i]);
							pw.write("|^");
							pw.write(mpwHoldCnt[i]);
							pw.write("|^");
							pw.write(spcssSeNm[i]);
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
