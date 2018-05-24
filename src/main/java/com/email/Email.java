package com.email;
import com.model.Idea;
import com.model.Person;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author Mikkel
 */
public class Email {

    // Her skal du &aelig;ndre email adressen
    final String username = "tryllemikkel@gmail.com";

    // Her skal du &aelig;ndre passwordet, som passer til ovenst&aring;ende e-mail adresse
    final String password = "dcc59vez";

    Properties props = new Properties();

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

    /**
     * Her i constructoren s&aelig;tter vi flere smtp parametre, bla. at vores mail host er googles gmail server
     * som bruger port 587.
     */
    public Email(){
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     * Denne metode bliver kaldt n&aring;r der bliver oprettet en ny person p&aring; vores side
     * E-mailen bliver sendt til personen der har oprettet en profil.
     */
    public void emailCreatePerson(Person person){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(person.getEmail()));
            message.setSubject("Din bruger er oprettet");
            message.setText("Hej, du har oprettet en bruger p&aring; Id&eacute;banken. " +
                    "Du kan logge ind p&aring; siden med din email: " + person.getEmail() +
                    " samt det kodeord du har valgt.");
            Transport.send(message);

            System.out.println("Email sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Denne metode bliver brugt n&aring;r en udvikler p&aring; siden ans&oslash;ger en id&eacute;.
     * E-mailen bliver sendt til den person som har oprettet id&eacute;en.
     */
    public void emailApplyToIdea(Person developer, String applyMessage, String ideaEmail, Idea idea){
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ideaEmail));
            message.setSubject("En udvikler har ans&oslash;gt din id&eacute;");
            message.setText("Hej, en udvikler p&aring; sitet har ans&oslash;gt din id&eacute;: " + idea.getIdeaName() +
                            " login p&aring; idebanken for at godkende udvikleren. \n " +
                            "Udvikler oplysninger: \n " +
                            "Navn: " + developer.getFirstName() + " " + developer.getLastName() + "\n" +
                            "Email: " + developer.getEmail() + "\n" +
                            "Besked fra udvikleren: " + applyMessage);

            Transport.send(message);

            System.out.println("Email sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}