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
	static ArrayList<Double> edgePixels = new ArrayList<Double>();
	static ArrayList<Double> poolingPixels = new ArrayList<Double>();

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

	private static void convolutionLayer()
	{

		//Perform matrix multiplication to each 3x3 input sub-matrix to filter out the edges
		//Produces matrix of dimensions 126x118
		double sum;
		boolean nextCol;
		boolean nextRow;
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
				sum += inputPixels.get(i+y).get(j+x)*edgeDetector[y][x];
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
	}

	private static void ReLULayer()
	{//Set every negative value to 0
		for(int i = 0; i < edgePixels.size(); i++)
		  if(edgePixels.get(i) < 0)
			edgePixels.set(i, 0.0);
	}

	private static void poolingLayer()
	{
		int height = HEIGHT-2;
		int width = WIDTH-2;
		boolean nextCol, nextRow;
		double max;
		for(int i = 0; i < height; i++)
		{
		  for(int j = 0; j < width; j++)
		  {
		    max = -9000;
		    nextCol = nextRow = false;
		    for(int y = 0; y < 2; y++)
		    {
		      if(i+y >= height)
		      {
			nextCol = true;
			break;
		      }
		      for(int x = 0; x < 2; x++)
		      {
			if(j+x >= width)
			{
			  nextRow = true;
			  break;
			}
			if(edgePixels.get((i*width)+j) > max)
			  max = edgePixels.get((i*width)+j);
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
		  for(int i = 0; i < HEIGHT; i++)
		  {
		    for(int j = 0; j < WIDTH; j++)
		      if(s.hasNextDouble())
			inputPixels.get(i).add(s.nextDouble());
		  }

	}
	public static void main(String[] args) throws Exception
	{
		storeFiles();	
		for(int i = 0; i < HEIGHT; i++)
		  inputPixels.add(new ArrayList<Double>(WIDTH));

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
		convolutionLayer();
		ReLULayer();
		poolingLayer();
	}
}











