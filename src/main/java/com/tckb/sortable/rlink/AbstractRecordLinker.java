package com.tckb.sortable.rlink;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created on 20/11/16.
 *
 * @author tckb
 */
public abstract class AbstractRecordLinker {
	protected final double SCORE_THRESHOLD;
	protected final List<FieldMatchingCriteria> criterion;
	protected final FieldMatcher matcher;
	protected final Logger LOGGER = Logger.getLogger(getClass().getName());

	public AbstractRecordLinker(final double threshold, final List<FieldMatchingCriteria> criterion, final FieldMatcher matcher) {
		this.SCORE_THRESHOLD = threshold;
		this.criterion = criterion;
		this.matcher = matcher;
	}

	protected double computeFieldScore(Field f1, Field f2) {
		for (int i = 0, criterionSize = criterion.size(); i < criterionSize; ++i) {
			final FieldMatchingCriteria criteria = criterion.get(i);
			if (criteria.accept(f1, f2)) {
				return matcher.computeScore(f1.getValue(), f2.getValue()) * criteria.getWeight();
			}
		}
		return 0.d;
	}


}
