package de.edirom.meigarage.meico;

import meico.audio.Audio;
import meico.midi.Midi;
import meico.supplementary.KeyValue;
import nu.xom.ParsingException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.SAXException;
import pl.psnc.dl.ege.component.Converter;
import pl.psnc.dl.ege.configuration.EGEConfigurationManager;
import pl.psnc.dl.ege.configuration.EGEConstants;
import pl.psnc.dl.ege.exception.ConverterException;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;
import pl.psnc.dl.ege.utils.EGEIOUtils;
import pl.psnc.dl.ege.utils.IOResolver;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import meico.mei.Mei;
import meico.mpm.Mpm;
import meico.msm.Msm;

import javax.sound.midi.InvalidMidiDataException;
import javax.xml.parsers.ParserConfigurationException;

public class MeicoConverter implements Converter {

    private static final Logger LOGGER = LogManager.getLogger(MeicoConverter.class);
    private static IOResolver ior = EGEConfigurationManager.getInstance()
            .getStandardIOResolver();

    public void convert(InputStream inputStream, OutputStream outputStream, ConversionActionArguments conversionDataTypes) throws ConverterException, IOException {
        convert(inputStream, outputStream, conversionDataTypes, null);
    }

    public void convert(InputStream inputStream, OutputStream outputStream, ConversionActionArguments conversionDataTypes, String tempDir) throws ConverterException, IOException {
        boolean found = false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        for (ConversionActionArguments cadt : ConverterConfiguration.CONVERSIONS) {
            if (conversionDataTypes.equals(cadt)) {
                String profile = cadt.getProperties().get(ConverterConfiguration.PROFILE_KEY);
                LOGGER.info(dateFormat.format(date) + ": Converting FROM:  "
                        + conversionDataTypes.getInputType().toString()
                        + " TO "
                        + conversionDataTypes.getOutputType().toString()
                        + " WITH profile " + profile);
                convertDocument(inputStream, outputStream, cadt.getInputType(), cadt.getOutputType(),
                        cadt.getProperties(), tempDir);
                found = true;
            }
        }
        if (!found) {
            throw new ConverterException(
                    ConverterException.UNSUPPORTED_CONVERSION_TYPES);
        }

    }

