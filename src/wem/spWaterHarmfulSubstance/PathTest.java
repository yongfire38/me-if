package wem.spWaterHarmfulSubstance;

import common.DBConnection;

public class PathTest {

	public static void main(String[] args) {

		/*System.out.println("path :::" + System.getenv("USERNAME"));
		
		System.out.println("path2 :::" + System.getenv("APP_ROOT"));*/
		
		String Sciencequery = DBConnection.getScienceProperty("wem_oracle_wem01_query");
		System.out.println("Sciencequery :::" + Sciencequery);
		
		/*String query = DBConnection.getProperty("wem_oracle_wem01_query");
		System.out.println("query :::" + query);*/
		
		
	}

}
