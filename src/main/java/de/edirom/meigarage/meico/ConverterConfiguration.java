package de.edirom.meigarage.meico;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.util.ArrayList;
import java.util.List;

public class ConverterConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(MeicoConverter.class);

    public static final List<ConversionActionArguments> CONVERSIONS = new ArrayList<ConversionActionArguments>();
    public static final String PROFILE_KEY = "de.edirom.meigarage.lilypond.profileNames";

    public static final String SCOREFAMILY = "Scores";
    public static final String SCOREFAMILYCODE = "score";

    public static final String DEFAULTFAMILY = "Other documents";

    static {
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOMSM));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOMPM));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOMIDIMEICO));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MIDITOMSM));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MIDITOMP3));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI30TOMSM));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI30TOMPM));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI30TOMIDI));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MIDIMEICOTOMP3));
        CONVERSIONS.add(getConversionActionArgument(Conversion.MEI40TOMP3));
    }

    private static ConversionActionArguments getConversionActionArgument(Conversion format) {

        StringBuffer sbParams = new StringBuffer();
        sbParams.append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">");
        sbParams.append("<properties>");
        sbParams.append("<entry key=\"");
        sbParams.append(PROFILE_KEY);
        sbParams.append("\">");
        sbParams.append("default");
        if (sbParams.charAt(sbParams.length() - 1) == ',') sbParams.deleteCharAt(sbParams.length() - 1);
        sbParams.append("</entry><entry key=\"" + PROFILE_KEY + ".type\">array</entry>");
        sbParams.append("</properties>");

        ConversionActionArguments caa = new ConversionActionArguments(
                new DataType(format.getIFormatId(), format.getIMimeType(), format.getInputDescription(),
                        getType(format.getInputType())),
                new DataType(format.getOFormatId(), format.getOMimeType(),
                        format.getOutputDescription(), getType(format.getOutputType())),
                sbParams.toString(), format.getVisible(), format.getCost());

        return caa;
    }

    private static String getType(String typeCode) {
        if (typeCode.equals(SCOREFAMILYCODE))
            return SCOREFAMILY;

        return DEFAULTFAMILY;
    }
}
