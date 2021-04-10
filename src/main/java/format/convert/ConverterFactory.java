package format.convert;

import format.convert.service.Converter;
import format.convert.service.JsonToXmlConverterImpl;

public class ConverterFactory {

    //use getShape method to get object of type shape
    public Converter getConverter(String convertToType) {
        if (convertToType == null) {
            return null;
        }
        if (convertToType.equalsIgnoreCase("XML")) {
            return new JsonToXmlConverterImpl();

        } else {
            return null;
        }
    }
}
