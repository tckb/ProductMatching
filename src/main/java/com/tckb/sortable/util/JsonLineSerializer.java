package com.tckb.sortable.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
 * A simple parser for writing and saving data into JSON lines format.
 * <p>
 * Created on 20/11/16.
 *
 * @author tckb
 */
public final class JsonLineSerializer<T> {

	protected final Logger LOGGER = Logger.getLogger(getClass().getName());
	private final ObjectMapper jsonObjectMapper = new ObjectMapper();
	private final Class<T> klass;

	public JsonLineSerializer(final Class<T> klass) {this.klass = klass;}

	/**
	 * Deserialize the file of type jsonl to data
	 *
	 * @param file
	 * @return list of records of type T
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<T> deserialize(File file) throws IOException {
		LOGGER.info("deserialize " + file.getName());
		List<T> tList = new ArrayList<T>();
		final LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");

		try {
			while (lineIterator.hasNext()) {
				final String line = lineIterator.nextLine();
				if (!line.isEmpty()) {
					tList.add(jsonObjectMapper.readValue(line, klass));
				}
			}
		} finally {
			LineIterator.closeQuietly(lineIterator);
		}

		return tList;
	}

	/**
	 * Serializes data into file.
	 *
	 * @param dataToWrite
	 * 		data to be serialized
	 * @param file
	 * 		file where the data has to be serialized. (old data will be truncated!)
	 * @throws IOException
	 */
	public void serialize(Collection<T> dataToWrite, File file) throws IOException {
		LOGGER.info("Serializing " + dataToWrite.size() + " items to " + file.getName());

		try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			Iterator<T> iterator = dataToWrite.iterator();
			while (iterator.hasNext()) {
				final T data = iterator.next();
				try {
					bufferedWriter.append(jsonObjectMapper.writeValueAsString(data)).append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
