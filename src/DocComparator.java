import java.util.Comparator;

public class DocComparator implements Comparator<Doc>
{
	public DocComparator()
	{
		super();
	}
	
	@Override
	public int compare(Doc o1, Doc o2)
	{
		if(o1.getWordCount() > o2.getWordCount())
			return 1;
		else if(o1.getWordCount() == o2.getWordCount())
		{
			if(o1.getLineCount() > o2.getLineCount())
				return 1;
			if(o1.getLineCount() < o2.getLineCount())
				return -1;
			else
				return 0;
		}
		else
			return -1;
	}

}
