package com.mmt.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.*;

 

public class SendMail{
    public  static void sendMail(){
    	
    	String[] to=  {"ayaz.hasan.testing@gmail.com","mail.ayaz.hasan@gmail.com"};
		String[] cc=  {"ayaz.hasan.testing@gmail.com"};
		String[] bcc= {"ayaz.hasan.testing@gmail.com"};
		
    	final String user="ayaz.hasan.testing@gmail.com";//change accordingly
    	final String password="pbskqzihrjzojrdt";//change accordingly
    	 
    	  //1) get the session object   
    	  Properties properties = System.getProperties();
    	  properties.put("mail.smtp.auth", "true");
    	  properties.put("mail.smtp.starttls.enable", "true");
    	  properties.put("mail.smtp.host", "smtp.gmail.com");
    	  properties.put("mail.smtp.port", "587");

    	  Session session = Session.getDefaultInstance(properties,
    	   new javax.mail.Authenticator() {
    	   protected PasswordAuthentication getPasswordAuthentication() {
    	   return new PasswordAuthentication(user,password);
    	   }
    	  });
    	   
    	  //2) compose message   
    	  try{
    	    MimeMessage message = new MimeMessage(session);
    	    message.setFrom(new InternetAddress(user));
    	    
    	    for(int i=0;i<to.length;i++){
    	    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
    	        }

    	     for(int i=0;i<cc.length;i++){
    	         message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
    	         }

    	      for(int i=0;i<bcc.length;i++){
                 message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
                 }
    	    
    	    message.setSubject("Digital Marketing Automation Report");
    	    
    	    //3) create MimeBodyPart object and set your message content    
    	    BodyPart messageBodyPart1 = new MimeBodyPart();
    	    messageBodyPart1.setText("Hi,\n\nPlease find the reports attached.\n\nRegards,\nAyaz");
    	    
    	    //4) create new MimeBodyPart object and set DataHandler object to this object    
    	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();

    	    String filepath=FindLatestFile();
    	    DataSource source = new FileDataSource(filepath);
    	   // System.out.println(filepath);
    	    messageBodyPart2.setDataHandler(new DataHandler(source));
    	    messageBodyPart2.setFileName("DM Automation Report.pdf");
    	    //5) create Multipart object and add MimeBodyPart objects to this object    
    	    Multipart multipart = new MimeMultipart();
    	    multipart.addBodyPart(messageBodyPart1);
    	    multipart.addBodyPart(messageBodyPart2);

    	    //6) set the multiplart object to the message object
    	    message.setContent(multipart );
    	   
    	    //7) send message
    	    Transport.send(message);
    	 
    	  System.out.println("Email sent....");
    	   }catch (MessagingException ex) {ex.printStackTrace();}
    	 }
	// find the last modified file to attach
    private static String FindLatestFile() 
    {
		File file=new File(System.getProperty("user.dir")+"\\PDF Reports");
		File[] listofFile = file.listFiles();
		long data=0,data1;
		File latest = null;
		for (File file2 : listofFile) {
			data1 = file2.lastModified();
			if(data1>data)
			{				
				long temp = data;
				data=data1;
				data1=temp;	
				latest=file2;
			}
		}
		return latest.getAbsolutePath();
	}

	public static void main(String[] args) throws Exception

    {
    	
        SendMail.sendMail();  

    }
}