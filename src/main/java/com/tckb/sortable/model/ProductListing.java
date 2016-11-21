package com.tckb.sortable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tckb.sortable.rlink.DataRecord;
import com.tckb.sortable.rlink.Field;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A pojo representing ProductListing.
 * Created on 20/11/16.
 *
 * @author tckb
 */
@Data
@ToString(of = "title")
@EqualsAndHashCode(of = {"title", "manufacturer", "currency", "price"})
@JsonInclude(Include.NON_NULL)
public class ProductListing implements DataRecord {
	private String title = "";
	private String currency = "";
	private String price = "";
	private String manufacturer = "";

	@Override
	@JsonIgnore
	public Field[] getFields() {
		return new Field[]{
				new Field("listing.title", title),
				new Field("listing.manufacturer", manufacturer)
		};
	}


}
