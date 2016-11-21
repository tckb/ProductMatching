package com.tckb.sortable.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created on 20/11/16.
 *
 * @author tckb
 */
@Data
@AllArgsConstructor
public class ProductMatch {
	@JsonProperty("product_name")
	private String productName;
	private List<ProductListing> listings;
}
