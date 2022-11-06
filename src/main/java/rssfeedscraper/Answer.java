package rssfeedscraper;

public class Answer {

    private final String title;
    private final String item;
    private final String author;

    public Answer(String title, String item, String author) {
        this.title = title;
        this.item = item;
        this.author = author;
    }


    public String getPrice() {
        if (author==null){
            throw new IllegalStateException();
        }
        return author;
    }

    public String getItem() {
        return item;
    }

    public String getSite() {
        return title;
    }

    @Override
    public String toString() {
        if (author == null) {
            return item + "@" + title + " : Not found";
        } else {
            return item + "@" + title + " : " + author;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (!title.equals(answer.title)) return false;
        if (!item.equals(answer.item)) return false;
        return author != null ? author.equals(answer.author) : answer.author == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}