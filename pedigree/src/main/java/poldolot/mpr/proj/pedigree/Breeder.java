package poldolot.mpr.proj.pedigree;

public class Breeder {

	private long id;
	private String name;
	private Country country;

	public Breeder(long id, String name, Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return String.format("%5d %s %2s", this.id, this.name, this.country.getCode());
	}

	@Override
	public boolean equals(Object aThat) {
		if ( this == aThat ) return true;
		if (!(aThat instanceof Breeder)) return false;
		Breeder that = (Breeder)aThat;
		return
			this.name.equals(that.name) &&
			this.country.equals(that.country);
	}

}
