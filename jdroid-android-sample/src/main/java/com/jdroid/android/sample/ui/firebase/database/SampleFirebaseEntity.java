package com.jdroid.android.sample.ui.firebase.database;

import com.jdroid.java.domain.Entity;

public class SampleFirebaseEntity extends Entity {

	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		builder.append("id='").append(getId()).append("\', ");
		builder.append("field='").append(field).append('\'');
		builder.append('}');
		return builder.toString();
	}
}
