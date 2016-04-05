import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TXTParser implements FileParser
{
	BufferedReader br;
	String LastError;
	
	public TXTParser()
	{
		br = null;
		LastError = null;
	}

	@Override
	public boolean loadFile(String Filename)
	{
		// TODO Auto-generated method stub
		try
		{
			br = new BufferedReader(new FileReader(Filename));
		}
		catch (FileNotFoundException e)
		{
			LastError = e.getMessage();
			return false;
		}
		
		return true;
		
	}

	/**
	 * Returns the next line or null if at the end of file
	 */
	public String getLine() throws IOException
	{
		if(br.ready())
			return br.readLine();
		else
			return null;
	}
	
	public boolean closeFile()
	{
		try
		{
			br.close();
		} catch (IOException e)
		{
			LastError = e.getMessage();
			return false;
		}
		return true;
	}
	
	public String getError()
	{
		return LastError;
	}

}
