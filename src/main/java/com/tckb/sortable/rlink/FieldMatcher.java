package com.tckb.sortable.rlink;

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
	double computeScore(Field f1, Field f2);

	default boolean validateFields(final Field f1, final Field f2) {
		return f1 != null && f1.getValue() != null && !f1.getValue().isEmpty() &&
				f2 != null && f2.getValue() != null && !f2.getValue().isEmpty();
	}
}
