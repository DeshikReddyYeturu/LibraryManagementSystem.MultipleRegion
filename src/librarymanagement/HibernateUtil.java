package librarymanagement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.*;
import java.util.*;

public class HibernateUtil{

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            Properties properties = loadProperties();
            configuration.setProperty("hibernate.connection.url", properties.getProperty("db.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("db.username"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("db.password"));
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

            return configuration.configure("hibernate.cfg.xml").buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("db.")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        properties.setProperty(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        }
        return properties;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}