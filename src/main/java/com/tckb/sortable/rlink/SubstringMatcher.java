package com.tckb.sortable.rlink;

/**
 * Created on 21/11/16.
 *
 * @author tckb
 */
public class SubstringMatcher implements FieldMatcher {
	@Override
	public double computeScore(final Field f1, final Field f2) {
		if (validateFields(f1, f2) && f2.getValue().contains(f1.getValue())) {
			return 1.0;
		}
		return 0.0;
	}
}