    /*
     * Prepares transformation : based on MIME type.
     */
    private void convertDocument(InputStream inputStream, OutputStream outputStream,
                                 DataType fromDataType, DataType toDataType, Map<String, String> properties, String tempDir) throws IOException,
            ConverterException {

        // MEI40TOMSM
        if (fromDataType.getFormat().equals(Conversion.MEI40TOMSM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI40TOMSM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei40", "msm", properties, tempDir);

        }
        // MEI40TOMPM
        else if (fromDataType.getFormat().equals(Conversion.MEI40TOMPM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI40TOMPM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei40", "mpm", properties, tempDir);

        }
        // MEI 4.0 to MIDI
        else if (fromDataType.getFormat().equals(Conversion.MEI40TOMIDIMEICO.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI40TOMIDIMEICO.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei40", "midi", properties, tempDir);

        }
        // MEI4.0 to MP3 MEI40TOMP3
        else if(fromDataType.getFormat().equals(Conversion.MEI40TOMP3.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI40TOMP3.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei40", "mp3", properties, tempDir);
        }
        // MIDITOMSM
        else if (fromDataType.getFormat().equals(Conversion.MIDITOMSM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MIDITOMSM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "midi", "msm", properties, tempDir);

        }
        //MIDITOMP3
        else if (fromDataType.getFormat().equals(Conversion.MIDITOMP3.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MIDITOMP3.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "midi", "mp3", properties, tempDir);

        }
        //MIDIMEICOTOMP3
        else if (fromDataType.getFormat().equals(Conversion.MIDIMEICOTOMP3.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MIDIMEICOTOMP3.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "midi", "mp3", properties, tempDir);

        }
        //MEI30TOMSM
        else if (fromDataType.getFormat().equals(Conversion.MEI30TOMSM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI30TOMSM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei30", "msm", properties, tempDir);

        }
        //MEI30TOMPM
        else if (fromDataType.getFormat().equals(Conversion.MEI30TOMPM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI30TOMPM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei30", "mpm", properties, tempDir);

        }
        //MEI30TOMIDI
        else if (fromDataType.getFormat().equals(Conversion.MEI30TOMPM.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI30TOMPM.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei30", "midi", properties, tempDir);

        }
        // MEI3.0 to MP3 MEI30TOMP3
        else if(fromDataType.getFormat().equals(Conversion.MEI30TOMP3.getIFormatId()) &&
                toDataType.getFormat().equals(Conversion.MEI30TOMP3.getOFormatId())) {
            performMeicoTransformation(inputStream, outputStream, "mei30", "mp3", properties, tempDir);
        }
    }

    private void performMeicoTransformation(InputStream inputStream, OutputStream outputStream,
                                            String inputFormat, String outputFormat,
                                            Map<String, String> properties, String tempDir) throws IOException, ConverterException {

        File inTmpDir = null;
        File outTempDir = null;
        try {
            inTmpDir = prepareTempDir(tempDir);
            ior.decompressStream(inputStream, inTmpDir);
            // avoid processing files ending in .bin
            File inputFile = searchForData(inTmpDir, "^.*(?<!bin)$");
            outTempDir = prepareTempDir(tempDir);
            if (inputFile != null) {
                //String newFileName = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf(".")) + ".ly";
                //inputFile.renameTo(new File(newFileName));
                if (inputFormat.equals("mei40") || inputFormat.equals("mei30")) {
                    Mei mei = new Mei(inputFile);
                    KeyValue<List<Msm>, List<Mpm>> msmpms = mei.exportMsmMpm(720);    // usually, the application should use mei.exportMsm(720); the cleanup flag is just for debugging (in debug mode no cleanup is done)
                    if (msmpms.getKey().isEmpty()) {
                        LOGGER.error("MSM and MPM data could not be created.");
                    }
                    if (outputFormat.equals("msm")) {
                        for (Msm msm1 : msmpms.getKey()) {
                            msm1.removeRests();  // purge the data (some applications may keep the rests from the mei; these should not call this function)
                            msm1.writeMsm(outTempDir.getPath() + "/" + msm1.getFile().getName());                 // write the msm file to the file system
                        }
                    } else if (outputFormat.equals("mpm")) {
                        for (Mpm mpm1 : msmpms.getValue()) {
                            mpm1.writeMpm(outTempDir.getPath() + "/" + mpm1.getFile().getName());                 // write the mpm file to the file system
                        }
                    } else if (outputFormat.equals("midi")) {
                        List<meico.midi.Midi> midis = new ArrayList<>();
                        for (int i = 0; i < msmpms.getKey().size(); ++i) {
                            midis.add(msmpms.getKey().get(i).exportExpressiveMidi(msmpms.getValue().get(i).getPerformance(0)));    // convert msm + mpm to expressive midi;
                        }
                        for (int i = 0; i < midis.size(); ++i) {
                            midis.get(i).writeMidi(outTempDir.getPath() + "/" + midis.get(i).getFile().getName());   // write midi file to the file system
                        }
                    }
                    else if (outputFormat.equals("mp3")) {
                            List<meico.midi.Midi> midis = new ArrayList<>();
                            for (int i = 0; i < msmpms.getKey().size(); ++i) {
                                midis.add(msmpms.getKey().get(i).exportExpressiveMidi(msmpms.getValue().get(i).getPerformance(0)));    // convert msm + mpm to expressive midi;
                            }
                            for (int i = 0; i < midis.size(); ++i) {
                                Audio audio = midis.get(i).exportAudio();
                                audio.writeMp3(outTempDir.getPath() + "/" + audio.getFile().getName().substring(0, audio.getFile().getName().lastIndexOf(".")) + ".mp3");   // write midi file to the file system
                            }
                        } else {
                        LOGGER.error("Output format" + outputFormat + "not available");
                    }
                } else if (inputFormat.equals("midi")) {
                    Midi midi = new Midi(inputFile);
                    if (outputFormat.equals("msm")) {
                        Msm msm = midi.exportMsm();
                        msm.writeMsm(outTempDir.getPath() + "/" + msm.getFile().getName());
                    } else if (outputFormat.equals("mp3")) {
                        Audio audio = midi.exportAudio();   // mittels exportAudio(soundbank) kannst Du auch andere Instrumentenklänge verwenden
                        byte[] mp3 = audio.getAudioAsMp3();
                        String path = outTempDir.getPath() + "/" + audio.getFile().getName().substring(0, audio.getFile().getName().lastIndexOf(".")) + ".mp3";
                        //audio.writeMp3(path);
                        //not using meico writing method for audio because it caused errors on rancher setup
                        File outputFile = new File(path);
                        LOGGER.info("starting to write audio file to disk");
                        try (FileOutputStream outputStream2 = new FileOutputStream(outputFile)) {
                            outputStream2.write(mp3);
                        }
                        LOGGER.info("written audio file to " + path);
                    } else {
                        LOGGER.error("Output format" + outputFormat + "not available");
                    }
                }
            } else {
                LOGGER.error("Input format" + inputFormat + "not available");
            }
            ior.compressData(outTempDir, outputStream);
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } finally {
            if (outTempDir != null && outTempDir.exists())
                //EGEIOUtils.deleteDirectory(outTempDir)
                ;
            if (inTmpDir != null && inTmpDir.exists())
                EGEIOUtils.deleteDirectory(inTmpDir)
                ;
        }
    }

    private File prepareTempDir() {
        return prepareTempDir(null);
    }

    private File prepareTempDir(String tempDir) {
        File inTempDir = null;
        String uid = UUID.randomUUID().toString();
        if (tempDir != null) {
            inTempDir = new File(tempDir + File.separator + uid
                    + File.separator);
        } else {
            inTempDir = new File(EGEConstants.TEMP_PATH + File.separator + uid
                    + File.separator);
        }
        inTempDir.mkdir();
        return inTempDir;
    }

    /*
     * Search for specified by regex file
     */
    private File searchForData(File dir, String regex) {
        for (File f : dir.listFiles()) {
            if (!f.isDirectory() && Pattern.matches(regex, f.getName())) {
                return f;
            } else if (f.isDirectory()) {
                File sf = searchForData(f, regex);
                if (sf != null) {
                    return sf;
                }
            }
        }
        return null;
    }


    public List<ConversionActionArguments> getPossibleConversions() {
        return ConverterConfiguration.CONVERSIONS;
    }
}
