package com.accountbook.data.export;

import com.accountbook.domain.model.Record;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CsvExporter {

    private static final String HEADER = "类型,金额,日期,分类,备注";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public void export(List<Record> records, Writer writer) throws Exception {
        List<Record> sorted = new ArrayList<>(records);
        Collections.sort(sorted, (a, b) -> a.getDate().compareTo(b.getDate()));
        
        writer.write(HEADER);
        writer.write("\n");
        for (Record r : sorted) {
            String typeChinese = "EXPENSE".equals(r.getType()) ? "支出" : "收入";
            writer.write(String.format(Locale.getDefault(),
                    "%s,%.2f,%s,%s,%s\n",
                    typeChinese,
                    r.getAmount(),
                    r.getDate(),
                    escapeCsv(r.getCategory()),
                    escapeCsv(r.getNote())
            ));
        }
        writer.flush();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
