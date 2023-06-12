package com.ej.automation.sbi.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailReport {
	public static final Logger logger = LoggerFactory.getLogger(SendEmailReport.class);
	
	static FileInputStream fis = null;
	static FileOutputStream fos = null;
	static ZipOutputStream zos = null; 
	
	public static int sendEmailWithAttachMent(String mailhost,String fileName,String from, String to,
			String emailsub,String messageContent) throws IOException {
		try {
			String zipFileName = fileCompressing(fileName);
			Properties props = System.getProperties();
			if (mailhost != null)
				props.put("mail.smtp.host", mailhost);
				props.put("mail.smtp.starttls.enable", true);
				String msgContent	=	messageContent;
				// Get a Session object
				Session session = Session.getDefaultInstance(props, null);
				//session.setDebug(true);
				MimeMessage msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(from));
				if (!emailsub.contains("\r") && !emailsub.contains("\n"))
					msg.setSubject(emailsub);
				msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
				//msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc, false));
				
				MimeMultipart mp = new MimeMultipart("related");
				// create the second message part
			    MimeBodyPart mbp2 = new MimeBodyPart();
	            // attach the file to the message
			    FileDataSource fds = new FileDataSource(zipFileName);
			    mbp2.setDataHandler(new DataHandler(fds));
			    mbp2.setFileName(fds.getName());
			    
			    BodyPart bp0 = new MimeBodyPart();

			   // msgContent+="</BODY></HTML>";
				bp0.setContent(msgContent,"text/html");
				mp.addBodyPart(bp0);
				mp.addBodyPart(mbp2);
				msg.setContent(mp);
				Transport.send(msg);
				Thread.sleep(1l);
				return 1;
		} catch (Exception e) {
			logger.error("Exception inside sendEmailWithAttachMent :: "+e.getMessage());
			return 0;
		}
	}
	
	
	public static int sendEmail(String mailhost,String from, String to,
			String emailsub,String messageContent) throws IOException {
		try {
			Properties props = System.getProperties();
			if (mailhost != null)
				props.put("mail.smtp.host", mailhost);
			
			Session session = Session.getDefaultInstance(props, null);
	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(from));

	         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

	         message.setSubject(emailsub);

	         // Now set the actual message
	        // message.setText(messageContent);
	         message.setContent(messageContent, "text/html;charset=UTF-8");
	         // Send message
	         Transport.send(message);
				return 1;
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	         return 0;
	      }
		
		finally {
		}
	}
	
	public static String fileCompressing(String fileName) {
		String outputFile = null;
		File file = new File(fileName);
		try {
			String format = file.getAbsolutePath().substring(file.getAbsolutePath().length()-3, file.getAbsolutePath().length());
			if(format.equalsIgnoreCase("txt") || format.equalsIgnoreCase("csv") || format.equalsIgnoreCase("xls")) {
				outputFile = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-4)+".zip";
			}else {
				outputFile = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-5)+".zip";
			}
			fos = new FileOutputStream(outputFile);
			zos = new ZipOutputStream(fos);

			ZipEntry zipEntry = new ZipEntry(file.getName());
			
			zos.putNextEntry(zipEntry);
			fis = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
		} catch (IOException e) {
			FileXLogger.logCommonError(e);
			//e.printStackTrace();
		} finally {
			try {
				zos.closeEntry();
	            zos.close();
	            fis.close();
	            fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputFile;
	}
}

