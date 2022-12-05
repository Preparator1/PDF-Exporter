package org.example;

import org.apache.pdfbox.text.TextPosition;
import org.fit.pdfdom.PDFBoxTree;
import org.fit.pdfdom.PathSegment;
import org.fit.pdfdom.TextMetrics;
import org.fit.pdfdom.resource.ImageResource;

import java.io.IOException;
import java.util.List;

public class GetData extends PDFBoxTree {

    public GetData() throws IOException {
        super();
    }

    @Override
    protected void startNewPage() {

    }

    @Override
    protected void renderText(String s, TextMetrics textMetrics) {

    }

    @Override
    protected void renderPath(List<PathSegment> list, boolean b, boolean b1) throws IOException {

    }

    @Override
    protected void renderImage(float v, float v1, float v2, float v3, ImageResource imageResource) throws IOException {

    }
}
