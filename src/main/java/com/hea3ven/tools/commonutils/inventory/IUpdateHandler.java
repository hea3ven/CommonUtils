package com.hea3ven.tools.commonutils.inventory;

public interface IUpdateHandler {
	int getFieldCount();

	void setField(int id, int data);

	int getField(int i);
}
