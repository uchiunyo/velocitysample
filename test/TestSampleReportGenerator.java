import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

public class TestSampleReportGenerator {

	@SuppressWarnings("deprecation")
	@Test
	public void testMakeReportFile() {
		Map<String, Map<String, Long>> testMap = new HashMap<String, Map<String, Long>>();

		Map<String, Long> map1 = new HashMap<String, Long>();
		map1.put("2014-02-01", 10L);
		map1.put("2014-02-02", 5L);
		map1.put("2014-02-03", 4L);
		map1.put("2014-02-04", 3L);
		map1.put("2014-02-05", 2L);
		testMap.put("graphDataNo1", map1);

		Map<String, Long> map2 = new HashMap<String, Long>();
		map2.put("2014-01-29", 5L);
		map2.put("2014-01-30", 6L);
		map2.put("2014-01-31", 7L);
		map2.put("2014-02-01", 8L);
		map2.put("2014-02-02", 9L);
		testMap.put("graphDataNo2", map2);

		Map<String, Long> map3 = new HashMap<String, Long>();
		map3.put("2014-03-01", 5L);
		map3.put("2014-03-02", 4L);
		map3.put("2014-03-03", 5L);
		map3.put("2014-03-04", 4L);
		map3.put("2014-03-05", 5L);
		testMap.put("graphDataNo3", map3);

		File outFile = new File("./test/testOutputReport.html");
		if (outFile.exists()) {
			outFile.delete();
		}
		String tempPath = "./templates/report.vm";
		try {
			SampleReportGenerator.makeReportFile(testMap, tempPath, outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File srcFile = new File("./test/ComparisonSourceReport.html");
		BufferedReader dstBr = null;
		BufferedReader srcBr = null;
		try {
			dstBr = new BufferedReader(new InputStreamReader(new FileInputStream(outFile)));
			StringBuffer dstSb = new StringBuffer();
			String line;
			while ((line = dstBr.readLine()) != null) {
				dstSb.append(line);
			}
			
			srcBr = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
			StringBuffer srcSb = new StringBuffer();
			System.out.println(dstBr.toString());
			// ファイルから１文字ずつ読み込み、バッファへ追加します。
			while ((line = srcBr.readLine()) != null) {
				srcSb.append(line);
			}
			assertEquals(srcSb.toString(), dstSb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				srcBr.close();
				dstBr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
