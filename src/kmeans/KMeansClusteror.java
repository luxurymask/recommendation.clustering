package kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Result;
import org.ansj.recognition.impl.StopRecognition;
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
	private Map<String, List<String>> splitQueryText(File queryFile) {
		Map<String, List<String>> resultMap = new HashMap<>();

		BufferedReader reader;
		String query;
		try {
			reader = new BufferedReader(new FileReader(queryFile));
			while ((query = reader.readLine()) != null) {
				queryCount++;
				// DicLibrary.insert(DicLibrary.DEFAULT, "消岩汤剂");
				Result result = DicAnalysis.parse(query);
				StopRecognition filter = new StopRecognition();
				List<String> filterWords = new ArrayList<>();
				filterWords.add("———");
				filterWords.add("的");
				filterWords.add("和");
				filterWords.add("中");
				filterWords.add("及");
				filterWords.add("对");
				filterWords.add("有");
				filterWords.add(" ");
				filterWords.add("‘");
				filterWords.add("’");
				filterWords.add("'");
				filterWords.add("/");
				filter.insertStopWords(filterWords);
				// System.out.println(result.recognition(filter).toStringWithOutNature());
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

	/**
	 * 获取TF-IDF向量
	 * 
	 * @param querySubTextsMap
	 *            查询词-分词List Map
	 * @return 查询词-tfidf向量Map
	 */
	private Map<String, double[]> getTFIDFVector(Map<String, List<String>> querySubTextsMap) {
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
		return querySubTextTFIDFVectorMap;
	}

	public List<List<String>> cluster(String queryFilePath, int k) {
		File queryFile = new File(queryFilePath);
		Map<String, List<String>> querySubTextsMap = splitQueryText(queryFile);

		Map<String, double[]> querySubTextTFIDFVectorMap = getTFIDFVector(querySubTextsMap);

		List<double[]> centersList = new ArrayList<>();
		List<Map<String, double[]>> clusterResultList = new ArrayList<>();

		// 初始化
		for (int i = 0; i < k; i++) {
			double[] centerVector = Tools.randomVector(subTextCount);
			centersList.add(centerVector);
			Map<String, double[]> clusterMap = new HashMap<>();
			clusterResultList.add(clusterMap);
		}

		// 初始分配
		for (Map.Entry<String, double[]> entry : querySubTextTFIDFVectorMap.entrySet()) {
			String queryText = entry.getKey();
			double[] tfidfVector = entry.getValue();
			double minDistance = 0;
			int minIndex = 0;
			for (int i = 0; i < k; i++) {
				double[] currentCenter = centersList.get(i);
				double currentDistance = Tools.cosSim(tfidfVector, currentCenter);
				if (minDistance < currentDistance) {
					minDistance = currentDistance;
					minIndex = i;
				}
			}
			clusterResultList.get(minIndex).put(queryText, tfidfVector);
		}

		// 聚类
		boolean flag = false;
		int count = 0;
		while (flag == false) {
			// 将所有向量分配到各个中心
			System.out.println("第 " + count++ + " 次循环...");

			// 计算新中心
			for (int i = 0; i < k; i++) {
				Map<String, double[]> currentClusterResultMap = clusterResultList.get(i);
				double[] newCenterVector = new double[subTextCount];
				int size = currentClusterResultMap.size();

				for (Map.Entry<String, double[]> entry : currentClusterResultMap.entrySet()) {
					String queryText = entry.getKey();
					double[] tfidfVector = entry.getValue();
					for (int j = 0; j < subTextCount; j++) {
						newCenterVector[j] += tfidfVector[j] / size;
					}
				}

				centersList.set(i, newCenterVector);
			}

			boolean flagInside = true;
			for (int i = 0; i < k; i++) {
				Iterator<Map.Entry<String, double[]>> iterator = clusterResultList.get(i).entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, double[]> entry = iterator.next();
					String queryText = entry.getKey();
					double[] tfidfVector = entry.getValue();
					double minDistance = 0;
					int minIndex = 0;
					for (int j = 0; j < k; j++) {
						double[] otherCenterVector = centersList.get(j);
						double distanceWithOtherCenter = Tools.cosSim(tfidfVector, otherCenterVector);
						if (minDistance < distanceWithOtherCenter) {
							minDistance = distanceWithOtherCenter;
							minIndex = j;
						}
					}

					if (minIndex != i) {
						iterator.remove();
						clusterResultList.get(minIndex).put(queryText, tfidfVector);
						flagInside = false;
					}
				}
			}

			if (flagInside == true)
				flag = true;
		}

		List<List<String>> result = new ArrayList<>();
		for (int i = 0; i < k; i++) {
			List<String> list = new ArrayList<>();
			for (Map.Entry<String, double[]> entry : clusterResultList.get(i).entrySet()) {
				String queryText = entry.getKey();
				list.add(queryText);
				System.out.println(queryText);
			}
			result.add(list);
			System.out.println(
					"----------------------------------------------------------------------------------------------------");
		}

		return result;
	}

	/**
	 * args[0]: 查询文件文件夹路径。
	 * (Mac："/Users/liuxl/Desktop/recommendation/phase1/input"；Windows：
	 * "D:\Development\eclipse-workspace\recommendation.clustering\input"。)
	 */
	public static void main(String[] args) {

		String queryFileFolderPath = args[0];
		String outputFolderPath = args[1];
		File folder = new File(queryFileFolderPath);
		File[] fileList = folder.listFiles();
		for (File queryFile : fileList) {
			String queryFilePath = queryFile.getAbsolutePath();
			if (queryFilePath.endsWith(".txt")) {
				KMeansClusteror clusteror = new KMeansClusteror();
				for(int i = 2;i < 11;i++){
					List<List<String>> resultList = clusteror.cluster(queryFilePath, i);
					String name = queryFile.getName();
					String resultName = name.split("\\.")[0] + i + ".txt";
					File resultFile = new File(outputFolderPath + "/" + resultName);
					if(!resultFile.exists()){
						try {
							resultFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					BufferedWriter writer;
					try{
						writer = new BufferedWriter(new FileWriter(resultFile));
						for(List<String> list : resultList){
							for(String s : list){
								writer.write(s);
								writer.newLine();
							}
							writer.write("-------------------------------");
							writer.newLine();
						}
						writer.flush();
						writer.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
				// TODO 准确率计算
				// TODO 文件输出
			}
			System.out.println(
					"############################################################################################");
		}

	}
}
