package com.tckb.sortable;

import com.tckb.sortable.model.JsonLineSerializer;
import com.tckb.sortable.model.Product;
import com.tckb.sortable.model.ProductListing;
import com.tckb.sortable.model.ProductMatch;
import com.tckb.sortable.rlink.FieldMatchingCriteria;
import com.tckb.sortable.rlink.PairwiseRecordLinkage;
import com.tckb.sortable.rlink.RecordLinker;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * Created on 20/11/16.
 *
 * @author tckb
 */
public class ProductListingMain {
	protected final static Logger LOGGER = Logger.getLogger(ProductListingMain.class.getName());

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		checkArgs(args);

		final JsonLineSerializer<Product> productDataSerializer = new JsonLineSerializer<>(Product.class);
		final JsonLineSerializer<ProductListing> productListingDataSerializer = new JsonLineSerializer<>(ProductListing.class);
		final JsonLineSerializer<ProductMatch> productMatchDataSerializer = new JsonLineSerializer<>(ProductMatch.class);

		final List<Product> products = productDataSerializer.deserialize(new File(args[0]));
		final List<ProductListing> listings = productListingDataSerializer.deserialize(new File(args[1]));

		final RecordLinker recordLinker = new PairwiseRecordLinkage(0.9, matchingCriteria());

		Instant now = Instant.now();
		final Map<Product, List<ProductListing>> matchingData = recordLinker.matchRecords(products, listings);

		LOGGER.info("matching time: " + ChronoUnit.SECONDS.between(now, Instant.now()) + "sec");

		List<ProductMatch> productListings = new ArrayList<>();
		for (final Entry<Product, List<ProductListing>> matchedEntries : matchingData.entrySet()) {
			productListings.add(new ProductMatch(matchedEntries.getKey().getProductName(), matchedEntries.getValue()));
		}
		productMatchDataSerializer.serialize(productListings, new File(args[2]));
	}

	private static void checkArgs(final String[] args) {
		if (args.length < 3) {
			throw new IllegalArgumentException("require 2 args: </path/to/products.jsonl> </path/to/listing.jsonl> </path/to/output.jsonl>");
		}
	}

	private static List<FieldMatchingCriteria> matchingCriteria() {
		final ArrayList<FieldMatchingCriteria> fieldMatchingCriterias = new ArrayList<>();
		fieldMatchingCriterias.add(new FieldMatchingCriteria("product.manufacturer", "listing.manufacturer", 0.6));
		fieldMatchingCriterias.add(new FieldMatchingCriteria("product.model", "listing.title", 0.3));
		fieldMatchingCriterias.add(new FieldMatchingCriteria("product.family", "listing.title", 0.1));
		return Collections.unmodifiableList(fieldMatchingCriterias);
	}


}
