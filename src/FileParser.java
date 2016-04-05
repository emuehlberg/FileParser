import java.io.IOException;

public interface FileParser
{
	public boolean loadFile(String Filename);
	public String getLine() throws IOException;
	public String getError();
}
