import java.io.*;
import java.io.StreamTokenizer;
import java.util.*;

public class FileParser {

    String line;
    FileReader inputStream = null;
    String content;
    int index;
    int supreme;
    String filename = "";
    int line1;
    boolean flag;
    ArrayList < Integer > arr;

    public FileParser(String s) throws InterruptedException,
        FileNotFoundException, IOException {
            supreme = -1;
            filename = s;
            content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
            content.trim();
            index = 0;
            line1 = 1;
            flag = false;
            arr = new ArrayList < Integer > (100000);
        }



    public String getPathValue(String path) throws InterruptedException,
        FileNotFoundException, IOException {

            if (supreme == -1) {
                parse();
                if (supreme == 0)
                    return "Invalid JSON Syntax : Please remove the errors from JSON File.";
            }
            if (supreme == 0)
                return "Invalid JSON Syntax : Please remove the errors from JSON File.";



            int i;
            int prev = -1;
            int false_path = 0;
            for (i = 0; i < path.length(); i++) {
                if (path.charAt(i) == '/') {
                    index = content.charAt(0);
                    content = get(path.substring(prev + 1, i));
                    if (content.contains("does not exist in the JSON File")) {
                        false_path = 1;
                        break;
                    }
                    prev = i;
                }
            }

            if (false_path == 1)
                return "Invalid Path : Such a path does not exist in Json File.";

            index = content.charAt(0);
            content = get(path.substring(prev + 1, i));

            return content;
        }




    public String get(String key) throws InterruptedException,
        FileNotFoundException, IOException {

            if (supreme == -1) {
                parse();
                if (supreme == 0)
                    return "Invalid JSON Syntax : Please remove the errors from JSON File.";
            }
            if (supreme == 0)
                return "Invalid JSON Syntax : Please remove the errors from JSON File.";

            int i, j, k, start = 0;
            int ss = 0, se = 0, cs = 0, ce = 0;

            if (content.contains(key)) {
                index = content.indexOf(key);
                for (i = index; i < content.length(); i++) {
                    if (content.charAt(i) == ':') {
                        index++;
                        WhiteSpaces();
                        start = index;
                        break;
                    } else
                        index++;
                }


                if (content.charAt(index) == '"') {
                    index++;
                    while (index < content.length()) {
                        if ((content.charAt(index) == '"') && (content.charAt(index - 1) != '\\'))
                            break;
                        index++;
                    }
                    return content.substring(start, index + 1);
                } else if (content.charAt(index) >= '0' && content.charAt(index) <= '9') {
                    int exp = 0;
                    while ((index < content.length()) && ((content.charAt(index) >= '0' && content.charAt(index) <= '9') ||
                            ((content.charAt(index) == '.' || content.charAt(index) == 'e' || content.charAt(index) == 'E') && exp == 0)
                        )) {
                        if (content.charAt(index) == 'e' || content.charAt(index) == 'E' || content.charAt(index) == '.') {
                            if (content.charAt(index + 1) == '+' || content.charAt(index + 1) == '-')
                                index++;
                            exp = 1;
                        }
                        index++;
                    }
                    index--;
                    return content.substring(start, index + 1);
                } else if (content.substring(index, index + 5).equals("false")) {
                    index += 4;
                    return content.substring(start, index + 1);
                } else if (content.substring(index, index + 4).equals("true") || content.substring(index, index + 4).equals("null")) {
                    index += 3;
                    return content.substring(start, index + 1);
                } else if (content.charAt(index) == '[' || content.charAt(index) == '{') {

                    if (content.charAt(index) == '[')
                        ss++;
                    if (content.charAt(index) == '{')
                        cs++;

                    index++;
                    while (true) {
                        if (content.charAt(index) == '[') {
                            ss++;
                        } else if (content.charAt(index) == ']') {
                            se++;
                        } else if (content.charAt(index) == '{') {
                            cs++;
                        } else if (content.charAt(index) == '}') {
                            ce++;
                        }

                        if (ss == se && cs == ce)
                            break;

                        index++;
                        if (index >= content.length())
                            break;
                    }

                    return content.substring(start, index + 1);
                } else
                    System.out.println("A new unhandled value starting character found****" + content.charAt(index) + "*****");

            }

            return "Key \"" + key + "\" does not exist in the JSON File";



        }


