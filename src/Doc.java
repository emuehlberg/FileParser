public class Doc
{
	private String Filename;
	private String Filepath;
	private int Lcount;
	private int Wcount;
	
	public Doc()
	{
		Filename = "";
		Lcount = Wcount = 0;
	}
	
	public Doc(String Filename, String Filepath)
	{
		this.Filename = Filename;
		this.Filepath = Filepath;
	}
	
	public Doc(String Filename, String Filepath, int Wcount, int Lcount)
	{
		this.Filename = Filename;
		this.Filepath = Filepath;
		this.Lcount = Lcount;
		this.Wcount = Wcount;
	}
	
	public int getLineCount()
	{
		return Lcount;
	}
	
	public void setLineCount(int Count)
	{
		Lcount = Count;
	}
	
	public void incrementLineCount(int Count)
	{
		Lcount += Count;
	}
	
	public int getWordCount()
	{
		return Wcount;
	}
	
	public void setWordCount(int Count)
	{
		Wcount = Count;
	}
	
	public void incrementWordCount(int Count)
	{
		Wcount += Count;
	}
	
	public String getFilename()
	{
		return Filename;
	}
	
	public void setFilename(String Filename)
	{
		this.Filename = Filename;
	}
	
	public String getFilepath()
	{
		return Filepath;
	}
	
	public void setFilepath(String Filepath)
	{
		this.Filepath = Filepath;
	}
	
}