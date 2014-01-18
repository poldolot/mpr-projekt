package poldolot.mpr.proj.pedigree;

public class Country {

	private long id;
	private String name;
	private String code;

	public Country(long id, String name, String code) {
		if (name == null) {
			throw new IllegalArgumentException("Country name cannot be null");
		}
		name = name.trim();
		if (name.length() == 0) {
			throw new IllegalArgumentException("Country name cannot be empty");
		}
		if (code == null) {
			throw new IllegalArgumentException("Country code cannot be null");
		}
		code = code.trim();
		if (code.length() != 2) {
			throw new IllegalArgumentException("Country code should use exactly two characters");
		}
		this.id = id;
		this.name = name;
		this.code = code;
	}

	@Override
	public String toString() {
		return String.format("%5d %-30s %2s", this.id, this.name, this.code);
	}

	@Override
	public boolean equals(Object aThat) {
		if ( this == aThat ) return true;
		if (!(aThat instanceof Country)) return false;
		Country that = (Country)aThat;
		return
			this.name.equals(that.name) &&
			this.code.equals(that.code);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + name.hashCode();
		hash = hash * 31 + code.hashCode();
		return hash;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

