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

public class Get_V_All_E06_01 {

	//토양지하수 정보시스템 - 먹는물 공동시설
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {
			
			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			//sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";
			
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
				
				if (args[0].equals("_tset")) {
					
					while (rs.next()) {
						
						String CLASS_ID = " ";
						String SPOT_NM = " ";
						
						CLASS_ID = rs.getString(1);
						SPOT_NM = rs.getString(2);
						
						System.out.println("CLASS_ID::" + CLASS_ID + "::SPOT_NM::" + SPOT_NM);
						
						
					}
					
					System.out.println("SGS_01 SELECT 프로세스 종료.");	
					
				} else {
					
					File file = null;

					file = new File(args[0]);
					
					while (rs.next()) {
						
						String CLASS_ID = " "; // 지점번호
						String SPOT_NM = " "; // 지점명
						String SPOT_STD_CODE = " "; // 지점표준코드
						String INFO_CREAT_INSTT_NM = " "; // 정보생성기관명칭
						String CL_LARGE = " "; // 특성분류_대분류
						String CL_MIDDLE = " "; // 특성분류_중분류
						String CL_SMALL = " "; // 특성분류_소분류
						String CL_LARGE_NM = " "; // 특성분류_대분류명
						String CL_MIDDLE_NM = " "; // 특성분류_중분류명
						String CL_SMALL_NM = " "; // 특성분류_소분류명
						String SIDO = " "; // 시도
						String SIGUNGU = " "; // 시군구
						String ADRES = " "; // 주소
						String ADMCODE = " "; // 주소코드
						String CRDNT_X = " "; // 좌표_X
						String CRDNT_Y = " "; // 좌표_Y
						String ABL_AT = " "; // 폐지여부
						String ABL_DE = " "; // 폐지일자
						String DAY_AVG = " "; // 1일평균이용자수
						String CHARGE = " "; // 담당자
						String INS_DATE = " "; // 설치일자
						String DEL_YN = " "; // 삭제여부(추정)
						String OFFICE = " "; // 담당자명
						String OFFICE_TEL = " "; // 담당자연락처
						String BUILDING_NO = " "; // 건물번호
						String LOC_JIBUN = " "; // 소재지_지번
						String COMMT = " "; // 비고
						String YYYY = " "; // 년
						String PERIOD = " "; // 분기
						String INSP_CHECK = " "; // 검사여부
						String UN_INSP_DESC = " "; // 미검사사유
						String ACCEPT_YN = " "; // 적합여부
						String SUIT = " "; // 적합
						String UNSUIT = " "; // 부적합
						String ITEM_GENBACLOW = " "; // 일반세균저온
						String ITEM_GENBACMID = " "; // 일반세균중온
						String ITEM_TOTBAC = " "; // 총대장균군
						String ITEM_BAC = " "; // 대장균
						String ITEM_FESTR = " "; // 분원성대장균군
						String ITEM_BRANFUNGUS = " "; //분원성연쇄상구균
						String ITEM_GRGUNGUS = " "; // 녹농균
						String ITEM_SALMOL = " "; // 살모넬라
						String ITEM_SEGEL = " "; // 쉬겔라
						String ITEM_SULFUNGUS = " "; // 아황산환원혐기성포자형성균
						String ITEM_YERSINA = " "; // 여시니아균
						String ITEM_PB = " "; // 납 
						String ITEM_F = " "; // 불소
						String ITEM_GAS = " "; // 비소
						String ITEM_SE = " "; // 셀레늄
						String ITEM_HG = " "; // 수은
						String ITEM_CN = " "; // 시안
						String ITEM_CR6 = " "; // 크롬
						String ITEM_NO3AM = " "; // 암모니아성질소
						String ITEM_NO3N = " "; // 질산성질소
						String ITEM_CD = " "; // 카드뮴
						String ITEM_BORON = " "; // 보론
						String ITEM_BRO3 = " "; // 브론산염
						String ITEM_PHENOL = " "; // 페놀
						String ITEM_DIAZN = " "; // 다이아지논
						String ITEM_PARAT = " "; // 파라티온
						String ITEM_PENITRO = " "; // 페니트로티온
						String ITEM_CARBARYL = " "; // 카바릴
						String ITEM_TCET = " "; // 1.1.1-트리클로로에탄
						String ITEM_TECE = " "; // 테트라클로로에틸렌
						String ITEM_TCE = " "; // 트리클로로에틸렌
						String ITEM_DCM = " "; // 디클로로메탄
						String ITEM_BENZENE = " "; // 벤젠
						String ITEM_TOLUENE = " "; // 톨루엔
						String ITEM_ETILBEN = " "; // 에틸벤젠
						String ITEM_XYLENE = " "; // 크실렌
						String ITEM_DCE = " "; // 1.1디클로로에틸렌
						String ITEM_CCL4 = " "; // 사염화탄소
						String ITEM_DBCP = " "; // 1,2-디브로모-3클로로프로판
						String ITEM_C4H8O2 = " "; // 1,4-다이옥산
						String ITEM_GRADIENT = " "; // 경도
						String ITEM_KMN = " "; // 과망간산칼륨소비량
						String ITEM_SMELL = " "; // 냄새(소독외의냄새)
						String ITEM_COLOR = " "; // 동
						String ITEM_CU = " "; // 색도
						String ITEM_ABS = " "; // 세제(음이온계면활성제)
						String ITEM_PH = " "; // 수소이온농도
						String ITEM_ZN = " "; // 아연
						String ITEM_CL = " "; // 염소이온
						String ITEM_FE = " "; // 철
						String ITEM_MN = " "; // 망간
						String ITEM_MUDDY = " "; // 탁도
						String ITEM_SO42 = " "; // 황산이온
						String ITEM_AL = " "; // 알루미늄
						
						CLASS_ID = JsonParser.colWrite_String_eic(rs.getString(1));
						SPOT_NM = JsonParser.colWrite_String_eic(rs.getString(2));
						SPOT_STD_CODE = JsonParser.colWrite_String_eic(rs.getString(3));
						INFO_CREAT_INSTT_NM = JsonParser.colWrite_String_eic(rs.getString(4));
						CL_LARGE = JsonParser.colWrite_String_eic(rs.getString(5));
						CL_MIDDLE = JsonParser.colWrite_String_eic(rs.getString(6));
						CL_SMALL = JsonParser.colWrite_String_eic(rs.getString(7));
						CL_LARGE_NM = JsonParser.colWrite_String_eic(rs.getString(8));
						CL_MIDDLE_NM = JsonParser.colWrite_String_eic(rs.getString(9));
						CL_SMALL_NM = JsonParser.colWrite_String_eic(rs.getString(10));
						SIDO = JsonParser.colWrite_String_eic(rs.getString(11));
						SIGUNGU = JsonParser.colWrite_String_eic(rs.getString(12));
						ADRES = JsonParser.colWrite_String_eic(rs.getString(13));
						ADMCODE = JsonParser.colWrite_String_eic(rs.getString(14));
						CRDNT_X = JsonParser.colWrite_String_eic(rs.getString(15));
						CRDNT_Y = JsonParser.colWrite_String_eic(rs.getString(16));
						ABL_AT = JsonParser.colWrite_String_eic(rs.getString(17));
						ABL_DE = JsonParser.colWrite_String_eic(rs.getString(18));
						DAY_AVG = JsonParser.colWrite_String_eic(rs.getString(19));
						CHARGE = JsonParser.colWrite_String_eic(rs.getString(20));
						INS_DATE = JsonParser.colWrite_String_eic(rs.getString(21));
						DEL_YN = JsonParser.colWrite_String_eic(rs.getString(22));
						OFFICE = JsonParser.colWrite_String_eic(rs.getString(23));
						OFFICE_TEL = JsonParser.colWrite_String_eic(rs.getString(24));
						BUILDING_NO = JsonParser.colWrite_String_eic(rs.getString(25));
						LOC_JIBUN = JsonParser.colWrite_String_eic(rs.getString(26));
						COMMT = JsonParser.colWrite_String_eic(rs.getString(27));
						YYYY = JsonParser.colWrite_String_eic(rs.getString(28));
						PERIOD = JsonParser.colWrite_String_eic(rs.getString(29));
						INSP_CHECK = JsonParser.colWrite_String_eic(rs.getString(30));
						UN_INSP_DESC = JsonParser.colWrite_String_eic(rs.getString(31));
						ACCEPT_YN = JsonParser.colWrite_String_eic(rs.getString(32));
						SUIT = JsonParser.colWrite_String_eic(rs.getString(33));
						UNSUIT = JsonParser.colWrite_String_eic(rs.getString(34));
						ITEM_GENBACLOW = JsonParser.colWrite_String_eic(rs.getString(35));
						ITEM_GENBACMID = JsonParser.colWrite_String_eic(rs.getString(36));
						ITEM_TOTBAC = JsonParser.colWrite_String_eic(rs.getString(37));
						ITEM_BAC = JsonParser.colWrite_String_eic(rs.getString(38));
						ITEM_FESTR = JsonParser.colWrite_String_eic(rs.getString(39));
						ITEM_BRANFUNGUS = JsonParser.colWrite_String_eic(rs.getString(40));
						ITEM_GRGUNGUS = JsonParser.colWrite_String_eic(rs.getString(41));
						ITEM_SALMOL = JsonParser.colWrite_String_eic(rs.getString(42));
						ITEM_SEGEL = JsonParser.colWrite_String_eic(rs.getString(43));
						ITEM_SULFUNGUS = JsonParser.colWrite_String_eic(rs.getString(44));
						ITEM_YERSINA = JsonParser.colWrite_String_eic(rs.getString(45));
						ITEM_PB = JsonParser.colWrite_String_eic(rs.getString(46));
						ITEM_F = JsonParser.colWrite_String_eic(rs.getString(47));
						ITEM_GAS = JsonParser.colWrite_String_eic(rs.getString(48));
						ITEM_SE = JsonParser.colWrite_String_eic(rs.getString(49));
						ITEM_HG = JsonParser.colWrite_String_eic(rs.getString(50));
						ITEM_CN = JsonParser.colWrite_String_eic(rs.getString(51));
						ITEM_CR6 = JsonParser.colWrite_String_eic(rs.getString(52));
						ITEM_NO3AM = JsonParser.colWrite_String_eic(rs.getString(53));
						ITEM_NO3N = JsonParser.colWrite_String_eic(rs.getString(54));
						ITEM_CD = JsonParser.colWrite_String_eic(rs.getString(55));
						ITEM_BORON = JsonParser.colWrite_String_eic(rs.getString(56));
						ITEM_BRO3 = JsonParser.colWrite_String_eic(rs.getString(57));
						ITEM_PHENOL = JsonParser.colWrite_String_eic(rs.getString(58));
						ITEM_DIAZN = JsonParser.colWrite_String_eic(rs.getString(59));
						ITEM_PARAT = JsonParser.colWrite_String_eic(rs.getString(60));
						ITEM_PENITRO = JsonParser.colWrite_String_eic(rs.getString(61));
						ITEM_CARBARYL = JsonParser.colWrite_String_eic(rs.getString(62));
						ITEM_TCET = JsonParser.colWrite_String_eic(rs.getString(63));
						ITEM_TECE = JsonParser.colWrite_String_eic(rs.getString(64));
						ITEM_TCE = JsonParser.colWrite_String_eic(rs.getString(65));
						ITEM_DCM = JsonParser.colWrite_String_eic(rs.getString(66));
						ITEM_BENZENE = JsonParser.colWrite_String_eic(rs.getString(67));
						ITEM_TOLUENE = JsonParser.colWrite_String_eic(rs.getString(68));
						ITEM_ETILBEN = JsonParser.colWrite_String_eic(rs.getString(69));
						ITEM_XYLENE = JsonParser.colWrite_String_eic(rs.getString(70));
						ITEM_DCE = JsonParser.colWrite_String_eic(rs.getString(71));
						ITEM_CCL4 = JsonParser.colWrite_String_eic(rs.getString(72));
						ITEM_DBCP = JsonParser.colWrite_String_eic(rs.getString(73));
						ITEM_C4H8O2 = JsonParser.colWrite_String_eic(rs.getString(74));
						ITEM_GRADIENT = JsonParser.colWrite_String_eic(rs.getString(75));
						ITEM_KMN = JsonParser.colWrite_String_eic(rs.getString(76));
						ITEM_SMELL = JsonParser.colWrite_String_eic(rs.getString(77));
						ITEM_COLOR = JsonParser.colWrite_String_eic(rs.getString(78));
						ITEM_CU = JsonParser.colWrite_String_eic(rs.getString(79));
						ITEM_ABS = JsonParser.colWrite_String_eic(rs.getString(80));
						ITEM_PH = JsonParser.colWrite_String_eic(rs.getString(81));
						ITEM_ZN = JsonParser.colWrite_String_eic(rs.getString(82));
						ITEM_CL = JsonParser.colWrite_String_eic(rs.getString(83));
						ITEM_FE = JsonParser.colWrite_String_eic(rs.getString(84));
						ITEM_MN = JsonParser.colWrite_String_eic(rs.getString(85));
						ITEM_MUDDY = JsonParser.colWrite_String_eic(rs.getString(86));
						ITEM_SO42 = JsonParser.colWrite_String_eic(rs.getString(87));
						ITEM_AL = JsonParser.colWrite_String_eic(rs.getString(88));
						
						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(CLASS_ID);
							pw.write("|^");
							pw.write(SPOT_NM);
							pw.write("|^");
							pw.write(SPOT_STD_CODE);
							pw.write("|^");
							pw.write(INFO_CREAT_INSTT_NM);
							pw.write("|^");
							pw.write(CL_LARGE);
							pw.write("|^");
							pw.write(CL_MIDDLE);
							pw.write("|^");
							pw.write(CL_SMALL);
							pw.write("|^");
							pw.write(CL_LARGE_NM);
							pw.write("|^");
							pw.write(CL_MIDDLE_NM);
							pw.write("|^");
							pw.write(CL_SMALL_NM);
							pw.write("|^");
							pw.write(SIDO);
							pw.write("|^");
							pw.write(SIGUNGU);
							pw.write("|^");
							pw.write(ADRES);
							pw.write("|^");
							pw.write(ADMCODE);
							pw.write("|^");
							pw.write(CRDNT_X);
							pw.write("|^");
							pw.write(CRDNT_Y);
							pw.write("|^");
							pw.write(ABL_AT);
							pw.write("|^");
							pw.write(ABL_DE);
							pw.write("|^");
							pw.write(DAY_AVG);
							pw.write("|^");
							pw.write(CHARGE );
							pw.write("|^");
							pw.write(INS_DATE);
							pw.write("|^");
							pw.write(DEL_YN);
							pw.write("|^");
							pw.write(OFFICE);
							pw.write("|^");
							pw.write(OFFICE_TEL);
							pw.write("|^");
							pw.write(BUILDING_NO);
							pw.write("|^");
							pw.write(LOC_JIBUN);
							pw.write("|^");
							pw.write(COMMT);
							pw.write("|^");
							pw.write(YYYY);
							pw.write("|^");
							pw.write(PERIOD);
							pw.write("|^");
							pw.write(INSP_CHECK);
							pw.write("|^");
							pw.write(UN_INSP_DESC);
							pw.write("|^");
							pw.write(ACCEPT_YN);
							pw.write("|^");
							pw.write(SUIT);
							pw.write("|^");
							pw.write(UNSUIT);
							pw.write("|^");
							pw.write(ITEM_GENBACLOW);
							pw.write("|^");
							pw.write(ITEM_GENBACMID);
							pw.write("|^");
							pw.write(ITEM_TOTBAC);
							pw.write("|^");
							pw.write(ITEM_BAC);
							pw.write("|^");
							pw.write(ITEM_FESTR);
							pw.write("|^");
							pw.write(ITEM_BRANFUNGUS);
							pw.write("|^");
							pw.write(ITEM_GRGUNGUS);
							pw.write("|^");
							pw.write(ITEM_SALMOL);
							pw.write("|^");
							pw.write(ITEM_SEGEL);
							pw.write("|^");
							pw.write(ITEM_SULFUNGUS);
							pw.write("|^");
							pw.write(ITEM_YERSINA);
							pw.write("|^");
							pw.write(ITEM_PB);
							pw.write("|^");
							pw.write(ITEM_F);
							pw.write("|^");
							pw.write(ITEM_GAS);
							pw.write("|^");
							pw.write(ITEM_SE);
							pw.write("|^");
							pw.write(ITEM_HG);
							pw.write("|^");
							pw.write(ITEM_CN);
							pw.write("|^");
							pw.write(ITEM_CR6);
							pw.write("|^");
							pw.write(ITEM_NO3AM);
							pw.write("|^");
							pw.write(ITEM_NO3N);
							pw.write("|^");
							pw.write(ITEM_CD);
							pw.write("|^");
							pw.write(ITEM_BORON);
							pw.write("|^");
							pw.write(ITEM_BRO3);
							pw.write("|^");
							pw.write(ITEM_PHENOL);
							pw.write("|^");
							pw.write(ITEM_DIAZN);
							pw.write("|^");
							pw.write(ITEM_PARAT);
							pw.write("|^");
							pw.write(ITEM_PENITRO);
							pw.write("|^");
							pw.write(ITEM_CARBARYL);
							pw.write("|^");
							pw.write(ITEM_TCET);
							pw.write("|^");
							pw.write(ITEM_TECE);
							pw.write("|^");
							pw.write(ITEM_TCE);
							pw.write("|^");
							pw.write(ITEM_DCM);
							pw.write("|^");
							pw.write(ITEM_BENZENE);
							pw.write("|^");
							pw.write(ITEM_TOLUENE);
							pw.write("|^");
							pw.write(ITEM_ETILBEN);
							pw.write("|^");
							pw.write(ITEM_XYLENE);
							pw.write("|^");
							pw.write(ITEM_DCE);
							pw.write("|^");
							pw.write(ITEM_CCL4);
							pw.write("|^");
							pw.write(ITEM_DBCP);
							pw.write("|^");
							pw.write(ITEM_C4H8O2);
							pw.write("|^");
							pw.write(ITEM_GRADIENT);
							pw.write("|^");
							pw.write(ITEM_KMN);
							pw.write("|^");
							pw.write(ITEM_SMELL);
							pw.write("|^");
							pw.write(ITEM_COLOR);
							pw.write("|^");
							pw.write(ITEM_CU);
							pw.write("|^");
							pw.write(ITEM_ABS);
							pw.write("|^");
							pw.write(ITEM_PH);
							pw.write("|^");
							pw.write(ITEM_ZN);
							pw.write("|^");
							pw.write(ITEM_CL);
							pw.write("|^");
							pw.write(ITEM_FE);
							pw.write("|^");
							pw.write(ITEM_MN);
							pw.write("|^");
							pw.write(ITEM_MUDDY);
							pw.write("|^");
							pw.write(ITEM_SO42);
							pw.write("|^");
							pw.write(ITEM_AL);
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
					
					System.out.println("SGS_01 SELECT 파일 생성 프로세스 종료.");
					
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
			
			
			
		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
