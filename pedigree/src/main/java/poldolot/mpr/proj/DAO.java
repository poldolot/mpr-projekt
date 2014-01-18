package poldolot.mpr.proj;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

import poldolot.mpr.proj.libs.EasyIn;
import poldolot.mpr.proj.pedigree.*;

public class DAO {
	private static Connection dm;
	private static String message = "";

	public static void setConnection(Connection con) {
		dm = con;
	}

	public static boolean createColor(Color color) {
		if (ifColorExist(color.getLname())) {
			setMessage("Kolor juz istnieje w bazie.");
			return false;
		}
		String insertTableSQL = "INSERT INTO COLOR (LNAME, SNAME) VALUES (?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, color.getLname());
			preparedStatement.setString(2, color.getSname());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static List readColors() {
		String query = "SELECT * FROM COLOR ORDER BY lname ASC";
		try {
			List<Color> colors = new LinkedList<Color>();
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				colors.add(new Color(rs.getLong("ID"), rs.getString("LNAME"), rs.getString("SNAME")));
			}
			return colors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Color readColorByName(String lname) {
		String query = "SELECT * FROM COLOR WHERE LNAME = '" + lname + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return new Color(rs.getLong("ID"), rs.getString("LNAME"), rs.getString("SNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Color readColorById(Integer id) {
		String query = "SELECT * FROM COLOR WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return new Color(rs.getLong("ID"), rs.getString("LNAME"), rs.getString("SNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean updateColor(Color color) {
		if (ifColorExist(color.getLname())) {
			setMessage("Kolor juz istnieje w bazie.");
			return false;
		}
		String query = "UPDATE COLOR SET LNAME = ?, SNAME = ? WHERE ID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(query);
			preparedStatement.setString(1, color.getLname());
			preparedStatement.setString(2, color.getSname());
			preparedStatement.setFloat(3, color.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean deleteColor(Color color) {
		String query = "DELETE FROM COLOR WHERE ID = " + color.getId();
		String checkQuery = "SELECT ID FROM HORSE WHERE COLOR = " + color.getId();
		try {
			Statement checkStatement = dm.createStatement();
			ResultSet rs = checkStatement.executeQuery(checkQuery);
			while (rs.next()) {
				DAO.setMessage("Kolor jest uzywany i nie mozna go usunac.");
				return false;
			}
			updateTableBeforeDelete("HORSE", "COLOR", color.getId());
			Statement statement = dm.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean ifColorExist(String lname) {
		String query = "SELECT * FROM COLOR WHERE LNAME = '" + lname + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean createCountry(Country country) {
		if (ifCountryExist(country.getName())) {
			setMessage("Kraj juz istnieje w bazie.");
			return false;
		}
		String insertTableSQL = "INSERT INTO COUNTRY (NAME, CODE) VALUES (?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, country.getName().toUpperCase());
			preparedStatement.setString(2, country.getCode().toUpperCase());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static List readCountries() {
		String query = "SELECT * FROM COUNTRY ORDER BY name ASC";
		try {
			List<Country> countries = new LinkedList<Country>();
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				countries.add(new Country(rs.getLong("ID"), rs.getString("NAME"), rs.getString("CODE")));
			}
			return countries;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Country readCountryByName(String name) {
		String query = "SELECT * FROM COUNTRY WHERE NAME = '" + name.toUpperCase() + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return new Country(rs.getLong("ID"), rs.getString("NAME"), rs.getString("CODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static Country readCountryById(Integer id) {
		String query = "SELECT * FROM COUNTRY WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return new Country(rs.getLong("ID"), rs.getString("NAME"), rs.getString("CODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean updateCountry(Country country) {
		if (ifCountryExist(country.getName())) {
			setMessage("Kraj juz istnieje w bazie.");
			return false;
		}
		String query = "UPDATE COUNTRY SET NAME = ?, CODE = ? WHERE ID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(query);
			preparedStatement.setString(1, country.getName().toUpperCase());
			preparedStatement.setString(2, country.getCode().toUpperCase());
			preparedStatement.setFloat(3, country.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean deleteCountry(Country country) {
		String query = "DELETE FROM COUNTRY WHERE ID = " + country.getId();
		try {
			updateTableBeforeDelete("BREEDER", "COUNTRY", country.getId());
			Statement statement = dm.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean ifCountryExist(String name) {
		String query = "SELECT * FROM COUNTRY WHERE NAME = '" + name.toUpperCase() + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean createBreeder(Breeder breeder) {
		if (ifBreederExist(breeder.getName())) {
			setMessage("Hodowca juz istnieje w bazie.");
			return false;
		}
		String insertTableSQL = "INSERT INTO BREEDER (NAME, COUNTRY) VALUES (?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, breeder.getName());
			preparedStatement.setFloat(2, breeder.getCountry().getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static List readBreeders() {
		String query = "SELECT * FROM BREEDER ORDER BY name ASC";
		try {
			List<Breeder> breeders = new LinkedList<Breeder>();
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Country country = readCountryById(rs.getInt("COUNTRY"));
				breeders.add(new Breeder(rs.getLong("ID"), rs.getString("NAME"), country));
			}
			return breeders;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Breeder readBreederByName(String name) {
		String query = "SELECT * FROM BREEDER WHERE NAME = '" + name + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Country country = readCountryById(rs.getInt("COUNTRY"));
				Breeder b = new Breeder(rs.getLong("ID"), rs.getString("NAME"), country);
				return b;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Breeder readBreederById(Integer id) {
		String query = "SELECT * FROM BREEDER WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Country country = readCountryById(rs.getInt("COUNTRY"));
				return new Breeder(rs.getLong("ID"), rs.getString("NAME"), country);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean updateBreeder(Breeder breeder) {
		if (ifBreederExist(breeder.getName())) {
			setMessage("Hodowca juz istnieje w bazie.");
			return false;
		}
		String query = "UPDATE BREEDER SET NAME = ?, COUNTRY = ? WHERE ID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(query);
			preparedStatement.setString(1, breeder.getName());
			preparedStatement.setFloat(2, breeder.getCountry().getId());
			preparedStatement.setFloat(3, breeder.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean deleteBreeder(Breeder breeder) {
		String query = "DELETE FROM BREEDER WHERE ID = " + breeder.getId();
		try {
			updateTableBeforeDelete("HORSE", "BREEDER", breeder.getId());
			Statement statement = dm.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean ifBreederExist(String name) {
		String query = "SELECT * FROM BREEDER WHERE NAME = '" + name + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean createHorse(Horse horse) {
		if (ifHorseExist(horse)) {
			setMessage("Kon juz istnieje w bazie.");
			return false;
		}
		String insertTableSQL = "INSERT INTO HORSE (NAME, SEX, COLOR, DOB, YEARONLY, DAM, SIRE, BREEDER) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, horse.getName());
			preparedStatement.setInt(2, readSexId(horse.getSex().toString()));
			preparedStatement.setFloat(3, horse.getColor().getId());
			preparedStatement.setDate(4, new java.sql.Date(horse.getDob().getDate().getTime()));
			preparedStatement.setBoolean(5, horse.getDob().getYearOnly());
			if (horse.getDam() == null) {
					preparedStatement.setNull(6, Types.INTEGER);
			} else {
					preparedStatement.setLong(6, horse.getDam().getId());
			}
			if (horse.getSire() == null) {
					preparedStatement.setNull(7, Types.INTEGER);
			} else {
					preparedStatement.setLong(7, horse.getSire().getId());
			}
			if (horse.getBreeder() == null) {
					preparedStatement.setNull(8, Types.INTEGER);
			} else {
					preparedStatement.setLong(8, horse.getBreeder().getId());
			}
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static List readHorses() {
		String query = "SELECT * FROM HORSE ORDER BY name ASC";
		try {
			List<Horse> horses = new LinkedList<Horse>();
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Horse horse = readHorseById(rs.getInt("ID"));
				horses.add(horse);
			}
			return horses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Horse readHorseById(Integer id) {
		String query = "SELECT * FROM HORSE WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Horse dam;
				int dId = rs.getInt("DAM");
				dam = rs.wasNull() ? null : readHorseById(dId);
				Horse sire;
				int sId = rs.getInt("SIRE");
				sire = rs.wasNull() ? null : readHorseById(sId);
				Breeder breeder;
				int bId = rs.getInt("BREEDER");
				breeder = rs.wasNull() ? null : readBreederById(bId);
				return new Horse(rs.getLong("ID"), rs.getString("NAME"), readSexById(rs.getInt("SEX")), new DateOfBirth(rs.getDate("DOB"), rs.getBoolean("YEARONLY")), readColorById(rs.getInt("COLOR")), sire, dam, breeder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean updateHorse(Horse horse) {
		if (ifHorseExist(horse)) {
			setMessage("Kon juz istnieje w bazie.");
			return false;
		}
		String query = "UPDATE HORSE SET NAME = ?, SEX = ?, COLOR = ?, DOB = ?, YEARONLY = ?, DAM = ?, SIRE = ?, BREEDER = ? WHERE ID = ?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = dm.prepareStatement(query);
			preparedStatement.setString(1, horse.getName());
			preparedStatement.setInt(2, readSexId(horse.getSex().toString()));
			preparedStatement.setFloat(3, horse.getColor().getId());
			preparedStatement.setDate(4, new java.sql.Date(horse.getDob().getDate().getTime()));
			preparedStatement.setBoolean(5, horse.getDob().getYearOnly());
			if (horse.getDam() == null) {
					preparedStatement.setNull(6, Types.INTEGER);
			} else {
					preparedStatement.setLong(6, horse.getDam().getId());
			}
			if (horse.getSire() == null) {
					preparedStatement.setNull(7, Types.INTEGER);
			} else {
					preparedStatement.setLong(7, horse.getSire().getId());
			}
			if (horse.getBreeder() == null) {
					preparedStatement.setNull(8, Types.INTEGER);
			} else {
					preparedStatement.setLong(8, horse.getBreeder().getId());
			}
			preparedStatement.setLong(9, horse.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean deleteHorse(Horse horse) {
		try {
			if (!horse.getSex().toString().equals("GELDING")) {
				String sex = horse.getSex().toString().equals("STALLION") ? "SIRE" : "DAM";
				updateTableBeforeDelete("HORSE", sex, horse.getId());
			}

			String query = "DELETE FROM HORSE WHERE ID = " + horse.getId();
			Statement statement = dm.createStatement();
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean ifHorseExist(Horse horse) {
		// TODO dokladniejsze sprawdzanie czy kon istnieje w bazie
		String query = "SELECT * FROM HORSE WHERE NAME = '" + horse.getName() + "'";
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean ifHorseIsSire(Integer id) {
		String query = "SELECT SEX FROM HORSE WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (readSexById(rs.getInt("SEX")).toString().equals("STALLION")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean ifHorseIsDam(Integer id) {
		String query = "SELECT SEX FROM HORSE WHERE ID = " + id;
		try {
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				if (readSexById(rs.getInt("SEX")).toString().equals("MARE")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static List readDescendants(Horse horse) {
		if (horse.getSex().toString().equals("GELDING")) {
			setMessage("To jest walach, nie ma potomstwa!");
			return null;
		}
		String sex = horse.getSex().toString().equals("STALLION") ? "SIRE" : "DAM";
		String query = "SELECT * FROM HORSE WHERE "+sex+" = "+horse.getId();
		try {
			List<Horse> horses = new LinkedList<Horse>();
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Horse dHorse = readHorseById(rs.getInt("ID"));
				horses.add(dHorse);
			}
			return horses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void updateTableBeforeDelete(String table, String column, long id) {
		String query = "UPDATE " + table + " SET " + column + " = ? WHERE " + column + " = ?";
		try {
			PreparedStatement preparedStatement;
			preparedStatement = dm.prepareStatement(query);
			preparedStatement.setNull(1, Types.INTEGER);
			preparedStatement.setFloat(2, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static int readSexId(String name) {
		try {
			String query = "SELECT ID FROM SEX WHERE NAME = '" + name + "'";
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	private static Sex readSexById(Integer id) {
		try {
			String query = "SELECT * FROM SEX WHERE ID = " + id;
			Statement statement = dm.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				return Sex.valueOf(rs.getString("NAME").toUpperCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		DAO.message = message;
	}
}