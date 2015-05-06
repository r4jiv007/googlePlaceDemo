package rajiv.imena.demo.models;

import java.io.Serializable;

public class MyLocation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double lat,longt;
	
	public MyLocation (double lat,double logt){
		this.lat=lat;
		this.longt=logt;
	}
	public double getLat() {
		return lat;
	}

	public double getLongt() {
		return longt;
	}
	
}