import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {
    Map<String, Document> documentMap = new HashMap<>();
    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null) {
            document = document.builder()
                    .id(UUID.randomUUID().toString())
                    .created(Instant.now())
                    .build();
        } else if (documentMap.get(document.getId()) != null)
            document = document.builder()
                    .created(document.getCreated())
                    .build();

        documentMap.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documentMap.values().stream()
                .filter(document -> matchTitlePrefixes(document, request.getTitlePrefixes()))
                .filter(document -> matchContent(document, request.getContainsContents()))
                .filter(document -> matchAuthorIds(document, request.getAuthorIds()))
                .filter(document -> matchCreatedFromTo(document, request.getCreatedFrom(), request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    boolean matchTitlePrefixes(Document document, List<String> titlePrefixes) {
        if (titlePrefixes == null || titlePrefixes.isEmpty())
            return true;

        for (String prefix : titlePrefixes) {
            if (document.getTitle() != null && document.getTitle().startsWith(prefix))
                return true;
        }
        return false;
    }

    boolean matchContent(Document document, List<String> containsContents) {
        if (containsContents == null || containsContents.isEmpty())
            return true;
        for (String content : containsContents) {
            if (document.getContent() != null && document.getContent().contains(content))
                return true;
        }
        return false;
    }

    boolean matchAuthorIds(Document document, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty())
            return true;
        for (String id : authorIds)
            if (document.getAuthor() != null && id.equals(document.getAuthor().getId()))
                return true;
        return false;
    }

    boolean matchCreatedFromTo(Document document, Instant createdFrom, Instant createdTo) {
        if (createdFrom != null && document.getCreated().isBefore(createdFrom)) {
            return false;
        }
        if (createdTo != null && document.getCreated().isAfter(createdTo)) {
            return false;
        }
        return true;
    }


    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        Document document = documentMap.get(id);
        return Optional.ofNullable(document);
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}