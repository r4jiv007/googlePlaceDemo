package rajiv.imena.demo.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MyPlace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyLocation mLocation;
	private String mIcon;
	private String mId;
	private String mName;
	private String mPlaceid;
	private ArrayList<MyPhoto> mPhotos = new ArrayList<MyPhoto>();
	private String mVicinity;
	private ArrayList<String> mAttributes = new ArrayList<String>();

	private String mAddress;
	
	
	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public ArrayList<MyPhoto> getPhotos() {
		return mPhotos;
	}

	public void addPhoto(MyPhoto photo) {
		mPhotos.add(photo);
	}

	public MyLocation getmLocation() {
		return mLocation;
	}

	public void setmLocation(float lat, float longt) {
		this.mLocation = new MyLocation(lat, longt);
	}
	
	public void setmLocation(MyLocation location) {
		this.mLocation = location;
	}

	public String getmIcon() {
		return mIcon;
	}

	public void setmIcon(String mIcon) {
		this.mIcon = mIcon;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmPlaceid() {
		return mPlaceid;
	}

	public void setmPlaceid(String mPlaceid) {
		this.mPlaceid = mPlaceid;
	}


	public String getmVicinity() {
		return mVicinity;
	}

	public void setmVicinity(String mVicinity) {
		this.mVicinity = mVicinity;
	}

}
