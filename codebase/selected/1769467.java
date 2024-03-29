package webcamstudio.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import webcamstudio.media.renderer.Capturer;
import webcamstudio.media.renderer.Exporter;
import webcamstudio.media.renderer.ProcessExecutor;
import webcamstudio.mixers.Frame;
import webcamstudio.streams.Stream;
import webcamstudio.util.Tools;
import webcamstudio.util.Tools.OS;

/**
 *
 * @author patrick
 */
public class ProcessRenderer {

    static final String RES_CAP = "ffmpeg-capture_OS.properties";

    static final String RES_OUT = "ffmpeg-output_OS.properties";

    public enum ACTION {

        CAPTURE, OUTPUT
    }

    java.io.DataInput input = null;

    boolean stopMe = false;

    boolean stopped = true;

    private Properties plugins = null;

    String plugin = "";

    int videoPort = 0;

    int audioPort = 0;

    int frequency = 44100;

    int channels = 2;

    int bitSize = 16;

    Stream stream;

    ProcessExecutor process;

    Capturer capture;

    Exporter exporter;

    FME fme = null;

    public ProcessRenderer(Stream s, ACTION action, String plugin) {
        stream = s;
        if (plugins == null) {
            plugins = new Properties();
            try {
                if (plugin.equals("custom")) {
                    plugins.load(stream.getFile().toURI().toURL().openStream());
                } else {
                    plugins.load(getResource(action).openStream());
                }
            } catch (IOException ex) {
                Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        process = new ProcessExecutor(s.getName());
        this.plugin = plugin;
    }

    private String translateTag(String value) {
        String result = value.toUpperCase().replace('.', '_');
        if (plugins.containsKey("TAG_" + result)) {
            result = plugins.getProperty("TAG_" + result);
        }
        return result;
    }

    public ProcessRenderer(Stream s, FME fme, String plugin) {
        stream = s;
        this.fme = fme;
        if (plugins == null) {
            plugins = new Properties();
            try {
                plugins.load(getResource(ACTION.OUTPUT).openStream());
            } catch (IOException ex) {
                Logger.getLogger(ProcessRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        process = new ProcessExecutor(s.getName());
        this.plugin = plugin;
    }

    private static URL getResource(ACTION a) throws MalformedURLException {
        File userSettings = null;
        switch(a) {
            case CAPTURE:
                userSettings = new File(new File(System.getProperty("user.home") + "/.webcamstudio"), RES_CAP.replaceAll("OS", Tools.getOSName()));
                break;
            case OUTPUT:
                userSettings = new File(new File(System.getProperty("user.home") + "/.webcamstudio"), RES_OUT.replaceAll("OS", Tools.getOSName()));
                break;
        }
        URL res = null;
        System.out.println(userSettings.getAbsolutePath());
        if (userSettings.exists()) {
            res = userSettings.toURI().toURL();
        } else {
            String path = null;
            switch(a) {
                case CAPTURE:
                    path = "/webcamstudio/ffmpeg/ffmpeg-capture_" + Tools.getOSName() + ".properties";
                    break;
                case OUTPUT:
                    path = "/webcamstudio/ffmpeg/ffmpeg-output_" + Tools.getOSName() + ".properties";
                    break;
            }
            res = ProcessRenderer.class.getResource(path);
        }
        System.out.println("Resource Used: " + res.toString());
        return res;
    }

    private String setParameters(String cmd) {
        String command = cmd;
        for (Tags tag : Tags.values()) {
            switch(tag) {
                case DESKTOPX:
                    command = command.replaceAll(Tags.DESKTOPX.toString(), stream.getDesktopX() + "");
                    break;
                case DESKTOPY:
                    command = command.replaceAll(Tags.DESKTOPY.toString(), stream.getDesktopY() + "");
                    break;
                case DESKTOPW:
                    command = command.replaceAll(Tags.DESKTOPW.toString(), stream.getDesktopW() + "");
                    break;
                case DESKTOPH:
                    command = command.replaceAll(Tags.DESKTOPH.toString(), stream.getDesktopH() + "");
                    break;
                case VCODEC:
                    if (fme != null) {
                        command = command.replaceAll(Tags.VCODEC.toString(), translateTag(fme.getVcodec()));
                    }
                    break;
                case ACODEC:
                    if (fme != null) {
                        command = command.replaceAll(Tags.ACODEC.toString(), translateTag(fme.getAcodec()));
                    }
                    break;
                case VBITRATE:
                    if (fme != null) {
                        command = command.replaceAll(Tags.VBITRATE.toString(), fme.getVbitrate());
                    }
                    break;
                case ABITRATE:
                    if (fme != null) {
                        command = command.replaceAll(Tags.ABITRATE.toString(), fme.getAbitrate());
                    }
                    break;
                case URL:
                    if (fme != null) {
                        command = command.replaceAll(Tags.URL.toString(), fme.getUrl() + "/" + fme.getStream());
                    } else if (stream.getURL() != null) {
                        command = command.replaceAll(Tags.URL.toString(), "" + stream.getURL());
                    }
                    break;
                case APORT:
                    command = command.replaceAll(Tags.APORT.toString(), "" + audioPort);
                    break;
                case CHEIGHT:
                    command = command.replaceAll(Tags.CHEIGHT.toString(), "" + stream.getCaptureHeight());
                    break;
                case CWIDTH:
                    command = command.replaceAll(Tags.CWIDTH.toString(), "" + stream.getCaptureWidth());
                    break;
                case FILE:
                    if (stream.getFile() != null) {
                        if (Tools.getOS() == OS.WINDOWS) {
                            command = command.replaceAll(Tags.FILE.toString(), "\"" + stream.getFile().getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "\"");
                        } else {
                            command = command.replaceAll(Tags.FILE.toString(), "" + stream.getFile().getAbsolutePath().replaceAll(" ", "\\ ") + "");
                        }
                    }
                    break;
                case OHEIGHT:
                    command = command.replaceAll(Tags.OHEIGHT.toString(), "" + stream.getHeight());
                    break;
                case OWIDTH:
                    command = command.replaceAll(Tags.OWIDTH.toString(), "" + stream.getWidth());
                    break;
                case RATE:
                    command = command.replaceAll(Tags.RATE.toString(), "" + stream.getRate());
                    break;
                case SEEK:
                    command = command.replaceAll(Tags.SEEK.toString(), "" + stream.getSeek());
                    break;
                case VPORT:
                    command = command.replaceAll(Tags.VPORT.toString(), "" + videoPort);
                    break;
                case FREQ:
                    command = command.replaceAll(Tags.FREQ.toString(), "" + frequency);
                    break;
                case BITSIZE:
                    command = command.replaceAll(Tags.BITSIZE.toString(), "" + bitSize);
                    break;
                case CHANNELS:
                    command = command.replaceAll(Tags.CHANNELS.toString(), "" + channels);
                    break;
            }
        }
        return command;
    }

    public Frame getFrame() {
        if (capture == null) {
            return null;
        } else {
            return capture.getFrame();
        }
    }

    public void read() {
        stopped = false;
        stopMe = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                capture = new Capturer(stream);
                if (stream.hasVideo()) {
                    videoPort = capture.getVideoPort();
                }
                if (stream.hasAudio()) {
                    audioPort = capture.getAudioPort();
                }
                String command = "";
                if (plugin.equals("custom")) {
                    command = plugins.getProperty("source").replaceAll("  ", " ");
                } else {
                    command = plugins.getProperty(plugin).replaceAll("  ", " ");
                }
                command = command.replaceAll(" ", "ABCDE");
                command = setParameters(command);
                String[] parms = command.split("ABCDE");
                try {
                    for (String p : parms) {
                        System.out.print(p + " ");
                    }
                    System.out.println();
                    process.execute(parms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void write() {
        stopped = false;
        stopMe = false;
        new Thread(new Runnable() {

            @Override
            public void run() {
                exporter = new Exporter(stream);
                videoPort = exporter.getVideoPort();
                audioPort = exporter.getAudioPort();
                stopped = false;
                String command = plugins.getProperty(plugin).replaceAll("  ", " ");
                command = setParameters(command);
                System.out.println(command);
                final String[] parms = command.split(" ");
                try {
                    process.execute(parms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        stopMe = true;
        if (capture != null) {
            capture.abort();
            capture = null;
        }
        if (exporter != null) {
            exporter.abort();
            exporter = null;
        }
        if (process != null) {
            process.destroy();
        }
        stopMe = false;
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getBitSize() {
        return bitSize;
    }

    public int getChannels() {
        return channels;
    }
}
