package org.ncpsb.phoenixcluster.enhancer.webservice.service;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {


    @Autowired
    DoAnalysisService doAnalysisService;
    @Autowired
    FileUploadService fileUploadService;

    @Value("${mail.user}")
    String username ;
    @Value("${mail.password}")
    String password;
    @Value("${mail.smtp.host}")
    String mailSmtpHost;
    @Value("${mail.smtp.port}")
    String mailSmtpPort;

    public String checkAnalysisToSendEmail() {
        List<AnalysisJob> analysisJobs =fileUploadService.getAnalysisJobsToSentEmail();
        if (analysisJobs == null || analysisJobs.size() <=0){
            return null;
        }
        for (AnalysisJob analysisJob : analysisJobs) {
            System.out.println(analysisJob.getId());
            String sentFlag = prepareAndSendEmail(analysisJob.getEmailAdd(), analysisJob.getStatus(), analysisJob.getId(), analysisJob.getToken());
            if (sentFlag.equalsIgnoreCase("done")) {
                fileUploadService.updateAnalysisRecordEmailSentStatus(analysisJob.getId(), true);
            }
        }
//        while (analysisJob.getStatus()!="finished" && analysisJob.getStatus() !="finished_with_error") {
//            analysisJob = doAnalysisService.getAnalysisJob(analysisJobId);
//        }
        return null;
    }

    private String prepareAndSendEmail(String toAddress, String analysisJobFinalStatus, Integer analysisJobId, String analysisJobToken) {
        String subject = "Phoenix Enhancer Analysis Job Finished";
        String emailContent = prepareEmailContent(analysisJobFinalStatus, analysisJobId, analysisJobToken);
        return (sendEmail(toAddress, subject, emailContent));
    }

    private String sendEmail(String toAddress, String subject, String mailContent) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);

        System.out.println(toAddress);
        System.out.println(subject);
        System.out.println(mailContent);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(mailContent);
            Transport.send(message);
            System.out.println("Done sending email to " + toAddress);
            return("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String prepareEmailContent(String analysisJobFinalStatus, Integer analysisJobId, String analysisJobToken) {
        StringBuffer mailContent = new StringBuffer();
        mailContent.append("Dear User, \n\n");
        if (analysisJobFinalStatus.equalsIgnoreCase("finished")) {
            mailContent.append("Your analysis job on Phenix Ehnacer was finished, please go to " + "http://enhancer.ncpsb.org/job_progress/" + analysisJobToken + " for details\n");
            mailContent.append("and to http://enhancer.ncpsb.org/high_conf/" + analysisJobId + " for checking your results." + "\n");
        }
        else {
            if(analysisJobFinalStatus.equalsIgnoreCase("finished_with_error")) {
                mailContent.append("Your analysis job on Phenix Ehnacer was finished, but with error, please go to " + "http://enhancer.ncpsb.org/job_progress/" + analysisJobToken + " for details\n");
            }
            else {
                System.out.println("The status of analysis job " + analysisJobId + "error, neither 'finished' nor 'finished_with_error'");
                return null;
            }
        }
        mailContent.append("Your analysis job's Id is " + analysisJobId +" and token is " + analysisJobToken +".\n");
        mailContent.append("If you have any questions, please contact our supporting group by replying this email." +"\n");
        mailContent.append("\n\n");
        mailContent.append("Best regards," + "\n");
        mailContent.append("PhoenixEnhancer supporting group");

        return mailContent.toString();
    }
}
