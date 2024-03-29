package net.sourceforge.cruisecontrol.distributed.core.jnlputil;

import org.apache.log4j.Logger;
import javax.jnlp.ExtensionInstallerService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import net.sourceforge.cruisecontrol.builders.AntScript;

/**
 * Installs {@link AntScript#LIBNAME_PROGRESS_LOGGER cruisecontrol-antprogresslogger.jar} (required by AntBuilder)
 * in webstart environment of webstart deployed Build Agents.
 *
 * @author Dan Rollo
 * Date: Sep 9, 2007
 * Time: 12:13:06 AM
 */
public final class AntProgressLoggerInstaller {

    private static final Logger LOG = Logger.getLogger(AntProgressLoggerInstaller.class);

    private static void log(final String msg) {
        System.out.println(msg);
        LOG.info(msg);
    }

    private static void log(final String msg, final Throwable thrown) {
        System.out.println(msg);
        thrown.printStackTrace();
        LOG.info(msg, thrown);
    }

    private static final String JNLP_MUFFIN_ANT_PROGRESS_LOGGER_PATH = "JNLPMuffinAntProgressLoggerPath";

    private AntProgressLoggerInstaller() {
    }

    public static void main(String[] args) {
        if ((args.length > 0) && (args[0].equalsIgnoreCase("install"))) {
            log("Installing Ant Progress Logger...");
            install();
        } else {
            log("Installer called without install arg, uninstalling Ant Progress Logger...");
            uninstall();
        }
    }

    private static void installTo(final URL urlResource, final File toFile) {
        final InputStream is;
        try {
            is = urlResource.openStream();
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception reading url: " + urlResource + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        }
        try {
            toFile.createNewFile();
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception creating file: " + toFile.getAbsolutePath() + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        }
        final OutputStream os;
        byte[] buf = new byte[255];
        try {
            os = new FileOutputStream(toFile);
            try {
                int len;
                while ((len = is.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }
            } finally {
                os.close();
            }
        } catch (IOException e) {
            final RuntimeException re = new RuntimeException("I/O Exception writing file: " + toFile.getAbsolutePath() + ", " + e, e);
            log(re.getMessage(), e);
            throw re;
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }

    private static File getExtPath(final ExtensionInstallerService extensionInstallerService) {
        final String path = extensionInstallerService.getInstallPath();
        return new File(path, AntScript.LIBNAME_PROGRESS_LOGGER);
    }

    /**
     * Attempt to fix possible race condition when creating directories on
     * WinXP, also Windows2000. If the mkdirs does not work, wait a little and
     * try again.
     * Taken from Ant Mkdir taskdef.
     *
     * @param f the path for which directories are to be created
     * @return <code>true</code> if and only if the directory was created,
     *         along with all necessary parent directories; <code>false</code>
     *         otherwise
     */
    private static boolean doMkDirs(File f) {
        if (!f.mkdirs()) {
            try {
                Thread.sleep(10);
                return f.mkdirs();
            } catch (InterruptedException ex) {
                return f.mkdirs();
            }
        }
        return true;
    }

    private static void install() {
        final ExtensionInstallerService extensionInstallerService = getExtensionInstallerService();
        final File extPath = getExtPath(extensionInstallerService);
        if (extPath.exists()) {
            extPath.delete();
        } else {
            final File extDir = extPath.getParentFile();
            if (!extDir.exists()) {
                if (!doMkDirs(extDir)) {
                    throw new RuntimeException("Error creating install dir: " + extDir.getAbsolutePath());
                }
            }
        }
        log("extPath: " + extPath.getAbsolutePath());
        final URL u = AntProgressLoggerInstaller.class.getClassLoader().getResource(AntScript.LIBNAME_PROGRESS_LOGGER);
        log("url: " + u);
        try {
            installTo(u, extPath);
        } catch (Exception e) {
            final RuntimeException re = new RuntimeException("Exception install url: " + u + " to file: " + extPath.getAbsolutePath() + e, e);
            log(re.getMessage(), e);
            extensionInstallerService.installFailed();
            throw re;
        }
        JNLPServiceUtil.save(JNLP_MUFFIN_ANT_PROGRESS_LOGGER_PATH, extPath.getAbsolutePath());
        extensionInstallerService.installSucceeded(false);
        log("End of Installation.");
    }

    private static void uninstall() {
        final ExtensionInstallerService extensionInstallerService = getExtensionInstallerService();
        final File extPath = getExtPath(extensionInstallerService);
        if (!extPath.exists()) {
            log("Doing nothing because expected file to remove does not exist: " + extPath.getAbsolutePath());
            return;
        }
        extPath.delete();
        extPath.deleteOnExit();
        log("Deleted file: " + extPath.getAbsolutePath());
        extPath.getParentFile().delete();
        extPath.getParentFile().deleteOnExit();
        log("Deleted parent dir: " + extPath.getParentFile().getAbsolutePath());
        JNLPServiceUtil.delete(JNLP_MUFFIN_ANT_PROGRESS_LOGGER_PATH);
        log("Unistall completed.");
    }

    private static ExtensionInstallerService getExtensionInstallerService() {
        final ExtensionInstallerService extensionInstallerService;
        try {
            extensionInstallerService = (ExtensionInstallerService) ServiceManager.lookup("javax.jnlp.ExtensionInstallerService");
        } catch (UnavailableServiceException e) {
            final RuntimeException re = new RuntimeException("Error getting jnlp install service.", e);
            log(re.getMessage(), e);
            throw re;
        }
        return extensionInstallerService;
    }

    public static String getJNLPMuffinAntProgressLoggerPath() {
        return JNLPServiceUtil.load(JNLP_MUFFIN_ANT_PROGRESS_LOGGER_PATH);
    }
}
