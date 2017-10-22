package kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.DicAnalysis;;

public class KMeansClusteror {
	private int subTextCount;
	private List<String> subTextVector = new ArrayList<>();
	private Map<String, Integer> subTextIndexMap = new HashMap<>();

	public Map<String, List<String>> splitQueryText(File queryFile) {
		Map<String, List<String>> resultMap = new HashMap<>();

		BufferedReader reader;
		String query;
		try {
			reader = new BufferedReader(new FileReader(queryFile));
			while ((query = reader.readLine()) != null) {
				// DicLibrary.insert(DicLibrary.DEFAULT, "消岩汤剂");
				Result result = DicAnalysis.parse(query);
				System.out.println(result);
				String[] subTexts = result.toStringWithOutNature().split(",");
				List<String> subTextList = new ArrayList<>();
				for (int i = 0; i < subTexts.length; i++) {
					String subText = subTexts[i];
					if (!subText.equals("") && !subText.equals(" ")) {
						subTextList.add(subText);
						if (!subTextIndexMap.containsKey(subText)) {
							subTextIndexMap.put(subText, subTextCount++);
						}
					}
				}
				resultMap.put(query, subTextList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	public List<List<String>> cluster(String queryFilePath) {
		File queryFile = new File(queryFilePath);
		Map<String, List<String>> querySubTextsMap = splitQueryText(queryFile);

		Map<String, int[]> querySubTextFrequencyVectorMap = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : querySubTextsMap.entrySet()) {
			String queryText = entry.getKey();
			List<String> subTexts = entry.getValue();
			int[] queryTextFrequencyVector = new int[subTextCount];
			for (String subText : subTexts) {
				int index = subTextIndexMap.get(subText);
				queryTextFrequencyVector[index]++;
			}
			querySubTextFrequencyVectorMap.put(queryText, queryTextFrequencyVector);
		}
		// TODO 将词频向量转换为TF-IDF向量。
		// TODO 聚类。

		return null;
	}

	/**
	 * args[0]: 查询文件文件夹路径。
	 * (Mac："/Users/liuxl/Desktop/recommendation/phase1/input"；Windows："D:\Development\eclipse-workspace\recommendation.clustering\input"。)
	 */
	public static void main(String[] args) {

		String queryFileFolderPath = args[0];
		File folder = new File(queryFileFolderPath);
		File[] fileList = folder.listFiles();
		for (File queryFile : fileList) {
			String queryFilePath = queryFile.getAbsolutePath();
			if (queryFilePath.endsWith(".input")) {
				KMeansClusteror clusteror = new KMeansClusteror();
				List<List<String>> list = clusteror.cluster(queryFilePath);
				// TODO 准确率计算
				// TODO 文件输出
			}
		}

	}
}
