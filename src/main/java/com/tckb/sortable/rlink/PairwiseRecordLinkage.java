package com.tckb.sortable.rlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * links the record by pairwise checking of records in the lists
 * Created on 20/11/16.
 *
 * @author tckb
 */
public class PairwiseRecordLinkage extends AbstractRecordLinker implements RecordLinker {

	public PairwiseRecordLinkage(final double threshold, final List<FieldMatchingCriteria> criterion, final FieldMatcher matcher) {
		super(threshold, criterion, matcher);
	}

	@Override
	public <U extends DataRecord, T extends DataRecord> Map<T, List<U>> matchRecords(final List<T> list1, final List<U> list2) {
		Map<T, List<U>> matchingData = new HashMap<>();
		LOGGER.info("linking records");
		for (int i = 0, dataRecordList1Size = list1.size(); i < dataRecordList1Size; ++i) {
			final T r1_i = list1.get(i);
			List<U> r1_i_matches = new ArrayList<>();

			for (int j = 0, dataRecordList2Size = list2.size(); j < dataRecordList2Size; ++j) {
				final U r2_j = list2.get(j);
				if (computeScore(r1_i, r2_j) >= SCORE_THRESHOLD) {
					r1_i_matches.add(r2_j);
				}
			}
			matchingData.put(r1_i, r1_i_matches);
		}
		return matchingData;
	}

	/**
	 * computes the similarity between records
	 *
	 * @param record1
	 * @param record2
	 * @param <U>
	 * @param <T>
	 * @return a score in [0,1]
	 */
	private <U extends DataRecord, T extends DataRecord> double computeScore(final T record1, final U record2) {

		final Field[] record1Fields = record1.getFields(), record2Fields = record2.getFields();

		double totalScore = 0;
		for (int i = 0, fieldsLength = record1Fields.length; i < fieldsLength; ++i) {
			for (int j = 0, record2FieldsLength = record2Fields.length; j < record2FieldsLength; ++j) {
				if (record1Fields[i].getValue() == null || record2Fields[j].getValue() == null) {
					LOGGER.info(record1 + ":" + record2);
				}
				totalScore += checkAndGetScore(record1Fields[i], record2Fields[j]);
			}
		}
		return totalScore;
	}


}
