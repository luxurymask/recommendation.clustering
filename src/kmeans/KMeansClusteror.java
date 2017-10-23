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
	/**
	 * 查询词总个数
	 */
	private int queryCount;

	/**
	 * 分词总个数
	 */
	private int subTextCount;

	/**
	 * 分词-向量索引Map
	 */
	private Map<String, Integer> subTextIndexMap = new HashMap<>();

	/**
	 * 分词-包含该分词的query个数Map
	 */
	private Map<String, Integer> subTextCountMap = new HashMap<>();

	/**
	 * 分词。
	 * 
	 * @param queryFile
	 *            查询词文件。
	 * @return 查询-分词List Map
	 */
	public Map<String, List<String>> splitQueryText(File queryFile) {
		Map<String, List<String>> resultMap = new HashMap<>();

		BufferedReader reader;
		String query;
		try {
			reader = new BufferedReader(new FileReader(queryFile));
			while ((query = reader.readLine()) != null) {
				queryCount++;

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
						int count = subTextCountMap.getOrDefault(subText, 0);
						subTextCountMap.put(subText, count + 1);
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

		Map<String, double[]> querySubTextTFIDFVectorMap = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : querySubTextsMap.entrySet()) {
			String queryText = entry.getKey();
			List<String> subTexts = entry.getValue();
			int[] queryTextFrequencyVector = new int[subTextCount];
			double[] queryTextTFIDFVector = new double[subTextCount];
			for (String subText : subTexts) {
				int index = subTextIndexMap.get(subText);
				queryTextFrequencyVector[index]++;
			}

			int subTextCountInCurrentQuery = subTexts.size();
			for (String subText : subTexts) {
				int index = subTextIndexMap.get(subText);
				int countInCurrentQuery = queryTextFrequencyVector[index];
				int countOfQueryContainingSubText = subTextCountMap.get(subText);
				double IDF = Math.log((double) queryCount / countOfQueryContainingSubText);
				double TF = (double) countInCurrentQuery / subTextCountInCurrentQuery;
				double TFIDF = TF * IDF;
				queryTextTFIDFVector[index] = TFIDF;
			}
			querySubTextTFIDFVectorMap.put(queryText, queryTextTFIDFVector);
		}
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
