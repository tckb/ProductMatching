package com.tckb.sortable.rlink.alg.support;

/**
 * Created on 21/11/16.
 *
 * @author tckb
 */
public class SubstringMatcher implements FieldMatcher {
	@Override
	public double computeScore(final String f1, final String f2) {
		if (validateFields(f1, f2) && f2.contains(f1)) {
			return 1.0;
		}
		return 0.0;
	}
}
