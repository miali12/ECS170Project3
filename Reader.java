import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Reader{

	final static int HEIGHT = 128;
	final static int WIDTH = 120;
	final static int[][] edgeDetector =
		{{0, 1, 0},
		{1, -4, 1},
		{0, 1, 0}};

	static File[] maleFiles, femaleFiles, testFiles;
	static ArrayList<String> allFiles = new ArrayList<String>();
	static List<List<Double>> inputPixels = new ArrayList<List<Double>>(HEIGHT);
	static ArrayList<Double> hiddenPixels = new ArrayList<Double>();

	private static double sigmoidFunction(int x)
	{
		return (1/(1+Math.pow(Math.E,-x)));
	}

	private static void storeFiles()
	{
		String str;
		File m = null, f = null, t = null;

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
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		storeFiles();

		//Take the input from a file into a 2D ArrayList
		for(int i = 0; i < HEIGHT; i++)
		  inputPixels.add(new ArrayList<Double>(WIDTH));
		try
		{
		  Scanner fileScanner = new Scanner(new File(allFiles.get(0)));
		  for(int i = 0; i < HEIGHT; i++)
		  {
		    for(int j = 0; j < WIDTH; j++)
		      if(fileScanner.hasNextDouble())
		      {
			inputPixels.get(i).add(fileScanner.nextDouble());
			//System.out.print(inputPixels.get(i).get(j) + " ");
		      }
		    //System.out.println();
		  }
		}
		catch(FileNotFoundException ex)
		{

		}

		//Perform matrix multiplication to each 3x3 input sub-matrix to filter out the edges
		double sum;
		boolean nextCol;
		boolean nextRow;
		for(int i = 0; i < HEIGHT; i++)
		{
		  for(int j = 0; j < WIDTH; j++)
		  {
		    sum = 0;
		    nextCol = false;
		    nextRow = false;
		    for(int y = 0; y < 3; y++)
		    {
			if(i+y >= HEIGHT)
			{
			  nextCol = true;
			  break;
			}
			for(int x = 0; x < 3; x++)
			{
				if(j+x >= WIDTH)
				{
				  nextRow = true;
				  break;
				}
				sum += inputPixels.get(i+y).get(j+x)*edgeDetector[y][x];
			}
		    }
		    hiddenPixels.add(sum);
		  }
		}

		System.out.println("Size: " + hiddenPixels.size());
		System.out.println("Size: " + inputPixels.size()*inputPixels.get(0).size());

	}
}











