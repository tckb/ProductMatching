package com.tckb.sortable.rlink.alg.impl;

import com.tckb.sortable.rlink.DataRecord;
import com.tckb.sortable.rlink.alg.support.CachedParallelLoop;
import com.tckb.sortable.rlink.alg.support.FieldMatcher;
import com.tckb.sortable.rlink.alg.support.FieldMatchingCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22/11/16.
 *
 * @author tckb
 */
public class ParallelNaivePairLinkage extends NaivePairLinkage {
	private final CachedParallelLoop pFor = new CachedParallelLoop();

	public ParallelNaivePairLinkage(final double threshold, final List<FieldMatchingCriteria> criterion, final FieldMatcher matcher) {
		super(threshold, criterion, matcher);
	}

	@Override
	public <U extends DataRecord, T extends DataRecord> List<MatchedRecord<T, U>> matchRecords(final List<T> list1, final List<U> list2) {
		List<MatchedRecord<T, U>> matchedRecordList = new ArrayList<>(list1.size());
		LOGGER.info("linking records");
		for (int i = 0, dataRecordList1Size = list1.size(); i < dataRecordList1Size; ++i) {
			final T r1_i = list1.get(i);
			List<U> r1_i_matches = new ArrayList<>();
			matchedRecordList.add(new MatchedRecord<>(r1_i, r1_i_matches));

			pFor.withIndex(0, list2.size(), i1 -> {
				U r2_j = list2.get(i1);
				if (computeScore(r1_i, r2_j) >= SCORE_THRESHOLD) {
					r1_i_matches.add(r2_j);
				}
			});
		}
		pFor.finish();
		return matchedRecordList;
	}

}
