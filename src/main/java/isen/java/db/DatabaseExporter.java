package isen.java.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import isen.java.db.daos.PersonDao;
import isen.java.db.entities.Person;

// TODO: may not be the best structure, check again later
public class DatabaseExporter {

    private static PersonDao personDao = new PersonDao();

    public static void exportDatabase(Path directoryPath) {

        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Person person : personDao.listPeople()) {

            String vcardData = createVCardString(person);

            String filename = "person_" + Integer.toString(person.getId()) + ".vcf";

            Path filePath = directoryPath.resolve(filename);

            try (PrintWriter writer = new PrintWriter(filePath.toFile())) {
                writer.write(vcardData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static String createVCardString(Person person) {

        String lastName = person.getLastname() != null ? person.getLastname() : "";
        String firstName = person.getFirstname() != null ? person.getFirstname() : "";
        String nickname = person.getNickname() != null ? person.getNickname() : "";
        String phoneNumber = person.getPhoneNumber() != null ? person.getPhoneNumber() : "";
        String address = person.getAddress() != null ? person.getAddress() : "";
        String emailAddress = person.getEmailAddress() != null ? person.getEmailAddress() : "";
        String birthDate = person.getBirthDate() != null
                ? person.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                : "";
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("BEGIN:VCARD\r\n");
        sb.append("VERSION:3.0\r\n");
        sb.append("N:").append(lastName).append(";").append(firstName).append(";;;\r\n");
        sb.append("FN:").append(firstName).append(" ").append(lastName).append("\r\n");
        sb.append("NICKNAME:").append(nickname).append("\r\n");
        sb.append("TEL;TYPE=HOME,VOICE:").append(phoneNumber).append("\r\n");
        sb.append("ADR;TYPE=HOME:").append(address).append("\r\n");
        sb.append("EMAIL:").append(emailAddress).append("\r\n");
        sb.append("BDAY:").append(birthDate).append("\r\n");
        sb.append("END:VCARD\r\n");
        
        return sb.toString();
    }
    
}
