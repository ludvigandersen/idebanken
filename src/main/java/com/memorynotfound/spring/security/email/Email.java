package com.memorynotfound.spring.security.email;

import com.memorynotfound.spring.security.model.Person;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {
    // write your code here
    // change accordingly
    final String username = "tryllemikkel@gmail.com";

    // change accordingly
    final String password = "dcc59vez";

    // Get system properties
    Properties props = new Properties();

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {

                //override the getPasswordAuthentication method
                protected PasswordAuthentication
                getPasswordAuthentication() {

                    return new PasswordAuthentication(username,
                            password);
                }
            });

    public Email() throws AddressException, MessagingException {

        // enable authentication
        props.put("mail.smtp.auth", "true");

        // enable STARTTLS
        props.put("mail.smtp.starttls.enable", "true");

        // Setup mail server
        props.put("mail.smtp.host", "smtp.gmail.com");

        // TLS Port
        props.put("mail.smtp.port", "587");

        // creating Session instance referenced to
        // Authenticator object to pass in
        // Session.getInstance argument

    }

    public void emailCreatePerson(Person person){
        try {

            // compose the message
            // javax.mail.internet.MimeMessage class is
            // mostly used for abstraction.
            Message message = new MimeMessage(session);

            // header field of the header.
            message.setFrom(new InternetAddress("tryllemikkel@gmail.com"));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(person.getEmail()));
            message.setSubject("Din bruger er oprettet");
            message.setText("Hej, du har oprettet en bruger på Idébanken. " +
                    "Du kan logge ind på siden med din email: " + person.getEmail() +
                    " samt det kodeord du har valgt.");

            Transport.send(message);         //send Message

            System.out.println("Email sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}