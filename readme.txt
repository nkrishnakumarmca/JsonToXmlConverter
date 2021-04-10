JSON TO XML converter

Pre requsitie
 1. Java should be installed
 2. Git should be installed
 3. Maven should be installed

Installation step
Step 1: Navigate to your workspace in terminal
Step 2: Type the command git clone https://github.com/nkrishnakumarmca/JsonToXmlConverter.git
Step 3: After successful download navigate to JsonToXmlConverter directory and  type the command mvn clean compile assembly:single
step 4: Navigate to target folder  using cd target command
step 5: Then run this command java -jar target/jsonToXmlConverter-jar-with-dependencies.jar <your input file path with file name> <your output file path>
