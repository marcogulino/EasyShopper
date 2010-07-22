package com.gmail.gulino.marco.easyshopper.db.helpers;


import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class MyCursor implements Cursor {

	private final Cursor cursor;

	public MyCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public void close() {
		cursor.close();
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		cursor.copyStringToBuffer(columnIndex, buffer);
	}

	public void deactivate() {
		cursor.deactivate();
	}

	public byte[] getBlob(int columnIndex) {
		return cursor.getBlob(columnIndex);
	}

	public int getColumnCount() {
		return cursor.getColumnCount();
	}

	public int getColumnIndex(String columnName) {
		return cursor.getColumnIndex(columnName);
	}

	public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
		return cursor.getColumnIndexOrThrow(columnName);
	}

	public String getColumnName(int columnIndex) {
		return cursor.getColumnName(columnIndex);
	}

	public String[] getColumnNames() {
		return cursor.getColumnNames();
	}

	public int getCount() {
		return cursor.getCount();
	}

	public double getDouble(int columnIndex) {
		return cursor.getDouble(columnIndex);
	}

	public Bundle getExtras() {
		return cursor.getExtras();
	}

	public float getFloat(int columnIndex) {
		return cursor.getFloat(columnIndex);
	}

	public int getInt(int columnIndex) {
		return cursor.getInt(columnIndex);
	}

	public long getLong(int columnIndex) {
		return cursor.getLong(columnIndex);
	}

	public int getPosition() {
		return cursor.getPosition();
	}

	public short getShort(int columnIndex) {
		return cursor.getShort(columnIndex);
	}

	public String getString(int columnIndex) {
		return cursor.getString(columnIndex);
	}

	public boolean getWantsAllOnMoveCalls() {
		return cursor.getWantsAllOnMoveCalls();
	}

	public boolean isAfterLast() {
		return cursor.isAfterLast();
	}

	public boolean isBeforeFirst() {
		return cursor.isBeforeFirst();
	}

	public boolean isClosed() {
		return cursor.isClosed();
	}

	public boolean isFirst() {
		return cursor.isFirst();
	}

	public boolean isLast() {
		return cursor.isLast();
	}

	public boolean isNull(int columnIndex) {
		return cursor.isNull(columnIndex);
	}

	public boolean move(int offset) {
		return cursor.move(offset);
	}

	public boolean moveToFirst() {
		return cursor.moveToFirst();
	}

	public boolean moveToLast() {
		return cursor.moveToLast();
	}

	public boolean moveToNext() {
		return cursor.moveToNext();
	}

	public boolean moveToPosition(int position) {
		return cursor.moveToPosition(position);
	}

	public boolean moveToPrevious() {
		return cursor.moveToPrevious();
	}

	public void registerContentObserver(ContentObserver observer) {
		cursor.registerContentObserver(observer);
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		cursor.registerDataSetObserver(observer);
	}

	public boolean requery() {
		return cursor.requery();
	}

	public Bundle respond(Bundle extras) {
		return cursor.respond(extras);
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		cursor.setNotificationUri(cr, uri);
	}

	public void unregisterContentObserver(ContentObserver observer) {
		cursor.unregisterContentObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		cursor.unregisterDataSetObserver(observer);
	}

	public long getLong(QueryColumn column) {
		return getLong(findColumnIndex(column));
	}

	public String getString(QueryColumn column) {
		return getString(findColumnIndex(column));
	}

	public int getInt(QueryColumn column) {
		return getInt(findColumnIndex(column));
	}
	
	private int findColumnIndex(QueryColumn column) {
		int columnCount = cursor.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			String columnName = cursor.getColumnName(i);
			if(columnName.equals(column.fullName()) || columnName.equals(column.name())) {
				return i;
			}
		}
		throw new RuntimeException("could not find column " + column.toString());
	}

}
