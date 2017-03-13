import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Reader{

	final int HEIGHT = 128;
	final int WIDTH = 120;
	public static void main(String[] args) throws FileNotFoundException
	{
		String str;
		File[] maleFiles, femaleFiles;
		File m = null, f = null;
		ArrayList<String> allFiles = new ArrayList<String>();

		m = new File("./Male/");
		maleFiles = m.listFiles();
		f = new File("./Female/");
		femaleFiles = f.listFiles();

		for(File i : maleFiles)
		{
		  if(i.getName().charAt(0) != 'a')
		  {
		    str = ("./Male/");
		    str = str + i.getName();
		    allFiles.add(str);
		  }
		}

		for(File i : femaleFiles)
		{
		  if(i.getName().charAt(0) != 'b')
		  {
		    str = ("./Female/");
		    str = str + i.getName();
		    allFiles.add(str);
		  }
		}

		List<List<Integer>> integers = new ArrayList<List<Integer>>(HEIGHT);
		for(int i = 0; i < HEIGHT; i++)
		  integers.add(new ArrayList<Integer>(WIDTH));
		try
		{
		  //Testing to see if input file is stored in 2D ArrayList
		  Scanner fileScanner = new Scanner(allFiles.get(0));
		  for(int i = 0; i < HEIGHT; i++)
		    for(int j = 0; j < WIDTH; j++)
		      if(fileScanner.hasNextInt())
			integers.get(i).add(fileScanner.nextInt());
		}
		catch(FileNotFoundException ex)
		{

		}
		
	}
}
