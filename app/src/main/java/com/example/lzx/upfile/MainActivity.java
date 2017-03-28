package com.example.lzx.upfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private ImageView userImage,downloadImg;
	private Button uploadImage,downloadBtn;
	private ProgressDialog progressDialog;
	
	private final String IMAGE_TYPE="image/*";
	private final int IMAGE_CODE=1;
	
	private Bitmap TestBitmap;
	private String Path="";
	private String url="";
	
	private Bitmap mBitmap;
	
	private Handler mhandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				downloadImg.setImageBitmap((Bitmap) msg.obj);
				progressDialog.dismiss();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bmob.initialize(MainActivity.this, "827201b94822832e4be6b9cfb7d5f252");
		initView();
		initListener();
//		initData();
	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				 try {
					 	Bitmap bitmap1=null;
		                URL myUrl;
		                url = "http://file.bmob.cn/" + url;
                		Toast.makeText(MainActivity.this, url+"", Toast.LENGTH_SHORT).show();
                        myUrl=new URL(url);
                        HttpURLConnection conn=(HttpURLConnection)myUrl.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.connect();
                        InputStream is=conn.getInputStream();
                        bitmap1=BitmapFactory.decodeStream(is);
                        //��bitmapת��Բ��
                        BitmapUtil bmuUtil = new BitmapUtil(MainActivity.this);
                        mBitmap=bmuUtil.toRoundBitmap(bitmap1);
                        is.close();
                        Message msg = mhandler.obtainMessage(1, mBitmap);
        				mhandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
				 Looper.loop();
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originUri = data.getData();
				Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originUri);
				TestBitmap = bm;
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = managedQuery(originUri, proj, null, null, null);
				
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				
				String path = cursor.getString(column_index);
				Path = path.substring(20);
				Toast.makeText(MainActivity.this, Path, Toast.LENGTH_SHORT).show();
				BitmapUtil bitmapUtil = new BitmapUtil(MainActivity.this);
				Bitmap myBitmap = bitmapUtil.toRoundBitmap(TestBitmap);
				userImage.setImageBitmap(myBitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	private void initListener() {
		
		userImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
				getIntent.setType(IMAGE_TYPE);
				startActivityForResult(getIntent,IMAGE_CODE);
			}
		});
		
		uploadImage.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File("/mnt/sdcard/"+Path);
				if (file != null) {
					final BmobFile bmobFile = new BmobFile(file);
					final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setMessage("�����ϴ�������");
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();
					bmobFile.upload(MainActivity.this, new UploadFileListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							url = bmobFile.getUrl();
							insertObject(new PersonBean("���ط���","123456",bmobFile));
							Toast.makeText(MainActivity.this, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
							progressDialog.dismiss();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "�ϴ�ʧ��"+arg1, Toast.LENGTH_SHORT).show();						
						}

					});
				}else {
						Toast.makeText(MainActivity.this, "�ļ�Ϊ��", Toast.LENGTH_SHORT).show();
					}

				}
			
		});
		
//		initData();
		
		downloadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("�����ϴ�������");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						 try {
							 	Bitmap bitmap1=null;
				                URL myUrl;
				                url = "http://file.bmob.cn/" + url;
		                		Toast.makeText(MainActivity.this, url+"", Toast.LENGTH_SHORT).show();
		                        myUrl=new URL(url);
		                        HttpURLConnection conn=(HttpURLConnection)myUrl.openConnection();
		                        conn.setConnectTimeout(5000);
		                        conn.connect();
		                        InputStream is=conn.getInputStream();
		                        bitmap1=BitmapFactory.decodeStream(is);
		                        //��bitmapת��Բ��
		                        BitmapUtil bmuUtil = new BitmapUtil(MainActivity.this);
		                        mBitmap=bmuUtil.toRoundBitmap(bitmap1);
		                        is.close();
		                        Message msg = mhandler.obtainMessage(1, mBitmap);
		        				mhandler.sendMessage(msg);
		                } catch (MalformedURLException e) {
		                        // TODO Auto-generated catch block
		                        e.printStackTrace();
		                } catch (IOException e) {
		                        // TODO Auto-generated catch block
		                        e.printStackTrace();
		                }
						 Looper.loop();
					}
				}).start();
				
	        }
		});
	}

	private void initView() {
		userImage = (ImageView) findViewById(R.id.imageview);
		uploadImage = (Button) findViewById(R.id.uploadBtn);
		downloadBtn = (Button) findViewById(R.id.downloadBtn);
		downloadImg = (ImageView) findViewById(R.id.downimageview);
	}
	
	private void insertObject(final BmobObject obj){
		obj.save(MainActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "�������ݳɹ�", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
