package de.edirom.meigarage.meico;

public enum Conversion {

    /*
      supported formats
     */
    MEI40TOMSM(
            "mei40tomsm", // "id"
            "text/xml", // "input mime type"
            "mei40", // "input format id"
            "MEI 4.0 (2018) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output mime type"
            "msm", // "output format id"
            "MSM (MSM)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MEI40TOMPM(
            "mei40tompm", // "id"
            "text/xml", // "input mime type"
            "mei40", // "input format id"
            "MEI 4.0 (2018) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output mime type"
            "mpm", // "output format id"
            "Music Performance Markup (MPM)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MEI40TOMIDI(
            "mei40tomidimeico", // "id"
            "text/xml", // "input mime type"
            "mei40", // "input format id"
            "MEI 4.0 (2018) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "audio/x-midi", // "output mime type"
            "midi-meico", // "output format id"
            "Musical Instrument Digital Interface (MIDI) (MEICO)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MIDITOMSM(
            "miditomsm", // "id"
            "audio/x-midi", // "input mime type"
            "midi", // "input format id"
            "Musical Instrument Digital Interface (MIDI)", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output mime type"
            "msm", // "output format id"
            "Musical Sequence Markup (MSM)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MIDITOMP3(
            "miditomp3", // "id"
            "audio/x-midi", // "input mime type"
            "midi", // "input format id"
            "Musical Instrument Digital Interface (MIDI)", // "input description"
            "audio", // "input type" (score, audio, image, customization)
            "audio/mp3", // "output mime type"
            "mp3", // "output format id"
            "MP3 audio file (MP3)", // "output description"
            "audio", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MEI30TOMSM(
            "mei30tomsm", // "id"
            "text/xml", // "input mime type"
            "mei30", // "input format id"
            "MEI 3.0 (2016) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output mime type"
            "msm", // "output format id"
            "MSM (MSM)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MEI30TOMPM(
            "mei30tompm", // "id"
            "text/xml", // "input mime type"
            "mei30", // "input format id"
            "MEI 3.0 (2016) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "text/xml", // "output mime type"
            "mpm", // "output format id"
            "Music Performance Markup (MPM)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    ),
    MEI30TOMIDI(
            "mei30tomidimeico", // "id"
            "text/xml", // "input mime type"
            "mei30", // "input format id"
            "MEI 3.0 (2016) Document", // "input description"
            "score", // "input type" (score, audio, image, customization)
            "audio/x-midi", // "output mime type"
            "midi-meico", // "output format id"
            "Musical Instrument Digital Interface (MIDI) (MEICO)", // "output description"
            "score", // "output type" (score, audio, image, customization)
            true, // "visible as input"
            9 // "cost"
    );

    // abc, darms, mei, pae, xml
    // mei, svg, or midi


    private String id;
    private String iMimeType;
    private String iFormatId;
    private String iDescription;
    private String iType;
    private String oMimeType;
    private String oFormatId;
    private String oDescription;
    private String oType;
    private boolean visible;
    private int cost;

    Conversion(String id, String iMimeType, String iFormatId, String iDescription, String iType, String oMimeType, String oFormatId, String oDescription, String oType, boolean visible, int cost) {
        this.id = id;
        this.iMimeType = iMimeType;
        this.iFormatId = iFormatId;
        this.iDescription = iDescription;
        this.iType = iType;
        this.oMimeType = oMimeType;
        this.oFormatId = oFormatId;
        this.oDescription = oDescription;
        this.oType = oType;
        this.visible = visible;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public String getIMimeType() {
        return iMimeType;
    }

    public String getOMimeType() {
        return oMimeType;
    }

    public String getOFormatId() {
        return oFormatId;
    }

    public String getIFormatId() {
        return iFormatId;
    }

    public String getInputDescription() {
        return iDescription;
    }

    public String getInputType() {
        return iType;
    }

    public String getOutputDescription() {
        return oDescription;
    }

    public String getOutputType() {
        return oType;
    }


    public boolean getVisible() {
        return visible;
    }

    public int getCost() {
        return cost;
    }
}
