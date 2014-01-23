package poldolot.mpr.proj.pedigree;

public class Horse {

	private long id;
	private String name;
	private Sex sex;
	private DateOfBirth dob;
	private Color color;
	private Horse sire;
	private Horse dam;
	private Breeder breeder;

	public Horse(long id, String name, Sex sex, DateOfBirth dob, Color color, Horse sire, Horse dam, Breeder breeder) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.dob = dob;
		this.color = color;
		this.sire = sire;
		this.dam = dam;
		this.breeder = breeder;
	}

	public Horse() {
		this.id = -1;
		this.name = null;
		this.sex = null;
		this.dob = null;
		this.color = null;
		this.sire = null;
		this.dam = null;
		this.breeder = null;
	}

	@Override
	public String toString() {
		return String.format("%d\t%-15s\t%-8s\t%-10s\t%s\t%s\t%s\t%s", this.id, this.name, this.sex.toString(), this.dob.toString(), this.color.getSname(),
			this.sire != null ? this.sire.getName() : "---", this.dam != null ? this.dam.getName() : "---", this.breeder != null ? this.breeder.getName() + " (" + this.breeder.getCountry().getCode() + ")" : "---");
	}

	@Override
	public boolean equals(Object aThat) {
		if ( this == aThat ) return true;
		if (!(aThat instanceof Horse)) return false;
		Horse that = (Horse)aThat;
		return
			this.name.equals(that.name) &&
			this.sex.equals(that.sex) &&
			this.dob.equals(that.dob) &&
			this.color.equals(that.color) &&
			this.sire != null ? (that.sire == null ? false : this.sire.equals(that.sire)) : (that.sire == null ? true : false) &&
			this.dam != null ? (that.dam == null ? false : this.dam.equals(that.dam)) : (that.dam == null ? true : false) &&
			this.breeder != null ? (that.breeder == null ? false : this.breeder.equals(that.breeder)) : (that.breeder == null ? true : false);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + name.hashCode();
		hash = hash * 31 + dob.hashCode();
		hash = hash + 47 * color.hashCode();
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

	public void setSex(Sex sex) {
			this.sex = sex;
	}

	public Sex getSex() {
			return sex;
	}

	public void setDob(DateOfBirth dob) {
			this.dob = dob;
	}

	public DateOfBirth getDob() {
			return dob;
	}

	public void setColor(Color color) {
			this.color = color;
	}

	public Color getColor() {
			return color;
	}

	public void setSire(Horse sire) {
			this.sire = sire;
	}

	public Horse getSire() {
			return sire;
	}

	public void setDam(Horse dam) {
			this.dam = dam;
	}

	public Horse getDam() {
			return dam;
	}

	public void setBreeder(Breeder breeder) {
			this.breeder = breeder;
	}

	public Breeder getBreeder() {
			return breeder;
	}
}
