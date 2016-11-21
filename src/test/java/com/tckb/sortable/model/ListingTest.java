package com.tckb.sortable.model;

import com.tckb.sortable.rlink.FieldMatchingCriteria;
import com.tckb.sortable.rlink.PairwiseRecordLinkage;
import com.tckb.sortable.rlink.RecordLinker;
import com.tckb.sortable.rlink.SubstringMatcher;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
		recordLinker = new PairwiseRecordLinkage(0.9, matchingCriteria(), new SubstringMatcher());
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

		final Map<Product, List<ProductListing>> matchRecords = recordLinker.matchRecords(listOfProducts, listOfProductListings);

		Assert.assertEquals(matchRecords.size(), listOfProducts.size());
		Assert.assertEquals(matchRecords.get(pixelBlack).size(), 1);
		Assert.assertEquals(matchRecords.get(moto2g).size(), 1);


	}

}