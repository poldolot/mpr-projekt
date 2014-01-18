package poldolot.mpr.proj.pedigree;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DateOfBirth {
	private Date date;
	private Boolean yearOnly;

	public DateOfBirth(Date date, boolean yearOnly) {
		this.date = date;
		this.yearOnly = yearOnly;
	}

	public DateOfBirth() {
	}

	public void setDate(Date date, boolean yearOnly) {
		this.date = date;
		this.yearOnly = yearOnly;
	}

	public Date getDate() {
		return this.date;
	}

	public Boolean getYearOnly() {
		return this.yearOnly;
	}

	@Override
	public String toString() {
		if (yearOnly) {
			return new SimpleDateFormat("yyyy").format(date);
		} else {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
	}
}
