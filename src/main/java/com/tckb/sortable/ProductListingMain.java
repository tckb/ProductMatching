package com.tckb.sortable;

import com.tckb.sortable.model.Product;
import com.tckb.sortable.model.ProductListing;
import com.tckb.sortable.model.ProductMatch;
import com.tckb.sortable.rlink.alg.RecordLinker.MatchedRecord;
import com.tckb.sortable.rlink.alg.impl.NaivePairLinkage;
import com.tckb.sortable.rlink.alg.impl.ParallelNaivePairLinkage;
import com.tckb.sortable.rlink.alg.support.FieldMatchingCriteria;
import com.tckb.sortable.rlink.alg.support.SubstringMatcher;
import com.tckb.sortable.util.JsonLineSerializer;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created on 20/11/16.
 *
 * @author tckb
 */
public class ProductListingMain {
	protected final static Logger LOGGER = Logger.getLogger(ProductListingMain.class.getName());

	public static void main(String[] args) throws IOException {
		checkArgs(args);

		final JsonLineSerializer<Product> productDataSerializer = new JsonLineSerializer<>(Product.class);
		final JsonLineSerializer<ProductListing> productListingDataSerializer = new JsonLineSerializer<>(ProductListing.class);

		final List<Product> products = productDataSerializer.deserialize(new File(args[0]));
		final List<ProductListing> listings = productListingDataSerializer.deserialize(new File(args[1]));


		Instant now = Instant.now();
		List<MatchedRecord<Product, ProductListing>> matchedRecords = new NaivePairLinkage(0.9, matchingCriteria(), new SubstringMatcher())
				.matchRecords(products, listings);
		System.out.println("matching time: " + ChronoUnit.SECONDS.between(now, Instant.now()) + "sec");


		now = Instant.now();
		List<MatchedRecord<Product, ProductListing>> matchedRecords2 = new ParallelNaivePairLinkage(0.9, matchingCriteria(), new SubstringMatcher())
				.matchRecords(products, listings);
		System.out.println("matching time: " + ChronoUnit.SECONDS.between(now, Instant.now()) + "sec");


		List<ProductMatch> productListings = new ArrayList<>();
		matchedRecords.forEach(data -> productListings.add(new ProductMatch(data.key.getProductName(), data.values)));
		new JsonLineSerializer<>(ProductMatch.class).serialize(productListings, new File(args[2]));
	}

	private static void checkArgs(final String[] args) {
		if (args.length < 3) {
			throw new IllegalArgumentException("require 3 args: </path/to/products.jsonl> </path/to/listing.jsonl> </path/to/output.jsonl> [verbose]");
		} else {
			// just a verbose condition
			if (args.length == 3) {
				LogManager.getLogManager().reset();
				Logger globalLogger = Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
				globalLogger.setLevel(java.util.logging.Level.OFF);
			}
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
