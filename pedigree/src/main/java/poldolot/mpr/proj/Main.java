package poldolot.mpr.proj;
import java.util.*;
import java.sql.*;

import poldolot.mpr.proj.libs.EasyIn;

public class Main {
	public static void main(String[] args) {
		// sekcja try umożliwia wychwycenie błędów
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			Connection dm = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb","sa","");
			DAO.setConnection(dm);
			// String queryStr = "SELECT * FROM COUNTRY ORDER BY name ASC";
			// Statement stmt = dm.createStatement();
			// ResultSet rs = stmt.executeQuery(queryStr);
			// while (rs.next()) {
			// 	System.out.print(rs.getString("name") + " ");
			// 	System.out.println(rs.getString("code"));
			// }
			// String queryStr = "INSERT INTO TOWAR (opis, koszt, cena) VALUES (?,?,?)";
			// PreparedStatement pstmt = dm.prepareStatement(queryStr);
			// System.out.print("Podaj opis produktu: ");
			// String opis = EasyIn.getString();
			// System.out.print("Podaj koszt produktu: ");
			// Float koszt = EasyIn.getFloat();
			// System.out.print("Podaj cenę produktu: ");
			// Float cena = EasyIn.getFloat();
			// pstmt.setString(1, opis);
			// pstmt.setFloat(2, koszt);
			// pstmt.setFloat(3, cena);
			// int addNum = pstmt.executeUpdate();
			// if (addNum >= 1) {
			// 	EasyIn.pause("\nTowar dodany.\nAby zakończyć naciśnij <Enter>");
			// } else {
			// 	System.out.println("Nie dodano żadnego produktu!");
			// }

			System.out.println("Genealogia koni - projekt z Metod programowania na PJWSTK");

			String userChoice = "";
			String command = "";

			do {
				System.out.println("");
				System.out.print("Mozliwe polecenia: create, read, update, delete, quit - koniec programu\n$ ");
				command = userChoice = EasyIn.getString();

				String help = "";
				if (!userChoice.equals("quit")) {
					switch (userChoice) {
						case "read":
							help = "breeder, country, horse, color, descendants";
							break;
						case "create":
						case "update":
						case "delete":
							help = "breeder, country, horse, color";
							break;
						default:
							userChoice = "error";
							System.out.println("Wybrano bledna komende!");
							break;
					}
					if (!userChoice.equals("error")) {
						System.out.print("Do wyboru: " + help + ", quit - koniec programu\n$ ");
						userChoice = EasyIn.getString();
						switch (userChoice) {
							case "breeder":
							case "country":
							case "horse":
							case "color":
							case "descendants":
								command += userChoice.substring(0,1).toUpperCase() + userChoice.substring(1);
								break;
							default:
								userChoice = "error";
								System.out.println("Wybrano bledna komende!");
								break;
						}
						if (!userChoice.equals("quit") && !userChoice.equals("error")) {
							Commands.Parse(command);
							System.out.println(DAO.getMessage());
							DAO.setMessage("");
						}
					}
				}
			} while (!userChoice.equals("quit"));

		} catch (Exception e) {
			System.out.println("Wystapil blad!");
			e.printStackTrace();
		}
	}
}
