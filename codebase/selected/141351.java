package uk.ac.bolton.archimate.editor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * Some useful Zip Utilities
 *
 * @author Phillip Beauvoir
 */
public final class ZipUtils {

    /**
     * @param file
     * @return True if file is an archive zip file
     * @throws IOException 
     */
    public static boolean isZipFile(File file) throws IOException {
        final byte[] sig = new byte[] { 0x50, 0x4B, 0x3, 0x4 };
        if (file != null && file.canRead()) {
            byte[] buf = new byte[4];
            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
                is.read(buf);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            for (int i = 0; i < buf.length; i++) {
                if (buf[i] != sig[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Add all files and sub-files to the Zip
     * @param srcFolder The folder to add to the zip file
     * @param zOut
     * @param exclude A list of files to exclude from the operation. Can be null.
     * @param progressMonitor an optional ProgressMonitor.  This can be null.
     * @throws IOException If error or user cancelled
     */
    public static void addFolderToZip(File srcFolder, ZipOutputStream zOut, File[] exclude, IProgressMonitor progressMonitor) throws IOException {
        if (!srcFolder.isDirectory()) {
            throw new IOException("Not a folder");
        }
        addFolderToZip(srcFolder, srcFolder, zOut, exclude, progressMonitor);
    }

    private static void addFolderToZip(File rootFolder, File srcFolder, ZipOutputStream zOut, File[] exclude, IProgressMonitor progressMonitor) throws IOException {
        File[] files = srcFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (progressMonitor != null) {
                progressMonitor.setTaskName(files[i].getName());
                if (progressMonitor.isCanceled()) {
                    throw new IOException("User cancelled.");
                }
            }
            if (files[i].isDirectory()) {
                addFolderToZip(rootFolder, files[i], zOut, exclude, progressMonitor);
            } else {
                boolean do_add_file = true;
                if (exclude != null) {
                    for (File file_exclude : exclude) {
                        if (file_exclude.equals(files[i])) {
                            do_add_file = false;
                            break;
                        }
                    }
                }
                if (do_add_file) {
                    String entryName = FileUtils.getRelativePath(files[i], rootFolder);
                    addFileToZip(files[i], entryName, zOut);
                }
            }
        }
    }

    /**
	 * Adds a file to the Zip file
	 * @param file The file to add
	 * @param entryName
	 * @param zOut
	 * @throws IOException
	 */
    public static void addFileToZip(File file, String entryName, ZipOutputStream zOut) throws IOException {
        if (file.isDirectory()) {
            return;
        }
        int bytesRead;
        final int bufSize = 8192;
        byte buf[] = new byte[bufSize];
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipEntry.setTime(file.lastModified());
        try {
            zOut.putNextEntry(zipEntry);
        } catch (IOException ex) {
            return;
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), bufSize);
        while ((bytesRead = in.read(buf)) != -1) {
            zOut.write(buf, 0, bytesRead);
        }
        zOut.closeEntry();
        in.close();
    }

    /**
	 * Add an image to Zip
	 * @param image
	 * @param entryName
	 * @param zOut
	 * @param format could be SWT.IMAGE_BMP, SWT.IMAGE_BMP, etc
	 * @param progressMonitor
	 * @throws IOException
	 */
    public static void addImageToZip(Image image, String entryName, ZipOutputStream zOut, int format, IProgressMonitor progressMonitor) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        try {
            zOut.putNextEntry(zipEntry);
        } catch (IOException ex) {
            return;
        }
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] { image.getImageData() };
        loader.save(zOut, format);
        zOut.closeEntry();
    }

    /**
	 * Adds a String as a field entry to an already opened ZipOutputStream
	 * @param text
	 * @param entryName
	 * @param zOut
	 * @throws IOException
	 */
    public static void addStringToZip(String text, String entryName, ZipOutputStream zOut) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        ZipEntry zipEntry = new ZipEntry(entryName);
        zOut.putNextEntry(zipEntry);
        int i;
        while ((i = reader.read()) != -1) {
            zOut.write(i);
        }
        zOut.closeEntry();
    }

    /**
	 * @param zipFile
	 * @param entryName
	 * @return True if the zip file has an entry
	 * @throws IOException
	 */
    public static boolean hasZipEntry(File zipFile, String entryName) throws IOException {
        ZipEntry zipEntry;
        ZipInputStream zIn;
        boolean foundEntry = false;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
        zIn = new ZipInputStream(in);
        while ((zipEntry = zIn.getNextEntry()) != null) {
            String zipEntryName = zipEntry.getName();
            if (zipEntryName.equalsIgnoreCase(entryName)) {
                foundEntry = true;
                break;
            }
            zIn.closeEntry();
        }
        zIn.close();
        return foundEntry;
    }

    /**
	 * Extracts a named entry out of the zip file and returns the entry as a String
	 * Returns null if weirdness happens
	 * @param zipFile
	 * @param entryName
	 * @return
	 * @throws IOException
	 */
    public static String extractZipEntry(File zipFile, String entryName) throws IOException {
        ZipEntry zipEntry;
        ZipInputStream zIn;
        int bit;
        StringBuffer sb;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
        zIn = new ZipInputStream(in);
        while ((zipEntry = zIn.getNextEntry()) != null) {
            String zipEntryName = zipEntry.getName();
            if (zipEntryName.equalsIgnoreCase(entryName)) {
                break;
            }
            zIn.closeEntry();
        }
        if (zipEntry == null) {
            try {
                zIn.close();
            } catch (IOException ex) {
            }
            return null;
        }
        sb = new StringBuffer();
        while ((bit = zIn.read()) != -1) {
            sb.append((char) bit);
        }
        zIn.close();
        return sb.toString();
    }

