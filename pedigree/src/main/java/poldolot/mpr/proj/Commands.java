package poldolot.mpr.proj;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

import com.itextpdf.text.DocumentException;

import poldolot.mpr.proj.libs.EasyIn;
import poldolot.mpr.proj.pedigree.*;

public class Commands {
	public static void Parse(String command) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// zamiast switcha na kilkanascie pozycji wywolujemy funkcje z otrzymanego stringa
		Method method = Commands.class.getDeclaredMethod(command);
		method.invoke(method, null);
	}

	public static void createHorse() throws ParseException {
		System.out.print("Podaj nazwe konia (max 40 znakow):\n$ ");
		String name = EasyIn.getString();
		System.out.print("Podaj plec (MARE, STALLION, GELDING):\n$ ");
		String sex = EasyIn.getString();
		System.out.print("Podaj date urodzenia konia (yyyy / yyyy-mm-dd):\n$ ");
		String bdate = EasyIn.getString();
		DateOfBirth dob = new DateOfBirth();
		if (bdate.length() == 4) {
			dob.setDate(new SimpleDateFormat("yyyy").parse(bdate), true);
		} else {
			dob.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(bdate), false);
		}
		String cname, id;
		Integer loop = 0;
		do {
			if (loop > 0)
				System.out.print("Bledna nazwa koloru! ");
			System.out.print("Podaj nazwe koloru:\n$ ");
			cname = EasyIn.getString();
			loop++;
		} while (!DAO.ifColorExist(cname));
		loop = 0;
		do {
			if (loop > 0)
				System.out.print("Ojcem moze byc tylko ogier! ");
			System.out.print("Podaj id ojca konia (dozwolony brak):\n$ ");
			id = EasyIn.getString();
			if (id.equals(""))
				break;
			loop++;
		} while (!DAO.ifHorseIsSire(Integer.parseInt(id)));
		Horse sire = id.equals("") ? null : DAO.readHorseById(Integer.parseInt(id));
		loop = 0;
		do {
			if (loop > 0)
				System.out.print("Matka moze byc tylko klacz! ");
			System.out.print("Podaj id matki konia (dozwolony brak):\n$ ");
			id = EasyIn.getString();
			if (id.equals(""))
				break;
			loop++;
		} while (!DAO.ifHorseIsDam(Integer.parseInt(id)));
		Horse dam = id.equals("") ? null : DAO.readHorseById(Integer.parseInt(id));
		String bname;
		loop = 0;
		do {
			if (loop > 0)
				System.out.print("Bledna nazwa hodowcy! ");
			System.out.print("Podaj nazwe hodowcy (dozwolony brak):\n$ ");
			bname = EasyIn.getString();
			if (bname.equals(""))
				break;
			loop++;
		} while (!DAO.ifBreederExist(bname));
		Breeder b = bname.equals("") ? null : DAO.readBreederByName(bname);
		Horse horse = new Horse(0, name.length() > 40 ? name.substring(0, 40) : name, Sex.valueOf(sex.toUpperCase()), dob, DAO.readColorByName(cname), sire, dam, DAO.readBreederByName(bname));
		if (DAO.createHorse(horse)) {
			DAO.setMessage("Kon zostal dodany.");
		}
	}

	public static void readHorse() {
		System.out.print("Podaj ID konia lub wcisnij ENTER aby wyswietlic wszystkie:\n$ ");
		String id = EasyIn.getString();
		if (id.equals("")) {
			List<Horse> horses = DAO.readHorses();
			if (!horses.isEmpty()) {
				System.out.println(String.format("%s\t%-15s\t%-8s\t%-10s\t%s\t%s\t%s\t%s\n", "id", "imie", "plec", "data ur.", "masc", "ojciec", "matka", "hodowca"));
				for (Horse h : horses) {
					System.out.println(h.toString());
				}
			} else {
				DAO.setMessage("Brak danych w bazie.");
			}
		} else {
			Horse horse = DAO.readHorseById(Integer.parseInt(id));
			if (horse != null) {
				System.out.println(String.format("%s\t%-15s\t%-8s\t%-10s\t%s\t%s\t%s\t%s\n", "id", "imie", "plec", "data ur.", "masc", "ojciec", "matka", "hodowca"));
				System.out.println(horse.toString());
				System.out.print("Czy wygenerowac rodowod dla danego konia? (y/n)\n$ ");
				String generate = EasyIn.getString();
				if (generate.equals("Y") || generate.equals("y")) {
					System.out.print("Jakies glebokosci ma byc rodowod?\n$ ");
					Integer deep = EasyIn.getInt();
					try {
						GeneratePDF pdf = new GeneratePDF(Integer.parseInt(id), deep);
						DAO.setMessage("Wygenerowano plik PDF.");
					} catch (Exception e) {
						DAO.setMessage("Blad generowania pliku PDF.");
					}
				}
			} else {
				DAO.setMessage("Podanego konia nie ma w bazie.");
			}
		}
	}

	public static void updateHorse() throws ParseException {
		System.out.print("Podaj ID konia do edycji:\n$ ");
		Integer hId = EasyIn.getInt();
		Horse horse = DAO.readHorseById(hId);
		if (horse != null) {
			System.out.print("Podaj nazwe konia (max 40 znakow):\n$ ");
			String name = EasyIn.getString();
			System.out.print("Podaj plec (MARE, STALLION, GELDING):\n$ ");
			String sex = EasyIn.getString();
			System.out.print("Podaj date urodzenia konia (yyyy / yyyy-mm-dd):\n$ ");
			String bdate = EasyIn.getString();
			DateOfBirth dob = new DateOfBirth();
			if (bdate.length() == 4) {
				dob.setDate(new SimpleDateFormat("yyyy").parse(bdate), true);
			} else {
				dob.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(bdate), false);
			}
			String cname, id;
			Integer loop = 0;
			do {
				if (loop > 0)
					System.out.print("Bledna nazwa koloru! ");
				System.out.print("Podaj nazwe koloru:\n$ ");
				cname = EasyIn.getString();
				loop++;
			} while (!DAO.ifColorExist(cname));
			loop = 0;
			do {
				if (loop > 0)
					System.out.print("Ojcem moze byc tylko ogier! ");
				System.out.print("Podaj id ojca konia (dozwolony brak):\n$ ");
				id = EasyIn.getString();
				if (id.equals(""))
					break;
				loop++;
			} while (!DAO.ifHorseIsSire(Integer.parseInt(id)));
			Horse sire = id.equals("") ? null : DAO.readHorseById(Integer.parseInt(id));
			loop = 0;
			do {
				if (loop > 0)
					System.out.print("Matka moze byc tylko klacz! ");
				System.out.print("Podaj id matki konia (dozwolony brak):\n$ ");
				id = EasyIn.getString();
				if (id.equals(""))
					break;
				loop++;
			} while (!DAO.ifHorseIsDam(Integer.parseInt(id)));
			Horse dam = id.equals("") ? null : DAO.readHorseById(Integer.parseInt(id));
			String bname;
			loop = 0;
			do {
				if (loop > 0)
					System.out.print("Bledna nazwa hodowcy! ");
				System.out.print("Podaj nazwe hodowcy (dozwolony brak):\n$ ");
				bname = EasyIn.getString();
				if (bname.equals(""))
					break;
				loop++;
			} while (!DAO.ifBreederExist(bname));
			Breeder b = bname.equals("") ? null : DAO.readBreederByName(bname);
			horse = new Horse(horse.getId(), name.length() > 40 ? name.substring(0, 40) : name, Sex.valueOf(sex.toUpperCase()), dob, DAO.readColorByName(cname), sire, dam, DAO.readBreederByName(bname));
			if (DAO.updateHorse(horse)) {
				DAO.setMessage("Kon zostal zmodyfikowany.");
			}
		} else {
			DAO.setMessage("Podanego konia nie ma w bazie.");
		}
	}

	public static void deleteHorse() {
		System.out.print("Podaj ID konia:\n$ ");
		Integer id = EasyIn.getInt();
		Horse horse = DAO.readHorseById(id);
		if (horse != null) {
			DAO.deleteHorse(horse);
			DAO.setMessage("Kon zostal usuniety.");
		} else {
			DAO.setMessage("Podanego konia nie ma w bazie.");
		}
	}

	public static void readDescendants() {
		System.out.print("Podaj ID konia\n$ ");
		Integer id = EasyIn.getInt();
		Horse horse = DAO.readHorseById(id);
		if (horse != null) {
			List<Horse> horses = DAO.readDescendants(horse);
			if (!horses.isEmpty()) {
				System.out.println(String.format("%s\t%-15s\t%-8s\t%-10s\t%s\t%s\t%s\t%s\n", "id", "imie", "plec", "data ur.", "masc", "ojciec", "matka", "hodowca"));
				for (Horse h : horses) {
					System.out.println(h.toString());
				}
			} else {
				DAO.setMessage("Podany kon nie ma potomstwa.");
			}
		} else {
			DAO.setMessage("Podanego konia nie ma w bazie.");
		}
	}

	public static void createBreeder() {
		System.out.print("Podaj nazwe hodowcy (max 120 znakow):\n$ ");
		String name = EasyIn.getString();
		String cname;
		Integer loop = 0;
		do {
			if (loop > 0)
				System.out.print("Bledna nazwa kraju! ");
			System.out.print("Podaj nazwe kraju:\n$ ");
			cname = EasyIn.getString();
			loop++;
		} while (!DAO.ifCountryExist(cname));
		Country country = DAO.readCountryByName(cname);
		Breeder breeder = new Breeder(0, name.length() > 120 ? name.substring(0, 120) : name, country);
		if (DAO.createBreeder(breeder)) {
			DAO.setMessage("Hodowca zostal dodany.");
		}
	}

	public static void readBreeder() {
		System.out.print("Podaj nazwe hodowcy lub wcisnij ENTER aby wyswietlic wszystkich:\n$ ");
		String name = EasyIn.getString();
		if (name.equals("")) {
			List<Breeder> breeders = DAO.readBreeders();
			for (Breeder b : breeders) {
				System.out.println(b.toString());
			}
		} else {
			Breeder breeder = DAO.readBreederByName(name);
			if (breeder != null) {
				System.out.println(breeder.toString());
			} else {
				DAO.setMessage("Podanego hodowcy nie ma w bazie.");
			}
		}
	}

	public static void updateBreeder() {
		System.out.print("Podaj nazwe hodowcy do edycji:\n$ ");
		String name = EasyIn.getString();
		Breeder breeder = DAO.readBreederByName(name);
		if (breeder != null) {
			System.out.print("Podaj nowa nazwe hodowcy (max 120 znakow):\n$ ");
			name = EasyIn.getString();
			breeder.setName(name.length() > 120 ? name.substring(0, 120) : name);
			String cname;
			Integer loop = 0;
			do {
				if (loop > 0)
					System.out.print("Bledna nazwa kraju! ");
				System.out.print("Podaj nowa nazwe kraju:\n$ ");
				cname = EasyIn.getString();
				loop++;
			} while (!DAO.ifCountryExist(cname));
			Country country = DAO.readCountryByName(cname);
			breeder.setCountry(country);
			if (DAO.updateBreeder(breeder)) {
				DAO.setMessage("Hodowca zostal zmodyfikowany.");
			}
		} else {
			DAO.setMessage("Podanego hodowcy nie ma w bazie.");
		}
	}

	public static void deleteBreeder() {
		System.out.print("Podaj nazwe hodowcy:\n$ ");
		String name = EasyIn.getString();
		Breeder breeder = DAO.readBreederByName(name);
		if (breeder != null) {
			DAO.deleteBreeder(breeder);
			DAO.setMessage("Hodowca zostal usuniety.");
		} else {
			DAO.setMessage("Podanego hodowcy nie ma w bazie.");
		}
	}

	public static void createColor() {
		System.out.print("Podaj pelna nazwe koloru (max 16 znakow):\n$ ");
		String lname = EasyIn.getString();
		System.out.print("Podaj skrocona nazwe koloru (max 8 znakow):\n$ ");
		String sname = EasyIn.getString();
		Color color = new Color(0, lname.length() > 16 ? lname.substring(0, 16) : lname, sname.length() > 8 ? sname.substring(0, 8) : sname);
		if (DAO.createColor(color)) {
			DAO.setMessage("Kolor zostal dodany.");
		}
	}

	public static void readColor() {
		System.out.print("Podaj pelna nazwe koloru lub wcisnij ENTER aby wyswietlic wszystkie:\n$ ");
		String lname = EasyIn.getString();
		if (lname.equals("")) {
			List<Color> colors = DAO.readColors();
			for (Color c : colors) {
				System.out.println(c.toString());
			}
		} else {
			Color color = DAO.readColorByName(lname);
			if (color != null) {
				System.out.println(color.toString());
			} else {
				DAO.setMessage("Podanego koloru nie ma w bazie.");
			}
		}
	}

	public static void updateColor() {
		System.out.print("Podaj pelna nazwe koloru:\n$ ");
		String lname = EasyIn.getString();
		Color color = DAO.readColorByName(lname);
		if (color != null) {
			System.out.print("Podaj pelna nazwe koloru (max 16 znakow):\n$ ");
			lname = EasyIn.getString();
			color.setLname(lname.length() > 16 ? lname.substring(0, 16) : lname);
			System.out.print("Podaj skrocona nazwe koloru (max 8 znakow):\n$ ");
			String sname = EasyIn.getString();
			color.setSname(sname.length() > 8 ? sname.substring(0, 8) : sname);
			if (DAO.updateColor(color)) {
				DAO.setMessage("Kolor zostal zmodyfikowany.");
			}
		} else {
			DAO.setMessage("Podanego koloru nie ma w bazie.");
		}
	}

	public static void deleteColor() {
		System.out.print("Podaj pelna nazwe koloru:\n$ ");
		String lname = EasyIn.getString();
		Color color = DAO.readColorByName(lname);
		if (color != null) {
			if (DAO.deleteColor(color)) {
				DAO.setMessage("Kolor zostal usuniety.");
			}
		} else {
			DAO.setMessage("Podanego koloru nie ma w bazie.");
		}
	}

	public static void createCountry() {
		System.out.print("Podaj nazwe kraju (max 30 znakow):\n$ ");
		String name = EasyIn.getString();
		System.out.print("Podaj kod kraju (2 znaki):\n$ ");
		String code = EasyIn.getString();
		Country country = new Country(0, name.length() > 30 ? name.substring(0, 30) : name, code.length() > 2 ? code.substring(0, 2) : code);
		if (DAO.createCountry(country)) {
			DAO.setMessage("Kraj zostal dodany.");
		}
	}

	public static void readCountry() {
		System.out.print("Podaj nazwe kraju lub wcisnij ENTER aby wyswietlic wszystkie:\n$ ");
		String name = EasyIn.getString();
		if (name.equals("")) {
			List<Country> countries = DAO.readCountries();
			for (Country c : countries) {
				System.out.println(c.toString());
			}
		} else {
			Country country = DAO.readCountryByName(name);
			if (country != null) {
				System.out.println(country.toString());
			} else {
				DAO.setMessage("Podanego kraju nie ma w bazie.");
			}
		}
	}

	public static void updateCountry() {
		System.out.print("Podaj nazwe kraju:\n$ ");
		String name = EasyIn.getString();
		Country country = DAO.readCountryByName(name);
		if (country != null) {
			System.out.print("Podaj nazwe kraju (max 30 znakow):\n$ ");
			name = EasyIn.getString();
			country.setName(name.length() > 30 ? name.substring(0, 30) : name);
			System.out.print("Podaj kod kraju (2 znaki):\n$ ");
			String code = EasyIn.getString();
			country.setCode(code.length() > 2 ? code.substring(0, 2) : code);
			if (DAO.updateCountry(country)) {
				DAO.setMessage("Kraj zostal zmodyfikowany.");
			}
		} else {
			DAO.setMessage("Podanego kraju nie ma w bazie.");
		}
	}

	public static void deleteCountry() {
		System.out.print("Podaj nazwe kraju:\n$ ");
		String name = EasyIn.getString();
		Country country = DAO.readCountryByName(name);
		if (country != null) {
			DAO.deleteCountry(country);
			DAO.setMessage("Kraj zostal usuniety.");
		} else {
			DAO.setMessage("Podanego kraju nie ma w bazie.");
		}
	}
}