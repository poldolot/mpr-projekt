package poldolot.mpr.proj;
import java.sql.*;
import java.util.Properties;

import poldolot.mpr.proj.libs.EasyIn;

public class Main {
	public static void main(String[] args) {
		try {
			Properties prop = new Properties();
			// zaladowanie konfiguracji
			prop.load(Main.class.getResourceAsStream("/jdbc.properties"));
			Class.forName(prop.getProperty("jdbc.driver"));
			Connection dm = DriverManager.getConnection(prop.getProperty("jdbc.url"),prop.getProperty("jdbc.user"),"");
			DAO.setConnection(dm);

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
							case "quit":
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
		System.out.println("Koniec programu.");
	}
}
