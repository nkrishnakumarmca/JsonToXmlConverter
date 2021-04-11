# JsonToXmlConverter

JSON TO XML converter

## Pre requisite
 - Java should be installed
 - Git should be installed
 - Maven should be installed

## Installation step
-  Navigate to your workspace in terminal
- Type the command 
` git clone https://github.com/nkrishnakumarmca/JsonToXmlConverter.git `
- After successful download navigate to JsonToXmlConverter directory and  type the command 

    ` cd JsonToXmlConverter `
    
    ` mvn clean compile assembly:single `
- Navigate to target folder  using cd target command
-Then run this command 
` java -jar target/jsonToXmlConverter-jar-with-dependencies.jar <your input file path with file name> <your output file path with name> `
