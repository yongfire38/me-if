package common;

import java.io.File;
import java.io.FileInputStream;



import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TransSftp {
	
	
	
	//대상서버에 sftp로 보냄 
	public static void transSftp(String filePath, String sysName) {
		
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null; 
		//보낼 대상 파일 경로
		File f = new File(filePath);
		FileInputStream in = null;

		System.out.println("preparing the host information for sftp.");

		try {

			JSch jsch = new JSch();
			session = jsch.getSession("agntuser", "172.29.129.11", 28);
			session.setPassword("Dpdlwjsxm1@");

			// host 연결
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			// sftp 채널 연결
			channel = session.openChannel("sftp");
			channel.connect();

			// 파일 업로드 처리
			channelSftp = (ChannelSftp) channel;

			System.out.println("=> Connected to host");
			in = new FileInputStream(f);

			// channelSftp.cd("/data1/if_data/WEI"); //as-is, 연계서버에 떨어지는 위치
			channelSftp.cd("/data1/if_data/" + sysName); // test, sysName은 각 시스템별 폴더명

			String fileName = f.getName();
			channelSftp.put(in, fileName);

			System.out.println("=> Uploaded : " + f.getPath());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				in.close();

				// sftp 채널을 닫음
				channelSftp.exit();

				// 채널 연결 해제
				channel.disconnect();

				// 호스트 세션 종료
				session.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("sftp transfer complete!");

	}
	

}
