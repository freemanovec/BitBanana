package app.michaelwuensch.bitbanana.lightning;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import app.michaelwuensch.bitbanana.models.LightningNodeUri;
import app.michaelwuensch.bitbanana.util.LightningNodeUirParser;

public class LightningNodeUirParserTest {

    @Test(expected = NullPointerException.class)
    public void givenNullString_whenParseNodeUri_thenThrowNullPointer() {
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(null);
    }

    @Test
    public void givenEmptyString_whenParseNodeUri_thenReturnNull() {
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri("");

        assertNull(parsedUri);
    }

    @Test
    public void givenTooShortUri_whenParseNodeUri_thenReturnNull() {
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri("02a40ff73c1a2c6469b95e7cc544876e9a3b1@127.0.0.1");

        assertNull(parsedUri);
    }

    @Test
    public void givenPubKeyOnly_whenParseNodeUri_thenReturnLightningNodeUri() {
        String validPubKey = "02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(validPubKey);

        assertEquals(validPubKey, parsedUri.getPubKey());
        assertNull(parsedUri.getHost());
    }

    @Test
    public void givenInvalidCharsInPubKey_whenParseNodeUri_thenReturnLightningNodeUri() {
        String invalidPubKey = "T2a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(invalidPubKey);

        assertNull(parsedUri);
    }

    @Test
    public void givenInvalidPubKeyLength_whenParseNodeUri_thenReturnLightningNodeUri() {
        String uri = "02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f73737af8be10330652923b67db7@127.0.0.1";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(uri);

        assertNull(parsedUri);
    }

    @Test
    public void givenValidUri_whenParseNodeUri_thenReturnLightningNodeUri() {
        String uri = "02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f@127.0.0.1";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(uri);

        assertEquals("02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f", parsedUri.getPubKey());
        assertEquals("127.0.0.1", parsedUri.getHost());
    }

    @Test
    public void givenValidUriWithPort_whenParseNodeUri_thenReturnLightningNodeUri() {
        String uri = "02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f@127.0.0.1:1337";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(uri);

        assertEquals("02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f", parsedUri.getPubKey());
        assertEquals("127.0.0.1:1337", parsedUri.getHost());
    }

    @Test
    public void givenInValidUri_whenParseNodeUri_thenReturnLightningNodeUri() {
        String validPubKey = "02a40ff73c1a2c6469b95e7cc544876e9a3b1d73737af8be10330652923b67db7f@";
        LightningNodeUri parsedUri = LightningNodeUirParser.parseNodeUri(validPubKey);

        assertNull(parsedUri);
    }
}
