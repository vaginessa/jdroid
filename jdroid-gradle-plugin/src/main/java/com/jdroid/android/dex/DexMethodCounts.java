/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdroid.android.dex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class DexMethodCounts {

	private static final String CLASSES_DEX = "classes.dex";

	private static boolean includeClasses = true;
	private static String packageFilter;
	private static int maxDepth = Integer.MAX_VALUE;
	private static DexMethodCounts.Filter filter = DexMethodCounts.Filter.ALL;

	private static PrintWriter printWriter;

	enum Filter {
		ALL,
		DEFINED_ONLY,
		REFERENCED_ONLY
	}

	private static class Node {
		int count = 0;
		NavigableMap<String, Node> children = new TreeMap<String, Node>();

		void output(String indent) {
			if (indent.length() == 0) {
				printWriter.println("<root>: " + count);
			}
			indent += "    ";
			for (String name : children.navigableKeySet()) {
				Node child = children.get(name);
				printWriter.println(indent + name + ": " + child.count);
				child.output(indent);
			}
		}
	}

	/*
		# dex-method-counts

		Simple tool to output per-package method counts in an Android DEX executable grouped by package, to aid in getting under the 65,536 referenced method limit. More details are [in this blog post](http://blog.persistent.info/2014/05/per-package-method-counts-for-androids.html).

		To use it:

			$ ant jar
			$ ./dex-method-counts path/to/App.apk # or .zip or .dex

		You'll see output of the form:

			Read in 65490 method IDs.
			<root>: 65490
				: 3
				android: 6837
					accessibilityservice: 6
					bluetooth: 2
					content: 248
						pm: 22
						res: 45
					...
				com: 53881
					adjust: 283
						sdk: 283
					codebutler: 65
						android_websockets: 65
					...

		Supported options are:

		* `--include-classes`: Treat classes as packages and provide per-class method counts. One use-case is for protocol buffers where all generated code in a package ends up in a single class.
		* `--package-filter=...`: Only consider methods whose fullly qualified name starts with this prefix.
		* `--max-depth=...`: Limit how far into package paths (or inner classes, with `--include-classes`) counts should be reported for.
		* `--filter=[all|defined_only|referenced_only]`: Whether to count all methods (the default), just those defined in the input file, or just those that are referenced in it. Note that referenced methods count against the 64K method limit too.

		The DEX file parsing is based on the `dexdeps` tool from
		[the Android source tree](https://android.googlesource.com/platform/dalvik.git/+/master/tools/dexdeps/).

	 */
	public static void generateFullReport(String apkPathName, File outputFile) {
		generateFullReport(apkPathName, includeClasses, packageFilter, maxDepth, filter, outputFile);
	}

	public static void generateFullReport(String apkPathName, boolean includeClasses, String packageFilter,
			int maxDepth, Filter filter, File outputFile) {

		DexData dexData = getDexData(apkPathName);

		try {
			printWriter = new PrintWriter(outputFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		MethodRef[] methodRefs = getMethodRefs(dexData, filter);
		printSummary(apkPathName, methodRefs, outputFile);

		Node packageTree = new Node();

		for (MethodRef methodRef : methodRefs) {
			String classDescriptor = methodRef.getDeclClassName();
			String packageName = includeClasses ?
					Output.descriptorToDot(classDescriptor).replace('$', '.') :
					Output.packageNameOnly(classDescriptor);
			if (packageFilter != null &&
					!packageName.startsWith(packageFilter)) {
				continue;
			}
			String packageNamePieces[] = packageName.split("\\.");
			Node packageNode = packageTree;
			for (int i = 0; i < packageNamePieces.length && i < maxDepth; i++) {
				packageNode.count++;
				String name = packageNamePieces[i];
				if (packageNode.children.containsKey(name)) {
					packageNode = packageNode.children.get(name);
				} else {
					Node childPackageNode = new Node();
					packageNode.children.put(name, childPackageNode);
					packageNode = childPackageNode;
				}
			}
			packageNode.count++;
		}

		packageTree.output("");


		printWriter.close();
	}

	public static void generateSummary(String apkPathName) {
		MethodRef[] methodRefs = getDexData(apkPathName).getMethodRefs();
		printSummary(apkPathName, methodRefs, null);
	}

	private static void printSummary(String apkPathName, MethodRef[] methodRefs, File outputFile) {
		System.out.println("");
		System.out.println("********************************************************************************************");
		System.out.println("* Method count: " + methodRefs.length + " of 65536");
		System.out.println("* APK analysed: " + apkPathName);
		if (outputFile != null) {
			System.out.println("* Report generated: " + outputFile.getAbsolutePath());
		}
		System.out.println("********************************************************************************************");
	}

	private static DexData getDexData(String apkPathName) {
		try {
			RandomAccessFile raf = openInputFile(apkPathName);
			DexData dexData = new DexData(raf);
			dexData.load();
			return dexData;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Opens an input file, which could be a .dex or a .jar/.apk with a
	 * classes.dex inside.  If the latter, we extract the contents to a
	 * temporary file.
	 *
	 * @param fileName the name of the file to open
	 */
	private static RandomAccessFile openInputFile(String fileName) throws IOException {
		RandomAccessFile raf;

		raf = openInputFileAsZip(fileName);
		if (raf == null) {
			File inputFile = new File(fileName);
			raf = new RandomAccessFile(inputFile, "r");
		}

		return raf;
	}

	/**
	 * Tries to open an input file as a Zip archive (jar/apk) with a
	 * "classes.dex" inside.
	 *
	 * @param fileName the name of the file to open
	 * @return a RandomAccessFile for classes.dex, or null if the input file
	 * is not a zip archive
	 * @throws IOException if the file isn't found, or it's a zip and
	 *                     classes.dex isn't found inside
	 */
	private static RandomAccessFile openInputFileAsZip(String fileName) throws IOException {
		ZipFile zipFile;

        /*
         * Try it as a zip file.
         */
		try {
			zipFile = new ZipFile(fileName);
		} catch (FileNotFoundException fnfe) {
            /* not found, no point in retrying as non-zip */
			System.err.println("Unable to open '" + fileName + "': " +
					fnfe.getMessage());
			throw fnfe;
		} catch (ZipException ze) {
            /* not a zip */
			return null;
		}

        /*
         * We know it's a zip; see if there's anything useful inside.  A
         * failure here results in some type of IOException (of which
         * ZipException is a subclass).
         */
		ZipEntry entry = zipFile.getEntry(CLASSES_DEX);
		if (entry == null) {
			System.err.println("Unable to find '" + CLASSES_DEX +
					"' in '" + fileName + "'");
			zipFile.close();
			throw new ZipException();
		}

		InputStream zis = zipFile.getInputStream(entry);

        /*
         * Create a temp file to hold the DEX data, open it, and delete it
         * to ensure it doesn't hang around if we fail.
         */
		File tempFile = File.createTempFile("dexdeps", ".dex");
		//System.out.println("+++ using temp " + tempFile);
		RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
		tempFile.delete();

        /*
         * Copy all data from input stream to output file.
         */
		byte copyBuf[] = new byte[32768];
		int actual;

		while (true) {
			actual = zis.read(copyBuf);
			if (actual == -1)
				break;

			raf.write(copyBuf, 0, actual);
		}

		zis.close();
		raf.seek(0);

		return raf;
	}

	private static MethodRef[] getMethodRefs(DexData dexData, Filter filter) {
		MethodRef[] methodRefs = dexData.getMethodRefs();
		printWriter.println("Read in " + methodRefs.length + " method IDs.");
		if (filter == Filter.ALL) {
			return methodRefs;
		}

		ClassRef[] externalClassRefs = dexData.getExternalReferences();
		printWriter.println("Read in " + externalClassRefs.length +
				" external class references.");
		Set<MethodRef> externalMethodRefs = new HashSet<MethodRef>();
		for (ClassRef classRef : externalClassRefs) {
			for (MethodRef methodRef : classRef.getMethodArray()) {
				externalMethodRefs.add(methodRef);
			}
		}
		printWriter.println("Read in " + externalMethodRefs.size() +
				" external method references.");
		List<MethodRef> filteredMethodRefs = new ArrayList<MethodRef>();
		for (MethodRef methodRef : methodRefs) {
			boolean isExternal = externalMethodRefs.contains(methodRef);
			if ((filter == Filter.DEFINED_ONLY && !isExternal) ||
					(filter == Filter.REFERENCED_ONLY && isExternal)) {
				filteredMethodRefs.add(methodRef);
			}
		}
		printWriter.println("Filtered to " + filteredMethodRefs.size() + " " +
				(filter == Filter.DEFINED_ONLY ? "defined" : "referenced") +
				" method IDs.");
		return filteredMethodRefs.toArray(
				new MethodRef[filteredMethodRefs.size()]);
	}
}
