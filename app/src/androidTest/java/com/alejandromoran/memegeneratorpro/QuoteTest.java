package com.alejandromoran.memegeneratorpro;

import android.test.suitebuilder.annotation.SmallTest;
import com.alejandromoran.memegeneratorpro.utils.Meme;
import com.alejandromoran.memegeneratorpro.utils.Quote;
import junit.framework.TestCase;

/**
 * Created by Ale on 23/01/2016.
 */
public class QuoteTest extends TestCase {

    private Meme meme;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.meme = new Quote();
        this.meme.setTopText("topText");
    }

    @SmallTest
    public void testEqualTopText() throws Exception {
        assertEquals("Meme topText is not the same", "topText", meme.getTopText());
    }
}
