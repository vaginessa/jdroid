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

package info.persistent.dex;

import com.android.dexdeps.DexData;
import com.android.dexdeps.DexDataException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

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
public class Main {
	private static final String CLASSES_DEX = "classes.dex";

	private boolean includeClasses = true;
	private String packageFilter;
	private int maxDepth = Integer.MAX_VALUE;
	private DexMethodCounts.Filter filter = DexMethodCounts.Filter.ALL;

	/**
	 * Start things up.
	 */
	public void count(String apkPathName, File outputFile) {
		try {
			boolean first = true;

			RandomAccessFile raf = openInputFile(apkPathName);
			DexData dexData = new DexData(raf);
			dexData.load();
			DexMethodCounts.generate(
					dexData, includeClasses, packageFilter, maxDepth, filter, outputFile);
			raf.close();
		} catch (IOException ioe) {
			if (ioe.getMessage() != null) {
				System.err.println("Failed: " + ioe);
			}
			System.exit(1);
		} catch (DexDataException dde) {
			/* a message was already reported, just bail quietly */
			System.exit(1);
		}
	}

	/**
	 * Opens an input file, which could be a .dex or a .jar/.apk with a
	 * classes.dex inside.  If the latter, we extract the contents to a
	 * temporary file.
	 *
	 * @param fileName the name of the file to open
	 */
	RandomAccessFile openInputFile(String fileName) throws IOException {
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
	RandomAccessFile openInputFileAsZip(String fileName) throws IOException {
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
}
