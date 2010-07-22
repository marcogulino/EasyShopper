package com.google.code.easyshopper.db.domaincreators;

import android.database.Cursor;

public interface DomainObjectCreator<T> {
	public T create(Cursor cursor);

}
