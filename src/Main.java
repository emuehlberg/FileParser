import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Main implements ActionListener
{
	//Document list
	public static LinkedList<Doc> filelist = new LinkedList<Doc>();
	public static Main m = new Main();
	public static JFrame frame = new JFrame("File Parser");
	public static JTable table;
	public static JScrollPane pane;
	public static String delim = " ";
	
	public static void main(String[] args)
	{
		InitWindow();
		LoadSessionFile();
	}
	
	/*
	 * Display Setup
	 */
	public static void InitWindow()
	{
		//Base setup
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		//Menu
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		bar.add(menu);
		JMenuItem setDelim = new JMenuItem("Set Delimiter");
		setDelim.addActionListener(m);
		JMenuItem load = new JMenuItem("Load File");
		load.addActionListener(m);
		JMenuItem remfile = new JMenuItem("Remove File");
		remfile.addActionListener(m);
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(m);
		menu.add(setDelim);
		menu.add(load);
		menu.add(remfile);
		menu.addSeparator();
		menu.add(close);
		frame.setJMenuBar(bar);
		//Table
		Vector<String> tableData = new Vector<String>();
		Vector<String> headers = new Vector<String>();
		headers.add("Filename");
		headers.add("Word Count");
		headers.add("Line Count");
		table = new JTable(tableData, headers);
		pane = new JScrollPane(table);
		frame.add(pane, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setResizable(true);
	}

	
	/*
	 * Document parsing
	 */
	public static Doc parseDoc(String Filepath)
	{	
		TXTParser parser = new TXTParser();
		if(parser.loadFile(Filepath))
		{
			Doc result = new Doc(Filepath.substring(Filepath.lastIndexOf("\\")+1), Filepath);
			
			String line = null;
			try
			{
				while((line=parser.getLine()) != null)
				{
					result.incrementWordCount(CountWords(line, delim));
					result.incrementLineCount(1);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			if(!parser.closeFile())
			{
				//display error message
				JOptionPane.showMessageDialog(frame, parser.getError());
			}
			return result;
		}
		else
		{
			//Display error message to user
			JOptionPane.showMessageDialog(frame, parser.getError());
			return null;
		}
	}
	
	public static void UpdateTable()
	{
		filelist.sort(new DocComparator());;
		DefaultTableModel tm = (DefaultTableModel) table.getModel();
		tm.setRowCount(0);
		for(Doc d:filelist)
		{
			String[] data = new String[3];
			data[0]=d.getFilename();
			data[1]=Integer.toString(d.getWordCount());
			data[2]=Integer.toString(d.getLineCount());
			tm.addRow(data);
		}
		tm.fireTableDataChanged();
	}
	
	public static int CountWords(String line, String delimiter)
	{
		int count = 0;
		for(String s:line.split(delimiter))
		{
			count++;
		}
		return count;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		if(e.getActionCommand() == "Close")
		{
			System.exit(0);
		}
		
		if(e.getActionCommand() == "Load File")
		{
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
			fc.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fc.showOpenDialog(frame);
			
			if(result == JFileChooser.APPROVE_OPTION)
			{
				for(Doc d:filelist)
				{
					if(d.getFilename().contentEquals(fc.getSelectedFile().getAbsolutePath()))
					{
						filelist.remove(d);
						parseDoc(fc.getSelectedFile().getAbsolutePath());
						UpdateSessionFile();
						return;
					}
				}
				
				Doc res = parseDoc(fc.getSelectedFile().getAbsolutePath());
				if(res != null)
				{
					filelist.add(res);
					UpdateTable();
					UpdateSessionFile();
				}
			}
		}
		
		if(e.getActionCommand() == "Set Delimiter")
		{
			String d = JOptionPane.showInputDialog("Set Delimiter");
			if(d!=null)
				delim = d;
		}
		
		if(e.getActionCommand() == "Remove File")
		{
			String file = JOptionPane.showInputDialog("Enter name of file to remove.");
			if(file != null)
			{
				for(Doc d:filelist)
				{
					if(d.getFilename().contentEquals(file))
						filelist.remove(d);
				}
				UpdateTable();				
			}
		}
		
	}
	
	public static void reloadFiles()
	{
		Stack<String> st = new Stack<String>();
		for(Doc d:filelist)
		{
			st.push(d.getFilepath());
		}
		filelist.clear();
		while(!st.empty())
		{
			parseDoc(st.pop());
		}
		UpdateTable();
	}
	
	public static void UpdateSessionFile()
	{
		try
		{
			PrintWriter pw = new PrintWriter("Session.txt", "UTF-8");
			for(Doc d:filelist)
			{
				pw.write(d.getFilename()+","+
						d.getFilepath()+","+
						Integer.toString(d.getWordCount())+","+
						Integer.toString(d.getLineCount())+"\n");
			}
			pw.close();	
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void LoadSessionFile()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("Session.txt"));
			String line;
			while((line = br.readLine())!= null)
			{
				String[] d = new String[4];
				int x = 0;
				for(String s:line.split(","))
				{
					d[x] = s;
					x++;
				}
				filelist.add(new Doc(d[0],d[1],Integer.parseInt(d[2]),Integer.parseInt(d[3])));
			}
			br.close();
			reloadFiles();
		}
		catch (IOException e)
		{
		}
	}
}
