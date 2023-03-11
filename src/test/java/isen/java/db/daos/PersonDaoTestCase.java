package isen.java.db.daos;

import static org.assertj.core.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.extractor.Extractors;
import org.junit.Before;
import org.junit.Test;
import isen.java.db.DataSourceFactory;
import isen.java.db.entities.Person;


public class PersonDaoTestCase {

	private PersonDao personDao = new PersonDao();

	@Before
	public void initDb() throws SQLException {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();

		statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (\r\n"
				+ "idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n"
				+ "lastname VARCHAR(45) NOT NULL,\r\n" + "firstname VARCHAR(45) NOT NULL,\r\n"
				+ "nickname VARCHAR(45) NOT NULL,\r\n" + "phone_number VARCHAR(15) NULL,\r\n"
				+ "address VARCHAR(200) NULL,\r\n" + "email_address VARCHAR(150) NULL,\r\n"
				+ "birth_date DATE NULL);");

		statement.executeUpdate("DELETE FROM person");

		statement.executeUpdate(
				"INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES ('Smith', 'John', 'Johnny', '555-1234', '123 Main St', 'john.smith@email.com', '1990-01-01 12:00:00.000');");

		statement.executeUpdate(
				"INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES ('Garcia', 'Maria', 'Mary', '555-5678', '456 Elm St', 'maria.garcia@email.com', '1995-05-05 12:00:00.000');");

		statement.executeUpdate(
				"INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES ('Kim', 'Soo', 'Sue', NULL, '789 Oak St', 'soo.kim@email.com', '1985-12-31 12:00:00.000');");

		statement.close();
		connection.close();
	}

	@Test
	public void shouldListPeople() {
		// GIVEN
		// WHEN
		List<Person> people = personDao.listPeople();
		// THEN
		assertThat(people).hasSize(3);
		assertThat(people)
				.extracting("lastname", "firstname", "nickname", "phoneNumber", "address",
						"emailAddress", "birthDate")
				.containsOnly(
						tuple("Smith", "John", "Johnny", "555-1234", "123 Main St",
								"john.smith@email.com", LocalDate.of(1990, 01, 01)),
						tuple("Garcia", "Maria", "Mary", "555-5678", "456 Elm St",
								"maria.garcia@email.com", LocalDate.of(1995, 05, 05)),
						tuple("Kim", "Soo", "Sue", null, "789 Oak St", "soo.kim@email.com",
								LocalDate.of(1985, 12, 31)));
	}

	@Test
	public void shouldAddPerson() {
		// GIVEN
		Person person = new Person();
		person.setLastname("Doe");
		person.setFirstname("John");
		person.setNickname("Johnny");
		person.setPhoneNumber("555-1234");
		person.setAddress("123 Main St");
		person.setEmailAddress("john.doe@email.com");
		person.setBirthDate(LocalDate.of(1990, 1, 1));
		// WHEN
		Person addedPerson = personDao.addPerson(person);
		// THEN
		assertThat(addedPerson).isNotNull();
		assertThat(addedPerson.getId()).isNotNull();
		assertThat(addedPerson).extracting("lastname", "firstname", "nickname", "phoneNumber",
				"address", "emailAddress", "birthDate").containsOnly("Doe", "John", "Johnny",
						"555-1234", "123 Main St", "john.doe@email.com", LocalDate.of(1990, 1, 1));
	}

	@Test
	public void shouldUpdatePerson() {
		// GIVEN
		Person person = new Person();
		person.setLastname("Doe");
		person.setFirstname("John");
		person.setNickname("Johnny");
		person.setPhoneNumber("555-1234");
		person.setAddress("123 Main St");
		person.setEmailAddress("john.doe@email.com");
		person.setBirthDate(LocalDate.of(1990, 1, 1));
		Person addedPerson = personDao.addPerson(person);

		// Update the person's information
		addedPerson.setLastname("Doe Jr.");
		addedPerson.setNickname("John");
		addedPerson.setPhoneNumber("555-5678");
		addedPerson.setAddress("456 Oak St");

		// WHEN
		Boolean success = personDao.updatePerson(addedPerson);

		// THEN
		assertThat(success).isTrue();

		// TODO: Uncomment if necessary
		// Verify that the updated person has the correct values in the database
		// Person updatedPerson = personDao.getPersonById(addedPerson.getId());
		// assertThat(updatedPerson).isNotNull();
		// assertThat(updatedPerson)
		// .extracting("lastname", "firstname", "nickname", "phoneNumber", "address",
		// "emailAddress", "birthDate")
		// .containsOnly("Doe", "John", "Johnny", "555-4321", "456 Second St",
		// "john.doe@email.com",
		// LocalDate.of(1990, 1, 1));
	}

	@Test
	public void shouldDeletePerson() {
		// GIVEN
		Person person = new Person();
		person.setLastname("Doe");
		person.setFirstname("John");
		person.setNickname("Johnny");
		person.setPhoneNumber("555-1234");
		person.setAddress("123 Main St");
		person.setEmailAddress("john.doe@email.com");
		person.setBirthDate(LocalDate.of(1990, 1, 1));

		// Add the person to the database so we can delete it
		Person addedPerson = personDao.addPerson(person);

		// WHEN
		Person deletedPerson = personDao.deletePerson(addedPerson.getId());

		// THEN
		assertThat(deletedPerson).isNotNull();
		assertThat(deletedPerson)
				.extracting("id", "lastname", "firstname", "nickname", "phoneNumber", "address",
						"emailAddress", "birthDate")
				.containsExactlyInAnyOrderElementsOf(
						Extractors
								.byName("id", "lastname", "firstname", "nickname", "phoneNumber",
										"address", "emailAddress", "birthDate")
								.apply(addedPerson).toList());

		// Verify that the deleted person is no longer in the database
		// Person deletedPerson = personDao.getPersonById(addedPerson.getId());
		// assertThat(deletedPerson).isNull();
	}
}
