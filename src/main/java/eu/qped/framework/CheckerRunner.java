package eu.qped.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.qped.framework.qf.QfObject;
import eu.qped.java.checkers.mass.Mass;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckerRunner {

	private static final String QF_OBJECT_FILE_PROPERTY = "file";

	private static final String QF_OBJECT_JSON_FILE_NAME = "qf.json";

	private final QfObject qfObject;

	private final Checker checker;

	private File qfObjectJsonFile;
	
	private static List<File> tempFiles = new ArrayList<>();
	
	private static void cleanupTempFiles() {
		for (File file : tempFiles) {
			if (file.isDirectory()) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					FileUtils.delete(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		tempFiles.clear();
	}

	public CheckerRunner(File qfObjectJsonFile) throws JsonMappingException, JsonProcessingException, IOException {
		this.qfObjectJsonFile = qfObjectJsonFile;
		String qfObjectJsonString = FileUtils.readFileToString(this.qfObjectJsonFile, Charset.defaultCharset());

		ObjectMapper mapper = new ObjectMapper();

		// sem, semObj
		// syntax, syntax
		// style, style
		// checkerClass: name
		Map<String, Object> qfObjectMap = mapper.readValue(qfObjectJsonString,
				new TypeReference<Map<String, Object>>() {
				});

		//qfObject = mapper.readValue(qfObjectJsonString, new TypeReference<QfObject>(){});

		String checkerClassName = (String) qfObjectMap.get("checkerClass");
		// if no Checker class is specified, use Mass as default.
		if (checkerClassName == null) {
			checkerClassName = Mass.class.getName();
		}

		try {
			@SuppressWarnings("unchecked")
			Class<Checker> cls = (Class<Checker>) Class.forName(checkerClassName);
			this.checker = cls.getDeclaredConstructor().newInstance();

			FileInfo fileInfo;
			if (qfObjectMap.containsKey(QF_OBJECT_FILE_PROPERTY)) {
				fileInfo = mapper.readValue(
						mapper.writeValueAsString(qfObjectMap.get(QF_OBJECT_FILE_PROPERTY)),
						new TypeReference<FileInfo>() {
						});

				downloadAndUnzipIfNecessary(fileInfo);

			} else {
				fileInfo = null;
				//submittedFile = null;
			}


			qfObject = mapper.readValue(mapper.writeValueAsString(qfObjectMap), new TypeReference<QfObject>() {
			});

			for (Field field : cls.getDeclaredFields()) {
				QfProperty qfPropertyAnnotation = field.getDeclaredAnnotation(QfProperty.class);
				if (qfPropertyAnnotation != null) {
					if (field.getName().equals("file")) {
						if (field.getType() != FileInfo.class) {
							throw new IllegalArgumentException("The field " + field.getName() + " of checker class " + checkerClassName + " has the QfProperty annotation, but does not have the type FileInfo.");
						}
						field.setAccessible(true);
						field.set(checker, fileInfo);
					} else {
						Object propertyValue = qfObjectMap.get(field.getName());
						field.setAccessible(true);
						field.set(checker, mapper.readValue(mapper.writeValueAsString(propertyValue), field.getType()));
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Illegal checker class specified", e);
		}
	}

	/**
	 * This method downloads a file from URL of the provided argument to a local temp file, and unzips it
	 * to a local temp directory of the mime type is zip.
	 * The allocated temporary file and possibly the directory with all its contained files and directories
	 * will be deleted at the end of the execution of this program. I.e., when the main-method in this class ends.
	 * The location of the downloaded file and (in case of a zip file) the location of the extracted files are
	 * set as properties of the passed FileInfo object.
	 * The downloaded file is unzipped if the passed fileInfo specifies as mime type either
	 * "application/zip", "application/zip-compressed" or "application/x-zip-compressed".
	 * 
	 * @param fileInfo
	 * @return The directory containing the downloaded file, if it is a plain file, or the unzipped contents in 
	 * case the downloaded file was a zip archive.
	 * @throws FileNotFoundException
	 * @throws MalformedURLException
	 * @throws ZipException
	 * @throws IOException
	 */
	public static File downloadAndUnzipIfNecessary(FileInfo fileInfo) throws FileNotFoundException, MalformedURLException, ZipException, IOException {
		File targetDirectory = createManagedTempDirectory();
		File submittedFile = new File(targetDirectory, fileInfo.getId() + fileInfo.getExtension());

		try (InputStream input = new URL(fileInfo.getUrl()).openStream()) {
			try (OutputStream output = new FileOutputStream(submittedFile)) {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead;
				while ((bytesRead = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
			}
		}
		fileInfo.setDownloadedFile(submittedFile);
		
		if (fileInfo.getMimetype().contains("application/x-zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip-compressed")
				|| fileInfo.getMimetype().contains("application/zip") ) {
			File unzipTarget = createManagedTempDirectory();

			ZipFile zipFile = new ZipFile(fileInfo.getDownloadedFile());
			zipFile.extractAll(unzipTarget.toString());

			fileInfo.setUnzippedDirectory(unzipTarget);
			return unzipTarget;
		}
		
		return targetDirectory;
	}

	public static File createManagedTempDirectory() throws IOException {
		File unzipTarget = Files.createTempDirectory("qf-checker").toFile();
		tempFiles.add(unzipTarget);
		return unzipTarget;
	}

	public static File createManagedTempFile(String filename, String extension) throws IOException {
		File submittedFile = File.createTempFile(filename, extension);
		tempFiles.add(submittedFile);
		return submittedFile;
	}
	
	public File getQfObjectJsonFile() {
		return qfObjectJsonFile;
	}

	public void check() throws Exception {
		checker.check(qfObject);
	}

	public static void main(String[] args) throws IOException {
		String qfJsonFileName;
		boolean overwriteJsonFile;
		if (args.length > 0) {
			qfJsonFileName = args[0];
			overwriteJsonFile = false;
		} else {
			qfJsonFileName = QF_OBJECT_JSON_FILE_NAME;
			overwriteJsonFile = true;
		}
		
		File qfJsonFile = new File(qfJsonFileName);
		
		CheckerRunner runner = new CheckerRunner(qfJsonFile);
		
		try {
			runner.check();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			runner.getQfObject().setMessage("exceptionDuringCheck", sw.toString());
		}

		ObjectMapper mapper = new ObjectMapper();
	
		ObjectWriter writer = mapper.writer(new MinimalPrettyPrinter());
		if (overwriteJsonFile) {
			writer.writeValue(runner.getQfObjectJsonFile(), runner.getQfObject());
		}
		else {
			writer.writeValue(System.out, runner.getQfObject());
		}
		
		cleanupTempFiles();
	}

	public QfObject getQfObject() {
		Arrays.stream(qfObject.getFeedback()).forEach(System.out::println);
		return qfObject;
	}
}
