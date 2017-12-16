import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Scrapes a .java file for comments and prints them onto a text file with the same name .txt
 * @author Jake Tran
 *
 */

public class CScrape
{

	public static void main(String[] args)
	{
		System.out.println("Enter file name: ");
		Scanner keyboard = new Scanner(System.in);
		String fileName = keyboard.next();
		String outputFileName = getFileName(fileName);

		Scanner inputStream = null;
		PrintWriter outputStream = null;
		
		try
		{
			inputStream = new Scanner(new File(fileName));
			outputStream = new PrintWriter(outputFileName);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error opening the file " + fileName);
			System.exit(0);
		}
		//add permission exception catch
		
		printComments(inputStream, outputStream);
		
		
		outputStream.close();
		inputStream.close();
		keyboard.close();
		System.out.println("Comments from " + fileName + " were printed to " + outputFileName);
	}
	
	/*
	 * if directory is entered, returns the name of the file w/o .java extension
	 */
	public static String getFileName(String directory)
	{
		if (directory.contains("\\"))
		{
			String[] directoryBeforePeriod = directory.split("\\."); // splits name at '.' period
			String outputFileName = directoryBeforePeriod[0] + ".txt";
			return outputFileName;
		}
		else
			return directory;

	}

	/*
	 * Prints comments
	 */
	public static void printComments(Scanner input, PrintWriter output)
	{
		int count = 1;
		while (input.hasNextLine())
		{
			String line = input.nextLine();

			if(line.contains("/*") && (line.contains("*/")) && line.contains("//")) // a "/* */" and "//" comment on the same line
			{
				int firstStar = line.indexOf('*');
				int secondStar = line.indexOf('*', firstStar + 1);
				output.print(count + ": /" + line.substring(firstStar, secondStar) + "*/");
				String[] comment = line.split("//");
				output.println(" //" + comment[1]);
			}
			else if (line.contains("//")) // if line contains a // comment
			{
				String[] comment = line.split("//");
				output.println(count + ": //" + comment[1]);
			}
			else if (line.contains("/*") && (line.contains("*/"))) // if line contains a /* */ comment on the same line
			{
				int firstStar = line.indexOf('*');
				int secondStar = line.indexOf('*', firstStar + 1);
				output.println(count + ": /" + line.substring(firstStar, secondStar) + "*/");
			}
			else if (line.contains("/*") || line.contains("*/")) // if a line contains /* or */ on the same line
			{
				int slash = line.indexOf('/');
				int star = line.indexOf('*');
				if (star == (slash + 1)) // "/*" case
					output.println(count + ": " + line.substring(slash));
				else if (star == (slash - 1)) // "*/" case
				{
					String trimmedLine = line.trim();
					output.println(count + ": " + trimmedLine.substring(0, trimmedLine.length()));
				}
			}
			else if (line.contains("*")) // if line contains just * as a part of a multi line comment
			{
				int star = line.indexOf('*');
				output.println(count + ": " + line.substring(star));
			}
			
			count++;
		}
	}
}
