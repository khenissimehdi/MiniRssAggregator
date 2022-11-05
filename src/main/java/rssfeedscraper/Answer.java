package rssfeedscraper;


import java.util.Date;

public class Answer {

  private final String title;
  private final String item;
  private final String author;
  private final Date publishedDate;

  public Answer(String title, String item, String author, Date publishedDate) {
    this.title = title;
    this.item = item;
    this.author = author;
    this.publishedDate = publishedDate;
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
  public Date getDate(){return publishedDate;}

  @Override
  public String toString() {
    if (author == null) {
        //return item + "@" + title + " : Not found" + "date : unknown ";
        return "@" + title + " : Not found" + " on date : unknown ";
    } else {
        //return item + "@" + title + " : " + author + "date :" + publishedDate;
        return  "@" + title + " : by " + author + " on date :" + publishedDate;
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