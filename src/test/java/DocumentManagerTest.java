import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class DocumentManagerTest {
    DocumentManager documentManager = new DocumentManager();
    DocumentManager.Document saveDocument;

    @Test
    void save() {

        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("1")
                .name("Name")
                .build();
        DocumentManager.Document document = DocumentManager.Document
                .builder()
                .id("1")
                .title("Title")
                .content("Content")
                .author(author)
                .created(Instant.ofEpochSecond(1500))
                .build();
        saveDocument = documentManager.save(document);
        assertEquals(saveDocument.getId(), "1");
        assertEquals(saveDocument.getTitle(), "Title");
        assertEquals(saveDocument.getContent(), "Content");
        assertEquals(saveDocument.getAuthor(), author);
        assertEquals(saveDocument.getCreated(), Instant.ofEpochSecond(1500));
    }

    @Test
    void search() {
        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("1")
                .name("Name")
                .build();
        DocumentManager.Document document = DocumentManager.Document
                .builder()
                .id("1")
                .title("Title")
                .content("Content")
                .author(author)
                .created(Instant.ofEpochSecond(1500))
                .build();
        List<String> titlePrefixesList = Arrays.asList("Hello", "Good morning", "Hi", "Title");
        List<String> containsContentsList = Arrays.asList("My name is Oleg", "My name is Artem", "My name is Maxim", "Content");
        List<String> authorIdsList = Arrays.asList("1", "2", "3");
        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(titlePrefixesList)
                .containsContents(containsContentsList)
                .authorIds(authorIdsList)
                .createdFrom(Instant.ofEpochSecond(1000))
                .createdTo(Instant.ofEpochSecond(2000))
                .build();
        documentManager.search(searchRequest);
        assertTrue(documentManager.matchTitlePrefixes(document, titlePrefixesList));
        assertTrue(documentManager.matchContent(document, containsContentsList));
        assertTrue(documentManager.matchAuthorIds(document, authorIdsList));
        assertTrue(documentManager.matchCreatedFromTo(document, searchRequest.getCreatedFrom(), searchRequest.getCreatedTo()));
    }

    @Test
    void findById() {
        Optional<DocumentManager.Document> byId = documentManager.findById("3");
        assertFalse(byId.isPresent());
    }
}