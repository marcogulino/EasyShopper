package com.gmail.gulino.marco.easyshopper.db.domaincreators;

import android.database.Cursor;

public interface DomainObjectCreator<T> {
	public T create(Cursor cursor);

}
