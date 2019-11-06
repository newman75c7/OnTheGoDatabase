package onthego.database.core.sqlprocessor.value;

import java.text.ParseException;

import onthego.database.core.table.Cursor;
import onthego.database.core.table.meta.ColumnType;
import onthego.database.core.table.meta.TypeConstants;

public final class IdValue extends Value {
	
	private String tableName;
	
	private String columnName;
	
	public IdValue(String tableName, String columnName) {
		super(Value.Type.ID);
		this.tableName = tableName;
		this.columnName = columnName;
	}
	
	public Value getValue(Cursor[] tables) throws ParseException {
		Cursor selectCursor = null;
		String columnValue = null; 
		ColumnType columnType = null;
		
		if (tableName == null) {
			selectCursor = tables[0];
		} else {
			for (Cursor cursor : tables) {
				if (tableName.equalsIgnoreCase(cursor.getTableName())) {
					selectCursor = cursor;
					break;
				}
			}
		}
		
		columnValue = selectCursor.getColumn(columnName);
		columnType = selectCursor.getColumnType(columnName);
		
		switch (columnType.getType().getTypeConstant().getName()) {
		case "CHAR":
		case "VARCHAR":
			return new StringValue(columnValue);
		case "INTEGER":
		case "NUMERIC":
			return new NumberValue(columnValue);
		case "BOOL":
			return new BooleanValue(columnValue);
		}
		
		return new NullValue();
	}
	
	public String toString(Cursor[] cursor) throws ParseException {
		return getValue(cursor).toString();
	}
}
