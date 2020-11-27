package ru.job4j.videocameradata;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*",
        "org.apache.http.conn.ssl.*", "com.amazonaws.*", "javax.net.ssl.*", "com.sun.*",
        "org.w3c.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({Videocameradata.class})
public class VideocameradataTest {
    private static String urlCameras = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5";
    public static final String LIST = "[\n"
            + "    {\n"
            + "        \"id\": 1999,\n"
            + "        \"sourceDataUrl\": \"http://www.mocky.io/v2/5c51b230340000094f129f5d\",\n"
            + "        \"tokenDataUrl\": \"http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-"
            + "delay=1s\"\n"
            + "    }\n"
            + "]";
    public static final String SOURCE_DATA_URL = "http://www.mocky.io/v2/5c51b230340000094f129f5d";
    public static final String SOURCEURL = "{\n"
            + "    \"urlType\": \"LIVE\",\n"
            + "    \"videoUrl\": \"rtsp://127.0.0.1/19\"\n"
            + "}";
    public static final String TOKEN_DATA_URL = "http://www.mocky.io/v2/5c51b5b6340000554e129f7b"
            + "?mocky-delay=1s";
    public static final String TOKEN_URL = "{\n"
            + "    \"value\": \"fa4b588e-249b-11e9-ab14-d663bd873d93\",\n"
            + "    \"ttl\": 1209\n"
            + "}";
    public static final Logger LOGGER = LoggerFactory.getLogger(VideocameradataTest.class);
    public static final String LN = System.lineSeparator();
    private static Camera cam;

    static {
        SourceDataUrl sourceDataUrl = new SourceDataUrl();
        sourceDataUrl.setUrlType("LIVE");
        sourceDataUrl.setVideoUrl("rtsp://127.0.0.1/19");
        TokenDataUrl tokenDataUrl = new TokenDataUrl();
        tokenDataUrl.setValue("fa4b588e-249b-11e9-ab14-d663bd873d93");
        tokenDataUrl.setTtl(1209);
        cam = new Camera(1999, sourceDataUrl, tokenDataUrl);
    }

    @Test
    public void astart() throws Exception {
        Videocameradata vid = new Videocameradata();
        DataAndURL dat = new DataAndURL();
        DataAndURL spydataAndURL = spy(dat);
        whenNew(DataAndURL.class).withNoArguments().thenReturn(spydataAndURL);
        doReturn(LIST).when(spydataAndURL).jsonFromURL(dat.strToURL(urlCameras));
        doReturn(SOURCEURL).when(spydataAndURL).jsonFromURL(dat.strToURL(SOURCE_DATA_URL));
        doReturn(TOKEN_URL).when(spydataAndURL).jsonFromURL(dat.strToURL(TOKEN_DATA_URL));

        vid.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        vid.stop();
        LOGGER.info(String.valueOf(vid.getCameraList().get(1999)));

        Camera camera = vid.getCameraList().get(1999);
        camera.hashCode();
        assertEquals(cam, camera);
    }

    @Test
    public void bequalsThisEqualsO() {
        assertTrue(cam.equals(cam));
        assertFalse(cam.equals(null));
        assertFalse(cam.equals("null"));
    }
}

