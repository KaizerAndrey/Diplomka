import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> wordIndex;

    public BooleanSearchEngine() throws IOException {
        wordIndex = Index.getIndexedStorage().getStorage();

        File[] arrOfPdfs = new File("pdfs").listFiles();

        for (int i = 0; i < Objects.requireNonNull(arrOfPdfs).length; i++) {
            var doc = new PdfDocument(new PdfReader(arrOfPdfs[i]));

            for (int j = 0; j < doc.getNumberOfPages(); j++) {
                var file = doc.getPage(j + 1);
                var text = PdfTextExtractor.getTextFromPage(file);

                String[] words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                String namePDFFile = doc.getDocumentInfo().getTitle();

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    String tmpWord = entry.getKey();
                    int tmpValue = entry.getValue();

                    if (!wordIndex.containsKey(tmpWord)) {
                        List<PageEntry> listPageTmp = new ArrayList<>();
                        wordIndex.put(tmpWord, listPageTmp);
                    }

                    wordIndex.get(tmpWord).add(new PageEntry(namePDFFile, j + 1, tmpValue));
                }
            }

            for (Map.Entry<String, List<PageEntry>> entry : wordIndex.entrySet()) {
                Collections.sort(entry.getValue());
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        String wordToLowReg = word.toLowerCase();
        return wordIndex.getOrDefault(wordToLowReg, Collections.emptyList());
    }
}
