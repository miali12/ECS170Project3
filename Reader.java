import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Reader{

	static int HEIGHT = 128;
	static int WIDTH = 120;
	final static int[][] edgeDetector =
		{{0, 1, 0},
		{1, -4, 1},
		{0, 1, 0}};

	static File[] maleFiles, femaleFiles, testFiles;
	static ArrayList<String> allFiles = new ArrayList<String>();
	static ArrayList<Double> inputPixels = new ArrayList<Double>();
	static ArrayList<Double> edgePixels = new ArrayList<Double>();
	static ArrayList<Double> poolingPixels = new ArrayList<Double>();

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

	private static void convolutionLayer()
	{//Perform matrix multiplication to each 3x3 input sub-matrix to filter out the edges
		double sum;
		boolean nextCol;
		boolean nextRow;
		edgePixels.clear();
		for(int i = 0; i < HEIGHT; i++)
		{
		  for(int j = 0; j < WIDTH; j++)
		  {
		    sum = 0;
		    nextCol = nextRow = false;
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
				sum += inputPixels.get((i*WIDTH)+j)*edgeDetector[y][x];
			}
			if(nextRow)
			  break;
		    }
		    if(nextCol || nextRow)
			break;
		    else
		    	edgePixels.add(sum);
		  }
		}
		HEIGHT -= 2;
		WIDTH -= 2;
	}

	private static void ReLULayer()
	{//Set every negative value to 0
		for(int i = 0; i < edgePixels.size(); i++)
		  if(edgePixels.get(i) < 0)
			edgePixels.set(i, 0.0);
	}

	private static void poolingLayer()
	{//Filter each location with a 2x2 matrix that takes the max value within that domain
		boolean nextCol, nextRow;
		double max;
		poolingPixels.clear();
		for(int i = 0; i < HEIGHT; i++)
		{
		  for(int j = 0; j < WIDTH; j++)
		  {
		    max = -9000;
		    nextCol = nextRow = false;
		    for(int y = 0; y < 2; y++)
		    {
		      if(i+y >= HEIGHT)
		      {
			nextCol = true;
			break;
		      }
		      for(int x = 0; x < 2; x++)
		      {
			if(j+x >= WIDTH)
			{
			  nextRow = true;
			  break;
			}
			if(edgePixels.get((i*WIDTH)+j) > max)
			  max = edgePixels.get((i*WIDTH)+j);
		      }
		      if(nextRow)
			break;
		    }
		    if(nextCol || nextRow)
			break;
		    else
		    	poolingPixels.add(max);
		  }
		}

		HEIGHT -= 1;
		WIDTH -= 1;

		//Transfer over the pooling array to initial array for the next iteration
		int offset = inputPixels.size() - poolingPixels.size();
		while(offset-- > 0)
		  inputPixels.remove(inputPixels.size()-1);

		for(int i = 0; i < HEIGHT; i++)
		  for(int j = 0; j < WIDTH; j++)
		    inputPixels.set((i*WIDTH)+j, poolingPixels.get((i*WIDTH)+j));
	}

	private static void printFirstHiddenLayer() throws IOException
	{
		try
		{
			File file = new File("./HiddenLayer.txt");
			FileWriter writer = new FileWriter(file);
			for(int i = 0; i < HEIGHT-2; i++)
			{
			  for(int j = 0; j < WIDTH-2; j++)
			    writer.write(edgePixels.get(i*(WIDTH-2)+j) + " ");
			  writer.write("\n");
			}
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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

		int i = 10;
		while(i-- > 0)
		{
			convolutionLayer();
			ReLULayer();
			poolingLayer();
		}
	}
}











