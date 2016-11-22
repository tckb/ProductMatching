#!/usr/bin/env bash
# script to build and run the project
PRODUCTS_FILE=data/products.jsonl
LISTING_FILE=data/listings.jsonl
OUTPUT=data/products_listings.jsonl


# sanity checks
for cmd in mvn java
do
    hash $cmd &>/dev/null
    if [[ $? -ne 0 ]]; then
	    echo "$cmd unavailable! this is required for building/running this project."
	    exit 100
    fi
done


# build&run the project
echo "building the project.."
mvn clean package
echo "////"

echo -e "\n running the project"
java -jar target/product-matching-1.0-SNAPSHOT-jar-with-dependencies.jar $PRODUCTS_FILE $LISTING_FILE $OUTPUT $1
echo "////"
echo "..done"

echo "the matching file is $OUTPUT"