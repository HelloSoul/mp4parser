package com.googlecode.mp4parser;

import com.coremedia.iso.IsoBufferWrapperImpl;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoOutputStream;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.subtext.SrtParser;
import com.googlecode.mp4parser.authoring.subtext.TextTrackImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Adds subtitles.
 */
public class SubTitleExample {
    public static void main(String[] args) throws IOException {
        IsoFile countVideoIsoFile = new IsoFile(new IsoBufferWrapperImpl(readFully(SubTitleExample.class.getResourceAsStream("/count-video.mp4"))));
        countVideoIsoFile.parse();
        Movie countVideo = new Movie(countVideoIsoFile);

        TextTrackImpl subTitleEng = new TextTrackImpl();
        subTitleEng.getTrackMetaData().setLanguage("eng");


        subTitleEng.getSubs().add(new TextTrackImpl.Line(5000, 6000, "Five"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(8000, 9000, "Four"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(12000, 13000, "Three"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(16000, 17000, "Two"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(20000, 21000, "one"));

        countVideo.addTrack(subTitleEng);

        TextTrackImpl subTitleDeu = SrtParser.parse(SubTitleExample.class.getResourceAsStream("/count-subs-deutsch.srt"));
        subTitleDeu.getTrackMetaData().setLanguage("deu");
        countVideo.addTrack(subTitleDeu);

        IsoFile out = new DefaultMp4Builder().build(countVideo);
        FileOutputStream fos = new FileOutputStream(new File("output.mp4"));
        out.getBox(new IsoOutputStream(fos));
        fos.close();
    }


    static byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            baos.write(buffer, 0, n);
        }
        return baos.toByteArray();
    }
}