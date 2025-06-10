package aut.ap.dao;

import aut.ap.model.Email;
import aut.ap.model.EmailRecipient;
import aut.ap.model.User;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class EmailRecipientDao {
    public void saveEmailRecipient(EmailRecipient recipient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(recipient);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save email recipient: " + e.getMessage(), e);
        }
    }

    public void markAsRead(EmailRecipient recipient) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            recipient.setRead(true);
            session.merge(recipient);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to mark email as read: " + e.getMessage(), e);
        }
    }

    public EmailRecipient findByEmailAndUser(Email email, User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<EmailRecipient> query = session.createQuery(
                    "FROM EmailRecipient WHERE email = :email AND recipient = :recipient",
                    EmailRecipient.class
            );
            query.setParameter("email", email);
            query.setParameter("recipient", user);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find email recipient: " + e.getMessage(), e);
        }
    }
}