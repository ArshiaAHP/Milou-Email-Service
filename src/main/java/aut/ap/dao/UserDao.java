package aut.ap.dao;

import aut.ap.model.User;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

//For working with User & Hibernate (saving and finding by email.)
public class UserDao {
    public void saveUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by email: " + e.getMessage(), e);
        }
    }
}