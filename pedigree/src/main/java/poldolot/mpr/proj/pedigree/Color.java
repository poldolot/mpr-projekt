package poldolot.mpr.proj.pedigree;

public class Color {

	private long id;
	private String lname;
	private String sname;

	public Color(long id, String lname, String sname) {
		this.id = id;
		this.lname = lname;
		this.sname = sname;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getLname() {
		return lname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSname() {
		return sname;
	}

	@Override
	public String toString() {
		return String.format("%5d %-16s %-8s", this.id, this.lname, this.sname);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + lname.hashCode();
		hash = hash * 31 + sname.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object aThat) {
		if ( this == aThat ) return true;
		if (!(aThat instanceof Color)) return false;
		Color that = (Color)aThat;
		return
			this.lname.equals(that.lname) &&
			this.sname.equals(that.sname);
	}
}
