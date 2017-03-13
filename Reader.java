import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Reader{

	final static int HEIGHT = 128;
	final static int WIDTH = 120;

	private double sigmoidFunction(int x)
	{
		return (1/(1+Math.pow(Math.E,-x)));
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		String str;
		File[] maleFiles, femaleFiles, testFiles;
		File m = null, f = null, t = null;
		ArrayList<String> allFiles = new ArrayList<String>();

		m = new File("./Male/");
		maleFiles = m.listFiles();
		f = new File("./Female/");
		femaleFiles = f.listFiles();
		t = new File("./Test/");
		testFiles = t.listFiles();

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

		for(File i : testFiles)
		{
		  str = ("./Test/");
		  str = str + i.getName();
		  allFiles.add(str);
		}

		List<List<Double>> pixels = new ArrayList<List<Double>>(HEIGHT);
		for(int i = 0; i < HEIGHT; i++)
		  pixels.add(new ArrayList<Double>(WIDTH));
		try
		{
		  //Testing to see if input file is stored in 2D ArrayList
		  Scanner fileScanner = new Scanner(new File(allFiles.get(0)));
		  for(int i = 0; i < HEIGHT; i++)
		  {
		    for(int j = 0; j < WIDTH; j++)
		      if(fileScanner.hasNextDouble())
		      {
			pixels.get(i).add(fileScanner.nextDouble());
			System.out.print(pixels.get(i).get(j) + " ");
		      }
		    System.out.println();
		  }
		}
		catch(FileNotFoundException ex)
		{

		}
		
	}
}
