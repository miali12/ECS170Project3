import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Reader{

	class Node
	{//Structure for every hidden-layer instance
	  double sum;
	  double sigmoidSum;
	  double delta;
	}
	static int HEIGHT = 128;
	static int WIDTH = 120;
	static int NUMNODES = 32;

	static File[] maleFiles, femaleFiles, testFiles;
	static ArrayList<String> allFiles = new ArrayList<String>();
	static ArrayList<Double> inputPixels = new ArrayList<Double>();

	//Size of 128*120*32 - weights between input layer and hidden layer
	static ArrayList<Double> firstWeights = new ArrayList<Double>();
	//Size of 32 - weights between hidden layer and output layer
	static ArrayList<Double> secondWeights = new ArrayList<Double>();
	//Size of 32 - stores all hidden layer nodes
	static ArrayList<Node> hiddenNodes = new ArrayList<Node>();

	private static double sigmoidFunction(int x)
	{
		return (1/(1+Math.pow(Math.E,-x)));
	}

	private static void storeFiles()
	{//Store the files in the appropriate data structure
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

	private static void gaussianWeights()
	{//Assign random numbers to the weights data structures
	  firstWeights.clear();
	  secondWeights.clear();
	  Random r = new Random();
	  for(int i = 0; i < HEIGHT*WIDTH*NUMNODES; i++)
		firstWeights.add(r.nextGaussian());
	  for(int i = 0; i < NUMNODES; i++)
		secondWeights.add(r.nextGaussian());
		
	}

	private static void processInput(Scanner s)
	{
		  for(int i = 0; i < HEIGHT*WIDTH; i++)
		      if(s.hasNextDouble())
			inputPixels.add(s.nextDouble());
	}

	public static void main(String[] args) throws Exception
	{
		storeFiles();

		gaussianWeights();
		try
		{
		  File f = new File(allFiles.get(0));
		  Scanner fileScanner = new Scanner(f);
		  processInput(fileScanner);
		}
		catch(Exception ex)
		{
		  System.err.println("File not found. Abort.\n");
		  System.exit(1);
		}
	}
}











