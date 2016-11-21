package com.tckb.sortable.rlink;

import java.util.List;

/**
 * Created on 20/11/16.
 *
 * @author tckb
 */
public abstract class AbstractRecordLinker {
	protected final double SCORE_THRESHOLD;
	protected final List<FieldMatchingCriteria> criterion;

	public AbstractRecordLinker(final double threshold, final List<FieldMatchingCriteria> criterion) {
		this.SCORE_THRESHOLD = threshold;
		this.criterion = criterion;
	}

	protected double checkAndGetScore(Field f1, Field f2) {
		for (int i = 0, criterionSize = criterion.size(); i < criterionSize; ++i) {
			final FieldMatchingCriteria criteria = criterion.get(i);
			if (criteria.accept(f1, f2) && matches(f1, f2)) {
				return criteria.getWeight();
			}
		}
		return 0.d;
	}

	/**
	 * checks if the pair of fields matches
	 *
	 * @param f1
	 * 		field1
	 * @param f2
	 * 		field2
	 * @return boolean
	 */
	protected boolean matches(final Field f1, final Field f2) {
		return f2.getValue().contains(f1.getValue());
	}
}
