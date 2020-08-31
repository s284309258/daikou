package com.renegade.util;

import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/** 
*  @Copyright    2018 
*        @author Renegade丶無歡  
*                    All right reserved
*      @Created   2018年11月30日 
*   @version  1.0
* @email 291312408@qq.com
*/
public class SSHConnection {
	private final static String S_PATH_FILE_PRIVATE_KEY = "I:\\SSH";
	private final static String S_PATH_FILE_KNOWN_HOSTS = "/Users/hdwang/.ssh/known_hosts";
	private final static String S_PASS_PHRASE = "";
	private final static int LOCAl_PORT = 3306;
	private final static int REMOTE_PORT = 3306; 
	private final static int SSH_REMOTE_PORT = 22;
	private final static String SSH_USER = "tianxiamei";
	private final static String SSH_PASSWORD = "83.TixaEI";
	private final static String SSH_REMOTE_SERVER = "47.52.129.60";
	private final static String MYSQL_REMOTE_SERVER = "localhost"; 

	private Session sesion; //represents each ssh session

	public void closeSSH ()
	{
	    sesion.disconnect();
	}

	public SSHConnection () throws Throwable
	{

	    JSch jsch = null;

	        jsch = new JSch();
	        //jsch.setKnownHosts(S_PATH_FILE_KNOWN_HOSTS);
	        jsch.addIdentity(S_PATH_FILE_PRIVATE_KEY);

	        sesion = jsch.getSession(SSH_USER, SSH_REMOTE_SERVER, SSH_REMOTE_PORT);

	        sesion.setPassword(SSH_PASSWORD);
	        Properties config = new Properties();
	        config.put("StrictHostKeyChecking", "no");
	        sesion.setConfig(config);

	        sesion.connect(); //ssh connection established!

	        //by security policy, you must connect through a fowarded port          
	        sesion.setPortForwardingL(LOCAl_PORT, MYSQL_REMOTE_SERVER, REMOTE_PORT);

	}
	}


