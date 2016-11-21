package com.tckb.sortable.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tckb.sortable.rlink.DataRecord;
import com.tckb.sortable.rlink.Field;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A pojo representing Product.
 * Created on 20/11/16.
 *
 * @author tckb
 */
@Data
@ToString(of = "productName")
@EqualsAndHashCode(exclude = {"productName", "announcedDate"})
@JsonInclude(Include.NON_NULL)
public class Product implements DataRecord {
	@JsonProperty("product_name")
	private String productName = "";
	private String manufacturer = "";
	private String model = "";
	private String family = "";
	@JsonProperty("announced-date")
	private String announcedDate = "";


	@Override
	@JsonIgnore
	public Field[] getFields() {
		return new Field[]{
				new Field("product.manufacturer", manufacturer),
				new Field("product.model", model),
				new Field("product.family", family),
		};
	}

}
