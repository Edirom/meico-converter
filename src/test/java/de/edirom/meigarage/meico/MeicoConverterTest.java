package de.edirom.meigarage.meico;

import pl.psnc.dl.ege.configuration.EGEConfigurationManager;
import pl.psnc.dl.ege.exception.ConverterException;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MeicoConverterTest {
    private MeicoConverter converter;

    @org.junit.Before
    public void setUp() throws Exception {
        converter = new MeicoConverter();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        converter = null;
    }

    @org.junit.Test
    public void convert() throws IOException, ConverterException {
        InputStream is = new FileInputStream("src/test/resources/test-input.mei.zip");
        OutputStream os = new FileOutputStream("src/test/resources/test-output.mpm.zip");
        DataType inputType = new DataType("mei40", "text/xml");
        //DataType inputType = new DataType("midi", "audio/x-midi");
        DataType outputType = new DataType("mpm", "text/xml");
        //DataType outputType = new DataType("mp3", "audio/mp3");
        ConversionActionArguments conversionActionArguments = new ConversionActionArguments(inputType, outputType, null);
        String tempDir = "src/test/temp";
        converter.convert(is, os, conversionActionArguments, tempDir);
        //assertNotNull(new File("src/test/resources/test-output.mp3.zip"));
        assertNotNull(new File("src/test/resources/test-output.mpm.zip"));
        InputStream isout = new FileInputStream("src/test/resources/test-output.mpm.zip");
        EGEConfigurationManager.getInstance().getStandardIOResolver().decompressStream(isout, new File("src/test/resources/test-output.mpm"));
        //assertNotEquals("", new String(Files.readAllBytes(Paths.get("src/test/resources/test-output.mpm/test2-input.mpm")), "UTF-8"));
        //assertArrayEquals("Binary files differ",
        //        Files.readAllBytes(Paths.get("src/test/resources/expected-output.mp3")),
        //        Files.readAllBytes(Paths.get("src/test/resources/test-output.mp3/test2-input_MEI export performance.mp3")));
        assertEquals("The files differ!",
                new String(Files.readAllBytes(Paths.get("src/test/resources/test-output.mpm/test2-input.mpm"))).replaceAll("uri=\"[\\s\\S]*?\"",""),
                new String(Files.readAllBytes(Paths.get("src/test/resources/expected-output.mpm"))).replaceAll("uri=\"[\\s\\S]*?\"",""));
        is.close();
        os.close();
        isout.close();
    }

    @org.junit.Test
    public void getPossibleConversions() {
        assertNotNull(converter.getPossibleConversions());
        System.out.println(converter.getPossibleConversions());
    }
}