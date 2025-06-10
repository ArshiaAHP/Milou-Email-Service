package aut.ap.dao;

import aut.ap.model.Email;
import aut.ap.model.User;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class EmailDao {
    public void saveEmail(Email email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(email);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save email: " + e.getMessage(), e);
        }
    }

    public Email findByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery("FROM Email WHERE code = :code", Email.class);
            query.setParameter("code", code);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find email by code: " + e.getMessage(), e);
        }
    }

    public List<Email> getSentEmails(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery(
                    "FROM Email WHERE sender = :sender ORDER BY sentDate DESC",
                    Email.class
            );
            query.setParameter("sender", user);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve sent emails: " + e.getMessage(), e);
        }
    }

    public List<Email> getReceivedEmails(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery(
                    "SELECT er.email FROM EmailRecipient er WHERE er.recipient = :recipient ORDER BY er.email.sentDate DESC",
                    Email.class
            );
            query.setParameter("recipient", user);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve received emails: " + e.getMessage(), e);
        }
    }

    public List<Email> getUnreadEmails(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Email> query = session.createQuery(
                    "SELECT er.email FROM EmailRecipient er WHERE er.recipient = :recipient AND er.isRead = false ORDER BY er.email.sentDate DESC",
                    Email.class
            );
            query.setParameter("recipient", user);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve unread emails: " + e.getMessage(), e);
        }
    }
}