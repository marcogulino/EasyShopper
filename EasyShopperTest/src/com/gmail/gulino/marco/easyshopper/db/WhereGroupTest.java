package com.gmail.gulino.marco.easyshopper.db;

import static com.gmail.gulino.marco.easyshopper.utility.Asserts.assertArrayEquals;
import android.test.AndroidTestCase;

import com.google.code.easyshopper.db.helpers.QueryColumn;
import com.google.code.easyshopper.db.helpers.Where;
import com.google.code.easyshopper.db.helpers.WhereGroup;
public class WhereGroupTest extends AndroidTestCase {
	
	private Where firstCondition;
	private Where secondCondition;
	private Where thirdCondition;

	public void setUp() {
		firstCondition=new Where(new FullNameOnlyColumn("first column"),  "first value");
		secondCondition=new Where(new FullNameOnlyColumn("second column"), "second value");
		thirdCondition=new Where(new FullNameOnlyColumn("third column"), "third value");
	}

	public void testWithFirstConditionOnly() {
		WhereGroup whereCondition = new WhereGroup(firstCondition);
		assertEquals("( first column = ? )", whereCondition.selection());
		assertArrayEquals(new String[] {"first value"}, whereCondition.values());
	}
	
	public void testAndConcatenation() {
		WhereGroup whereCondition = new WhereGroup(firstCondition);
		whereCondition.and(secondCondition);
		assertEquals("( first column = ? AND second column = ? )", whereCondition.selection());
		assertArrayEquals(new String[] {"first value", "second value"}, whereCondition.values());
	}
	
	public void testOrConcatenation() {
		WhereGroup whereCondition = new WhereGroup(firstCondition);
		whereCondition.or(thirdCondition);
		assertEquals("( first column = ? OR third column = ? )", whereCondition.selection());
		assertArrayEquals(new String[] {"first value", "third value"}, whereCondition.values());
	}
	
	public void testMultipleConcatenation() {
		WhereGroup whereCondition = new WhereGroup(firstCondition);
		whereCondition.and(secondCondition);
		whereCondition.or(thirdCondition);
		assertEquals("( first column = ? AND second column = ? OR third column = ? )", whereCondition.selection());
		assertArrayEquals(new String[] {"first value", "second value", "third value"}, whereCondition.values());
	}
	
	public void testConcatenationWithMultipleGroups() {
		WhereGroup partialGroup = new WhereGroup(firstCondition);
		partialGroup.or(secondCondition);
		WhereGroup whereGroup = new WhereGroup(partialGroup);
		whereGroup.and(thirdCondition);
		assertEquals("( ( first column = ? OR second column = ? ) AND third column = ? )", whereGroup.selection());
		assertArrayEquals(new String[] {"first value", "second value", "third value"}, whereGroup.values());
	}
	
	private class FullNameOnlyColumn implements QueryColumn {
		private final String fullname;

		public FullNameOnlyColumn(String fullname) {
			this.fullname = fullname;
		}
		
		public String fullName() {
			return fullname;
		}

		public String name() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
