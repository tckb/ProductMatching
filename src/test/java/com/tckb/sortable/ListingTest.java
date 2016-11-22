package com.tckb.sortable;

import com.tckb.sortable.model.Product;
import com.tckb.sortable.model.ProductListing;
import com.tckb.sortable.rlink.alg.RecordLinker;
import com.tckb.sortable.rlink.alg.RecordLinker.MatchedRecord;
import com.tckb.sortable.rlink.alg.impl.NaivePairLinkage;
import com.tckb.sortable.rlink.alg.impl.ParallelNaivePairLinkage;
import com.tckb.sortable.rlink.alg.support.FieldMatchingCriteria;
import com.tckb.sortable.rlink.alg.support.SubstringMatcher;
import com.tckb.sortable.util.JsonLineSerializer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 21/11/16.
 *
 * @author tckb
 */
public class ListingTest {

	Product pixelBlack, moto2g;
	JsonLineSerializer<Product> productSerializer;
	RecordLinker recordLinker;
	JsonLineSerializer<ProductListing> productListingSerializer;

	private List<FieldMatchingCriteria> matchingCriteria() {
		final ArrayList<FieldMatchingCriteria> fieldMatchingCriterias = new ArrayList<>();
		fieldMatchingCriterias.add(new FieldMatchingCriteria("product.manufacturer", "listing.manufacturer", 0.6));
		fieldMatchingCriterias.add(new FieldMatchingCriteria("product.model", "listing.title", 0.4));
		return Collections.unmodifiableList(fieldMatchingCriterias);
	}

	@BeforeMethod
	public void setUp() throws Exception {
		recordLinker = new NaivePairLinkage(0.9, matchingCriteria(), new SubstringMatcher());
		productSerializer = new JsonLineSerializer<>(Product.class);
		productListingSerializer = new JsonLineSerializer<>(ProductListing.class);
	}

	@Test
	public void testMatcher() throws Exception {

		final List<Product> listOfProducts = productSerializer.deserialize(new File("data/test_products.jsonl"));
		productSerializer.serialize(listOfProducts, new File("/tmp/product.data"));

		final List<Product> deserializedProductList = productSerializer.deserialize(new File("/tmp/product.data"));


		Assert.assertEquals(deserializedProductList.size(), listOfProducts.size(), "serialized/deserialization mismatched!");
		Assert.assertTrue(listOfProducts.get(0).equals(deserializedProductList.get(0)));


		pixelBlack = listOfProducts.get(0);
		moto2g = listOfProducts.get(1);

		final List<ProductListing> listOfProductListings = productListingSerializer.deserialize(new File("data/test_listings.jsonl"));

		final List<MatchedRecord<Product, ProductListing>> matchedRecords = recordLinker.matchRecords(listOfProducts, listOfProductListings);

		Assert.assertEquals(matchedRecords.size(), listOfProducts.size());
		Assert.assertEquals(matchedRecords.get(0).values.size(), 1);
		Assert.assertEquals(matchedRecords.get(1).values.size(), 1);

	}

	@Test
	public void testParallelVsSerial() throws Exception {
		final List<Product> listOfProducts = productSerializer.deserialize(new File("data/test_products.jsonl"));
		final List<ProductListing> listOfProductListings = productListingSerializer.deserialize(new File("data/test_listings.jsonl"));

		final List<MatchedRecord<Product, ProductListing>> matchSerial = new NaivePairLinkage(0.9, matchingCriteria(), new SubstringMatcher())
				.matchRecords(listOfProducts, listOfProductListings);

		final List<MatchedRecord<Product, ProductListing>> matchParallel = new ParallelNaivePairLinkage(0.9, matchingCriteria(), new SubstringMatcher())
				.matchRecords(listOfProducts, listOfProductListings);

		Assert.assertEquals(matchSerial, matchParallel);

	}


}