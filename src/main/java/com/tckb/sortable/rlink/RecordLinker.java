package com.tckb.sortable.rlink;

import java.util.List;
import java.util.Map;

/**
 * A spec for record linkage
 * <p>
 * Created on 20/11/16.
 *
 * @author tckb
 */
public interface RecordLinker {
	/**
	 * links the datarecord from first list to the second.
	 *
	 * @param list1
	 * 		list of datarecords which has to be linked
	 * @param list2
	 * 		list of datarecords which has to be matched to
	 * @param <T>
	 * 		the datarecord
	 * @param <U>
	 * 		the datarecord
	 * @return a map containing the matches.
	 * @see com.tckb.sortable.rlink.DataRecord
	 */
	<U extends DataRecord, T extends DataRecord> Map<T, List<U>> matchRecords(List<T> list1, List<U> list2);
}
