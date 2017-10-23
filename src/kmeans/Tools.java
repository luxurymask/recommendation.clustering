package kmeans;

import java.util.Random;

public class Tools {
	public static double cosSim(double[] v1, double[] v2) {
		int vectorLength;
		if((vectorLength = v1.length) != v2.length)
			try {
				throw new Exception("wrong input.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		double squareV1 = 0, squareV2 = 0, multi = 0;
		
		for(int i = 0;i < vectorLength;i++) {
			squareV1 += v1[i] * v1[i];
			squareV2 += v2[i] * v2[i];
			multi += v1[i] * v2[i];
		}
		
		squareV1 = Math.sqrt(squareV1);
		squareV2 = Math.sqrt(squareV2);
		
		double result = multi / (squareV1 * squareV2);
		return result;
	}
	
	public static double[] randomVector(int vectorLength) {
		Random random = new Random();
		double[] result = new double[vectorLength];
		for(int i = 0;i < vectorLength;i++) {
			result[i] = random.nextDouble();
		}
		return result;
	}
	
	public static boolean checkVectors(double[] v1, double[] v2) {
		int vectorLength;
		if((vectorLength = v1.length) != v2.length) return false;
		for(int i = 0;i < vectorLength;i++) {
			if(v1[i] != v2[i]) return false;
		}
		return true;
	}
}
