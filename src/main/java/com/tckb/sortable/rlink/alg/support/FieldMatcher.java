package com.tckb.sortable.rlink.alg.support;

/**
 * Created on 21/11/16.
 *
 * @author tckb
 */
public interface FieldMatcher {

	/**
	 * Computes the similarity between the fields and returns the score
	 *
	 * @param f1
	 * 		field 1
	 * @param f2
	 * 		field 2
	 * @return the similarity score ∈ [0,1]
	 */
	double computeScore(String f1, String f2);

	default boolean validateFields(final String f1, final String f2) {
		return f1 != null && !f1.isEmpty() &&
				f2 != null && !f2.isEmpty();
	}
}
