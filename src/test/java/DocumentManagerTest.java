import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {

    @Test
    void save() {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("1")
                .name("Name")
                .build();
        Instant instant = Instant.now();
        DocumentManager.Document document = DocumentManager.Document
                .builder()
                .id("1")
                .title("Title")
                .content("Content")
                .author(author)
                .created(instant)
                .build();
        DocumentManager.Document saveDocument = documentManager.save(document);
        assertEquals(saveDocument.getId(), "1");
        assertEquals(saveDocument.getTitle(), "Title");
        assertEquals(saveDocument.getContent(), "Content");
        assertEquals(saveDocument.getAuthor(), author);
        assertEquals(saveDocument.getCreated(), instant);
    }

    @Test
    void search() {
    }

    @Test
    void findById() {
    }
}