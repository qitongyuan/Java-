package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger= LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }
    //对外公开的接口(上传成功是否)
    public static  boolean uploadFile(List<File>fileList) throws IOException {
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始链接服务器");
        boolean result=ftpUtil.uploadFile("img",fileList);
        logger.info("开始链接ftp服务器,结束上传,上传结果为：{}");
        return result;

    }

    //具体的上传方法
    private boolean uploadFile(String remotePath,List<File>fileList) throws IOException {
        boolean upload=true;
        FileInputStream fis=null;
        //链接服务器
        if (connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                //是否需要新建上传文件目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                //开始遍历文件进行上传
                for (File fileItem:fileList){
                   fis=new FileInputStream(fileItem);
                   ftpClient.storeFile(fileItem.getName(),fis);

                }
            } catch (IOException e) {
                upload=false;
               logger.error("上传异常",e);
            }finally {
                fis.close();
                ftpClient.disconnect();;
            }
        }
        return upload;
    }
    //封装的链接服务器的方法
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess=false;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            //登录
            isSuccess=ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("链接ftp服务器异常",e);
        }
        return isSuccess;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getIp() {
        return ip;

    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
