package kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.arrimpl.UserDefineRecognition;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;;

public class KMeans {
	public static List<List<String>> cluster(String queryFilePath){
		File queryFile = new File(queryFilePath);
		BufferedReader reader;
		String query;
		try{
			reader = new BufferedReader(new FileReader(queryFile));
			while((query = reader.readLine()) != null){
				DicLibrary.insert(DicLibrary.DEFAULT, "消岩汤剂");
				Result result = DicAnalysis.parse(query);
				System.out.println(result);
				String[] subTexts = result.toStringWithOutNature().split(",");
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args){
		String queryFilePath = "/Users/liuxl/Desktop/recommendation/phase1/input/task1.input";
		cluster(queryFilePath);
	}
}
