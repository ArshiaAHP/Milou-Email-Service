package aut.ap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "email_recipients")
public class EmailRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "email_id")
    private Email email;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    public EmailRecipient() {}

    public EmailRecipient(Email email, User recipient) {
        this.email = email;
        this.recipient = recipient;
    }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public Email getEmail() { return email; }
    public User getRecipient() { return recipient; }
}
