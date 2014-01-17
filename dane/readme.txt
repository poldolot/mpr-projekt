Kilka słów na temat projektu. W załącznikach znajdują się:

	a) struktura bazy – plik horses-01.sql
	b) więzy i podstawowe dane – plik horses-02.sql
	c) szkielet kodu klas (w zasadzie przedstawiany na ostatnich zajęciach) – plik szkielet.zip


Poniżej „wytyczne”, czyli co jest do zrobienia:

1. CRUD obsługujący "bazę rodowodową" (wszystkie występujące w niej tabele).

2. Oprócz operacji CRUD należy zaimplementować:

	a) wuszukiwanie rodowodu konia o zadanej "głębokości" (liczbie pokoleń wstecz)
	b) wyszukiwanie potomstwa danego konia
	c) generowanie rodowodu w postaci "drzewa" (format PDF, np. z użyciem iText)
	d) interfejs użytkownika pozwalający na wykonywanie wymienionych wyżej operacji
	   (jeśli ktoś bardzo chce, może wykorzystać tryb graficzny, ale sugeruję tryb znakowy)

3. Narzędzia, z których trzeba skorzystać:

	a) kolekcje języka Java - do reprezentowania wyników operacji "masowych"
	a) Maven – do konfiguracji całości projektu
	b) jUnit do testowania operacji CRUD (i nie tylko)

4. Na naszym kolejnym spotkaniu (5 stycznia) podam zestaw "testów akceptacyjnych”. Podczas
   sprawdzania projektów testy te będą uruchamiane na przygotowanych przez mnie przykładowych
   danych do tabel BREEDER, HORSE oraz COUNTRY.
