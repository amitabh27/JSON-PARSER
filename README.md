# JSON-PARSER
The repository has a JSON Parser that validates a JSON file and shows errors if any with proper exception handling.
Also it has built in methods to return JSON object for a specified KEY or Qualified Path.


KEY Features :

1.It uses JSON Grammer to validate the input Json File.
2. Displays line and column number and the poistion in the file where error occured.
3. All the primitive data types have been handled.
4. Scope rules have also been implemented as in keys in a given scope cannot be same
5. It also returns JSON object for a specified key or a valid key hierarchy.


/*
FileParser has built in methods to parse the JSON input file.

How to use the JSON Parser:

1. Create an object of class FileParse and pass the name of Json File as a string to its constructor.
2. The method parse() of FileParser parses the Json File.
objectName.parse();

3. Other Methods:
objectName.get(keyName); // Returns the value of specified keyName
objectName.getPathValue(key1/key2/key3); //Return the value of the key3 specified using key hierarchy.

*/
