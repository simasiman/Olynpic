package modelPack;

public class Word
{
    private String word;
    private String wordRead;
    private String wordHead;
    private String wordTail;
    private int level;

    public Word(int level, String word, String wordRead, String wordHead, String wordTail)
    {
        this.level = level;
        this.word = word;
        this.wordRead = wordRead;
        this.wordHead = wordHead;
        this.wordTail = wordTail;
    }

    public String getWord()
    {
        return word;
    }

    public String getWordRead()
    {
        return wordRead;
    }

    public int getLevel()
    {
        return level;
    }

    public String checkSiritori(String wordRead)
    {
        if (wordRead.endsWith(this.wordRead.substring(0, 1)))
        {
            return this.wordRead;
        }

        return null;
    }

    public String getWordHead()
    {
        return wordHead;
    }

    public String getWordTail()
    {
        return wordTail;
    }

    public int getScore()
    {
        return getBaseScore() + getBonusScore();
    }

    public int getBaseScore()
    {
        return wordRead.length();
    }

    public int getBonusScore()
    {
        return level;
    }
}
