import java.io.*;
import java.io.StreamTokenizer;
import java.util.*;

public class User{

public static void main(String[] args)throws Exception
{
	FileParser fp=new FileParser("input.txt");
	fp.parse();
	
	System.out.println(fp.get("GlossList"));
	System.out.println();System.out.println();
	System.out.println(fp.getPathValue("glossary/GlossDiv/title"));
	
}


}
