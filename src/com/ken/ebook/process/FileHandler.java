package com.ken.ebook.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class FileHandler {
	public static final String ROOT_FOLDER = "epubreader";
	public static final String DATA_FOLDER = "data";
	private static ArrayList<File> chapterList = new ArrayList<File>();
	private static ArrayList<File> cssList = new ArrayList<File>();

	public static String ncxPath;
	public static String contentPath;
	public static String coverPath;
	public static String chapterPath;

	public static String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ FileHandler.ROOT_FOLDER
			+ File.separator;

	public static void createRootFolder() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Log.d("MyApp", "No SDCARD");
		} else {
			// root's app's folder
			File directory = new File(Environment.getExternalStorageDirectory()
					+ File.separator + ROOT_FOLDER);

			if (directory.exists()) {
				deleteBookFolder(directory);
			}

			directory.mkdir();

			// data's app's folder
			File data = new File(Environment.getExternalStorageDirectory()
					+ File.separator + ROOT_FOLDER + File.separator
					+ DATA_FOLDER);
			data.mkdir();
		}
	}// end-func createRootFolder

	public static void writeData(String str_data, String file_name) {
		try {
			File myFile = new File(FileHandler.rootPath
					+ FileHandler.DATA_FOLDER + File.separator + file_name);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile, false);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(str_data);
			myOutWriter.close();
			fOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end-func

	public static String createBookFolder(String _bookFolderName) {
		File aBook = new File(Environment.getExternalStorageDirectory()
				+ File.separator + ROOT_FOLDER + File.separator
				+ _bookFolderName);
		aBook.mkdirs();
		return Environment.getExternalStorageDirectory() + File.separator
				+ ROOT_FOLDER + File.separator + _bookFolderName;
	}

	public static boolean deleteBookFolder(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteBookFolder(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	static public void doUnzip(String inputZip, String destinationDirectory)
			throws IOException {
		int BUFFER = 2048;
		List zipFiles = new ArrayList();
		File sourceZipFile = new File(inputZip);
		File unzipDestinationDirectory = new File(destinationDirectory);
		unzipDestinationDirectory.mkdir();

		ZipFile zipFile;
		// Open Zip file for reading
		zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);

		// Create an enumeration of the entries in the zip file
		Enumeration zipFileEntries = zipFile.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

			String currentEntry = entry.getName();

			File destFile = new File(unzipDestinationDirectory, currentEntry);

			if (currentEntry.endsWith(".zip")) {
				zipFiles.add(destFile.getAbsolutePath());
			}

			// grab file's parent directory structure
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			try {
				// extract file if not a directory
				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(
							zipFile.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos,
							BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		zipFile.close();

		for (Iterator iter = zipFiles.iterator(); iter.hasNext();) {
			String zipName = (String) iter.next();
			doUnzip(zipName, destinationDirectory + File.separatorChar
					+ zipName.substring(0, zipName.lastIndexOf(".zip")));
		}

	}

	// tìm kiếm file trong thư mục
	public static void findChapterPath(String name, String _pathFoler) {
		File directory = new File(_pathFoler);
		File[] list = directory.listFiles();
		if (list != null) {
			for (File fil : list) {
				if (fil.isDirectory()) {
					findChapterPath(name, _pathFoler + "\\" + fil.getName());
				} else if (name.equalsIgnoreCase(fil.getName())) {
					chapterPath = fil.getPath();
				}
			}
		}
	}

	// // get List chapter
	// public static ArrayList<File> getAllChapter(File dir) {
	// File listFile[] = dir.listFiles();
	// if (listFile != null && listFile.length > 0) {
	// for (int i = 0; i < listFile.length; i++) {
	//
	// if (listFile[i].isDirectory()) {
	// // fileList.add(listFile[i]);
	// getAllChapter(listFile[i]);
	// } else {
	// if (listFile[i].getName().endsWith(".ncx")) {
	// chapterList.add(listFile[i]);
	// }
	// }
	// }
	// }
	// return chapterList;
	// }

	// get List css FILE format
	public static ArrayList<File> getAllCSS(File dir) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					// fileList.add(listFile[i]);
					getAllCSS(listFile[i]);
				} else {
					if (listFile[i].getName().endsWith(".css")) {
						cssList.add(listFile[i]);
					}
				}
			}
		}
		return cssList;
	}

	public static String getChapterPath(File dir, String chapter_name) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					getChapterPath(listFile[i], chapter_name);
				} else {
					if (listFile[i].getName().equals(chapter_name)) {
						chapterPath = listFile[i].getPath();
					}
				}
			}
		}
		return chapterPath;
	}

	// get path file Toc.ncx
	@SuppressLint("DefaultLocale")
	public static String getNcxFilePath(String direct) {
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					// fileList.add(listFile[i]);
					getNcxFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().toLowerCase().equals("toc.ncx")) {
						// fileList.add(listFile[i]);
						ncxPath = listFile[i].getPath();
					}
				}
			}
		}
		return ncxPath;
	}

	// get path file content.opf
	public static String getContentFilePath(String direct) {
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getContentFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().toLowerCase()
							.equals("content.opf")) {
						contentPath = listFile[i].getPath();
					}
				}
			}
		}
		return contentPath;
	}

	// get chapter path
	public static String getChapterFilePath(String direct, String chapter_name) {
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getChapterFilePath(direct + "/" + listFile[i].getName(),
							chapter_name);
				} else {
					if (listFile[i].getName().toLowerCase()
							.equals(chapter_name)) {
						chapterPath = listFile[i].getPath();
					}
				}
			}
		}
		return chapterPath;
	}

	// get path file cover
	public static String getCoverFilePath(String direct) {
		File dir = new File(direct);
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					getCoverFilePath(direct + "/" + listFile[i].getName());
				} else {
					if (listFile[i].getName().startsWith("cover")) {
						coverPath = listFile[i].getPath();
					}
				}
			}
		}
		return coverPath;
	}

	public static String getLastTokenizer(String string, String delimiters) {
		StringTokenizer tokens = new StringTokenizer(string, delimiters);
		String result = "";
		while (tokens.hasMoreTokens()) {
			result = tokens.nextToken();
		}

		StringTokenizer newToken = new StringTokenizer(result, "#");
		if (newToken.hasMoreTokens()) {
			result = newToken.nextToken();
		}

		return result;
	}
}
