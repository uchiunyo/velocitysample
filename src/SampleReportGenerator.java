import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.SortTool;

public class SampleReportGenerator {

	public static final String TEMPLATE_PATH = "./templates/report.vm";
	public static final String OUTPUT_PATH = "./report.html";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Map<String, Long>> nameAndDateAndScore = createTestData();
		File file = new File(OUTPUT_PATH);
		try {
			makeReportFile(nameAndDateAndScore, TEMPLATE_PATH, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void makeReportFile(Map<String, Map<String, Long>> nameAndDateAndScore, String tempPath, File out) throws IOException{
		Velocity.init();
		VelocityContext context = new VelocityContext();
		context.put("DATA_TABLE", createDataTable(nameAndDateAndScore));
		context.put("sorter", new SortTool());
		Template template = Velocity.getTemplate(tempPath,"UTF-8");
		StringWriter w = new StringWriter();
		template.merge(context, w);
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		bw.write(w.toString());
		bw.close();
		//System.out.println(w.toString());
	}
	
	private static List<String> createDataTable(Map<String, Map<String, Long>> nameAndDateAndScore){
		List<String> result = new ArrayList<String>();
		Map<String, Long[]> mapTmp = new HashMap<String, Long[]>();
		String header = "[\"date\"";
		
		int i = 0;
		for(String name : nameAndDateAndScore.keySet()) {
			header = header + ",\"" + name + "\"";
			for(String keyDate : nameAndDateAndScore.get(name).keySet()){
				if(null == mapTmp.get(keyDate)){
					Long[] listTmp = new Long[nameAndDateAndScore.size()];
					Arrays.fill(listTmp,0L);
					mapTmp.put(keyDate, listTmp);
				}
				mapTmp.get(keyDate)[i] = nameAndDateAndScore.get(name).get(keyDate);
			}
			i = i + 1;
		}
		header = header + "]";
		result.add(header);
		
		Object[] dateList = mapTmp.keySet().toArray();
		Arrays.sort(dateList);
		
		Long[] sumTmp = new Long[nameAndDateAndScore.size()];
		Arrays.fill(sumTmp, 0L);
		int j = 0;
		
		for(Object date : dateList){
			String lineData = "[\"" + date.toString() + "\",";
			for(Long count : mapTmp.get(date.toString())){
				sumTmp[j] = sumTmp[j] + count;
				lineData = lineData + sumTmp[j].toString() + ",";
				j = j + 1;
			}
			lineData = lineData + "]";
			result.add(lineData);
			j = 0;
		}
		
		return result;
	}
	
	private static Map<String, Map<String, Long>> createTestData() {
		Map<String, Map<String, Long>> result = new HashMap<String, Map<String, Long>>();

		Map<String, Long> map1 = new HashMap<String, Long>();
		map1.put("2014-02-01", 10L);
		map1.put("2014-02-02", 5L);
		map1.put("2014-02-03", 4L);
		map1.put("2014-02-04", 3L);
		map1.put("2014-02-05", 2L);
		result.put("graphDataNo1", map1);

		Map<String, Long> map2 = new HashMap<String, Long>();
		map2.put("2014-01-29", 5L);
		map2.put("2014-01-30", 6L);
		map2.put("2014-01-31", 7L);
		map2.put("2014-02-01", 8L);
		map2.put("2014-02-02", 9L);
		result.put("graphDataNo2", map2);

		Map<String, Long> map3 = new HashMap<String, Long>();
		map3.put("2014-03-01", 5L);
		map3.put("2014-03-02", 4L);
		map3.put("2014-03-03", 5L);
		map3.put("2014-03-04", 4L);
		map3.put("2014-03-05", 5L);
		result.put("graphDataNo3", map3);
		
		return result;
	}
}