    public void parse() throws InterruptedException,
        FileNotFoundException, IOException {


            int p = 0;
            arr.add(-1);

            while (true) {


                if (content.charAt(index) == '\n') {
                    arr.add(index);
                    p++;
                }
                index++;
                if (index >= content.length())
                    break;
            }

            index = 0;
            ParseN(content.charAt(index), 0);
            if (flag || index < content.length()) {
                line1 = 0;int k;
                for (k = 1; k < p; k++) {
                    if (index > arr.get(k - 1) && index <= arr.get(k))
                        break;
                    else
                        line1++;
                }
                line1++;

                System.out.println("Error in file at line " + line1 + ":" +(index-arr.get(k - 1))+ " at position: \'" + content.substring(index, index + 1) + "\'");
                supreme = 0;
                System.out.println("                                        ^");
            } else {
                String temp=scope_rules();
                if(temp==null)
                {supreme=1;System.out.println("VALID JSON : FILE WAS PARSED SUCCESSFULLY.");
                }
                else
                {
                System.out.println("Error : Duplicate Key \""+temp+"\" found");
                }
                //supreme = 1;
                //System.out.println("VALID JSON : FILE WAS PARSED SUCCESSFULLY.");
            }
        }

    public void WhiteSpaces() {
        if (content.charAt(index) == ' ' || content.charAt(index) == '\n' || content.charAt(index) == '\t') {
            index++;
            while (content.charAt(index) == ' ' || content.charAt(index) == '\n' || content.charAt(index) == '\t') {
                index++;
            }
        }

        for (int i = 2; i < arr.size(); i++) {
            if (index < arr.get(1))
                if (index >= arr.get(i - 1) && index < arr.get(i)) {
                    if (line1 < i)
                        line1++;
                    break;
                }
        }

    }



