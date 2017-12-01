package modelPack;

import java.util.ArrayList;

public class Panel
{
    private int id;
    private String baseWord;
    private String picture;
    private ArrayList<Word> wordList;
    private Word selectedWord;
    private String selectedUserId;

    private boolean isUsed = false;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getBaseWord()
    {
        return baseWord;
    }

    public void setBaseWord(String baseWord)
    {
        this.baseWord = baseWord;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public ArrayList<Word> getWordList()
    {
        return wordList;
    }

    public void setWordList(ArrayList<Word> word)
    {
        this.wordList = word;
    }

    public void addWordList(Word w)
    {
        if (wordList == null)
        {
            wordList = new ArrayList<Word>();
        }

        wordList.add(w);
    }

    public int isMatchWord(String wordHead, String wordTail)
    {
        for (int i = 0; i < wordList.size(); i++)
        {
            Word w = wordList.get(i);
            String tH = w.getWordHead();
            String tT = w.getWordTail();

            if (wordHead.equals(tH) || wordHead.equals(tT) || wordTail.equals(tH) || wordTail.equals(tT))
            {
                selectedWord = w;
                return i;
            }
        }

        return -1;
    }

    public boolean isUsed()
    {
        return isUsed;
    }

    public void setUsed(boolean isUsed)
    {
        this.isUsed = isUsed;
    }

    public Word getSelectedWord()
    {
        return selectedWord;
    }

    public void setSelectedWord(Word selectedWord)
    {
        this.selectedWord = selectedWord;
    }

    public String getSelectedUserId()
    {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId)
    {
        this.selectedUserId = selectedUserId;
    }

}
