package com.movetothebit.newholland.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movetothebit.newholland.android.utils.lConstants;
@DatabaseTable
public class ObjetiveItem implements lConstants{

	@DatabaseField(id = true, columnName = ID)		
	public int id;
	@DatabaseField( columnName = VALUE)
	public String value;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
