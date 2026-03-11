package interfaces;

import stores.Keyword;

public interface IKeywords{

    public boolean add(int filmID, Keyword[] keywords);
    public boolean add(int filmID, Keyword keyword);

    public boolean remove(int filmID);
    public boolean removeKeywordFromFilm(int filmID, int keywordID);

    public int[] getFilmIDs();
    public int[] getKeywordIDs();

    public int[] getFilmsWithKeyword(int keywordID);

    public Keyword[] getKeywordsForFilm(int filmID);
    public Keyword[] getUnique();

    public int[] getMostKeywordFilms(int numResults);

    public Keyword[] findKeywords(String searchTerm);

    public String toString();
    public int size();
    
}
