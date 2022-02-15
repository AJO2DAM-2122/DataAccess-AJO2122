package com.aajaor2122.unit5;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class LibraryModel {

    // Creating and opening the session
    public static Session openSession() throws HibernateException {
        // Code to avoid the warnings
        @SuppressWarnings("unused")
        org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
        java.util.logging.Logger.getLogger("org.hibernate") .setLevel(Level.SEVERE);

        SessionFactory sessionFactory = new
                Configuration().configure().buildSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
        } catch (Exception ex) {
            System.err.println("Exception while opening session...");
            LibraryController.reportError(ex);
        }
        if (session == null) {
            LibraryController.resultMessage("Session was found to be null.");
        }

        return session;
    }

    public static UsersJpaEntity getUserByCode(String code) {
        UsersJpaEntity user = null;
        try (Session session = openSession()) {
            Query<UsersJpaEntity> userQuery =
                    session.createQuery("from com.aajaor2122.unit5.UsersJpaEntity where code='" +
                            String.valueOf(code) + "' ");
            List<UsersJpaEntity> users = userQuery.list();
            user = (UsersJpaEntity) users.get(0);

            if (user == null)
                LibraryController.resultMessage("The code of the user is NOT correct.");
        } catch (Exception e) {
            LibraryController.reportError(e);
        }

        return user;
    }

    public static BooksJpaEntity getBookByIsbn (String isbn) {
        BooksJpaEntity book = null;
        try (Session session = openSession()) {
            Query<BooksJpaEntity> booksQuery =
                    session.createQuery("from com.aajaor2122.unit5.BooksJpaEntity where isbn='" +
                            String.valueOf(isbn) + "' ");
            List<BooksJpaEntity> books = booksQuery.list();
            book = (BooksJpaEntity) books.get(0);

            if (book == null)
                LibraryController.resultMessage("The ISBN of the book is NOT correct.");

        } catch (Exception e) {
            LibraryController.reportError(e);
        }

        return book;
    }

    //CORRECTO: ahora si que funciona
    public static int getNumberTotalBorrowedCopies(String isbn) {
        BooksJpaEntity book = null;
        int copiesLendedNow = 0;
        try (Session session = openSession()) {
            Query<BooksJpaEntity> booksQuery =
                    session.createQuery("from com.aajaor2122.unit5.BooksJpaEntity where isbn='" +
                            String.valueOf(isbn) + "' ");
            List<BooksJpaEntity> books = booksQuery.list();
            book = (BooksJpaEntity) books.get(0);

            copiesLendedNow = book.getBorrowedBy().size();

            if (book == null)
                LibraryController.resultMessage("The ISBN of the book is NOT correct.");

        } catch (Exception e) {
            LibraryController.reportError(e);
        }

        return copiesLendedNow;
    }

    //TODO: da un error de ArrayIndexOutOfBoundsException, intentar solucionarlo
    public static int getUserBooksBorrowedNow(String code) {
        UsersJpaEntity user = null;
        int booksBorrowedNow = 0;

        try (Session session = openSession()) {
            Query<UsersJpaEntity> usersQuery =
                    session.createQuery("from com.aajaor2122.unit5.UsersJpaEntity where code='" +
                            String.valueOf(code) + "' ");
            List<UsersJpaEntity> users = usersQuery.list();
            user = (UsersJpaEntity) users.get(0);

            booksBorrowedNow = user.getLentBooks().size();

            if (user == null)
                LibraryController.resultMessage("The code of the User is NOT correct.");

        } catch (Exception e) {
            LibraryController.reportError(e);
        }

        return booksBorrowedNow;
    }

    public static void insertUser(String code, String name, String surname, Date birthday) {
        try (Session session = openSession()) {
            Transaction transaction = session.beginTransaction();
            UsersJpaEntity newUser = new UsersJpaEntity();
            newUser.setCode(code);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setBirthdate(birthday);
            session.save(newUser);
            transaction.commit();

            LibraryController.resultMessage("The User has been inserted into the database.");
        } catch (Exception e) {
            LibraryController.reportError(e);
        }
    }

    public static void insertBook(String isbn, String title, int copies, String outline, String publisher) {
        try (Session session = openSession()) {
            Transaction transaction = session.beginTransaction();
            BooksJpaEntity newBook = new BooksJpaEntity();
            newBook.setIsbn(isbn);
            newBook.setTitle(title);
            newBook.setCopies(copies);
            newBook.setOutline(outline);
            newBook.setPublisher(publisher);
            session.save(newBook);
            transaction.commit();

            LibraryController.resultMessage("The Book has been inserted into the database.");
        } catch (Exception e) {
            LibraryController.reportError(e);
        }
    }

    public static void editUser(String code, String name, String surname) {
        try (Session session = openSession()) {
            Query<UsersJpaEntity> usersQuery =
                    session.createQuery("from com.aajaor2122.unit5.UsersJpaEntity where code='" +
                            String.valueOf(code) + "' ");
            List<UsersJpaEntity> users = usersQuery.list();
            Transaction transaction = session.beginTransaction();
            UsersJpaEntity user = (UsersJpaEntity) users.get(0);
            user.setName(name);
            user.setSurname(surname);
            session.update(user);
            transaction.commit();

            LibraryController.resultMessage("The User has been updated successfully.");
        } catch (Exception e) {
        LibraryController.reportError(e);
        }
    }

    public static void editBook(String isbn, String title, int copies, String outline, String publisher) {
        try (Session session = openSession()) {
            Query<BooksJpaEntity> booksQuery =
                    session.createQuery("from com.aajaor2122.unit5.BooksJpaEntity where isbn='" +
                            String.valueOf(isbn) + "' ");
            List<BooksJpaEntity> books = booksQuery.list();
            Transaction transaction = session.beginTransaction();
            BooksJpaEntity book = (BooksJpaEntity) books.get(0);
            book.setTitle(title);
            book.setCopies(copies);
            book.setOutline(outline);
            book.setPublisher(publisher);
            session.update(book);
            transaction.commit();

            LibraryController.resultMessage("The Book has been updated successfully.");
        } catch (Exception e) {
            LibraryController.reportError(e);
        }
    }

    public static void insertLending() {

    }

}
