package modelPack;

import java.util.ArrayList;

public class Panel
{
    private int id;
    private String baseWord;
    private boolean isOriginal;
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

    public boolean isOriginal()
    {
        return isOriginal;
    }

    public void setOriginal(boolean isOriginal)
    {
        this.isOriginal = isOriginal;
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

    public int isMatchWord(Word word)
    {
        String tH = word.getWordHead();
        String tT = word.getWordTail();

        for (int i = 0; i < wordList.size(); i++)
        {
            Word w = wordList.get(i);
            String wH = w.getWordHead();
            String wT = w.getWordTail();

            if (tH.equals(wH) || tH.equals(wT) || tT.equals(wH) || tT.equals(wT))
            {
                return i;
            }
        }

        return -1;
    }

    public int isMatchWord(Word word, ArrayList<Word> selectedWordList)
    {
        String tH = word.getWordHead();
        String tT = word.getWordTail();

        for (int i = 0; i < wordList.size(); i++)
        {
            Word w = wordList.get(i);
            String wH = w.getWordHead();
            String wT = w.getWordTail();

            if (tH.equals(wH) || tH.equals(wT) || tT.equals(wH) || tT.equals(wT))
            {
                // 既に使用された単語でないかを確認
                boolean isHit = false;
                for (int j = 0; j < selectedWordList.size(); j++)
                {
                    if (w.getWordRead().equals(selectedWordList.get(j).getWordRead()))
                    {
                        isHit = true;
                        break;
                    }
                }
                if (!isHit)
                {
                    // 使用単語リストに存在しなければTRUE
                    return i;
                }
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
