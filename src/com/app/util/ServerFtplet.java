package com.app.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author Ni Guijun
 *
 */
public class ServerFtplet extends DefaultFtplet {

	public static final int PORT = 3333;
//	public static final String directory = Environment.getExternalStorageDirectory().getPath();//指定SD卡根目录
	public static final String directory = Environment.getExternalStorageDirectory().getPath() + "/zStore/data/";//指定到SD卡某个目录
	
	private FtpServer mFtpServer;
	private boolean isAnonymous = true;//是否需要匿名登陆

	private final String mUser = "admin";//非匿名登陆时的账户
	private final String mPassword = "";

	private static ServerFtplet mInstance;

	public static ServerFtplet getInstance() {
		if (mInstance == null) {
			mInstance = new ServerFtplet();
		}
		return mInstance;
	}

	/**
	 * FTP启动
	 * 
	 * @throws FtpException
	 */
	public void start(){
		try{
			if (null != mFtpServer && false == mFtpServer.isStopped()) {
				return;
			}
	
			File file = new File(directory);
			if (!file.exists()) {
				file.mkdirs();
			}
	
			FtpServerFactory serverFactory = new FtpServerFactory();
			ListenerFactory listenerFactory = new ListenerFactory();
	
			// 设定端末番号
			listenerFactory.setPort(PORT);
	
			// 通过PropertiesUserManagerFactory创建UserManager然后向配置文件添加用户
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
			UserManager userManager = userManagerFactory.createUserManager();
	
			List<Authority> auths = new ArrayList<Authority>();
			Authority auth = new WritePermission();
			auths.add(auth);
	
			// 添加用户
			BaseUser user = new BaseUser();
			if(isAnonymous){
				user.setName("anonymous");//匿名登陆,不需要密码
			}else{
				user.setName(mUser);
				user.setPassword(mPassword);
				user.setAuthorities(auths);
			}
			user.setHomeDirectory(directory);
			userManager.save(user);
	
			// 设定Ftplet
			Map<String, Ftplet> ftpletMap = new HashMap<String, Ftplet>();
			ftpletMap.put("Ftplet", this);
	
			serverFactory.setUserManager(userManager);
			serverFactory.addListener("default", listenerFactory.createListener());
			serverFactory.setFtplets(ftpletMap);
	
			// 创建并启动FTPServer
			mFtpServer = serverFactory.createServer();
			mFtpServer.start();
		}catch(FtpException e){
			e.printStackTrace();
		}
	}

	/**
	 * FTP停止
	 */
	public void stop() {
		// FtpServer不存在和FtpServer正在运行中
		if (null != mFtpServer && false == mFtpServer.isStopped()) {
			mFtpServer.stop();
			mFtpServer=null;
		}
	}

	@Override
	public FtpletResult onAppendStart(FtpSession session, FtpRequest request)
			throws FtpException, IOException {
//		Log.i("tag", "===========onAppendStart=============");
		return super.onAppendStart(session, request);
	}

	@Override
	public FtpletResult onAppendEnd(FtpSession session, FtpRequest request)
			throws FtpException, IOException {
//		Log.i("tag", "===========onAppendEnd=============");
		return super.onAppendEnd(session, request);
	}

	@Override
	public FtpletResult onLogin(FtpSession session, FtpRequest request)
			throws FtpException, IOException {
//		Log.i("tag", "===========onLogin=============");
		return super.onLogin(session, request);
	}

	@Override
	public FtpletResult onConnect(FtpSession session) throws FtpException,
			IOException {
//		Log.i("tag", "===========onConnect=============");
		return super.onConnect(session);
	}

	@Override
	public FtpletResult onDisconnect(FtpSession session) throws FtpException,
			IOException {
//		Log.i("tag", "===========onDisconnect=============");
		return super.onDisconnect(session);
	}

	@Override
	public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
			throws FtpException, IOException {
//		Log.i("tag", "===========onUploadStart=============");
		return super.onUploadStart(session, request);
	}

	@Override
	public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
			throws FtpException, IOException {
//		Log.i("tag", "===========onUploadEnd=============");
		String FtpUploadPath = directory + request.getArgument();
		// 接收到文件后立即删除
		File uploadFile = new File(FtpUploadPath);
		uploadFile.delete();
		return super.onUploadEnd(session, request);
	}
}
