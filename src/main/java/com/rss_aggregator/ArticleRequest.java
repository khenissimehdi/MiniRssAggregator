package com.rss_aggregator;

import com.rss_aggregator.entity.Article;
import rssfeedscraper.Answer;

import java.util.List;

public record ArticleRequest(Answer answer) {
}
