package com.tckb.sortable.rlink.alg.support;

import com.tckb.sortable.rlink.Field;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A field-to-field matching criteria.
 * <p>
 * Created on 20/11/16.
 *
 * @author tckb
 */
@Data
@EqualsAndHashCode(exclude = {"weight"})
public class FieldMatchingCriteria {
	final String field1Name;
	final String field2Name;
	final double weight;

	public FieldMatchingCriteria(String field1Name, String field2Name, double weight) {
		this.field1Name = field1Name;
		this.field2Name = field2Name;
		this.weight = weight;
	}

	/**
	 * checks if the given fields are a part of this criteria
	 *
	 * @param f1
	 * 		field1
	 * @param f2
	 * 		field2
	 * @return
	 */
	public boolean accept(final Field f1, final Field f2) {
		return f1.getName().equalsIgnoreCase(field1Name) &&
				f2.getName().equalsIgnoreCase(field2Name);
	}
}
