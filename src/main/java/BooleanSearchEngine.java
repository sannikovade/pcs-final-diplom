import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    protected final Map<String, List<PageEntry>> data = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        List<PageEntry> pageEntries;
        for (File file : pdfsDir.listFiles()) {
            if (file.getName().contains(".pdf")) {
                try (var doc = new PdfDocument(new PdfReader(file))) {
                    for (int page = 1; page < doc.getNumberOfPages(); page++) {
                        String text = PdfTextExtractor.getTextFromPage(doc.getPage(page));
                        String[] words = text.split("\\P{IsAlphabetic}+");
                        Set<String> setWords = Arrays.stream(words)
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());
                        for (String word : setWords) {
                            int count = Collections.frequency(Arrays.stream(words).collect(Collectors.toList()), word);
                            PageEntry pageEntry = new PageEntry(file.getName(), page, count);
                            if (!data.containsKey(word)) {
                                pageEntries = new ArrayList<>();
                                pageEntries.add(pageEntry);
                                data.put(word, pageEntries);
                            } else {
                                data.get(word).add(pageEntry);
                            }
                            data.get(word).sort(Collections.reverseOrder());
                        }

                    }
                } catch (IOException e) {
                    System.out.println("Reading error");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return data.get(word.toLowerCase());
    }
}