    /**
     * @param zipFile
     * @param entryName
     * @return A Zip Entry Stream
     * @throws IOException
     */
    public static InputStream getZipEntryStream(File zipFile, String entryName) throws IOException {
        ZipEntry zipEntry;
        ZipInputStream zIn;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
        zIn = new ZipInputStream(in);
        while ((zipEntry = zIn.getNextEntry()) != null) {
            String zipEntryName = zipEntry.getName();
            if (zipEntryName.equalsIgnoreCase(entryName)) {
                break;
            }
            zIn.closeEntry();
        }
        if (zipEntry == null) {
            try {
                zIn.close();
            } catch (IOException ex) {
            }
            return null;
        }
        return zIn;
    }

    /**
	 * Extracts a named entry out of the zip file to the specified file
	 * Returns the File ref if OK or null if weirdness happens
	 * @param zipFile
	 * @param entryName
	 * @param outFile
	 * @return
	 * @throws IOException
	 */
    public static File extractZipEntry(File zipFile, String entryName, File outFile) throws IOException {
        ZipInputStream zIn;
        ZipEntry zipEntry;
        int bytesRead;
        final int bufSize = 8192;
        byte buf[] = new byte[bufSize];
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
        zIn = new ZipInputStream(in);
        while ((zipEntry = zIn.getNextEntry()) != null) {
            String zipEntryName = zipEntry.getName();
            if (zipEntryName.equalsIgnoreCase(entryName)) {
                break;
            }
            zIn.closeEntry();
        }
        if (zipEntry == null) {
            return null;
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile), bufSize);
        while ((bytesRead = zIn.read(buf)) != -1) {
            out.write(buf, 0, bytesRead);
        }
        out.flush();
        out.close();
        zIn.close();
        outFile.setLastModified(zipEntry.getTime());
        return outFile;
    }

    /**
	 * Gets all entry names out of a zip file and returns a list of entry names excluding directories
	 * @param zipFile
	 * @return a list of entry names which may be empty if the zip file does not exist
	 * @throws IOException
	 */
    public static List<String> getZipFileEntryNames(File zipFile) throws IOException {
        List<String> fileList = new ArrayList<String>();
        if (zipFile == null || !zipFile.canRead()) {
            return fileList;
        }
        ZipInputStream zIn = null;
        ZipEntry zipEntry;
        final int bufSize = 1024 * 16;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
        zIn = new ZipInputStream(in);
        try {
            while ((zipEntry = zIn.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    String zipEntryName = zipEntry.getName();
                    fileList.add(zipEntryName);
                }
                zIn.closeEntry();
            }
        } catch (IOException ex) {
            zIn.close();
            throw ex;
        } finally {
            zIn.close();
        }
        return fileList;
    }

    /**
	 * Extracts all entries out of the zip file to the specified folder
	 * Target folder is created if it doesn't exist
	 * @param zipFile
	 * @param targetFolder
	 * @throws IOException
	 */
    public static void unpackZip(File zipFile, File targetFolder) throws IOException {
        unpackZip(zipFile, targetFolder, null);
    }

    /**
	 * Extracts all entries out of the zip file to the specified folder
	 * Target folder is created if it doesn't exist
	 * Returns true or false to indicate if the progress cancel was pressed
	 * @param zipFile
	 * @param targetFolder
	 * @param progressMonitor an optional ProgressMonitor.  This can be null.
	 * @return false if the progressMonitor is cancelled
	 * @throws IOException If error or user cancelled
	 */
    public static void unpackZip(File zipFile, File targetFolder, IProgressMonitor progressMonitor) throws IOException {
        targetFolder.mkdirs();
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        ZipInputStream zIn = null;
        ZipEntry zipEntry;
        int bytesRead;
        final int bufSize = 1024;
        byte buf[] = new byte[bufSize];
        in = new BufferedInputStream(new FileInputStream(zipFile), bufSize);
        zIn = new ZipInputStream(in);
        try {
            while ((zipEntry = zIn.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    File outFile = new File(targetFolder, zipEntry.getName());
                    if (!outFile.getParentFile().exists()) {
                        outFile.getParentFile().mkdirs();
                    }
                    out = new BufferedOutputStream(new FileOutputStream(outFile), bufSize);
                    if (progressMonitor != null) {
                        progressMonitor.setTaskName(zipEntry.getName());
                    }
                    int sleep_count = 0;
                    while ((bytesRead = zIn.read(buf)) != -1) {
                        out.write(buf, 0, bytesRead);
                        if (sleep_count >= 40) {
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            sleep_count = 0;
                        }
                        sleep_count++;
                        if (progressMonitor != null && progressMonitor.isCanceled()) {
                            out.flush();
                            out.close();
                            zIn.close();
                            throw new IOException("User Cancelled");
                        }
                    }
                    outFile.setLastModified(zipEntry.getTime());
                    out.flush();
                    out.close();
                }
                zIn.closeEntry();
            }
            zIn.close();
        } catch (IOException ex) {
            zIn.close();
            if (out != null) {
                out.flush();
                out.close();
            }
            throw ex;
        }
    }
}
