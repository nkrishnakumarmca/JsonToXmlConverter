package format.convert;

public class XMLGeneratorMain {

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new IllegalArgumentException("Please provide the required inputs");
        }
        System.out.println("Json to Xml conversation started...");
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        ConverterFactory converterFactory = new ConverterFactory();

        converterFactory.getConverter("XML").xmlJsonConverter(inputFilePath, outputFilePath);
        System.out.println("Json to Xml conversation completed...");
    }
}