    public void ParseN(char a, int brace_logic) throws IOException {



        WhiteSpaces();
        if (content.charAt(index) == '"') {
            index++;
            while (index < content.length()) {
                if ((content.charAt(index) == '"') && (content.charAt(index - 1) != '\\'))
                    break;
                index++;
            }
            if (index >= content.length() || content.charAt(index) != '"') {
                flag = true;
                System.out.println("I am getting true here2");
                return;
            }
            index++;


            //brace-logic
            if (brace_logic == 1) {
                WhiteSpaces();

                if (content.charAt(index) == ':') {
                    index++;
                    WhiteSpaces();
                    if (content.length() - 1 >= index) {
                        ParseN(content.charAt(index), 0);
                    } else {
                        flag = true; //System.out.println("I am getting true here3");
                        return;
                    }
                    if (content.length() - 1 >= index)
                        ParseZ(content.charAt(index));
                    else {
                        flag = true; //System.out.println("I am getting true here4");
                        return;
                    }

                } else {
                    flag = true; //System.out.println("I am getting true here5");
                    return;
                }
                brace_logic = 0;
            }
            //close brace logic

        } else if (content.charAt(index) >= '0' && content.charAt(index) <= '9') {
            int exp = 0;
            while ((index < content.length()) && ((content.charAt(index) >= '0' && content.charAt(index) <= '9') ||
                    ((content.charAt(index) == '.' || content.charAt(index) == 'e' || content.charAt(index) == 'E') && exp == 0)
                )) {
                if (content.charAt(index) == 'e' || content.charAt(index) == 'E' || content.charAt(index) == '.') {
                    if (content.charAt(index + 1) == '+' || content.charAt(index + 1) == '-')
                        index++;
                    exp = 1;
                }
                index++;
            }
        } else if (content.substring(index, index + 5).equals("false"))
            index += 5;
        else if (content.substring(index, index + 4).equals("true") || content.substring(index, index + 4).equals("null"))
            index += 4;


        else if (content.charAt(index) == '[') {

            index++;
            if (content.length() - 1 >= index)
                ParseN(content.charAt(index), 0);
            else {
                flag = true; //System.out.println("I am getting true here6");
                return;
            }
            // System.out.println(index);
            if (content.length() - 1 >= index)
                ParseY(content.charAt(index));
            else {
                flag = true; //System.out.println("I am getting true here7");
                return;
            }

        } else if (a == '{') {

            index++;
            WhiteSpaces();
            if (content.charAt(index) == '"') {
                index++;
                while (index < content.length()) {
                    if (content.charAt(index) == '"' && content.charAt(index - 1) != '\\')
                        break;
                    index++;
                }
                if (index >= content.length() || content.charAt(index) != '"') {
                    flag = true;
                    return;
                }
                index++;
                WhiteSpaces();

                if (content.charAt(index) == ':') {
                    index++;
                    WhiteSpaces();
                    if (content.length() - 1 >= index) {
                        ParseN(content.charAt(index), 0);
                    } else {
                        flag = true;
                    return;
                }
                if (content.length() - 1 >= index) {
                    ParseZ(content.charAt(index));
                } else {
                    flag = true; //System.out.println("I am getting true here13");
                    return;
                }

            } else {
                flag = true; //System.out.println("I am getting true here14");
                return;
            }
        }
        //double quote 
        else {
            flag = true; //System.out.println("I am getting true here15");
            return;
        }
    }
    else {
        flag = true; //System.out.println("I am getting true here16"+content.charAt(index)+"**");
        return;
    }

}



public void ParseY(char a) throws IOException {

    WhiteSpaces();

    if (content.charAt(index) == ',') {
        index++;
        if (content.length() - 1 >= index)
            ParseN(content.charAt(index), 0);
        else {
            flag = true;
            System.out.println("I am getting true here20");
            return;
        }
        if (content.length() - 1 >= index)
            ParseY(content.charAt(index));
        else {
            flag = true; //System.out.println("I am getting true here21");
            return;
        }
    } else if (content.charAt(index) == ']') {
        index++;
        return;
    } else {
        flag = true; //System.out.println("I am getting true here22");
        return;
    }

}



public void ParseZ(char a) throws IOException {


    WhiteSpaces();
    if (content.charAt(index) == ',') {
        index++;
        WhiteSpaces();
        if (content.length() - 1 >= index) {
            ParseN(content.charAt(index), 1);
        } else {
            flag = true; //System.out.println("I am getting true here24");
            return;
        }


    } else if (content.charAt(index) == '}') {

        index++;
        return;
    } else {
        flag = true; //System.out.println("I am getting true here26----");
        return;
    }




}


public String scope_rules()
{
int i,j,k;
int included=1;
index=0;
int pp=0;
int start;
int end=0;
int pos=0;
String tt="";	
ArrayList list = new ArrayList();
List<List<String>> arr = new ArrayList<List<String>>();	

	for(i=0;i<10000;i++)
	{
	 arr.add(i, new ArrayList<String>());
	}

	index=0;
	
	
	while(content.charAt(index)!='{')
	{
		index++;
	}
	//System.out.println("First opening brace found at :"+index);
	while(true)
	{	
		WhiteSpaces();		
		if(content.charAt(index)=='"')
		{
			start=index+1;
     			index++;
    			 while(index < content.length()){
				if((content.charAt(index)=='"') && (content.charAt(index-1)!='\\'))
				break;
			index++;
   			}
			end=index;
			index++;
			WhiteSpaces();
			if(content.charAt(index)==':')
			{
				//System.out.println("List="+pos+"   "+content.substring(start,end));
				arr.get(pos).add(content.substring(start,end));
			}
			
		}
		else if(content.charAt(index)=='{')
		{pos++;pp++;}
		else if(content.charAt(index)=='}')
		pos--;
	
		index++;
		if(index==content.length())
		break;
		
	}
	
	ArrayList<String> arg;
	for(i=0;i<pp;i++)
	{
	arg=new ArrayList<String>();	
		for(j=0;j<arr.get(i).size();j++)
		arg.add(arr.get(i).get(j));
		
		tt=duplicates(arg);
		if(tt!=null)
		return tt;
	}
	
	

	return null;
}

String duplicates(ArrayList<String> array)
{
  Set<String> l = new HashSet<String>();
  for (String i : array)
  {
    if (l.contains(i)) return i;
    l.add(i);
  }
  return null;
}


}
//class
