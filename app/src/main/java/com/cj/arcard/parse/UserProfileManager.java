package com.cj.arcard.parse;

import android.content.Context;
import android.os.Environment;

import com.cj.arcard.DemoHelper;
import com.cj.arcard.DemoHelper.DataSyncListener;
import com.cj.arcard.bean.MyUser;
import com.cj.arcard.utils.LogUtil;
import com.cj.arcard.utils.PreferenceManager;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserProfileManager {

	private String avatorUrl;
	private String useravatarUrl;

	/**
	 * application context
	 */
	protected Context appContext = null;

	/**
	 * init flag: test if the sdk has been inited before, we don't need to init
	 * again
	 */
	private boolean sdkInited = false;

	/**
	 * HuanXin sync contact nick and avatar listener
	 */
	private List<DataSyncListener> syncContactInfosListeners;

	private boolean isSyncingContactInfosWithServer = false;

	private EaseUser currentUser;

	public UserProfileManager() {
	}

	public synchronized boolean init(Context context) {
		if (sdkInited) {
			return true;
		}
		ParseManager.getInstance().onInit(context);
		syncContactInfosListeners = new ArrayList<DataSyncListener>();
		sdkInited = true;
		return true;
	}

	public void addSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (!syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.add(listener);
		}
	}

	public void removeSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.remove(listener);
		}
	}

	public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
		if (isSyncingContactInfosWithServer) {
			return;
		}
		isSyncingContactInfosWithServer = true;
		ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {

			@Override
			public void onSuccess(List<EaseUser> value) {
				isSyncingContactInfosWithServer = false;
				// in case that logout already before server returns,we should
				// return immediately
				if (!DemoHelper.getInstance().isLoggedIn()) {
					return;
				}
				if (callback != null) {
					callback.onSuccess(value);
				}
			}

			@Override
			public void onError(int error, String errorMsg) {
				isSyncingContactInfosWithServer = false;
				if (callback != null) {
					callback.onError(error, errorMsg);
				}
			}

		});

	}

	public void notifyContactInfosSyncListener(boolean success) {
		for (DataSyncListener listener : syncContactInfosListeners) {
			listener.onSyncComplete(success);
		}
	}

	public boolean isSyncingContactInfoWithServer() {
		return isSyncingContactInfosWithServer;
	}

	public synchronized void reset() {
		isSyncingContactInfosWithServer = false;
		currentUser = null;
		PreferenceManager.getInstance().removeCurrentUserInfo();
	}

	public synchronized EaseUser getCurrentUserInfo() {
		if (currentUser == null) {
			String username = EMClient.getInstance().getCurrentUser();
			currentUser = new EaseUser(username);
			String nick = getCurrentUserNick();
			currentUser.setNick((nick != null) ? nick : username);
			currentUser.setAvatar(getCurrentUserAvatar());
		}
		return currentUser;
	}

	public boolean updateCurrentUserNickName(final String nickname) {
		boolean isSuccess = ParseManager.getInstance().updateParseNickName(nickname);
		if (isSuccess) {
			setCurrentUserNick(nickname);
		}
		return isSuccess;
	}

	public String uploadUserAvatar(final byte[] data) {
		//String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);

		//String avatarUrl = "http://bmob-cdn-9905.b0.upaiyun.com/2017/03/20/4d19e8864059118780babf6be40a179b.jpg";
		new Thread(new Runnable() {
			@Override
			public void run() {
				useravatarUrl = uploadAvator(data);
				if (useravatarUrl != null) {
				}
			}
		}).start();

		return useravatarUrl;


	}

	public String uploadAvator(byte[] data){

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(Environment.getExternalStorageDirectory(),"avator.jpg");
			if(!file.exists()&&file.isDirectory()){
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(data);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}


		}

		//将头像文件上传到bmob

		final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory(),"avator.jpg"));
		bmobFile.uploadblock(new UploadFileListener() {

			@Override
			public void done(BmobException e) {
				if(e==null){
					//bmobFile.getFileUrl()--返回的上传文件的完整地址
					avatorUrl = bmobFile.getFileUrl();
					setCurrentUserAvatar(avatorUrl);
					LogUtil.d(avatorUrl);
					String currentUserID = BmobUser.getCurrentUser(MyUser.class).getObjectId();
					LogUtil.d("currentUserID"+currentUserID);


					getCurrentUserInfo().setAvatar(avatorUrl);
					//DemoHelper.getInstance().setCurrentUserAvatorUrl(avatorUrl);

					MyUser myUser = new MyUser();
					myUser.setAvatorUrl(avatorUrl);
					myUser.update(currentUserID, new UpdateListener() {
						@Override
						public void done(BmobException e) {
							if (e == null){
								LogUtil.d("suss");
							}else {
								LogUtil.d(e.getMessage()+e.getErrorCode());
							}

						}
					});
				}else{
					LogUtil.d("cjj",e.getMessage()+e.getErrorCode());
				}

			}

			@Override
			public void onProgress(Integer value) {
				// 返回的上传进度（百分比）
			}
		});

		return avatorUrl;
	}

	public void asyncGetCurrentUserInfo() {
		ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {

			@Override
			public void onSuccess(EaseUser value) {
			    if(value != null){
    				setCurrentUserNick(value.getNick());
    				setCurrentUserAvatar(value.getAvatar());
			    }
			}

			@Override
			public void onError(int error, String errorMsg) {

			}
		});

	}
	public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){
		ParseManager.getInstance().asyncGetUserInfo(username, callback);
	}
	private void setCurrentUserNick(String nickname) {
		getCurrentUserInfo().setNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);
	}

	private void setCurrentUserAvatar(String avatar) {
		getCurrentUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}

	private String getCurrentUserNick() {
		return PreferenceManager.getInstance().getCurrentUserNick();
	}

	private String getCurrentUserAvatar() {
		return PreferenceManager.getInstance().getCurrentUserAvatar();
	}

}
