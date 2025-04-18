package edu.guilford.ctisfinal.Backend;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A lightweight in‑memory CSV “DataFrame” to mirror Pandas DataFrame.
 *   • Load once on construction
 *   • getColumns(), getRows(), getColumn(name), get(row, col)
 */
public class CsvParser {

    private final List<String> headers;
    private final Map<String,Integer> headerMap;
    private final List<Row> rows;

    /** Represents one CSV row, with both index‑ and name‑based access. */
    public static class Row {
        private final List<String> values;
        private final Map<String,Integer> headerMap;

        private Row(List<String> values, Map<String,Integer> headerMap) {
            this.values    = Collections.unmodifiableList(values);
            this.headerMap = headerMap;
        }

        /** Get by column name. */
        public String get(String column) {
            Integer idx = headerMap.get(column);
            if (idx == null) throw new IllegalArgumentException("No such column: " + column);
            return values.get(idx);
        }

        /** Get by column index (0‑based). */
        public String get(int colIndex) {
            return values.get(colIndex);
        }

        /** Return as an unmodifiable Map<columnName,value>. */
        public Map<String,String> asMap() {
            Map<String,String> m = new LinkedHashMap<>();
            for (Map.Entry<String,Integer> e : headerMap.entrySet()) {
                m.put(e.getKey(), values.get(e.getValue()));
            }
            return Collections.unmodifiableMap(m);
        }
    }

    /**
     * Load the CSV at csvPath, using the first row as header.
     * @throws IOException on I/O error
     */
    public CsvParser(Path csvPath) throws IOException {
        try (Reader reader = Files.newBufferedReader(csvPath);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {

            // capture the header order & map
            this.headerMap = new LinkedHashMap<>(parser.getHeaderMap());
            this.headers   = new ArrayList<>(headerMap.keySet());

            // read & store each row
            List<Row> tempRows = new ArrayList<>();
            for (CSVRecord rec : parser) {
                List<String> vals = new ArrayList<>(headers.size());
                for (String h : headers) {
                    vals.add(rec.get(h));
                }
                tempRows.add(new Row(vals, headerMap));
            }
            this.rows = Collections.unmodifiableList(tempRows);
        }
    }

    /** @return List of column names in original order */
    public List<String> getColumns() {
        return headers;
    }

    /** @return number of columns */
    public int getColumnCount() {
        return headers.size();
    }

    /** @return number of rows */
    public int getRowCount() {
        return rows.size();
    }

    /** @return unmodifiable list of all Row objects */
    public List<Row> getRows() {
        return rows;
    }

    /** @return the Row at the given 0‑based index */
    public Row getRow(int index) {
        return rows.get(index);
    }

    /** @return entire column as a List<String> */
    public List<String> getColumn(String columnName) {
        Integer idx = headerMap.get(columnName);
        if (idx == null) throw new IllegalArgumentException("No such column: " + columnName);
        List<String> col = new ArrayList<>(rows.size());
        for (Row r : rows) {
            col.add(r.get(idx));
        }
        return Collections.unmodifiableList(col);
    }

    /** @return one cell by row and column name */
    public String get(int rowIndex, String columnName) {
        return getRow(rowIndex).get(columnName);
    }

    /** @return one cell by row and column index */
    public String get(int rowIndex, int colIndex) {
        return getRow(rowIndex).get(colIndex);
    }
}
