package poldolot.mpr.test;

import static org.junit.Assert.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import poldolot.mpr.proj.DAO;
import poldolot.mpr.proj.pedigree.*;

public class TestDAO {

	Connection db;

	@Before
	public void setUp() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		db = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb","sa","");
		DAO.setConnection(db);
		Statement statement = db.createStatement();
		statement.executeUpdate("CREATE TABLE COLOR ( ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0) PRIMARY KEY, LNAME VARCHAR(16) NOT NULL, SNAME VARCHAR(8) NOT NULL);");
		statement.executeUpdate("CREATE TABLE HORSE ( ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY, NAME VARCHAR(40) NOT NULL, SEX INTEGER NOT NULL, COLOR INTEGER NOT NULL, DOB DATE NOT NULL, YEARONLY BOOLEAN DEFAULT False, DAM INTEGER, SIRE INTEGER, BREEDER INTEGER);");
		statement.executeUpdate("CREATE TABLE BREEDER ( ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY, NAME VARCHAR(120) NOT NULL, COUNTRY INTEGER);");
		statement.executeUpdate("CREATE TABLE COUNTRY ( ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY, NAME VARCHAR(30) NOT NULL, CODE VARCHAR(2) NOT NULL);");
		statement.executeUpdate("CREATE TABLE SEX ( ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 0) PRIMARY KEY, NAME VARCHAR_IGNORECASE(8) );");
		statement.executeUpdate("ALTER TABLE HORSE ADD CONSTRAINT Sex_Ref FOREIGN KEY (SEX) REFERENCES SEX(ID);");
		statement.executeUpdate("ALTER TABLE HORSE ADD CONSTRAINT Color_Ref FOREIGN KEY (COLOR) REFERENCES COLOR(ID);");
		statement.executeUpdate("ALTER TABLE HORSE ADD CONSTRAINT Dam_Ref FOREIGN KEY (DAM) REFERENCES HORSE(ID);");
		statement.executeUpdate("ALTER TABLE HORSE ADD CONSTRAINT Sire_Ref FOREIGN KEY (SIRE) REFERENCES HORSE(ID);");
		statement.executeUpdate("ALTER TABLE HORSE ADD CONSTRAINT Breeder_Ref FOREIGN KEY (BREEDER) REFERENCES BREEDER(ID);");
		statement.executeUpdate("ALTER TABLE SEX ADD CONSTRAINT Sex_Enum CHECK (NAME IN ( 'MARE', 'STALLION', 'GELDING') );");
		statement.executeUpdate("INSERT INTO COLOR(ID, LNAME, SNAME) VALUES (0, 'bay', 'bay'), (1, 'light bay', 'l.bay');");
		statement.executeUpdate("INSERT INTO SEX (NAME) VALUES ('mare'), ('stallion'), ('gelding');");
		statement.executeUpdate("INSERT INTO COUNTRY (name,code) VALUES ('ANGOLA','AO'), ('POLAND','PL');");
		statement.executeUpdate("INSERT INTO BREEDER ( ID, NAME, COUNTRY ) VALUES (0, 'Milky Way', 1)");
		statement.executeUpdate("INSERT INTO HORSE ( ID, NAME, SEX, COLOR, DOB, YEARONLY, DAM, SIRE, BREEDER ) VALUES (0, 'Bold Ruler', 1, 1, '1954-01-01', true, null, null, 0), (1, 'Magneto', 0, 0, '1953-01-01', true, null, null, 0), (2, 'Stupendous', 1, 1, '1963-01-01', true, 1, 0, 0)");
	}

	@After
	public void tearDown() throws Exception {
		Statement statement = db.createStatement();
		statement.executeUpdate("DROP TABLE HORSE");
		statement.executeUpdate("DROP TABLE COLOR");
		statement.executeUpdate("DROP TABLE BREEDER");
		statement.executeUpdate("DROP TABLE COUNTRY");
		statement.executeUpdate("DROP TABLE SEX");
	}

	@Test
	public void testCreateColor() {
		Color color = new Color(-1, "black", "bl");
		boolean res = DAO.createColor(color);
		assertEquals(true, res);
		Color dbColor = DAO.readColorByName("black");
		assertEquals(color, dbColor);
	}

	@Test
	public void testReadColors() {
		List<Color> colors =  new LinkedList<Color>();
		colors.add(new Color(0, "bay", "bay"));
		colors.add(new Color(1, "light bay", "l.bay"));
		List<Color> dbColors = DAO.readColors();
		assertEquals(colors, dbColors);
	}

	@Test
	public void testReadColorByName() {
		Color color = DAO.readColorByName("black");
		assertEquals(null, color);
		color = new Color(0, "bay", "bay");
		Color dbColor = DAO.readColorByName("bay");
		assertEquals(color, dbColor);
		assertNotEquals(null, dbColor);
	}

	@Test
	public void testReadColorById() {
		Color color = new Color(1, "light bay", "l.bay");
		Color dbColor = DAO.readColorById(1);
		assertEquals(color, dbColor);
	}

	@Test
	public void testUpdateColor() {
		Color color = DAO.readColorByName("bay");
		color.setLname("black");
		color.setSname("bl");
		DAO.updateColor(color);
		Color dbColor = DAO.readColorByName("bay");
		assertEquals(null, dbColor);
		dbColor = DAO.readColorByName("black");
		assertEquals(color, dbColor);
	}

	@Test
	public void testDeleteColor() {
		Color color = new Color(2, "black", "bl");
		DAO.createColor(color);
		Color dbColor = DAO.readColorByName("black");
		assertEquals(color, dbColor);
		DAO.deleteColor(color);
		dbColor = DAO.readColorByName("black");
		assertEquals(null, dbColor);
		// nie mozna usunac koloru wykorzystywanego w tabeli HORSE
		color = DAO.readColorByName("bay");
		DAO.deleteColor(color);
		dbColor = DAO.readColorByName("bay");
		assertEquals(color, dbColor);
	}

	@Test
	public void testIfColorExist() {
		boolean res = DAO.ifColorExist("bay");
		assertEquals(true, res);
		res = DAO.ifColorExist("black");
		assertEquals(false, res);
	}

	@Test
	public void testCreateHorse() {
		DateOfBirth dob = new DateOfBirth();
		try {
			dob.setDate(new SimpleDateFormat("yyyy").parse("1970"), true);
		} catch (ParseException e) {
		}
		Color color = new Color(0, "bay", "bay");
		Horse sire = null;
		Horse dam = null;
		Breeder breeder = null;
		Horse horse = new Horse(3, "Zorilla", Sex.valueOf("MARE"), dob, color, sire, dam, breeder);
		boolean res = DAO.createHorse(horse);
		assertEquals(true, res);
		Horse dbHorse = DAO.readHorseById(3);
		assertEquals(horse, dbHorse);
	}

	@Test
	public void testIfHorseIsSire() {
		boolean res = DAO.ifHorseIsSire(0);
		assertEquals(true, res);
		res = DAO.ifHorseIsSire(1);
		assertEquals(false, res);
	}

	@Test
	public void testIfHorseIsDam() {
		boolean res = DAO.ifHorseIsDam(1);
		assertEquals(true, res);
		res = DAO.ifHorseIsDam(0);
		assertEquals(false, res);
	}

	@Ignore
	@Test
	public void testReadHorses() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadHorseById() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testUpdateHorse() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDeleteHorse() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIfHorseExist() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadDescendants() {
		fail("Not yet implemented");
	}

		@Ignore
	@Test
	public void testCreateCountry() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadCountries() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadCountryByName() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testUpdateCountry() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDeleteCountry() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIfCountryExist() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testCreateBreeder() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadBreeders() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadBreederByName() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testReadBreederById() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testUpdateBreeder() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDeleteBreeder() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIfBreederExist() {
		fail("Not yet implemented");
	}

}
