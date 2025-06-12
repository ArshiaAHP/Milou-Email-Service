package aut.ap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Lob
    private String body;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @Column(length = 6, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @OneToMany(mappedBy = "email")
    private List<EmailRecipient> recipients = new ArrayList<>();


    public Email() {} //for hibernate to use
    public Email(String subject, String body, LocalDateTime sentDate, String code, User sender) {
        this.subject = subject;
        this.body = body;
        this.sentDate = sentDate;
        this.code = code;
        this.sender = sender;
    }

    //setters and getters
    public Long getId() { return id; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public String getCode() { return code; }
    public LocalDateTime getSentDate() { return sentDate; }
    public User getSender() { return sender; }
    public List<EmailRecipient> getRecipients() { return recipients; }

    public void setSubject(String subject) { this.subject = subject; }
    public void setBody(String body) { this.body = body; }
    public void setCode(String code) { this.code = code; }
}
