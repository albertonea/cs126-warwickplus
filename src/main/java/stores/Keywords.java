package stores;

import interfaces.IKeywords;
import interfaces.AbstractStores;
import structures.MyArrayList;

public class Keywords implements IKeywords{
    AbstractStores stores;

    MyArrayList<Integer> id;
    MyArrayList<Keyword[]> keywords;
    MyArrayList<Keyword> unique;

    Keywords(AbstractStores stores) {
        this.stores = stores;
        id = new MyArrayList<>();
        keywords = new MyArrayList<>();
        unique = new MyArrayList<>();
    }

    @Override
    public boolean add(int filmID, Keyword keyword) {
        boolean result = true;

        for (int i = 0; i < this.id.size(); i++) {
            if(this.id.get(i) == filmID) {
                Keyword[] tmp = new Keyword[keywords.get(i).length + 1];
                for (int j = 0; j < keywords.get(i).length; j++) {
                    tmp[j] = keywords.get(i)[j];
                }
                tmp[keywords.get(i).length] = keyword;
                keywords.set(i, tmp);
                return result;
            }
        }

        boolean flag = true;
        for(int i = 0; i < unique.size(); i++) {
            if(this.unique.get(i).getID() == keyword.getID()) {
                flag = false;
                break;
            }
        }

        if (flag) {
            unique.add(keyword);
        }
        

        result &= this.id.add(filmID);
        Keyword[] tmp = {keyword};
        result &= keywords.add(tmp);
        return result;
    }

    @Override
    public boolean add(int id, Keyword[] keywords) {
        boolean result = true;

        for (int i = 0; i < this.id.size(); i++) {
            if (this.id.get(i) == id) {
                Keyword[] tmp = new Keyword[this.keywords.get(i).length + keywords.length];
                for (int j = 0; j < this.keywords.get(i).length; j++) {
                    tmp[j] = this.keywords.get(i)[j];
                }
                for (int j = 0; j < keywords.length; j++){
                    tmp[this.keywords.get(i).length+j] = keywords[j];
                }
                this.keywords.set(i, tmp);
                return result;
            }
        }

        for (int i = 0; i < keywords.length; i++) {
            boolean flag = true;
            for (int j = 0; j < unique.size(); j++) {
                if(this.unique.get(j).getID() == keywords[i].getID()) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                unique.add(keywords[i]);
            }
        }

        result &= this.id.add(id);
        result &= this.keywords.add(keywords);
        return result;
    }

    @Override
    public boolean remove(int id) {
        int index = this.id.indexOf(id);
        boolean result = this.id.remove(id);
        result &= this.keywords.remove(this.keywords.get(index));
        return result;
    }

    @Override
    public boolean removeKeywordFromFilm(int id, int keywordID) {
        for (int i = 0; i < this.id.size(); i++) {
            if(this.id.get(i) == id) {
                int indexToRemove = -1;
                for (int j = 0; j < this.keywords.get(i).length; j++) {
                    if(this.keywords.get(i)[j].getID() == keywordID) {
                        indexToRemove = j;
                        break;
                    }
                }
                if (indexToRemove >=0) {
                    Keyword[] tmp = new Keyword[this.keywords.get(i).length-1];
                    int counter = 0;
                    for (int j = 0; j < this.keywords.get(i).length; j++) {
                        if (j == indexToRemove) {
                            continue;
                        }
                        tmp[counter] = this.keywords.get(i)[j];
                        counter++;
                    }

                    this.keywords.set(i, tmp);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getFilmIDs() {
        int[] result = new int[id.size()];
        for (int i = 0; i < id.size(); i++) {
            result[i] = id.get(i);
        }
        return result;
    }

    @Override
    public int[] getKeywordIDs(){
        int[] uniqueKeyword = new int[unique.size()];

        for (int i = 0; i < unique.size(); i++) {
            uniqueKeyword[i] = unique.get(i).getID();
        }

        return uniqueKeyword;
    }

    @Override
    public int[] getFilmsWithKeyword(int keywordID) {
        MyArrayList<Integer> tmp = new MyArrayList<>();
        for (int i = 0; i < this.id.size(); i++) {
            for (int j = 0; j < this.keywords.get(i).length; j++) {
                if (keywordID == this.keywords.get(i)[j].getID()) {
                    tmp.add(this.id.get(i));
                    break;
                }
            }
        }

        int[] result = new int[tmp.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tmp.get(i);
        }
        return result;
    }

    @Override
    public Keyword[] getKeywordsForFilm(int id) {
        int index = this.id.indexOf(id);

        if (index < 0 || index > size()) {
            return null;
        }
        return keywords.get(index);
    }

    @Override
    public Keyword[] getUnique() {
        Keyword[] uniqueKeyword = new Keyword[unique.size()];

        for (int i = 0; i < unique.size(); i++) {
            uniqueKeyword[i] = unique.get(i);
        }

        return uniqueKeyword;
    }

    @Override
    public int size() {
        return keywords.size();
    }

    @Override
    public Keyword[] findKeywords(String keyword) {
        MyArrayList<Keyword> tmpResult = new MyArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            for (int j = 0; j < keywords.get(i).length; j++) {
                if (keywords.get(i)[j].getName().contains(keyword)) {
                    tmpResult.add(keywords.get(i)[j]);
                }
            }
        }

        Keyword[] result = new Keyword[tmpResult.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = tmpResult.get(i);
        }

        return result;
    }
    

    @Override
    public int[] getMostKeywordFilms(int numResults) {
        int[] results = new int[numResults];
        int[] keywordCount = new int[numResults];

        for (int i = 0; i < id.size(); i++) {
            int currentID = id.get(i);
            int currentNumKeywords = keywords.get(i).length;
            for (int j = 0 ; j < results.length; j++) {
                if (keywordCount[j] == 0) {
                    results[j] = currentID;
                    keywordCount[j] = currentNumKeywords;
                    break;
                } else if (keywordCount[j] < currentNumKeywords) {
                    for (int k = (results.length-1); k > j; k--) {
                        results[k] = results[k-1];
                        keywordCount[k] = keywordCount[k-1];
                    }
                    results[j] = currentID;
                    keywordCount[j] = currentNumKeywords;
                    break;
                }
            }
        }

        return results;
    }
}
