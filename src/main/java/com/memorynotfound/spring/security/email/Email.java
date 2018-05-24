package com.memorynotfound.spring.security.email;

import com.memorynotfound.spring.security.model.Idea;
import com.memorynotfound.spring.security.model.Person;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author Mikkel
 */
public class Email {

    // Her skal du ændre email adressen
    final String username = "tryllemikkel@gmail.com";

    // Her skal du ændre passwordet, som passer til ovenstående e-mail adresse
    final String password = "dcc59vez";

    Properties props = new Properties();

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

    /**
     * Her i constructoren sætter vi flere smtp parametre, bla. at vores mail host er googles gmail server
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
     * Denne metode bliver kaldt når der bliver oprettet en ny person på vores side
     * E-mailen bliver sendt til personen der har oprettet en profil.
     */
    public void emailCreatePerson(Person person){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(person.getEmail()));
            message.setSubject("Din bruger er oprettet");
            message.setText("Hej, du har oprettet en bruger på Idébanken. " +
                    "Du kan logge ind på siden med din email: " + person.getEmail() +
                    " samt det kodeord du har valgt.");
            Transport.send(message);

            System.out.println("Email sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Denne metode bliver brugt når en udvikler på siden ansøger en idé.
     * E-mailen bliver sendt til den person som har oprettet idéen.
     */
    public void emailApplyToIdea(Person developer, String applyMessage, String ideaEmail, Idea idea){
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ideaEmail));
            message.setSubject("En udvikler har ansøgt din idé");
            message.setText("Hej, en udvikler på sitet har ansøgt din idé: " + idea.getIdeaName() +
                            " login på idebanken for at godkende udvikleren. \n " +
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