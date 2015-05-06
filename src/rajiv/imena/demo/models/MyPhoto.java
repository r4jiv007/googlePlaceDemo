package rajiv.imena.demo.models;

import java.io.Serializable;

public class MyPhoto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mHeight;
	private int mWidth;
	private String mPhotoReference;
	private String mThumbNailUrl;
	
	public MyPhoto(int mHeight, int mWidth, String mPhotoReference,String url) {
		super();
		this.mHeight = mHeight;
		this.mWidth = mWidth;
		this.mPhotoReference = mPhotoReference;
		this.mThumbNailUrl=url;
	}
	
	public String getmThumbNailUrl() {
		return mThumbNailUrl;
	}

	public void setmThumbNailUrl(String mThumbNailUrl) {
		this.mThumbNailUrl = mThumbNailUrl;
	}

	public int getmHeight() {
		return mHeight;
	}
	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}
	public int getmWidth() {
		return mWidth;
	}
	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}
	public String getmPhotoReference() {
		return mPhotoReference;
	}
	public void setmPhotoReference(String mPhotoReference) {
		this.mPhotoReference = mPhotoReference;
	}
	
	
}
