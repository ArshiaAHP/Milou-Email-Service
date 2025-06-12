package aut.ap.service;

import aut.ap.dao.EmailDao;
import aut.ap.dao.EmailRecipientDao;
import aut.ap.dao.UserDao;
import aut.ap.model.Email;
import aut.ap.model.EmailRecipient;
import aut.ap.model.User;
import aut.ap.util.CodeGenerator;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//using Dao classes to give services for email and user
public class EmailService {
    private final EmailDao emailDao = new EmailDao();
    private final EmailRecipientDao recipientDao = new EmailRecipientDao();
    private final UserDao userDao = new UserDao();

    public String sendEmail(User sender, String recipientEmails, String subject, String body) throws Exception {
        if (subject == null || subject.trim().isEmpty()) {
            throw new Exception("Subject cannot be empty.");
        }
        if (subject.trim().length() > 255) {
            throw new Exception("Subject too long (max 255 characters).");
        }
        if (recipientEmails == null || recipientEmails.trim().isEmpty()) {
            throw new Exception("Recipient list cannot be empty.");
        }
        String code = CodeGenerator.generateCode(emailDao);
        Email email = new Email(subject.trim(), body != null ? body.trim() : "", LocalDateTime.now(), code, sender);
        emailDao.saveEmail(email);

        List<String> recipientList = Arrays.asList(recipientEmails.split(",\\s*"));
        for (String recipientEmail : recipientList) {
            String normalizedEmail = recipientEmail.trim().toLowerCase();
            if (normalizedEmail.isEmpty()) {
                continue;
            }
            if (!normalizedEmail.endsWith("@milou.com")) {
                normalizedEmail = normalizedEmail + "@milou.com";
            }
            User recipient = userDao.findByEmail(normalizedEmail);
            if (recipient == null) {
                throw new Exception("Recipient " + normalizedEmail + " does not exist.");
            }
            recipientDao.saveEmailRecipient(new EmailRecipient(email, recipient));
        }
        return code;
    }

    public List<Email> getAllEmails(User user) {
        List<Email> sentEmails = emailDao.getSentEmails(user);
        List<Email> receivedEmails = emailDao.getReceivedEmails(user);
        sentEmails.addAll(receivedEmails);
        return sentEmails.stream()
                .distinct()
                .sorted((e1, e2) -> e2.getSentDate().compareTo(e1.getSentDate()))
                .collect(Collectors.toList());
    }

    public List<Email> getUnreadEmails(User user) {
        return emailDao.getUnreadEmails(user);
    }

    public List<Email> getSentEmails(User user) {
        return emailDao.getSentEmails(user);
    }

    public Email getEmailByCode(User user, String code) throws Exception {
        if (code == null || code.trim().isEmpty()) {
            throw new Exception("Code cannot be empty.");
        }
        Email email = emailDao.findByCode(code.trim());
        if (email == null) {
            throw new Exception("Email not found.");
        }
        if (!email.getSender().equals(user) && recipientDao.findByEmailAndUser(email, user) == null) {
            throw new Exception("You cannot read this email.");
        }
        EmailRecipient recipient = recipientDao.findByEmailAndUser(email, user);
        if (recipient != null && !recipient.isRead()) {
            recipientDao.markAsRead(recipient);
        }
        return email;
    }

    public String replyToEmail(User user, String code, String body) throws Exception {
        Email original = getEmailByCode(user, code);
        String newSubject = "[Re] " + original.getSubject();
        if (newSubject.length() > 255) {
            newSubject = newSubject.substring(0, 255);
        }
        String newCode = CodeGenerator.generateCode(emailDao);
        Email reply = new Email(newSubject, body != null ? body.trim() : "", LocalDateTime.now(), newCode, user);
        emailDao.saveEmail(reply);

        // Reply to original sender
        recipientDao.saveEmailRecipient(new EmailRecipient(reply, original.getSender()));
        // Reply to all other recipients, excluding the replying user
        for (EmailRecipient originalRecipient : original.getRecipients()) {
            if (!originalRecipient.getRecipient().equals(user)) {
                recipientDao.saveEmailRecipient(new EmailRecipient(reply, originalRecipient.getRecipient()));
            }
        }
        return newCode;
    }

    public String forwardEmail(User user, String code, String recipientEmails) throws Exception {
        Email original = getEmailByCode(user, code);
        String newSubject = "[Fw] " + original.getSubject();
        if (newSubject.length() > 255) {
            newSubject = newSubject.substring(0, 255);
        }
        String newCode = CodeGenerator.generateCode(emailDao);
        Email forward = new Email(newSubject, original.getBody(), LocalDateTime.now(), newCode, user);
        emailDao.saveEmail(forward);

        List<String> recipientList = Arrays.asList(recipientEmails.split(",\\s*")); // split to emails after the comma (ignoring whitespaces) then add to a list of strings.
        for (String recipientEmail : recipientList) {
            String normalizedEmail = recipientEmail.trim().toLowerCase(); //whitespaces. number one enemy :)
            if (normalizedEmail.isEmpty()) {
                continue;
            }
            if (!normalizedEmail.endsWith("@milou.com")) {
                normalizedEmail = normalizedEmail + "@milou.com";
            }
            User recipient = userDao.findByEmail(normalizedEmail);
            if (recipient == null) {
                throw new Exception("Recipient " + normalizedEmail + " does not exist.");
            }
            recipientDao.saveEmailRecipient(new EmailRecipient(forward, recipient));
        }
        return newCode;
    }
}